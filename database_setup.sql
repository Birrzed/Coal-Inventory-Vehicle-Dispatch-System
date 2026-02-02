CREATE DATABASE IF NOT EXISTS dispatch_system;
USE dispatch_system;

-- 1. Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- 2. Dispatch table
CREATE TABLE IF NOT EXISTS dispatch (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_mass DOUBLE NOT NULL,
    dispatch_date DATE NOT NULL,
    arrival_date DATE,
    status VARCHAR(20) DEFAULT 'Pending',
    seller_id INT,
    transporter_id INT,
    destination_id INT,
    FOREIGN KEY (seller_id) REFERENCES users(id),
    FOREIGN KEY (transporter_id) REFERENCES users(id),
    FOREIGN KEY (destination_id) REFERENCES users(id)
);

-- 3. Payment table
CREATE TABLE IF NOT EXISTS payment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dispatch_id INT NOT NULL UNIQUE,
    amount DOUBLE NOT NULL,
    payment_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Paid',
    FOREIGN KEY (dispatch_id) REFERENCES dispatch(id)
);

-- Initial Data
INSERT INTO users (username, password, role) VALUES 
('amen plc', '1$isbester', 'Admin'),
('seller1', '1234', 'Seller'),
('driver1', '1234', 'Transporter'),
('store1', '1234', 'Destination');
