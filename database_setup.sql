CREATE DATABASE IF NOT EXISTS mydatabase;

USE mydatabase;

CREATE TABLE IF NOT EXISTS students (

    id INT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL

);

INSERT INTO students (name)
SELECT 'Alice' WHERE NOT EXISTS (SELECT 1 FROM students WHERE name = 'Alice');

INSERT INTO students (name)
SELECT 'Bob' WHERE NOT EXISTS (SELECT 1 FROM students WHERE name = 'Bob');

INSERT INTO students (name)
SELECT 'Charlie' WHERE NOT EXISTS (SELECT 1 FROM students WHERE name = 'Charlie');

INSERT INTO students (name)
SELECT 'Diana' WHERE NOT EXISTS (SELECT 1 FROM students WHERE name = 'Diana');

INSERT INTO students (name)
SELECT 'Ethan' WHERE NOT EXISTS (SELECT 1 FROM students WHERE name = 'Ethan');

SELECT * FROM students;
