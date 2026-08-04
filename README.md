# AgriLink

A production-ready, full-stack location-based agricultural job marketplace built with Spring Boot, HTML5, CSS3, Vanilla JavaScript, and MySQL. It connects landowners looking for labour with labourers seeking work, featuring location/wage filtering, application workflows, post-job comments and ratings, and real-time status dashboards.

---

## 📖 Overview

AgriLink simplifies agricultural labour management and job matching by combining:
- Seamless user registration and profile management with distinct roles (Landowners vs. Labourers).
- Dynamic job posting by landowners with details like location, compensation (wage), and required skills.
- Location-based job matching and advanced filtering (category, wage) for labourers.
- Complete application lifecycle tracking (apply, accept, reject) with real-time status updates.
- Post-job feedback via a comment and star-rating system.

It is designed with containerization (Docker support) and clean layered architecture (REST API + Vanilla SPA frontend), making it suitable for production-level deployments or observation of robust enterprise patterns.

---

## ✨ Key Features

- 🔐 **User Authentication & Role Management** (Landowners vs. Labourers) via Spring Security + JWT.
- 📤 **Location-Based Job Posting** & dynamic management for Landowners.
- 🔍 **Location, Wage, and Category-Based Search** & filtering for Labourers.
- 📋 **Interactive Job Applications Flow** (Apply, Accept, Reject) with notifications.
- ⭐ **Feedback Loop** via post-job reviews, star ratings, and comments.
- 📊 **Dashboard Overview** featuring active jobs, total applications, and recent activities.
- 🌓 **Responsive, Modern UI** built purely with HTML5, CSS3, and Vanilla JavaScript.
- 🐳 **Dockerized Environment** support for easy backend execution.

---

## 🏗️ Architecture

```
User Browser (Landowner / Labourer)
        │
        ▼
 Vanilla SPA Frontend (Vercel)
        │
        ▼
Spring Security Filter (JWT Authentication)
        │
        ▼
Spring Boot REST API (Port 8090)
   ├── AuthController
   ├── JobController
   ├── ApplicationController
   ├── ReviewController
   └── ProfileController
        │
        ▼
   Service Layer (Business Logic)
        │
        ▼
   Data JPA Layer (Repositories)
        │
        ▼
   PostgreSQL / MySQL Database (Data Persistence)
```

> [!NOTE]
> The system is flexible and can connect to either MySQL or PostgreSQL database engines depending on the configuration.

---

## 🛠️ Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3**
- **Spring Security & JSON Web Token (JWT)** for secure authentication
- **Spring Data JPA & Hibernate**
- **PostgreSQL / MySQL Connector** (Database support)
- **Maven** (build & dependency manager)
- **Lombok & Jakarta Validation**

### Frontend
- **HTML5 & CSS3** (Vanilla responsive layout, custom themes)
- **Vanilla JavaScript** (ES6+ fetch API, dynamic DOM rendering, localStorage state management)
- **Komoot Photon / OSM Nominatim API** (Client-side geocoding)

### Infrastructure & Deployment
- **Docker** (containerization)
- **Vercel** (Static Frontend hosting)
- **Render / Self-hosted VM** (Backend hosting)

---

## 📁 Repository Structure

### Backend — agrilink-backend

```
agrilink/
├── src/main/java/com/agrilink/
│   ├── AgrilinkApplication.java      # Spring Boot application entry point
│   ├── config/                       # Security & CORS configuration
│   ├── controller/                   # REST controllers (Auth, Job, App, Profile, Review)
│   ├── dto/                          # Request/Response DTO models
│   ├── entity/                       # JPA entities (User, Job, Application, Review, Profile)
│   ├── exception/                    # Global exception handlers
│   ├── repository/                   # Spring Data JPA repositories
│   └── service/                      # Business logic implementations
├── src/main/resources/
│   └── application.properties        # Application configuration
├── Dockerfile                        # Docker container specification
└── pom.xml                           # Maven dependencies and build configuration
```

---

## 🚀 Getting Started

### Prerequisites
Make sure the following are installed:
- **Java 17+**
- **Maven**
- **MySQL** or **PostgreSQL** database running locally
- **Docker Desktop** (Optional, for containerized running)

### 1. Clone both repositories
```bash
git clone https://github.com/Sanjay9999607/agrilink-backend.git
git clone https://github.com/Sanjay9999607/agrilink-frontend.git
```

### 2. Run the backend locally
```bash
cd agrilink-backend
mvn clean package
mvn spring-boot:run
```
The backend REST API will run at: `http://localhost:8090`

### 3. Serve the frontend locally
No complex build steps or dependencies are required. You can run the frontend by opening the `index.html` file directly in your browser, or serving it with a static web server:

Using Python:
```bash
cd agrilink-frontend
python -m http.server 3000
```

Using Node (npx):
```bash
cd agrilink-frontend
npx serve -l 3000
```
Open `http://localhost:3000` in your web browser.

---

## ☁️ Production Deployment

### Backend Deployment (Render)
Configure your `application.properties` dynamically via environment variables (e.g. `SPRING_DATASOURCE_URL`, `PORT`).

### Frontend Deployment (Vercel)
Deploy `index.html` as a static project. Vercel hosts single-page static files out of the box with zero build configuration.

---

## 📡 API Overview

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/auth/register` | Register a new landowner or labourer account |
| **POST** | `/auth/login` | Log in and receive a JWT token |
| **GET** | `/profile` | Retrieve the authenticated user's profile |
| **PUT** | `/profile` | Update profile information |
| **POST** | `/jobs` | Post a new job listing (Landowners only) |
| **GET** | `/jobs` | Retrieve nearby job listings matching the labourer's location |
| **GET** | `/jobs/my` | Retrieve all jobs posted by the logged-in landowner |
| **GET** | `/jobs/{id}` | Get detailed view of one specific job |
| **GET** | `/jobs/{id}/applications` | View all applications received for a job |
| **POST** | `/applications/{jobId}` | Apply for an agricultural job (Labourers only) |
| **PUT** | `/applications/{id}/status` | Accept or reject a job application (Landowners only) |
| **GET** | `/applications/my` | Retrieve job applications submitted by the logged-in labourer |
| **GET** | `/applications/notifications` | Fetch pending notifications for a labourer |
| **GET** | `/applications/notifications/landowner` | Fetch pending notifications for a landowner |
| **POST** | `/reviews` | Submit post-job feedback and rating |
| **GET** | `/reviews/user/{userId}` | Fetch reviews for a specific user |
| **GET** | `/reviews/my` | Retrieve reviews written about the current user |
| **GET** | `/dashboard` | Retrieve statistics, application count, and recent activities |

### Example Request
```bash
curl -X POST http://localhost:8090/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name": "Sanjay", "email": "sanjay@example.com", "password": "password123", "role": "LANDOWNER"}'
```

---

## 📊 Dashboard Features

The frontend dashboard includes:
- **Summary Cards**: Displaying total jobs, active jobs, and recent applications.
- **Notification Badges**: For both landowners (pending applications count) and labourers (accepted applications count).
- **Settings Toggle**: Easy control settings to manage comments or view feedback.
- **Responsive Layout**: Adjusts to mobile and desktop resolutions with real-time updates as status changes.

---

## 🧪 Usage Examples

### Creating a Job (Landowner)
Landowners can fill out the job form specifying wage, location, required skills, and job description to broadcast to nearby labourers.

### Applying for a Job (Labourer)
Labourers browse available jobs matching their category/wage criteria, click "Apply", and instantly notify the landowner.

---

## ⚙️ Configuration

| Parameter | Purpose | Set via |
| :--- | :--- | :--- |
| `spring.datasource.url` | Database connection URL | `application.properties` / Environment variables |
| `spring.datasource.username` | Database username | `application.properties` |
| `spring.datasource.password` | Database password | `application.properties` |
| `spring.datasource.driver-class-name` | Database driver class name | `application.properties` |
| `spring.jpa.hibernate.ddl-auto` | Hibernate DDL generation strategy | `application.properties` |
| `server.port` | Port number the backend runs on (Default: 8090) | `application.properties` / Environment variables |
| `jwt.secret` | Secret key for signing/verifying JWTs | `application.properties` |
| `jwt.expiration` | Token lifetime duration in milliseconds | `application.properties` |

---

## 🔒 Security

- **JWT Authentication**: Secure token-based validation on all non-public endpoints.
- **Role-Based Access Control**: Strict separation of privileges. Only landowners can post jobs and modify application statuses; only labourers can apply.
- **CORS Configured**: Allowed origins mapped properly to prevent unauthorized external requests.

---

## 🐛 Troubleshooting

- **"Failed to fetch" or CORS errors**: Ensure the frontend host is listed in the backend's allowed CORS origins or configured in `CorsConfig`.
- **Database Connection Failure**: Check that your PostgreSQL or MySQL database is active, and the credentials in `application.properties` are correct.
- **Port Conflict (8090)**: If port 8090 is already in use on your machine, modify `server.port` in `application.properties` or run with a `PORT` environment variable.

---

## 📝 Known Limitations / Future Work

- **Password Encryption**: Integration of `BCryptPasswordEncoder` for production-grade security.
- **Search Optimization**: Advanced search filters (e.g. proximity/distance range queries).
- **Image Uploads**: Support for profile pictures or job site photos.

---

## 📄 License

This project is intended for educational, demo, and portfolio use.

---

## 🙏 Acknowledgments

- The open-source communities behind **Spring Boot**, **Vanilla JavaScript**, and **Hibernate**.
- **Vercel** and **Render** for hosting services.

---

## 👤 Author

**Sanjay**
