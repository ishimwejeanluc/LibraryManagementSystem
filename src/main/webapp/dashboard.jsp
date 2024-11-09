<%@ page import="java.util.*, Dao.MembershipTypeDao,Dao.BookDao,modal.Book, modal.MembershipType" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard - Library System</title>
    <link rel="stylesheet" href="styles.css">
    <!-- Bootstrap CSS for quick styling -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .dashboard-container {
            display: flex;
            flex-direction: row;
        }

        .sidebar {
            width: 250px;
            border-right: 1px solid #ddd;
            padding: 20px;
        }

        .content {
            flex-grow: 1;
            padding: 20px;
        }

        .nav-link {
            cursor: pointer;
        }
    </style>
</head>

<body>
    <div class="container">
        <h1> Dashboard</h1>
        <div class="dashboard-container">
            <!-- Sidebar for navigation -->
            <div class="sidebar bg-light">
                <h4>Dashboard</h4>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link active" onclick="showSection('membership')">Membership Registration</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" onclick="showSection('borrowBook')">Borrow Book</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" onclick="showSection('checkStatus')">Check Membership Status</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" onclick="showSection('fines')">Fines</a>
                    </li>
                </ul>
                <ul class="nav flex-column mt-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="logout" onclick="logoutUser()">Logout</a>
                    </li>
                </ul>
            </div>

            <!-- Content area for each section -->
            <div class="content">
                <!-- Membership Registration Section -->
                <div id="membership" class="section">
                    <% 
                        String successMessage = (String) session.getAttribute("successMessage");
                        String errorMessage = (String) session.getAttribute("errorMessage");
                        if (successMessage != null) {
                    %>
                    <div class="alert alert-success">
                        <h6><%= successMessage %></h6>
                    </div>
                    <% 
                        session.removeAttribute("successMessage");
                        }
                        if (errorMessage != null) {
                    %>
                    <div class="alert alert-danger">
                        <h4><%= errorMessage %></h4>
                    </div>
                    <% 
                        session.removeAttribute("errorMessage");
                        }
                    %>
                    <h3>Membership Registration</h3>
                    <p>If you are not a member, please register to access book borrowing services.</p>
                    <form id="membershipForm" action="register" method="post">
                        <div class="form-group">
                            <label for="membershipType">Select Membership Type</label>
                            <select class="form-control" id="membershipType" name="membershipType" required>
                                <option value="" disabled selected>Select a membership type</option>
                                <% 
                                    // Retrieve room list
                                    MembershipTypeDao typeDao = new MembershipTypeDao();
                                    List<MembershipType> types = typeDao.getAllMemberships();
                                %>
                                <% for (MembershipType type : types) { %>
                                    <option value="<%= type.getMembershipTypeId() %>"><%= type.getMembershipName() %></option>
                                <% } %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="registrationDate">Registration Date</label>
                            <input type="date" class="form-control" id="registrationDate" name = "date" required>
                        </div>

                        <button type="submit" class="btn btn-primary">Register for Membership</button>
                    </form>
                </div>

                <!-- Borrow Book Section -->
                <div id="borrowBook" class="section" style="display:none">
                    <% 
                        String borrowSuccessMessage = (String) session.getAttribute("borrowSuccessmessage");
                        String borrowErrorMessage = (String) session.getAttribute("borrowErrorMessage");
                        if (borrowSuccessMessage != null) {
                    %>
                    <div class="alert alert-success">
                        <h6><%= borrowSuccessMessage %></h6>
                    </div>
                    <% 
                        }
                        if (borrowErrorMessage != null) {
                    %>
                    <div class="alert alert-danger">
                        <h6><%= borrowErrorMessage %></h6>
                    </div>
                    <% 
                        }
                    %>
                    <h3>Borrow Book</h3>
                    <form id="borrowBookForm" action = "borrow" method="post">
                        <div class="form-group">
                            <label for="bookSelect">Available Book to Borrow</label>
                            <select id="bookSelect" class="form-control" name="book">
                                <option value="" disabled selected>Select a book</option>
                                <% 
                                    // Retrieve available books
                                    BookDao bookdao = new BookDao();
                                    List<Book> books = bookdao.getAvailableBooks();
                                %>
                                <% for (Book book : books) { %>
                                    <option value="<%= book.getBookId() %>"><%=book.getTitle() %></option>
                                <%}%>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="pickupDate">Pick-Up Date</label>
                            <input type="date" id="pickupDate" class="form-control" name="pickupDate" required>
                        </div>

                        <div class="form-group">
                            <label for="returnDate">Return Date</label>
                            <input type="date" id="returnDate" class="form-control" name="returnDate" required>
                        </div>

                        <button type="submit" class="btn btn-primary">Borrow Book</button>
                    </form>
                </div>

                <!-- Check Membership Status Section -->
                <div id="checkStatus" class="section" style="display:none;">
                 <% 
                        String statusmessage = (String) session.getAttribute("statussuccessmessage");
                        String statuserrormessage = (String) session.getAttribute("statuserrormessage");
                        if (statusmessage != null) {
                    %>
                    <div class="alert alert-success">
                        <h6><%= statusmessage %></h6>
                    </div>
                    <% 
                        }
                        if (statuserrormessage != null) {
                    %>
                    <div class="alert alert-danger">
                        <h6><%= statuserrormessage %></h6>
                    </div>
                    <% 
                        }
                    %>
                    <h3>Check Membership Status</h3>
                    <p>Your membership status will be retrieved automatically.</p>
                    <form id="statusForm" action="checkMembershipStatus" method="post">
                        <input type="hidden" name="action" value="checkStatus">
                        <button type="submit" class="btn btn-info">Check Status</button>
                    </form>
                </div>

              

                <!-- Fines Section -->
                <div id="fines" class="section" style="display:none;">
                <% 
			String fineSuccessMessage = (String) session.getAttribute("finesuccessmessage");
			String fineErrorMessage = (String) session.getAttribute("fineerrormessage");
			%>
			
			<% if (fineSuccessMessage != null) { %>
			    <div class="alert alert-success">
			        <%= fineSuccessMessage %>
			    </div>
			    <% session.removeAttribute("finesuccessmessage"); %>
			<% } %>
			
			<% if (fineErrorMessage != null) { %>
			    <div class="alert alert-danger">
			        <%= fineErrorMessage %>
			    </div>
			    <% session.removeAttribute("fineerrormessage"); %>
			<% } %>
                
                    <h3>Fines</h3>
                    <form id="statusForm" action="fine" method="post">
                        <input type="hidden" name="action" value="fine">
                        <button type="submit" class="btn btn-info">Check Status</button>
                    </form>
                   </div>
               </div>
          </div>
        </div>

    <!-- JavaScript for switching sections -->
    <script>
         

        // JavaScript for managing selected books
        const selectedBooks = [];

        function showSection(sectionId) {
            document.querySelectorAll('.section').forEach(function (section) {
                section.style.display = 'none';
            });
            document.getElementById(sectionId).style.display = 'block';
        }

       
        window.onload = function() {
            const urlParams = new URLSearchParams(window.location.search);
            const section = urlParams.get('section');
            if (section) {
                showSection(section);
            } else {
                // If no section parameter, show the default section (e.g., membership)
                showSection('membership');
            }
        };
    </script>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
