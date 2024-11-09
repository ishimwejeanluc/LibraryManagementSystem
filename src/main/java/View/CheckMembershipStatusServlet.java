package View;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import Dao.MembershipDao;

@WebServlet(urlPatterns = "/checkMembershipStatus")
public class CheckMembershipStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Helper method to validate user session
    private boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null && session.getAttribute("userId") != null);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Validate session
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html");
            return;
        }
        // If session is valid, redirect to the membership status section
        response.sendRedirect("dashboard.jsp?section=checkStatus");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Validate session
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html");
            return;
        }

        String action = request.getParameter("action");
        if ("checkStatus".equals(action)) {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            // Handle case where userId might be null
            if (userId == null) {
                
                response.sendRedirect("login.html");
                return;
            }

            MembershipDao membershipDao = new MembershipDao();
            String status = membershipDao.getMembershipStatusByUserId(userId);

            // Prepare response
            

            
            if (status != null && !status.isEmpty()) {
                session.setAttribute("statussuccessmessage","Your membership status is: " + status.toUpperCase() );
            } else {
            	 session.setAttribute("statuserrormessage","Your membership not found register for membership");
            }
           

            // Include the dashboard.jsp for additional content if needed
            response.sendRedirect("dashboard.jsp?section=checkStatus");
            
        } else {
            // Handle other actions or missing action parameters
            response.sendRedirect("dashboard.jsp?section=checkStatus");
        }
    }
}
