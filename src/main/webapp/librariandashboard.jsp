<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Librarian Dashboard</title>
<link rel="stylesheet" href="styles.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<style>
    /* Sidebar and general styling here */
    #sidebar {
        width: 200px;
        float: left;
        /* Add any additional styling needed for your sidebar */
    }
    #main-content {
        margin-left: 210px; /* Adjust to match sidebar width */
    }
    .logout-button {
        position: absolute;
        bottom: 10px; /* Adjust as needed */
        left: 10px; /* Adjust as needed */
        width: 180px; /* Adjust as needed */
        text-align: center;
    }
</style>
</head>
<body>
    <div id="sidebar">
        <h4 class="text-center">Library Dashboard</h4>
        <a href="bookManagement.jsp" target="contentFrame">Book Management</a>
        <a href="memberApproval.jsp" target="contentFrame">Member Approval</a>
        <a href="borrowerManagement.jsp" target="contentFrame">Borrower Management</a>
        <a href="shelfManagement.jsp" target="contentFrame">Shelf Management</a>
        <a href="logout" class="btn btn-danger logout-button">Logout</a> <!-- Logout button -->
    </div>

    <div id="main-content">
        <iframe name="contentFrame" src="bookManagement.jsp" width="100%" height="800px" frameborder="0">
           
        </iframe>
    </div>
</body>
</html>