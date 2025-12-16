import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

// Interface que representa FichaDigital do backend
interface FichaDigital {
  id: number;
  cliente: {
    id: number;
    nome: string;
    email: string;
    telefone: string;
  };
  saldo: number;
  limite: number;
  codigo: string;
  status: 'GERADA' | 'UTILIZADA';
}

@Component({
  selector: 'app-validacao',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './validacao.component.html',
  styleUrls: ['./validacao.component.css']
})
export class ValidacaoComponent {
  
  codigoInput: string = '';
  fichaEncontrada: FichaDigital | null = null;
  mensagemErro: string = '';
  carregando: boolean = false;

  apiUrl = '/api/fichas'; 

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token);
  }

  consultarFicha() {
    if (!this.codigoInput.trim()) {
      this.mensagemErro = 'Digite um código válido';
      return;
    }

    this.mensagemErro = '';
    this.fichaEncontrada = null;
    this.carregando = true;

    this.http.get<FichaDigital>(`${this.apiUrl}/${this.codigoInput}`, { headers: this.getHeaders() })
      .subscribe({
        next: (dados) => {
          this.fichaEncontrada = dados;
          this.carregando = false;
        },
        error: (erro) => {
          console.error('Erro ao buscar ficha:', erro);
          this.mensagemErro = 'Ficha não encontrada ou código inválido.';
          this.carregando = false;
        }
      });
  }

  entregarProduto() {
    if(!this.fichaEncontrada) return;

<<<<<<< HEAD
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

    if (!confirm(`Confirma a entrega para ${this.fichaEncontrada.cliente.nome}?\n\nValor: R$ ${valorConsumir.toFixed(2)}\nSaldo após: R$ ${(this.fichaEncontrada.saldo - valorConsumir).toFixed(2)}`)) {
      return;
    }

    const urlConsumir = `${this.apiUrl}/${this.fichaEncontrada.codigo}/consumir`;
    this.carregando = true;

    const payload = { valor: valorConsumir };

    this.http.post<FichaDigital>(urlConsumir, payload, { headers: this.getHeaders() }).subscribe({
      next: (fichaAtualizada) => {
        const saldoRestante = fichaAtualizada.saldo;
        if (saldoRestante <= 0) {
          alert(`✅ PRODUTO ENTREGUE COM SUCESSO!\n\n⚠️ Ficha totalmente utilizada (saldo zerado).`);
        } else {
          alert(`✅ PRODUTO ENTREGUE COM SUCESSO!\n\nSaldo restante: R$ ${saldoRestante.toFixed(2)}`);
        }
=======
    if (!confirm(`Confirma a entrega para ${this.fichaEncontrada.cliente.nome}?`)) {
      return;
    }

    const urlConsumir = `${this.apiUrl}/${this.fichaEncontrada.codigo}/consumir`;
    this.carregando = true;

    this.http.post<FichaDigital>(urlConsumir, {}, { headers: this.getHeaders() }).subscribe({
      next: (fichaAtualizada) => {
        alert('✅ PRODUTO ENTREGUE COM SUCESSO!');
>>>>>>> 7a918497ebee151fabee2fa8e53dade07b3544a5
        this.fichaEncontrada = fichaAtualizada;
        this.carregando = false;
      },
      error: (erro) => {
        console.error('Erro ao consumir ficha:', erro);
        this.carregando = false;
        
<<<<<<< HEAD
        const mensagemErro = erro.error || erro.message || 'Erro desconhecido';
        
        if (erro.status === 409) {
          alert(`❌ ${mensagemErro}`);
        } else if (erro.status === 404) {
          alert('❌ Ficha não encontrada!');
        } else if (erro.status === 400) {
          alert(`❌ ${mensagemErro}`);
=======
        if (erro.status === 409) {
          alert('❌ Esta ficha já foi utilizada anteriormente!');
        } else if (erro.status === 404) {
          alert('❌ Ficha não encontrada!');
>>>>>>> 7a918497ebee151fabee2fa8e53dade07b3544a5
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
}