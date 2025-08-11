package com.example.library.dao;

import com.example.library.entity.IssuedBook;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Data Access Object for issuing and returning books.
 */
public class IssuedBookDao {

    /**
     * Inserts a new issue record and returns the generated issue_id.
     * This method expects a connection with auto-commit managed by the caller (for transactions).
     */
    public int insertIssue(Connection conn, int bookId, String userName, LocalDateTime issueDate) throws SQLException {
        final String sql = "INSERT INTO issued_books (book_id, user_name, issue_date, return_date) VALUES (?, ?, ?, NULL)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bookId);
            ps.setString(2, userName);
            ps.setTimestamp(3, Timestamp.valueOf(issueDate));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Failed to retrieve generated issue_id");
            }
        }
    }

    /**
     * Marks a return for a given issue_id. Returns true if updated.
     * Only updates if return_date is currently NULL (still issued).
     */
    public boolean markReturn(Connection conn, int issueId, LocalDateTime returnDate) throws SQLException {
        final String sql = "UPDATE issued_books SET return_date = ? WHERE issue_id = ? AND return_date IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(returnDate));
            ps.setInt(2, issueId);
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }

    /** Retrieves an issue record by id, or null if not found. */
    public IssuedBook findById(Connection conn, int issueId) throws SQLException {
        final String sql = "SELECT issue_id, book_id, user_name, issue_date, return_date FROM issued_books WHERE issue_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, issueId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    IssuedBook ib = new IssuedBook();
                    ib.setIssueId(rs.getInt("issue_id"));
                    ib.setBookId(rs.getInt("book_id"));
                    ib.setUserName(rs.getString("user_name"));
                    Timestamp issueTs = rs.getTimestamp("issue_date");
                    Timestamp returnTs = rs.getTimestamp("return_date");
                    ib.setIssueDate(issueTs != null ? issueTs.toLocalDateTime() : null);
                    ib.setReturnDate(returnTs != null ? returnTs.toLocalDateTime() : null);
                    return ib;
                }
                return null;
            }
        }
    }
}