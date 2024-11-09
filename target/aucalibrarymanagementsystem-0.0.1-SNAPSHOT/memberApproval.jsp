<!DOCTYPE html>
<html>
<head>
    <title>Member Approval</title>
    
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    
</head>
<body>
    <h3>Member Approval</h3>
      <div id="memberApproval" class="section">
            
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
