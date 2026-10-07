package com.example.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookTest {
    @Test
    void constructorInitializesBookFieldsAndBookIsNotBorrowed() {
        Book book = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018);

        assertEquals("978-0134685991", book.getIsbn());
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals(2018, book.getPublicationYear());
        assertFalse(book.isBorrowed());
    }

    @Test
    void borrowMarksBookAsBorrowed() {
        Book book = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018);

        book.borrow();

        assertTrue(book.isBorrowed());
    }

    @Test
    void returnBookMarksBookAsNotBorrowed() {
        Book book = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018);
        book.borrow();

        book.returnBook();

        assertFalse(book.isBorrowed());
    }

    @Test
    void borrowCountIncrementsOncePerBorrowingPeriod() {
        Book book = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018);

        book.borrow();
        book.borrow();
        assertEquals(1, book.getBorrowCount());

        book.returnBook();
        book.borrow();

        assertEquals(2, book.getBorrowCount());
    }

    @Test
    void toStringIncludesBookDetails() {
        Book book = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018);

        assertEquals(
                "Book{isbn='978-0134685991', title='Effective Java', author='Joshua Bloch', year=2018, borrowed=false}",
                book.toString());
    }
}
