package com.example.library;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private boolean isBorrowed;
    private int borrowCount;

    public Book(String isbn, String title, String author, int publicationYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isBorrowed = false;
    }

    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    // Méthodes métier
    public void borrow() {
        if (!isBorrowed) {
            this.isBorrowed = true;
            borrowCount++;
        }
    }

    public void returnBook() {
        this.isBorrowed = false;
    }

    @Override
    public String toString() {
        return String.format("Book{isbn='%s', title='%s', author='%s', year=%d, borrowed=%b}",
                isbn, title, author, publicationYear, isBorrowed);
    }
}