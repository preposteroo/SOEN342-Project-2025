CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100),
    password VARCHAR(100) NOT NULL,
    user_type ENUM('ADMIN','INSTRUCTOR','CLIENT')
);

CREATE TABLE instructors (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    specialization VARCHAR(25),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE clients (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE city (
    id INT PRIMARY KEY AUTO_INCREMENT,
    city_name VARCHAR(255) NOT NULL
);

CREATE TABLE instructor_cities (
    instructor_id INT,
    city_id INT,
    PRIMARY KEY (instructor_id, city_id),
    FOREIGN KEY (instructor_id) REFERENCES instructors(id),
    FOREIGN KEY (city_id) REFERENCES city(id)
);