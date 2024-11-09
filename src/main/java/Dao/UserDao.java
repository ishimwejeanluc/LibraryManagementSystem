package Dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import modal.User;
import util.HibernateUtil;

public class UserDao {
public String createAccount(User account) {
		
		try(Session session = HibernateUtil.getSession().openSession()){
			
			Transaction trans = session.beginTransaction();
			
			//this is like save
			session.persist(account);
			
			trans.commit();
			
			session.close();
			
			return "saved";
			
		}catch(Exception ex) {
			ex.printStackTrace();
			return "error";
		}
}


public User login(User userObj) {
    String username = userObj.getUsername();
    String password = userObj.getPassword();
    User user = null; // Declare user variable

    try (Session session = HibernateUtil.getSession().openSession()) { // Ensure session is closed automatically
        Transaction transaction = session.beginTransaction();

        // Query to check username and password
        List<User> users = session.createQuery(
                "FROM User u WHERE u.username = :username AND u.password = :password", 
                User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList(); // Get the list of results

        if (!users.isEmpty()) {
            user = users.get(0); // Get the first user if found
        } else {
            System.out.println("No user found with the provided credentials.");
            return null; // Return null if no user found
        }

        transaction.commit(); // Commit if user is found
    } catch (Exception e) {
        e.printStackTrace(); // Log the exception
        // If you had opened the transaction, you would roll back here, but we are using try-with-resources so no need.
    }
    return user; // Will be null if no user found or an error occurred
 }
public User getLocation(String number) {
    
    User user = null; // Declare user variable

    try (Session session = HibernateUtil.getSession().openSession()) { // Ensure session is closed automatically
        Transaction transaction = session.beginTransaction();

        // Query to check username and password
        List<User> users = session.createQuery(
                "FROM User u WHERE u.phoneNumber = :number ", 
                User.class)
                .setParameter("number", number)
                .getResultList(); // Get the list of results

        if (!users.isEmpty()) {
            user = users.get(0); 
        } else {
            System.out.println("No user found with the provided credentials.");
            return null; 
        }

        transaction.commit(); 
    } catch (Exception e) {
        e.printStackTrace(); 
    }
    return user; // Will be null if no user found or an error occurred
 }

}



