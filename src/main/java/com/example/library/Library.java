package com.example.library;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Comparator;

public class Library {
    private static final int MAX_BORROWED_BOOKS_PER_MEMBER = 3;

    private final ArrayList<Book> books = new ArrayList<>();
    private final Map<String, ArrayDeque<Reservation>> reservationsByIsbn = new HashMap<>();
    private final Map<String, String> borrowerByIsbn = new HashMap<>();

    public void addBook(Book book) {
        Objects.requireNonNull(book, "Le livre ne peut pas être null.");
        books.add(book);
    }

    public Book findByIsbn(String isbn) {
        return books.stream()
                .filter(book -> Objects.equals(book.getIsbn(), isbn))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Aucun livre trouvé pour l'ISBN : " + isbn));
    }

    public List<Book> findByAuthor(String author) {
        return books.stream()
                .filter(book -> Objects.equals(book.getAuthor(), author))
                .toList();
    }

    public int getAvailableBookCount() {
        return (int) books.stream()
                .filter(book -> !book.isBorrowed() && !hasReservations(book.getIsbn()))
                .count();
    }

    public List<Book> getMostBorrowedBooks() {
        return books.stream()
                .filter(book -> book.getBorrowCount() > 0)
                .sorted(Comparator.comparingInt(Book::getBorrowCount).reversed())
                .toList();
    }

    public int getBorrowedBookCount(Member member) {
        Objects.requireNonNull(member, "Le membre ne peut pas être null.");
        return (int) borrowerByIsbn.values().stream()
                .filter(memberId -> Objects.equals(memberId, member.getMemberId()))
                .count();
    }

    public void reserveBook(String isbn, Member member) {
        findByIsbn(isbn);
        Objects.requireNonNull(member, "Le membre ne peut pas être null.");

        ArrayDeque<Reservation> reservations = reservationsByIsbn.computeIfAbsent(
                isbn, ignored -> new ArrayDeque<>());
        boolean alreadyReserved = reservations.stream().anyMatch(reservation ->
            Objects.equals(reservation.getMember().getMemberId(), member.getMemberId()));
        if (alreadyReserved) {
            throw new IllegalStateException("Le membre a déjà réservé ce livre : " + isbn);
        }
        reservations.addLast(new Reservation(isbn, member));
    }

    public List<Member> getReservations(String isbn) {
        findByIsbn(isbn);
        ArrayDeque<Reservation> reservations = reservationsByIsbn.get(isbn);
        return reservations == null
            ? List.of()
            : reservations.stream().map(Reservation::getMember).toList();
    }

    public void cancelReservation(String isbn, Member member) {
        findByIsbn(isbn);
        Objects.requireNonNull(member, "Le membre ne peut pas être null.");

        ArrayDeque<Reservation> reservations = reservationsByIsbn.get(isbn);
        boolean removed = reservations != null && reservations.removeIf(reservation ->
            Objects.equals(reservation.getMember().getMemberId(), member.getMemberId()));
        if (!removed) {
            throw new IllegalStateException("Aucune réservation trouvée pour ce membre : " + isbn);
        }
        if (reservations.isEmpty()) {
            reservationsByIsbn.remove(isbn);
        }
    }

    public void borrowBook(String isbn) {
        Book book = findByIsbn(isbn);
        if (book.isBorrowed()) {
            throw new IllegalStateException("Le livre est déjà emprunté : " + isbn);
        }
        if (hasReservations(isbn)) {
            throw new IllegalStateException("Le livre est réservé par un membre : " + isbn);
        }
        book.borrow();
    }

    public void borrowBook(String isbn, Member member) {
        Book book = findByIsbn(isbn);
        Objects.requireNonNull(member, "Le membre ne peut pas être null.");
        if (book.isBorrowed()) {
            throw new IllegalStateException("Le livre est déjà emprunté : " + isbn);
        }
        if (getBorrowedBookCount(member) >= MAX_BORROWED_BOOKS_PER_MEMBER) {
            throw new IllegalStateException("Le membre a atteint la limite de trois livres empruntés.");
        }

        ArrayDeque<Reservation> reservations = reservationsByIsbn.get(isbn);
        if (reservations != null && !reservations.isEmpty()) {
            Member nextMember = reservations.peekFirst().getMember();
            if (!Objects.equals(nextMember.getMemberId(), member.getMemberId())) {
                throw new IllegalStateException("Le livre est réservé par un autre membre : " + isbn);
            }
            reservations.removeFirst();
            if (reservations.isEmpty()) {
                reservationsByIsbn.remove(isbn);
            }
        }
        book.borrow();
        borrowerByIsbn.put(isbn, member.getMemberId());
    }

    public void returnBook(String isbn) {
        Book book = findByIsbn(isbn);
        if (!book.isBorrowed()) {
            throw new IllegalStateException("Le livre n'est pas emprunté : " + isbn);
        }
        book.returnBook();
        borrowerByIsbn.remove(isbn);
    }

    private boolean hasReservations(String isbn) {
        ArrayDeque<Reservation> reservations = reservationsByIsbn.get(isbn);
        return reservations != null && !reservations.isEmpty();
    }
}
