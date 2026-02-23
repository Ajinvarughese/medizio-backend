CREATE TABLE speciality (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO speciality (name) VALUES
('General Physician'),
('Cardiologist'),
('Neurologist'),
('Dermatologist'),
('Orthopedic'),
('Pediatrician'),
('Gynecologist'),
('Psychiatrist'),
('Radiologist'),
('Surgeon');