-- Add missing columns to consultations table (if they don't exist)
ALTER TABLE consultations
ADD COLUMN IF NOT EXISTS lawyer_id BIGINT,
ADD COLUMN IF NOT EXISTS client_id BIGINT,
ADD COLUMN IF NOT EXISTS slot_start DATETIME,
ADD COLUMN IF NOT EXISTS duree_minutes INT,
ADD COLUMN IF NOT EXISTS status VARCHAR(50),
ADD COLUMN IF NOT EXISTS notes TEXT;

-- Check if the foreign key constraints exist before adding them
SET @fk_lawyer_exists = (
    SELECT COUNT(*)
    FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_NAME = 'FK_consultations_lawyer'
    AND TABLE_NAME = 'consultations'
    AND TABLE_SCHEMA = DATABASE()
);

SET @fk_client_exists = (
    SELECT COUNT(*)
    FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_NAME = 'FK_consultations_client'
    AND TABLE_NAME = 'consultations'
    AND TABLE_SCHEMA = DATABASE()
);

-- Only add the foreign key if it doesn't exist
SET @sql_lawyer = IF(@fk_lawyer_exists = 0, 
    'ALTER TABLE consultations ADD CONSTRAINT FK_consultations_lawyer FOREIGN KEY (lawyer_id) REFERENCES users(id)',
    'SELECT 1');
PREPARE stmt_lawyer FROM @sql_lawyer;
EXECUTE stmt_lawyer;
DEALLOCATE PREPARE stmt_lawyer;

-- Only add the foreign key if it doesn't exist
SET @sql_client = IF(@fk_client_exists = 0,
    'ALTER TABLE consultations ADD CONSTRAINT FK_consultations_client FOREIGN KEY (client_id) REFERENCES users(id)',
    'SELECT 1');
PREPARE stmt_client FROM @sql_client;
EXECUTE stmt_client;
DEALLOCATE PREPARE stmt_client;

-- Remove the problematic statement that references dateHeure
-- The Java entity has backward compatibility methods but the DB column doesn't exist
