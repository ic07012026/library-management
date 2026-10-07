package com.example.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryLoanLimitTest {
    private static final Member MEMBER = new Member("M001", "Alex Martin", "alex@example.com");

    @Test
    void memberCannotBorrowMoreThanThreeBooks() {
        Library library = libraryWithFourBooks();

        library.borrowBook("ISBN-1", MEMBER);
        library.borrowBook("ISBN-2", MEMBER);
        library.borrowBook("ISBN-3", MEMBER);

        assertEquals(3, library.getBorrowedBookCount(MEMBER));
        assertThrows(IllegalStateException.class, () -> library.borrowBook("ISBN-4", MEMBER));
        assertFalse(library.findByIsbn("ISBN-4").isBorrowed());
        assertEquals(3, library.getBorrowedBookCount(MEMBER));
    }

    @Test
    void returningBookFreesOnePlaceInMemberLimit() {
        Library library = libraryWithFourBooks();
        library.borrowBook("ISBN-1", MEMBER);
        library.borrowBook("ISBN-2", MEMBER);
        library.borrowBook("ISBN-3", MEMBER);

        library.returnBook("ISBN-1");
        library.borrowBook("ISBN-4", MEMBER);

        assertEquals(3, library.getBorrowedBookCount(MEMBER));
        assertTrue(library.findByIsbn("ISBN-4").isBorrowed());
    }

    @Test
    void borrowingLimitIsTrackedSeparatelyForEachMember() {
        Library library = libraryWithFourBooks();
        Member anotherMember = new Member("M002", "Sam Lee", "sam@example.com");
        library.borrowBook("ISBN-1", MEMBER);
        library.borrowBook("ISBN-2", MEMBER);
        library.borrowBook("ISBN-3", MEMBER);

        library.borrowBook("ISBN-4", anotherMember);

        assertEquals(3, library.getBorrowedBookCount(MEMBER));
        assertEquals(1, library.getBorrowedBookCount(anotherMember));
    }

    @Test
    void rejectedBorrowDoesNotRemoveMembersReservation() {
        Library library = libraryWithFourBooks();
        library.borrowBook("ISBN-1", MEMBER);
        library.borrowBook("ISBN-2", MEMBER);
        library.borrowBook("ISBN-3", MEMBER);
        library.reserveBook("ISBN-4", MEMBER);

        assertThrows(IllegalStateException.class, () -> library.borrowBook("ISBN-4", MEMBER));
        assertEquals(java.util.List.of(MEMBER), library.getReservations("ISBN-4"));

        library.returnBook("ISBN-1");
        library.borrowBook("ISBN-4", MEMBER);
        assertTrue(library.getReservations("ISBN-4").isEmpty());
    }

    private Library libraryWithFourBooks() {
        Library library = new Library();
        for (int index = 1; index <= 4; index++) {
            library.addBook(new Book("ISBN-" + index, "Book " + index, "Author", 2026));
        }
        return library;
    }
}
