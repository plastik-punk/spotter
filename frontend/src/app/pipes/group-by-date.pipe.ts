import { Pipe, PipeTransform } from '@angular/core';
import moment from 'moment';

@Pipe({
  standalone: true,
  name: 'groupByDate'
})
export class GroupByDatePipe implements PipeTransform {
  transform(value: any[], ...args: unknown[]): any {
    const groupedResult = value.reduce((acc, currentValue) => {
      const date = moment(currentValue.date).format('dddd, MMMM Do YYYY');
      if (!acc[date]) {
        acc[date] = [];
      }
      acc[date].push(currentValue);
      return acc;
    }, {});

    return Object.keys(groupedResult).map(key => ({ key, value: groupedResult[key] }));
  }
}
