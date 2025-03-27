# Train Ticket Booking Microservice System

A robust, scalable train ticket booking system built using microservices architecture. This system allows users to book train tickets, manage train schedules, and handle user accounts through a set of interconnected microservices.

## 🏗️ Architecture Overview

The application is built using a microservices architecture with the following components:

### Core Services

1. **Service Registry (service-reg)**
   - Eureka Server for service discovery
   - Central registry for all microservices
   - Dashboard for monitoring service status
   - Port: 8761

2. **Configuration Server (config-server)**
   - Centralized configuration management
   - Git-based configuration storage
   - Dynamic configuration updates
   - Port: 8085

3. **User Service (userms)**
   - User account management
   - Authentication and authorization
   - MongoDB for user data storage
   - Profile management

4. **Train Service (trainms)**
   - Train schedule management
   - Seat availability
   - Train information and routes
   - Train search functionality

5. **Ticket Service (ticketms)**
   - Ticket booking and management
   - Booking history
   - Ticket status tracking

6. **API Gateway**
   - Port: 8084
   - Routes requests to appropriate microservices
   - Implements rate limiting and circuit breaking
   - Provides unified API endpoints:
     - User Service: `/v1/user/**`
     - Train Service: `/v1/train/**`
     - Ticket Service: `/v1/tickets/**`
   - Features:
     - Request rate limiting
     - Circuit breaker pattern
     - Fallback mechanisms
     - Load balancing
     - Security configuration

## 🛠️ Technology Stack

### Backend
- **Java Spring Boot** - Core framework
- **Spring Cloud** - Microservices ecosystem
- **Spring Cloud Config** - Centralized configuration
- **Netflix Eureka** - Service discovery
- **Spring Data MongoDB** - Database operations
- **Spring Cloud Gateway** - API Gateway

### Database
- **MongoDB** - NoSQL database for user service

### DevOps & Deployment
- **Docker** - Containerization
- **Docker Compose** - Multi-container deployment
- **Git** - Version control
- **GitHub** - Remote configuration storage

### Monitoring & Logging
- **Spring Actuator** - Application monitoring
- **Eureka Dashboard** - Service health monitoring

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Docker and Docker Compose
- MongoDB
- Git

### Installation & Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/train-ticket-microservice.git
   cd train-ticket-microservice
   ```

2. Start the services using Docker Compose:
   ```bash
   docker-compose up -d
   ```

   This will start all services in the following order:
   - Service Registry (Eureka Server)
   - Config Server
   - MongoDB
   - User Service
   - Train Service
   - Ticket Service

### Service Endpoints

- **Service Registry**: http://localhost:8761
- **Config Server**: http://localhost:8085
- **User Service**: http://localhost:8081
  - API Documentation: http://localhost:8081/swagger-ui.html
- **Train Service**: http://localhost:8082
  - API Documentation: http://localhost:8082/swagger-ui.html
- **Ticket Service**: http://localhost:8083
  - API Documentation: http://localhost:8083/swagger-ui.html

## 🌐 Service Communication

The services communicate with each other through:
- **OpenFeign** - Declarative REST client for service-to-service communication (replaced RestTemplate for reduced boilerplate)
- **RESTful APIs** - HTTP-based API endpoints
- **Service Discovery (Eureka)** - Dynamic service registration and discovery
- **Centralized Configuration (Config Server)** - Shared configuration management

### Inter-Service Communication Example
```java
@FeignClient(name = "userms")
public interface UserServiceClient {
    @GetMapping("/users/{userId}")
    UserResponse getUserDetails(@PathVariable String userId);
}
```

## 🔒 Security

- Basic authentication and authorization
- Service-level security through Spring Security
- Encrypted configuration
- Secure service-to-service communication through Eureka

Note: JWT implementation is planned for future releases to enhance security.

## 🔄 Development Profiles

The application supports multiple profiles:
- Development (dev)
- Docker
- Production (prod)

## 📝 Configuration

External configuration is stored in:
https://github.com/yaksh1/train-ticket-config-server/

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## Architecture

The application follows a microservices architecture with the following components:

1. **Service Registry (Eureka Server)**
   - Port: 8761
   - Handles service discovery and registration
   - All microservices register with this server

2. **Config Server**
   - Port: 8085
   - Centralized configuration management
   - Uses Git repository for configuration storage

3. **API Gateway**
   - Port: 8084
   - Routes requests to appropriate microservices
   - Implements rate limiting and circuit breaking
   - Provides unified API endpoints:
     - User Service: `/v1/user/**`
     - Train Service: `/v1/train/**`
     - Ticket Service: `/v1/tickets/**`
   - Features:
     - Request rate limiting
     - Circuit breaker pattern
     - Fallback mechanisms
     - Load balancing
     - Security configuration

4. **User Service**
   - Port: 8081
   - Handles user management
   - Features:
     - User registration
     - User login
     - User profile management
     - Role-based access control

5. **Train Service**
   - Port: 8082
   - Manages train information
   - Features:
     - Train search
     - Schedule management
     - Seat availability
     - Route information

6. **Ticket Service**
   - Port: 8083
   - Handles ticket booking
   - Features:
     - Ticket booking
     - Booking history
     - Payment integration
     - Booking status updates

7. **MongoDB**
   - Port: 27017
   - Stores user data
   - Features:
     - User profiles
     - Authentication data
     - Session management



