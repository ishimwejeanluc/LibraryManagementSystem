package Dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.Query;
import modal.Shelf;
import util.HibernateUtil;

public class ShelfDao {
	 public String saveShelf(Shelf shelfObj) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            transaction = session.beginTransaction();
	            session.persist(shelfObj);  // `persist()` is used for saving new entities
	            transaction.commit();
	            return "saved";
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	            return "not saved";
	        }
	    }

	    // Get a Membership by ID
	    public Shelf getShelfId(Shelf shelfObj) {
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            Shelf shelf =session.get(Shelf.class, shelfObj.getShelfId());
	            return shelf ;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    // Update an existing Membership
	    public String updateShelf(Shelf shelfObj) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            transaction = session.beginTransaction();
	              
	            transaction.commit();
	             session.merge(shelfObj);
	            return "updated";
	         
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	            return "error";
	        }
	    }

	    // Delete a Membership by ID
	    public String deleteShelf(Shelf shelfObj) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            transaction = session.beginTransaction();
	            Shelf shelf = session.get(Shelf.class, shelfObj.getShelfId());
	            if (shelf != null) {
	                session.remove(shelf);  // Use `remove()` instead of `delete()`
	                System.out.println("Book deleted successfully");
	              return "deleted";
	            }
	            transaction.commit();
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	            System.out.println(e);
	            return "error";
	        }
			return null;
	    }

	    // Get all Memberships
	    public List<Shelf> getAllShelfs() {
	    	  List<Shelf> shelfs = new ArrayList<>();
	  	    try (Session session = HibernateUtil.getSession().openSession()) {
	  	    	String sql = "SELECT * FROM shelf ";
	  	    	Query query = session.createNativeQuery(sql, Shelf.class);
	  	    	
	  	    	 shelfs = query.getResultList();
	    }catch (Exception e) {
	        e.printStackTrace();
	    }
	    return shelfs;


}
}