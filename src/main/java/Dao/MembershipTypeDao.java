package Dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.Query;
import modal.MembershipType;
import util.HibernateUtil;

public class MembershipTypeDao {
	 public String saveMembershipType (MembershipType membershipTypeObj) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            transaction = session.beginTransaction();
	            session.persist(membershipTypeObj);  // `persist()` is used for saving new entities
	            transaction.commit();
	            return "saved";
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	            return "not saved";
	        }
	    }

	    // Get a Membership by ID
	    public MembershipType getMembershipById(MembershipType membershipTypeObj) {
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            MembershipType membershiptype =session.get(MembershipType.class, membershipTypeObj.getMembershipTypeId());
	            return membershiptype ;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    // Update an existing Membership
	    public String updateMembership(MembershipType membershipTypeObj) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            transaction = session.beginTransaction();
	              // Use `merge()` for updating in Hibernate 6
	            transaction.commit();
	             session.merge(membershipTypeObj);
	            return "updated";
	         
	        } catch (Exception e) {
	            if (transaction != null) transaction.rollback();
	            e.printStackTrace();
	            return "error";
	        }
	    }

	    // Delete a Membership by ID
	    public String deleteMembership(MembershipType membershipObj) {
	        Transaction transaction = null;
	        try (Session session = HibernateUtil.getSession().openSession()) {
	            transaction = session.beginTransaction();
	            MembershipType membershiptype = session.get(MembershipType.class, membershipObj.getMembershipTypeId());
	            if (membershiptype != null) {
	                session.remove(membershiptype);  // Use `remove()` instead of `delete()`
	                System.out.println("MembershipType deleted successfully");
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
	    public List<MembershipType> getAllMemberships() {
	    	List<MembershipType> types = new ArrayList<>();
	  	    try (Session session = HibernateUtil.getSession().openSession()) {
	  	    	String sql = "SELECT * FROM MembershipType ";
	  	    	Query query = session.createNativeQuery(sql, MembershipType.class);
	  	    	
	  	    	 types = query.getResultList();
	    }catch (Exception e) {
	        e.printStackTrace();
	    }
	    return types;
	    }

}
