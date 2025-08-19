# 📝 ToDo List API

A **Spring Boot REST API** for managing ToDo tasks with **JWT authentication**.  
Users can sign up, log in, and manage their own todos securely.  
Includes **role-based access control**, **refresh tokens**, and auditing for created/updated records.

---

## 🚀 Features
- User Registration & Login
- JWT Authentication (Access + Refresh Tokens)
- Role-based Authorization (`USER`, `ADMIN`)
- CRUD APIs for managing Todos
- Only owners can manage their own Todos
- Global Exception Handling with custom API responses
- Audit fields (`createdBy`, `updatedBy`, `createdAt`, `updatedAt`)
- MySQL database integration
- Secure password hashing with **BCrypt**

---

## 🛠️ Tech Stack
- **Java 17**
- **Spring Boot 3**
- **Spring Security + JWT**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Maven**

---

## 📂 Project Structure

```

src/main/java/com/project/todolist
│── controller/       # REST Controllers
│── model/            # Entities & DTOs
│── repository/       # JPA Repositories
│── security/         # JWT & Security Config
│── service/          # Business Logic
│── exception/        # Custom Exception Handlers
│── payload/          # Request/Response DTOs

````

---

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/Masood395/todolist.git
cd todolist
````

### 2. Configure Database

* Create a MySQL database (example: `todolist`).
* Update `src/main/resources/application.properties` with your DB credentials.
  ⚠️ Don’t commit this file — use `application-example.properties` for sharing configs.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todolist
spring.datasource.username=your-username
spring.datasource.password=your-password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=your-secret-key
jwt.expiration=3600000
```

### 3. Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🔑 API Endpoints

### Authentication

| Method | Endpoint            | Description          |
| ------ | ------------------- | -------------------- |
| POST   | `/api/auth/signup`  | Register new user    |
| POST   | `/api/auth/login`   | Login & get tokens   |
| POST   | `/api/auth/refresh` | Refresh access token |

### Todos

| Method | Endpoint          | Description          |
| ------ | ----------------- | -------------------- |
| GET    | `/api/todos`      | Get all todos (user) |
| POST   | `/api/todos`      | Create new todo      |
| PUT    | `/api/todos/{id}` | Update todo          |
| DELETE | `/api/todos/{id}` | Delete todo          |

---

## 📌 Sample Requests

### Signup

```json
POST /api/auth/signup
{
  "username": "john",
  "password": "password123",
  "role": "USER"
}
```

### Login

```json
POST /api/auth/login
{
  "username": "john",
  "password": "password123"
}
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1...",
  "refreshToken": "eyJhbGciOiJIUzI1...",
  "tokenType": "Bearer"
}
```

### Create Todo

```json
POST /api/todos
Authorization: Bearer <access_token>

{
  "todoName": "Learn Spring Boot",
  "status": "PENDING"
}
```

---

## 🧑‍💻 Development Notes

* `application.properties` is ignored by Git — use `application-example.properties` instead.
* Uses **BCryptPasswordEncoder** for secure password hashing.
* Exception handling is centralized via `@ControllerAdvice`.

---

## ✅ Future Improvements

* Dockerize the application
* Swagger/OpenAPI Documentation
* Role-based admin dashboard
* CI/CD setup with GitHub Actions

---

## 👨‍💻 Author

Developed by [Mahammad Masood](https://github.com/Masood395)
🚀 Open to contributions & suggestions!

```
