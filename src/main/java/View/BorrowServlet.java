package View;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import Dao.BookDao;
import Dao.BorrowerDao;
import Dao.MembershipDao;
import modal.Book;
import modal.BookStatus;
import modal.Borrower;
import modal.User;

@WebServlet(urlPatterns = "/borrow")
public class BorrowServlet extends HttpServlet {
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
        // If session is valid, forward to the borrowing section in the dashboard
        response.sendRedirect("dashboard.jsp?section=borrowBook");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if session is valid
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html");
            return;
        }

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        Borrower borrower = new Borrower();
        User reader = new User();
        Book book = new Book();
        BookDao bookDao = new BookDao();
        BorrowerDao borrowerDao = new BorrowerDao();
        MembershipDao membershipDao = new MembershipDao();

        // Step 1: Check if user has a valid membership
        if (!membershipDao.hasMembership(userId)) {
            session.setAttribute("borrowErrorMessage", "You do not have a membership. Please register first.");
            response.sendRedirect("dashboard.jsp?section=borrowBook");
            return;
        }

        // Step 2: Check membership approval status
        String membershipStatus = membershipDao.getMembershipStatusByUserId(userId);
        if (!"APPROVED".equalsIgnoreCase(membershipStatus)) {
            session.setAttribute("borrowErrorMessage", "Your membership is not approved yet.");
            response.sendRedirect("dashboard.jsp?section=borrowBook");
            return;
        }

        // Step 3: Check if user has exceeded borrowing limit
        int maxBooksAllowed = membershipDao.getMaxBooksAllowed(userId);
        int borrowedBooksCount = borrowerDao.getBorrowedBooksCount(userId);
        if (borrowedBooksCount >= maxBooksAllowed) {
            session.setAttribute("borrowErrorMessage", "You have surpassed your borrowing limit.");
            response.sendRedirect("dashboard.jsp?section=borrowBook");
            return;
        }

        // Step 4: Proceed with the borrowing process
        try {
            int bookId = Integer.parseInt(request.getParameter("book"));
            Date pickupDate = stringToDate(request.getParameter("pickupDate"));
            Date returnDate = stringToDate(request.getParameter("returnDate"));

            LocalDate dueDate = calculateExpirationDate(pickupDate);
            LocalDate currentDate = LocalDate.now();
            int lateCharge = borrower.calculateFine(dueDate, currentDate);

            // Set borrower details
            book.setBookId(bookId);
            borrower.setBook(book);
            reader.setUserId(userId);
            borrower.setPickupDate(pickupDate);
            borrower.setReturnDate(returnDate);
            borrower.setDueDate(Date.valueOf(dueDate));
            borrower.setReader(reader);
            borrower.setLateChargeFees(lateCharge);
            borrower.setFine(10); // Default fine (can be adjusted)

            // Save borrower information
            String isSaved = borrowerDao.saveBorrower(borrower);

            // Step 5: Update book status and provide user feedback
            if ("saved".equalsIgnoreCase(isSaved)) {
                bookDao.updateBookStatus(bookId, BookStatus.RESERVED);
                session.setAttribute("borrowSuccessMessage", "Book borrowed successfully.");
            } else {
                session.setAttribute("borrowErrorMessage", "Failed to borrow the book.");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("borrowErrorMessage", "Invalid book ID or date format.");
        }

        // Redirect to the borrowBook section of the dashboard
        response.sendRedirect("dashboard.jsp?section=borrowBook");
    }

    // Utility method to convert String to SQL Date
    public static Date stringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date parsedDate = dateFormat.parse(dateString);
            return new Date(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Utility method to calculate due date (30 days from pickup date)
    public static LocalDate calculateExpirationDate(Date regDate) {
        if (regDate != null) {
            return regDate.toLocalDate().plusDays(30);
        }
        return LocalDate.now().plusDays(30); // Default due date
    }
}
