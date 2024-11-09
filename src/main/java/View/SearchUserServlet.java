package View;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modal.Location;
import modal.User;
import Dao.UserDao;

import java.io.IOException;

@WebServlet(urlPatterns = "/searchUser")
public class SearchUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Helper method to check session validity
    private boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null && session.getAttribute("userId") != null);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if session is valid
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html"); // Redirect to login page if session is invalid
            return;
        }

        // Simply forward the request to the search user page to show the form or initial view
        RequestDispatcher dispatcher = request.getRequestDispatcher("borrowerManagement.jsp?section=searchUser");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if session is valid
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html"); // Redirect to login page if session is invalid
            return;
        }

        // Get the phone number from the request
        String phoneNumber = request.getParameter("phoneNumber");

        // Create an instance of UserDao to access the data
        UserDao dao = new UserDao();

        // Retrieve the user based on the phone number
        User user = dao.getLocation(phoneNumber);

        if (user == null) {
            // If no user found, return an error message
        	request.setAttribute("message", "No user found with this number");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/borrowerManagement.jsp");
            dispatcher.forward(request, response);
          
            return;
        }

        // Retrieve the location data (village, province)
        Location village = user.getVillage();
        Location province = village.getParent().getParent().getParent().getParent();

        // Display the user information and location
        
        request.setAttribute("message", "Name:" +user.getUsername() +", " + "Location:"+ province.getLocationName());
        // Forward to the appropriate JSP page for inclusion
        RequestDispatcher dispatcher = request.getRequestDispatcher("borrowerManagement.jsp?section=searchUser");
        dispatcher.include(request, response);
    }
}
