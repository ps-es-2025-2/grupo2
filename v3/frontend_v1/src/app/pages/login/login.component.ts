import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  // AQUI ESTÁ A MUDANÇA IMPORTANTE:
  // Estamos apontando para os nomes novos que você renomeou
  templateUrl: './login.component.html', 
  styleUrls: ['./login.component.css']
})
export class LoginComponent { 
  username = '';
  password = '';
  loginResponse = '';
  loading = false;

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    const payload = { email: this.username, senha: this.password };
    const urlBackend = 'http://localhost:8080/api/auth/login';

    this.loading = true;
    this.loginResponse = '';

    this.http.post<any>(urlBackend, payload).subscribe({
      next: (res) => {
        this.loading = false;
        localStorage.setItem('token', res.token);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        this.loginResponse = 'Erro ao logar: ' + (err.error?.message || 'Falha');
      }
    });
  }
}