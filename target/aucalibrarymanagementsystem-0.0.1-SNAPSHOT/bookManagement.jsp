<%@ page import="java.util.*, Dao.ShelfDao,Dao.BookDao, modal.Shelf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Book Management</title>
    <link rel="stylesheet" href="styles.css">
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
    <!-- Book Management Section -->
        <div id="bookManagement" class="section">
        <% String message = (String) request.getAttribute("message"); %>
    <c:if test="${not empty message}">
    <div class="alert alert-success">
        <h4>${message}</h4>
    </div>
   </c:if>
            <h3>Book Management</h3>
            <form id="bookForm" action="book" method="post">
                <input type="hidden" id="action" name="action" value="add"> <!-- Hidden input for action -->

                <div class="form-group">
                    <label for="bookTitle">Book Title</label>
                    <input type="text" class="form-control" id="bookTitle" name="bookTitle" required>
                </div>
                <div class="form-group">
             <label for="author">Author</label>
             <input type="text" class="form-control" id="author" name="author" required>
                </div>
                <div class="form-group">
              <label for="edition">Edition</label>
              <input type="text" class="form-control" id="edition" name="edition" required>
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
            <th>Status</th>
            <th>Publisher Name</th>
            <th>Shelf Category</th>
            <th>Room</th>
            <th>ISBN</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody id="bookTable">
        <% 
            // Retrieve books with room and shelf info
            BookDao bookDao = new BookDao();
            List<Map<String, String>> books = bookDao.getAllBooksWithRoomAndShelf();

            if (books != null && !books.isEmpty()) {
                for (Map<String, String> book : books) {
        %>
        <tr>
            <td><%= book.get("bookTitle") %></td>
            <td><%= book.get("bookStatus") %></td>
            <td><%= book.get("publisherName") %></td>
            <td><%= book.get("shelfCategory") %></td>
            <td><%= book.get("roomName") %></td>
            <td><%= book.get("isbn") %></td>
            <td>
                <button class="btn btn-info" 
                        onclick="editBook('<%= book.get("title") %>', '<%= book.get("isbn") %>')">Edit</button>
                <button class="btn btn-danger">Delete</button>
            </td>
        </tr>
        <% 
                } 
            } else { 
        %>
        <tr>
            <td colspan="7">No books found.</td>
        </tr>
        <% } %>
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
            document.getElementById("bookForm").submit(); // Submit the form
        }
    </script>
    
</body>
</html>
