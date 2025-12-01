import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

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

  ngOnInit() {
    this.carregarDados();
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

    this.status = 'ABERTO';
    this.saldo = valor;
    
    this.registrarMovimento('ABERTURA', valor, 'Abertura de Caixa');
    this.salvarDados();
    alert('Caixa Aberto! Pode iniciar as vendas.');
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