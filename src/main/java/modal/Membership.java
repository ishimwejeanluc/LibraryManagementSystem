package modal;

import java.util.Date;
import jakarta.persistence.*;

@Entity
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generate the integer ID
    private int membershipId;

    @Column(nullable = false)
    private String membershipCode;

    @ManyToOne
    @JoinColumn(name = "membershipTypeId", nullable = false)
    private MembershipType membershipType;

    @Column(nullable = false)
    private Date registrationDate;

    @Column(nullable = false)
    private Date expiringTime;

    @Enumerated(EnumType.STRING)
    private Status membershipStatus;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user; // Changed variable name to lowercase

    // Getters and Setters

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public String getMembershipCode() {
        return membershipCode;
    }

    public void setMembershipCode(String membershipCode) {
        this.membershipCode = membershipCode;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getExpiringTime() {
        return expiringTime;
    }

    public void setExpiringTime(Date expiringTime) {
        this.expiringTime = expiringTime;
    }

    public Status getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(Status membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public User getUser() {
        return user; // Changed to lowercase
    }

    public void setUser(User user) {
        this.user = user; // Changed to lowercase
    }
}
