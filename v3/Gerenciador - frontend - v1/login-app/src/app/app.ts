import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  username = '';
  password = '';
  loginResponse = '';

  constructor(private http: HttpClient) { }

  login() {
    const payload = {
      email: this.username, // aqui você pode até renomear a variável para `email` se quiser
      senha: this.password
    };

    this.http.post<any>('http://127.0.0.1:8080/api/auth/login', payload).subscribe({
      next: (res) => {
        console.log('Resposta da API:', res);
        this.loginResponse = 'Login realizado com sucesso!';
      },
      error: (err) => {
        console.error('Erro no login:', err);
        this.loginResponse = 'Falha no login. Verifique usuário e senha.';
      }
    });
  }

}
