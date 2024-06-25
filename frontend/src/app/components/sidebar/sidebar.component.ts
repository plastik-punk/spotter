import { Component, Input, OnInit, HostListener } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, NavigationEnd } from '@angular/router';
import { LayoutService } from '../../services/layout.service';
import { AreaListDto } from '../../dtos/layout';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  isSidebarVisible: boolean = false;
  @Input() showToggleButton: boolean = true;
  hasNoAreas: boolean = false;

  constructor(
    public authService: AuthService,
    private router: Router,
    private layoutService: LayoutService
  ) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.isSidebarVisible = false;
      }
    });
  }

  ngOnInit() {
    this.checkAreas();
  }

  // Method to check if there are no areas
  checkAreas(): void {
    this.layoutService.getAllAreas().subscribe((data: AreaListDto) => {
      this.hasNoAreas = data.areas.length === 0;
    });
  }

  // Method to toggle the sidebar's visibility
  toggleSidebar(): void {
    this.isSidebarVisible = !this.isSidebarVisible;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    let targetElement = event.target as HTMLElement;

    // Continue only if toggle button is not clicked
    if (!targetElement.closest('#sidebar-toggle-button') && this.isSidebarVisible) {
      // Check if click was outside the sidebar
      if (!targetElement.closest('#sidebar')) {
        this.isSidebarVisible = false;
      }
    }
  }
}
