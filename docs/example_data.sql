-- Insert into users table
INSERT INTO users (id, name, email, role, created_at, updated_at)
VALUES
  ('d16a5cc6-213f-47a2-9b6a-e83cf6608b63', 'John Doe', 'john.doe@example.com', 'Instructor', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('f49e4178-5986-4161-8e69-b1e7cf6f3d34', 'Jane Smith', 'jane.smith@student.com', 'Student', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert into exam table
INSERT INTO exam (id, title, description, instructions, duration_minutes, total_marks, no_question_per_level, start_time, end_time, creator_id, created_at, updated_at)
VALUES
  ('3f4a9edb-8e90-4a1f-b2fe-e95f3d643849', 'Math Exam', 'This is a sample Math exam', ARRAY['Instruction 1', 'Instruction 2'], '01:30:00', 100, ARRAY[10, 15, 20, 5], '2025-08-01 09:00:00', '2025-08-01 10:30:00', 'd16a5cc6-213f-47a2-9b6a-e83cf6608b63', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('0d1f0c8e-e95c-4e9e-a1da-d57de39fe62f', 'History Exam', 'This is a sample History exam', ARRAY['Instruction 1', 'Instruction 2'], '01:00:00', 80, ARRAY[8, 12, 15, 5], '2025-08-02 10:00:00', '2025-08-02 11:00:00', 'd16a5cc6-213f-47a2-9b6a-e83cf6608b63', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert into question table
INSERT INTO question (id, exam_id, question_type, question_text, question_image_url, correct_answer, suggested_answer, weight, difficulty, show_options, created_at, updated_at)
VALUES
  ('5cbf8c8b-d0ac-4ed1-b80e-235e60968d89', '3f4a9edb-8e90-4a1f-b2fe-e95f3d643849', 'MCQ', 'What is 2 + 2?', NULL, 'A', ARRAY['A: 4', 'B: 5', 'C: 6', 'D: 3'], 10, 'Easy', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('da23cf02-f3a3-4c56-8a1b-746de34c54db', '0d1f0c8e-e95c-4e9e-a1da-d57de39fe62f', 'Subjective', 'Describe the causes of World War II.', NULL, NULL, ARRAY['Causes include nationalism, imperialism, etc.'], 20, 'Hard', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert into question_option table
INSERT INTO question_option (id, question_id, option_label, option, image_url)
VALUES
  ('f1ba20d1-19d5-4bda-bf8f-6a0d9d1c8c8a', '5cbf8c8b-d0ac-4ed1-b80e-235e60968d89', 'A', '4', NULL),
  ('34cd081a-3a44-4334-bf90-38588c871ef2', '5cbf8c8b-d0ac-4ed1-b80e-235e60968d89', 'B', '5', NULL),
  ('f91efb18-051b-4a62-bbbe-beb9d4ccf4ba', '5cbf8c8b-d0ac-4ed1-b80e-235e60968d89', 'C', '6', NULL),
  ('9a0b72f7-c1bb-4b06-b510-5d216b7399d0', '5cbf8c8b-d0ac-4ed1-b80e-235e60968d89', 'D', '3', NULL);

-- Insert into mark_scheme table
INSERT INTO mark_scheme (id, difficulty, mark, exam_id, updated_at)
VALUES
  ('58e2f592-5b8a-4fa5-b900-d90f9e5c05d9', 'Easy', 10, '3f4a9edb-8e90-4a1f-b2fe-e95f3d643849', CURRENT_TIMESTAMP),
  ('dff69a59-234e-44e3-b075-9c3cb6e2d460', 'Medium', 20, '3f4a9edb-8e90-4a1f-b2fe-e95f3d643849', CURRENT_TIMESTAMP);

-- Insert into student_exam_assigned table
INSERT INTO student_exam_assigned (id, student_id, exam_id, status, score, start_time, completion_time, creator_id, assign_at)
VALUES
  ('ab12b6d1-c1a2-4b07-951d-90710d7ef94a', 'f49e4178-5986-4161-8e69-b1e7cf6f3d34', '3f4a9edb-8e90-4a1f-b2fe-e95f3d643849', 'Pending', NULL, '2025-08-01 09:00:00', NULL, 'd16a5cc6-213f-47a2-9b6a-e83cf6608b63', CURRENT_TIMESTAMP),
  ('a01b9e8e-f231-47da-ae47-f893d0f8c4c2', 'f49e4178-5986-4161-8e69-b1e7cf6f3d34', '0d1f0c8e-e95c-4e9e-a1da-d57de39fe62f', 'Generated', NULL, '2025-08-02 10:00:00', NULL, 'd16a5cc6-213f-47a2-9b6a-e83cf6608b63', CURRENT_TIMESTAMP);

-- Insert into student_exam_questions table
INSERT INTO student_exam_questions (id, exam_assigned_id, question_id, exam_id, evaluation, assign_at)
VALUES
  ('b0f410b9-d75a-4e73-a44c-899fc107e0db', 'ab12b6d1-c1a2-4b07-951d-90710d7ef94a', '5cbf8c8b-d0ac-4ed1-b80e-235e60968d89', '3f4a9edb-8e90-4a1f-b2fe-e95f3d643849', NULL, CURRENT_TIMESTAMP),
  ('b9c6f271-30e2-4d83-9d01-504b1294a0a7', 'a01b9e8e-f231-47da-ae47-f893d0f8c4c2', 'da23cf02-f3a3-4c56-8a1b-746de34c54db', '0d1f0c8e-e95c-4e9e-a1da-d57de39fe62f', NULL, CURRENT_TIMESTAMP);

-- Insert into student_answer table
INSERT INTO student_answer (id, submission_id, selected_option, subjective_answer, mode, submitted_at)
VALUES
  ('d728c81b-0a66-47b9-b0b5-d4409c3a71c5', 'b0f410b9-d75a-4e73-a44c-899fc107e0db', 'A', NULL, 'Answered', CURRENT_TIMESTAMP),
  ('7598a92b-f1bb-40f0-b6c0-7c615ddfcdbd', 'b9c6f271-30e2-4d83-9d01-504b1294a0a7', NULL, 'The causes include nationalism, imperialism, and economic interests.', 'NotAnswered', CURRENT_TIMESTAMP);

