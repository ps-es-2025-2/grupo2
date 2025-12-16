import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

// Interfaces que representam os DTOs do backend
interface Produto {
  id: number;
  nome: string;
  preco: number;
  categoria: 'ALCOOLICA' | 'NAO_ALCOOLICA' | 'COMESTIVEL';
  ativo: boolean;
}

interface ItemCarrinho {
  produtoId: number;
  nome: string;
  preco: number;
  quantidade: number;
  subtotal: number;
}

interface VendaRequest {
  clienteId?: number;
  operadorEmail: string;
  itens: { produtoId: number; quantidade: number }[];
  clienteNovo?: {
    nome: string;
    telefone?: string;
    email?: string;
  };
}

interface FichaDigital {
  id: number;
  codigo: string;
  status: string;
  cliente: {
    nome: string;
  };
}

interface VendaResponse {
  id: number;
  valorTotal: number;
  dataHora: string;
  ficha: FichaDigital;
}

@Component({
  selector: 'app-venda',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './venda.component.html',
  styleUrls: ['./venda.component.css']
})
export class VendaComponent implements OnInit {
  listaProdutos: Produto[] = [];
  carrinho: ItemCarrinho[] = [];

  fichaGerada: FichaDigital | null = null;
  vendaConcluida: boolean = false;
  carregando: boolean = false;

  operadorEmail: string = '';

  // Cliente novo
  clienteNovo = {
    nome: '',
    telefone: '',
    email: ''
  };

  apiUrl = '/api';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.carregarOperador();
    this.carregarProdutos();
  }

  carregarOperador() {
    this.operadorEmail = localStorage.getItem('userEmail') || 'admin@local';
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders().set('Authorization', 'Bearer ' + token).set('Content-Type', 'application/json');
  }

  private async openCaixaBackendFromVenda(fundoInicial: number): Promise<number> {
    const operadorEmail = this.operadorEmail || localStorage.getItem('userEmail') || 'admin@local';
    const operadorId = localStorage.getItem('operadorId');

    // Try multiple endpoint names and payload shapes that backend might expect.
    const endpoints = ['/api/caixas', '/api/caixa/abrir', '/api/caixa', '/api/caixas/abrir'];

    const payloadVariants: any[] = [
      { operadorEmail, fundoInicial },
      { operadorEmail, saldoInicial: fundoInicial },
      { operadorEmail, saldo_inicial: fundoInicial },
      { operadorEmail, fundoTroco: fundoInicial },
      { operadorEmail, saldo: fundoInicial },
      { operadorId, saldoInicial: fundoInicial },
      { operadorId, operadorEmail, saldoInicial: fundoInicial }
    ];

    for (const ep of endpoints) {
      for (const payload of payloadVariants) {
        try {
          console.debug('Tentando abrir caixa', ep, payload);
          const res: any = await this.http.post(ep, payload, { headers: this.getHeaders() }).toPromise();
          console.debug('Resposta abrir caixa', ep, res);
          const id = res?.id || res?.caixaId || res?.data?.id || null;
          if (id) {
            localStorage.setItem('caixaId', String(id));
            return id;
          }
          if (res && typeof res === 'object') {
            const possible = Object.values(res).find(v => typeof v === 'number');
            if (possible) { localStorage.setItem('caixaId', String(possible)); return Number(possible); }
          }
        } catch (err: any) {
          // Log full error details to help debugging (server likely returns validation message)
          console.debug('Tentativa abrir caixa falhou para', ep, 'payload', payload, 'error', err?.status, err?.error ?? err?.message ?? err);
        }
      }
    }

    throw new Error('Abertura de caixa falhou em todos os endpoints/payloads');
  }

  carregarProdutos() {
    this.http.get<Produto[]>(`${this.apiUrl}/produtos`, { headers: this.getHeaders() })
      .subscribe({
        next: (produtos) => {
          this.listaProdutos = produtos.filter(p => p.ativo);
        },
        error: (erro) => {
          console.error('Erro ao carregar produtos:', erro);
          alert('Erro ao carregar produtos');
        }
      });
  }

  adicionarProduto(produto: Produto) {
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

  aumentarQtd(item: ItemCarrinho) {
    item.quantidade++;
    item.subtotal = item.preco * item.quantidade;
  }

  diminuirQtd(item: ItemCarrinho) {
    if (item.quantidade > 1) {
      item.quantidade--;
      item.subtotal = item.preco * item.quantidade;
    }
  }

  removerDoCarrinho(index: number) {
    this.carrinho.splice(index, 1);
  }

  calcularTotal(): string {
    return this.carrinho.reduce((acc, i) => acc + i.subtotal, 0).toFixed(2);
  }

  finalizarVenda() {
    if (this.carrinho.length === 0) {
      alert('Adicione produtos ao carrinho');
      return;
    }

    if (!this.clienteNovo.nome.trim()) {
      alert('Informe o nome do cliente');
      return;
    }

    this.carregando = true;

    const payload: VendaRequest = {
      operadorEmail: this.operadorEmail,
      itens: this.carrinho.map(i => ({ produtoId: i.produtoId, quantidade: i.quantidade })),
      clienteNovo: {
        nome: this.clienteNovo.nome,
        telefone: this.clienteNovo.telefone || undefined,
        email: this.clienteNovo.email || undefined
      }
    };

    // Inclui caixaId (se disponível) no payload para backend
    const caixaIdStored = localStorage.getItem('caixaId');
    if (caixaIdStored) {
      (payload as any).caixaId = Number(caixaIdStored);
    }

    this.http.post<VendaResponse>(`${this.apiUrl}/vendas`, payload, { headers: this.getHeaders() })
      .subscribe({
        next: (venda) => {
          this.fichaGerada = venda.ficha;
          this.vendaConcluida = true;
          this.carrinho = [];
          this.carregando = false;
          alert('✅ Venda realizada com sucesso!');
        },
        error: (erro) => {
          console.error('Erro ao finalizar venda:', erro);
          this.carregando = false;

          let mensagemErro = 'Erro ao finalizar venda.';

          if (erro.status === 404) {
            mensagemErro = '❌ Operador não encontrado. Verifique o email.';
          } else if (erro.status === 400 || erro.status === 500) {
            // Captura mensagens específicas do backend de forma robusta
            let serverMsg = '';
            if (erro.error) {
              if (typeof erro.error === 'string') {
                serverMsg = erro.error;
              } else if (erro.error.message) {
                serverMsg = erro.error.message;
              } else if ((erro.error as any).error) {
                const inner = (erro.error as any).error;
                serverMsg = typeof inner === 'string' ? inner : JSON.stringify(inner);
              } else {
                serverMsg = JSON.stringify(erro.error);
              }
              mensagemErro = '❌ ' + serverMsg;
            }

            // Tratamento específico para caixa não aberto (detecção mais ampla)
            const lower = (serverMsg || '').toLowerCase();
            if (lower.includes('caixa') && (lower.includes('abr') || lower.includes('aberto') || lower.includes('não') || lower.includes('nao') || lower.includes('sem'))) {
              mensagemErro += '\n\n💡 Dica: Vá até "Controle de Caixa" e abra um caixa antes de realizar vendas.';
              const abrirAgora = confirm('Parece que não há um caixa aberto. Deseja abrir o caixa automaticamente no servidor agora e tentar novamente?');
              if (abrirAgora) {
                const fundoStr = prompt('Informe o Fundo de Troco (R$) para abrir o caixa:', '0.00');
                if (fundoStr === null) return;
                const fundo = parseFloat(fundoStr.replace(',', '.'));
                if (isNaN(fundo)) { alert('Valor inválido'); return; }
                this.openCaixaBackendFromVenda(fundo).then((novoId) => {
                  localStorage.setItem('caixaId', String(novoId));
                  (payload as any).caixaId = Number(novoId);
                  this.http.post<VendaResponse>(`${this.apiUrl}/vendas`, payload, { headers: this.getHeaders() })
                    .subscribe({
                      next: (venda2) => {
                        this.fichaGerada = venda2.ficha;
                        this.vendaConcluida = true;
                        this.carrinho = [];
                        this.carregando = false;
                        alert('✅ Venda realizada com sucesso!');
                      },
                      error: (e2) => {
                        console.error('Erro ao finalizar venda após abrir caixa:', e2);
                        const body = e2.error ?? e2.message ?? JSON.stringify(e2);
                        alert('Erro ao finalizar venda após abrir caixa: ' + (typeof body === 'string' ? body : JSON.stringify(body)));
                      }
                    });
                }).catch((err: any) => {
                  console.error('Falha ao abrir caixa no backend:', err);
                  alert('Falha ao abrir caixa no servidor: ' + (err?.message || String(err)));
                });
              }
            }

            // Tratamento para estoque insuficiente
            if ((serverMsg || '').toLowerCase().includes('estoque insuficiente')) {
              mensagemErro += '\n\n💡 Dica: Verifique a disponibilidade do produto no estoque.';
            }
          }

          alert(mensagemErro);
        }
      });
  }

  novaVenda() {
    this.vendaConcluida = false;
    this.fichaGerada = null;
    this.clienteNovo = { nome: '', telefone: '', email: '' };
  }
}