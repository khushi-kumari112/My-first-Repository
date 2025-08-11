-- Create database (run once)
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- Books table
CREATE TABLE IF NOT EXISTS books (
  book_id INT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  quantity INT NOT NULL CHECK (quantity >= 0)
);

-- Issued books table
CREATE TABLE IF NOT EXISTS issued_books (
  issue_id INT PRIMARY KEY AUTO_INCREMENT,
  book_id INT NOT NULL,
  user_name VARCHAR(255) NOT NULL,
  issue_date DATETIME NOT NULL,
  return_date DATETIME NULL,
  CONSTRAINT fk_issued_book_book FOREIGN KEY (book_id)
    REFERENCES books(book_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

-- Optional: Example data
-- INSERT INTO books (book_id, title, author, quantity) VALUES (1, 'Clean Code', 'Robert C. Martin', 3);