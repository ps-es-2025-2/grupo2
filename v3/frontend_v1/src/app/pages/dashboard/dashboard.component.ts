import { Component, ElementRef, HostListener } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  // CORREÇÃO: Aqui deve ser o HTML do DASHBOARD, não do Login
  templateUrl: './dashboard.component.html', 
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  mgmtOpen = false;
  
  constructor(private router: Router, private el: ElementRef) {}

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  toggleMgmt() {
    this.mgmtOpen = !this.mgmtOpen;
  }

  closeMgmt() {
    this.mgmtOpen = false;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const dropdown = this.el.nativeElement.querySelector('.dropdown');
    if (dropdown && !dropdown.contains(event.target as Node)) {
      this.mgmtOpen = false;
    }
  }
}