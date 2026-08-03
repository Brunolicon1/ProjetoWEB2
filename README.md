<div align="center">
  <b>🇺🇸 English</b> | <a href="README.pt-br.md">🇧🇷 Português</a>
</div>

---

# 🛒 Commercial Management System (ProdutosWeb2)

<div align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-3.5.6-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img alt="Thymeleaf" src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white" />
  <img alt="H2 Database" src="https://img.shields.io/badge/H2_Database-blue?style=for-the-badge&logo=database&logoColor=white" />
</div>

<br>

This is an academic **Commercial Management** project developed with the Spring ecosystem (Boot, MVC, Data JPA, and Security). The system allows complete management of **People (Individuals and Corporate Entities)**, **Products**, and the registration of **Sales** (shopping cart).

## 🚀 Features

- **Authentication & Authorization**: Secure login system using Spring Security.
- **People Management**: Differentiated registration for Individuals (CPF) and Corporate Entities (CNPJ).
- **Product Management**: Catalog control of products available for sale.
- **Sales Management**: Shopping cart functionality and sales processing linked to clients (people) and products.

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot 3.5.6**
  - Spring MVC
  - Spring Data JPA
  - Spring Security
- **Thymeleaf** (Template engine for HTML views)
- **H2 Database** (In-memory/file database for rapid development)
- **Maven** (Dependency manager)

## ⚙️ How to run the project locally

Follow the steps below to run the project on your machine:

1. **Clone the repository** (or extract the project files) and navigate to the root folder:
   ```bash
   cd ProjetoWEB2-master
   ```

2. **Run the project using the Maven Wrapper**:
   You do not need to have Maven installed globally, just use the included scripts.
   
   On **Linux/Mac**:
   ```bash
   ./mvnw spring-boot:run
   ```
   On **Windows**:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

3. **Access the application in the browser**:
   After Spring Boot initializes, access:
   [http://localhost:8080](http://localhost:8080)

## 🔐 Default Access (Login)

When starting the application for the first time, the database is automatically populated with a default administrator user:

- **Username:** `admin`
- **Password:** `admin`

*(You can check this logic in the `DataInitializer.java` class)*.

---
Developed by Bruno Licon.
