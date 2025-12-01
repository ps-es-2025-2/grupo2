import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-eventos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './eventos.component.html',
  styleUrls: ['./eventos.component.css']
})
export class EventosComponent implements OnInit {
  
  listaEventos: any[] = [];

  // Objeto igual ao que o Java espera
  novoEvento = {
    nome: '',
    descricao: '',
    local: '',
    dataInicio: '',
    dataFim: ''
  };

  apiUrl = 'http://localhost:8080/api/eventos'; 

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.carregarEventos();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }

  carregarEventos() {
    this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() }).subscribe({
      next: (dados) => this.listaEventos = dados,
      error: (erro) => console.error('Erro ao buscar eventos:', erro)
    });
  }

  salvar() {
    // O input datetime-local do HTML precisa ser convertido para ISO string se o Java reclamar,
    // mas geralmente o Spring aceita o formato padrão do input se estiver como String ou LocalDateTime.
    
    this.http.post(this.apiUrl, this.novoEvento, { headers: this.getHeaders() }).subscribe({
      next: (res) => {
        alert('Evento criado com sucesso!');
        this.carregarEventos();
        // Limpa o form
        this.novoEvento = { nome: '', descricao: '', local: '', dataInicio: '', dataFim: '' };
      },
      error: (erro) => {
        console.error('Erro ao salvar:', erro);
        alert('Erro ao salvar evento. Verifique as datas!');
      }
    });
  }
}