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
  
  // Lista de movimentos (Histórico)
  movimentacoes: any[] = [];
  
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
    // Recupera o estado atual do navegador
    const dados = localStorage.getItem('caixa_local');
    if (dados) {
      const objeto = JSON.parse(dados);
      this.saldo = objeto.saldo || 0;
      this.status = objeto.status || 'FECHADO';
      this.movimentacoes = objeto.movimentacoes || [];
    }
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

    const valorStr = prompt('Informe o Fundo de Troco (R$):', '0.00');
    if (valorStr === null) return;

    const valor = parseFloat(valorStr.replace(',', '.'));
    if (isNaN(valor)) {
      alert('Valor inválido!');
      return;
    }

    // Tenta abrir o caixa no backend e salva o caixaId retornado
    this.openCaixaBackend(valor).then((novoId) => {
      // sucesso
      this.status = 'ABERTO';
      this.saldo = valor;
      this.registrarMovimento('ABERTURA', valor, 'Abertura de Caixa');
      this.salvarDados();
      alert('Caixa Aberto! ID: ' + novoId + '. Pode iniciar as vendas.');
    }).catch((err) => {
      console.error('Falha ao abrir caixa no backend:', err);
      alert('Falha ao abrir caixa no servidor. Caixa local não foi alterado. Verifique o servidor.');
    });
  }

  private async openCaixaBackend(fundoInicial: number): Promise<number> {
    const operadorEmail = localStorage.getItem('userEmail') || 'admin@local';
    const operadorId = localStorage.getItem('operadorId');

    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('Usuário não autenticado. Faça login antes de abrir o caixa.');
    }

    const endpoint = '/api/caixa/abrir';
    const payload = { saldoInicial: fundoInicial, operadorEmail };

    try {
      console.debug('Abrir caixa - endpoint', endpoint, 'payload', payload);
      const res: any = await this.http.post(endpoint, payload, { headers: this.getHeaders() }).toPromise();
      console.debug('Abrir caixa - resposta', res);
      const id = res?.id || res?.caixaId || res?.data?.id || null;
      if (id) {
        localStorage.setItem('caixaId', String(id));
        return id;
      }
      // Se não retornou id explícito, tenta extrair um número da resposta
      if (res && typeof res === 'object') {
        const possible = Object.values(res).find(v => typeof v === 'number');
        if (possible) {
          localStorage.setItem('caixaId', String(possible));
          return Number(possible);
        }
      }
      // Se chegou aqui, o backend respondeu sem id — lançar para UI tentar abrir localmente
      throw new Error('Resposta inesperada do servidor ao abrir caixa');
    } catch (err: any) {
      // Mostra a mensagem do servidor quando disponível
      const serverMsg = err?.error ?? err?.message ?? err;
      console.debug('Abrir caixa - erro', serverMsg, 'status', err?.status);

      // Se o servidor informou que já existe caixa aberto, propagar mensagem específica
      if (typeof serverMsg === 'string' && serverMsg.toLowerCase().includes('já existe') ) {
        throw new Error(serverMsg);
      }

      // Se foi um Bad Request/Validation, repassar o corpo do erro
      if (err?.error) {
        throw new Error(typeof err.error === 'string' ? err.error : JSON.stringify(err.error));
      }

      throw new Error('Falha ao abrir caixa no servidor: ' + (err?.status ?? 'erro desconhecido'));
    }
    // Se todos os endpoints falharem, pergunta ao usuário se deseja abrir caixa localmente
    const abrirLocal = confirm('Não foi possível abrir o caixa no servidor (erros 400/500). Deseja abrir o caixa localmente para permitir vendas offline?');
    if (abrirLocal) {
      const tempId = -Date.now();
      localStorage.setItem('caixaId', String(tempId));
      console.warn('Caixa aberto localmente com id temporário', tempId);
      return tempId;
    }

    throw new Error('Nenhum endpoint de abertura de caixa respondeu com sucesso');
  }

  realizarSangria() {
    if (this.status === 'FECHADO') {
      alert('Abra o caixa primeiro.');
      return;
    }

    const valorStr = prompt('Valor da Sangria (Retirada) R$:');
    if (!valorStr) return;
    const valor = parseFloat(valorStr.replace(',', '.'));

    if (valor > this.saldo) {
      alert('ERRO: Saldo insuficiente para essa retirada!');
      return;
    }

    const motivo = prompt('Motivo da retirada (Ex: Pagamento Fornecedor):');

    this.saldo -= valor;
    this.registrarMovimento('SANGRIA', valor, motivo || 'Retirada Diversa');
    this.salvarDados();
    alert('Sangria realizada com sucesso!');
  }

  fecharCaixa() {
    if (this.status === 'FECHADO') return;

    if (!confirm(`Confirma o fechamento?\nSaldo Final em Caixa: R$ ${this.saldo.toFixed(2)}`)) return;

    this.registrarMovimento('FECHAMENTO', 0, 'Fechamento do Dia');
    this.status = 'FECHADO';
    // Opcional: Zerar saldo ou manter para conferência
    // this.saldo = 0; 
    
    this.salvarDados();
    alert('Caixa Fechado com Sucesso.');
  }

  // Função auxiliar para gravar no histórico
  registrarMovimento(tipo: string, valor: number, descricao: string) {
    this.movimentacoes.unshift({
      data: new Date(),
      tipo: tipo, // ABERTURA, SANGRIA, FECHAMENTO
      valor: valor,
      descricao: descricao
    });
  }

  toggleDetalhes() {
    this.mostrarDetalhes = !this.mostrarDetalhes;
  }
}