# 1.RESUMEN DE ENDPOINTS
Usuarios
POST   /users               →  Crear usuario
GET    /users/{id}          →  Ver usuario

Sesiones
POST   /sessions            →  Crear sesión
GET    /sessions            →  Ver sesiones disponibles

Reservas
POST   /bookings            →  Crear reserva
GET    /users/{id}/bookings →  Ver reservas de un usuario
PATCH  /bookings/{id}/cancel →  Cancelar reserva

IA
POST   /training/recommendation →  Generar plan con OpenAI

Health Check (Cloud Run)
GET    /actuator/health     →  Estado de la app

# 2.RESUMEN DE RESPONSABILIDADES
UserService
├── createUser()        → valida email único, guarda usuario
└── getUserById()       → busca o lanza excepción

SessionService
├── createSession()     → crea sesión con cupos
├── getAvailableSessions() → solo futuras con cupos
├── decrementAvailableSpots() → al reservar
└── incrementAvailableSpots() → al cancelar

BookingService
├── createBooking()     → valida todo, descuenta cupo (@Transactional)
├── cancelBooking()     → devuelve cupo (@Transactional)
└── getBookingsByUser() → historial del usuario

TrainingService
├── generatePlan()      → construye prompt, llama OpenAI, guarda
├── buildPrompt()       → personaliza con nivel y objetivo
└── callOpenAI()        → HTTP call y extrae respuesta

# 1. Construir y levantar todo (primera vez tarda por las descargas)
docker compose up --build

# Verás algo así cuando esté listo:
# boxinggym-app | Started ApiApplication in 4.123 seconds


# Levantar sin reconstruir (más rápido)
docker compose up

# Levantar en segundo plano
docker compose up -d

# Ver logs de la app
docker compose logs -f app

# Apagar todo
docker compose down

# Apagar y borrar la base de datos (datos incluidos)
docker compose down -v

# Deploy.sh
1. Verifica que estés autenticado en GCP
2. Configura el proyecto correcto
3. Habilita las APIs necesarias
4. Crea los secretos (te pide las claves por consola de forma segura)
5. Configura permisos IAM
6. Construye y sube la imagen
7. Despliega en Cloud Run
8. Muestra la URL final