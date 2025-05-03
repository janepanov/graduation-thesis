### Graduation Thesis Management System

A microservice-based application for managing the complete lifecycle of graduation theses using Domain-Driven Design, CQRS, and Event Sourcing patterns.

https://opensource.org/licenses/MIT)

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)



- [API Documentation](#api-documentation)
- [Development](#development)

- [Command Flow](#command-flow)
- [Event Flow](#event-flow)
- [Adding New Features](#adding-new-features)



- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)


## Overview

The Graduation Thesis Management System is designed to streamline the process of thesis proposal, approval, defense, and grading. It provides a comprehensive solution for universities to manage the entire thesis lifecycle, from initial proposal to final grading.

Key features include:

- Thesis proposal creation and submission
- Multi-stage approval workflow (student, administration, vice dean)
- Thesis text upload and review
- Committee approval process
- Defense scheduling and grading
- Notification system for all stakeholders
- Comprehensive audit trail through event sourcing


## Architecture

This application follows a microservice architecture based on Domain-Driven Design (DDD) principles and implements the Command Query Responsibility Segregation (CQRS) pattern with Event Sourcing.





### Key Architectural Components:

1. **Domain Layer**: Contains the core business logic, aggregates, commands, events, and value objects
2. **Application Layer**: Orchestrates the domain logic and provides services to the presentation layer
3. **Infrastructure Layer**: Handles persistence, messaging, and external service communication
4. **Presentation Layer**: Exposes the API endpoints and handles user interaction


### CQRS Implementation:

- **Command Side**: Handles commands that change the system state
- **Query Side**: Provides optimized read models for querying data
- **Event Store**: Stores all events as the source of truth


### Saga Pattern:

The application implements the Saga pattern to manage long-running processes and coordinate actions across multiple steps in the thesis lifecycle.

## Technologies

- **Language**: Kotlin 1.9.x
- **Framework**: Spring Boot 3.2.x
- **CQRS/Event Sourcing**: Axon Framework 4.9.x
- **Database**: PostgreSQL 15
- **Messaging**: Apache Kafka
- **API Documentation**: OpenAPI/Swagger
- **Containerization**: Docker & Docker Compose
- **Circuit Breaker**: Resilience4j
- **Service Communication**: Spring Cloud OpenFeign


## Project Structure

```plaintext
graduation-thesis/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── mk/
│   │   │       └── ukim/
│   │   │           └── finki/
│   │   │               └── soa/
│   │   │                   └── graduationthesis/
│   │   │                       ├── application/
│   │   │                       │   ├── config/
│   │   │                       │   ├── service/
│   │   │                       │   └── saga/
│   │   │                       ├── domain/
│   │   │                       │   ├── aggregate/
│   │   │                       │   ├── command/
│   │   │                       │   ├── event/
│   │   │                       │   ├── exception/
│   │   │                       │   └── valueobject/
│   │   │                       ├── infrastructure/
│   │   │                       │   ├── persistence/
│   │   │                       │   ├── messaging/
│   │   │                       │   └── client/
│   │   │                       └── presentation/
│   │   │                           ├── api/
│   │   │                           └── dto/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── logback.xml
│   └── test/
│       └── kotlin/
│           └── mk/
│               └── ukim/
│                   └── finki/
│                       └── soa/
│                           └── graduationthesis/
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.8+
- Docker and Docker Compose
- Git


### Installation

1. Clone the repository:

```shellscript
git clone https://github.com/yourusername/graduation-thesis.git
cd graduation-thesis
```


2. Build the application:

```shellscript
mvn clean package
```




### Running the Application

#### Using Docker Compose (Recommended)

1. Start all services:

```shellscript
docker-compose up -d
```


2. Check the status of the services:

```shellscript
docker-compose ps
```


3. Access the application at: [http://localhost:8080](http://localhost:8080)
4. Access Swagger UI at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


#### Running Locally

1. Start the required infrastructure:

```shellscript
docker-compose up -d postgres axon-server kafka
```


2. Run the application:

```shellscript
mvn spring-boot:run
```




## API Documentation

The API documentation is available via Swagger UI at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Key Endpoints

| Method | Endpoint | Description
|-----|-----|-----
| POST | /api/thesis | Create a new thesis
| POST | /api/thesis/thesisId/accept | Accept thesis proposal by student
| POST | /api/thesis/thesisId/validate | Validate thesis proposal by administration
| POST | /api/thesis/thesisId/approve | Approve thesis proposal by vice dean
| POST | /api/thesis/thesisId/upload | Upload thesis text
| POST | /api/thesis/thesisId/approve-mentor | Approve thesis text by mentor
| POST | /api/thesis/thesisId/approve-committee | Approve thesis text by committee member
| POST | /api/thesis/thesisId/schedule | Schedule thesis defense
| POST | /api/thesis/thesisId/complete | Complete thesis defense
| POST | /api/thesis/thesisId/cancel | Cancel thesis
| GET | /api/thesis/thesisId | Get thesis by ID
| GET | /api/thesis/student/studentId | Get theses by student ID
| GET | /api/thesis/mentor/mentorId | Get theses by mentor ID
| GET | /api/thesis/status/status | Get theses by status
| GET | /api/thesis | Get all theses


## Development

### Command Flow

1. Client sends a command via REST API
2. Command is dispatched to the appropriate aggregate
3. Aggregate validates the command and applies events
4. Events are stored in the event store
5. Events are published to event handlers
6. Event handlers update the read model


### Adding New Features

1. Define the command in `domain/command/`
2. Define the event in `domain/event/`
3. Add command handler in `domain/aggregate/ThesisAggregate.kt`
4. Add event sourcing handler in `domain/aggregate/ThesisAggregate.kt`
5. Add event handler in `infrastructure/persistence/`
6. Add API endpoint in `presentation/api/`
7. Add DTO in `presentation/dto/`
8. Add service method in `application/service/`
9. Update saga if necessary in `application/saga/`


## Testing

### Running Tests

```shellscript
mvn test
```

### Test Coverage

```shellscript
mvn test jacoco:report
```

The coverage report will be available at: `target/site/jacoco/index.html`

## Contributing

We welcome contributions to the Graduation Thesis Management System! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


### Commit Message Guidelines

We follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

- `feat:` - A new feature
- `fix:` - A bug fix
- `docs:` - Documentation only changes
- `style:` - Changes that do not affect the meaning of the code
- `refactor:` - A code change that neither fixes a bug nor adds a feature
- `perf:` - A code change that improves performance
- `test:` - Adding missing tests or correcting existing tests
- `chore:` - Changes to the build process or auxiliary tools


Example: `feat: add thesis defense scheduling functionality`

### Code Style

We follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html). Please ensure your code is formatted accordingly.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- [Axon Framework](https://axoniq.io/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Domain-Driven Design](https://domainlanguage.com/)


## Contact

Project Link: [https://github.com/yourusername/graduation-thesis](https://github.com/yourusername/graduation-thesis)

---

*This README follows [Make a README](https://www.makeareadme.com/) guidelines.*