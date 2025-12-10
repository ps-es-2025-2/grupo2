import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-consultar-fichas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './consultar-fichas.component.html',
  styleUrls: ['./consultar-fichas.component.css']
})
export class ConsultarFichasComponent implements OnInit {

  listaFichas: any[] = [];
  filtro: string = '';

  ngOnInit() {
    this.carregarDados();
  }

  carregarDados() {
    // Busca do "Banco do Navegador"
    const dados = localStorage.getItem('tb_fichas');
    this.listaFichas = dados ? JSON.parse(dados) : [];
  }

  darBaixa(ficha: any) {
    if(!confirm(`Confirma dar baixa na ficha ${ficha.codigo}?`)) return;

    ficha.status = 'BAIXADA'; // Muda o status
    ficha.dataBaixa = new Date(); // Registra quando foi usada
    
    this.salvarAlteracoes();
  }

  reativar(ficha: any) {
    ficha.status = 'ATIVA';
    ficha.dataBaixa = null;
    this.salvarAlteracoes();
  }

  excluir(index: number) {
    if(!confirm('Tem certeza que deseja apagar este registro?')) return;
    
    this.listaFichas.splice(index, 1);
    this.salvarAlteracoes();
  }

  // Atualiza o "Banco"
  salvarAlteracoes() {
    localStorage.setItem('tb_fichas', JSON.stringify(this.listaFichas));
  }

  // Filtro simples de busca visual
  get fichasFiltradas() {
    if (!this.filtro) return this.listaFichas;
    return this.listaFichas.filter(f => 
      f.codigo.toLowerCase().includes(this.filtro.toLowerCase()) ||
      f.status.toLowerCase().includes(this.filtro.toLowerCase())
    );
  }
  
  calcularTotalVendido() {
    return this.listaFichas.reduce((acc, f) => acc + f.valor, 0);
  }
}