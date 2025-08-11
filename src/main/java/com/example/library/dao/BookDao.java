package com.example.library.dao;

import com.example.library.entity.Book;
import com.example.library.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for {@link Book} using JDBC.
 */
public class BookDao {

    /** Inserts a new book. Returns true if inserted, false if duplicate key. */
    public boolean insert(Book book) throws SQLException {
        final String sql = "INSERT INTO books (book_id, title, author, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, book.getBookId());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setInt(4, book.getQuantity());
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        }
    }

    /** Returns all books ordered by book_id. */
    public List<Book> findAll() throws SQLException {
        final String sql = "SELECT book_id, title, author, quantity FROM books ORDER BY book_id";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Book b = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity")
                );
                books.add(b);
            }
        }
        return books;
    }

    /** Deletes a book by id. Returns true if a row was deleted. */
    public boolean deleteById(int bookId) throws SQLException {
        final String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }

    /** Returns current quantity for a book, or -1 if not found. */
    public int getQuantity(int bookId) throws SQLException {
        final String sql = "SELECT quantity FROM books WHERE book_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
                return -1;
            }
        }
    }

    /** Decrements quantity by 1 if quantity > 0. Returns true if updated. */
    public boolean decrementQuantity(Connection conn, int bookId) throws SQLException {
        final String sql = "UPDATE books SET quantity = quantity - 1 WHERE book_id = ? AND quantity > 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }

    /** Increments quantity by 1. Returns true if updated. */
    public boolean incrementQuantity(Connection conn, int bookId) throws SQLException {
        final String sql = "UPDATE books SET quantity = quantity + 1 WHERE book_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }
}