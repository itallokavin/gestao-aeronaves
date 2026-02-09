# Gestão de Aeronaves

Este projeto é uma aplicação *full-stack* desenvolvida para o gerenciamento de uma frota de aeronaves, permitindo controle de inventário e visualização de estatísticas.

## Funcionamento

O sistema permite realizar o CRUD completo de aeronaves (Criar, Ler, Atualizar e Deletar). Além disso, oferece um *dashboard* com métricas importantes, como:
- Quantidade de aeronaves por década de fabricação.
- Aeronaves cadastradas na última semana.
- Contagem de aeronaves não vendidas (disponíveis).

A interface foi projetada para ser responsiva e intuitiva, com validações em tempo real e feedback visual para o usuário.

## Arquitetura da Solução

A solução foi construída utilizando uma arquitetura em camadas e microsserviços containerizados:

1.  **Backend (API REST)**:
    - Desenvolvido em **Java 17** com **Spring Boot**.
    - Segue o padrão MVC (Model-View-Controller) e Service Layer.
    - Utiliza **Spring Data JPA** para persistência e **Lombok** para redução de código boilerplate.
    - Banco de dados **PostgreSQL**.

2.  **Frontend (SPA)**:
    - Desenvolvido em **AngularJS (1.x)** seguindo uma arquitetura baseada em componentes.
    - Utiliza **Angular Material** para UI/UX.
    - Comunicação com a API via serviço injetável (`AircraftService`).

3.  **Infraestrutura**:
    - **Docker Compose** orquestra os três contêineres: banco de dados, aplicação backend e o servidor web (Nginx) que entrega o frontend.

## Como executar o projeto

Para rodar a aplicação, você precisa apenas ter o **Docker** e **Docker Compose** instalados.

1.  Na raiz do projeto, execute:
    ```bash
    docker-compose up --build
    ```

2.  Aguarde os contêineres subirem. O sistema estará disponível em:
    - **Aplicação Web**: [http://localhost:80](http://localhost:80)
    - **API Documentation/Endpoints**: [http://localhost:8080/aeronaves](http://localhost:8080/aeronaves)
