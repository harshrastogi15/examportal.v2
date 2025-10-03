## 📂 API Modules

### 1. **User**

- `GET /user/detail` – Get current user details
- `GET /user/students` – Fetch all students
- `GET /user/student/detail` – Fetch student detail (ID required)
- `POST /user` – Create new user (student)

> ⚠️ **Note:** Creating a new **Instructor** user is **not possible via API calls**.
> Please use **pgAdmin** to add instructors manually.
> In the future, this functionality may be managed by an **Admin user** through APIs.

### 2. **Exam**

- `POST /exam` – Create exam
- `GET /exam` – Get exam detail
- `PUT /exam` – Update exam
- `DELETE /exam` – Delete exam
- `GET /exam/all` – Get all exams
- `POST /exam/ready` – if exam is ready
<!-- - `POST /exam/copy` – Copy an existing exam with questions not in current cycle -->

### 3. **Questions**

- `POST /question` – Create question
- `GET /question?id={id}` – Get question details
- `PUT /question` – Update question
- `DELETE /question` – Delete question
- `GET /question/all` – Get all questions
- `PUT /image` – Upload image for question
- `DELETE /image` – Delete image
- `GET /image/{token}` – Retrieve image

### 4. **Assign Exam**

- `POST /assign/exam` – Assign exam to users
- `DELETE /assign/exam` – Remove assigned exam

### 5. **User Attempt**

- `POST /attempt/` – Start exam (requires `assignmentId`)
- `GET /attempt/question?assignmentId={id}&userquestionId={id}` – Fetch question during attempt
- `POST /attempt/answer?assignmentId={id}&userquestionId={id}` – Submit user’s answer

### 6. **Generate Result**

- `POST /result/calculate/mcq` – Auto-calculate MCQ results
- `POST /result/calculate/subjective` – Mark subjective answers manually
- `POST /result/generate` – Compile final result

### 7. **User Result**

- `GET /result?assignmentId={id}` – Fetch result for a specific assignment

## 📦 Postman Collection

For detailed request/response payloads, import the Postman collections:

👉 [exam.v2.postman_collection.json](exam.v2.postman_collection.json) <br>
👉 [exam.v2.postman_environment.json](exam.v2.postman_environment.json)

---

## 🔑 Authentication & Roles

<!-- - Uses **Keycloak JWT** for authentication. -->

- Roles:
  - **INSTRUCTOR** → Manage users, exams, questions, assignments, results
  - **STUDENT** → Attempt exams, view results
