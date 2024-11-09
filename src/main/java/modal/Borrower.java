package modal;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import jakarta.persistence.*;

@Entity
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generate the integer ID
    private int id;

    @ManyToOne
    @JoinColumn(name = "bookId", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Date dueDate;

    @Column
    private Date pickupDate;

    @Column
    private Date returnDate;

    @Column
    private int fine;

    @Column
    private int lateChargeFees;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User reader;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public int getLateChargeFees() {
        return lateChargeFees;
    }

    public void setLateChargeFees(int lateChargeFees) {
        this.lateChargeFees = lateChargeFees;
    }

    public User getReader() {
        return reader;
    }

    public void setReader(User reader) {
        this.reader = reader;
    }
    public int calculateFine(LocalDate dueDate, LocalDate currentDate) {
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, currentDate);

        if (daysOverdue <= 0) {
            return 0;
        }

        int finePerDay = 10; // Fine per day
        return finePerDay * (int) daysOverdue; // Total fine
    }
    
    
}
