package View;

import java.io.IOException;
import Dao.MembershipDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modal.Status;

@WebServlet(urlPatterns = "/memberapproval")
public class MemberApprovalServlet extends HttpServlet {
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
            response.sendRedirect("login.html"); // Redirect to login if session is invalid
            return;
        }

        // If valid librarian, forward to memberApproval.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/memberApproval.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if session is valid
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html");
            return;
        }

        // Check if the user is a librarian
        if (!isLibrarian(request)) {
            // If not a librarian, deny the action and show error message
            response.getWriter().write("Access Denied: You do not have permission to perform this action.");
            return;
        }

        // Get action and membershipId from the request
        String action = request.getParameter("action");
        int membershipId = Integer.parseInt(request.getParameter("membershipId"));

        // Create an instance of MembershipDao to update status
        MembershipDao dao = new MembershipDao();
        boolean result = false;

        // Determine the status based on the action
        Status status = null;
        if ("approve".equalsIgnoreCase(action)) {
            status = Status.APPROVED;
        } else if ("reject".equalsIgnoreCase(action)) {
            status = Status.REJECTED;
        }

        // Update the membership status in the database
        if (status != null) {
            result = dao.updateMembershipStatus(membershipId, status);
        }

        // Prepare the response message
        String message = null;
        if (result) {
            message = "Membership status updated to: " + status;
        } else {
            message = "Failed to update membership status for ID: " + membershipId;
        }

        // Send the message back to the client
        response.getWriter().write(message);
    }
}
