CREATE TABLE IF NOT EXISTS patient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    last_name VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL,
    genre VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(20)
    );