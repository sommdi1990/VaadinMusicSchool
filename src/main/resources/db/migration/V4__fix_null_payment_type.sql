-- Fix NULL payment_type values in payments table
-- Add payment_type column with default if not exists, then make NOT NULL (for legacy data)
ALTER TABLE payments ADD COLUMN IF NOT EXISTS payment_type VARCHAR(50);
UPDATE payments SET payment_type = 'COURSE_FEE' WHERE payment_type IS NULL;
ALTER TABLE payments ALTER COLUMN payment_type SET NOT NULL;