# CogniCart – Event Contracts

All services communicate asynchronously using Kafka.

Events are published in JSON format.

---

## 1. Product Events

### ProductCreated

{
  "eventId": "uuid",
  "eventType": "ProductCreated",
  "timestamp": "ISO_DATE",
  "productId": "string",
  "sellerId": "string",
  "name": "string",
  "price": 0,
  "category": "string"
}

---

### ProductEnriched

{
  "eventId": "uuid",
  "eventType": "ProductEnriched",
  "timestamp": "ISO_DATE",
  "productId": "string",
  "attributes": {},
  "confidenceScore": 0.0
}

---

## 2. Order Events

### OrderCreated

{
  "eventId": "uuid",
  "eventType": "OrderCreated",
  "timestamp": "ISO_DATE",
  "orderId": "string",
  "userId": "string",
  "totalAmount": 0
}

---

### OrderConfirmed

{
  "eventId": "uuid",
  "eventType": "OrderConfirmed",
  "timestamp": "ISO_DATE",
  "orderId": "string"
}

---

## 3. Payment Events

### PaymentSucceeded

{
  "eventId": "uuid",
  "eventType": "PaymentSucceeded",
  "timestamp": "ISO_DATE",
  "orderId": "string",
  "transactionId": "string"
}

### PaymentFailed

{
  "eventId": "uuid",
  "eventType": "PaymentFailed",
  "timestamp": "ISO_DATE",
  "orderId": "string"
}

---

## 4. Inventory Events

### StockReserved
### StockReleased

Each event must include:

- eventId
- eventType
- timestamp
- correlationId (for tracing)

---

## Event Naming Convention

- Use PascalCase
- Include action-based naming
- Avoid versioning in name (use schema evolution instead)