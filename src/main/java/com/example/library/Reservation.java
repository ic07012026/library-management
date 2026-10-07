package com.example.library;

import java.util.Objects;

public class Reservation {
    private final String isbn;
    private final Member member;

    public Reservation(String isbn, Member member) {
        this.isbn = Objects.requireNonNull(isbn, "L'ISBN ne peut pas être null.");
        this.member = Objects.requireNonNull(member, "Le membre ne peut pas être null.");
    }

    public String getIsbn() {
        return isbn;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return String.format("Reservation{isbn='%s', member=%s}", isbn, member);
    }
}
