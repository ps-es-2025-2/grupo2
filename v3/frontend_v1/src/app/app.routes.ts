import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ProdutosComponent } from './pages/produtos/produtos.component';
import { EventosComponent } from './pages/eventos/eventos.component';
import { CaixaComponent } from './pages/caixa/caixa.component';
import { VendaComponent } from './pages/venda/venda.component';
import { EstoqueComponent } from './pages/estoque/estoque.component';
import { ValidacaoComponent } from './pages/validacao/validacao.component';
import { VendaFichasComponent } from './pages/venda-fichas/venda-fichas.component';
// 1. IMPORTAR A NOVA TELA DE CONSULTA
import { ConsultarFichasComponent } from './pages/consultar-fichas/consultar-fichas.component';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'dashboard', component: DashboardComponent },
    { path: 'produtos', component: ProdutosComponent },
    { path: 'eventos', component: EventosComponent },
    { path: 'caixa', component: CaixaComponent },
    { path: 'venda', component: VendaComponent },
    { path: 'estoque', component: EstoqueComponent },
    { path: 'validacao', component: ValidacaoComponent },
    { path: 'venda-fichas', component: VendaFichasComponent },
    
    // 2. REGISTRAR A NOVA ROTA
    { path: 'consultar-fichas', component: ConsultarFichasComponent } 
];