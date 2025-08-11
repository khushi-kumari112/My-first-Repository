package com.example.library.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a record of a book issued to a user.
 *
 * Fields:
 * - issueId: Unique identifier of issue transaction (auto-generated in DB)
 * - bookId: The issued book's id
 * - userName: The name of the user who borrowed the book
 * - issueDate: Date and time when the book was issued
 * - returnDate: Date and time when the book was returned (nullable)
 */
public class IssuedBook {

    private int issueId;
    private int bookId;
    private String userName;
    private LocalDateTime issueDate;
    private LocalDateTime returnDate; // may be null

    /** Default constructor. */
    public IssuedBook() { }

    /** Full constructor. */
    public IssuedBook(int issueId, int bookId, String userName, LocalDateTime issueDate, LocalDateTime returnDate) {
        this.issueId = issueId;
        this.bookId = bookId;
        this.userName = userName;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "IssuedBook{" +
                "issueId=" + issueId +
                ", bookId=" + bookId +
                ", userName='" + userName + '\'' +
                ", issueDate=" + issueDate +
                ", returnDate=" + returnDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssuedBook that = (IssuedBook) o;
        return issueId == that.issueId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueId);
    }
}