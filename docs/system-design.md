# CogniCart – System Design Document

## 1. Project Overview

CogniCart is an AI-native, event-driven e-commerce platform built using microservices architecture.

The system is designed to demonstrate:

- Distributed systems design
- Event-driven architecture
- AI integration in commerce
- Polyglot persistence
- Production-grade DevOps practices

---

## 2. Architecture Style

- Microservices Architecture
- Event-Driven Communication (Kafka)
- Saga Pattern for Distributed Transactions
- Polyglot Persistence
- Containerized Infrastructure

---

## 3. High-Level Architecture Components

### Edge Layer
- API Gateway
- Reverse Proxy (Nginx - future)

### Core Services
- Identity Service
- Catalog Service
- Inventory Service
- Order Service
- Payment Service
- Search Service

### AI Services
- Attribute Extraction Engine
- Recommendation Engine
- Conversational Shopping Engine

### Infrastructure
- Kafka (Event Bus)
- Redis (Cache)
- Elasticsearch (Search Index)
- Docker (Containerization)

---

## 4. Communication Pattern

- Synchronous: REST APIs
- Asynchronous: Kafka Events

Services communicate via events to reduce tight coupling.

---

## 5. Transaction Management

Order flow uses Saga Pattern:

1. Order Created
2. Inventory Reserved
3. Payment Processed
4. Order Confirmed

If failure occurs:
- Compensating transactions are triggered.

---

## 6. Scalability Strategy

- Stateless services
- Horizontal scaling
- Event-based async processing
- Database separation per service

---

## 7. Security

- JWT Authentication
- Role-Based Access Control (RBAC)
- Encrypted passwords (BCrypt)
- API validation
- Rate limiting (future)

---

## 8. Deployment Strategy

- Docker Compose (local)
- Cloud deployment (future: AWS)
- CI/CD pipeline integration