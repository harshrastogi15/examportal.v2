# 🧪 Exam Portal (v2) - In Progress
An online **exam management system** built with **Spring Boot (backend)** and **React (frontend)**.
It enables admins to create and assign exams, manage questions, upload images, and evaluate results, while enabling students to securely attempt randomized sets of questions. Built with a focus on scalability, confidentiality, and ease of use.

This is an upgraded version of the [original Exam Portal](https://github.com/harshrastogi15/examportal), redesigned with a modern full-stack architecture using **React**, **Spring Boot**, **PostgreSQL**.

## 📚 Features Upcoming
- **User Management**: Create and manage students/admins with role-based access.
- **Exam Management**: Create, update, delete, copy, and list exams with flexible settings (marks, difficulty, instructions, etc.).
- **Question Bank**: Add subjective/MCQ questions, attach images, update/delete, and fetch all questions.
- **Image Handling**: Upload and retrieve question-related images via MinIO object storage.
- **Exam Assignment**: Assign exams to specific students, remove assignments, and track allocation.
- **Exam Attempts**: Start exam, attempt questions, and auto-submit responses.
- **Result Generation**: Automated result generation for MCQs, manual scoring for subjective, and final result calculation.

## 🚀 Features in Future
- **Copy Exam Set**: Copy complete exam set with questions to create new exam and modify it based on need.


## 🛠️ Tech Stack & Dependencies

### **Backend (Spring Boot - Java)**
- **Spring Boot** – Core backend framework for building REST APIs.
- **Spring Data JPA (Hibernate)** – ORM for mapping Java objects to PostgreSQL database tables.
- **PostgreSQL** – Relational database to store users, exams, questions, attempts, and results.
- **MinIO** – Object storage service for handling image uploads (e.g., question images, diagrams).
- **Lombok** – Reduces boilerplate code with annotations like `@Getter`, `@Setter`, `@Builder`.
<!-- - **Keycloak** – Authentication and role-based access control (ADMIN, STUDENT). -->
<!-- - **JUnit & Mockito** – For unit testing services, repositories, and controllers. -->

### **Frontend (React)**
- Updated soon
<!-- - **ReactJS** – SPA frontend for students/admins. -->
<!-- - **Axios / Fetch API** – For making API calls to the backend. -->
<!-- - **TailwindCSS** – UI styling for a clean and responsive layout. -->

### **Other Tools**
- **Docker** – For containerizing services like PostgreSQL and MinIO.
- **Postman** – API testing (collection provided in `docs/`).
- **pgAdmin** - for testing the data entry and ensuring consistency in database
- **Maven/Gradle** – Dependency management and build tool for Spring Boot.

## 📂 Project Structure
```bash
examportal.v2/
│
├── backend/
│ ├── src/
│ ├── pom.xml
│
├── frontend/ (updated soon)
│
├── docs/
│
├── README.md
```
## Access

- **Backend API** → [http://localhost:8080](http://localhost:8080)
<!-- - **Frontend App** → [http://localhost:3000](http://localhost:3000) -->

---

## 📖 API Documentation

All backend API endpoints (Users, Exams, Questions, Assignments, Attempts, Results) are documented in:

👉 [docs/api-document.md](docs/api-document.md)

You can also import the Postman collection for ready-to-use requests:
`docs/exam.v2.postman_collection.json`

## 🚀 Getting Started (Coming Soon)
- to be updated


#### For More information, [docs/REAdME.md](docs/README.md).


### 👨‍💻 Developed By

[**Harsh Rastogi**](https://harshrastogi15.github.io/Personal/)<br>
💼 [LinkedIn](https://www.linkedin.com/in/harshrastogi15/)<br>
🐙 [GitHub](https://github.com/harshrastogi15)<br>
