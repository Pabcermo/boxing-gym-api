# 🥊 Boxing Gym API

> REST API para gestión de gimnasio de boxeo con recomendaciones de entrenamiento generadas por IA y arquitectura event-driven con Apache Kafka.
> Desplegada en **Google Cloud Run** con pipeline **CI/CD automatizado via GitHub Actions**.

[![Deploy to Cloud Run](https://github.com/Pabcermo/boxing-gym-api/actions/workflows/deploy.yml/badge.svg)](https://github.com/Pabcermo/boxing-gym-api/actions/workflows/deploy.yml)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![GCP](https://img.shields.io/badge/GCP-Cloud%20Run-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-3.x-black)

---

## 📋 Tabla de contenidos

- [Descripción](#-descripción)
- [Arquitectura](#-arquitectura)
- [Stack tecnológico](#-stack-tecnológico)
- [Modelo de datos](#-modelo-de-datos)
- [Endpoints](#-endpoints-api)
- [Eventos Kafka](#-eventos-kafka)
- [Ejecutar en local](#-ejecutar-en-local)
- [Despliegue en GCP](#-despliegue-en-gcp)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Seguridad](#-seguridad)

---

## 📖 Descripción

API REST desarrollada con **Java 21 y Spring Boot 3** que permite:

- Gestión de usuarios del gimnasio con niveles de experiencia
- Consulta y reserva de sesiones de boxeo en tiempo real
- Cancelación de reservas con devolución automática de cupos
- Generación de planes de entrenamiento personalizados mediante **OpenAI GPT-4o**
- Sistema de notificaciones asíncrono mediante **Apache Kafka**

---

## 🏗 Arquitectura
┌─────────────────────────────────────────────────────────┐
│                  Cliente (Postman / App)                  │
└─────────────────────┬───────────────────────────────────┘
│ HTTP REST
┌─────────────────────▼───────────────────────────────────┐
│                   Spring Boot API                         │
│                                                           │
│  Controller → Service → Repository → PostgreSQL           │
│                  │                                        │
│                  │ Publica eventos                        │
│                  ▼                                        │
│            Apache Kafka                                   │
│          (booking-events)                                 │
│                  │                                        │
│                  │ Consume eventos                        │
│                  ▼                                        │
│       NotificationConsumer → PostgreSQL                   │
│                                                           │
│  TrainingService → OpenAI GPT-4o                         │
└───────────────────────────────────────────────────────────┘
│
┌─────────────────────▼───────────────────────────────────┐
│                Google Cloud Platform                      │
│                                                           │
│   Cloud Run  │  Cloud SQL  │  Secret Manager             │
└─────────────────────────────────────────────────────────┘

### Capas de la aplicación
controller/   →  Recibe peticiones HTTP, valida DTOs
service/      →  Lógica de negocio, publica eventos Kafka
repository/   →  Acceso a datos con Spring Data JPA
model/        →  Entidades JPA (tablas PostgreSQL)
dto/          →  Objetos de transferencia de datos
event/        →  Eventos Kafka (BookingEvent)
config/       →  Configuración de OpenAI, Kafka, errores

---

## 🛠 Stack tecnológico

| Categoría | Tecnología |
|-----------|-----------|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.x |
| Base de datos | PostgreSQL 18 (Cloud SQL) |
| ORM | Spring Data JPA / Hibernate |
| Mensajería | Apache Kafka |
| IA | OpenAI GPT-4o Mini |
| Cloud | Google Cloud Run |
| Secretos | GCP Secret Manager |
| Contenedores | Docker (multi-stage build) |
| CI/CD | GitHub Actions |
| Auth GCP | Workload Identity Federation |

---

## 🗄 Modelo de datos
users
├── id (PK)
├── name
├── email (UNIQUE)
├── level (BEGINNER, INTERMEDIATE, ADVANCED)
└── created_at
sessions
├── id (PK)
├── date
├── capacity
├── available_spots
├── coach
└── type (BOXING, CARDIO, SPARRING)
bookings
├── id (PK)
├── user_id (FK → users)
├── session_id (FK → sessions)
├── status (CONFIRMED, CANCELLED)
├── created_at
└── UNIQUE(user_id, session_id)
training_plans
├── id (PK)
├── user_id (FK → users)
├── goal
├── generated_plan (TEXT)
└── created_at
notifications
├── id (PK)
├── user_id (FK → users)
├── message
├── type (BOOKING_CONFIRMED, BOOKING_CANCELLED)
├── is_read
└── created_at

---

## 🔌 Endpoints API

### Usuarios
POST   /users              Crear usuario
GET    /users/{id}         Obtener usuario por ID

### Sesiones
POST   /sessions           Crear sesión
GET    /sessions           Listar sesiones disponibles

### Reservas
POST   /bookings                  Crear reserva
GET    /users/{id}/bookings       Ver reservas de un usuario
PATCH  /bookings/{id}/cancel      Cancelar reserva

### Entrenamiento IA
POST   /training/recommendation   Generar plan con OpenAI GPT-4o

### Notificaciones
GET    /notifications/{userId}          Todas las notificaciones
GET    /notifications/{userId}/unread   Solo no leídas
PATCH  /notifications/{id}/read         Marcar como leída

### Health Check
GET    /actuator/health    Estado de la aplicación

---

## 📨 Eventos Kafka

Cada vez que se crea o cancela una reserva se publica un evento en Kafka:

**Topic:** `booking-events`
**Key:** `userId` (garantiza orden por usuario)
**Particiones:** 3

```json
{
  "bookingId": 1,
  "userId": 1,
  "userName": "Carlos López",
  "sessionId": 1,
  "sessionDate": "2026-06-01T10:00:00",
  "sessionType": "BOXING",
  "eventType": "BOOKING_CONFIRMED",
  "occurredAt": "2026-05-08T12:00:00"
}
```

El consumer `BookingEventConsumer` escucha el topic y guarda
una notificación en PostgreSQL automáticamente.

---

## 🚀 Ejecutar en local

### Requisitos
- Docker Desktop
- Git

### Pasos

**1. Clona el repositorio**
```bash
git clone https://github.com/Pabcermo/boxing-gym-api.git
cd boxing-gym-api
```

**2. Crea el archivo `.env`**
OPENAI_API_KEY=sk-proj-xxxxxxxxxxxxxxxxxxxxxxxx

**3. Levanta todos los servicios**
```bash
docker compose up --build
```

Esto levanta automáticamente:
- 🐘 PostgreSQL en `localhost:5432`
- 📨 Kafka en `localhost:9092`
- 🌀 Zookeeper en `localhost:2181`
- 🚀 API en `localhost:8080`

**4. Verifica que está funcionando**
```bash
curl http://localhost:8080/actuator/health
# {"status":"UP"}
```

---

## ☁️ Despliegue en GCP(https://boxing-gym-api-961164869972.europe-west1.run.app)

La aplicación se despliega en **Google Cloud Run** conectada a **Cloud SQL PostgreSQL**.
Las credenciales se gestionan con **Secret Manager**.

Variables configuradas como secretos en GCP:
DB_URL          →  URL de conexión a Cloud SQL via socket factory
DB_USER         →  Usuario PostgreSQL
DB_PASS         →  Contraseña PostgreSQL
OPENAI_API_KEY  →  API Key de OpenAI

Para desplegar manualmente:
```bash
./deploy.sh
```

---

## 🔄 CI/CD Pipeline

Cada `push` a `main` activa automáticamente el pipeline de GitHub Actions:
git push origin main
↓
GitHub Actions se activa
↓
Autenticación con GCP (Workload Identity Federation)
↓
Build imagen Docker
↓
Push a Container Registry (gcr.io)
↓
Deploy automático en Cloud Run
↓
API actualizada en producción ✅

### Autenticación sin claves JSON

Se usa **Workload Identity Federation** para que GitHub Actions
se autentique con GCP sin guardar claves JSON en ningún sitio.

Secretos configurados en GitHub:
WIF_PROVIDER          →  Workload Identity Provider de GCP
WIF_SERVICE_ACCOUNT   →  Cuenta de servicio con permisos de despliegue

---

## 🔒 Seguridad

- ✅ Credenciales gestionadas con **GCP Secret Manager**
- ✅ Sin claves hardcodeadas en el código ni en variables de entorno del repo
- ✅ Autenticación GCP sin claves JSON via **Workload Identity Federation**
- ✅ `.env` en `.gitignore`, nunca sube al repositorio
- ✅ Docker multi-stage build, imagen final sin código fuente ni Maven