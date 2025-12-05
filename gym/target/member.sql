CREATE DATABASE gym_db;
use gym_db;
CREATE TABLE members (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(100) NOT NULL,
    membership_type_id VARCHAR(100) NOT NULL,
    joining_date DATE NOT NULL,
    status VARCHAR(100)
);