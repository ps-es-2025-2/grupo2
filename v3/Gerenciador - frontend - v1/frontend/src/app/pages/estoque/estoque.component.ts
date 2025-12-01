import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-estoque',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './estoque.component.html',
  styleUrls: ['./estoque.component.css']
})
export class EstoqueComponent implements OnInit {
  
  listaEstoque: any[] = [];
  apiUrl = 'http://localhost:8080/api/produtos'; 

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.carregarEstoque();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token);
  }

  carregarEstoque() {
    this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() }).subscribe({
      next: (dados) => {
        this.listaEstoque = dados;
        console.log('Estoque carregado:', dados);
      },
      error: (erro) => console.error('Erro ao buscar estoque:', erro)
    });
  }
}