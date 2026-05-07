# 🥊 Boxing Gym API

API REST para gestión de gimnasio de boxeo con recomendaciones de entrenamiento personalizadas mediante Inteligencia Artificial.

[![Deploy to Cloud Run](https://github.com/Pabcermo/boxing-gym-api/actions/workflows/deploy.yml/badge.svg)](https://github.com/Pabcermo/boxing-gym-api/actions/workflows/deploy.yml)

## Stack Tecnológico

| Capa | Tecnología |
|------|------------|
| Backend | Java 21 · Spring Boot 3 |
| Base de Datos | PostgreSQL 18 (Cloud SQL) |
| ORM | JPA / Hibernate |
| IA | OpenAI GPT-4o mini |
| Cloud | Google Cloud Run |
| Secretos | GCP Secret Manager |
| Contenedor | Docker multi-stage build |
| CI/CD | GitHub Actions |

---

## Arquitectura

```
┌─────────────────────────────────────────────┐
│              GitHub Actions CI/CD            │
│         push → build → deploy               │
└───────────────────┬─────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────┐
│              Google Cloud Run                │
│                                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │Controller│→ │ Service  │→ │Repository│  │
│  └──────────┘  └──────────┘  └──────────┘  │
│                     │                       │
│              ┌──────┴──────┐                │
│              ▼             ▼                │
│         OpenAI API    Cloud SQL             │
│         (GPT-4o)     (PostgreSQL)           │
└─────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────┐
│           GCP Secret Manager                │
│   DB_URL · DB_USER · DB_PASS · OPENAI_KEY   │
└─────────────────────────────────────────────┘
```

---

## Modelo de Datos

```
users
├── id, name, email
└── level (BEGINNER / INTERMEDIATE / ADVANCED)

sessions
├── id, date, capacity, availableSpots
├── coach
└── type (BOXING / CARDIO / SPARRING)

bookings
├── id, status (CONFIRMED / CANCELLED)
├── user_id → users
└── session_id → sessions

training_plans
├── id, goal, generated_plan
└── user_id → users
```

---

## Endpoints

### Usuarios
```
POST   /users              Registrar usuario
GET    /users/{id}         Obtener usuario
```

### Sesiones
```
POST   /sessions           Crear sesión
GET    /sessions           Ver sesiones disponibles
```

### Reservas
```
POST   /bookings                  Crear reserva
GET    /users/{id}/bookings       Ver reservas del usuario
PATCH  /bookings/{id}/cancel      Cancelar reserva
```

### IA
```
POST   /training/recommendation   Generar plan con OpenAI
```

### Health
```
GET    /actuator/health    Estado del servicio
```

---

## CI/CD — GitHub Actions

Cada `push` a `main` ejecuta automáticamente:

```
git push origin main
        ↓
GitHub Actions
        ↓
Autenticación con GCP (Workload Identity)
        ↓
Build imagen Docker
        ↓
Push a Container Registry
        ↓
Deploy automático en Cloud Run
        ↓
API actualizada en producción ✅
```

Sin claves JSON expuestas. Autenticación mediante **Workload Identity Federation**.

---

## Seguridad

- Credenciales gestionadas con **GCP Secret Manager**
- Autenticación CI/CD con **Workload Identity Federation** (sin claves JSON)
- Variables de entorno inyectadas en runtime, nunca en el código
- `.env` y `target/` excluidos del repositorio

---

## Ejecutar en local

### Requisitos
- Docker Desktop
- Java 21
- Maven

### Levantar con Docker Compose

```bash
# Clonar el repositorio
git clone https://github.com/Pabcermo/boxing-gym-api.git
cd boxing-gym-api

# Crear archivo de variables de entorno
cp .env.example .env
# Edita .env con tus credenciales

# Levantar app + PostgreSQL
docker compose up --build
```

La API estará disponible en `http://localhost:8080`

### Variables de entorno necesarias

```bash
# .env (nunca subir al repo)
DB_URL=jdbc:postgresql://localhost:5432/boxinggym
DB_USER=postgres
DB_PASS=tu_password
OPENAI_API_KEY=sk-proj-...
```

---

## Despliegue en GCP

### Requisitos
- Google Cloud CLI instalado
- Proyecto GCP con Cloud Run y Cloud SQL activos

### Despliegue manual

```bash
# Linux / Mac / Git Bash
chmod +x deploy.sh
./deploy.sh

# Windows PowerShell
.\deploy.ps1
```

El script gestiona automáticamente:
- Habilitación de APIs
- Creación de secretos en Secret Manager
- Permisos IAM
- Build y push de imagen Docker
- Despliegue en Cloud Run

### Despliegue automático

Cualquier `push` a `main` despliega automáticamente via GitHub Actions.

---

## Estructura del Proyecto

```
boxing-gym-api/
├── .github/
│   └── workflows/
│       └── deploy.yml          ← CI/CD pipeline
├── src/main/java/com/boxinggym/api/
│   ├── controller/             ← Endpoints REST
│   ├── service/                ← Lógica de negocio
│   ├── repository/             ← Acceso a datos (JPA)
│   ├── model/                  ← Entidades JPA
│   ├── dto/                    ← Objetos de transferencia
│   └── config/                 ← OpenAI + Exception Handler
├── src/main/resources/
│   └── application.yml         ← Configuración
├── Dockerfile                  ← Multi-stage build
├── docker-compose.yml          ← Entorno local
├── deploy.sh                   ← Script despliegue (Linux/Mac/Git Bash)
├── deploy.ps1                  ← Script despliegue (PowerShell)
└── pom.xml                     ← Dependencias Maven
```

---

## Producción

API desplegada en Google Cloud Run:

```
https://boxing-gym-api-961164869972.europe-west1.run.app
```

Health check:
```
https://boxing-gym-api-961164869972.europe-west1.run.app/actuator/health
```

---

## Autor

**Pablo Cerro Montoya**  
Backend Engineer · Java · Spring Boot · GCP  
[LinkedIn](https://linkedin.com/in/pablo-cerro) · [GitHub](https://github.com/Pabcermo)
