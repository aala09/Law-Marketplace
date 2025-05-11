CREATE TABLE lawyer_availability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lawyer_id BIGINT NOT NULL,
    day_of_week VARCHAR(20),
    specific_date DATE,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (lawyer_id) REFERENCES users(id)
);

-- Insert some sample data for testing
INSERT INTO lawyer_availability (lawyer_id, day_of_week, start_time, end_time)
VALUES 
    (1, 'MONDAY', '09:00:00', '12:00:00'),
    (1, 'MONDAY', '14:00:00', '17:00:00'),
    (1, 'WEDNESDAY', '09:00:00', '17:00:00'),
    (1, 'FRIDAY', '09:00:00', '15:00:00');

-- If you have more lawyer users, you can add their availability too
-- For example, for lawyer with ID 2
INSERT INTO lawyer_availability (lawyer_id, day_of_week, start_time, end_time)
VALUES 
    (2, 'TUESDAY', '10:00:00', '16:00:00'),
    (2, 'THURSDAY', '08:00:00', '14:00:00');
