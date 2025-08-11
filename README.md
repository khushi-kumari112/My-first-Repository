# Library Management System (JDBC + MySQL)

Simple console application built with Java 17, JDBC, and MySQL.

## Tech
- Language: Java (Core)
- Database: MySQL
- Connectivity: JDBC
- Build tool: Maven

## Folder Hierarchy
```
/workspace
├── pom.xml
├── scripts
│   └── mysql_schema.sql
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── library
│       │               ├── app
│       │               │   └── Main.java
│       │               ├── dao
│       │               │   ├── BookDao.java
│       │               │   └── IssuedBookDao.java
│       │               ├── entity
│       │               │   ├── Book.java
│       │               │   └── IssuedBook.java
│       │               └── service
│       │                   └── LibraryService.java
│       └── resources
│           └── db.properties
```

## 1) MySQL Setup (Workbench or CLI)
Run the following in MySQL Workbench (or `mysql` CLI):

```sql
SOURCE /absolute/path/to/workspace/scripts/mysql_schema.sql;
```

Or paste commands directly:

```sql
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

CREATE TABLE IF NOT EXISTS books (
  book_id INT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  quantity INT NOT NULL CHECK (quantity >= 0)
);

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
```

## 2) Configure DB Credentials
Edit `src/main/resources/db.properties`:

```
db.url=jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=your_mysql_username
db.password=your_mysql_password
```

## 3) Build and Run (VS Code or Terminal)
- In VS Code, open the folder and use the Maven extension or run commands below in terminal at the project root:

```bash
mvn -q clean package
mvn -q exec:java -Dexec.mainClass=com.example.library.app.Main
```

If you prefer `java -cp`, first copy dependencies to a folder:

```bash
mvn -q dependency:copy-dependencies -DoutputDirectory=target/dependency
java -cp "target/classes:target/dependency/*" com.example.library.app.Main
```

## 4) App Menu (Enhanced Switch)
```
[Main Menu]
1. Add Book
2. View All Books
3. Issue Book (create a method which returns the date and time)
4. Return Book (use the same method)
5. Delete Book
6. Exit
```

- The shared method for date/time is `DateTimeUtil.now()` used for both issue and return.

## 5) Example Usage
- Add Book
```
Enter Book ID (int): 101
Enter Title: Effective Java
Enter Author: Joshua Bloch
Enter Quantity (int): 2
Book added successfully.
```

- View Books
```
Books:
book_id | title | author | quantity
101 | Effective Java | Joshua Bloch | 2
```

- Issue Book
```
Enter Book ID to issue: 101
Enter User Name: Alice
Book issued. Issue ID: 1, Issue DateTime: 2025-08-11T12:34:56.789
```

- Return Book
```
Enter Issue ID to return: 1
Book returned at: 2025-08-11T13:40:01.123
```

- Delete Book (only possible if no outstanding issues; FK restricts delete while issued)
```
Enter Book ID to delete: 101
Book deleted.
```

## Notes
- All JDBC calls use PreparedStatements.
- Issue and Return are transactional: quantity change + issue/return record happen together.
- If you see an error about timezone, ensure `serverTimezone=UTC` is present in the JDBC URL.
- Requires Java 17+.
