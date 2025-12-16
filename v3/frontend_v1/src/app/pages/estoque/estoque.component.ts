import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
<<<<<<< HEAD
import { RouterLink, Router } from '@angular/router';
=======
import { RouterLink } from '@angular/router';
>>>>>>> 7a918497ebee151fabee2fa8e53dade07b3544a5
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-estoque',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './estoque.component.html',
  styleUrls: ['./estoque.component.css']
})
export class EstoqueComponent implements OnInit {
  
  listaEstoque: any[] = [];
  apiUrl = '/api/estoque'; 
  
  // Para modal de entrada/saída
  modalAberto: boolean = false;
  modalTipo: 'entrada' | 'saida' = 'entrada';
  produtoSelecionado: any = null;
  quantidadeModal: number = 0;
  origemModal: string = 'ENTRADA_MANUAL';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.carregarEstoque();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }

  carregarEstoque() {
    this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() }).subscribe({
      next: (dados) => {
        this.listaEstoque = dados;
        console.log('Estoque carregado:', dados);
        this.verificarEstoqueBaixo(dados);
      },
      error: (erro) => {
        console.error('Erro ao buscar estoque:', erro);
        alert('Erro ao carregar estoque. Verifique se você tem produtos cadastrados.');
      }
    });
  }

<<<<<<< HEAD
  verificarEstoqueBaixo(dados: any[]) {
    const produtosBaixos = dados.filter(item => {
      const qtdMinima = item.quantidadeMinima || 10;
      return item.quantidadeAtual <= qtdMinima && item.quantidadeAtual > 0;
    });

    const produtosZerados = dados.filter(item => item.quantidadeAtual === 0);

    if (produtosZerados.length > 0) {
      const nomes = produtosZerados.map(p => p.produto.nome).join(', ');
      console.warn('⚠️ PRODUTOS ZERADOS:', nomes);
    }

    if (produtosBaixos.length > 0) {
      const nomes = produtosBaixos.map(p => p.produto.nome).join(', ');
      console.warn('⚠️ ESTOQUE BAIXO:', nomes);
    }
  }

  getProdutosComEstoqueBaixo() {
    return this.listaEstoque.filter(item => {
      const qtdMinima = item.quantidadeMinima || 10;
      return item.quantidadeAtual <= qtdMinima && item.quantidadeAtual > 0;
    });
  }

  getProdutosZerados() {
    return this.listaEstoque.filter(item => item.quantidadeAtual === 0);
  }

  irParaSolicitacoes(produtoId: number) {
    // Navega para a página de solicitações com o produto pré-selecionado
    this.router.navigate(['/solicitacoes'], { queryParams: { produtoId } });
  }

=======
>>>>>>> 7a918497ebee151fabee2fa8e53dade07b3544a5
  abrirModal(tipo: 'entrada' | 'saida', item: any) {
    this.modalTipo = tipo;
    this.produtoSelecionado = item;
    this.quantidadeModal = 0;
    this.origemModal = tipo === 'entrada' ? 'ENTRADA_MANUAL' : 'SAIDA_MANUAL';
    this.modalAberto = true;
  }

  fecharModal() {
    this.modalAberto = false;
    this.produtoSelecionado = null;
    this.quantidadeModal = 0;
  }

  confirmarMovimentacao() {
    if (!this.quantidadeModal || this.quantidadeModal <= 0) {
      alert('Digite uma quantidade válida!');
      return;
    }

    if (this.modalTipo === 'entrada') {
      this.incrementarEstoque(this.produtoSelecionado.produto.id, this.quantidadeModal);
    } else {
      this.decrementarEstoque(this.produtoSelecionado.produto.id, this.quantidadeModal);
    }
  }

  incrementarEstoque(produtoId: number, quantidade: number) {
    const url = `${this.apiUrl}/produto/${produtoId}/incrementar?quantidade=${quantidade}&origem=${this.origemModal}`;
    
    this.http.post(url, {}, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Estoque incrementado com sucesso!');
        this.carregarEstoque();
        this.fecharModal();
      },
      error: (erro) => {
        console.error('Erro ao incrementar:', erro);
        alert('❌ Erro ao incrementar estoque: ' + (erro.error || 'Erro desconhecido'));
      }
    });
  }

  decrementarEstoque(produtoId: number, quantidade: number) {
    const url = `${this.apiUrl}/produto/${produtoId}/decrementar?quantidade=${quantidade}`;
    
    this.http.post(url, {}, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Estoque decrementado com sucesso!');
        this.carregarEstoque();
        this.fecharModal();
      },
      error: (erro) => {
        console.error('Erro ao decrementar:', erro);
        alert('❌ Erro ao decrementar estoque: ' + (erro.error || 'Erro desconhecido'));
      }
    });
  }

  getStatusClass(quantidade: number, quantidadeMinima: number = 0): string {
    if (quantidade === 0) return 'status-danger';
    if (quantidadeMinima > 0 && quantidade <= quantidadeMinima) return 'status-warning';
    if (quantidade <= 10) return 'status-warning';
    return 'status-ok';
  }

  getStatusText(quantidade: number, quantidadeMinima: number = 0): string {
    if (quantidade === 0) return '🚫 ZERADO';
    if (quantidadeMinima > 0 && quantidade <= quantidadeMinima) return '⚠️ ABAIXO DO MÍNIMO';
    if (quantidade <= 10) return '⚠️ BAIXO';
    return '✅ OK';
  }
}