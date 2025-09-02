# Authentication API Requirements

This document outlines the detailed requirements for implementing the authentication features, specifically user registration.

## 1. `AuthenticationController`

### 1.1. Overview
Create a Spring REST controller to handle authentication-related requests.

### 1.2. Details
- **Class Name:** `AuthenticationController`
- **Package:** `com.example.todos.controller`
- **Annotation:** `@RestController`
- **Request Mapping:** `@RequestMapping("/api/auth")`

### 1.3. Endpoints

#### 1.3.1. User Registration
- **Endpoint:** `POST /register`
- **Method:** `register`
- **Request Body:** `RegisterRequest` object. The request body should be validated using `@Valid`.
- **Response:**
    - **Success:** HTTP 201 (Created) with a success message or the created user object (without sensitive information).
    - **Failure (Validation):** HTTP 400 (Bad Request) with validation error messages.
    - **Failure (User Exists):** HTTP 409 (Conflict) with an error message like "User with this email already exists."
- **Service Dependency:** This method will call the `register` method of the `AuthenticationService`.

## 2. `RegisterRequest` Data Transfer Object (DTO)

### 2.1. Overview
Create a DTO class to encapsulate the data for a user registration request.

### 2.2. Details
- **Class Name:** `RegisterRequest`
- **Package:** `com.example.todos.request` (or a similar `dto` package)

### 2.3. Fields
- **`firstName`**:
    - **Type:** `String`
    - **Validation:**
        - `@NotEmpty` (or `@NotBlank`)
        - `@Size(min = 2, max = 30)`
- **`lastName`**:
    - **Type:** `String`
    - **Validation:**
        - `@NotEmpty` (or `@NotBlank`)
        - `@Size(min = 2, max = 30)`
- **`email`**:
    - **Type:** `String`
    - **Validation:**
        - `@NotEmpty` (or `@NotBlank`)
        - `@Email`
- **`password`**:
    - **Type:** `String`
    - **Validation:**
        - `@NotEmpty` (or `@NotBlank`)
        - `@Size(min = 5, max = 30)`

## 3. `AuthenticationService`

### 3.1. Overview
Create a service layer to handle the business logic for authentication.

### 3.2. `AuthenticationService` Interface
- **Interface Name:** `AuthenticationService`
- **Package:** `com.example.todos.service`
- **Methods:**
    - `void register(RegisterRequest request);`

### 3.3. `AuthenticationServiceImpl` Class
- **Class Name:** `AuthenticationServiceImpl`
- **Package:** `com.example.todos.service`
- **Implements:** `AuthenticationService`
- **Annotation:** `@Service`

### 3.4. Dependencies
- `UserRepository`: To interact with the user data in the database.
- `PasswordEncoder`: To hash passwords before saving them.

### 3.5. `register` Method Logic
- **Input:** `RegisterRequest` object.
- **Flow:**
    1.  **Check for Existing User:**
        - Use the `UserRepository` to check if a user with the provided email already exists.
        - If a user exists, throw a Runtime exception.
    2.  **Hash Password:**
        - Use the `PasswordEncoder` to encode the raw password from the `RegisterRequest`.
    3.  **Create User Entity:**
        - Create a new `User` entity.
        - Map the fields from the `RegisterRequest` to the `User` entity.
        - Set the hashed password on the `User` entity.
    4.  **Save User:**
        - Use the `UserRepository` to save the new `User` entity to the database.