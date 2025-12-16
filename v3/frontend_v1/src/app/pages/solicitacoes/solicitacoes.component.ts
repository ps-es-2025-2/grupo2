import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-solicitacoes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './solicitacoes.component.html',
  styleUrls: ['./solicitacoes.component.css']
})
export class SolicitacoesComponent implements OnInit {
  
  listaSolicitacoes: any[] = [];
  listaProdutos: any[] = [];
  filtroStatus: string = 'TODOS';

  novaSolicitacao = {
    produtoId: 0,
    quantidade: 0,
    observacao: ''
  };

  apiSolicitacoesUrl = '/api/solicitacoes';
  apiProdutosUrl = '/api/produtos';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.carregarProdutos();
    this.carregarSolicitacoes();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }

  carregarProdutos() {
    this.http.get<any[]>(this.apiProdutosUrl, { headers: this.getHeaders() }).subscribe({
      next: (dados) => this.listaProdutos = dados.filter(p => p.ativo),
      error: (erro) => console.error('Erro ao buscar produtos:', erro)
    });
  }

  carregarSolicitacoes() {
    let url = this.apiSolicitacoesUrl;
    
    if (this.filtroStatus !== 'TODOS') {
      url += `?status=${this.filtroStatus}`;
    }

    this.http.get<any[]>(url, { headers: this.getHeaders() }).subscribe({
      next: (dados) => this.listaSolicitacoes = dados,
      error: (erro) => console.error('Erro ao buscar solicitações:', erro)
    });
  }

  aplicarFiltro() {
    this.carregarSolicitacoes();
  }

  salvarSolicitacao() {
    if (this.novaSolicitacao.produtoId === 0 || this.novaSolicitacao.quantidade <= 0) {
      alert('Selecione um produto e informe uma quantidade válida!');
      return;
    }

    this.http.post(this.apiSolicitacoesUrl, this.novaSolicitacao, { headers: this.getHeaders() }).subscribe({
      next: (res) => {
        alert('Solicitação criada com sucesso!');
        this.carregarSolicitacoes();
        this.limparFormulario();
      },
      error: (erro) => {
        console.error('Erro ao criar solicitação:', erro);
        alert('Erro ao criar solicitação!');
      }
    });
  }

  aprovarSolicitacao(id: number) {
    if (!confirm('Tem certeza que deseja aprovar esta solicitação?')) return;

    this.http.patch(`${this.apiSolicitacoesUrl}/${id}/status?novoStatus=CONCLUIDA`, {}, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('Solicitação aprovada! O estoque foi atualizado.');
        this.carregarSolicitacoes();
      },
      error: (erro) => {
        console.error('Erro ao aprovar solicitação:', erro);
        alert('Erro ao aprovar solicitação!');
      }
    });
  }

  rejeitarSolicitacao(id: number) {
    if (!confirm('Tem certeza que deseja rejeitar esta solicitação?')) return;

    this.http.patch(`${this.apiSolicitacoesUrl}/${id}/status?novoStatus=REJEITADA`, {}, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('Solicitação rejeitada!');
        this.carregarSolicitacoes();
      },
      error: (erro) => {
        console.error('Erro ao rejeitar solicitação:', erro);
        alert('Erro ao rejeitar solicitação!');
      }
    });
  }

  limparFormulario() {
    this.novaSolicitacao = {
      produtoId: 0,
      quantidade: 0,
      observacao: ''
    };
  }

  getNomeProduto(produtoId: number): string {
    const produto = this.listaProdutos.find(p => p.id === produtoId);
    return produto ? produto.nome : 'Produto não encontrado';
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'PENDENTE':
        return 'badge-pendente';
      case 'CONCLUIDA':
        return 'badge-concluida';
      case 'REJEITADA':
        return 'badge-rejeitada';
      default:
        return 'badge-default';
    }
  }

  formatarData(dataString: string): string {
    if (!dataString) return '-';
    const data = new Date(dataString);
    return data.toLocaleString('pt-BR');
  }
}
