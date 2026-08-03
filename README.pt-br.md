<div align="center">
  <a href="README.md">🇺🇸 English</a> | <b>🇧🇷 Português</b>
</div>

---

# 🛒 Sistema de Gestão Comercial (ProdutosWeb2)

<div align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-3.5.6-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img alt="Thymeleaf" src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white" />
  <img alt="H2 Database" src="https://img.shields.io/badge/H2_Database-blue?style=for-the-badge&logo=database&logoColor=white" />
</div>

<br>

Este é um projeto acadêmico de **Gestão Comercial** desenvolvido com o ecossistema Spring (Boot, MVC, Data JPA e Security). O sistema permite o gerenciamento completo de **Pessoas (Físicas e Jurídicas)**, **Produtos** e o registro de **Vendas** (carrinho de compras).

## 🚀 Funcionalidades

- **Autenticação e Autorização**: Sistema de login seguro utilizando Spring Security.
- **Gestão de Pessoas**: Cadastro diferenciado para Pessoas Físicas (CPF) e Pessoas Jurídicas (CNPJ).
- **Gestão de Produtos**: Controle de catálogo de produtos disponíveis para venda.
- **Gestão de Vendas**: Funcionalidade de carrinho e processamento de vendas vinculadas aos clientes (pessoas) e produtos.

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.6**
  - Spring MVC
  - Spring Data JPA
  - Spring Security
- **Thymeleaf** (Motor de templates para as views HTML)
- **H2 Database** (Banco de dados em memória/arquivos para desenvolvimento rápido)
- **Maven** (Gerenciador de dependências)

## ⚙️ Como executar o projeto localmente

Siga os passos abaixo para rodar o projeto em sua máquina:

1. **Clone o repositório** (ou extraia os arquivos do projeto) e navegue até a pasta raiz:
   ```bash
   cd ProjetoWEB2-master
   ```

2. **Execute o projeto utilizando o Maven Wrapper**:
   Não é necessário ter o Maven instalado globalmente, basta utilizar os scripts inclusos.
   
   No **Linux/Mac**:
   ```bash
   ./mvnw spring-boot:run
   ```
   No **Windows**:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

3. **Acesse a aplicação no navegador**:
   Após a inicialização do Spring Boot, acesse:
   [http://localhost:8080](http://localhost:8080)

## 🔐 Acesso Padrão (Login)

Ao iniciar a aplicação pela primeira vez, o banco de dados é populado automaticamente com um usuário administrador padrão:

- **Usuário:** `admin`
- **Senha:** `admin`

*(Você pode conferir essa lógica na classe `DataInitializer.java`)*.

---
Desenvolvido por Bruno Licon.
