import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.css']
})
export class UsuariosComponent implements OnInit {
  
  listaUsuarios: any[] = [];
  usuarioSelecionado: any = null;
  modoEdicao = false;

  // Formulário de novo usuário (cadastro)
  novoUsuario = {
    username: '',
    password: '',
    nome: '',
    role: 'OPERADOR'
  };

  // Formulário de edição (sem senha)
  usuarioEdit = {
    username: '',
    nome: '',
    role: 'OPERADOR',
    ativo: true
  };

  apiUrl = '/api/usuarios';
  apiAuthUrl = '/api/auth/register';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.carregarUsuarios();
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders()
      .set('Authorization', 'Bearer ' + token)
      .set('Content-Type', 'application/json');
  }

  carregarUsuarios() {
    this.http.get<any[]>(this.apiUrl, { headers: this.getHeaders() }).subscribe({
      next: (dados) => this.listaUsuarios = dados,
      error: (erro) => {
        console.error('Erro ao buscar usuários:', erro);
        if (erro.status === 403) {
          alert('Você não tem permissão para acessar esta funcionalidade. Apenas administradores.');
        }
      }
    });
  }

  salvarNovoUsuario() {
    // Usa o endpoint de register para criar um novo usuário
    this.http.post(this.apiAuthUrl, this.novoUsuario, { headers: this.getHeaders() }).subscribe({
      next: (res) => {
        alert('Usuário criado com sucesso!');
        this.carregarUsuarios();
        this.limparFormularioNovo();
      },
      error: (erro) => {
        console.error('Erro ao criar usuário:', erro);
        alert('Erro ao criar usuário. Verifique os dados!');
      }
    });
  }

  editarUsuario(usuario: any) {
    this.modoEdicao = true;
    this.usuarioSelecionado = usuario;
    this.usuarioEdit = {
      username: usuario.username,
      nome: usuario.nome,
      role: usuario.role,
      ativo: usuario.ativo
    };
  }

  salvarEdicao() {
    if (!this.usuarioSelecionado) return;

    this.http.put(`${this.apiUrl}/${this.usuarioSelecionado.id}`, this.usuarioEdit, { headers: this.getHeaders() }).subscribe({
      next: (res) => {
        alert('Usuário atualizado com sucesso!');
        this.carregarUsuarios();
        this.cancelarEdicao();
      },
      error: (erro) => {
        console.error('Erro ao atualizar usuário:', erro);
        alert('Erro ao atualizar usuário!');
      }
    });
  }

  cancelarEdicao() {
    this.modoEdicao = false;
    this.usuarioSelecionado = null;
    this.usuarioEdit = {
      username: '',
      nome: '',
      role: 'OPERADOR',
      ativo: true
    };
  }

  deletarUsuario(id: number) {
    if (!confirm('Tem certeza que deseja deletar este usuário?')) return;

    this.http.delete(`${this.apiUrl}/${id}`, { headers: this.getHeaders() }).subscribe({
      next: () => {
        alert('Usuário deletado com sucesso!');
        this.carregarUsuarios();
      },
      error: (erro) => {
        console.error('Erro ao deletar usuário:', erro);
        alert('Erro ao deletar usuário!');
      }
    });
  }

  limparFormularioNovo() {
    this.novoUsuario = {
      username: '',
      password: '',
      nome: '',
      role: 'OPERADOR'
    };
  }

  getRoleBadgeClass(role: string): string {
    return role === 'ADMIN' ? 'badge-admin' : 'badge-operador';
  }

  getStatusBadgeClass(ativo: boolean): string {
    return ativo ? 'badge-ativo' : 'badge-inativo';
  }
}
