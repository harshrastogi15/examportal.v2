CREATE TYPE user_role_enum AS ENUM ('Instructor', 'Student');
CREATE TYPE difficulty_level_enum AS ENUM ('Hard', 'Medium', 'Easy', 'Subjective');
CREATE TYPE question_type_enum AS ENUM ('MCQ', 'Subjective');
CREATE TYPE option_enum AS ENUM ('A', 'B', 'C', 'D');
CREATE TYPE status_enum AS ENUM ('Pending', 'Generated', 'Completed', 'Result');
CREATE TYPE mode_enum AS ENUM ('Answered', 'NotAnswered');

-- user table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role user_role_enum NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL
);

-- exam table
CREATE TABLE IF NOT EXISTS exam (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    instructions TEXT[],
    duration_minutes TIME NOT NULL,
    total_marks INT NOT NULL,
    no_question_per_level INT[4],
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    creator_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL
);

-- question table
CREATE TABLE IF NOT EXISTS question (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    exam_id UUID NOT NULL REFERENCES exam(id) ON DELETE CASCADE,
    question_type question_type_enum NOT NULL,
    question_text TEXT NOT NULL,
    question_image_url TEXT,
    correct_answer option_enum,
    suggested_answer TEXT[],
    weight INT NOT NULL,
    difficulty difficulty_level_enum,
    show_options BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL
);

-- question_option table
CREATE TABLE IF NOT EXISTS question_option (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    question_id UUID NOT NULL REFERENCES question(id) ON DELETE CASCADE,
    option_label option_enum NOT NULL,
    option TEXT,
    image_url TEXT,
    CONSTRAINT uq_question_option UNIQUE (question_id, option_label)
);

-- mark_scheme table
CREATE TABLE IF NOT EXISTS mark_scheme (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    difficulty difficulty_level_enum,
    mark INT NOT NULL,
    exam_id UUID NOT NULL REFERENCES exam(id) ON DELETE CASCADE,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT uq_exam_mark UNIQUE (exam_id, difficulty)
);

-- student_exam_assigned table
CREATE TABLE IF NOT EXISTS student_exam_assigned (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    exam_id UUID NOT NULL REFERENCES exam(id) ON DELETE CASCADE,
    status status_enum NOT NULL,
    score INT,
    start_time TIMESTAMP,
    completion_time TIMESTAMP,
    creator_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    assign_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_exam_student UNIQUE (exam_id, student_id)
);

-- student_exam_questions table
CREATE TABLE IF NOT EXISTS student_exam_questions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    exam_assigned_id UUID NOT NULL REFERENCES student_exam_assigned(id) ON DELETE CASCADE,
    question_id UUID NOT NULL REFERENCES question(id) ON DELETE CASCADE,
    exam_id UUID NOT NULL REFERENCES exam(id) ON DELETE CASCADE,
    evaluation INT,
    assign_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_exam_exam_assigned_question UNIQUE (exam_assigned_id, question_id)
);

-- student_answer table
CREATE TABLE IF NOT EXISTS student_answer (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES student_exam_questions(id) ON DELETE CASCADE,
    selected_option option_enum,
    subjective_answer TEXT,
    mode mode_enum,
    submitted_at TIMESTAMP NOT NULL,
    CONSTRAINT uq_submission_option UNIQUE (submission_id, selected_option)
);
