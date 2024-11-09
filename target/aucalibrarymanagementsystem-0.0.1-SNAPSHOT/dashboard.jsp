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
        <h1>Student Dashboard</h1>
        <div class="dashboard-container">
            <!-- Sidebar for navigation -->
            <div class="sidebar bg-light">
                <h4>Dashboard</h4>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link active" onclick="showSection('membership')">Membership Registration</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" onclick="showSection('availableBooks')">Available Books</a>
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
        <h4><%= successMessage %></h4>
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

                <!-- Available Books Section -->
                <div id="availableBooks" class="section" style="display:none;">
                    <h3>Available Books</h3>
                    <ul>
                        <!-- List of available books will be populated here -->
                    </ul>
                </div>
     <div id="borrowBook" class="section" style="display:none;">
       <% 
    String borrowSuccessMessage = (String) session.getAttribute("borrowSuccessMessage");
    String borrowErrorMessage = (String) session.getAttribute("borrowErrorMessage");
    if (borrowSuccessMessage != null) {
%>
    <div class="alert alert-success">
        <h4><%= borrowSuccessMessage %></h4>
    </div>
<%
        session.removeAttribute("borrowSuccessMessage");
    }
    if (borrowErrorMessage != null) {
%>
    <div class="alert alert-danger">
        <h4><%= borrowErrorMessage %></h4>
    </div>
<%
        session.removeAttribute("borrowErrorMessage");
    }
%>
       
    <h3>Borrow Book</h3>
    <form id="borrowBookForm" action = "borrow" method="post">
        <div class="form-group">
            <label for="bookSelect">Select Book to Borrow</label>
            <select id="bookSelect" class="form-control" name="book">
                <option value="" disabled selected>Select a book</option>
                <% 
                // Retrieve available books
                BookDao bookdao = new BookDao();
                List<Book> books = bookdao.getAvailableBooks();
                %>
                <% for (Book book : books) { %>
                    <option value="<%= book.getBookId() %>"><%= book.getTitle() %></option>
                <% } %>
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
                
                   
                    

  <div id="checkStatus" class="section" style="display:none;">
    <h3>Check Membership Status</h3>
    <p>Your membership status will be retrieved automatically.</p>
    <form id="statusForm" action="checkMembershipStatus" method="post">
    <input type="hidden" name="action" value="checkStatus">
    <button type="submit" class="btn btn-info">Check Status</button>
</form>
    
</div>

<div class="modal fade" id="membershipModal" tabindex="-1" role="dialog" aria-labelledby="membershipModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="membershipModalLabel">Membership Details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="statusResult">
                <!-- Check if statusMessage is present and display it -->
                <c:if test="${not empty statusMessage}">
                    <p>${statusMessage}</p>
                </c:if>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
                <!-- Fines Section -->
                <div id="fines" class="section" style="display:none;">
                    <h3>Fines</h3>
                    <form id="statusForm" action="fine" method="post">
    <input type="hidden" name="action" value="fine">
    <button type="submit" class="btn btn-info">Check Status</button>
</form>
 <!-- Fine details display -->
    <div id="fineDetails">
        <c:if test="${not empty fineDetails}">
            ${fineDetails}
        </c:if>
    </div>
</div>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript for switching sections -->
    <script>
        

        // JavaScript for calculating expiration date
        document.getElementById('registrationDate').addEventListener('change', function () {
            const registrationDate = new Date(this.value);
            const expirationDate = new Date(registrationDate);
            expirationDate.setMonth(expirationDate.getMonth() + 3);
            const formattedDate = expirationDate.toISOString().split('T')[0];
            document.getElementById('expirationDate').value = formattedDate;
        });

        // JavaScript for managing selected books
        const selectedBooks = [];

        function addBook() {
            const bookSelect = document.getElementById('bookSelect');
            const selectedOption = bookSelect.options[bookSelect.selectedIndex];
            if (selectedOption.value) {
                selectedBooks.push({
                    title: selectedOption.text,
                    author: 'Author Name' // Replace with author name if available in options
                });
                displaySelectedBooks();
            }
        }

        function removeBook(index) {
            selectedBooks.splice(index, 1);
            displaySelectedBooks();
        }

        function displaySelectedBooks() {
            const tableBody = document.getElementById('selectedBooksTable');
            tableBody.innerHTML = '';

            selectedBooks.forEach((book, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${book.title}</td>
                    <td>${book.author}</td>
                    <td><button onclick="removeBook(${index})" class="btn btn-danger btn-sm">Remove</button></td>
                `;
                tableBody.appendChild(row);
            });
        }

        function showSection(sectionId) {
            document.querySelectorAll('.section').forEach(section => section.style.display = 'none');
            document.getElementById(sectionId).style.display = 'block';
        }

        // Check if there's a section parameter in the URL to display the correct section
        window.onload = function() {
            const urlParams = new URLSearchParams(window.location.search);
            const section = urlParams.get('section');
            if (section) {
                showSection(section);
            } else {
                showSection('membership'); // Default section if no parameter is provided
            }
        };
        
        <c:if test="${not empty statusMessage}">
        $(document).ready(function() {
            $('#membershipModal').modal('show');
        });
    </c:if>
        
    </script>

    <!-- Bootstrap JS -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>

</html>
