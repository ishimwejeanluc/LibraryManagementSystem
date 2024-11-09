package View;



import java.io.IOException;
import java.util.List;
import Dao.MembershipDao;
import Dao.ShelfDao;
import Dao.BorrowerDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modal.Membership;
import modal.Shelf;
import modal.Borrower;

@WebServlet(urlPatterns = "/admin")
public class AdministratorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null && session.getAttribute("userId") != null);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isSessionValid(request)) {
            response.sendRedirect("login.html");
            return;
        }
        
        String action = request.getParameter("section");

        if ("memberships".equals(action)) {
            List<Membership> memberships = new MembershipDao().getPendingMemberships();
            request.setAttribute("memberships", memberships);
            RequestDispatcher dispatcher = request.getRequestDispatcher("hodMemberApproval.jsp");
            dispatcher.forward(request, response);
        } else if ("shelves".equals(action)) {
            List<Shelf> shelves = new ShelfDao().getAllShelfs();
            request.setAttribute("shelves", shelves);
            RequestDispatcher dispatcher = request.getRequestDispatcher("hodShelfManagement.jsp");
            dispatcher.forward(request, response);
        } else if ("borrowers".equals(action)) {
            List<Borrower> borrowers = new BorrowerDao().getAllBorrowers();
            request.setAttribute("borrowers", borrowers);
            RequestDispatcher dispatcher = request.getRequestDispatcher("hodBorrowerManagement.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("dashboard.jsp");
        }
    }
}
