<%@page import="Dao.RoomDao"%>
<%@ page import="java.util.List, Dao.BorrowerDao, modal.Shelf, modal.Borrower" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
     <style>
        body {
            display: flex;
        }
        #sidebar {
            min-width: 200px;
            max-width: 200px;
            background-color: #343a40;
            color: #fff;
            height: 100vh;
            position: fixed;
            padding-top: 20px;
        }
        #sidebar a {
            color: #ffffff;
            text-decoration: none;
            display: block;
            padding: 10px;
            border-bottom: 1px solid #495057;
        }
        #sidebar a:hover {
            background-color: #495057;
        }
        #main-content {
            margin-left: 200px;
            padding: 20px;
            width: 100%;
        }
        
        
       
        .selected-row {
            background-color: #d6e9f9;
        }
    </style>
    
</head>
<body>
   <div id="searchUser" class="section">
    <h3>Search User by Phone Number</h3>
    <form id="searchUser" action="searchUser" method="post">
        <div class="form-group">
            <label for="phoneNumber">Phone Number</label>
            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" required>
        </div>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>
    
  </div>
<div id="borrowerManagement" class="section">
            <h3>Borrower Management</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th> Name</th>
                        <th>Book Title</th>
                        <th>Borrow Date</th>
                        <th>Return Date</th>
                        <th>Due Date</th>
                         <th>late charges</th>
                    </tr>
                </thead>
                <tbody id="borrowerTable">
                                                                       <% 
        BorrowerDao dao1 = new BorrowerDao();
        List<Borrower> borrowerList = dao1.getAllBorrowers();

        if (borrowerList != null && !borrowerList.isEmpty()) {
            for (Borrower borrower : borrowerList) {
                // Assuming Borrower class has a method getRoom()
                ; 
    %>
                <tr>
                    <td><%= borrower.getReader().getUsername() %></td>
                    <td><%= borrower.getBook().getTitle() %></td>
                    <td><%= borrower.getPickupDate() %></td>
                    <td><%= borrower.getReturnDate() %></td>
                    <td><%= borrower.getDueDate() %></td>
                    <td><%= borrower.getLateChargeFees() %></td> <!-- Ensure getRoomName() exists -->
                </tr>
    <% 
            }
        } else { 
    %>
            <tr>
                <td colspan="4">No borrowers found.</td>
            </tr>
    <% 
        } 
    %>
                </tbody>
            </table>
        </div>
        <script>
       

        const books = [];

        function selectRow(row, index) {
            const rows = document.querySelectorAll('#bookTable tr');
            rows.forEach(r => r.classList.remove('selected-row'));
            row.classList.add('selected-row');

            const book = books[index];
            document.getElementById('bookTitle').value = book.title;
            document.getElementById('publisherYear').value = book.publisherYear;
            document.getElementById('publisherName').value = book.publisherName;
            document.getElementById('shelfCategory').value = book.shelfCategory;
            document.getElementById('room').value = book.room;
            document.getElementById('isbn').value = book.isbn;
        }

        function resetForm() {
            document.getElementById('bookForm').reset();
            document.querySelectorAll('#bookTable tr').forEach(r => r.classList.remove('selected-row'));
        }

        
        
        function setAction(action) {
            document.getElementById("action").value = action; // Set the action value
            document.getElementById("bookForm").submit(); // Submit the form
        }
        function setShelfAction(action) {
            document.getElementById("action").value = action;
            document.getElementById("shelfForm").submit(); // Submit the form
        }
    </script>
    
    

</body>
</html>