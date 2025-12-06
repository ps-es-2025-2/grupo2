import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-validacao',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './validacao.component.html',
  styleUrls: ['./validacao.component.css']
})
export class ValidacaoComponent {
  
  codigoInput: string = '';
  fichaEncontrada: any = null;
  mensagemErro: string = '';

  apiUrl = 'http://localhost:8080/api/fichas'; 

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token);
  }

  consultarFicha() {
    if (!this.codigoInput) return;

    this.mensagemErro = '';
    this.fichaEncontrada = null;

    // Busca a ficha pelo código (ex: /api/fichas/abc-123-uuid)
    this.http.get<any>(`${this.apiUrl}/${this.codigoInput}`, { headers: this.getHeaders() })
      .subscribe({
        next: (dados) => {
          this.fichaEncontrada = dados;
        },
        error: (erro) => {
          console.error(erro);
          this.mensagemErro = 'Ficha não encontrada ou código inválido.';
        }
      });
  }

  // Função para simular a entrega do produto (marcar como usada)
  entregarProduto() {
    if(!this.fichaEncontrada) return;

    // Supomos que exista uma rota para consumir (se não tiver no back, vai dar erro, mas o front tá pronto)
    const urlConsumir = `${this.apiUrl}/${this.fichaEncontrada.codigo}/consumir`;

    this.http.post(urlConsumir, {}, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('PRODUTO ENTREGUE! ✅');
        this.fichaEncontrada.status = 'UTILIZADA'; // Atualiza na tela
      },
      error: () => {
        alert('Erro ao baixar ficha. O Backend precisa dessa função.');
      }
    });
  }
}