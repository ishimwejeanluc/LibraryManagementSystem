package Dao;



import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.mysql.cj.xdevapi.Result;

import modal.Book;
import modal.Borrower;
import util.HibernateUtil;

public class BorrowerDao {
	// Save a new Borrower
    public String saveBorrower(Borrower borrower) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
            session.persist(borrower);
            transaction.commit();
            return "saved";
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return "notsaved";
        }
		
    }

    // Get a Borrower by ID
    public Borrower getBorrowerById(Borrower borrowerObj) {
        try (Session session = HibernateUtil.getSession().openSession()) {
            Borrower borrower=session.get(Borrower.class, borrowerObj.getId());
            return borrower ;
        } catch (Exception e) {
        	
            e.printStackTrace();
            return null;
        }
    }

    // Update an existing Borrower
    public void updateBorrower(Borrower borrower) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
            session.merge(borrower);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Delete a Borrower by ID
    public Borrower deleteBorrower(Borrower borrowerObj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
            Borrower borrower = session.get(Borrower.class, borrowerObj.getId());
            if (borrower != null) {
                session.remove(borrower);
                System.out.println("Borrower deleted successfully");
              return borrowerObj ;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return null ;
    }

    // Get all Borrowers
    public List<Borrower> getAllBorrowers() {
        try (Session session = HibernateUtil.getSession().openSession()) {
            return session.createQuery("from Borrower", Borrower.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Integer getBorrowedBooksCount(int userId) {
        Integer borrowedCount = 0;

        try (Session session = HibernateUtil.getSession().openSession()) {
            String hql = "SELECT COUNT(*) FROM Borrower b WHERE b.userId = :userId";
            Query query = session.createNativeQuery(hql, Integer.class);
            query.setParameter("userId", userId);

            int  countResult = (int) query.getSingleResult();
             borrowedCount = countResult ;

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }

        return borrowedCount;
    }
    public List<Borrower> getBorrowedBooksByUserId(int userId) {
        List<Borrower> borrowedBooks = new ArrayList<>();
        
        Transaction transaction = null;
        List<Borrower> borrower = null;

        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();

            // Native SQL query to fetch books where status is 'AVAILABLE'
            Query query = session.createNativeQuery(
                "SELECT * FROM BORROWER WHERE userId = :userId", Borrower.class);
            query.setParameter("userId", userId);

            borrower = query.getResultList();
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Log the exception for debugging
        }

        return borrower;
            
            
           
    }
    public int updateLateChargesFeeByBookTitle(String bookTitle, int newLateFee) {
        int result = 0;
        String sql = "UPDATE borrower br " +
                     "JOIN book b ON br.bookId = b.bookId " +
                     "SET br.lateChargeFees = :newLateFee " +
                     "WHERE b.title = :bookTitle";

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();

            // Execute the update query
            Query query = session.createNativeQuery(sql,Integer.class);
            query.setParameter("newLateFee", newLateFee);
            query.setParameter("bookTitle", bookTitle);

            // Execute the update and get the number of affected rows
            result = query.executeUpdate();

            // Commit the transaction
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            result = 0;
        }

        return result; // Returns 1 or more if successful, or 0 if failed
    }
}
