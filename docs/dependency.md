🔧 Why These Dependencies?
1. Spring Web
Why: Provides all the necessary components to build RESTful web services (like controllers, request mappings).

Needed for: Exposing endpoints (e.g., /create-exam, /submit-answers).

2. Spring Data JPA
Why: Makes database operations easier using JpaRepository. No need to write boilerplate SQL.

Needed for: Saving exams, questions, answers, etc., to PostgreSQL with minimal effort.

3. PostgreSQL Driver
Why: Required to connect Java (Spring) with PostgreSQL.

Needed for: JDBC communication.

4. Spring Security (later for Keycloak)
Why: Used for securing endpoints and integrating with Keycloak using OAuth2 or OpenID.

Needed for: Authenticating instructor/student roles securely.

5. Lombok
Why: Removes boilerplate (getters/setters/constructors).

Needed for: Keeping entities and DTOs clean.

6. Validation
Why: Ensures input correctness (@NotNull, @Size, etc.).

Needed for: Validating incoming requests (like exam creation).

7. Spring Boot Actuator (Optional)
Why: Helps monitor app health, metrics, etc.

Needed for: Observability (for prod/future scaling).

<!--
Inserting users: Two users (Instructor and Student) are added to the users table.

Inserting exams: Two exams, "Math Exam" and "History Exam", are added to the exam table.

Inserting questions: Two questions are added: one MCQ for the Math exam and one Subjective question for the History exam.

Inserting options: Multiple choice options are added for the MCQ question.

Inserting mark schemes: Mark schemes for different difficulty levels are added for the Math exam.

**Inserting assigned exams for students


 -->

