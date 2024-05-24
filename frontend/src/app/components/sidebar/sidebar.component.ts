import { Component, Input, OnInit, HostListener } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  isSidebarVisible: boolean = false;
  @Input() showToggleButton: boolean = true;

  constructor(public authService: AuthService, private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.isSidebarVisible = false;
      }
    });
  }

  ngOnInit() {
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
