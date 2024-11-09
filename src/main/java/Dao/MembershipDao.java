package Dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import jakarta.persistence.Query;
import modal.BookStatus;
import modal.Membership;
import modal.Status;
import util.HibernateUtil;

public class MembershipDao {

	  // Save a new Membership
    public String saveMembership(Membership membershipObj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
            session.persist(membershipObj);  // `persist()` is used for saving new entities
            transaction.commit();
            return "saved";
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return "not saved";
        }
    }

    // Get a Membership by ID
    public Membership getMembershipById(Membership membershipObj) {
        try (Session session = HibernateUtil.getSession().openSession()) {
            Membership membership =session.get(Membership.class, membershipObj.getMembershipId());
            return membership ;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update an existing Membership
    public String updateMembership(Membership membershipObj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
              // Use `merge()` for updating in Hibernate 6
            transaction.commit();
             session.merge(membershipObj);
            return "updated";
         
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return "error";
        }
    }

    // Delete a Membership by ID
    public String deleteMembership(Membership membershipObj) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
            Membership membership = session.get(Membership.class, membershipObj.getMembershipId());
            if (membership != null) {
                session.remove(membership);  // Use `remove()` instead of `delete()`
                System.out.println("Membership deleted successfully");
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
    public List<Membership> getAllMemberships() {
        try (SessionFactory session2 = HibernateUtil.getSession();
				Session session = session2.openSession()) {
            return session.createQuery("from Membership", Membership.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            
        }
		return null;
    }
    public String getMembershipStatusByUserId(int userId) {
        String membershipStatus = null;
        
        try (Session session = HibernateUtil.getSession().openSession()) {
            String sql = "SELECT m.membershipStatus FROM Membership m WHERE m.userId = :userId"; // Assuming "status" is the column storing the membership status
            Query query = session.createNativeQuery(sql,String.class);
            query.setParameter("userId", userId);
            
            // Get the result as a String directly
            membershipStatus = (String) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception as needed
        }
        
        return membershipStatus; // Return the status as a String or null if not found
    }

    
    public Integer getMaxBooksAllowed(int userId) {
        Integer maxBooksAllowed = null;

        try (Session session = HibernateUtil.getSession().openSession()) {
            String hql = "SELECT mt.maxBooks " +
                    "FROM membership m " +
                    "JOIN membershiptype mt " +
                    "ON mt.membershiptypeid = m.membershiptypeid " +
                    "WHERE m.userid = :userId";
                         
            Query query = session.createNativeQuery(hql, Integer.class);
            query.setParameter("userId", userId);

            // Using list() to retrieve results
            int results = (int) query.getSingleResult();

            // If there are results, get the first one
            if (results != 0) {
            	maxBooksAllowed = results;  // Get the first result if it exists
            }

        } catch (Exception e) {
            e.printStackTrace();
            
        }

        return maxBooksAllowed;
    }
    public List<Membership> getPendingMemberships() {
        try (Session session = HibernateUtil.getSession().openSession()) {
            String sql = "SELECT * FROM Membership  WHERE membershipStatus = 'pending'";
            return session.createNativeQuery(sql, Membership.class).getResultList();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
		return null;
    }
    public boolean updateMembershipStatus(int membershipId, Status status) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession().openSession()) {
            transaction = session.beginTransaction();
            
            // Use HQL to update the status of the book with the given bookId
            String sql ="UPDATE Membership SET membershipStatus = :status WHERE membershipId = :membershipId";
            Query query = session.createNativeQuery(sql,Integer.class);
            query.setParameter("status", status.toString());
            query.setParameter("membershipId", membershipId);
            
            int result = query.executeUpdate();  // Execute the update query
            
            transaction.commit();
            return result > 0;  // Returns true if at least one record was updated
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
    public Boolean hasMembership(int userId) {
        Integer borrowedCount = 0;

        try (Session session = HibernateUtil.getSession().openSession()) {
            String hql = "SELECT COUNT(*) FROM membership  WHERE userId = :userId";
            Query query = session.createNativeQuery(hql, Integer.class);
            query.setParameter("userId", userId);

            int  countResult = (int) query.getSingleResult();
             borrowedCount = countResult ;

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }

        return borrowedCount > 0 ;
    }

}
