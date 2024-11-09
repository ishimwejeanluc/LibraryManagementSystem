package Dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import modal.Room;
import util.HibernateUtil;

public class RoomDao {
	 public String saveRoom(Room roomObj) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            transaction = session.beginTransaction();
	            session.persist(roomObj);  // `persist()` is used for saving new entities
	            transaction.commit();
	            return "saved";
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	            return "not saved";
	        }
	    }

	    // Get a Membership by ID
	    public Room getBookId(Room roomObj) {
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            Room room =session.get(Room.class, roomObj.getRoomId());
	            return room ;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    // Update an existing Membership
	    public String updateRoom(Room roomObj) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            transaction = session.beginTransaction();
	              // Use `merge()` for updating in Hibernate 6
	            transaction.commit();
	             session.merge(roomObj);
	            return "updated";
	         
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	            return "error";
	        }
	    }

	    // Delete a Membership by ID
	    public String deleteRoom(Room roomObj) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            transaction = session.beginTransaction();
	            Room room = session.get(Room.class, roomObj.getRoomId());
	            if (room != null) {
	                session.remove(room);  // Use `remove()` instead of `delete()`
	                System.out.println("Room deleted successfully");
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
	    public List<Room> getAllRoom() {
	    	  List<Room> rooms = new ArrayList<>();
		  	    try (Session session = HibernateUtil.getSession().openSession()) {
		  	    	String sql = "SELECT * FROM Room ";
		  	    	Query<Room> query = session.createNativeQuery(sql, Room.class);
		  	    	
		  	    	 rooms = query.getResultList();
		    }catch (Exception e) {
		        e.printStackTrace();
		    }
		    return rooms;



	    }
	    public int getTotalAvailableBooksInRoom(int roomId) {
	       
	        int totalAvailableBooks = 0;

	        try (Session session = HibernateUtil.getSession().openSession()) {
	            

	        	String sql = "SELECT SUM(s.availableStock) "
	                    + "FROM Shelf s "
	                    + "JOIN Room r ON s.roomId = r.roomId "
	                    + "WHERE r.roomId = :roomId";
	            Query<Integer> query = session.createNativeQuery(sql, Integer.class);
	            query.setParameter("roomId", roomId);

	            Integer result = query.getSingleResult();
	            if (result != null) {
	                totalAvailableBooks = result;
	            } else {
	                totalAvailableBooks = 0;
	            }

	           
	        } catch (Exception e) {
	            
	            e.printStackTrace();
	        }

	        return totalAvailableBooks;
	    }
}
