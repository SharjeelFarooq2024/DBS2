-- Insert test users
INSERT INTO `user` (`name`, `email`, `password`, `role`, `created_at`) 
VALUES ('Mahad', 'mahad@gmail.com', 'password123', 'TUTOR', NOW());

INSERT INTO `tutor` (`id`)
VALUES (LAST_INSERT_ID());

INSERT INTO `user` (`name`, `email`, `password`, `role`, `created_at`) 
VALUES ('Ali', 'ali@gmail.com', 'password123', 'STUDENT', NOW());

INSERT INTO `student` (`id`, `grade`)
VALUES (LAST_INSERT_ID(), '10th Grade');

-- Insert tutor subjects for Mahad
INSERT INTO `tutor_subject` (`tutor_id`, `subject_id`, `experience`)
SELECT 
    (SELECT `user_id` FROM `user` WHERE `email` = 'mahad@gmail.com'), 
    `subject_id`, 
    '5 years' 
FROM `subject` 
WHERE `name` IN ('Mathematics', 'Physics');