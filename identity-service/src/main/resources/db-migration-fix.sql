-- Fix for existing user records in the database
-- Run this in your PostgreSQL database before starting the application

-- First, add the columns as nullable
ALTER TABLE users ADD COLUMN IF NOT EXISTS failed_attempts INTEGER;
ALTER TABLE users ADD COLUMN IF NOT EXISTS account_locked BOOLEAN;

-- Update existing records with default values
UPDATE users SET failed_attempts = 0 WHERE failed_attempts IS NULL;
UPDATE users SET account_locked = false WHERE account_locked IS NULL;

-- Now make them NOT NULL
ALTER TABLE users ALTER COLUMN failed_attempts SET NOT NULL;
ALTER TABLE users ALTER COLUMN account_locked SET NOT NULL;

-- Set default values for future inserts
ALTER TABLE users ALTER COLUMN failed_attempts SET DEFAULT 0;
ALTER TABLE users ALTER COLUMN account_locked SET DEFAULT false;
