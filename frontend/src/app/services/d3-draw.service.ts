import { Injectable, ElementRef } from '@angular/core';
import * as d3 from 'd3';
import {AreaLayoutDto, CoordinateDto, PlaceVisualDto} from "../dtos/layout";

@Injectable({
  providedIn: 'root'
})
export class D3DrawService {
  private layoutWidth: number = 1600;
  private layoutHeight: number = 900;

  createSeatingPlan(container: ElementRef) {
    const element = container.nativeElement;
    const width = element.clientWidth;
    const aspectRatio = this.layoutWidth / this.layoutHeight;
    const height = width / aspectRatio;

    d3.select(container.nativeElement)
      .append('svg')
      .attr('viewBox', `0 0 ${this.layoutWidth} ${this.layoutHeight}`)
      .attr('preserveAspectRatio', 'xMidYMid meet')
      .attr('width', width)
      .attr('height', height);
  }

  adjustSvgSize(container: ElementRef) {
    const element = container.nativeElement;
    const width = element.clientWidth;
    const aspectRatio = this.layoutWidth / this.layoutHeight;
    const height = width / aspectRatio;

    d3.select(element).select('svg')
      .attr('width', width)
      .attr('height', height);
  }

  updateSeatingPlan(container: ElementRef, areaLayout: AreaLayoutDto, selectedPlaces: { placeId: number, numberOfSeats: number }[], onPlaceClick: (placeId: number, numberOfSeats: number) => void, employee: boolean) {
    const element = container.nativeElement;
    const svg = d3.select(element).select('svg');
    svg.selectAll('*').remove();

    const gridSize = 100;
    const group = svg.selectAll('g')
      .data(areaLayout.placeVisuals)
      .enter()
      .append('g')
      .on('click', (event, d) => {
        event.stopPropagation();
        if (!d.reservation && d.status || employee) {
          onPlaceClick(d.placeNumber, d.numberOfSeats);
        }
      })
      .on('mouseover', (event, d) => this.showTooltip(event, d))
      .on('mouseout', () => this.hideTooltip());

    group.each((d, i, nodes) => {
      const g = d3.select(nodes[i]);
      const polygonPoints = this.generatePolygonPoints(d.coordinates, gridSize);
      const uniqueFilteredPoints = this.filterDuplicatePoints(polygonPoints);
      const orderedPoints = this.orderPoints(uniqueFilteredPoints);
      const pathData = this.polygonWithRoundedCorners(orderedPoints, 10);

      g.append('path')
        .attr('d', pathData)
        .attr('fill', () => this.getPlaceColor(d, selectedPlaces));

      const textPosition = this.getTextPosition(d.coordinates, gridSize);
      g.append('text')
        .attr('x', textPosition.x)
        .attr('y', textPosition.y)
        .attr('dy', '.35em')
        .attr('text-anchor', 'middle')
        .attr('fill', 'white')
        .attr('font-size', '35px')
        .attr('font-family', 'Arial')
        .text(d.numberOfSeats.toString());
    });

    d3.select('body').append('div')
      .attr('id', 'tooltip')
      .style('position', 'absolute')
      .style('background', '#fff')
      .style('border', '1px solid #ccc')
      .style('padding', '10px')
      .style('display', 'none');
  }

  private generatePolygonPoints(coordinates: CoordinateDto[], gridSize: number): number[][] {
    return coordinates
      .map(coord => {
        const x = coord.x * gridSize;
        const y = coord.y * gridSize;
        return [[x, y], [x + gridSize, y], [x + gridSize, y + gridSize], [x, y + gridSize]];
      })
      .reduce((acc, val) => acc.concat(val), []);
  }

  private filterDuplicatePoints(polygonPoints: number[][]): number[][] {
    const pointCounts: Record<string, number> = {};
    polygonPoints.forEach(point => {
      const key = point.join(',');
      pointCounts[key] = (pointCounts[key] || 0) + 1;
    });
    return Object.entries(pointCounts)
      .filter(([_, count]) => count % 2 === 1)
      .map(([key]) => key.split(',').map(Number));
  }

  private orderPoints(points: number[][]): number[][] {
    if (points.length <= 1) return points;

    const orderedPoints = [points.shift()!];
    while (points.length > 0) {
      const lastPoint = orderedPoints[orderedPoints.length - 1];
      const nextIndex = points.findIndex(point => point[0] === lastPoint[0] || point[1] === lastPoint[1]);
      orderedPoints.push(points.splice(nextIndex >= 0 ? nextIndex : 0, 1)[0]);
    }

    return orderedPoints;
  }

  private polygonWithRoundedCorners(points: number[][], r: number): string {
    let d = "";
    points.forEach((point, i) => {
      const previous = points[i - 1 >= 0 ? i - 1 : points.length - 1];
      const next = points[i + 1 < points.length ? i + 1 : 0];
      const c = { x: point[0], y: point[1] };
      const l1 = { x: previous[0], y: previous[1] };
      const l2 = { x: next[0], y: next[1] };
      const a1 = this.getAngle(c, l1);
      const a2 = this.getAngle(c, l2);

      const x1 = (c.x + r * Math.cos(a1)).toFixed(3);
      const y1 = (c.y + r * Math.sin(a1)).toFixed(3);
      const x2 = (c.x + r * Math.cos(a2)).toFixed(3);
      const y2 = (c.y + r * Math.sin(a2)).toFixed(3);
      d += `L${x1},${y1} Q${c.x},${c.y} ${x2},${y2}`;
    });
    return `M${d.slice(1)}Z`;
  }

  private getAngle(c: { x: number, y: number }, l: { x: number, y: number }): number {
    const deltaX = l.x - c.x;
    const deltaY = l.y - c.y;
    return Math.atan2(deltaY, deltaX);
  }

  private getPlaceColor(place: PlaceVisualDto, selectedPlaces: { placeId: number, numberOfSeats: number }[]): string {
    if (selectedPlaces.some(p => p.placeId === place.placeNumber)) {
      return '#377eb8';
    }
    if (!place.status) {
      return '#767676FF';
    }
    return !place.reservation ? '#4daf4a' : '#e41a1c';
  }

  private showTooltip(event: MouseEvent, place: PlaceVisualDto) {
    const tooltip = d3.select('#tooltip');
    tooltip.style('display', 'block')
      .style('left', `${event.pageX + 10}px`)
      .style('top', `${event.pageY + 10}px`)
      .html(`ID: ${place.placeNumber}<br>Seats: ${place.numberOfSeats}<br>Status: ${place.status ? (!place.reservation ? 'Free' : 'Booked') : 'Unavailable'}`);
  }

  private hideTooltip() {
    d3.select('#tooltip').style('display', 'none');
  }

  private getTextPosition(coordinates: CoordinateDto[], gridSize: number): { x: number, y: number } {
    for (const coord of coordinates) {
      const x = coord.x * gridSize + gridSize / 2;
      const y = coord.y * gridSize + gridSize / 2;
      const covered = coordinates.some(c => c.x === coord.x && c.y === coord.y + 1);
      if (covered) {
        return { x, y: y + gridSize / 2 };
      }
    }

    let x = 0, y = 0;
    coordinates.forEach(coord => {
      x += coord.x * gridSize + gridSize / 2;
      y += coord.y * gridSize + gridSize / 2;
    });
    x /= coordinates.length;
    y /= coordinates.length;

    return { x, y };
  }
}
