package View;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(value = "/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Check if there is an existing session
        HttpSession session = req.getSession(false); // false = don't create new session if it doesn't exist

        if (session != null && session.getAttribute("role") != null) {
            // Session is active, get the role and redirect to the appropriate dashboard
            String role = (String) session.getAttribute("role");
            redirectToDashboard(req, res, role);
        } else {
            // No active session or role, redirect to login page
            res.sendRedirect("login.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Handle POST request after login to redirect to the correct dashboard
    	 HttpSession session = req.getSession();
    	String role = (String) session.getAttribute("role");
        if (role == null) {
            res.sendRedirect("login.html");
            return;
        }

        // Save the role in session for subsequent requests
       
        session.setAttribute("role", role);

        // Redirect based on the role
        redirectToDashboard(req, res, role);
    }

    // Helper method to redirect to the appropriate dashboard
    private void redirectToDashboard(HttpServletRequest req, HttpServletResponse res, String role)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;

        // Students and Teachers go to the same dashboard
        if (role.equalsIgnoreCase("STUDENT") || role.equalsIgnoreCase("TEACHER")) {
            dispatcher = req.getRequestDispatcher("/dashboard.jsp");
        }
        // Librarians have their own dashboard
        else if (role.equalsIgnoreCase("LIBRARIAN") || role.equalsIgnoreCase("HOD") || role.equalsIgnoreCase("MANAGER") || role.equalsIgnoreCase("DEAN")) {
            dispatcher = req.getRequestDispatcher("/librariandashboard.jsp");
        }
        // HoD and Registrar are treated like librarians
         
        // If no valid role is found, redirect to login
        else {
            res.sendRedirect("login.html");
            return;
        }

        dispatcher.forward(req, res);
    }
}
