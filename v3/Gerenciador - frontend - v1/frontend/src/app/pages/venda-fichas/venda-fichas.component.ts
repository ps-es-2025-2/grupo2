import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-venda-fichas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './venda-fichas.component.html',
  styleUrls: ['./venda-fichas.component.css']
})
export class VendaFichasComponent {

  valorInput: number | null = null;
  quantidadeInput: number = 1;
  
  fichasGeradas: any[] = [];

  // Método para gerar a ficha
gerarFichas() {
    if (!this.valorInput || this.valorInput <= 0) {
      alert('Digite um valor válido!');
      return;
    }

    // Pega o que já tem salvo antes
    const bancoFichas = JSON.parse(localStorage.getItem('tb_fichas') || '[]');

    for (let i = 0; i < this.quantidadeInput; i++) {
      const novaFicha = {
        id: Date.now() + i, // ID único baseado no tempo
        valor: this.valorInput,
        codigo: `VALE-${this.valorInput}-${this.gerarHashAleatorio()}`,
        dataHora: new Date(),
        status: 'ATIVA' // Começa ativa
      };
      
      this.fichasGeradas.unshift(novaFicha);
      bancoFichas.unshift(novaFicha); // Adiciona na lista geral
    }

    // SALVA NO NAVEGADOR PARA A OUTRA TELA LER
    localStorage.setItem('tb_fichas', JSON.stringify(bancoFichas));

    this.valorInput = null;
  }

  // Função auxiliar para criar a "senha" aleatória da ficha
  private gerarHashAleatorio(): string {
    const caracteres = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
    let resultado = '';
    for (let i = 0; i < 6; i++) {
      resultado += caracteres.charAt(Math.floor(Math.random() * caracteres.length));
    }
    return resultado;
  }

  imprimir() {
    window.print();
  }

  limparTela() {
    this.fichasGeradas = [];
  }
}