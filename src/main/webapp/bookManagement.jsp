<%@ page import="java.util.*, Dao.ShelfDao, Dao.BookDao, modal.Shelf, modal.Room, Dao.RoomDao" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Book Management</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        /* Main layout */
        body {
            font-family: Arial, sans-serif;
            display: flex;
            margin: 0;
        }

        /* Sidebar styling */
        #sidebar {
            width: 200px;
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
            padding: 10px 20px;
            border-bottom: 1px solid #495057;
        }

        #sidebar a:hover {
            background-color: #495057;
        }

        /* Main content area */
        #main-content {
            margin-left: 200px;
            padding: 20px;
            width: calc(100% - 200px);
            overflow-x: auto;
        }

        /* Book form and table styling */
        #bookManagement {
            display: flex;
            flex-direction: column;
        }

        .form-and-table-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: space-between;
        }

        .form-section, .table-section {
            width: 100%;
            max-width: 100%;
            margin-bottom: 20px;
        }

        .form-section {
            flex: 1 1 300px;
            max-width: 400px;
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .table-section {
            flex: 2 1 600px;
            overflow-x: auto;
        }

        .table-section table {
            width: 100%;
            min-width: 600px;
            table-layout: auto;
        }

        /* Form buttons styling */
        .form-section button {
            margin-right: 10px;
        }

        /* Button group styling in table */
        .button-group {
            display: flex;
            gap: 5px; /* space between buttons */
        }
    </style>

    <script>
        function setAction(action) {
            document.getElementById("action").value = action;
            document.getElementById("bookForm").submit();
        }

        function resetForm() {
            document.getElementById("bookForm").reset();
            document.getElementById("action").value = "add";
        }

        function editBook(title, isbn) {
            document.getElementById("bookTitle").value = title;
            document.getElementById("isbn").value = isbn;
            document.getElementById("action").value = "update";
        }
    </script>
</head>
<body>

    <!-- Main Content -->
    
        <div id="bookManagement" class="section">
            
            <!-- Room Search Section -->
            <div id="totalbooks" class="search-section mb-4">
                <h3>Number of Books in a Room</h3>
                <form id="availablebooks" action="availablebooks" method="post">
                    <label for="room">Room</label>
                    <select id="room" name="room" class="form-control" required>
                        <option value="" disabled selected>Select Room</option>
                        <% 
                            RoomDao roomDao = new RoomDao();
                            List<Room> rooms = roomDao.getAllRoom();
                            for (Room room : rooms) { 
                        %>
                        <option value="<%= room.getRoomId() %>"><%= room.getRoomCode() %></option>
                        <% } %>
                    </select>
                    <button type="submit" class="btn btn-primary mt-2">Avalaible Books</button>
                </form>
            </div>

            <!-- Success Message Display -->
            <% String message = (String) request.getAttribute("message"); %>
            <c:if test="${not empty message}">
                <div class="alert alert-success">
                    <h6>${message}</h6>
                </div>
            </c:if>

            <div class="form-and-table-container">
                <!-- Book Form Section -->
                <div class="form-section">
                    <h3>Book Management</h3>
                    <form id="bookForm" action="book" method="post">
                        <input type="hidden" id="action" name="action" value="add">
                        <!-- Form Fields -->
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
                                    List<Shelf> shelves = daoshelf.getAllShelfs();
                                    for (Shelf shelf : shelves) {
                                %>
                                <option value="<%= shelf.getShelfId() %>"><%= shelf.getBookCategory() %></option>
                                <% } %>
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
                </div>

                <!-- Book List Table Section -->
                <div class="table-section">
                    <h4>Book List</h4>
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Title</th>
                                <th>Status</th>
                                <th>Publisher</th>
                                <th>Shelf Category</th>
                                <th>Room</th>
                                <th>ISBN</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="bookTable">
                            <% 
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
                                <td class="button-group">
                                    <button class="btn btn-info" onclick="editBook('<%= book.get("bookTitle") %>', '<%= book.get("isbn") %>')">Edit</button>
                                    <button class="btn btn-danger">Delete</button>
                                </td>
                            </tr>
                            <% 
                                    }
                                } 
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

  
</body>
</html>
