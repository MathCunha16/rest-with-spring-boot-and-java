# Rest with Spring Boot and Java

Este repositório contém o código-fonte de um projeto de API RESTful desenvolvido com Spring Boot e Java. O projeto foi baseado no curso do professor **Leandro Costa**, mas foi aprimorado com modificações e implementações próprias, focando nas melhores práticas de desenvolvimento e nas tecnologias mais atuais do ecossistema Java.

## Tecnologias Utilizadas

A arquitetura do projeto foi construída utilizando um conjunto de tecnologias modernas e consolidadas no mercado, garantindo performance, segurança e manutenibilidade.

### Backend

* **Java 17:** Utiliza a versão de Suporte de Longo Prazo (LTS) mais recente do Java, aproveitando seus novos recursos de linguagem, melhorias de performance e o ecossistema maduro da plataforma.
* **Spring Boot 3:** É o núcleo do projeto. Facilita a criação de aplicações Spring autônomas e prontas para produção com o mínimo de configuração. Ele gerencia o ciclo de vida da aplicação, as dependências e fornece um servidor web embutido (Tomcat).
* **Spring Web MVC:** Framework utilizado para a construção da camada web da aplicação. Ele fornece a arquitetura MVC (Model-View-Controller) e facilita a criação de endpoints RESTful.
* **Spring Data JPA:** Simplifica a camada de persistência de dados. Ele abstrai a complexidade do acesso a bancos de dados relacionais, utilizando o **Hibernate** como implementação padrão do provedor de persistência JPA.
* **Flyway:** Ferramenta de migração de banco de dados que permite o versionamento do schema do banco de dados de forma automática e segura, garantindo consistência entre ambientes.

### Banco de Dados

* **MySQL:** Um dos sistemas de gerenciamento de banco de dados relacional mais populares do mundo, escolhido pela sua robustez, confiabilidade e performance.

### Mapeamento, Serialização e Documentação

* **Dozer:** Biblioteca utilizada para o mapeamento de objetos (Object Mapping), convertendo Entidades JPA (Model) em VOs (Value Objects) ou DTOs, ajudando a desacoplar as camadas da aplicação.
* **Jackson:** Biblioteca padrão do Spring Boot para serializar objetos Java em JSON e desserializar JSON em objetos Java. O projeto está configurado para suportar negociação de conteúdo em **XML** e **YAML** também.
* **Springdoc OpenAPI (Swagger):** Gera automaticamente a documentação da API a partir do código, criando uma interface de usuário (Swagger UI) interativa para visualizar e testar os endpoints.

### Testes

* **REST Assured:** Biblioteca poderosa para testes de integração de APIs REST, validando endpoints, códigos de status e corpos de resposta.
* **JUnit 5:** Principal plataforma de testes para o ecossistema Java, utilizada para os testes unitários e de integração.
* **Mockito:** Framework de mocking para isolar componentes em testes unitários, simulando o comportamento de suas dependências.
* **Testcontainers:** Biblioteca que fornece instâncias Docker descartáveis de bancos de dados ou outros serviços para testes de integração, garantindo um ambiente fiel ao de produção.

### DevOps, Cloud & Orquestração

* **Docker & Docker Compose:** Utilizados para a containerização da aplicação e seus serviços. O Docker empacota a aplicação em uma imagem portátil, e o Docker Compose orquestra os contêineres em ambiente de desenvolvimento.
* **Kubernetes (k8s):** Embora os manifestos não estejam neste repositório, a aplicação foi projetada para ser "Cloud Native", ou seja, pronta para ser implantada em um cluster Kubernetes. O Kubernetes é um orquestrador de contêineres que automatiza a implantação, o escalonamento e a gestão de aplicações em contêineres, proporcionando alta disponibilidade e resiliência.
* **AWS / GCP:** A arquitetura do projeto permite que ele seja implantado nos principais provedores de nuvem, como Amazon Web Services (AWS) e Google Cloud Platform (GCP). A aplicação pode ser executada em serviços como **AWS Elastic Kubernetes Service (EKS)** ou **Google Kubernetes Engine (GKE)**, utilizando bancos de dados gerenciados como **AWS RDS** ou **Google Cloud SQL** para máxima escalabilidade e performance.
* **GitHub Actions:** Plataforma de Integração Contínua e Entrega Contínua (CI/CD) do GitHub, configurada para automatizar o processo de build e teste a cada commit, garantindo a qualidade do código.
* **Maven:** Ferramenta de automação de build e gerenciamento de dependências, responsável por compilar o código, executar os testes e empacotar a aplicação.

## Autor

* **[Cunha]** - *Desenvolvimento e modificações* - [GitHub](https://github.com/MathCunha16)

## Agradecimentos

* Créditos ao professor **Leandro Costa** pela criação do curso na Udemy - [GitHub](https://github.com/leandrocgsi)
