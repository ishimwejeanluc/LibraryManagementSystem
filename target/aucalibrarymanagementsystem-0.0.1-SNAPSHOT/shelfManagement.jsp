<%@page import="Dao.RoomDao"%>
<%@ page import="java.util.List, Dao.ShelfDao, modal.Shelf, modal.Room" %>
<!DOCTYPE html>
<html>
<head>
    <title>Shelf Management</title>
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
        
        #shelfManagement {
            margin-left: 200px;
            padding: 20px;
            width: calc(100% - 200px);
            display: grid;
            grid-template-columns: 1fr;
            align-items: center; 
        }
        .selected-row {
            background-color: #d6e9f9;
        }
    </style>
</head>
<body>
    <div id="sidebar">
        <!-- Sidebar Links (e.g., for navigation) -->
    </div>

    <div id="shelfManagement" class="section">
        <h3>Shelf Management</h3>
        
        <form id="shelfForm" action="shelf" method="post">
            <input type="hidden" id="action" name="action" value="addShelf">

            <div class="form-group">
                <label for="initialStock">Initial Stock</label>
                <input type="number" class="form-control" id="initialStock" name="initialStock" required>
            </div>
            <div class="form-group">
                <label for="room">Room</label>
                <select id="room" name="room" class="form-control" required>
                    <option value="" disabled selected>Select Room</option>
                    <% 
                        // Retrieve room list
                       RoomDao roomDao = new RoomDao();
                        List<Room> rooms = roomDao.getAllRoom();
                    %>
                    <% for (Room room : rooms) { %>
                        <option value="<%= room.getRoomId() %>"><%= room.getRoomCode() %></option>
                    <% } %>
                </select>
            </div>
            <div class="form-group">
                <label for="roomLocated">Category</label>
                <input type="text" class="form-control" id="shelfCategory" name="shelfCategory" required>
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
    <% 
        // Retrieve shelves
        ShelfDao shelfDao = new ShelfDao();
        List<Shelf> shelves = shelfDao.getAllShelfs();

        if (shelves != null && !shelves.isEmpty()) {
            for (Shelf shelf : shelves) {
                Room room = shelf.getRoom();
    %>
    <tr>
        <td><%= shelf.getInitialStock() %></td>
        <td><%= shelf.getBookCategory() %></td>
        <td><%= room.getRoomCode() %></td>
        <td>
            <button 
                class="btn btn-info" 
                onclick="editShelf('<%= shelf.getInitialStock() %>', '<%= shelf.getBookCategory() %>')">Edit</button>
            <button class="btn btn-danger">Delete</button>
        </td>
    </tr>
    <% 
            } 
        } else { 
    %>
    <tr>
        <td colspan="4">No shelves found.</td>
    </tr>
    <% } %>
</tbody>
            
        </table>
    </div>

    <script>
        function setShelfAction(action) {
            document.getElementById("action").value = action;
            document.getElementById("shelfForm").submit();
        }
        
        function resetShelfForm() {
            document.getElementById('shelfForm').reset();
        }
        function editShelf(initialStock, category) {
            document.getElementById("initialStock").value = initialStock;
            document.getElementById("shelfCategory").value = category;
            }
    </script>
</body>
</html>
