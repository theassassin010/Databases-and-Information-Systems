import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;  



/**
 * Servlet implementation class Login
 */
@WebServlet(description = "Contains the Login functions", urlPatterns = { "/Login" })
public class Login extends HttpServlet {
	public static String host = "localhost";
	public static String user = "suyash";
	public static String password = "";
	public static String port = "5100";

	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	public static boolean validate(String uid, String pswd){  
			boolean status = false;  

		    try(
				Connection conn = DriverManager.getConnection(
						"jdbc:postgresql://"+host+":"+port+"/postgres", user, password);
			){
				PreparedStatement ps = conn.prepareStatement("select * from password where uid = ? and password = ?");  
				ps.setString(1, uid);  
				ps.setString(2, pswd);  
				      
				ResultSet rs = ps.executeQuery();  
				status = rs.next();
				conn.close();
			}
		    catch(Exception e){
		    	System.out.println(e);
		    }
		    return status;
	}
	
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }
    

    protected void printPostWithComment(ResultSet rs, PrintWriter out, String str){
		try{
		    if(rs.next()){
				Connection conn = DriverManager.getConnection(
						"jdbc:postgresql://localhost:"+port+"/postgres", user, password);
    			do{
	    			String postid = rs.getString("postid");
	    			String text = rs.getString("text");
	    			String ts = rs.getString("timestamp");
	    			String name = rs.getString("name");
	    			out.println("<i>"+name+"</i>"+":");
	    			out.println("<div style=\"font-family:courier\";>");
	    			out.println("&nbsp;&nbsp;&nbsp;"+text+"<br>");
	    			out.println("</div>");
	    			out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ts+"<br>");
	    			try {
	    				PreparedStatement ps = conn.prepareStatement("select * from comment natural join public.user where postid = ? order by timestamp;");
	    				ps.setString(1, postid);
	    				ResultSet rsd = ps.executeQuery();
	    				printComment(rsd, out);
	    				out.println("<form action=\"Login\" method=\"get\"> "+
	    								"<input type = \"submit\" name = \"createComment\" "+
	    								"value = \"Comment\"> "+
	    								"<input type=\"hidden\" name=\"postid\" value="+postid+"> "+
	    								"<input type=\"hidden\" name=\"commentType\" value="+str+"> "+
	    							"</form>");
	    			}
	    		    catch(Exception e){out.println(e);}
    			}
    			while(rs.next());
    			conn.close();
			}
    		else {
    			out.println("No Posts yet made by the user");
    		}
	    	out.println("<form action=\"Login\" method=\"get\">"+
							"<input type = \"submit\" value = \"Back to Home\" name = \"Home\">"+
	    				"</form>"+
	    				"<form action=\"Login\" method=\"get\">"+
							"<input type = \"submit\" value = \"Logout\" name = \"Logout\">"+
	    				"</form>");
	    }
	    catch(Exception e){out.println(e);}    	
    }
    
    protected void printComment(ResultSet rs, PrintWriter out){
	    try{
	    	if(rs.next()){
	    		out.println("Comments:");
				do{
					out.println("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+rs.getString("text")+"<br>");
					out.println("<div style=\"font-size:95%;\">");
					out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By: "+"<i>"+rs.getString("name")+"</i>"+",&nbsp"+rs.getString("timestamp"));
					out.println("</div>");
				}
				while(rs.next());
			}
	    	else {
    			out.println("Comments: <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No Comments on this post yet! Be the first one to comment.");
    		}
	    }
	    catch(Exception e){out.println(e);}
    }
    
    protected void Logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html");  
        PrintWriter out=response.getWriter();
    	try{ 
        	HttpSession session=request.getSession();
//	        session.setAttribute("uid", null);
	        session.invalidate(); 
	        out.println("Login Again"); 
	    	response.sendRedirect("index.html");
//	    	request.getRequestDispatcher("index.html").include(request, response);
        }
        catch(Exception e){
	    	response.sendRedirect("index.html");
//        	request.getRequestDispatcher("index.html").include(request, response);
        }
        out.close();    	
    }
    
    protected void seeFollowerPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	try{
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			out.println("</body>");
		    out.println("</html>");
		    out.println("<title>View Follower's Post</title>");
		    Connection conn = null;
		    Statement stmt = null;
		    try {
				conn = DriverManager.getConnection(
						"jdbc:postgresql://localhost:"+port+"/postgres", user, password);
				stmt = conn.createStatement();
				
				HttpSession session = request.getSession(false);
				if(session != null){
					String uid = (String) session.getAttribute("uid");
					if(uid != null){
						PreparedStatement ps = conn.prepareStatement("select * from public.user natural join post "+
																		"where uid in (	"+
																		"select uid2 from public.user, follows "+
																		"where uid = ? and uid = uid1 "+
																	") order by timestamp desc;");
						ps.setString(1, uid);
						ResultSet rs = ps.executeQuery();
						out.println("<h1> See your followers posts: </h1>");
						printPostWithComment(rs, out, "follower");
					}
				}
				else {
		        	out.print("Please login first");  
		        	response.sendRedirect("index.html");  
		        }
		    } 
		    catch (SQLException e) {out.println(e);} 
		    finally {
		    	try {
		    		if (stmt != null) {stmt.close();}
		    		if (conn != null) {conn.close();}
		        } 
		        catch (SQLException ex) {out.println(ex);}
		    }
		    
		    out.println("</body>");
		    out.println("</html>");
		    out.close();
		    
	    	
    	}
		catch(Exception e) {System.out.println(e);}
    }
    
    protected void viewPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	try{
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			out.println("</body>");
		    out.println("</html>");
		    out.println("<title>View Post</title>");

		    Connection conn = null;
		    Statement stmt = null;
		    try {
				conn = DriverManager.getConnection(
						"jdbc:postgresql://localhost:"+port+"/postgres", user, password);
				stmt = conn.createStatement();
				
				HttpSession session = request.getSession(false);
				if(session != null){
					String uid = (String) session.getAttribute("uid");
					if(uid != null){
						PreparedStatement ps = conn.prepareStatement("select * from public.user natural join post where uid = ? order by timestamp");
						ps.setString(1, uid);
						ResultSet rs = ps.executeQuery();
						out.println("<h1> See your own posts: </h1>");
						printPostWithComment(rs, out, "user");
					}
				}
				else {
		        	out.print("Please login first");  
		        	response.sendRedirect("index.html");  
		        }
		    } 
		    catch (SQLException e) {out.println(e);} 
		    finally {
		    	try {
		    		if (stmt != null) {stmt.close();}
		    		if (conn != null) {conn.close();}
		        } 
		        catch (SQLException ex) {out.println(ex);}
		    }
		    
		    out.println("</body>");
		    out.println("</html>");
		    out.close();
		    
	    	
    	}
		catch(Exception e) {System.out.println(e);}
    }
    
    protected void updateDBPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		out.println("<html>");
	    out.println("<body>");
    	Connection conn = null;
 	    Statement stmt = null;
	    String text = request.getParameter("post");
 	    try {
 			conn = DriverManager.getConnection(
 					"jdbc:postgresql://localhost:"+port+"/postgres", user, password);
 			stmt = conn.createStatement();

 			HttpSession session = request.getSession(false);
 			if(session != null){
 				String uid = (String) session.getAttribute("uid");
 				if(uid != null){
 					PreparedStatement ps = conn.prepareStatement("create sequence if not exists newpostid owned by post.postid;");
 					ps.executeUpdate();
 					java.sql.Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
 					PreparedStatement pst = conn.prepareStatement("insert into post values(nextval('newpostid'), ?, ?, ?);");
 					pst.setString(1, uid);
 					pst.setString(2, text);
 					pst.setTimestamp(3, timestamp);
 					if(!text.isEmpty() && text != null) {pst.executeUpdate();}
 					else {
 						out.println("Post cannot be empty");
 						createPost(request, response);
 					}
 					viewPost(request, response);
 				}
 			}
 			else {
 	        	out.print("Please login first");  
 	        	response.sendRedirect("index.html");  
 	        }
 	    } 
 	    catch (SQLException e) {out.println(e);} 
 	    finally {
 	    	try {
 	    		if (stmt != null) {stmt.close();}
 	    		if (conn != null) {conn.close();}
 	        } 
 	        catch (SQLException ex) {out.println(ex);}
 	    }
 	    out.println("</body>");
 	    out.println("</html>");
    }
 
    protected void updateDBComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		out.println("<html>");
	    out.println("<body>");
    	Connection conn = null;
 	    Statement stmt = null;
 	    String text = request.getParameter("comment");
    	String postid = request.getParameter("postid");
 	    try {
 			conn = DriverManager.getConnection(
 					"jdbc:postgresql://localhost:"+port+"/postgres", user, password);
 			stmt = conn.createStatement();
 			
 			HttpSession session = request.getSession(false);
 			if(session != null){
 				String uid = (String) session.getAttribute("uid");
 				if(uid != null){
 					PreparedStatement ps = conn.prepareStatement("create sequence if not exists newcommentid owned by post.postid;");
 					ps.executeUpdate();
 					java.sql.Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
 					PreparedStatement pst = conn.prepareStatement("insert into comment values(nextval('newcommentid'), ?, ?, ?, ?);");
 					pst.setString(1, postid);
 					pst.setString(2, uid);
 					pst.setTimestamp(3, timestamp);
 					pst.setString(4, text);
 					String commentType = request.getParameter("commentType");
 					if(!text.isEmpty() && text != null) {pst.executeUpdate();}
 					else {
 						out.println("Comment cannot be empty <br>");
 						createComment(request, response, postid, commentType);
 					}
 					if(commentType.equals("follower")) {seeFollowerPosts(request, response);}
 					else if(commentType.equals("user")) {viewPost(request, response);}
 				}
 			}
 			else {
 	        	out.print("Please login first");  
 	        	response.sendRedirect("index.html");  
 	        }
 	    } 
 	    catch (SQLException e) {out.println(e);} 
 	    finally {
 	    	try {
 	    		if (stmt != null) {stmt.close();}
 	    		if (conn != null) {conn.close();}
 	        } 
 	        catch (SQLException ex) {out.println(ex);}
 	    }
 	    out.println("</body>");
 	    out.println("</html>");
    }
    
    protected void createPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		out.println("<html>");
	    out.println("<body>");
	    out.println("<title>Create Post</title>");
	    HttpSession session = request.getSession(false);
		if(session != null){
	    	out.println("<form action=\"Login\" method=\"get\">"+
							"Enter Post: <input type=\"text\" name = \"post\"></br>"+
							"<input type = \"submit\" name = \"updateDBPost\" value = \"Create\">"+
						"</form>");
	    	out.println("<form action=\"Login\" method=\"get\">"+
							"<input type = \"submit\" value = \"Logout\" name = \"Logout\">"+
	    				"</form>");
	    	out.println("<form action=\"Login\" method=\"get\">"+
							"<input type = \"submit\" value = \"Back to Home\" name = \"Home\">"+
	    				"</form>");
    	}
		else {
        	out.print("Please login first");  
        	response.sendRedirect("index.html");  
        }
	    out.println("</body>");
	    out.println("</html>");
	    out.close();   	
	}
        
    protected void createComment(HttpServletRequest request, HttpServletResponse response, String postid, String str) throws ServletException, IOException{
    	response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		out.println("<html>");
	    out.println("<body>");
	    out.println("<title>Create Comment</title>");
	    HttpSession session = request.getSession(false);
		if(session != null){
		    out.println("Create Comment:\n");
			out.println("<form action=\"Login\" method=\"get\">"+
							"Enter Comment: <input type=\"text\" name = \"comment\"></br>"+
							"<input type=\"hidden\" name=\"postid\" value="+postid+"> "+
							"<input type=\"hidden\" name=\"commentType\" value="+str+"> "+
							"<input type = \"submit\" name = \"updateDBComment\" value = \"Create\">"+
						"</form>");
		}
		else {
        	out.print("Please login first");  
        	response.sendRedirect("index.html");  
        }
	    out.println("</body>");
	    out.println("</html>");
	    out.close();   	
    }
    
    protected void Home(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		out.println("<html><body>");
	    out.println("<title>HomePage</title>");
	    HttpSession session = request.getSession(false);
		if(session != null){
	    	out.println("Welcome to your Homepage");
	    	out.println("<form action=\"Login\" method=\"get\">"+
							"To create a new post, Click &nbsp"+
							"<input type = \"submit\" value = \"Here\" name = \"createPost\">"+
						"</form>");
	    	out.println("<form action=\"Login\" method=\"get\">"+
							"To view posts made by you, Click"+
							"<input type = \"submit\" value = \"Here\" name = \"viewPost\">"+
						"</form>");
	    	out.println("<form action=\"Login\" method=\"get\">"+
							"To see your followers posts, Click"+
							"<input type = \"submit\" value = \"Here\" name = \"seeFollowerPosts\">"+
						"</form>");
	    	out.println("<form action=\"Login\" method=\"get\">"+
							"<input type = \"submit\" value = \"Logout\" name = \"Logout\">"+
	    				"</form>");
		}
		else {
        	out.print("Please login first: ");  
            response.sendRedirect("index.html");  
        }
    	out.println("</body></html>");
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		Login login = new Login();
		HttpSession session = request.getSession(false);
		if(session != null){
			try{
				if(request.getParameter("createPost") != null){
					login.createPost(request, response);
				}
				else if(request.getParameter("viewPost") != null){
					login.viewPost(request, response);
				}
				else if(request.getParameter("seeFollowerPosts") != null){
					login.seeFollowerPosts(request, response);
				}
				else if(request.getParameter("updateDBPost") != null){
					login.updateDBPost(request, response);
				}
				else if(request.getParameter("createComment") != null){
					String postid = request.getParameter("postid");
					String ct = request.getParameter("commentType");
					login.createComment(request, response, postid, ct);
				}
				else if(request.getParameter("updateDBComment") != null){
					login.updateDBComment(request, response);
				}
				else if(request.getParameter("Home") != null){
					login.Home(request, response);
				}
				else if(request.getParameter("Logout") != null){
					login.Logout(request, response);
				}
			}
			catch(Exception e) {out.println(e);}
		}
		else {
        	out.print("Please login first");  
        	response.sendRedirect("index.html");  
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try{
			response.setContentType("text/html");
		    PrintWriter out = response.getWriter();
		    String uid = request.getParameter("uid");
		    String pswd = request.getParameter("pswd");
		    
		    if(validate(uid, pswd)){  
		    	HttpSession session = request.getSession();
		    	session.setAttribute("uid", uid);
				out.println("<html><body>");
			    out.println("<title>HomePage</title>");
		    	out.println("Welcome to your Homepage");
		    	out.println("<form action=\"Login\" method=\"get\">"+
								"To create a new post, Click &nbsp"+
								"<input type = \"submit\" value = \"Here\" name = \"createPost\">"+
							"</form>");
		    	out.println("<form action=\"Login\" method=\"get\">"+
								"To view posts made by you, Click"+
								"<input type = \"submit\" value = \"Here\" name = \"viewPost\">"+
							"</form>");
		    	out.println("<form action=\"Login\" method=\"get\">"+
								"To see your followers posts, Click"+
								"<input type = \"submit\" value = \"Here\" name = \"seeFollowerPosts\">"+
							"</form>");
		    	out.println("<form action=\"Login\" method=\"get\">"+
								"<input type = \"submit\" value = \"Logout\" name = \"Logout\">"+
		    				"</form>");
		    	out.println("</body></html>");
		    }  
		    else{
		        out.print("Sorry username or password incorrect");  
		        RequestDispatcher rd = request.getRequestDispatcher("index.html");  
		        rd.include(request,response);  
		    }
		}
		catch(Exception e) {System.out.println(e);}
	}

}
