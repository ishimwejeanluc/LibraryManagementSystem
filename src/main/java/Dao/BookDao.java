package Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import modal.Book;
import modal.BookStatus;
import modal.Room;
import util.HibernateUtil;

public class BookDao {

    // Save a new Book
    public String saveBook(Book bookObj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
            session.persist(bookObj);  // Use `persist()` for new entities
            transaction.commit();
            return "saved";
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return "not saved";
        }
    }

    // Get a Book by ID
    public Book getBookById(Long bookId) {
        try (Session session = HibernateUtil.getSession().openSession()) {
            return session.get(Book.class, bookId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update an existing Book
    public String updateBook(Book bookObj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
            session.merge(bookObj);  // Use `merge()` for updating in Hibernate 6
            transaction.commit();
            return "updated";
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return "error";
        }
    }

    // Delete a Book by ID
    public String deleteBook(Long bookId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
            Book book = session.get(Book.class, bookId);
            if (book != null) {
                session.remove(book);  // Use `remove()` instead of `delete()`
                System.out.println("Book deleted successfully");
                transaction.commit();
                return "deleted";
            } else {
                System.out.println("Book not found");
                transaction.rollback();
                return "not found";
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return "error";
        }
    }

    // Get all Books
    public List<Map<String, String>> getAllBooksWithRoomAndShelf() {
    	List<Map<String, String>> books = new ArrayList<>();
    	String hql = "SELECT b.bookId, b.title, b.status, b.publisher, b.ISBN, b.author, " +
    	             "s.bookCategory, r.roomCode " +
    	             "FROM Book b " +
    	             "JOIN b.shelf s " +
    	             "JOIN s.room r";

    	try (Session session = HibernateUtil.getSession().openSession()) {
    	    Query<Object[]> query = session.createQuery(hql, Object[].class);
    	    List<Object[]> results = query.getResultList();
    	    
    	    // Process results
    	    for (Object[] row : results) {
    	        Map<String, String> book = new HashMap<>();
    	        book.put("bookId", String.valueOf(row[0]));
    	        book.put("bookTitle", (String) row[1]);
    	        BookStatus status = (BookStatus) row[2]; // row[2] is the status
    	        book.put("bookStatus", status.name());
    	        book.put("publisherName", (String) row[3]);
    	        book.put("isbn", (String) row[4]);
    	        book.put("author", (String) row[5]);
    	        book.put("shelfCategory", (String) row[6]);
    	        book.put("roomName", (String) row[7]);
    	        books.add(book);
    	    }
    	}
		return books;

    }
    public List<Book> getAvailableBooks() {
        Transaction transaction = null;
        List<Book> availableBooks = null;

        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();

            // Native SQL query to fetch books where status is 'AVAILABLE'
            Query query = session.createNativeQuery(
                "SELECT * FROM Book WHERE status = :status", Book.class);
            query.setParameter("status", "AVAILABLE");

            availableBooks = query.getResultList();
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Log the exception for debugging
        }

        return availableBooks;
    }
    public boolean updateBookStatus(int bookId, BookStatus status) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
            
            // Use HQL to update the status of the book with the given bookId
            String sql ="UPDATE Book SET status = :status WHERE bookId = :bookId";
            Query query = session.createNativeQuery(sql,Integer.class);
            query.setParameter("status", status);
            query.setParameter("bookId", bookId);
            
            int result = query.executeUpdate();  // Execute the update query
            
            transaction.commit();
            return result > 0;  // Returns true if at least one record was updated
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
    

}
