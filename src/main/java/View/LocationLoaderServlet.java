package View;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import Dao.LocationDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modal.Location;
import modal.User;

@WebServlet("/LocationLoaderServlet")
public class LocationLoaderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String selectname = request.getParameter("selectname");
        int parentId = Integer.parseInt(request.getParameter("parentId"));  // Now using String for UUID

        LocationDao locationDao = new LocationDao();
        
       
        List<Location> locations;

        try {
            switch (selectname) {
                case "district":
                    locations = locationDao.getDistrictsByProvince(parentId);
                    break;
                case "sector":
                    locations = locationDao.getSectorsByDistrict(parentId);
                    break;
                case "cell":
                    locations = locationDao.getCellsBySector(parentId);
                    break;
                case "village":
                    locations = locationDao.getVillagesByCell(parentId);
                    User villageId = new User();
                    break;
                default:
                    locations = null;
            }

            if (locations != null) {
                for (Location location : locations) {
                    out.println("<option value=\"" + location.getLocationId() + "\">" + location.getLocationName() + "</option>");
                }
            } else {
                out.println("<option value=\"\">No data available</option>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<option value=\"\">Error loading data</option>");
        } finally {
            out.close();
        }
    }
}
