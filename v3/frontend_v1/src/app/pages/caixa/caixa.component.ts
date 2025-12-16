import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-caixa',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './caixa.component.html',
  styleUrls: ['./caixa.component.css']
})
export class CaixaComponent implements OnInit {

  // Estado do Caixa
  saldo: number = 0;
  status: string = 'FECHADO'; // 'ABERTO' ou 'FECHADO'
  caixaAtual: any = null;
  
  // Lista de movimentos (Histórico)
  movimentacoes: any[] = [];
  
  // Histórico de caixas fechados
  historicoCaixas: any[] = [];
  mostrarHistorico: boolean = false;
  
  // Controle visual para mostrar a tabela
  mostrarDetalhes: boolean = false;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.carregarDados();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders().set('Authorization', 'Bearer ' + token).set('Content-Type', 'application/json');
  }

  carregarDados() {
    // Tenta buscar caixa aberto no backend
    this.verificarCaixaAberto();
    
    // Carrega dados locais como fallback
    const dados = localStorage.getItem('caixa_local');
    if (dados) {
      const objeto = JSON.parse(dados);
      if (!this.caixaAtual) {
        this.saldo = objeto.saldo || 0;
        this.status = objeto.status || 'FECHADO';
        this.movimentacoes = objeto.movimentacoes || [];
      }
    }
  }

  verificarCaixaAberto() {
    this.http.get<any[]>('/api/caixa', { headers: this.getHeaders() }).subscribe({
      next: (caixas) => {
        const operadorEmail = localStorage.getItem('userEmail') || 'admin@local';
        const caixaAberto = caixas.find(c => c.status === 'ABERTO' && c.operador?.email === operadorEmail);
        
        if (caixaAberto) {
          this.caixaAtual = caixaAberto;
          this.status = 'ABERTO';
          // Usa saldoFinal se disponível, senão usa saldoInicial
          this.saldo = caixaAberto.saldoFinal || caixaAberto.saldoInicial || 0;
          localStorage.setItem('caixaId', String(caixaAberto.id));
          this.salvarDados();
        }
      },
      error: (erro) => console.error('Erro ao verificar caixa aberto:', erro)
    });
  }

  carregarHistoricoCaixas() {
    this.http.get<any[]>('/api/caixa', { headers: this.getHeaders() }).subscribe({
      next: (caixas) => {
        this.historicoCaixas = caixas.filter(c => c.status === 'FECHADO').reverse();
        this.mostrarHistorico = true;
      },
      error: (erro) => {
        console.error('Erro ao carregar histórico:', erro);
        alert('❌ Erro ao carregar histórico de caixas.');
      }
    });
  }

  salvarDados() {
    const dados = {
      saldo: this.saldo,
      status: this.status,
      movimentacoes: this.movimentacoes
    };
    localStorage.setItem('caixa_local', JSON.stringify(dados));
  }

  // --- AÇÕES DO CAIXA ---

  abrirCaixa() {
    if (this.status === 'ABERTO') {
      alert('O caixa já está aberto!');
      return;
    }

    const valorStr = prompt('Informe o Fundo de Troco (R$):', '100.00');
    if (valorStr === null) return;

    const valor = parseFloat(valorStr.replace(',', '.'));
    if (isNaN(valor) || valor < 0) {
      alert('Valor inválido! Digite um número válido.');
      return;
    }

    const operadorEmail = localStorage.getItem('userEmail') || 'admin@local';
    const endpoint = '/api/caixa/abrir';
    const payload = { saldoInicial: valor, operadorEmail };

    this.http.post(endpoint, payload, { headers: this.getHeaders() }).subscribe({
      next: (res: any) => {
        const id = res?.id || res?.caixaId || res?.data?.id || Math.floor(Date.now() / 1000);
        localStorage.setItem('caixaId', String(id));
        this.status = 'ABERTO';
        this.saldo = valor;
        this.registrarMovimento('ABERTURA', valor, 'Abertura de Caixa');
        this.salvarDados();
        alert(`Caixa Aberto com sucesso! ID: ${id}\nFundo inicial: R$ ${valor.toFixed(2)}\n\nPode iniciar as vendas.`);
      },
      error: (erro) => {
        console.error('Erro ao abrir caixa no backend:', erro);
        const abrirLocal = confirm('Não foi possível conectar ao servidor.\n\nDeseja abrir o caixa em modo LOCAL?');
        if (abrirLocal) {
          const tempId = -Date.now();
          localStorage.setItem('caixaId', String(tempId));
          localStorage.setItem('caixa_local_offline', 'true');
          this.status = 'ABERTO';
          this.saldo = valor;
          this.registrarMovimento('ABERTURA', valor, 'Abertura de Caixa (LOCAL)');
          this.salvarDados();
          alert(`Caixa aberto em modo LOCAL!\nID Temporário: ${tempId}\nFundo inicial: R$ ${valor.toFixed(2)}\n\nOs dados serão salvos apenas no navegador.`);
        }
      }
    });
  }

  realizarSangria() {
    if (this.status === 'FECHADO') { alert('Abra o caixa primeiro.'); return; }
    const valorStr = prompt('Valor da Sangria (Retirada) R$:');
    if (!valorStr) return;
    const valor = parseFloat(valorStr.replace(',', '.'));
    if (isNaN(valor) || valor <= 0) { alert('Valor inválido'); return; }
    if (valor > this.saldo) { alert('ERRO: Saldo insuficiente para essa retirada!'); return; }
    const motivo = prompt('Motivo da retirada (Ex: Pagamento Fornecedor):');
    
    const caixaId = localStorage.getItem('caixaId');
    if (caixaId && Number(caixaId) > 0) {
      // Registra sangria no backend
      const idCaixa = Number(caixaId);
      const operadorEmail = localStorage.getItem('userEmail') || 'admin@local';
      const payload = { valor, justificativa: motivo || 'Retirada Diversa', operadorUsername: operadorEmail };
      this.http.post(`/api/sangrias?caixaId=${idCaixa}`, payload, { headers: this.getHeaders() }).subscribe({
        next: () => {
          this.saldo -= valor;
          this.registrarMovimento('SANGRIA', valor, motivo || 'Retirada Diversa');
          this.salvarDados();
          alert('✅ Sangria realizada com sucesso!');
          this.verificarCaixaAberto();
        },
        error: (erro) => {
          console.error('Erro ao registrar sangria:', erro);
          const msg = erro.error?.message || erro.message || 'Erro desconhecido';
          alert('❌ Erro ao registrar sangria: ' + msg);
        }
      });
    } else {
      // Modo local
      this.saldo -= valor;
      this.registrarMovimento('SANGRIA', valor, motivo || 'Retirada Diversa');
      this.salvarDados();
      alert('Sangria realizada (LOCAL)!');
    }
  }

  async fecharCaixa() {
    if (this.status === 'FECHADO') return;
    if (!confirm(`Confirma o fechamento?\nSaldo Final em Caixa: R$ ${this.saldo.toFixed(2)}`)) return;

    // se existir caixaId salvo e for positivo (backend), tenta fechar no servidor
    const caixaIdStr = localStorage.getItem('caixaId');
    let closedOnServer = false;
    if (caixaIdStr) {
      const idNum = Number(caixaIdStr);
      if (!isNaN(idNum) && idNum > 0) {
        try {
          const payload = { saldoFinal: this.saldo };
          await this.http.post(`/api/caixa/${idNum}/fechar`, payload, { headers: this.getHeaders() }).toPromise();
          closedOnServer = true;
        } catch (err) {
          console.debug('Erro ao fechar caixa no servidor', err);
        }
      }
    }

    this.registrarMovimento('FECHAMENTO', 0, 'Fechamento do Dia');
    this.status = 'FECHADO';
    this.salvarDados();
    alert('Caixa Fechado com Sucesso.' + (closedOnServer ? ' (fechado no servidor)' : ' (local)'));
  }

  // Função auxiliar para gravar no histórico
  registrarMovimento(tipo: string, valor: number, descricao: string) {
    const movimento = { data: new Date(), tipo: tipo, valor: valor, descricao: descricao };
    this.movimentacoes.unshift(movimento);
    // If opened local (temp id negative), persist unsynced movements separately for later synchronization
    const caixaIdStr = localStorage.getItem('caixaId');
    if (caixaIdStr && Number(caixaIdStr) < 0) {
      try {
        const key = 'caixa_local_unsynced';
        const raw = localStorage.getItem(key);
        const arr = raw ? JSON.parse(raw) : [];
        arr.push(movimento);
        localStorage.setItem(key, JSON.stringify(arr));
      } catch (e) {
        console.debug('Erro ao persistir movimento offline', e);
      }
    }
  }

  // Try to create the caixa on the server using local data and mark movements as synced.
  async sincronizarCaixaLocal(): Promise<void> {
    const caixaLocalRaw = localStorage.getItem('caixa_local');
    if (!caixaLocalRaw) throw new Error('Nenhum caixa local encontrado');
    const caixaLocal = JSON.parse(caixaLocalRaw);
    const saldoInicial = caixaLocal.saldo ?? 0;
    const operadorEmail = localStorage.getItem('userEmail') || 'admin@local';

    // create caixa on server
    const endpoint = '/api/caixa/abrir';
    const payload = { saldoInicial, operadorEmail };
    try {
      const res: any = await this.http.post(endpoint, payload, { headers: this.getHeaders() }).toPromise();
      const id = res?.id || res?.caixaId || (Object.values(res || {}).find(v => typeof v === 'number') as any) || null;
      if (!id) throw new Error('Servidor não retornou id para o caixa criado');
      // store server caixa id
      localStorage.setItem('caixaId', String(id));
      // clear offline flag and unsynced movements (note: full movements replay not implemented)
      localStorage.removeItem('caixa_local_offline');
      localStorage.removeItem('caixa_local_unsynced');
      // update status and persist
      this.status = 'ABERTO';
      this.salvarDados();
    } catch (err: any) {
      throw err;
    }
  }

  toggleDetalhes() {
    this.mostrarDetalhes = !this.mostrarDetalhes;
  }

  fecharHistorico() {
    this.mostrarHistorico = false;
  }

}