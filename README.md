# Project Management API

Mini Project Management REST API built with **Spring Boot 3**.
Users can register/login, create projects, and manage tasks within projects. Auth is via **JWT**. Docker support and a Postman collection are included.

---

## Project description
Build a Mini Project Management REST APIs where users can create projects and manage tasks under those projects.

**Core features**

* **User Management**

    * Register and login
    * Each user can have multiple projects
    * Authentication via JWT (Spring Security)
* **Projects**

    * Fields: `id`, `name`, `description`, `createdAt`, `updatedAt`
    * Users can create/view/update/delete their own projects
* **Tasks**

    * Belongs to a project
    * Fields: `id`, `title`, `description`, `status` (`PENDING`, `IN_PROGRESS`, `COMPLETED`), `priority` (`LOW`, `MEDIUM`, `HIGH`), `dueDate`, `createdAt`, `updatedAt`
    * Features: add, update, list (with filters), delete
* **Search & Sorting**

    * Search tasks across all projects of a user by title/description
    * Sort by `dueDate` or `priority`
* **Tech**

    * Spring Boot 3.x (Java 17+), Spring Data JPA, Spring Security + JWT, MySQL/H2/Postgres support, validation, exception handling

---

## 📁 Postman collection

I see you provided a Postman collection file. Use this local file path to import into Postman:

```
/mnt/data/ProjectApiTest.postman_collection.json
```


### 1. Prerequisites

* JDK 17+ (or 20 as you used)
* Maven
* MySQL

### 2. Configure DB

src/main/resources/application.properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/projectdb?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=pass

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=your-very-long-secret-at-least-32-chars
jwt.expiration=3600000
```

**Create DB:**

```sql
CREATE DATABASE projectdb;
```

App will start at: `http://localhost:8080`


## API 

> Base path: `/v1`

### Auth

* `POST /v1/auth/signup` — register user
* `POST /v1/auth/signin` — login and receive JWT
* `GET  /v1/auth/profile` — get own profile (requires Authorization)
* `GET  /v1/auth/all` — list all users (open or protected based on your config)

### Projects

* `POST   /v1/projects` — create project (auth)

    * body: `{ "projectName": "...", "projectDescription": "..." }`
* `GET    /v1/projects/all` — list all projects (for the user)
* `PUT    /v1/projects/update/{id}` — update project (auth)
* `DELETE /v1/projects/delete/{id}` — delete project (auth)

### Tasks

* `POST   /v1/tasks/project/{projectId}` — add task to project

    * body example:

      ```json
      {
        "taskTitle": "Implement X",
        "taskDescription": "Details...",
        "taskStatus": "PENDING",
        "taskPriority": "HIGH",
        "taskDueDate": "2025-11-27"
      }
      ```
* `PUT    /v1/tasks/{taskId}` — update task
* `GET    /v1/tasks` — list tasks with optional filters: `?status=...&priority=...&sortBy=dueDate`
* `GET    /v1/tasks/filter?keyword=...` — search tasks by title/description
* `DELETE /v1/tasks/{taskId}` — delete task


##  DB Fields

* **User**

    * `userId (UUID)`, `fullName`, `emailAddress`, `passwordHash`
* **Project**

    * `projectId (UUID)`, `projectName`, `projectDescription`, `createdOn`, `updatedOn`, `owner (User)`
* **Task**

    * `taskId (UUID)`, `taskTitle`, `taskDescription`, `taskStatus`, `taskPriority`, `taskDueDate`, `createdOn`, `updatedOn`, `project (Project)`, `assignedUser (User)`

