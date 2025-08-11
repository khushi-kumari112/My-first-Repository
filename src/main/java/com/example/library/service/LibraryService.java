package com.example.library.service;

import com.example.library.dao.BookDao;
import com.example.library.dao.IssuedBookDao;
import com.example.library.entity.Book;
import com.example.library.entity.IssuedBook;
import com.example.library.util.DbUtil;
import com.example.library.util.DateTimeUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer that coordinates DAO operations and enforces simple business rules.
 */
public class LibraryService {

    private final BookDao bookDao;
    private final IssuedBookDao issuedBookDao;

    public LibraryService() {
        this.bookDao = new BookDao();
        this.issuedBookDao = new IssuedBookDao();
    }

    /** Adds a new book record. Returns message describing the outcome. */
    public String addBook(Book book) {
        try {
            boolean ok = bookDao.insert(book);
            return ok ? "Book added successfully." : "Book ID already exists. Choose a different ID.";
        } catch (SQLException e) {
            return "Failed to add book: " + e.getMessage();
        }
    }

    /** Returns all books. */
    public List<Book> viewAllBooks() throws SQLException {
        return bookDao.findAll();
    }

    /** Deletes a book by id. Returns message describing the outcome. */
    public String deleteBook(int bookId) {
        try {
            boolean ok = bookDao.deleteById(bookId);
            return ok ? "Book deleted." : "No book found with given ID, or book is currently issued.";
        } catch (SQLException e) {
            return "Failed to delete book: " + e.getMessage();
        }
    }

    /**
     * Issues a book to a user. Decrements quantity and creates an issue record in a single transaction.
     */
    public String issueBook(int bookId, String userName) {
        try (Connection conn = DbUtil.getConnection()) {
            conn.setAutoCommit(false);
            boolean decremented = bookDao.decrementQuantity(conn, bookId);
            if (!decremented) {
                conn.rollback();
                return "Cannot issue: book not found or no available copies.";
            }
            LocalDateTime now = DateTimeUtil.now();
            int issueId = issuedBookDao.insertIssue(conn, bookId, userName, now);
            conn.commit();
            return "Book issued. Issue ID: " + issueId + ", Issue DateTime: " + now;
        } catch (SQLException e) {
            return "Failed to issue book: " + e.getMessage();
        }
    }

    /**
     * Returns a book by issue id. Marks return date and increments quantity in a single transaction.
     */
    public String returnBook(int issueId) {
        try (Connection conn = DbUtil.getConnection()) {
            conn.setAutoCommit(false);
            IssuedBook record = issuedBookDao.findById(conn, issueId);
            if (record == null) {
                conn.rollback();
                return "Issue ID not found.";
            }
            if (record.getReturnDate() != null) {
                conn.rollback();
                return "This issue record is already returned.";
            }
            LocalDateTime now = DateTimeUtil.now();
            boolean updated = issuedBookDao.markReturn(conn, issueId, now);
            if (!updated) {
                conn.rollback();
                return "Failed to mark return.";
            }
            boolean incremented = bookDao.incrementQuantity(conn, record.getBookId());
            if (!incremented) {
                conn.rollback();
                return "Failed to update book quantity.";
            }
            conn.commit();
            return "Book returned at: " + now;
        } catch (SQLException e) {
            return "Failed to return book: " + e.getMessage();
        }
    }
}