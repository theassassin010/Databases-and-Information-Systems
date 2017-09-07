Created a password table in the database, which has two attributes, ID, and password, both varchar(20).
Filled sample user ID/password in the table, with ID matching data from the student and instructor tables in the sample University data you already have.
Created an index.html page and a Login link; the login link  simply refers to the Login servlet below, without passing any form parameters.
Created a Login servlet with a doPost method that allows a user to login with a login/password. 
If the parameters ID and password are not set it displays a form to fill in the parameters and invoke the same servlet on submit.
If they are set it checks if the ID/password match. 
On failed authentication, it displays an error message and displays the form as above..
On successful authentication of the password, it sets a session variable to store the ID, and displays a home page by redirecting to a Home servlet:
Used response.sendRedirect("Home")   to do this.
Created a Home servlet that does the following:
a link to a servlet that displays the course_id, title, year, semester and sec_id of courses taken by that ID, and 
a link to a servlet that shows course_id, title and credits of all courses.
a logout button
Each of the above links are implemented by a servlet
The first two servlets get the ID from the session, and display all relevant records as a HTML table
The Logout servlet calls session.invalidate() to logout the user, and redirects to the Login servlet.
All servlets other than the Login servlet (and including the Home servlet) check for authentication via the session variable. 
