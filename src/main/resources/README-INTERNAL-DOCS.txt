Library Management System - Internal Notes

- Entities: Book, IssuedBook
- DAO Layer: BookDao, IssuedBookDao (pure JDBC; prepared statements; minimal surface)
- Service Layer: LibraryService (coordinates transactions for Issue/Return using a single Connection)
- Utilities: DbUtil (centralized connection factory via db.properties), DateTimeUtil (single method now() used in Issue/Return)
- App: Main (console UI; enhanced switch for menu)

Transactions
- Issue: decrement books.quantity then insert into issued_books
- Return: update issued_books.return_date then increment books.quantity

Database
- MySQL with FK from issued_books.book_id to books.book_id (ON DELETE RESTRICT)
- Quantities are non-negative (CHECK constraint)

Error Handling
- User-friendly strings returned from service and printed by Main
- Validation for integer inputs in Main