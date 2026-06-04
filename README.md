# Biblioteca API

API REST desarrollada con Django REST Framework y PostgreSQL para la gestión de biblioteca.


## Instalación

### 1. Clonar el repositorio

git clone https://github.com/dylan18x/fernandez-control-biblioteca-backend.git

cd fernandez-biblioteca-api

### 2. Activar entorno virtual

.\.venv\Scripts\Activate.ps1

### 3. Configurar PostgreSQL

En pgAdmin4 o 
CREATE USER fernandez_biblioteca_user WITH PASSWORD 'fernandez_biblioteca_pass';
CREATE DATABASE fernandez_biblioteca_db OWNER fernandez_biblioteca_user;
GRANT ALL PRIVILEGES ON DATABASE fernandez_biblioteca_db TO fernandez_biblioteca_user;


### 4. Crear superusuario

uv run python manage.py createsuperuser

### 5. Ejecutar servidor

uv run python manage.py runserver

http://localhost:8000/


# Autenticación

La API utiliza JWT mediante SimpleJWT.

## Registro

POST

{{base_url}}/auth/register/

Body:

{
  "username": "dylan",
  "email": "dylan@test.com",
  "password": "dylan123",
  "password2": "dylan123"
}


## Login

POST

{{base_url}}/auth/login/

Body:

{
  "username": "admin",
  "password": "admin"
}

## Variables de entorno:
{{baseUrl}} -> http://localhost:8000/api
{{access}} -> accessToken
{{refresh}} -> refreshToken 



# Permisos

### Usuario normal

Puede realizar:

- GET

### Administrador

Puede realizar:

- GET
- POST
- PUT
- PATCH
- DELETE


# Endpoints

## Auth (Autorización)

POST /auth/register/  
POST /auth/login/        
POST /auth/token/refresh/ 
POST /auth/token/verify/   
POST /auth/logout/        

## Users (Usuarios)

GET    /users/              
GET    /users/{id}/         
GET    /users/profile/        
PATCH  /users/profile/        
POST   /users/change-password/  
POST   /users/                  
POST   /users/{id}/toggle-active/ 
GET    /users/stats/           

## Category Books (Categoria de Libros)

GET    /category-books/             
GET    /category-books/?search=      
GET    /category-books/?active=true  
POST   /category-books/             
PATCH  /category-books/{id}/        
DELETE /category-books/{id}/        
GET    /category-books/stats/    

## Books (Libros)

GET    /books/             
GET    /books/{id}/       
GET    /books/?search=      
GET    /books/?ordering=    
POST   /books/             
PUT    /books/{id}/        
PATCH  /books/{id}/       
DELETE /books/{id}/        
GET    /books/stats/       

## Readers (Lectores)

GET    /reader/            
GET    /reader/{id}/        
GET    /reader/?search=     
POST   /reader/           
PUT    /reader/{id}/        
PATCH  /reader/{id}/      
DELETE /reader/{id}/         
GET    /reader/stats/        

## Loans (Prestamo)

GET    /loan/             
GET    /loan/{id}/        
POST   /loan/             
PUT    /loan/{id}/         
PATCH  /loan/{id}/       
DELETE /loan/{id}/        
GET    /loan/stats/      

## Loan Details (Detalle de prestamo)

GET    /loan-details/            
GET    /loan-details/{id}/      
POST   /loan-details/            
PUT    /loan-details/{id}/       
PATCH  /loan-details/{id}/    
DELETE /loan-details/{id}/    