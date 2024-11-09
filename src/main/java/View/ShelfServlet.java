package View;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modal.Room;
import modal.Shelf;
import Dao.ShelfDao;

import java.io.IOException;
import java.security.SecureRandom;

@WebServlet(value = "/shelf")
public class ShelfServlet extends HttpServlet {
	 
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 4;
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final long serialVersionUID = 1L;

    // Helper method to check session validity
    private boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null && session.getAttribute("userId") != null);
    }

    // Helper method to check if the user is a librarian
    private boolean isLibrarian(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String userRole = (session != null) ? (String) session.getAttribute("role") : null;
        return "librarian".equalsIgnoreCase(userRole);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if session is valid
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html"); // Redirect to login page if session is invalid
            return;
        }

        // Forward the request to shelf management page
        RequestDispatcher dispatcher = request.getRequestDispatcher("shelfManagement.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if session is valid
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html"); // Redirect to login page if session is invalid
            return;
        }

        // Check if the user is a librarian
        if (!isLibrarian(request)) {
            request.setAttribute("ErrorShelfMessage", "Access Denied: Only librarians can perform this action.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("shelfManagement.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Get parameters for adding a shelf
        String action = request.getParameter("action");

        if ("addShelf".equals(action)) {
            try {
                int roomId = Integer.parseInt(request.getParameter("room"));
                int initialStock = Integer.parseInt(request.getParameter("initialStock"));
                String category = request.getParameter("shelfCategory");
                

                // Create Room and Shelf objects and set properties
                Room room = new Room();
                room.setRoomId(roomId);

                Shelf shelf = new Shelf();
                shelf.setRoom(room);
                shelf.setInitialStock(initialStock);
                shelf.setAvailableStock(initialStock);
                shelf.setBorrowedNumber(0); 
                shelf.setShelfCode(generateShelfCode()); 
                shelf.setBookCategory(category);

                // Save shelf using ShelfDao
                ShelfDao shelfDao = new ShelfDao();
                String result = shelfDao.saveShelf(shelf);

                // Redirect with appropriate messages based on the result
                if ("saved".equalsIgnoreCase(result)) {
                    request.setAttribute("SuccessShelfMessage", "Shelf created successfully!");
                } else {
                    request.setAttribute("ErrorShelfMessage", "Error creating shelf.");
                }

            } catch (NumberFormatException e) {
                request.setAttribute("ErrorShelfMessage", "Invalid input. Please check the values.");
            }

            // Forward back to shelfManagement.jsp with the result message
            RequestDispatcher dispatcher = request.getRequestDispatcher("shelfManagement.jsp?section=shelfManagement");
            dispatcher.forward(request, response);
        }
       
    }
    public static String generateShelfCode() {
        StringBuilder shelfCode = new StringBuilder(CODE_LENGTH);
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHAR_SET.length());
            shelfCode.append(CHAR_SET.charAt(randomIndex));
        }
        
        return shelfCode.toString();
    }
}
