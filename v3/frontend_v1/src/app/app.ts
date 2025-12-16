import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgIf], 
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  isLoginRoute = true;

  constructor(private router: Router) {
    this.updateRouteState(this.router.url);

    // Track route changes to toggle header text between login and internal pages.
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateRouteState(event.urlAfterRedirects);
      }
    });
  }

  private updateRouteState(currentUrl: string): void {
    // Consider "/login" (or root redirect) as the initial screen.
    this.isLoginRoute = currentUrl === '/login' || currentUrl === '/';
  }
}