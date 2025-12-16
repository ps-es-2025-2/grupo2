import { Component, OnInit, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-eventos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './eventos.component.html',
  styleUrls: ['./eventos.component.css']
})
export class EventosComponent implements OnInit {
  listaEventos: any[] = [];
  mgmtOpen = false;

  // opções de status
  statusOptions = ['PLANEJADO','PREPARACAO','EXECUCAO','PAUSADO','FINALIZADO','CANCELADO'];

  // Edição
  eventoEditando: any = null;
  eventoEdit: any = { nome: '', descricao: '', local: '', dataInicio: '', dataFim: '' };
  // local overrides (persistidos no navegador)
  statusOverridesKey = 'event_status_overrides';

  // Objeto igual ao que o Java espera
  novoEvento = {
    nome: '',
    descricao: '',
    local: '',
    dataInicio: '',
    dataFim: '',
    status: 'PLANEJADO'
  };

  apiUrl = '/api/eventos'; 

  constructor(private http: HttpClient, private el: ElementRef) {}

  ngOnInit() {
    this.carregarEventos();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }

  carregarEventos() {
    this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() }).subscribe({
      next: (dados) => {
        // Normaliza status para string (caso venha como enum/objeto)
        const loaded = (dados || []).map(e => {
          const statusRaw = e.status ?? '';
          const statusStr = (typeof statusRaw === 'string') ? statusRaw : (statusRaw?.name ?? statusRaw?.toString() ?? '');
          return { ...e, status: statusStr };
        });
        // aplica overrides locais, se houver
        const overrides = this.loadStatusOverrides();
        this.listaEventos = loaded.map(e => ({ ...e, status: overrides[e.id] ?? e.status }));
      },
      error: (erro) => console.error('Erro ao buscar eventos:', erro)
    });
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'PLANEJADO':
        return 'badge-planejado';
      case 'PREPARACAO':
        return 'badge-preparacao';
      case 'EXECUCAO':
        return 'badge-execucao';
      case 'PAUSADO':
        return 'badge-pausado';
      case 'FINALIZADO':
        return 'badge-finalizado';
      case 'CANCELADO':
        return 'badge-cancelado';
      default:
        return 'badge-default';
    }
  }

  salvar() {
    this.http.post(this.apiUrl, this.novoEvento, { headers: this.getHeaders() }).subscribe({
      next: (res) => {
        alert('Evento criado com sucesso!');
        this.carregarEventos();
        this.novoEvento = { nome: '', descricao: '', local: '', dataInicio: '', dataFim: '', status: 'PLANEJADO' };
      },
      error: (erro) => {
        console.error('Erro ao salvar:', erro);
        alert('Erro ao salvar evento. Verifique as datas!');
      }
    });
  }

  changeStatus(evento: any, novoStatus: string) {
    if (evento.status === novoStatus) return;
    
    const payload = { ...evento, status: novoStatus };
    
    this.http.put(`${this.apiUrl}/${evento.id}`, payload, { headers: this.getHeaders() }).subscribe({
      next: () => {
        // Remove override local, pois agora está persistido
        this.removeStatusOverride(evento.id);
        this.carregarEventos();
      },
      error: (erro) => {
        console.error('Erro ao mudar status:', erro);
        alert('❌ Erro ao alterar status do evento.');
      }
    });
  }

  loadStatusOverrides(): { [id: number]: string } {
    try {
      const raw = localStorage.getItem(this.statusOverridesKey);
      return raw ? JSON.parse(raw) : {};
    } catch (e) {
      console.error('Erro ao ler overrides locais:', e);
      return {};
    }
  }

  saveStatusOverride(id: number, status: string) {
    const map = this.loadStatusOverrides();
    map[id] = status;
    try {
      localStorage.setItem(this.statusOverridesKey, JSON.stringify(map));
    } catch (e) {
      console.error('Erro ao salvar override local:', e);
    }
  }

  removeStatusOverride(id: number) {
    const map = this.loadStatusOverrides();
    delete map[id];
    try {
      localStorage.setItem(this.statusOverridesKey, JSON.stringify(map));
    } catch (e) {
      console.error('Erro ao remover override local:', e);
    }
  }

  abrirEdicao(evento: any) {
    this.eventoEditando = evento;
    this.eventoEdit = {
      nome: evento.nome,
      descricao: evento.descricao,
      local: evento.local,
      dataInicio: evento.dataInicio,
      dataFim: evento.dataFim
    };
  }

  cancelarEdicao() {
    this.eventoEditando = null;
  }

  salvarEdicao() {
    if (!this.eventoEdit.nome || !this.eventoEdit.dataInicio) {
      alert('Preencha os campos obrigatórios!');
      return;
    }

    const payload = {
      ...this.eventoEditando,
      ...this.eventoEdit
    };

    this.http.put(`${this.apiUrl}/${this.eventoEditando.id}`, payload, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Evento atualizado com sucesso!');
        this.eventoEditando = null;
        this.carregarEventos();
      },
      error: (erro) => {
        console.error('Erro ao editar:', erro);
        alert('❌ Erro ao atualizar evento.');
      }
    });
  }

  excluirEvento(id: number) {
    if (!confirm('Tem certeza que deseja excluir este evento?')) {
      return;
    }

    this.http.delete(`${this.apiUrl}/${id}`, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('✅ Evento excluído com sucesso!');
        this.removeStatusOverride(id);
        this.carregarEventos();
      },
      error: (erro) => {
        console.error('Erro ao excluir:', erro);
        alert('❌ Erro ao excluir evento. Pode haver caixas ou produtos vinculados.');
      }
    });
  }

  getStatusLabel(status: string): string {
    const labels: { [key: string]: string } = {
      'PLANEJADO': 'PLANEJADO',
      'PREPARACAO': 'PREPARAÇÃO',
      'EXECUCAO': 'EXECUÇÃO',
      'PAUSADO': 'PAUSADO',
      'FINALIZADO': 'FINALIZADO',
      'CANCELADO': 'CANCELADO'
    };
    return labels[status] || status;
  }

  getStatusColor(status: string): string {
    const colors: { [key: string]: string } = {
      'PLANEJADO': '#2196F3',
      'PREPARACAO': '#FF9800',
      'EXECUCAO': '#4CAF50',
      'PAUSADO': '#FFC107',
      'FINALIZADO': '#9C27B0',
      'CANCELADO': '#F44336'
    };
    return colors[status] || '#9e9e9e';
  }
}