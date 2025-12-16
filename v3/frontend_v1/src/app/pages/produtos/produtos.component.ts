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
    quantidadeInicial: 0
  };

  // Para edição
  produtoEditando: any = null;
  produtoEdit: any = { nome: '', preco: 0, categoria: '' };

  apiUrl = '/api/produtos'; 

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
    // Extrai quantidadeInicial antes de enviar
    const { quantidadeInicial, ...produtoData } = this.novoProduto;
    
    this.http.post(this.apiUrl, produtoData, { headers: this.getHeaders() }).subscribe({
      next: (res: any) => {
        alert('Produto cadastrado com sucesso!');
        
        // Criar estoque inicial para o produto com a quantidade informada
        const produtoId = res.id;
        const quantidade = quantidadeInicial || 0;
        const estoqueUrl = `/api/estoque/produto/${produtoId}/incrementar?quantidade=${quantidade}&origem=ENTRADA_MANUAL`;
        
        this.http.post(estoqueUrl, {}, { headers: this.getHeaders() }).subscribe({
          next: () => {
            console.log(`Estoque inicial criado com quantidade: ${quantidade}`);
            this.carregarProdutos();
          },
          error: (erro) => {
            console.error('Erro ao criar estoque:', erro);
            alert('Produto criado, mas houve erro ao criar o estoque. Ajuste manualmente.');
            this.carregarProdutos();
          }
        });
        
        // Limpa o form
        this.novoProduto = { nome: '', preco: 0, categoria: 'ALCOOLICA', quantidadeInicial: 0 }; 
      },
      error: (erro) => {
        console.error('Erro:', erro);
        alert('Erro ao salvar produto.');
      }
    });
  }

  excluirProduto(id: number) {
    if (!confirm('Tem certeza que deseja excluir este produto?')) {
      return;
    }

    this.http.delete(`${this.apiUrl}/${id}`, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Produto excluído com sucesso!');
        this.carregarProdutos();
      },
      error: (erro) => {
        console.error('Erro ao excluir:', erro);
        alert('❌ Erro ao excluir produto. Pode haver vendas ou estoque vinculados.');
      }
    });
  }

  abrirEdicao(produto: any) {
    this.produtoEditando = produto;
    this.produtoEdit = {
      nome: produto.nome,
      preco: produto.preco,
      categoria: produto.categoria
    };
  }

  cancelarEdicao() {
    this.produtoEditando = null;
  }

  salvarEdicao() {
    if (!this.produtoEdit.nome || !this.produtoEdit.preco) {
      alert('Preencha todos os campos!');
      return;
    }

    this.http.put(`${this.apiUrl}/${this.produtoEditando.id}`, this.produtoEdit, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Produto atualizado com sucesso!');
        this.produtoEditando = null;
        this.carregarProdutos();
      },
      error: (erro) => {
        console.error('Erro ao editar:', erro);
        alert('❌ Erro ao atualizar produto.');
      }
    });
  }
}