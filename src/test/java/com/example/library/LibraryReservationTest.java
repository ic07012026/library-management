package com.example.library;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryReservationTest {
    private static final String ISBN = "978-0134685991";

    @Test
    void reservationsAreQueuedInFifoOrder() {
        Library library = createLibrary();
        Member first = new Member("M001", "Alex Martin", "alex@example.com");
        Member second = new Member("M002", "Sam Lee", "sam@example.com");
        library.borrowBook(ISBN);

        library.reserveBook(ISBN, first);
        library.reserveBook(ISBN, second);

        assertEquals(List.of(first, second), library.getReservations(ISBN));
    }

    @Test
    void onlyNextMemberCanBorrowReservedBook() {
        Library library = createLibrary();
        Book book = library.findByIsbn(ISBN);
        Member first = new Member("M001", "Alex Martin", "alex@example.com");
        Member second = new Member("M002", "Sam Lee", "sam@example.com");
        library.borrowBook(ISBN);
        library.reserveBook(ISBN, first);
        library.reserveBook(ISBN, second);
        library.returnBook(ISBN);

        assertEquals(0, library.getAvailableBookCount());
        assertThrows(IllegalStateException.class, () -> library.borrowBook(ISBN));
        assertThrows(IllegalStateException.class, () -> library.borrowBook(ISBN, second));

        library.borrowBook(ISBN, first);
        assertTrue(book.isBorrowed());
        assertEquals(List.of(second), library.getReservations(ISBN));

        library.returnBook(ISBN);
        library.borrowBook(ISBN, second);
        assertTrue(book.isBorrowed());
        assertTrue(library.getReservations(ISBN).isEmpty());
    }

    @Test
    void duplicateReservationBySameMemberIsRejected() {
        Library library = createLibrary();
        Member member = new Member("M001", "Alex Martin", "alex@example.com");
        library.reserveBook(ISBN, member);

        assertThrows(IllegalStateException.class, () -> library.reserveBook(
                ISBN, new Member("M001", "Alex Martin", "alex@example.com")));
    }

    @Test
    void cancelReservationRemovesMemberAndMakesBookAvailable() {
        Library library = createLibrary();
        Member member = new Member("M001", "Alex Martin", "alex@example.com");
        library.reserveBook(ISBN, member);

        library.cancelReservation(ISBN, member);

        assertTrue(library.getReservations(ISBN).isEmpty());
        assertEquals(1, library.getAvailableBookCount());
        assertFalse(library.findByIsbn(ISBN).isBorrowed());
    }

    @Test
    void cancelReservationThrowsWhenMemberHasNoReservation() {
        Library library = createLibrary();
        Member member = new Member("M001", "Alex Martin", "alex@example.com");

        assertThrows(IllegalStateException.class, () -> library.cancelReservation(ISBN, member));
    }

    @Test
    void reservationThrowsWhenBookDoesNotExist() {
        Library library = new Library();
        Member member = new Member("M001", "Alex Martin", "alex@example.com");

        assertThrows(NoSuchElementException.class,
                () -> library.reserveBook("unknown-isbn", member));
    }

    private Library createLibrary() {
        Library library = new Library();
        library.addBook(new Book(ISBN, "Effective Java", "Joshua Bloch", 2018));
        return library;
    }
}
