import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-consultar-fichas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './consultar-fichas.component.html',
  styleUrls: ['./consultar-fichas.component.css']
})
export class ConsultarFichasComponent implements OnInit {

  listaFichas: any[] = [];
  filtro: string = '';
  apiUrl = '/api/fichas';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.carregarDados();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }

  carregarDados() {
    this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() }).subscribe({
      next: (dados) => this.listaFichas = dados,
      error: (erro) => console.error('Erro ao buscar fichas:', erro)
    });
  }

  darBaixa(ficha: any) {
    if(!confirm(`Confirma dar baixa na ficha ${ficha.codigo}?`)) return;

    this.http.post(`${this.apiUrl}/${ficha.codigo}/validar`, {}, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Ficha baixada com sucesso!');
        this.carregarDados();
      },
      error: (erro) => {
        console.error('Erro ao dar baixa:', erro);
        alert('❌ Erro ao dar baixa na ficha.');
      }
    });
  }

  reativar(ficha: any) {
    // Backend não suporta reativar - funcionalidade removida ou implementar endpoint específico
    alert('⚠️ Funcionalidade de reativar não disponível no servidor.');
  }

  excluir(codigo: string) {
    if(!confirm('Tem certeza que deseja apagar este registro?')) return;
    
    this.http.delete(`${this.apiUrl}/${codigo}`, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Ficha excluída com sucesso!');
        this.carregarDados();
      },
      error: (erro) => {
        console.error('Erro ao excluir:', erro);
        alert('❌ Erro ao excluir ficha.');
      }
    });
  }

  // Filtro simples de busca visual
  get fichasFiltradas() {
    if (!this.filtro) return this.listaFichas;
    return this.listaFichas.filter(f => 
      f.codigo.toLowerCase().includes(this.filtro.toLowerCase()) ||
      f.status.toLowerCase().includes(this.filtro.toLowerCase())
    );
  }
  
  calcularTotalVendido() {
    return this.listaFichas.reduce((acc, f) => acc + f.valor, 0);
  }
}