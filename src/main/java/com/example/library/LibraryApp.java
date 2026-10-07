package com.example.library;

import java.util.NoSuchElementException;

public class LibraryApp {
	public static void main(String[] args) {
		Library library = new Library();

		Book effectiveJava = new Book(
				"978-0134685991", "Effective Java", "Joshua Bloch", 2018);
		Book headFirstJava = new Book(
				"978-0596009205", "Head First Java", "Kathy Sierra", 2005);

		library.addBook(effectiveJava);
		library.addBook(headFirstJava);

		System.out.println("Livres de Joshua Bloch :");
		library.findByAuthor("Joshua Bloch").forEach(System.out::println);
		System.out.println("Nombre de livres disponibles : "
				+ library.getAvailableBookCount());

		try {
			Book foundBook = library.findByIsbn("978-0134685991");
			System.out.println("Livre trouvé : " + foundBook);

			library.borrowBook(foundBook.getIsbn());
			System.out.println("Livre emprunté : " + foundBook.getTitle());
			System.out.println("Livres disponibles après emprunt : "
					+ library.getAvailableBookCount());

			try {
				library.borrowBook(foundBook.getIsbn());
			} catch (IllegalStateException exception) {
				System.out.println("Emprunt refusé : " + exception.getMessage());
			}

			library.returnBook(foundBook.getIsbn());
			System.out.println("Livre retourné : " + foundBook.getTitle());
			System.out.println("Livres disponibles après retour : "
					+ library.getAvailableBookCount());
		} catch (NoSuchElementException | IllegalStateException exception) {
			System.out.println("Opération impossible : " + exception.getMessage());
		}

		try {
			library.findByIsbn("ISBN-inconnu");
		} catch (NoSuchElementException exception) {
			System.out.println("Recherche impossible : " + exception.getMessage());
		}
	}
}