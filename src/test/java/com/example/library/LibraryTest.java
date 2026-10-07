package com.example.library;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryTest {
    private static final String ISBN_ONE = "978-0134685991";
    private static final String ISBN_TWO = "978-0596009205";

    @Test
    void addBookStoresBookInLibrary() {
        Library library = new Library();
        Book book = new Book(ISBN_ONE, "Effective Java", "Joshua Bloch", 2018);

        library.addBook(book);

        assertSame(book, library.findByIsbn(ISBN_ONE));
    }

    @Test
    void addBookRejectsNull() {
        Library library = new Library();

        assertThrows(NullPointerException.class, () -> library.addBook(null));
    }

    @Test
    void findByIsbnReturnsMatchingBook() {
        Library library = new Library();
        Book book = new Book(ISBN_ONE, "Effective Java", "Joshua Bloch", 2018);
        library.addBook(book);

        assertSame(book, library.findByIsbn(ISBN_ONE));
    }

    @Test
    void findByIsbnThrowsWhenBookDoesNotExist() {
        Library library = new Library();

        assertThrows(NoSuchElementException.class, () -> library.findByIsbn(ISBN_ONE));
    }

    @Test
    void findByAuthorReturnsAllBooksByAuthor() {
        Library library = new Library();
        Book first = new Book(ISBN_ONE, "Effective Java", "Joshua Bloch", 2018);
        Book second = new Book(ISBN_TWO, "Java Concurrency in Practice", "Joshua Bloch", 2006);
        library.addBook(first);
        library.addBook(second);
        library.addBook(new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008));

        List<Book> results = library.findByAuthor("Joshua Bloch");

        assertEquals(2, results.size());
        assertSame(first, results.get(0));
        assertSame(second, results.get(1));
    }

    @Test
    void findByAuthorReturnsEmptyListWhenNoBooksMatch() {
        Library library = new Library();

        assertTrue(library.findByAuthor("Unknown Author").isEmpty());
    }

    @Test
    void borrowBookMarksBookAsBorrowed() {
        Library library = new Library();
        Book book = new Book(ISBN_ONE, "Effective Java", "Joshua Bloch", 2018);
        library.addBook(book);

        library.borrowBook(ISBN_ONE);

        assertTrue(book.isBorrowed());
    }

    @Test
    void borrowBookThrowsWhenBookDoesNotExist() {
        Library library = new Library();

        assertThrows(NoSuchElementException.class, () -> library.borrowBook(ISBN_ONE));
    }

    @Test
    void borrowBookThrowsWhenBookIsAlreadyBorrowed() {
        Library library = new Library();
        library.addBook(new Book(ISBN_ONE, "Effective Java", "Joshua Bloch", 2018));
        library.borrowBook(ISBN_ONE);

        assertThrows(IllegalStateException.class, () -> library.borrowBook(ISBN_ONE));
    }

    @Test
    void returnBookMarksBookAsAvailable() {
        Library library = new Library();
        Book book = new Book(ISBN_ONE, "Effective Java", "Joshua Bloch", 2018);
        library.addBook(book);
        library.borrowBook(ISBN_ONE);

        library.returnBook(ISBN_ONE);

        assertFalse(book.isBorrowed());
    }

    @Test
    void returnBookThrowsWhenBookDoesNotExist() {
        Library library = new Library();

        assertThrows(NoSuchElementException.class, () -> library.returnBook(ISBN_ONE));
    }

    @Test
    void returnBookThrowsWhenBookIsNotBorrowed() {
        Library library = new Library();
        library.addBook(new Book(ISBN_ONE, "Effective Java", "Joshua Bloch", 2018));

        assertThrows(IllegalStateException.class, () -> library.returnBook(ISBN_ONE));
    }

    @Test
    void availableBookCountChangesWhenBooksAreBorrowedAndReturned() {
        Library library = new Library();
        library.addBook(new Book(ISBN_ONE, "Effective Java", "Joshua Bloch", 2018));
        library.addBook(new Book(ISBN_TWO, "Java Concurrency in Practice", "Brian Goetz", 2006));

        assertEquals(2, library.getAvailableBookCount());

        library.borrowBook(ISBN_ONE);
        assertEquals(1, library.getAvailableBookCount());

        library.returnBook(ISBN_ONE);
        assertEquals(2, library.getAvailableBookCount());
    }

    @Test
    void availableBookCountIsZeroForEmptyLibrary() {
        assertEquals(0, new Library().getAvailableBookCount());
    }
}
