## 📂 API Modules

### 1. **User**
- `GET /user/detail` – Get current user details
- `GET /user/students` – Fetch all students
- `GET /user/student/detail` – Fetch student detail (ID required)
- `POST /user` – Create new user (student/teacher)

### 2. **Exam**
- `POST /exam` – Create exam
- `GET /exam` – Get exam detail
- `PUT /exam` – Update exam
- `DELETE /exam` – Delete exam
- `GET /exam/all` – Get all exams
<!-- - `POST /exam/copy` – Copy an existing exam with questions not in current cycle -->

### 3. **Questions**
- `POST /question` – Create question
- `GET /question/{id}` – Get question details
- `PUT /question` – Update question
- `DELETE /question` – Delete question
- `GET /question/all` – Get all questions
- `POST /upload` – Upload image for question
- `DELETE /upload` – Delete image

### 4. **Assign Exam**
- `POST /assign/exam` – Assign exam to users
- `DELETE /assign/exam/user` – Remove assigned exam from a user

### 5. **User Attempt**
- `GET /attempt/start` – Start exam for user

### 6. **Generate Result**
- `POST /generate/result/mcq` – Auto-generate MCQ results
- `POST /generate/result/subjective` – Mark subjective answers
- `POST /generate/result` – Compile final result

### 7. **User Result**
- `GET /result` – Fetch user result
