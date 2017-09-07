<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<style>
			table {
			    font-family: arial, sans-serif;
			    border-collapse: collapse;
			    width: 25%;
			}
			
			td, th {
			    border: 1px solid #dddddd;
			    text-align: center;
			    padding: 8px;
			}
			
			tr:nth-child(even) {
			    background-color: #dddddd;
			}
		</style>
		<title>HomePage</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		
		<script>
			$.ajax({
				url : "Home",
			    dataType : 'html',
			    error : function() {
			    	alert("Error Occured");
			    },
			    success : function(data) {
			   		var json_obj = $.parseJSON(data);
			    	var availableTags=[];
			    	for (i=0; i < json_obj.length; i++){
						var user = json_obj[i];
			    		availableTags.push({
			    		  	"label" : user.uid+"    "+user.name+"    "+user.email,
			    			"value" : user.name,
			    			"id"	: user.uid
			    		});
			    	}
			    	  
			    	$("#userids").autocomplete({
						source: availableTags,
			    	    select: function(event, ui){
			    	    	getUserPosts(ui.item.id, ui.item.value, 0);
			    	    }
					});
				},
				error: function(){
					alert("Error in ajax call for getting search parameters");
				}
			});		
		</script>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
		
		<script>
			function getUserPosts(id, name, j){
				$.ajax({
					url : "Home",
				    dataType : 'html',
					type: "POST",
					data: {
						uid: id,
						userPost: name
					},
				    error : function() {
				    	alert("Error Occured");
				    },
				    success : function(data) {
				    	var json_new = $.parseJSON(data);
				   		if(j == 0) {var output = "<h1>See posts from " + name + ":</h1>";}
				   		else {var output = "";}
						for(i=j; i<json_new.length && i<j+10; i++){
							var post = json_new[i];
							output += "<i>"+post.name+"</i>"+":"+"<div style=\"font-family:courier\";>"+
										"&nbsp;&nbsp;&nbsp;"+post.text+"<br>"+"</div>"+
										"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+post.timestamp+"<br>"
							output += "<div id=\"post_"+post.postid+"\">";
							output += "<button id=\"getComments_"+post.postid+"\" type=\"button\" onclick=\"getComments("+post.postid+")\">View comments</button><br>";
							output += "</div>";
						}
						j += 10;
						if(json_new.length-j > 0) {
							output += "<div id=\"morePosts_"+j+"\">&nbsp;&nbsp;&nbsp;"
									+"<a id=\"getPosts_"+j+"\" href=\"#\" onclick=\" getUserPosts('"+id+"','"+name+"',"+j+")"
									+"\">More...</a></div>";
						}				
						console.log(output);
						if(json_new.length == 0){$("#content").html(output+"<br> No posts from "+name+" yet");}
						else{
							if(j <= 10) {$("#content").html(output);}				
							else{
								j -= 10;
								$("#morePosts_"+j).html(output);
							}
						}
					}
				});		
			}
		</script>
		
		<%-- FUNCTION FOR GETTING FOLLOWERS POSTS --%>
		<script>
		function getFollowerPosts(j){
			$.ajax({
				url : "Home",
			    dataType : 'html',
				type: "POST",
				data: {
					followerPosts: "followerPosts"
				},
			    error : function() {
			    	alert("Error Occured");
			    },
			    success : function(data) {
			   		var json_obj = $.parseJSON(data);
			   		if(j == 0) {var output = "<h1>See posts from the people you follow:</h1>";}
			   		else {var output = "";}
					for(i=j; i<json_obj.length && i<j+10; i++){
						var post = json_obj[i];
						output += "<i>"+post.name+"</i>"+":"+"<div style=\"font-family:courier\";>"+
									"&nbsp;&nbsp;&nbsp;"+post.text+"<br>"+"</div>"+
									"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+post.timestamp+"<br>"
						output += "<div id=\"post_"+post.postid+"\">";
						output += "<button id=\"getComments_"+post.postid+"\" type=\"button\" onclick=\"getComments("+post.postid+")\">View comments</button><br>";
						output += "</div>";
					}
					j += 10;
					if(json_obj.length-j > 0) {
						output += "<div id=\"morePosts_"+j+"\">&nbsp;&nbsp;&nbsp;"
								+"<a id=\"getPosts_"+j+"\" href=\"#\" onclick=\" getFollowerPosts("+j+")"
								+"\">More...</a></div>"
					}				
					console.log(output);
					if(json_obj.length == 0){$("#content").html(output+"<br> No posts from followers yet");}
					else{
						if(j <= 10) {$("#content").html(output);}				
						else{
							j -= 10;
							$("#morePosts_"+j).html(output);
						}
					}
				}
			});		
		}
		</script>
		
		<%-- SCRIPT FOR AUTOLOAD OF FOLLOWERS POSTS --%>
		<script>
			getFollowerPosts(0);
		</script>
		
		<%-- SCRIPT FOR LOAD OF FOLLOWERS POSTS ON CLICKING ON HOME --%>
		<script>
			$(document).ready(function(){
				$("#home").click(function(){
					getFollowerPosts(0);
				});
			});
		</script>
		
		<%-- SCRIPT FOR DELETING ROW ON CLICKING ON X --%>
		<script>
			function deleteRow(r, uid) {
				$(document).ready(function(){
				    var i = r.parentNode.parentNode.rowIndex;
				    $.ajax({
						url : "Home",
					    dataType : 'html',
						type: "POST",
						data: {
							deleteFollower: "deleteFollower",
							uid2: uid
						},
					    error : function() {
					    	alert("Error Occured");
					    },
					    success : function(data) {
						    document.getElementById("followerTable").deleteRow(i);
						}
					});
				});
			}
		</script>

		<%-- SCRIPT FOR LOADING FOLLOWER TABLE ON CLICKING ON FOLLOWER LINK --%>
		<script>
			function getFollowers(){
				$.ajax({
					url: "Home",
					dataType : 'html',
					type: "POST",
					data: {
						getFollowers: "getFollowers"
					},
					success: function(result){
						var json_obj = $.parseJSON(result);
							var output= "<h1>Look who you follow. Click on 'X' to unfollow anyone</h1><table id=\"followerTable\">";
							for(i=0; i<json_obj.length; i++){
								var follower = json_obj[i];
								uid = follower.uid2;
								output +="<tr>"
									    +"<th>"+uid+"</th>"
									    +"<th>"+"<button type = \"button\" id = \"delete_"+uid+"\""
									    +" onclick=\"if (confirm('Do you really want to unfollow "+uid+"?')){deleteRow(this, '"+uid+"')}\">X</button></th>"
									  	+"</tr>";
				    	  	}
							output += "</table>";
							if(json_obj.length == 0) {$("#content").html("You don't follow anyone!!");}
							else {$("#content").html(output);}
					},
					error: function(){
						alert("Error in ajax call for getting comments");
					}
				});
			}
		</script>
		
		<%-- SCRIPT FOR GETTING LESS THAN OR EQUAL TO THREE COMMENTS --%>
		<script>
			function getComments(postid){
				$.ajax({
					url: "Home",
					dataType : 'html',
					type: "POST",
					data: {
						postid: postid,
						getComments: "getComments"
					},
					success: function(result){
						var json_obj = $.parseJSON(result);
						var output= "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Comments:";
						for(i=0; i<json_obj.length && i<3; i++){
							var comment = json_obj[i];
							output += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+comment.text+"<br>"
									+"<div style=\"font-size:95%;\">"
									+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By: <i>"
			    			  		+comment.name+"</i>&nbsp &nbsp Posted on: "+comment.timestamp
					    			+"</div>"; 
			    	  	}
						if(json_obj.length > 3) {output += "<div id=\"moreComments_"+postid+"\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id=\"getMoreComments\" href=\"#\" onclick=\"getMoreComments("+postid+")\">More...</a><div>";}
						if(json_obj.length == 0){
							$("#post_"+postid).html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No Comments on this post yet!");
						}
						else {$("#post_"+postid).html(output);}
					},
					error: function(){
						alert("Error in ajax call for getting comments");
					}
				});
			}
		</script>
		
		<%-- SCRIPT FOR GETTING MORE THAN THREE COMMENTS --%>
		<script>
			function getMoreComments(postid){
				$.ajax({
					url: "Home",
					dataType : 'html',
					type: "POST",
					data: {
						postid: postid,
						getComments: "getComments"
					},
					success: function(result){
						var json_obj = $.parseJSON(result);
							var output= "";
							for(i=3; i<json_obj.length; i++){
								var comment = json_obj[i];
								output += "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+comment.text+"<br>"
										+"<div style=\"font-size:95%;\">"
										+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By: <i>"
				    			  		+comment.name+"</i>&nbsp &nbsp Posted on: "+comment.timestamp
						    			+"</div>"; 
				    	  	}
							if(json_obj.length-3 == 0){
								$("#moreComments_"+postid).html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No more Comments!");
							}
							else {$("#moreComments_"+postid).html(output);}
					},
					error: function(){
						alert("Error in ajax call for getting comments");
					}
				});
			}
		</script>
		
		<%-- SCRIPT FOR LOAD OF CREATE POST --%>
		<script>
			$(document).ready(function(){
				$("#addPost").click(function(){
					$.post(
						"Home", 
						{createPost: "writePost"}, 
						function(data, status){
							$("#content").html(data);
					}); 
				});
			});
		</script>
		
		<%-- SCRIPT FOR SUCCESS MESSAGE ON CREATING POST --%>
		<script>
			function Post(){
				$(document).ready(function(){
					$.post(
						"Home", 
						{Post: "updateDBPost", text: $("#post").val()}, 
						function(data, status){
							$("#content").html(data);
						}
					);
				});
			}
		</script>
	</head>
		
	<body>
		Welcome to your Homepage
		<label for="userids"> <br> Enter Search Query:</label><br>
		<input type = "text" id = "userids" name = "user"/>
		
		<br> <br> 
		<button type = "button" id = "home">Home</button> 
		<br> <br> 
		
		<a id="addPost" href="#">Add Post</a>
		&nbsp; &nbsp; &nbsp;
		<a id="follows" href="#" onclick="getFollowers()">Followers<br><br></a>
		
		<div id = "content"></div>
		<br> <br>
		<form action="Home" method="get">
			<input type = "submit" value = "Logout" name = "Logout">
		</form>
	</body>
</html>