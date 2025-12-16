import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ProdutosComponent } from './pages/produtos/produtos.component';
import { EventosComponent } from './pages/eventos/eventos.component';
import { CaixaComponent } from './pages/caixa/caixa.component';
import { VendaComponent } from './pages/venda/venda.component';
import { EstoqueComponent } from './pages/estoque/estoque.component';
import { FichasComponent } from './pages/fichas/fichas.component';
import { UsuariosComponent } from './pages/usuarios/usuarios.component';
import { SolicitacoesComponent } from './pages/solicitacoes/solicitacoes.component';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'dashboard', component: DashboardComponent },
    { path: 'produtos', component: ProdutosComponent },
    { path: 'eventos', component: EventosComponent },
    { path: 'usuarios', component: UsuariosComponent },
    { path: 'solicitacoes', component: SolicitacoesComponent },
    { path: 'caixa', component: CaixaComponent },
    { path: 'venda', component: VendaComponent },
    { path: 'estoque', component: EstoqueComponent },
    { path: 'fichas', component: FichasComponent }
];