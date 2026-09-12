package org.example;

import jakarta.persistence.*;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Publisher;
import org.example.util.JpaUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bookstore");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // Authors
        Author author1 = new Author("John");;
        saveAuthor(author1);

        Author author2 = new Author("Jane");
        saveAuthor(author2);

        // Books
        Book book1 = new Book("Book1", author1);
        saveBook(book1);

        Book book2 = new Book("Book2", author2);
        saveBook(book2);

        /* dodavanje pod kolekciju */
        Set<Book> bookSet = new HashSet<>();
        bookSet.add(book1);
        bookSet.add(book2);
        // em.persist(bookSet);

        // Publisher
        Publisher publisher1 = new Publisher("Publisher1", bookSet);
        savePublisher(publisher1);

        /* ispis svih knjiga (provjera) */
        for (Book book : bookSet) {
            System.out.println(book.getTitle());
        }

        // Metode
        getAllAuthorsAndBooks();

        updateBookTitle(book2, "Book5");

        deleteBook(book2);

        tx.commit();
        em.close();
        emf.close();
    }

    public static void saveAuthor(Author author){
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(author);
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()){
                System.out.println(e.getMessage());
                em.getTransaction().rollback();
            }
        }
    }

    public static void saveBook(Book book){
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()){
                System.out.println(ex.getMessage());
                em.getTransaction().rollback();
            }
        }
    }

    public static void savePublisher(Publisher publisher){
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(publisher);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()){
                System.out.println(ex.getMessage());
                em.getTransaction().rollback();
            }
        }
    }

    public static void getAllAuthorsAndBooks(){
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            List<Author> authors = em.createQuery("from Author", Author.class).getResultList();
            for (Author author : authors) {
                System.out.println(author);
                for (Book book : author.getBooks()) {
                    System.out.println(book);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()){
                System.out.println(ex.getMessage());
                em.getTransaction().rollback();
            }
        }
    }

    public static void updateBookTitle(Book book, String title){
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            Book book2 = em.find(Book.class, book.getId());
            book2.setTitle(title);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()){
                System.out.println(ex.getMessage());
                em.getTransaction().rollback();
            }
        }
    }

    public static void deleteBook(Book book){
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Book book2 = em.find(Book.class, book.getId());
            em.remove(book2);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()){
                System.out.println(ex.getMessage());
                em.getTransaction().rollback();
            }
        }
    }
}
