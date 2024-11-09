package modal;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class MembershipType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generate the integer ID
    private int membershipTypeId; // Changed from UUID to int

    @Column(nullable = false)
    private String membershipName;

    @Column(nullable = false)
    private int maxBooks;

    @Column(nullable = false)
    private int price;

    @OneToMany(mappedBy = "membershipType")
    private List<Membership> memberships;

    // Getters and Setters

    public int getMembershipTypeId() {
        return membershipTypeId;
    }

    public void setMembershipTypeId(int membershipTypeId) {
        this.membershipTypeId = membershipTypeId;
    }

    public String getMembershipName() {
        return membershipName;
    }

    public void setMembershipName(String membershipName) {
        this.membershipName = membershipName;
    }

    public int getMaxBooks() {
        return maxBooks;
    }

    public void setMaxBooks(int maxBooks) {
        this.maxBooks = maxBooks;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }
}
