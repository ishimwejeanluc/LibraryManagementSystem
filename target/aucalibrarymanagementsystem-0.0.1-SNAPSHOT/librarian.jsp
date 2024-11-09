<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="Dao.ShelfDao" %>
<%@ page import="Dao.RoomDao" %>
<%@ page import="modal.Shelf" %>
<%@ page import="modal.Room" %>

<%@ page import="javax.sql.DataSource" %>
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
        
        .section { 
            margin-top: 20px; 
            display: none; 
        }
        #bookManagement {
            display: block; /* Set Book Management to display by default */
        }
        #shelfManagement{
         margin-left: 200px;
    padding: 20px;
    width: calc(100% - 200px); /* Adjust width based on sidebar */
    display: grid;
    grid-template-columns: 1fr; /* Define a single column */
    align-items: center; 
    }
        .selected-row {
            background-color: #d6e9f9;
        }
    </style>
</head>
<body>
    <!-- Sidebar Navigation -->
    <div id="sidebar">
        <h4 class="text-center">Librarian Menu</h4>
        <a href="javascript:void(0);" onclick="showSection('bookManagement')">Book Management</a>
        <a href="javascript:void(0);" onclick="showSection('memberApproval')">Member Approval</a>
        <a href="javascript:void(0);" onclick="showSection('borrowerManagement')">Borrower Management</a>
        <a href="javascript:void(0);" onclick="showSection('shelfManagement')">Shelf Management</a>
    </div>

    <!-- Main Content -->
    <div id="main-content">
        <h2 class="text-center mt-4">Librarian Dashboard</h2>

        <!-- Book Management Section -->
        <div id="bookManagement" class="section">
            <h3>Book Management</h3>
            <form id="bookForm" action="bookManagement" method="post">
                <input type="hidden" id="action" name="action" value="add"> <!-- Hidden input for action -->

                <div class="form-group">
                    <label for="bookTitle">Book Title</label>
                    <input type="text" class="form-control" id="bookTitle" name="bookTitle" required>
                </div>
                <div class="form-group">
                    <label for="publisherYear">Publisher Year</label>
                    <input type="date" class="form-control" id="publisherYear" name="publisherYear" required>
                </div>
                <div class="form-group">
                    <label for="publisherName">Publisher Name</label>
                    <input type="text" class="form-control" id="publisherName" name="publisherName" required>
                </div>
                <div class="form-group">
                    <label for="shelfCategory">Shelf Category</label>
                    <select id="shelfCategory" name="shelfCategory" class="form-control" required>
                        <option value="" disabled selected>Select a Shelf</option>
                                                      <%
    ShelfDao daoshelf = new ShelfDao();
    List<Shelf> shelves = new ArrayList<>();
	    
    try {
        shelves = daoshelf.getAllShelfs();
    } catch (Exception e) {
        e.printStackTrace();
    }

    if (shelves != null && !shelves.isEmpty()) {
        for (Shelf shelf : shelves) {
%>
            <option value="<%= shelf.getShelfId() %>"><%= shelf.getBookCategory() %></option>
<%
        }
    } else {
%>
        <option value="">No Shelves Available</option>
<%
    }
%>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="isbn">ISBN</label>
                    <input type="text" class="form-control" id="isbn" name="isbn" required>
                </div>
                <button type="button" onclick="setAction('add')" class="btn btn-primary">Add Book</button>
                <button type="button" onclick="setAction('update')" class="btn btn-warning">Update Book</button>
                <button type="button" onclick="resetForm()" class="btn btn-secondary">Reset</button>
            </form>
            <h4 class="mt-4">Book List</h4>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Publisher Year</th>
                        <th>Publisher Name</th>
                        <th>Shelf Category</th>
                        <th>Room</th>
                        <th>ISBN</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="bookTable">
                    <!-- Books will be displayed here -->
                </tbody>
            </table>
        </div>

        <!-- Member Approval Section -->
        <div id="memberApproval" class="section">
            <h3>Member Approval</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Member Name</th>
                        <th>Email</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="memberTable">
                    <!-- Member requests will be displayed here -->
                </tbody>
            </table>
        </div>

        <!-- Borrower Management Section -->
        <div id="borrowerManagement" class="section">
            <h3>Borrower Management</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Borrower Name</th>
                        <th>Book Title</th>
                        <th>Borrow Date</th>
                        <th>Return Date</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody id="borrowerTable">
                    <!-- Borrowers and book status will be displayed here -->
                </tbody>
            </table>
        </div>
    
        <!-- Shelf Management Section -->
        <div id="shelfManagement" class="section">
            <h3>Shelf Management</h3>
            <%
    String shelfCreated = request.getParameter("shelfManagement");
    String message = request.getParameter("message");

    if (shelfCreated != null && shelfCreated.equals("true")) {
%>
        <div class="alert alert-success">
            <%= message %>
        </div>
<% } %>
            <form id="shelfForm" action="shelf" method="post">
                <input type="hidden" id="action" name="action" value="addShelf"> <!-- Hidden input for action -->

                <div class="form-group">
                    <label for="initialStock">Initial Stock</label>
                    <input type="number" class="form-control" id="initialStock" name="initialStock" required>
                </div>
                <div class="form-group">
                    <label for="room">Room</label>
                    <select id="room" name="room" class="form-control" required>
                        <option value="" disabled selected>Select Room</option>
                        <% 
                    RoomDao roomdao = new RoomDao(); // Assuming you have initialized RoomDao properly
                    List<Room> rooms = roomdao.getAllRoom();
                         %>

                  <% if (rooms != null && !rooms.isEmpty()) { %>
                <%
                        for (Room room : rooms) { 
                 %>
                      <option value="<%= room.getRoomId() %>"><%= room.getRoomCode()  %></option>
               <% } %>
      <% } else { %>
           <option value="">No Rooms Available</option>
        <% } %> 
                    </select>
                </div>
                <div class="form-group">
                    <label for="roomLocated">Room Located In</label>
                    <input type="text" class="form-control" id="roomLocated" name="shelfCategory" required>
                </div>
                <button type="button" onclick="setShelfAction('addShelf')" class="btn btn-primary">Add Shelf</button>
                <button type="button" onclick="setShelfAction('updateShelf')" class="btn btn-warning">Update Shelf</button>
                <button type="button" onclick="resetShelfForm()" class="btn btn-secondary">Reset</button>
            </form>
            <h4 class="mt-4">Shelf List</h4>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Initial Stock</th>
                        <th>Category</th>
                        <th>Room Located</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="shelfTable">
                    <!-- Shelf data will be displayed here -->
                </tbody>
            </table>
        </div>
    
       

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            showSection('bookManagement'); // Display Book Management section by default
        });

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

        function showSection(sectionId) {
            document.querySelectorAll('.section').forEach(section => section.style.display = 'none');
            document.getElementById(sectionId).style.display = 'block';
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