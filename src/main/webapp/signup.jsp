<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="Dao.LocationDao"%>
<%@ page import="modal.Location"%>

<%@ page import="javax.sql.DataSource"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign Up</title>
    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <script>
        function loadOptions(selectname, parentId) {
            const xhr = new XMLHttpRequest();
            xhr.open("GET", "LocationLoaderServlet?parentId=" + parentId + "&selectname=" + selectname, true);
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    document.getElementById(selectname).innerHTML = xhr.responseText;
                }
            };
            xhr.send();
        }
    </script>
</head>
<body>
    <div class="container mt-5">
        <h2 class="text-center mb-4">Sign Up</h2>
        <form action="Signup" method="POST" class="p-4 border rounded bg-light">
            <!-- User Information -->
            <div class="form-group">
                <label for="first_name">First Name:</label>
                <input type="text" id="first_name" name="first_name" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="last_name">Last Name:</label>
                <input type="text" id="last_name" name="last_name" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" class="form-control" required>
            </div>
             <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" class="form-control" required>
            </div>
            

            <div class="form-group">
                <label for="gender">Gender:</label>
                <select id="gender" name="gender" class="form-control" required>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                </select>
            </div>
            <div class="form-group">
    <label for="role">Role:</label>
    <select id="role" name="role" class="form-control" required>
        <option value="">Select Role</option>
        <option value="STUDENT">Student</option>
        <option value="MANAGER">Manager</option>
        <option value="TEACHER">Teacher</option>
        <option value="DEAN">Dean</option>
        <option value="HOD">Head of Department</option>
        <option value="LIBRARIAN">Librarian</option>
    </select>
</div>

            <div class="form-group">
                <label for="phone_number">Phone Number:</label>
                <input type="text" id="phone_number" name="phone_number" class="form-control" required>
            </div>

            <!-- Location Selection -->
            <div class="form-group">
                <label for="province">Province:</label>
                <select id="province" name="province" class="form-control" onchange="loadOptions('district', this.value)" required>
                    <option value="">Select Province</option>
                    <%
                        LocationDao daopro = new LocationDao();
                        List<Location> provinces = daopro.getProvinces();
                        for (Location province : provinces) {
                            %>
                                <option value="<%= province.getLocationId() %>"><%= province.getLocationName() %></option>
                            <%
                                } 
                        %>
                        
                        
                </select>
            </div>

            <div class="form-group">
                <label for="district">District:</label>
                <select id="district" name="district" class="form-control" onchange="loadOptions('sector', this.value)" required>
                    <option value="">Select District</option>
                </select>
            </div>

            <div class="form-group">
                <label for="sector">Sector:</label>
                <select id="sector" name="sector" class="form-control" onchange="loadOptions('cell', this.value)" required>
                    
                    <option value="">Select Sector</option>
                </select>
            </div>

            <div class="form-group">
                <label for="cell">Cell:</label>
                <select id="cell" name="cell" class="form-control" onchange="loadOptions('village', this.value)" required>
                    <option value="">Select Cell</option>
                </select>
            </div>

            <div class="form-group">
                <label for="village">Village:</label>
                <select id="village" name="village" class="form-control" required>
                    <option value="">Select Village</option>
                </select>
            </div>

            <!-- Submit Button -->
            <button type="submit" class="btn btn-primary btn-block">Sign Up</button>
        </form>
    </div>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
