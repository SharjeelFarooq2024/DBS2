-- Check if subject_id column exists in availability table
SELECT COUNT(*) FROM information_schema.columns 
WHERE table_name = 'availability' AND column_name = 'subject_id';

-- If it doesn't exist, add it
ALTER TABLE availability ADD COLUMN subject_id BIGINT;

-- Add foreign key constraint if it doesn't exist
ALTER TABLE availability 
ADD CONSTRAINT fk_availability_subject 
FOREIGN KEY (subject_id) REFERENCES subject(subject_id);

-- Make sure time_slot exists (and start_time/end_time are dropped if they exist)
SELECT COUNT(*) FROM information_schema.columns 
WHERE table_name = 'availability' AND column_name = 'time_slot';

-- If it doesn't exist, add it
ALTER TABLE availability ADD COLUMN time_slot VARCHAR(50);

-- Drop old time columns if they exist
SELECT COUNT(*) FROM information_schema.columns 
WHERE table_name = 'availability' AND column_name = 'start_time';

-- If they exist, drop them
ALTER TABLE availability DROP COLUMN IF EXISTS start_time;
ALTER TABLE availability DROP COLUMN IF EXISTS end_time;