package View;

import Dao.RoomDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/availablebooks")
public class AvailableBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Helper method to check if a session is valid
    private boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get existing session, don't create a new one
        return (session != null && session.getAttribute("userId") != null);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is logged in
        if (!isSessionValid(request)) {
            // Redirect to login page if session is not valid
            response.sendRedirect("login.html");
            return;
        }

        // Forward to the book management page if session is valid
        RequestDispatcher dispatcher = request.getRequestDispatcher("bookManagement.jsp?section=totalbooks");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is logged in
        if (!isSessionValid(request)) {
            // Redirect to login page if session is not valid
            response.sendRedirect("login.html");
            return;
        }

        // Get the roomId from the request parameter
        String roomIdParam = request.getParameter("room");
        int roomId;
        try {
            roomId = Integer.parseInt(roomIdParam);
        } catch (NumberFormatException e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Invalid Room ID provided</h3>");
            return;
        }

        // Get the total available books for the specified roomId
        RoomDao dao = new RoomDao();
        int totalAvailableBooks = dao.getTotalAvailableBooksInRoom(roomId);

        // Set response content type and write the output
        request.setAttribute("message", "Available books:" + totalAvailableBooks );
        RequestDispatcher dispatcher = request.getRequestDispatcher("/bookManagement.jsp");
        dispatcher.forward(request, response);
        

        // Include the section on the book management page
        
    }
}
