import {Component, OnInit, Renderer2, OnDestroy, Inject} from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { DOCUMENT } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'SE PR Group Phase';

  constructor(private router: Router, private renderer: Renderer2, @Inject(DOCUMENT) private document: Document) {}

  ngOnInit() {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.updateBodyClass(event.urlAfterRedirects);
    });
  }

  updateBodyClass(url: string) {
    if (url.includes('/login') || url.includes('/registration')) {
      this.renderer.setStyle(this.document.body, 'background-image', 'url("assets/images/loginBackground.png")');
    } else {
      this.renderer.setStyle(this.document.body, 'background-image', 'url("assets/images/background.png")');
    }
  }


  ngOnDestroy() {
  }
}
