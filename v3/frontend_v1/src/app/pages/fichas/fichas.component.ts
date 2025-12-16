import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-fichas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './fichas.component.html',
  styleUrls: ['./fichas.component.css']
})
export class FichasComponent implements OnInit {
  
  abaAtiva: 'vender' | 'validar' | 'relatorio' = 'vender';
  
  // VENDA DE FICHAS
  valorInput: number = 0;
  quantidadeInput: number = 1;
  fichasGeradas: any[] = [];
  
  // VALIDAÇÃO
  codigoInput: string = '';
  fichaEncontrada: any = null;
  mensagemErro: string = '';
  carregando: boolean = false;
  
  // RELATÓRIO
  listaFichas: any[] = [];
  filtro: string = '';
  // Cliente selecionado para emissão de fichas
  clienteIdInput: string = '';
  clienteIdSelecionado: number | null = null;
  clientesList: any[] = [];
  clienteSelectTemp: number | null = null;
  // Campos para criar cliente via UI
  clienteNomeInput: string = '';
  clienteTelefoneInput: string = '';
  clienteEmailInput: string = '';
  clienteUsernameInput: string = '';
  
  apiUrl = '/api/fichas';
  
  constructor(private http: HttpClient) {}
  
  ngOnInit() {
    this.carregarFichas();
    const stored = localStorage.getItem('clienteId');
    if (stored) {
      this.clienteIdSelecionado = Number(stored);
      this.clienteIdInput = stored;
    }
  }

  buscarClientes() {
    this.http.get<any[]>('/api/clientes', { headers: this.getHeaders() }).subscribe({
      next: (dados) => {
        this.clientesList = dados || [];
        if (this.clientesList.length === 0) {
          alert('Nenhum cliente encontrado. Você pode criar um novo cliente usando o botão de criação automática.');
        }
      },
      error: (err) => {
        console.error('Erro ao buscar clientes:', err);
        const body = err.error ?? err.message ?? JSON.stringify(err);
        alert('Erro ao buscar clientes: ' + (typeof body === 'string' ? body : JSON.stringify(body)));
      }
    });
  }

  createCliente() {
    if (!this.clienteNomeInput || !this.clienteNomeInput.trim()) {
      alert('Informe o nome do cliente para criação.');
      return;
    }
    const payload: any = { nome: this.clienteNomeInput.trim() };
    // incluir username se informado ou gerar um a partir do nome
    const username = this.clienteUsernameInput && this.clienteUsernameInput.trim()
      ? this.clienteUsernameInput.trim()
      : this.generateUsername(this.clienteNomeInput);
    if (username) payload.username = username;
    if (this.clienteTelefoneInput && this.clienteTelefoneInput.trim()) payload.telefone = this.clienteTelefoneInput.trim();
    if (this.clienteEmailInput && this.clienteEmailInput.trim()) payload.email = this.clienteEmailInput.trim();

    this.http.post<any>('/api/clientes', payload, { headers: this.getHeaders() }).subscribe({
      next: (created) => {
        const novoId = created?.id || created?.clienteId || null;
        if (!novoId) {
          alert('Cliente criado, mas o servidor não retornou o ID. Verifique no backend.');
          return;
        }
        localStorage.setItem('clienteId', String(novoId));
        this.clienteIdSelecionado = Number(novoId);
        this.clienteIdInput = String(novoId);
        alert('Cliente criado com sucesso. ID: ' + novoId);
        // limpa inputs
        this.clienteNomeInput = '';
        this.clienteTelefoneInput = '';
        this.clienteEmailInput = '';
          this.clienteUsernameInput = '';
        // atualiza lista local
        this.buscarClientes();
      },
      error: (err) => {
        console.error('Erro ao criar cliente:', err);
        const body = err.error ?? err.message ?? JSON.stringify(err);
        alert('Erro ao criar cliente: ' + (typeof body === 'string' ? body : JSON.stringify(body)));
      }
    });
  }

  selecionarClienteTemp() {
    if (!this.clienteSelectTemp) {
      alert('Selecione um cliente da lista.');
      return;
    }
    this.clienteIdSelecionado = this.clienteSelectTemp;
    this.clienteIdInput = String(this.clienteSelectTemp);
    localStorage.setItem('clienteId', String(this.clienteSelectTemp));
    alert('Cliente selecionado: ID ' + this.clienteSelectTemp);
  }
  
  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }
  
  mudarAba(aba: 'vender' | 'validar' | 'relatorio') {
    this.abaAtiva = aba;
    if (aba === 'relatorio') {
      this.carregarFichas();
    }
  }
  
  // ========== VENDA DE FICHAS ==========
  gerarFichas() {
    if (!this.valorInput || this.valorInput <= 0) {
      alert('Digite um valor válido!');
      return;
    }
    
    const payload: { valor: number; quantidade: number; clienteId?: number } = {
      valor: this.valorInput,
      quantidade: this.quantidadeInput
    };

    // Inclui `clienteId` no corpo da requisição, lendo de localStorage.
    const clienteId = this.clienteIdSelecionado ?? (localStorage.getItem('clienteId') ? Number(localStorage.getItem('clienteId')) : null);
    if (!clienteId) {
      alert('Cliente não selecionado. Defina um cliente antes de gerar fichas ou informe o ID do cliente.');
      return;
    }

    payload['clienteId'] = Number(clienteId);

    // Envia `clienteId` tanto no corpo quanto como query param (compatibilidade com backend)
    const url = `${this.apiUrl}?clienteId=${clienteId}`;

    this.http.post<any>(url, payload, { headers: this.getHeaders() }).subscribe({
      next: (ficha) => {
        if (ficha && ficha.codigo) {
          this.fichasGeradas = [ficha];
          alert(`Ficha gerada com sucesso! Código: ${ficha.codigo}`);
        } else {
          alert('Ficha gerada mas o servidor não retornou código.');
          this.fichasGeradas = [];
        }
      },
      error: (erro) => {
        console.error('Erro ao gerar fichas:', erro);
        // Mostrar detalhes úteis ao usuário para diagnóstico
        const serverBody = erro.error ?? erro.message ?? JSON.stringify(erro);
        const serverMsg = typeof serverBody === 'string' ? serverBody : JSON.stringify(serverBody);
        alert('Erro ao gerar fichas. Resposta do servidor: ' + serverMsg);

        // Se o backend informou que o cliente não foi encontrado, oferecer criar um cliente automaticamente
        try {
          const parsed = typeof serverBody === 'string' ? JSON.parse(serverBody) : serverBody;
          const errorText = parsed?.error || parsed?.message || serverMsg;
          if (errorText && errorText.toLowerCase().includes('cliente não encontrado')) {
            const criar = confirm('Cliente não encontrado. Deseja criar um novo cliente automaticamente e tentar novamente?');
            if (criar) {
              this.criarClienteAutomaticoAndRetry(payload);
            }
          }
        } catch (e) {
          // ignore parse errors
        }
      }
    });
  }

  private criarClienteAutomaticoAndRetry(originalPayload?: { valor: number; quantidade: number; clienteId?: number }, onCreated?: (novoId: number) => void) {
    // Pergunta nome do cliente ao usuário (padrão baseado no ID informado ou timestamp)
    const defaultName = this.clienteIdInput ? `Cliente ${this.clienteIdInput}` : `Cliente ${Date.now()}`;
    const nome = prompt('Informe o nome do cliente a ser criado:', defaultName);
    if (!nome) {
      alert('Criação de cliente cancelada.');
      return;
    }

    const clientePayload: any = { nome };
    // gerar username automático
    clientePayload.username = this.generateUsername(nome);

    this.http.post<any>('/api/clientes', clientePayload, { headers: this.getHeaders() }).subscribe({
      next: (created) => {
        const novoId = created?.id || created?.clienteId || null;
        if (!novoId) {
          alert('Cliente criado, mas o servidor não retornou o ID. Defina manualmente o clienteId e tente novamente.');
          return;
        }
        // Salva e atualiza estado
        localStorage.setItem('clienteId', String(novoId));
        this.clienteIdSelecionado = Number(novoId);
        this.clienteIdInput = String(novoId);
        alert('Cliente criado com ID ' + novoId + '.');

        // se foi passada uma callback, chama-a (ex.: retry de validação)
        if (onCreated) {
          try { onCreated(novoId); } catch (e) { console.error('Erro na callback pós-criação:', e); }
          return;
        }

        // Caso contrário, se houver um payload original, tenta gerar fichas (comportamento anterior)
        if (originalPayload) {
          alert('Tentando gerar fichas novamente...');
          const retryPayload = { ...originalPayload, clienteId: Number(novoId) };
          const url = `${this.apiUrl}?clienteId=${novoId}`;
          this.http.post<any[]>(url, retryPayload, { headers: this.getHeaders() }).subscribe({
            next: (fichas) => {
              this.fichasGeradas = fichas;
              alert(`${fichas.length} ficha(s) gerada(s) com sucesso!`);
            },
            error: (err) => {
              console.error('Erro ao gerar fichas após criar cliente:', err);
              const body = err.error ?? err.message ?? JSON.stringify(err);
              alert('Falha ao gerar fichas após criar cliente: ' + (typeof body === 'string' ? body : JSON.stringify(body)));
            }
          });
        }
      },
      error: (err) => {
        console.error('Erro ao criar cliente automaticamente:', err);
        const body = err.error ?? err.message ?? JSON.stringify(err);
        alert('Erro ao criar cliente: ' + (typeof body === 'string' ? body : JSON.stringify(body)));
      }
    });
  }

  private generateUsername(name: string): string {
    if (!name) return 'user' + Date.now();
    // cria versão simplificada do nome
    const base = name.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '');
    const suffix = Math.floor(Math.random() * 9000) + 1000; // 4 dígitos
    return (base || 'user') + '-' + suffix;
  }

  definirCliente() {
    if (!this.clienteIdInput || isNaN(Number(this.clienteIdInput))) {
      alert('Informe um ID de cliente válido.');
      return;
    }
    const id = Number(this.clienteIdInput);
    localStorage.setItem('clienteId', String(id));
    this.clienteIdSelecionado = id;
    alert('Cliente definido (ID: ' + id + '). Agora você pode gerar fichas.');
  }

  limparCliente() {
    localStorage.removeItem('clienteId');
    this.clienteIdSelecionado = null;
    this.clienteIdInput = '';
    alert('Cliente removido. Defina outro cliente se necessário.');
  }
  
  imprimir() {
    window.print();
  }
  
  limparTela() {
    this.fichasGeradas = [];
    this.valorInput = 0;
    this.quantidadeInput = 1;
  }
  
  // ========== VALIDAÇÃO ==========
  consultarFicha() {
    if (!this.codigoInput.trim()) {
      alert('Digite o código da ficha!');
      return;
    }
    
    this.carregando = true;
    this.mensagemErro = '';
    this.fichaEncontrada = null;
    
    this.http.get<any>(`${this.apiUrl}/${this.codigoInput}`, { headers: this.getHeaders() }).subscribe({
      next: (ficha) => {
        this.fichaEncontrada = ficha;
        this.carregando = false;
      },
      error: (erro) => {
        console.error('Erro ao buscar ficha:', erro);
        this.mensagemErro = erro.error?.message || 'Ficha não encontrada ou inválida.';
        this.carregando = false;
      }
    });
  }
  
  entregarProduto() {
    if (!this.fichaEncontrada) return;
    
    // Verifica se há saldo disponível
    if (this.fichaEncontrada.saldo <= 0) {
      alert('❌ Esta ficha não possui saldo disponível!');
      return;
    }

    // Pergunta o valor a ser consumido (padrão: todo o saldo)
    const valorStr = prompt(`Valor a consumir (disponível: R$ ${this.fichaEncontrada.saldo.toFixed(2)}):`, this.fichaEncontrada.saldo.toFixed(2));
    if (valorStr === null) return; // Cancelou
    
    const valorConsumir = parseFloat(valorStr.replace(',', '.'));
    if (isNaN(valorConsumir) || valorConsumir <= 0) {
      alert('❌ Valor inválido!');
      return;
    }

    if (valorConsumir > this.fichaEncontrada.saldo) {
      alert(`❌ Valor informado (R$ ${valorConsumir.toFixed(2)}) é maior que o saldo disponível (R$ ${this.fichaEncontrada.saldo.toFixed(2)})`);
      return;
    }
    
    const confirma = confirm(`Confirma a entrega para ${this.fichaEncontrada.cliente?.nome || 'este cliente'}?\n\nValor: R$ ${valorConsumir.toFixed(2)}\nSaldo após: R$ ${(this.fichaEncontrada.saldo - valorConsumir).toFixed(2)}`);
    if (!confirma) return;
    
    this.carregando = true;
    const payload = { valor: valorConsumir };

    this.http.post<any>(`${this.apiUrl}/${this.fichaEncontrada.codigo}/consumir`, payload, { headers: this.getHeaders() }).subscribe({
      next: (fichaAtualizada) => {
        const saldoRestante = fichaAtualizada.saldo;
        if (saldoRestante <= 0) {
          alert(`✅ PRODUTO ENTREGUE COM SUCESSO!\n\n⚠️ Ficha totalmente utilizada (saldo zerado).`);
        } else {
          alert(`✅ PRODUTO ENTREGUE COM SUCESSO!\n\nSaldo restante: R$ ${saldoRestante.toFixed(2)}`);
        }
        this.fichaEncontrada = fichaAtualizada;
        this.carregando = false;
      },
      error: (erro) => {
        console.error('Erro ao consumir ficha:', erro);
        this.carregando = false;
        
        const mensagemErro = erro.error || erro.message || 'Erro desconhecido';
        
        if (erro.status === 409) {
          alert(`❌ ${mensagemErro}`);
        } else if (erro.status === 404) {
          alert('❌ Ficha não encontrada!');
        } else if (erro.status === 400) {
          alert(`❌ ${mensagemErro}`);
        } else {
          alert('❌ Erro ao processar a entrega. Tente novamente.');
        }
      }
    });
  }
  
  limparBusca() {
    this.codigoInput = '';
    this.fichaEncontrada = null;
    this.mensagemErro = '';
  }
  
  // ========== RELATÓRIO ==========
  carregarFichas() {
    this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() }).subscribe({
      next: (dados) => {
        this.listaFichas = dados;
      },
      error: (erro) => console.error('Erro ao buscar fichas:', erro)
    });
  }
  
  get fichasFiltradas() {
    if (!this.filtro) return this.listaFichas;
    const termo = this.filtro.toLowerCase();
    return this.listaFichas.filter(f => 
      f.codigo.toLowerCase().includes(termo) || 
      f.status.toLowerCase().includes(termo)
    );
  }
  
  calcularTotalVendido() {
    return this.listaFichas.reduce((total, ficha) => total + (ficha.valor || 0), 0);
  }
  
  darBaixa(ficha: any) {
    const confirma = confirm(`Dar baixa na ficha ${ficha.codigo}?`);
    if (!confirma) return;
    
    // Atualiza localmente
    ficha.status = 'BAIXADA';
    ficha.dataBaixa = new Date();
    alert('Ficha marcada como usada!');
  }
  
  reativar(ficha: any) {
    const confirma = confirm(`Reativar a ficha ${ficha.codigo}?`);
    if (!confirma) return;
    
    ficha.status = 'ATIVA';
    ficha.dataBaixa = null;
    alert('Ficha reativada!');
  }
  
  excluir(index: number) {
    const confirma = confirm('Deseja realmente excluir este registro?');
    if (!confirma) return;
    
    const ficha = this.fichasFiltradas[index];
    
    this.http.delete(`${this.apiUrl}/${ficha.codigo}`, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Ficha excluída com sucesso!');
        this.carregarFichas();
      },
      error: (erro) => {
        console.error('Erro ao excluir:', erro);
        alert('❌ Erro ao excluir ficha.');
      }
    });
  }

  // Edição de fichas
  fichaEditando: any = null;
  fichaEdit: any = { valor: 0 };

  abrirEdicaoFicha(ficha: any) {
    this.fichaEditando = ficha;
    this.fichaEdit = { valor: ficha.valor };
  }

  cancelarEdicaoFicha() {
    this.fichaEditando = null;
  }

  salvarEdicaoFicha() {
    const payload = { ...this.fichaEditando, valor: this.fichaEdit.valor };
    
    this.http.put(`${this.apiUrl}/${this.fichaEditando.codigo}`, payload, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Ficha atualizada com sucesso!');
        this.fichaEditando = null;
        this.carregarFichas();
      },
      error: (erro) => {
        console.error('Erro ao editar:', erro);
        alert('❌ Erro ao atualizar ficha.');
      }
    });
  }

  // Edição e exclusão de clientes
  clienteEditando: any = null;
  clienteEdit: any = { nome: '', email: '', telefone: '' };

  abrirEdicaoCliente(cliente: any) {
    this.clienteEditando = cliente;
    this.clienteEdit = {
      nome: cliente.nome,
      email: cliente.email,
      telefone: cliente.telefone
    };
  }

  cancelarEdicaoCliente() {
    this.clienteEditando = null;
  }

  salvarEdicaoCliente() {
    const payload = { ...this.clienteEdit };
    
    this.http.put(`/api/clientes/${this.clienteEditando.id}`, payload, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Cliente atualizado com sucesso!');
        this.clienteEditando = null;
        this.buscarClientes();
      },
      error: (erro) => {
        console.error('Erro ao editar:', erro);
        alert('❌ Erro ao atualizar cliente.');
      }
    });
  }

  excluirCliente(id: number) {
    if (!confirm('Tem certeza que deseja excluir este cliente?')) return;

    this.http.delete(`/api/clientes/${id}`, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Cliente excluído com sucesso!');
        this.buscarClientes();
      },
      error: (erro) => {
        console.error('Erro ao excluir:', erro);
        alert('❌ Erro ao excluir cliente. Pode haver fichas vinculadas.');
      }
    });
  }
}
