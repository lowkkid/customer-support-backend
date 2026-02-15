# Customer Support Triage Dashboard — Backend

- [Frontend Repository](https://github.com/lowkkid/customer-support)
- [Live Frontend](https://customer-support.lowkkid.dev/)
- [Live Swagger API](https://customer-support-backend.lowkkid.dev/swagger-ui/index.html#/)

---

## Overview

This is the backend part of the **Customer Support Triage Dashboard** — a lightweight internal tool that helps support leads quickly triage incoming customer messages by automatically categorizing and prioritizing them using AI.

Built with **Spring Boot 4**, **H2 in-memory database**, and **Google Gemini LLM** for intelligent message classification.

## Tech Stack

- **Java 25**, **Spring Boot 4.0.2**
- **Spring Data JPA** + **H2** (in-memory database)
- **Google Gemini 3 Flash** (LLM-based categorization & prioritization)
- **MapStruct** (entity-to-DTO mapping)
- **SpringDoc OpenAPI** (Swagger UI)
- **Docker** (multi-stage build)
- **GitHub Actions** (CI/CD)

## How to Test

The easiest way to explore the API is through the deployed **[Swagger UI](https://customer-support-backend.lowkkid.dev/swagger-ui/index.html#/)** — all endpoints are documented and can be executed directly from the browser.

Alternatively, you can hit the endpoints with any tool you prefer (cURL, Postman, HTTPie, etc.). The base URL is:

```
https://customer-support-backend.lowkkid.dev
```

Quick examples:

```bash
# Get all messages
curl https://customer-support-backend.lowkkid.dev/messages

# Get statistics
curl https://customer-support-backend.lowkkid.dev/stats

# Create a new message (may take ~30-60s due to synchronous LLM call)
curl -X POST https://customer-support-backend.lowkkid.dev/messages \
  -H "Content-Type: application/json" \
  -d '{"customerName": "John Doe", "messageBody": "The app crashes when I upload a PDF."}'

# Mark a message as resolved
curl -X PATCH https://customer-support-backend.lowkkid.dev/messages/1/resolve
```

## How to Run

### Prerequisites

- Java 25+
- Maven 3.8.6+
- Google API key (for Gemini LLM)

### Environment Variables

| Variable | Description |
|----------|-------------|
| `SERVER_PORT` | Application port (e.g. `8080`) |
| `CORS_ALLOWED_ORIGINS` | Comma-separated allowed origins |
| `GOOGLE_API_KEY` | Google Gemini API key |

### Run Locally

```bash
export SERVER_PORT=8080
export CORS_ALLOWED_ORIGINS=http://localhost:3000
export GOOGLE_API_KEY=your-api-key

mvn clean package -DskipTests
java -jar target/customer-support-backend.jar
```

### Run with Docker

```bash
docker build -t customer-support-backend .
docker run -p 8080:8080 \
  -e SERVER_PORT=8080 \
  -e CORS_ALLOWED_ORIGINS=http://localhost:3000 \
  -e GOOGLE_API_KEY=your-api-key \
  customer-support-backend
```

The API will be available at `http://localhost:8080/api/v1/customer-support`.

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/messages` | Create a new support message |
| `GET` | `/messages` | Get paginated messages (with filtering & sorting) |
| `GET` | `/messages/{id}` | Get a single message by ID |
| `PATCH` | `/messages/{id}/resolve` | Mark a message as resolved |
| `GET` | `/stats` | Get statistics (counts by category & priority) |

> **Note:** The POST endpoint processes messages **synchronously** — it sends the message body to the Gemini LLM for classification before responding. This means a single request can take **~30-60 seconds** depending on LLM response time. In a production system this would be handled asynchronously (e.g. message queue + background worker), but for the scope of this exercise synchronous processing keeps the architecture simple and straightforward.

## How Categorization & Prioritization Works

### Primary: AI-based (Google Gemini)

Each incoming message is sent to **Google Gemini 3 Flash** with a structured prompt that asks the model to classify it into:

- **Category:** `BUG`, `BILLING`, `FEATURE_REQUEST`, or `GENERAL`
- **Priority:** `HIGH`, `MEDIUM`, or `LOW`

The model returns a structured response which is then parsed and stored alongside the message.

### Fallback: Keyword-based Rules

If the LLM call fails or times out (60s limit), the system falls back to regex-based keyword matching:

- **BUG** — matches: `crash`, `error`, `bug`, `broken`, `not working`, `fail`, `down`, `outage`
- **BILLING** — matches: `invoice`, `charge`, `payment`, `bill`, `refund`, `subscription`
- **FEATURE_REQUEST** — matches: `feature`, `add`, `support`, `would like`, `request`, `improve`
- **GENERAL** — default fallback

Priority is determined by keyword severity and category context (e.g. a BUG mentioning "crash" or "data loss" gets HIGH priority; FEATURE_REQUEST defaults to LOW).

## Project Structure

```
src/main/java/com/lowkkid/github/customersupport/
├── config/          — Spring beans (Gemini client, Security, Swagger)
├── controller/      — REST API interface + implementation
├── service/         — Business logic (message processing, AI categorization)
├── domain/
│   ├── entity/      — JPA entities & enums (Category, Priority, Status)
│   ├── repository/  — Spring Data JPA repositories
│   └── projection/  — Query result projections for stats
├── dto/             — Request/response DTOs
└── mapper/          — MapStruct mappers
```

## What I Would Improve With More Time

- **Asynchronous message processing** — decouple the LLM call from the HTTP request using a message queue (e.g. RabbitMQ or Kafka) with a background worker, so the POST endpoint responds instantly and classification happens in the background
- **JWT-based authentication with refresh tokens** — since this is an internal company tool dealing with customer data, proper auth is essential. I would implement JWT access/refresh token flow with role-based access control (e.g. support agent vs. lead)
- **Image/attachment support in tickets** — allow customers to attach screenshots or files to their messages. I would integrate **AWS S3** (or compatible object storage) for media file storage and link attachments to tickets
- **WebSocket notifications** — push real-time updates to the dashboard when new messages arrive or priorities change, instead of requiring manual refresh
- **Persistent database** — replace H2 with PostgreSQL for production use, with Flyway migrations for schema versioning
- **Rate limiting & request validation** — protect the API from abuse, especially the LLM-backed endpoint

## Pre-loaded Data

The application starts with **18 sample messages** (loaded via `data.sql`) covering all categories — bugs, billing issues, feature requests, and general inquiries. These are pre-categorized so the dashboard is populated on first load.

## Deployment

The project is containerized with Docker and deployed automatically via **GitHub Actions** on push to `master`. The CI/CD pipeline builds the Docker image, transfers it to the server via SCP, and restarts the container.