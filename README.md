# Project Management System - Backend

A production-oriented backend system built using Spring Boot for managing projects, tasks, users, and role-based collaboration.

This project was designed with a strong focus on backend engineering principles such as clean architecture, stateless authentication, transaction management, scalable API design, and performance-aware persistence handling.

---

## Features

### Authentication & Authorization
- JWT-based stateless authentication
- Secure login flow
- Role-based access control
- Protected endpoints using Spring Security

### Project Management
- Create and manage projects
- Add users to projects
- Delete projects
- Paginated project retrieval
- Role-aware access handling

### Task Management
- Create and assign tasks
- Task status tracking
- Project-task relationships

### API Design
- RESTful API structure
- DTO-based request/response handling
- Global exception handling
- Validation support
- Standardized API responses
- Proper HTTP status codes

### Persistence & Performance
- Spring Data JPA + Hibernate
- Optimized entity relationships
- Lazy loading
- Pagination support
- Activity Logging

### Architecture
- Layered architecture
    - Controller
    - Service
    - Repository
- Separation of concerns
- Scalable backend structure

---

## Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

### Database
- PostgreSQL

### Caching
- Redis (In progress)

### Authentication
- JWT (JSON Web Tokens)

### Documentation
- Swagger / OpenAPI (View documentation at <project-url>/swagger-ui/index.html>

---

## Project Structure

```text
src/main/java
│
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── exception
├── config
```

---

## Security Features

- Stateless authentication
- JWT token validation
- Role-based endpoint protection
- Password hashing using BCrypt
- Secure API access

---

## API Features

### Pagination Example

```http
GET /projects?page=0&size=8
```

### Authenticated Requests

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## Running the Project (Locally)

### Clone Repository

```bash
git clone https://github.com/kirannamburi06/collaborative-project-and-task-management-system.git
```

---

### Configure Environment Variables

Create application.yaml configuration for:

```properties
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

jwt.secret=
jwt.expiration=
```

---

### Build Project

```bash
mvn clean install
```

---

### Run Application

```bash
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```

---

# Running the Project with Docker

This project can be run using Docker.

The backend application is containerized using a Dockerfile.

---

# Prerequisites

Make sure the following are installed:

- Docker

Verify Docker installation:

```bash
docker --version
```

---

# Project Structure

```text
project-root/
│
├── Dockerfile
├── pom.xml
├── src/
└── ...
```

---

# Build Docker Image

From the project root:

```bash
docker build -t project-management-backend .
```

This command:

- Builds the Spring Boot application
- Creates a Docker image
- Packages the backend inside the container

---

# Running with Environment Variables

If the application uses environment variables:

```bash
docker run \
-p 8080:8080 \
-e DB_URL=your_database_url \
-e DB_USERNAME=your_db_username \
-e DB_PASSWORD=your_db_password \
-e JWT_SECRET=your_jwt_secret \
project-management-backend
```

---

The backend will now run on:

```text
http://localhost:8080
```

---

# Swagger UI

Access Swagger documentation:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# View Running Containers

```bash
docker ps
```

---

# Stop Container

```bash
docker stop <container_id>
```

---

# View Logs

```bash
docker logs <container_id>
```

---

# Remove Container

```bash
docker rm <container_id>
```

---

# Remove Docker Image

```bash
docker rmi project-management-backend
```

---

# Notes

- PostgreSQL should be running separately
- Environment variables should be configured properly
- Docker is currently used only for backend containerization

---

## Future Improvements

- Swagger/OpenAPI documentation
- Dockerization
- CI/CD pipeline
- Kubernetes deployment
- Email notifications
- WebSocket notifications
- Microservices migration

---

## Learning Outcomes

This project helped strengthen understanding of:

- Spring Boot architecture
- REST API design
- JWT authentication
- Spring Security filter chain
- JPA/Hibernate relationships
- Transaction management
- Pagination and scalable querying
- Backend system design

---

## Author

Kiran

Backend-focused developer interested in:
- Java backend engineering
- Distributed systems
- DevOps
- Scalable architectures
- System design
