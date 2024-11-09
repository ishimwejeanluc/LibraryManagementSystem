package util;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import modal.Borrower;
import modal.Location;
import modal.Membership;
import modal.MembershipType;
import modal.Room;
import modal.Shelf;
import modal.User;
import modal.Book;



public class HibernateUtil {
private static SessionFactory sessionFactory = null;
	
	public static SessionFactory getSession() {
		
		if(sessionFactory == null) {
			Configuration conf = new Configuration();
			
			Properties settings = new Properties();
			
			// Set JDBC driver
			settings.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");

			// Set JDBC URL
			settings.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/auca_library_db");

			// Set database username and password
			settings.setProperty("hibernate.connection.username", "root");
			settings.setProperty("hibernate.connection.password", "Lukatoni123");
			settings.setProperty("hibernate.hbm2ddl.auto", "update");
			// Set additional Hibernate settings if necessary
			settings.setProperty("hibernate.show_sql", "true");
			
			settings.setProperty("hibernate.format_sql", "true");
			
			conf.setProperties(settings);
			
			conf.addAnnotatedClass(Room.class);
			conf.addAnnotatedClass(User.class);
			conf.addAnnotatedClass(Membership.class);
			conf.addAnnotatedClass(MembershipType.class);
			conf.addAnnotatedClass(Shelf.class);
			conf.addAnnotatedClass(Borrower.class);
			conf.addAnnotatedClass(Location.class);
			conf.addAnnotatedClass(Book.class);
			
 			sessionFactory = conf.buildSessionFactory();
			System.out.println("done");
			
			return sessionFactory;
			
		}else {
			return sessionFactory;
		}
	}
}
