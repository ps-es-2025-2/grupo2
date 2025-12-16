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

    if (!confirm(`Confirma a entrega para ${this.fichaEncontrada.cliente.nome}?`)) {
      return;
    }

    const urlConsumir = `${this.apiUrl}/${this.fichaEncontrada.codigo}/consumir`;
    this.carregando = true;

    this.http.post<FichaDigital>(urlConsumir, {}, { headers: this.getHeaders() }).subscribe({
      next: (fichaAtualizada) => {
        alert('✅ PRODUTO ENTREGUE COM SUCESSO!');
        this.fichaEncontrada = fichaAtualizada;
        this.carregando = false;
      },
      error: (erro) => {
        console.error('Erro ao consumir ficha:', erro);
        this.carregando = false;
        
        if (erro.status === 409) {
          alert('❌ Esta ficha já foi utilizada anteriormente!');
        } else if (erro.status === 404) {
          alert('❌ Ficha não encontrada!');
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