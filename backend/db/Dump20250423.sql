create database tms;
use tms;

CREATE TABLE user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    UNIQUE KEY uk_email (email)
);

CREATE TABLE student (
    id BIGINT PRIMARY KEY,
    grade VARCHAR(50),
    FOREIGN KEY (id) REFERENCES user(user_id)
);

CREATE TABLE tutor (
    id BIGINT PRIMARY KEY,
    specialization VARCHAR(255),
    qualifications TEXT,
    FOREIGN KEY (id) REFERENCES user(user_id)
);

CREATE TABLE subject (
    subject_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    UNIQUE KEY uk_name (name)
);

CREATE TABLE availability (
    availability_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tutor_id BIGINT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (tutor_id) REFERENCES tutor(id)
);

CREATE TABLE student_subject (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id),
    UNIQUE KEY uk_student_subject (student_id, subject_id)
);

CREATE TABLE tutor_subject (
    tutor_subject_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tutor_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    experience VARCHAR(255),
    FOREIGN KEY (tutor_id) REFERENCES tutor(id),
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id),
    UNIQUE KEY uk_tutor_subject (tutor_id, subject_id)
);

CREATE TABLE tutoring_request (
    request_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id)
);

CREATE TABLE job (
    job_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    request_id BIGINT,
    tutor_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    status VARCHAR(50),
    assigned_at TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES tutoring_request(request_id),
    FOREIGN KEY (tutor_id) REFERENCES tutor(id),
    FOREIGN KEY (student_id) REFERENCES student(id)
);

CREATE TABLE rating_feedback (
    feedback_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    rating INT,
    comment TEXT,
    timestamp TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (job_id) REFERENCES job(job_id)
);

ALTER TABLE availability
DROP COLUMN time_slot,
DROP COLUMN start_time,
DROP COLUMN end_time,
ADD COLUMN time_slot VARCHAR(50) NOT NULL;


INSERT INTO subject VALUES (1,'Mathematics','Science'),(2,'English','Language'),(3,'Physics','Science'),(4,'Chemistry','Science'),(5,'Biology','Science'),(6,'Computer Science','Technology'),(7,'Urdu','Language'),(8,'Islamic Studies','Humanities'),(9,'Geography','Social Studies'),(10,'History','Social Studies');

DROP TABLE student_request;
DROP TABLE student_request;

RENAME TABLE tutoring_request TO student_request;