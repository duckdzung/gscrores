<div align="center">

# 🎓 GScores Application

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)

**A modern application for viewing and analyzing student exam scores.**

</div>

---

## 📑 Overview

GScores is a full-stack application designed to help users search, view, and analyze student exam scores. The application consists of a React frontend, Spring Boot backend, and PostgreSQL database - all containerized with Docker for easy setup and deployment.

## 🚀 Setup and Running with Docker

### Prerequisites

- [Docker](https://www.docker.com/get-started) (version 20.10+)
- [Docker Compose](https://docs.docker.com/compose/install/) (version 2.0+)

### Configuration

The application uses environment variables for configuration. Create a `.env` file in the root directory with the following variables:

```env
# 📊 Database Configuration
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=gscores
POSTGRES_PORT=5432

# 🔧 Backend Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres_db:5432/gscores
BACKEND_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:3000

# 🖥️ Frontend Configuration
FRONTEND_PORT=3000
REACT_APP_API_URL=http://localhost:8080/api
```

### 🏃 Running the Application

1. Clone the repository and navigate to the project directory

   ```bash
   git clone <repository-url>
   cd <project-directory>
   ```

2. Start all services using Docker Compose:

   ```bash
   docker-compose up -d
   ```

   <details>
   <summary>What this command does:</summary>

   - 🐘 Builds and starts the PostgreSQL database
   - ☕ Builds and starts the Spring Boot backend
   - ⚛️ Builds and starts the React frontend
   </details>

3. Access the application:
   - 🌐 Frontend: http://localhost:3000
   - 🛠️ Backend API: http://localhost:8080/api

### 🛑 Stopping the Application

To stop all services:

```bash
docker-compose down
```

To stop all services and remove volumes (this will delete the database data):

```bash
docker-compose down -v
```

### 📝 Viewing Logs

To view logs of all services:

```bash
docker-compose logs -f
```

To view logs of a specific service:

```bash
docker-compose logs -f [service_name]
```

Where `[service_name]` can be:

| Service       | Description             |
| ------------- | ----------------------- |
| `postgres_db` | PostgreSQL database     |
| `backend`     | Spring Boot application |
| `frontend`    | React application       |

### 🔄 Rebuilding Services

If you make changes to the code and need to rebuild:

```bash
docker-compose build [service_name]
docker-compose up -d [service_name]
```

### ⚠️ Troubleshooting

<details>
<summary><b>Database connection issues</b></summary>

- Ensure the PostgreSQL container is running:
  ```bash
  docker ps | grep postgres
  ```
- Check database logs:
  ```bash
  docker-compose logs postgres_db
  ```
  </details>

<details>
<summary><b>Backend not starting</b></summary>

- Check backend logs:
  ```bash
  docker-compose logs backend
  ```
- Verify environment variables in `.env` match your configuration
</details>

<details>
<summary><b>Frontend issues</b></summary>

- Check frontend logs:
  ```bash
  docker-compose logs frontend
  ```
- Ensure the `REACT_APP_API_URL` is correctly set in the `.env` file
</details>

## 📦 Project Structure

```
gscores/
├── docker-compose.yml     # Docker Compose configuration
├── .env                   # Environment variables
├── gscores-api/           # Spring Boot backend
│   ├── Dockerfile
│   └── src/
└── gscores-ui/            # React frontend
    ├── Dockerfile
    └── src/
```

## 👨‍💻 Development

### Adding Data to the Database

The application automatically processes the exam scores from the CSV data file during startup:

- Data file: `gscores-api/src/main/resources/dataset/diem_thi_thpt_2024.csv`

### Useful Development Commands

- Check the application health:

  ```bash
  curl http://localhost:8080/api/actuator/health
  ```

- Follow application logs during development:

  ```bash
  docker-compose logs -f
  ```

- Rebuild all containers after code changes:
  ```bash
  docker-compose up --build -d
  ```

## 📚 Features

- 🔍 Search for student scores by registration number
- 📊 View statistical reports of exam scores across different subjects
- 🏆 See top 10 students from Group A (Math, Physics, Chemistry)
- 📱 Responsive design that works on all devices

---

<div align="center">

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

</div>
