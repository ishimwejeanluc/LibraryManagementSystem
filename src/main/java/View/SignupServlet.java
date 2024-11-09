package View;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Dao.UserDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modal.Gender;
import modal.Location;
import modal.Role;
import modal.User;
@WebServlet(value = "/Signup")
public class SignupServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String firstName = request.getParameter("first_name");
	    String lastName = request.getParameter("last_name");
	    String gender = request.getParameter("gender");
	    String role = request.getParameter("role");
	    String phoneNumber = request.getParameter("phone_number");
	    String villageId = request.getParameter("village");
	    String password = hashPassword(request.getParameter("password"));
	    String username = request.getParameter("username");
	    // This will contain the selected village ID
	     Location lo = new Location();
	     lo.setLocationId(Integer.parseInt(villageId));
	     Gender gender1 = Gender.valueOf(gender);
	     Role role1 = Role.valueOf(role);
	     response.setContentType("text/html");
	       User user = new User();
	        user.setFirstName(firstName);
	        user.setLastName(lastName);
	        user.setGender(gender1);
	        user.setRole(role1);
	        user.setPhoneNumber(phoneNumber);
	        user.setVillage(lo);
	        user.setPassword(password);
	        user.setUsername(username);
	        UserDao userDao = new UserDao();
	        String isSaved = userDao.createAccount(user);
	        PrintWriter out = response.getWriter();

	        if( isSaved.equalsIgnoreCase("saved")) {
	        	 out.println("<h1> Account Created Successfully </h1>");
	        	RequestDispatcher dispatcher = request.getRequestDispatcher("/login.html");
				dispatcher.forward(request, response);
	    } else {
	    	
	    	
	        out.println("<h1> Account not created  </h1>"); // Fixed typo "crendentials"
	        RequestDispatcher dispatcher = request.getRequestDispatcher("/signup.jsp");
	        dispatcher.include(request, response);
     	}


 }
	public static String hashPassword(String password) {
        try {
            // Get an instance of SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Hash the password bytes
            byte[] hashedBytes = md.digest(password.getBytes());
            
            // Convert the hashed bytes to a hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));  // Converts each byte to a hex format
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
