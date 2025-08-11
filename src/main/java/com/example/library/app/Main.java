package com.example.library.app;

import com.example.library.entity.Book;
import com.example.library.service.LibraryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for the Library Management System.
 *
 * Menu:
 * 1. Add Book
 * 2. View All Books
 * 3. Issue Book (uses a single method to get current date-time)
 * 4. Return Book (uses the same method to get current date-time)
 * 5. Delete Book
 * 6. Exit
 */
public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        LibraryService service = new LibraryService();
        boolean running = true;
        System.out.println("Welcome to Library Management System");
        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String choiceInput = SCANNER.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(choiceInput);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number between 1 and 6.\n");
                continue;
            }

            switch (choice) {
                case 1 -> addBookFlow(service);
                case 2 -> viewBooksFlow(service);
                case 3 -> issueBookFlow(service);
                case 4 -> returnBookFlow(service);
                case 5 -> deleteBookFlow(service);
                case 6 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("[Main Menu]");
        System.out.println("1. Add Book");
        System.out.println("2. View All Books");
        System.out.println("3. Issue Book (create a method which returns the date and time)");
        System.out.println("4. Return Book (use the same method)");
        System.out.println("5. Delete Book");
        System.out.println("6. Exit");
    }

    private static void addBookFlow(LibraryService service) {
        try {
            System.out.print("Enter Book ID (int): ");
            int id = Integer.parseInt(SCANNER.nextLine().trim());
            System.out.print("Enter Title: ");
            String title = SCANNER.nextLine().trim();
            System.out.print("Enter Author: ");
            String author = SCANNER.nextLine().trim();
            System.out.print("Enter Quantity (int): ");
            int quantity = Integer.parseInt(SCANNER.nextLine().trim());

            Book b = new Book(id, title, author, quantity);
            String result = service.addBook(b);
            System.out.println(result + "\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input. Please try again.\n");
        }
    }

    private static void viewBooksFlow(LibraryService service) {
        try {
            List<Book> books = service.viewAllBooks();
            if (books.isEmpty()) {
                System.out.println("No books found.\n");
                return;
            }
            System.out.println("Books:");
            System.out.println("book_id | title | author | quantity");
            for (Book b : books) {
                System.out.printf("%d | %s | %s | %d%n", b.getBookId(), b.getTitle(), b.getAuthor(), b.getQuantity());
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Failed to fetch books: " + e.getMessage() + "\n");
        }
    }

    private static void issueBookFlow(LibraryService service) {
        try {
            System.out.print("Enter Book ID to issue: ");
            int bookId = Integer.parseInt(SCANNER.nextLine().trim());
            System.out.print("Enter User Name: ");
            String userName = SCANNER.nextLine().trim();
            String result = service.issueBook(bookId, userName);
            System.out.println(result + "\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid Book ID.\n");
        }
    }

    private static void returnBookFlow(LibraryService service) {
        try {
            System.out.print("Enter Issue ID to return: ");
            int issueId = Integer.parseInt(SCANNER.nextLine().trim());
            String result = service.returnBook(issueId);
            System.out.println(result + "\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid Issue ID.\n");
        }
    }

    private static void deleteBookFlow(LibraryService service) {
        try {
            System.out.print("Enter Book ID to delete: ");
            int id = Integer.parseInt(SCANNER.nextLine().trim());
            String result = service.deleteBook(id);
            System.out.println(result + "\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid Book ID.\n");
        }
    }
}