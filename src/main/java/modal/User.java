package modal;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User extends Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generate the integer ID
    private int userId; // Changed from UUID to int
    
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @OneToMany(mappedBy = "user")
    private List<Membership> membershipList;

    @ManyToOne
    @JoinColumn(name = "village_id", nullable = false)
    private Location village;

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Membership> getMembershipList() {
        return membershipList;
    }

    public void setMembershipList(List<Membership> membershipList) {
        this.membershipList = membershipList;
    }

    public Location getVillage() {
        return village;
    }

    public void setVillage(Location village) {
        this.village = village;
    }
}
