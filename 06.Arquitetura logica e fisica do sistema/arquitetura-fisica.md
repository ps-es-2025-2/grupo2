# Arquitetura Física — Caneco Cheio

```plantuml
@startuml
title Arquitetura Física — Caneco Cheio

skinparam shadowing false
skinparam defaultTextAlignment left
skinparam componentStyle rectangle
skinparam packageStyle rectangle

cloud "Internet" as INTERNET {
  node "Cliente Web\n(Navegador - Angular)" as CLIENT_WEB <<client>>
}

rectangle "DMZ (Zona Pública)" as DMZ {
  node "Nginx / API Gateway\n(Reverse Proxy, TLS)" as GATEWAY <<gateway>>
}

rectangle "Servidor A — Aplicação (Backend)" as SRV_APP {
  node "Caneco Cheio API\n(Spring Boot 3 / Java 17)" as API <<app>>
  node "Nginx (opcional)\n(static assets / proxy)" as NGINX_APP <<infra>>
  database "Banco de Dados\nPostgreSQL (container)" as DB
  node "H2 (dev) - in-memory" as H2
}

rectangle "Servidor B — Frontend / CDN" as SRV_FE {
  node "Frontend Angular\n(ng serve dev / build static)" as FRONTEND <<web>>
  node "CDN / Static Hosting\n(nginx / hosting)" as CDN <<infra>>
}

rectangle "Servidor C — Infra & Serviços" as SRV_INFRA {
  node "Docker Host\n(docker-compose)" as DOCKER_HOST <<infra>>
  node "Storage / S3 compatible" as STORAGE <<external>>
  node "SMTP / Email Service" as SMTP <<external>>
  node "Serviços PDF / QR\n(terceiros ou container)" as EXT_SERVICES <<external>>
}

CLIENT_WEB --> GATEWAY : HTTPS Requests (443)
GATEWAY --> CDN : GET static assets (prod)
GATEWAY --> API : REST (/api/**)

FRONTEND --> API : REST (dev: ng serve → http://localhost:8080)

API --> DB : JDBC (PostgreSQL 5432)
API --> EXT_SERVICES : HTTP (PDF, QR)
API --> SMTP : SMTP / API
API --> STORAGE : Upload/Download arquivos

DOCKER_HOST --> DB
DOCKER_HOST --> API
DOCKER_HOST --> CDN

note top of SRV_APP
Servidor A — Backend Principal
• Executa a API Spring Boot
• Conecta ao Postgres (container)
• Em dev pode usar H2
end note

note right of SRV_FE
Servidor B — Frontend
• Dev: ng serve (4200)
• Prod: build estático via CDN/Nginx
end note

note right of SRV_INFRA
Servidor C — Infra & Serviços
• Docker + docker-compose
• Serviços externos integrados
end note
@enduml
