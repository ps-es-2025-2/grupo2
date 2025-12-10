# Frontend (Aplicação Angular)

Este repositório contém o front-end da aplicação, desenvolvido com Angular 21.x. Este README descreve como preparar o ambiente, executar a aplicação em modo de desenvolvimento, gerar builds de produção, executar testes e solucionar problemas comuns.

**Sumário**
- **Pré-requisitos**: versões recomendadas do Node.js e npm
- **Instalação**: como instalar dependências
- **Desenvolvimento**: iniciar servidor e opções úteis
- **Build**: gerar artefatos para produção
- **Estrutura**: visão rápida das pastas principais
- **Solução de problemas**: vulnerabilidades e erros comuns

**Pré-requisitos**
- **Node.js**: recomenda-se usar uma versão LTS (por exemplo Node 18.x ou 20.x). Verifique com:
	```powershell
	node -v
	npm -v
	```
- **npm**: vem com o Node; a propriedade `packageManager` em `package.json` sugere `npm@11.6.2`, mas qualquer versão compatível com seu Node LTS deve funcionar.

**Instalação (uma vez por máquina)**
1. Abra um terminal PowerShell.
2. Entre no diretório raiz do frontend (onde está o `package.json`):
	 ```powershell
	 cd 'C:\Users\PICHAU\Desktop\FACULDADE\7º SEMESTRE\grupo2\v3\frontend_v1'
	 ```
3. Instale dependências:
	 ```powershell
	 npm install
	 ```

Observação: após a instalação, a pasta `node_modules` e o arquivo `package-lock.json` estarão na raiz do projeto (`frontend_v1`).

**Scripts úteis (em `package.json`)**
- `npm start` — inicia o servidor de desenvolvimento (executa `ng serve`).
- `npm run build` — gera build de produção (arquivos em `dist/`).
- `npm run watch` — build em modo watch para desenvolvimento.
- `npm test` — executa testes (configuração do Vitest / executor de testes do Angular).

**Rodando em desenvolvimento**
1. No diretório do projeto, rode:
	 ```powershell
	 npm start
	 ```
2. Abra um navegador em `http://localhost:4200`.
3. O Angular CLI recompilará a aplicação automaticamente ao salvar alterações nos arquivos fonte.

Opções comuns (passar argumentos adicionais após `--`):
```powershell
npm start -- --port 4201 --host 0.0.0.0
```

**Build para produção**
```powershell
npm run build
```
Os arquivos otimizados serão colocados em `dist/` (verifique `angular.json` para detalhes de saída e opções).


**Estrutura do projeto (visão rápida)**
- `src/` — código-fonte da aplicação
	- `app/` — módulo/aplicação principal e páginas
		- `pages/` — componentes de página (ex.: `dashboard`, `login`, `venda`, `estoque`, etc.)
	- `index.html`, `main.ts`, `styles.css` — entrada e estilos globais
- `angular.json` — configuração do Angular CLI
- `package.json` — scripts e dependências

**Configurações e variáveis de ambiente**
- Para configurar variáveis (API endpoints, chaves, etc.), use os arquivos de ambiente do Angular (ex.: `src/environments/`) se presentes, ou crie/edite conforme necessidade. Ajuste `angular.json` para incluir configurações específicas de build.

**Vulnerabilidades e auditoria de dependências**
Após `npm install` você pode ver avisos de vulnerabilidades. Comandos recomendados:
```powershell
npm audit
npm audit fix
```
Se houver vulnerabilidades que exigem atualizações quebradoras, o comando abaixo força atualizações, mas pode introduzir incompatibilidades — use com cuidado e sempre teste a aplicação:
```powershell
npm audit fix --force
```

**Solução de problemas comuns**
- Erro: `ng: command not found` — o `@angular/cli` está instalado localmente. Use `npm start` ou execute `npx ng serve`.
- Erros de versão do TypeScript/Node — verifique `devDependencies` em `package.json` e atualize sua versão do Node para a recomendada.
- Erro ao compilar — leia o log do terminal; faltas de import/export costumam apontar para o arquivo/linha exata.


