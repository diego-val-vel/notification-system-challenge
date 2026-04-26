# Notification System

A full-stack notification system built with Spring Boot and React, designed with scalability, clean architecture, and extensibility in mind.

---

## Demo Video

Watch the full end-to-end demo here:

https://drive.google.com/file/d/1EZA8sIXKGrSkFuq8OqiP3LH_LY0myOlp/view?usp=sharing

---

## Overview

This application allows sending categorized notifications to users subscribed to specific categories and delivery channels (Email, SMS, Push).

It ensures:

- Reliable delivery with retry logic
- Idempotency to avoid duplicate sends
- Clear visibility through delivery logs
- Clean separation of concerns
- Easy extensibility for new channels

---

## Key Features

- Send notifications by category
- Dynamic user filtering based on subscriptions
- Multi-channel delivery (Email, SMS, Push)
- Retry mechanism with configurable attempts
- Failure simulation per channel
- Idempotency control per user + channel
- Full delivery logs:
  - status
  - attempts
  - error messages
- Log history ordered from newest to oldest
- Real-time UI updates after sending
- Health checks and monitoring
- Swagger API documentation
- Fully Dockerized setup (one-command run)

---

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway (migrations & seeders)
- Spring Actuator (health checks)
- OpenAPI / Swagger

---

### Frontend

- React (Vite)
- Fetch API
- Component-based architecture

---

### Infrastructure

- Docker
- Docker Compose

---

## Architecture

The backend follows a clean layered architecture:

- Controllers = HTTP entry points
- Services = Business logic
- Repositories = Data access layer
- DTOs = API contracts
- Strategies = Notification delivery abstraction

---

## Design Patterns

### Strategy Pattern

Used to dynamically select the notification channel:

- EmailNotificationStrategy
- SmsNotificationStrategy
- PushNotificationStrategy

This allows adding new channels with minimal changes.

---

### Retry Mechanism

- Configurable via properties
- Applies per notification attempt
- Ensures fault tolerance

---

### Idempotency

Each notification is uniquely identified by:

```
messageId + userId + channel
```

Prevents duplicate delivery.

---

## Database

- PostgreSQL
- Managed with Flyway migrations
- Includes:
  - catalogs (categories, channels)
  - messages
  - notification logs
- Uses:
  - foreign keys
  - indexing
  - proper data types

---

## Project Structure

```
backend/
  controllers/
  services/
  repositories/
  models/
  dtos/
  strategies/
  config/
  exceptions/

frontend/
  api/
  components/
  constants/
  utils/
```

---

## Running the Project

### One command setup

```
docker compose up -d
```

This will start:

- PostgreSQL database
- Spring Boot backend
- React frontend

---

### Important note

The startup process may take a short time on the first run.

This is expected because the backend container performs a full Maven build before starting the application.

The frontend is intentionally configured to wait until the backend is fully available. This prevents failed API calls or inconsistent behavior that could occur if the UI loads before the backend is ready.

Once all services are healthy, the application will be fully accessible.

---

### Stop the project

```
docker compose down
```

This will:

- Stop all containers
- Remove the network
- Keep persisted database data (volume is preserved)

---

### Optional: full reset (including database)

```
docker compose down -v
```

This will:

- Remove containers
- Remove volumes
- Delete all database data

---

## Access URLs

### Frontend

```
http://localhost:5174
```

---

### Backend API

```
http://localhost:8080
```

---

### Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

### Health Check

```
http://localhost:8080/actuator/health
```

---

## API Endpoints

### 1. Create Notification

```
curl -X POST http://localhost:8080/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "categoryCode": "SPORTS",
    "message": "Game tonight at 8 PM"
  }'
```

---

### 2. Get Notification Logs

```
curl -X GET http://localhost:8080/api/notifications/logs
```

Returns logs ordered from newest to oldest.

---

## Frontend Behavior

- Left panel = notification form
- Right panel = delivery logs
- After submitting:
  - Sends POST request
  - Fetches updated logs
  - Displays results immediately

---

## Testing

### Run backend tests

```
mvn test
```

Includes:

- Service tests
- Strategy tests
- Resolver tests
- Log history tests

---

## Configuration

### Retry configuration

```
notification.retry.max-attempts=3
```

---

### Failure simulation

```
notification.failure.email-rate=0.20
notification.failure.sms-rate=0.30
notification.failure.push-rate=0.10
```

These values represent the probability of failure for each notification channel.

- Values closer to **0** mean the channel will **fail less frequently**.
- Values closer to **1** mean the channel will **fail more frequently**.

Examples:

- `0.10` = ~10% failure rate (mostly successful)
- `0.50` = ~50% failure rate (unstable)
- `0.90` = ~90% failure rate (almost always fails)

This configuration is used to simulate real-world unreliable providers and validate retry behavior.


---

## CORS Configuration

Frontend and backend communicate via:

```
http://localhost:5174 = http://localhost:8080
```

CORS is configured in:

```
/notification-system-challenge/backend/src/main/java/com/notifications/config/WebConfig.java
```

---

## Scalability

The system is designed to:

- Add new notification channels easily
- Extend retry logic
- Replace mock users with real persistence
- Scale independently per service

---

## Manual Container Access

The project is designed to run with:

```
docker compose up -d
```

However, each container can also be accessed manually if needed for debugging, compilation, or direct execution.

---

### Backend container

```
docker exec -it notification-system-backend bash
```

---

### Frontend container

```
docker exec -it notification-system-frontend sh
```

---

## Manual Backend Commands

Inside the backend container:

### Clean build files

```
mvn clean
```

### Compile and package the application

```
mvn clean package
```

### Run the packaged application

```
java -jar target/notification-system-0.0.1-SNAPSHOT.jar
```

---

## Manual Frontend Commands

Inside the frontend container:

### Enter the React application folder

```
cd app/
```

### Run the Vite development server

```
npm run dev
```

---

## Manual Database Access

The PostgreSQL database can be accessed directly from its Docker container.

### Enter PostgreSQL container

```
docker exec -it notification-system-db sh
```

### Connect to the database

```
psql -U notifications_user -d notifications_db
```

---

## Database Table Inspection

### Flyway schema history

Describe table:

```
\d flyway_schema_history
```

Select records:

```
SELECT * FROM flyway_schema_history;
```

---

### Categories

Describe table:

```
\d categories
```

Select records:

```
SELECT * FROM categories;
```

---

### Notification channels

Describe table:

```
\d notification_channels
```

Select records:

```
SELECT * FROM notification_channels;
```

---

### Messages

Describe table:

```
\d messages
```

Select records:

```
SELECT * FROM messages;
```

---

### Notification logs

Describe table:

```
\d notification_logs
```

Select records:

```
SELECT * FROM notification_logs;
```

---

### Exit PostgreSQL

```
\q
```

### Exit container

```
exit
```
