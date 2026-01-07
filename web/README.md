# ABA Banking System - Web Application

This is the web-based version of the ABA Banking System, built with HTML/CSS/JavaScript frontend connected to a Java backend.

## Project Structure

```
TestingDB/
├── src/
│   ├── web/                     # Web server and API controllers
│   │   ├── WebServer.java       # Main HTTP server
│   │   ├── BaseController.java  # Base controller utilities
│   │   ├── CustomerController.java
│   │   ├── TransactionController.java
│   │   └── AdminController.java
│   ├── service/                 # Business logic services
│   ├── repository/              # Data access layer
│   └── model/                   # Entity classes
├── web/                         # Frontend static files
│   ├── index.html               # Main HTML file
│   ├── css/
│   │   └── styles.css           # Stylesheet
│   └── js/
│       └── app.js               # JavaScript application
└── lib/                         # External libraries
```

## How to Run

### 1. Compile the Java Backend

From the project root directory, compile all Java files:

```bash
# Windows
javac -cp "lib/*" -d bin src/**/*.java

# Or compile individually
javac -cp "lib/*" -d bin src/config/*.java src/model/*.java src/security/*.java src/repository/*.java src/service/*.java src/web/*.java
```

### 2. Run the Web Server

```bash
# Windows
java -cp "bin;lib/*" web.WebServer

# Linux/Mac
java -cp "bin:lib/*" web.WebServer
```

### 3. Access the Application

Open your web browser and navigate to:

```
http://localhost:8080
```

## API Endpoints

### Customer Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/customer/login` | Customer login |
| POST | `/api/customer/create` | Create new customer |
| GET | `/api/customer/all` | Get all customers |
| GET | `/api/customer/find/{accNo}` | Find customer by account number |
| GET | `/api/customer/search?term=xxx` | Search customers |

### Transaction Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/transactions/all` | Get all transactions |
| GET | `/api/transactions/customer/{id}` | Get customer transactions |
| GET | `/api/transactions/search?term=xxx` | Search transactions |
| POST | `/api/transactions/withdraw` | Process withdrawal |
| POST | `/api/transactions/deposit` | Process deposit |
| POST | `/api/transactions/transfer` | Process transfer |

### Admin Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/admin/login` | Admin login |

## Default Admin Credentials

- **Username:** admin
- **Password:** admin123

(These are set by the `AdminService.initialize()` method)

## Features

### Customer Features
- Login with account number and password
- View account balance
- View transaction history
- Withdraw funds
- Deposit funds
- Transfer to other accounts
- View account details
- Generate transaction reports

### Admin Features
- Login with admin credentials
- View all customer accounts
- Search customers by name or account number
- Create new customer accounts
- View all transactions
- Search transactions
- Generate transaction reports

## Technology Stack

- **Frontend:** HTML5, CSS3, Vanilla JavaScript
- **Backend:** Java (using built-in HttpServer)
- **Database:** Connects to existing repository implementations
- **Architecture:** RESTful API design

## UI Design

The web UI is designed to match the original Java Swing application, using the same color scheme:

- Primary Background: #668090 (Steel Blue)
- Panel Background: #3D5260
- Text Color: #4FA6B7 (Teal)
- Card Background: #4B5F6E
- Accent Border: #8A2BE2 (Purple)

## Notes

- The web server uses Java's built-in `com.sun.net.httpserver.HttpServer`
- No external web frameworks required
- CORS is enabled for development purposes
- The server runs on port 8080 by default
