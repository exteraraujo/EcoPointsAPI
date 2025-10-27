# EcoPoints API

API simples em Spring Boot para registrar usuários e ações sustentáveis com pontuação.

Como usar (local):

1. Configurar o banco de dados PostgreSQL (`application.properties` já aponta para `jdbc:postgresql://localhost:5432/ecopoints_db`).
2. Ajustar `ecopoints.jwt.secret` em `src/main/resources/application.properties` ou exportar como variável de ambiente nas configurações do Spring (ex.: `-Decopoints.jwt.secret=...`).
3. Rodar:

```bash
./mvnw.cmd spring-boot:run
```

Endpoints principais:
- POST /api/usuarios/cadastrar  — body: JSON de `Usuario` (nome, email, senha)
- POST /api/usuarios/login  — body: `{ "email": "...", "senha": "..." }` (retorna `{ "token": "..." }`)
- POST /api/acoes/criar — criar ação autenticado (Authorization: Bearer <token>)
- GET /api/acoes/minhas — listar ações do usuário autenticado
- GET /api/acoes/pontos — total de pontos do usuário autenticado

Testando com Postman (passo a passo)

1) Rodar a API localmente:

```powershell
./mvnw.cmd spring-boot:run
```

2) Cadastrar um usuário (POST)
- URL: http://localhost:8080/api/usuarios/cadastrar
- Body (JSON):

```json
{
  "nome": "João",
  "email": "joao@ex.com",
  "senha": "1234"
}
```

Resposta esperada: 201 CREATED com JSON do usuário (campo `senha` não será retornado).

3) Fazer login (POST)
- URL: http://localhost:8080/api/usuarios/login
- Body (JSON):

```json
{
  "email": "joao@ex.com",
  "senha": "1234"
}
```

Resposta esperada: 200 OK com body `{ "token": "<JWT>" }`. Copie o valor do token.

4) Criar uma ação (POST) — use o token no header Authorization
- URL: http://localhost:8080/api/acoes/criar
- Headers: `Authorization: Bearer <token>` and `Content-Type: application/json`
- Body (JSON):

```json
{
  "tipo": "RECICLAR_LIXO",
  "data": "2025-10-26",
  "descricao": "Descartei papel na lixeira separada"
}
```

Resposta esperada: 200 OK com JSON da ação criada (campo `pontos` preenchido automaticamente pelo tipo).

5) Listar ações do usuário (GET)
- URL: http://localhost:8080/api/acoes/minhas
- Header: `Authorization: Bearer <token>`

6) Consultar pontos (GET)
- URL: http://localhost:8080/api/acoes/pontos
- Header: `Authorization: Bearer <token>`

Resposta esperada: `{ "pontos": 10 }` (ou outro valor conforme ações registradas).

Observações de segurança:
- Não comitar segredos no repositório. Em `src/main/resources/application.properties` a propriedade `ecopoints.jwt.secret` está intencionalmente vazia no repositório.

Como fornecer o segredo localmente (exemplos):

- Como variável de ambiente (Windows PowerShell):

```powershell
$env:ECOPPOINTS_JWT_SECRET = 'seuSegredoAqui'
./mvnw.cmd spring-boot:run
```

- Como JVM/system property (passando via -D):

```powershell
./mvnw.cmd -Decopoints.jwt.secret="seuSegredoAqui" spring-boot:run
```

Observação: o nome da variável de ambiente mapeia para a propriedade Spring Boot (`ecopoints.jwt.secret`) usando a convenção uppercase/underscores (`ECOPPOINTS_JWT_SECRET`). Use o método que preferir no seu ambiente. Nunca comite o valor real no repositório.

- `spring.jpa.hibernate.ddl-auto=create` está ativo e irá recriar o schema a cada execução — útil para desenvolvimento, mas não recomendado para produção.
