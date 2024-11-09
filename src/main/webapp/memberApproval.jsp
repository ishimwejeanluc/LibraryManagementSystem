<%@ page import="java.util.List" %>
<%@ page import="modal.Membership" %>
<%@ page import="Dao.MembershipDao" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Member Approval</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h3 class="text-center">Member Approval</h3>
        <div id="memberApproval" class="section mt-4">
        
         <% String message = (String) request.getAttribute("message"); %>
            <c:if test="${not empty message}">
            
                <div class="alert alert-success">
                    <h6>${message}</h6>
                </div>
            </c:if>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Member Name</th>
                        <th>Membership Type</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="memberTable">
                    <%  
                        MembershipDao dao = new MembershipDao();
                        List<Membership> pendingMemberships = dao.getPendingMemberships();
                        if (pendingMemberships != null && !pendingMemberships.isEmpty()) {
                            for (Membership membership : pendingMemberships) {
                    %>
                    <tr id="memberRow<%= membership.getMembershipId() %>">
                        <td><%= membership.getUser().getUsername() %></td>
                        <td><%= membership.getMembershipType().getMembershipName() %></td>
                        <td><%= membership.getMembershipStatus() %></td>
                        <td>
                            <button class="btn btn-success" onclick="updateMembershipStatus(<%= membership.getMembershipId() %>, 'approve')">Approve</button>
                            <button class="btn btn-danger" onclick="updateMembershipStatus(<%= membership.getMembershipId() %>, 'reject')">Reject</button>
                        </td>
                    </tr>
                    <%  
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="4" class="text-center">No pending memberships</td>
                    </tr>
                    <%  
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>

    <script>
    function updateMembershipStatus(membershipId, action) {
        // Prepare the data to send to the servlet
        const params = new URLSearchParams({ 
            membershipId: membershipId, 
            action: action 
        });

        fetch('memberapproval', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params.toString()
        })
        .then(response => response.text())  // Use text() to get the message
        .then(message => {
            // Update the message on the page
            const messageContainer = document.getElementById("memberApproval");
            messageContainer.innerHTML = `<div class="alert alert-success"><h6>${message}</h6></div>`;
            
            // Optionally, reload the page to reflect the changes
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred. Please check the console for details.');
        });
    }
    </script>
</body>
</html>
