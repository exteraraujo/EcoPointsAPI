EcoPoints API

API simples em Spring Boot para registrar usuários e ações sustentáveis com pontuação.

Como usar (local):

Configurar o banco de dados PostgreSQL (application.properties já aponta para jdbc:postgresql://localhost:5432/ecopoints_db).
Ajustar ecopoints.jwt.secret em src/main/resources/application.properties ou exportar como variável de ambiente nas configurações do Spring (ex.: -Decopoints.jwt.secret=...).
Rodar:
./mvnw.cmd spring-boot:run
Endpoints principais:

POST /api/usuarios/cadastrar — body: JSON de Usuario (nome, email, senha)
POST /api/usuarios/login — body: { "email": "...", "senha": "..." } (retorna { "token": "..." })
POST /api/acoes/criar — criar ação autenticado (Authorization: Bearer )
GET /api/acoes/minhas — listar ações do usuário autenticado
GET /api/acoes/pontos — total de pontos do usuário autenticado
Testando com Postman (passo a passo)

Rodar a API localmente:
./mvnw.cmd spring-boot:run
Cadastrar um usuário (POST)
URL: http://localhost:8080/api/usuarios/cadastrar
Body (JSON):
{
  "nome": "João",
  "email": "joao@ex.com",
  "senha": "1234"
}
Resposta esperada: 201 CREATED com JSON do usuário (campo senha não será retornado).

Fazer login (POST)
URL: http://localhost:8080/api/usuarios/login
Body (JSON):
{
  "email": "joao@ex.com",
  "senha": "1234"
}
Resposta esperada: 200 OK com body { "token": "<JWT>" }. Copie o valor do token.

Criar uma ação (POST) — use o token no header Authorization
URL: http://localhost:8080/api/acoes/criar
Headers: Authorization: Bearer <token> and Content-Type: application/json
Body (JSON):
{
  "tipo": "RECICLAR_LIXO",
  "data": "2025-10-26",
  "descricao": "Descartei papel na lixeira separada"
}
Resposta esperada: 200 OK com JSON da ação criada (campo pontos preenchido automaticamente pelo tipo).

Listar ações do usuário (GET)
URL: http://localhost:8080/api/acoes/minhas
Header: Authorization: Bearer <token>
Consultar pontos (GET)
URL: http://localhost:8080/api/acoes/pontos
Header: Authorization: Bearer <token>
Resposta esperada: { "pontos": 10 } (ou outro valor conforme ações registradas).

Observações de segurança:

Não comitar segredos no repositório. Em src/main/resources/application.properties a propriedade ecopoints.jwt.secret está intencionalmente vazia no repositório.
Como fornecer o segredo localmente (exemplos):

Como variável de ambiente (Windows PowerShell):
$env:ECOPPOINTS_JWT_SECRET = 'seuSegredoAqui'
./mvnw.cmd spring-boot:run
Como JVM/system property (passando via -D):
./mvnw.cmd -Decopoints.jwt.secret="seuSegredoAqui" spring-boot:run
Observação: o nome da variável de ambiente mapeia para a propriedade Spring Boot (ecopoints.jwt.secret) usando a convenção uppercase/underscores (ECOPPOINTS_JWT_SECRET). Use o método que preferir no seu ambiente. Nunca comite o valor real no repositório.

spring.jpa.hibernate.ddl-auto=create está ativo e irá recriar o schema a cada execução — útil para desenvolvimento, mas não recomendado para produção.
