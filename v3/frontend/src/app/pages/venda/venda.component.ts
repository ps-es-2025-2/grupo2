import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-venda',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './venda.component.html',
  styleUrls: ['./venda.component.css']
})
export class VendaComponent implements OnInit {
  
  listaProdutos: any[] = [];
  carrinho: any[] = [];
  itemAtual = { produtoId: null, quantidade: 1 };
  
  ultimasFichasGeradas: any[] = [];
  vendaConcluida: boolean = false;

  operadorEmail = 'admin@test.com';
  clienteId = null;
  apiUrl = 'http://localhost:8080/api'; 

  constructor(private http: HttpClient) {}

  ngOnInit() { this.carregarProdutos(); }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders().set('Authorization', 'Bearer ' + token);
  }

  carregarProdutos() {
    this.http.get<any[]>(`${this.apiUrl}/produtos`, { headers: this.getHeaders() })
      .subscribe({
        next: (d) => this.listaProdutos = d,
        error: (e) => console.error(e)
      });
  }

  adicionarProduto(produto: any) {
    const itemExistente = this.carrinho.find(i => i.produtoId === produto.id);
    if (itemExistente) {
      itemExistente.quantidade++;
      itemExistente.subtotal = itemExistente.preco * itemExistente.quantidade;
    } else {
      this.carrinho.push({
        produtoId: produto.id,
        nome: produto.nome,
        preco: produto.preco,
        quantidade: 1,
        subtotal: produto.preco
      });
    }
  }

  aumentarQtd(item: any) {
    item.quantidade++;
    item.subtotal = item.preco * item.quantidade;
  }

  diminuirQtd(item: any) {
    if (item.quantidade > 1) {
      item.quantidade--;
      item.subtotal = item.preco * item.quantidade;
    }
  }

  adicionarAoCarrinho() {
    if (!this.itemAtual.produtoId || this.itemAtual.quantidade <= 0) return;
    const prod = this.listaProdutos.find(p => p.id == this.itemAtual.produtoId);
    if (prod) {
      this.carrinho.push({
        produtoId: prod.id,
        nome: prod.nome,
        preco: prod.preco,
        precoUnitario: prod.preco,
        quantidade: this.itemAtual.quantidade,
        subtotal: prod.preco * this.itemAtual.quantidade
      });
      this.itemAtual = { produtoId: null, quantidade: 1 };
    }
  }

  removerDoCarrinho(i: number) { this.carrinho.splice(i, 1); }
  
  calcularTotal() { 
    return this.carrinho.reduce((acc, i) => acc + i.subtotal, 0).toFixed(2); 
  }

  finalizarVenda() {
    if (this.carrinho.length === 0) return;

    const payload = {
      clienteId: this.clienteId,
      operadorEmail: this.operadorEmail,
      itens: this.carrinho.map(i => ({ produtoId: i.produtoId, quantidade: i.quantidade }))
    };

    this.http.post<any>(`${this.apiUrl}/vendas`, payload, { headers: this.getHeaders() })
      .subscribe({
        next: (vendaRetornada) => {
          this.ultimasFichasGeradas = this.carrinho.map(item => ({
            produto: item.nome,
            codigo: item.nome.substring(0,3).toUpperCase() + '-' + Math.floor(Math.random() * 100000)
          }));

          this.vendaConcluida = true;
          this.carrinho = [];
        },
        error: (erro) => {
          alert('Erro na venda! Verifique caixa ou email.');
          console.error(erro);
        }
      });
  }

  novaVenda() {
    this.vendaConcluida = false;
    this.ultimasFichasGeradas = [];
  }
}