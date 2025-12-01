import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-produtos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './produtos.component.html',
  styleUrls: ['./produtos.component.css']
})
export class ProdutosComponent implements OnInit {
  
  listaProdutos: any[] = [];

  novoProduto = {
    nome: '',
    preco: 0,
    categoria: 'ALCOOLICA',
    quantidade: 100 // <--- NOVO: Já começa sugerindo 100 unidades
  };

  apiUrl = 'http://localhost:8080/api/produtos'; 

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.carregarProdutos();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }

  carregarProdutos() {
    this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() }).subscribe({
      next: (dados) => this.listaProdutos = dados,
      error: (erro) => console.error('Erro ao buscar:', erro)
    });
  }

  salvar() {
    this.http.post(this.apiUrl, this.novoProduto, { headers: this.getHeaders() }).subscribe({
      next: (res) => {
        alert('Produto cadastrado com estoque!');
        this.carregarProdutos();
        // Limpa o form
        this.novoProduto = { nome: '', preco: 0, categoria: 'ALCOOLICA', quantidade: 100 }; 
      },
      error: (erro) => {
        console.error('Erro:', erro);
        alert('Erro ao salvar produto.');
      }
    });
  }
}