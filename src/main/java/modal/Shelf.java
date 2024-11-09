package modal;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Shelf {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generate the integer ID
    private int shelfId; // Changed from UUID to int

    @Column(nullable = false)
    private String shelfCode;

    @Column(nullable = false)
    private int availableStock;

    @Column(nullable = false)
    private int initialStock;

    @Column(nullable = false)
    private int borrowedNumber;

    @Column(nullable = false)
    private String bookCategory;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;

    @OneToMany(mappedBy ="shelf")
    private List<Book> booklist;

    // Getters and Setters

    public int getShelfId() {
        return shelfId;
    }

    public void setShelfId(int shelfId) {
        this.shelfId = shelfId;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public int getInitialStock() {
        return initialStock;
    }

    public void setInitialStock(int initialStock) {
        this.initialStock = initialStock;
    }

    public int getBorrowedNumber() {
        return borrowedNumber;
    }

    public void setBorrowedNumber(int borrowedNumber) {
        this.borrowedNumber = borrowedNumber;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Book> getBooklist() {
        return booklist;
    }

    public void setBooklist(List<Book> booklist) {
        this.booklist = booklist;
    }
}
