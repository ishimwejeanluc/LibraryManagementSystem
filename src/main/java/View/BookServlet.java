package View;

import java.io.IOException;
import Dao.BookDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modal.Book;
import modal.BookStatus;
import modal.Shelf;

@WebServlet(value = "/book")
public class BookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Helper method to check if a session is valid
    private boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get existing session, don't create a new one
        return (session != null && session.getAttribute("userId") != null);
    }

    // Helper method to check if the user has librarian role
    private boolean isLibrarian(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String userRole = (session != null) ? (String) session.getAttribute("role") : null;
        return "librarian".equalsIgnoreCase(userRole);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is logged in
        if (!isSessionValid(request)) {
            // Redirect to login page if session is not valid
            response.sendRedirect("login.html");
            return;
        }

        // Forward to book management page if session is valid
        RequestDispatcher dispatcher = request.getRequestDispatcher("bookManagement.jsp");
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

        // Check if the user is a librarian
        if (!isLibrarian(request)) {
            request.setAttribute("message", "You are not authorized to perform this action.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/bookManagement.jsp");
            dispatcher.forward(request, response);
            return;
        }

        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            // Retrieve book details from the request
            String bookTitle = request.getParameter("bookTitle");
            String publisherName = request.getParameter("publisherName");
            String isbn = request.getParameter("isbn");
            String author = request.getParameter("author");
            
            int shelfCategory;
            try {
                shelfCategory = Integer.parseInt(request.getParameter("shelfCategory"));
            } catch (NumberFormatException e) {
                request.setAttribute("message", "Invalid shelf category ID");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/bookManagement.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            // Create Shelf and Book objects
            Shelf shelf = new Shelf();
            shelf.setShelfId(shelfCategory);
            
            Book book = new Book();
            book.setAuthor(author);
            book.setTitle(bookTitle);
            book.setShelf(shelf);
            book.setISBN(isbn);
            book.setPublisher(publisherName);
            book.setStatus(BookStatus.AVAILABLE);

            // Save the book using BookDao
            BookDao dao = new BookDao();
            String isSaved = dao.saveBook(book);

            // Set appropriate messages based on save result
            if ("saved".equals(isSaved)) {
                request.setAttribute("message", "Book added successfully");
            } else {
                request.setAttribute("message", "Failed to add book");
            }

            // Forward to the book management page with a success/failure message
            RequestDispatcher dispatcher = request.getRequestDispatcher("/bookManagement.jsp");
            dispatcher.forward(request, response);
        } else {
            // Handle other actions if needed
            response.sendRedirect("bookManagement.jsp");
        }
    }
}
