package View;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import Dao.BorrowerDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modal.Borrower;

@WebServlet(urlPatterns = "/fine")
public class FineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Helper method to check session validity
    private boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null && session.getAttribute("userId") != null);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if session is valid
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html");
            return;
        }
        response.sendRedirect("dashboard.jsp?section=fines");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("fine".equals(action)) {
            // Check if session is valid
            if (!isSessionValid(request)) {
                response.sendRedirect("login.html");
                return;
            }
            // Display fines for the user
            displayFines(request, response);
        } else {
            response.sendRedirect("dashboard.jsp");
        }
    }

    private void displayFines(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Initialize BorrowerDao to retrieve borrowed books
        BorrowerDao borrowerDao = new BorrowerDao();

        // Retrieve all borrowed books for the current user
        List<Borrower> borrowedBooks = borrowerDao.getBorrowedBooksByUserId(userId);

        if (borrowedBooks != null && !borrowedBooks.isEmpty()) {
           
            for (Borrower borrowedBook : borrowedBooks) {
                // Fetch book details
                String bookTitle = borrowedBook.getBook().getTitle();
                Date dueDate = borrowedBook.getDueDate();

                // Convert java.util.Date to LocalDate
                LocalDate localDueDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate currentDate = LocalDate.now();

                // Calculate fine
                int fine = borrowedBook.calculateFine(localDueDate, currentDate);

                if (fine > 0) {
                    borrowerDao.updateLateChargesFeeByBookTitle(bookTitle, fine);
            
                // Set the fine details in session attribute
                session.setAttribute("finesuccessmessage","Book:"+ bookTitle+ "Your late charges is:" +" "+ fine );
                session.removeAttribute("fineerrormessage");
            } else {
                session.setAttribute("fineerrormessage", "You have no borrowed books with fines.");
                session.removeAttribute("finesuccessmessage");
            }

        } 

        // Redirect to dashboard.jsp
        response.sendRedirect("dashboard.jsp?section=fines");
    }
}
    }
