I have used Javascript/AJAX with jQuery to create a better interface to the previously created social network application(Twitter).

Wrote an updated version of the home page servlet (or JSP) named Home.java, which does the following:

Redirected to the Home servlet on successful login.

Once a user is logged in, they see a home page.  The home page has the following:

---A home button whose function is described below.

---A search users box at the top.   More on what this does below.

---An add post link

---A link to show all the people you follow, as a table.

The lower part of the page contains the list of posts by users that are followed by the currently logged in user, sorted in increasing order of time.  Used a div tag (e.g. <div id="content">  </div>) for this part of the page, and the div has no content at all; instead filled the content by using an AJAX call to servlets, which returns all the posts to be shown in html format; the AJAX call is invoked onLoad of the page and replaces the div content by the result of the AJAX call.

The search box works as follows
The user can type in the ID, name and email of any user.  As the user is typing in any one of these, the system shows autocomplete suggestions, implemented using jquery autocomplete feature. Once a user is chosen in the search box, an AJAX call is made to a servlet which returns all posts of that user.  Once the result is received, the div of lower part of the page is replaced by the result.

The home button replaces the contents of the lower part of the page by posts of all followed users (the default content of the page). The add post link also replaces the lower part of the page by a form that allows the user to add a post.
         
Used the version of the interface which takes an array of objects with labelandvalueproperties:[ { label: "Choice1", value: "value1" }, ... ]

Posts are sent from the servlet in JSON format, and displayed using Javascript.

Along with each post, show up to 3 comments.  If there are more comments, it shows text that says More ..., which when clicked shows the full set of comments.

Only 10 posts are shown at a time.  At the bottom of the page, there is a link that says More ..., which when clicked shows 10 more posts. 
  
The table of followers is displayed using JQuery table. 
Along with each row of the table is an X icon, which can be used to unfollow the user. It will first have a popup that verifies from the user that he/she really wants to unfollow the user, and then performs the unfollow using an AJAX call.

References:

jQuery Autocomplete: 
	https://jqueryui.com/autocomplete/
	And http://api.jqueryui.com/autocomplete/.  

html Tables: http://www.w3schools.com/html/html_tables.asp

DeleteRow Reference:
	http://www.w3schools.com/jsref/tryit.asp?filename=tryjsref_table_deleterow2