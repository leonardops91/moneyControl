<h1 align="center">Money Control</h1>

![Badge Produção](https://img.shields.io/badge/LANGUAGE-JAVA%201.8-green)
![Badge Produção](https://img.shields.io/badge/FRAMEWORK-SPRING%20BOOT-red)
![Badge Produção](https://img.shields.io/badge/DATABASE-POSTGRES-blue)
![Badge Produção](https://img.shields.io/badge/AUTENTICATION-JWT-brightgreen)


## Objetivo

Criação de uma API Rest para controle de Receitas e Despesas, que possa 
criar usuários e que gere 
relatório mensal financeiro para o usuário.


### Stack
+ Linguagem - Java
+ Ambiente de desenvolvimento - IntelliJ IDEA Comunity Edition

### Configurando o projeto para produção

Copie o arquivo "application.properties.example" para "application.properties". <br>

```
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=create
spring.jpa.hibernate.show-sql=true
Spring.jpa.properties.hibernate.show_sql=true
Spring.jpa.properties.hibernate.format_sql=true
spring.datasource.url=jdbc:postgresql://localhost:5432/MoneyControl
spring.datasource.username=
spring.datasource.password=

spring.datasource.initialization-mode=always
spring.datasource.initialize=true
spring.datasource.continue-on-error=true

spring.jackson.serialization.fail-on-empty-beans=false

# jwt
moneycontrol.jwt.secret=rm'!@N=Ke!~p8VTA2ZRK~nMDQX5Uvm!m'D&]{@Vr?G;\2?XhbC:Qa#9#eMLN\}x3?JR3.2zr~v)gYF^8\:8>:XfB:Ww75N/emt9Yj[bQMNCWwW\J?N,nvH.<2\.r~w]*e~vgak)X"v8H`MH/7"2E`,^k@n<vE-wD3g9JWPy;CrY*.Kd2_D])=><D?YhBaSua5hW%{2]_FVXzb9`8FH^b[X3jzVER&:jw2<=c38=>L/zBq`}C6tT*cCSVC^c]-L}&/
moneycontrol.jwt.expiration=86400000

# actuator
management.endpoint.health.show-details=always
management.endpoints.web.exposure.include=*
info.app.name=@project.name@
info.app.description=@project.description@
info.app.version=@project.version@
info.app.encoding=@project.build.sourceEncoding@
info.app.java.version=@java.version@
```

**Atribua as seguinte modificações no "application.properties" criado.**
* `spring.datasource.username==[usuário do banco de dados]` nome do usuário do banco de dados.
* `spring.datasource.password==[senha do banco de dados]` senha do banco de dados.

### Métodos

As requisições para a API devem seguir os padrões:

| Método   | Descrição                                             |
|:---------|-------------------------------------------------------|
| `GET`    | Retorna informações de um ou mais registros.          |
| `POST`   | Utilizado para criar unm novo registro.               |
| `PUT`    | Atualiza dados de um registro.                        |
| `DELETE` | Remove um registro do sistema.                        |

### Respostas

| Código | Descrição                                                                  |
|:-------|----------------------------------------------------------------------------|
| `200`  | Requisição executada com sucesso.                                          |
| `201`  | Registro cadastrado.                                                        |
| `403`  | Acesso proibido.                                        |
| `404`  | Registro pesquisado não encontrado. 
| `400`  | Campos não válidos para requisição.                                        |
| `500`  | Erro interno no servidor.                                                  |

# Grupo de Recursos

***

## Cadatro de usuário [/user]

Para o cadastro é necessário:

- Nome do usuário.
- E-mail do usuário.
- Senha do usuário.

### Cadastrar [POST]

+ API endpoint
    + `user`
+ Request (/application/json)
    + Body
        ```json
        {
          "name": "Nome do usuário.",
          "email": "Email do usuário.",
          "password": "Senha do usuário."
        }
        ```
+ Response 201 (application/json) <br/>
    ```json
    {
      "id": 1,
      "name": "Ipsum",
      "email": "Ipsum@lorem.com"
    }
    ```
+ Response 400 (application/json) <br/>
    ```json
    {
      "timestamp": "2022-03-07T22:31:55.371+00:00",
      "status": 400,
      "error": "Bad Request",
      "trace": "..."
    }
    ```

## Login de usuário [/auth]

Para o login é necessário:

- E-mail do usuário.
- Senha do usuário.

### Login [POST]

+ API endpoint
    + `auth`
+ Request (/application/json)
    + Body
        ```json
        {
          "email": "Email do usuário. (Ex.: teste@teste.com)",
          "password": "Senha do usuário. (Ex.: 12345)"
        }
        ```
+ Response 200 - Ok (application/json) <br/>
  ```json
    {
       "token": "JWT_TOKEN (Ex.: eyJhbGciOiJIezI1NiJ9.eyJpc3MiOiJBUEkgbW9dZXlDb240cm9sIiwic3ViIjoiOTk5OTk5OTkiLCJpYXQiOjE2NDU5OTk0NTEsImV4cCI6MTY0NjA4NTg1MX0.1YtjnlHeWV5P9kNDV1olxxbCU4mqHxCR90JGfdzg21w)",
       "type": "Bearer"
    }
  ```
+ Response 400 - Bad Request (application/json) <br/>
    ```text
      "Algo deu errado, verifique se o usuário e a senha estão corretos."
    ```

## Receitas [/receitas]

As receitas são todos os ganhos financeiras do usuário.

### Listar [GET]

+ API endpoint
    + `receitas`
+ headers
    + Authorization
        + `"Bearer <Inserir o JWT Token>"`
+ Response 200 - Ok (application/json) <br/>
    ```json
    {
    "content": [
        {
            "description": "Consulta",
            "value": 20.0,
            "date": "2022-06-12",
            "category": "Outras"
        },
        {
            "description": "Consulta",
            "value": 20.0,
            "date": "2022-06-12",
            "category": "Outras"
        },
        {
            "description": "Consulta",
            "value": 20.0,
            "date": "2022-06-12",
            "category": "Outras"
        }
    ],
    "pageable": {
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 10,
        "paged": true,
        "unpaged": false
    },
    "totalElements": 3,
    "last": true,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 3,
    "first": true,
    "empty": false
    }
    ```

### Cadastrar [POST]

+ API endpoint
    + `receitas`
+ headers
    + Authorization
        + `"Bearer <Inserir o JWT Token>"`
+ Request (/application/json)
    + Body
        ```json
        {
          "description": "Descrição da receita.",
          "value": "Valor da receita (Ex.: 1200.55).",
          "date": "Data da receita (Ex.: 2022-06-10).",
          "type": "Tipo da receita (Variável ou fixa)",
          "category": "Categoria da receita (Aluguel, proventos, etc)"
        }
        ```
+ Response 201 - Created (application/json) <br/>
    ```json
    {
      "description": "Descrição da receita",
      "value": 1200.55,
      "date": "2022-06-10",
      "category": "Outras"
    }
    ```
+ Response 400 - Bad request (application/json) <br/>
    ```json
    {
      "timestamp": "2022-03-07T22:31:55.371+00:00",
      "status": 400,
      "error": "Bad Request",
      "trace": "..."
    }
    ```

### Buscar por Ano e Mês [GET]

+ API endopint
    + `receitas/{ano}/{mes}`
+ headers
    + Authorization
        + `"Bearer <Inserir o JWT Token>"`
+ Response 200 - Ok (application/json) <br/>
    ```json
    {
    "content": [
        {
            "description": "Consulta",
            "value": 20.0,
            "date": "2022-06-12",
            "category": "Outras"
        },
        {
            "description": "Consulta",
            "value": 20.0,
            "date": "2022-06-12",
            "category": "Outras"
        }
    ],
    "pageable": {
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 10,
        "paged": true,
        "unpaged": false
    },
    "totalElements": 2,
    "last": true,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 2,
    "first": true,
    "empty": false
    }
    ```

### Detalhar [GET]

+ API endopint
    + `receitas/{id}`
+ headers
    + Authorization
        + `"Bearer <Inserir o JWT Token>"`
+ Response 200 - Ok (application/json) <br/>
    ```json
    {
      "description": "Descrição da receita cadastrada.",
      "value": "Valor da receita cadastrada.",
      "date": "data da receita cadastrada.",
      "category": "Categoria da receita"
    }
    ```
+ Response 404 - Not found (application/json) <br/>
  O body da resposta é retornado vazio.

### Editar [PUT]

+ API endopint
    + `receitas/{id}`
+ headers
    + Authorization
        + `"Bearer <Inserir o JWT Token>"`
+ Request (/application/json)
    + Body
      ```json
      {
        "description": "Descrição da receita.",
        "value": "Valor da receita. (Ex.: 1200.55).",
        "date": "Data da receita (Ex.: 2022-01-13).",
        "type": "tipo da receita"
      }
      ```
+ Response 200 - Ok (application/json) <br/>
    ```json
    {
      "description": "Descrição da receita editada.",
      "value": "Valor da receita editada.",
      "date": "data da receita editada.",
      "type": "tipo da receita"
    }
    ```
+ Response 404 - Not found (application/json) <br/>
  O body da resposta é retornado vazio. </br></br>

+ Response 400 - Bad request (application/json) <br/>
    ```json
    {
      "timestamp": "2022-03-07T22:31:55.371+00:00",
      "status": 400,
      "error": "Bad Request",
      "trace": "..."
    }

### Remover [DELETE]

+ API endopint
    + `receitas/{id}`
+ headers
    + Authorization
        + `"Bearer <Inserir o JWT Token>"`
+ Response 200 (application/json) <br/>
    ```json
    {
      "success": "Receita removida com sucesso!"
    }
    ```
+ Response 404 (application/json) <br/>
  O body da resposta é retornado vazio.

## Despesas [/despesas]

As despesas são todos os gastos com aplicações financeiras ou qualquer outro 
custo. </br>
+ Esse endpoint segue o mesmo padrão do de receitas com a diferença somente do 
caminho.

Ex.: </br>Para cadastrar utiliza-se o método POST em /despesas </br>
     Para retornar despesas em um determinado mês utiliza-se o GET em 
/despesas/{ano}/{mês}

## Resumo Mensal [/resumo/{ano}/{mês}]

O resumo do mês contem as seguintes informações:

- Valor total gasto no mês em cada uma das categorias.
- Valor total das despesas no mês.
- Valor total das receitas no mês.
- Saldo final no mês.

### Listar [GET]

+ API endopint
    + `despesas/{ano}/{mes}`
+ headers
    + Authorization
        + `"Bearer <Inserir o JWT Token>"`
+ Response 200 - Ok (application/json) <br/>
    ```json
    {
      "DespesaPorCategoria": {
             "Outras": 0.0,
             "Transporte": 0.0,
             "Alimentação": 300.0,
             "Moradia": 1000.0,
             "Educação": 0.0,
             "Imprevistos": 0.0,
             "Saúde": 0.0,
             "Lazer": 0.0
      },
      "TotalGeral": {
             "TotalDespesas": 1300.0,
             "TotalReceitas": 2000.0,
             "SaldoPeriodo": 700.0
      }
    }
    ```

# Contribuições

Dúvidas, sugestões e críticas são bem vindas. Contate-me através do e-mail: 
<a href="mailto:Leonardops91@gmail.com">Leonardops91@gmail.com</a>

# Licença

Desenvolvido por <a href="https://www.linkedin. com/in/leonardosouza-dev/">@Leonardops91</a>.