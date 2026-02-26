# CogniCart – Database Design

CogniCart follows Polyglot Persistence architecture.

Different services use different databases based on requirements.

---

## 1. PostgreSQL (Relational)

Used for transactional services:

- Identity Service
- Order Service
- Payment Service
- Inventory Service

Reason:
- ACID compliance
- Strong consistency
- Transactional integrity

---

### Identity Tables

Users
- id
- name
- email
- password
- role
- created_at
- updated_at

Roles
- id
- name

---

### Orders Tables

Orders
- id
- user_id
- total_amount
- status
- created_at

Order_Items
- id
- order_id
- product_id
- quantity
- price

---

### Payments Tables

Payments
- id
- order_id
- status
- transaction_id
- created_at

---

## 2. MongoDB (Document DB)

Used for:

- Catalog Service (Products)

Reason:
- Flexible schema
- Dynamic attributes
- Nested structures
- AI metadata storage

---

### Product Document Structure

- _id
- name
- slug
- sellerId
- category
- price
- discount
- attributes (dynamic object)
- images (array)
- aiMetadata (object)
- createdAt
- updatedAt

---

## 3. Redis

Used for:
- Caching frequently accessed products
- Session storage (future)
- Rate limiting

---

## 4. Elasticsearch

Used for:
- Full-text product search
- Filtering
- Sorting
- Autocomplete