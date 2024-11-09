package View;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import Dao.UserDao;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modal.User;

@WebFilter(urlPatterns = "/home")
public class AuthenticationFilter extends HttpFilter implements Filter {

    private static final long serialVersionUID = 1L;

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false); // Get existing session, don't create a new one

        // Step 1: Check if session is active
        if (session != null && session.getAttribute("role") != null) {
            chain.doFilter(req, res); // Session is valid, proceed to requested resource
            return;
        }

        // Step 2: Handle login attempts for POST requests (initial authentication)
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String username = req.getParameter("username");
            String password = hashPassword(req.getParameter("password"));

            // Authenticate user via UserDao
            UserDao dao = new UserDao();
            User theUser = new User();
            theUser.setUsername(username);
            theUser.setPassword(password);

            try {
                User authenticatedUser = dao.login(theUser);
                
                if (authenticatedUser != null) {
                	String role = authenticatedUser.getRole().toString();
                	int userId = authenticatedUser.getUserId();
                	
                    // Create a new session and set user role
                    session = req.getSession(); // Create new session
                    session.setAttribute("role", role);
                    session.setAttribute("userId", userId );
                    session.setMaxInactiveInterval(5*60); // 30 minutes session timeout

                    chain.doFilter(req, res); // User authenticated, proceed
                } else {
                    // Authentication failed
                    sendInvalidCredentialsResponse(req, res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendErrorResponse(req, res, "An unexpected error occurred. Please try again.");
            }

        } else {
            // No session and not a login attempt, redirect to login page
            res.sendRedirect("login.html");
        }
    }

    /**
     * Utility method to hash passwords using SHA-256.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Utility method to send an "Invalid credentials" response.
     */
    private void sendInvalidCredentialsResponse(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        out.println("<h1>Invalid credentials</h1>");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/login.html");
        dispatcher.include(req, res);
    }

    /**
     * Utility method to send a generic error response.
     */
    private void sendErrorResponse(HttpServletRequest req, HttpServletResponse res, String message)
            throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        out.println("<h1>" + message + "</h1>");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/login.html");
        dispatcher.include(req, res);
    }
}
