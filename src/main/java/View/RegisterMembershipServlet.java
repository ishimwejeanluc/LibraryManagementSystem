package View;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Random;

import Dao.MembershipDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modal.Membership;
import modal.MembershipType;
import modal.Status;
import modal.User;

@WebServlet(urlPatterns ="/register")
public class RegisterMembershipServlet extends HttpServlet {

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
            response.sendRedirect("login.html"); // Redirect to login page if session is invalid
        } else {
            response.sendRedirect("dashboard.jsp?section=membership"); // Redirect to membership section if session is valid
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if session is valid
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html"); // Redirect to login if session is invalid
            return;
        }

        // Retrieve the form data and process it
        int typeId = Integer.parseInt(request.getParameter("membershipType"));
        Date regDate = stringToDate(request.getParameter("date"));
        Date expDate = calculateExpirationDate(regDate);
        String code = generateRandomCharacter();
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Create the Membership object
        Membership membership = new Membership();
        MembershipType type = new MembershipType();
        User user = new User();
        user.setUserId(userId);
        type.setMembershipTypeId(typeId);
        membership.setExpiringTime(expDate);
        membership.setRegistrationDate(regDate);
        membership.setMembershipType(type);
        membership.setMembershipStatus(Status.PENDING);
        membership.setMembershipCode(code);
        membership.setUser(user);

        // Save the membership information
        MembershipDao membershipdao = new MembershipDao();
        String isSaved = membershipdao.saveMembership(membership);

        // Forward or redirect with appropriate messages
        if ("saved".equalsIgnoreCase(isSaved)) {
            request.getSession().setAttribute("successMessage", "Registration made successfully!");
            response.sendRedirect("dashboard.jsp?section=membership");
        } else {
            request.getSession().setAttribute("errorMessage", "Error creating membership.");
            response.sendRedirect("dashboard.jsp?section=membership");
        }
    }

    // Utility method to convert String to SQL Date
    public static Date stringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sqlDate = null;

        try {
            java.util.Date parsedDate = dateFormat.parse(dateString);
            sqlDate = new Date(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace(); // Log the exception or handle it as needed
        }

        return sqlDate;
    }

    // Utility method to calculate expiration date (30 days after registration)
    public static Date calculateExpirationDate(Date regDate) {
        LocalDate registrationLocalDate = regDate.toLocalDate();
        LocalDate expirationLocalDate = registrationLocalDate.plusDays(30);
        return Date.valueOf(expirationLocalDate);
    }

    // Method to generate a random character string
    public static String generateRandomCharacter() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        return String.valueOf(characters.charAt(random.nextInt(characters.length())));
    }
}
