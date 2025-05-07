-- Update availability table if needed
ALTER TABLE availability ADD COLUMN subject_id BIGINT NOT NULL;
ALTER TABLE availability ADD CONSTRAINT fk_availability_subject FOREIGN KEY (subject_id) REFERENCES subject(subject_id);

-- If start_time/end_time columns still exist, drop them
-- Note: Only run this if the previous ALTER TABLE from Dump20250423.sql didn't work
-- ALTER TABLE availability DROP COLUMN start_time, DROP COLUMN end_time;