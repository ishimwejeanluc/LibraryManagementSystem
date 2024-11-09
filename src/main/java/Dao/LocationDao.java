package Dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import jakarta.persistence.Query;
import modal.Location;
import util.HibernateUtil;

public class LocationDao {
	
	public List<Location> getProvinces() {
	    List<Location> provinces = new ArrayList<>();
	    try (Session session = HibernateUtil.getSession().openSession()) {
	        // Use a query to fetch provinces
	        provinces = session.createQuery("FROM Location WHERE locationType = 'PROVINCE'", Location.class).list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    // Return an empty list if no provinces found or if there was an exception
	    return provinces; // This will be empty instead of null
	}


    // Retrieve districts by province ID
	public List<Location> getDistrictsByProvince(int lo) {
	    List<Location> results = new ArrayList<>();
	    try (Session session = HibernateUtil.getSession().openSession()) {
	    	String sql = "SELECT * FROM location WHERE locationType = :locationType AND parent_id = :parentId";
	    	Query query = session.createNativeQuery(sql, Location.class);
	    	query.setParameter("locationType", "DISTRICT"); // Make sure the value is valid
	    	query.setParameter("parentId", lo);
	    	 results = query.getResultList();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return results;
	}

    // Retrieve sectors by district ID
    public List<Location> getSectorsByDistrict(int parentId) {
        List<Location> sectors = new ArrayList<>();
        try (Session session = HibernateUtil.getSession().openSession()) {
        	String sql = "SELECT * FROM location WHERE locationType = :locationType AND parent_id = :parentId";
	    	Query query = session.createNativeQuery(sql, Location.class);
	    	query.setParameter("locationType", "SECTOR"); // Make sure the value is valid
	    	query.setParameter("parentId", parentId);
	    	 sectors = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sectors;
    }

    // Retrieve cells by sector ID
    public List<Location> getCellsBySector(int parentId) {
        List<Location> cells = new ArrayList<>();
        try (Session session = HibernateUtil.getSession().openSession()) {
        	String sql = "SELECT * FROM location WHERE locationType = :locationType AND parent_id = :parentId";
	    	Query query = session.createNativeQuery(sql, Location.class);
	    	query.setParameter("locationType", "CELL"); // Make sure the value is valid
	    	query.setParameter("parentId", parentId);
	    	 cells = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cells;
    }

    // Retrieve villages by cell ID
    public List<Location> getVillagesByCell(int parentId) {
        List<Location> villages = new ArrayList<>();
        try (Session session = HibernateUtil.getSession().openSession()) {
        	String sql = "SELECT * FROM location WHERE locationType = :locationType AND parent_id = :parentId";
	    	Query query = session.createNativeQuery(sql, Location.class);
	    	query.setParameter("locationType", "VILLAGE"); // Make sure the value is valid
	    	query.setParameter("parentId", parentId);
	    	 villages = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return villages;
    }
}
