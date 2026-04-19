# 🔐 Auth Service (Java - Spring Boot)

Authentication & session management service built with **Spring Boot** implementing **JWT-based authentication with sliding session**.

Designed with focus on:

- Security (token rotation, hashing, session control)
- Clean architecture
- Scalability (stateless access token + stateful session)

---

## 🚀 Features

### 🔑 Authentication

- User Registration (create account + initial session)
- Login (email & password)
- Password hashing (BCrypt)

### 🎟️ Token System

- Access Token (short-lived JWT)
- Refresh Token (long-lived, stored in DB)
- Secure token hashing (refresh token not stored in plain text)

### 🔄 Sliding Session

- Refresh token rotation on every refresh
- Session stays alive as long as user is active
- Absolute expiration enforced (max session lifetime)

### 🛡️ Security

- JWT validation via filter
- Stateless authentication for API
- Session tracking in database
- Protection against:
    - Token replay
    - Token theft (rotation + hash)
    - Unauthorized access

### ⚠️ Error Handling

- Centralized global exception handler
- Consistent API response format
- Proper HTTP status mapping (401, 403, 500, dll)

---

## 🧠 Architecture Overview

```
Client
  ↓
[ Access Token (JWT) ]
  ↓
Spring Security Filter
  ↓
Controller → Service → Repository → Database

Refresh Flow:
Client → /refresh
  → Validate Refresh Token (DB)
  → Rotate Token
  → Issue new Access + Refresh Token
```

---

## 🔄 Sliding Session Flow

1. User login → create session
2. Access token expired → client uses refresh token
3. Server:
    - Validate refresh token
    - Check session validity
    - Rotate refresh token (invalidate old)
    - Issue new tokens
4. Session continues until:
    - Absolute expiry reached
    - User logout / revoked

---

## 🗂️ Project Structure

├── controller # REST endpoints  
├── service # Business logic  
├── repository # Data access (JPA)  
├── entity # Database models  
├── dto # Request/Response models  
├── security # JWT & filter config  
├── config # App configuration  
├── exception # Custom exceptions & handlers

---

## ⚙️ Configuration

Example `application.properties`:

jwt.secret=${JWT_SECRET}

# minutes

jwt.access-token-expiration=15

# minutes (14 days)

jwt.refresh-token-expiration=20160

---

## 📌 API Endpoints

| Method | Endpoint       | Description   |
| ------ | -------------- | ------------- |
| POST   | /auth/register | Register user |
| POST   | /auth/login    | Login         |
| POST   | /auth/refresh  | Refresh token |
| POST   | /auth/logout   | Logout        |

---

## 📦 Request & Response Example

### Login Request

```json
{
    "email": "user@email.com",
    "password": "password"
}
```

### Login Response

```json
{
    "message": "success",
    "data": {
        "accessToken": "...",
        "refreshToken": "...",
        "tokenType": "Bearer"
    },
    "error": null
}
```

---

## 🔐 Session Model

Each login creates a session:

- session_id (UUID)
- user_id
- token_hash (hashed refresh token)
- expires_at (absolute expiry)
- created_at

---

## 🧩 Design Decisions

### Why JWT + Session?

- JWT → fast, stateless API calls
- Session DB → control & revoke access

### Why Refresh Token Rotation?

- Prevent replay attack
- Limit damage if token leaked

### Why Hash Refresh Token?

- Prevent DB leak → usable tokens

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT

---

## ▶️ Running the Project

```bash
docker-compose up -d
```

---

## 📈 Future Improvements

- Multi-device session management
- Rate limiting (login & refresh)
- Audit logging
- OAuth login (Google, GitHub)

---

## 🧑‍💻 Author

Riski Putra
