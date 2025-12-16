import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-venda-fichas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './venda-fichas.component.html',
  styleUrls: ['./venda-fichas.component.css']
})
export class VendaFichasComponent {

  valorInput: number | null = null;
  quantidadeInput: number = 1;
  clienteId: number | null = null;
  
  fichasGeradas: any[] = [];
  clientes: any[] = [];
  
  apiUrl = '/api/fichas';

  constructor(private http: HttpClient) {
    this.carregarClientes();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }

  carregarClientes() {
    this.http.get<any[]>('/api/clientes', { headers: this.getHeaders() }).subscribe({
      next: (dados) => this.clientes = dados,
      error: (erro) => console.error('Erro ao buscar clientes:', erro)
    });
  }

  // Método para gerar fichas no backend
  gerarFichas() {
    if (!this.valorInput || this.valorInput <= 0) {
      alert('Digite um valor válido!');
      return;
    }

    if (!this.clienteId) {
      alert('Selecione um cliente!');
      return;
    }

    const payload = {
      clienteId: this.clienteId,
      valor: this.valorInput,
      quantidade: this.quantidadeInput
    };

    this.http.post<any[]>(this.apiUrl, payload, { headers: this.getHeaders() }).subscribe({
      next: (fichas) => {
        this.fichasGeradas = [...fichas, ...this.fichasGeradas];
        alert(`✅ ${fichas.length} ficha(s) gerada(s) com sucesso!`);
        this.valorInput = null;
        this.quantidadeInput = 1;
      },
      error: (erro) => {
        console.error('Erro ao gerar fichas:', erro);
        alert('❌ Erro ao gerar fichas no servidor.');
      }
    });
  }

  // Função auxiliar para criar a "senha" aleatória da ficha
  private gerarHashAleatorio(): string {
    const caracteres = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
    let resultado = '';
    for (let i = 0; i < 6; i++) {
      resultado += caracteres.charAt(Math.floor(Math.random() * caracteres.length));
    }
    return resultado;
  }

  imprimir() {
    window.print();
  }

  limparTela() {
    this.fichasGeradas = [];
  }
}