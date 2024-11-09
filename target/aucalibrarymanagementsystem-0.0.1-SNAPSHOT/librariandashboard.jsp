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
</style>
</head>
<body>
    <div id="sidebar">
        <h4 class="text-center">Librarian Menu</h4>
        <a href="bookManagement.jsp" target="contentFrame">Book Management</a>
        <a href="memberApproval.jsp" target="contentFrame">Member Approval</a>
        <a href="borrowerManagement.jsp" target="contentFrame">Borrower Management</a>
        <a href="shelfManagement.jsp" target="contentFrame">Shelf Management</a>
    </div>

    <div id="main-content">
        <iframe name="contentFrame" width="100%" height="800px" frameborder="0">
            <!-- Load sections here -->
        </iframe>
    </div>
</body>
</html>
