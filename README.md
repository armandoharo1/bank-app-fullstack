# 🏦 Devsu Bank – FullStack Application
## Spring Boot (Java 17) + Angular 17

Aplicación bancaria FullStack compuesta por:

- Backend API REST (Spring Boot + PostgreSQL + Docker)
- Frontend SPA (Angular 17 Standalone + Lazy Loading)

Permite la gestión integral de:

- 👤 Clientes
- 💳 Cuentas
- 💰 Movimientos (Crédito / Débito)
- 📊 Reporte de Estado de Cuenta (JSON + PDF descargable)

# 🧱 Arquitectura General del Sistema
### 🔷 Diagrama End-to-End
```
                     ┌─────────────────────────────┐
                     │        Usuario / Browser     │
                     └──────────────┬──────────────┘
                                    │
                                    ▼
                     ┌─────────────────────────────┐
                     │     Angular 17 Frontend     │
                     │  (Standalone + Lazy Load)   │
                     └──────────────┬──────────────┘
                                    │
                           HttpClient (/api)
                                    │
                                    ▼
                     ┌─────────────────────────────┐
                     │        Proxy Angular        │
                     │      proxy.conf.json        │
                     └──────────────┬──────────────┘
                                    │
                                    ▼
                     ┌─────────────────────────────┐
                     │    Spring Boot REST API     │
                     │  Controllers → Services     │
                     │  → Repositories             │
                     └──────────────┬──────────────┘
                                    │
                                    ▼
                     ┌─────────────────────────────┐
                     │        PostgreSQL DB        │
                     └─────────────────────────────┘
```
# 📦 Estructura del Repositorio

```
devsu-bank-fullstack/
│
├── bank-backend/        → Proyecto Spring Boot
│   └── README.md
│
├── front-end/           → Proyecto Angular 17
│   └── README.md
│
└── README.md            → (Este documento principal)
```

# 🚀 Stack Tecnológico Completo
## 🔹 Backend

- Java 17
- Spring Boot 3.5.x
- Spring Data JPA (Hibernate)
- Bean Validation (Jakarta)
- PostgreSQL 16
- Docker & Docker Compose
- JUnit 5 + MockMvc
- Maven

## 🔹 Frontend

- Angular 17
- Standalone Components
- Lazy Loading con loadComponent()
- Reactive Forms
- HttpClient
- Jest (Testing)
- CSS puro (sin frameworks UI)

# 🧠 Arquitectura Backend

```
Controller
   ↓
Service (Reglas de negocio)
   ↓
Repository (JPA)
   ↓
PostgreSQL
```
Incluye:

- Strategy Pattern (Crédito / Débito)
- BusinessException
- GlobalExceptionHandler
- Generación de PDF (ReportePdfGenerator)
- Dockerización completa

📄 Documentación detallada:

````
 👉 Ver bank-backend/README.md
 ````
# 🖥 Arquitectura Frontend
Arquitectura modular con separación por capas:
````
Angular Router
    ↓
LayoutComponent
    ↓
Pages (Lazy Loaded)
    ├── Clientes
    ├── Cuentas
    ├── Movimientos
    └── Reportes
    ↓
Core Layer
    ├── Services (API Calls)
    └── Models (DTO Interfaces)

````

- Consumo de endpoints REST
- Manejo de errores 400
- Normalización de LocalDate
- Conversión Base64 → Blob para PDF

📄 Documentación detallada:
````
👉 Ver front-end/README.md
````
# ⚙️ Ejecución del Proyecto
🔹 Opción Recomendada (Docker Backend + Angular local)
## 1️⃣ Levantar Backend
```bash
cd bank-backend
docker compose up -d --build
```
Backend disponible en:
```bash
http://localhost:8080
```

## 2️⃣ Ejecutar Frontend
```bash
cd front-end
npm install
npm start
```
Frontend disponible en:
```bash
http://localhost:4200
```
# 📊 Funcionalidades Implementadas

| Módulo | Estado | |
| :--- | :---: | :--- |
| **CRUD Clientes** | ✅ |  |
| **CRUD Cuentas** | ✅ |  |
| **CRUD Movimientos** | ✅ | |
| **Reporte JSON** | ✅ |  |
| **Reporte PDF** | ✅ | |
| **Validaciones Backend** | ✅ | |
| **Tests Backend** | ✅ | |
| **Tests Frontend** | ✅ | |
| **Docker Backend** | ✅ | |
---

# 🧪 Testing
## Backend
```bash
./mvnw test
````
## Frontend
```bash
npm test
````
Todos los test unitarios pasan correctamente.

# 🔐 Reglas de Negocio Clave
- Créditos → valores positivos
- Débitos → valores negativos
- Control de saldo disponible
- Límite diario configurable
- Validaciones con @Valid
- Manejo estructurado de errores

# 📸 Evidencias
Las capturas del frontend se encuentran en:
````
front-end/docs/screenshots/
````

# 📌 Entregables Cumplidos
- Repositorio público en GitHub
- Backend dockerizado
- Base de datos PostgreSQL
- Frontend Angular sin frameworks UI
- Tests unitarios backend y frontend
- Reporte PDF
- Arquitectura por capas
- Documentación técnica

# ✍️ Autor

| **Armando Haro** | Data Engineer • Backend Developer |
| :--- | :--- |
| **Tech Stack** | Java • Spring Boot • Microservices • Kafka • Python •  Pyspark • Azure • Databricks • AWS • Datio • Collibra • Data Factory • SQL • DBT
| **GitHub** | [github.com/armandoharo1](https://github.com/armandoharo1) |
