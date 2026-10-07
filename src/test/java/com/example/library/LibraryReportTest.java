package com.example.library;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryReportTest {
    @Test
    void mostBorrowedBooksAreRankedByBorrowCountAndExcludeNeverBorrowedBooks() {
        Library library = new Library();
        Book first = new Book("ISBN-1", "First", "Author", 2020);
        Book second = new Book("ISBN-2", "Second", "Author", 2021);
        Book neverBorrowed = new Book("ISBN-3", "Never borrowed", "Author", 2022);
        library.addBook(first);
        library.addBook(second);
        library.addBook(neverBorrowed);

        borrowAndReturn(library, first, 2);
        borrowAndReturn(library, second, 3);

        assertEquals(List.of(second, first), library.getMostBorrowedBooks());
        assertEquals(3, second.getBorrowCount());
        assertEquals(2, first.getBorrowCount());
    }

    @Test
    void booksWithEqualBorrowCountsKeepTheirLibraryOrder() {
        Library library = new Library();
        Book first = new Book("ISBN-1", "First", "Author", 2020);
        Book second = new Book("ISBN-2", "Second", "Author", 2021);
        library.addBook(first);
        library.addBook(second);

        borrowAndReturn(library, first, 1);
        borrowAndReturn(library, second, 1);

        assertEquals(List.of(first, second), library.getMostBorrowedBooks());
    }

    @Test
    void reportIsEmptyWhenNoBookHasBeenBorrowed() {
        Library library = new Library();
        library.addBook(new Book("ISBN-1", "First", "Author", 2020));

        assertTrue(library.getMostBorrowedBooks().isEmpty());
    }

    private void borrowAndReturn(Library library, Book book, int times) {
        for (int count = 0; count < times; count++) {
            library.borrowBook(book.getIsbn());
            library.returnBook(book.getIsbn());
        }
    }
}
