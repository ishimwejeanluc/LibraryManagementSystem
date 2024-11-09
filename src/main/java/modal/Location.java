package modal;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Location {
    @Id
    private int locationId;

    @Column(nullable = false)
    private String locationCode;

    @Column(nullable = false)
    private String locationName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocationType locationType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id") // Ensure this matches the DB column name
    private Location parent; // Renamed for consistency

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL) // Adjusted to match the field name
    private List<Location> children;

    // Getters and Setters
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public Location getParent() { // Renamed for consistency
        return parent;
    }

    public void setParent(Location parent) { // Renamed for consistency
        this.parent = parent;
    }

    public List<Location> getChildren() {
        return children;
    }

    public void setChildren(List<Location> children) {
        this.children = children;
    }
}
