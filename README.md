# Project Setup and Run Instructions

Follow the steps below to set up and run the project:


## Prerequisites

- Java 17 or later
- Docker and Docker Compose installed

## Steps

1. **Navigate to the Deploy Directory**

   ``cd deploy/``



2. **Add the .env File into the Deploy directory.**

   Make sure the .env file is in the Deploy directory.



3. **Start the PostgreSQL Database - Run Docker Compose**

   ``docker compose up -d --build``


4. **Run the application**

   The application runs locally (e.g., from your IDE or command line).  
   Before starting, make sure to configure the required environment variables (such as database connection info) in your system environment or your IDE's run configuration, matching those in the `.env` file.


5. **Access Swagger UI**

   After starting the application, open 👉 [http://localhost:8085/](http://localhost:8085/)


# API Documentation

## Authentication Endpoints

### POST `/auth/login`

#### Description:
Authenticates a user and starts a session.

#### Request Body:
```json
{
"username": "string",
"password": "string"
}
```

#### Response:
- `200 OK` on successful login (no content returned)
- `401 Unauthorized` if credentials are invalid
---

### POST `/auth/logout`

#### Description:
Logs out the current user.

#### Response:
- `200 OK` on successful logout
---

## User Management Endpoints

### POST `/users`

#### Description:
Creates a new user.

#### Request Body:
```json
{
  "username": "string",
  "email": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "password": "string",
  "birthDate": "YYYY-MM-DD"
}
```

#### Response:
- `201 Created` with JSON representation of the created user.
---
### GET `/users`

#### Description: 
Retrieves a paginated list of users. Optional search by username or other criteria.

#### Query Parameters:

- `search` (optional): string to filter users
- `page` (optional, default 0): page number
- `size` (optional, default 10): page size

### Response:

- `200 OK` with JSON array of users.
---
### GET `/users/{id}`

#### Description:
Retrieves a user by their ID.

### Response:

- `200 OK` with JSON representation of the user.
- `404 Not Found` if user does not exist.
---
### PATCH `/users`

#### Description:
Updates fields of the currently authenticated user. Accepts partial updates.

#### Request Body:
- Partial JSON object containing any subset of user fields (e.g. `firstName`, `lastName`, `phoneNumber`, `email`, `birthDate`).

### Response:

- `200 OK` with updated user data
---
### PATCH `/users/{id}`

#### Description:
(Admin only) Updates any user's fields by ID.

#### Request Body:
- Partial JSON object containing any subset of user fields (e.g. `firstName`, `lastName`, `phoneNumber`, `email`, `birthDate`).

### Response:

- `200 OK` with updated user data
- `403 Forbidden` if current user is not an admin.
---
### PUT `/users/change-password`

#### Description:
Changes password of the currently authenticated user.

#### Request Body:
```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```

### Response:

- `204 No Content` on success.
- `400 Bad Request` if the new passwords do not match..
---
### DELETE `/users/{id}`

#### Description:
(Admin only) Deletes a user by ID.

### Response:

- `204 No Content` on success.
- `403 Forbidden` if current user is not an admin.