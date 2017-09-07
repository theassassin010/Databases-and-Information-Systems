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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Home
 */
@WebServlet(description = "New Version of the Twitter Assignment", urlPatterns = { "/Home" })
public class Home extends HttpServlet {
	public static String host = "localhost";
	public static String user = "suyash";
	public static String password = "";
	public static String port = "5100";
	public static String connString = "jdbc:postgresql://"+host+":"+port+"/postgres";
	
	private static final long serialVersionUID = 1L;

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
    
    protected void Logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text/html");  
        PrintWriter out=response.getWriter();
    	try{ 
        	HttpSession session=request.getSession();
	        session.invalidate(); 
	        out.println("Login Again"); 
	    	RequestDispatcher rd = request.getRequestDispatcher("index.html");  
	        rd.include(request,response);  
        }
        catch(Exception e){
	    	RequestDispatcher rd = request.getRequestDispatcher("index.html");  
	        rd.include(request,response);  
        }
        out.close();    	
    }
    
private static JSONArray ResultSetConverter(ResultSet rs) throws SQLException, JSONException {
		
		// TODO Auto-generated method stub
		JSONArray json = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    while(rs.next()) {
	        int numColumns = rsmd.getColumnCount();
	        JSONObject obj = new JSONObject();

	        for (int i=1; i<numColumns+1; i++) {
	          String column_name = rsmd.getColumnName(i);

	          if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
	           obj.put(column_name, rs.getArray(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
	           obj.put(column_name, rs.getBoolean(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
	           obj.put(column_name, rs.getBlob(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
	           obj.put(column_name, rs.getDouble(column_name)); 
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
	           obj.put(column_name, rs.getFloat(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
	           obj.put(column_name, rs.getNString(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
	           obj.put(column_name, rs.getString(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
	           obj.put(column_name, rs.getDate(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
	          obj.put(column_name, rs.getTimestamp(column_name));   
	          }
	          else{
	           obj.put(column_name, rs.getObject(column_name));
	          }
	        }

	        json.put(obj);
	      }
	    return json;
	}
    
    public static JSONArray getfollowerPosts(String uid) throws ServletException, IOException{
    	JSONArray jsonArr = new JSONArray();
		try{
			Connection conn = DriverManager.getConnection(connString, user, password);
			PreparedStatement ps = conn.prepareStatement("select * from public.user natural join post "+
															"where uid in (	"+
															"select uid2 from public.user, follows "+
															"where uid = ? and uid = uid1 "+
														") order by timestamp;");
			ps.setString(1, uid);
			ResultSet rs =  ps.executeQuery();
			jsonArr = ResultSetConverter(rs);
			ps.close();
			conn.close();
		} 
		catch(Exception e){
			System.out.println(e);
		}
		
		return jsonArr;    	
    }
    
    protected String WritePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	String ret = "";
	    HttpSession session = request.getSession(false);
		if(session != null){
	    	ret = ret + (
	    				"<label for=\"post\"> <br> Enter Post:"+"</label><br>"+
						"<input type = \"text\" id = \"post\" name = \"user\"/>"+
						"<button type = \"button\" onclick=\"Post()\" id=\"postButton\" >Create</button>"
	    				);
//	    	ret = ret + ("<form action=\"Login\" method=\"get\">"+
//							"<input type = \"submit\" value = \"Logout\" name = \"Logout\">"+
//	    				"</form>");
//	    	ret = ret + ("<form action=\"Login\" method=\"get\">"+
//							"<input type = \"submit\" value = \"Back to Home\" name = \"Home\">"+
//	    				"</form>");
    	}
		else {
			ret = ret + ("Please login first");  
        	response.sendRedirect("index.html");  
        } 
    	return ret;
    }
    

    protected String updateDBPost(HttpServletRequest request, HttpServletResponse response, String text) throws ServletException, IOException{
    	String ret = "";
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
 					PreparedStatement ps = conn.prepareStatement("create sequence if not exists newpostid owned by post.postid;");
 					ps.executeUpdate();
 					java.sql.Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
 					PreparedStatement pst = conn.prepareStatement("insert into post values(nextval('newpostid'), ?, ?, ?);");
 					pst.setString(1, uid);
 					pst.setString(2, text);
 					pst.setTimestamp(3, timestamp);
 					if(!text.isEmpty() && text != null) {pst.executeUpdate();}
 					else {
 						ret = ret + ("Post cannot be empty");
 						WritePost(request, response);
 					}
 					ret = "Post recorded Successfully!";
 				}
 			}
 			else {
 	        	ret = ret + ("Please login first");  
 	        	response.sendRedirect("index.html");  
 	        }
 	    } 
 	    catch (SQLException e) {System.out.println(e);} 
 	    finally {
 	    	try {
 	    		if (stmt != null) {stmt.close();}
 	    		if (conn != null) {conn.close();}
 	        } 
 	        catch (SQLException ex) {System.out.println(ex);}
 	    }
 	    return ret;
    }
    
    public static JSONArray getPosts(String uid){
    	JSONArray jsonArr = new JSONArray();
		try{
			Connection conn = DriverManager.getConnection(connString, user, password);
			String query = "select * from post natural join public.user where uid = ? order by timestamp;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, uid);
			ResultSet rs = ps.executeQuery();
			jsonArr = ResultSetConverter(rs);
			ps.close();
			conn.close();
		} 
		catch(Exception e){
			System.out.println(e);
		}
		
		return jsonArr;
    }
    
	public static JSONArray getAutoCompleteList(){
		JSONArray jsonArr = new JSONArray();
		try{
			Connection conn = DriverManager.getConnection(connString, user, password);
			String query = "select * from public.user";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs =  ps.executeQuery();
			jsonArr = ResultSetConverter(rs);
			ps.close();
			conn.close();
		} 
		catch(Exception e){
			System.out.println(e);
		}
		
		return jsonArr;
	}
	
	public static JSONArray getComments(String postid){
		JSONArray jsonArr = new JSONArray();
		try{
			Connection conn = DriverManager.getConnection(connString, user, password);
			String query = "select * from comment natural join public.user where postid = ? order by timestamp;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, postid);
			ResultSet rs = ps.executeQuery();
			jsonArr = ResultSetConverter(rs);
			ps.close();
			conn.close();			 
		} 
		catch(Exception e){
			System.out.println(e);
		}
		
		return jsonArr;
	}
	
	public static JSONArray getFollowers(String uid){
		JSONArray jsonArr = new JSONArray();
		try{
			Connection conn = DriverManager.getConnection(connString, user, password);
			String query = "select * from follows where uid1 = ?;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, uid);
			ResultSet rs = ps.executeQuery();
			jsonArr = ResultSetConverter(rs);
			ps.close();
			conn.close();			 
		} 
		catch(Exception e){
			System.out.println(e);
		}
		
		return jsonArr;
	}
	
	public static void deleteFollower(String uid1, String uid2){
		try{
			Connection conn = DriverManager.getConnection(connString, user, password);
			String query = "delete from follows where uid1 = ? and uid2 = ?;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, uid1);
			ps.setString(2, uid2);
			ps.executeUpdate();
			ps.close();
			conn.close();			 
		} 
		catch(Exception e){
			System.out.println(e);
		}
	}
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Home home = new Home();
		if(request.getParameter("Logout") != null){
			home.Logout(request, response);
		}
		else{
			HttpSession session = request.getSession(false);
 			if(session != null){	
				JSONArray jsArr = Home.getAutoCompleteList();
				response.getWriter().write(jsArr.toString());
 			}
 			else{}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Home home = new Home();
		if(request.getParameter("followerPosts") != null){
//			System.out.println("followerposts");
			HttpSession session = request.getSession(false);
 			if(session != null){
				String uid = (String) session.getAttribute("uid");
				JSONArray jsArr = Home.getfollowerPosts(uid);
//				System.out.println(jsArr);
				response.getWriter().write(jsArr.toString());
 			}
 			else{
 				RequestDispatcher rd = request.getRequestDispatcher("index.html");  
		        rd.include(request,response);
 			}
		}
		else if(request.getParameter("userPost") != null){
//			System.out.println("userPosts");
			HttpSession session = request.getSession(false);
 			if(session != null){
				String uid = request.getParameter("uid");
				JSONArray jsArr = Home.getPosts(uid);
//				System.out.println(jsArr);
				response.getWriter().write(jsArr.toString());
 			}
 			else{
 				RequestDispatcher rd = request.getRequestDispatcher("index.html");  
		        rd.include(request,response);
 			}
		}
		else if(request.getParameter("getComments") != null){
//			System.out.println("Getting comments");
			HttpSession session = request.getSession(false);
 			if(session != null){
				String postid = request.getParameter("postid");
				JSONArray jsArr = Home.getComments(postid);
//				System.out.println(jsArr);
				response.getWriter().write(jsArr.toString());
 			}
 			else{
 				RequestDispatcher rd = request.getRequestDispatcher("index.html");  
		        rd.include(request,response);
 			}
		}
		else if(request.getParameter("getFollowers") != null){
//			System.out.println("Getting followers");
			HttpSession session = request.getSession(false);
 			if(session != null){
				String uid = (String) session.getAttribute("uid");
				JSONArray jsArr = Home.getFollowers(uid);
//				System.out.println(jsArr);
				response.getWriter().write(jsArr.toString());
 			}
 			else{
 				RequestDispatcher rd = request.getRequestDispatcher("index.html");  
		        rd.include(request,response);
 			}
		}
		else if(request.getParameter("deleteFollower") != null){
//			System.out.println("Deleting follower");
			HttpSession session = request.getSession(false);
 			if(session != null){
 				String uid1 = (String) session.getAttribute("uid");
				String uid2 = request.getParameter("uid2");
//				System.out.println(uid1 + "\t" + uid2);
				Home.deleteFollower(uid1, uid2);
 			}
 			else{
 				RequestDispatcher rd = request.getRequestDispatcher("index.html");  
		        rd.include(request,response);
 			}
		}
		else if(request.getParameter("createPost") != null){
//			System.out.println("createPosts");
			response.getWriter().write(home.WritePost(request, response));
		}
		else if(request.getParameter("Post") != null){
//			System.out.println("Inside UpdateDBPost");
			String text = request.getParameter("text");
			response.getWriter().write(home.updateDBPost(request, response, text));
		}
		else{
			try{
				response.setContentType("text/html");
			    PrintWriter out = response.getWriter();
			    String uid = request.getParameter("uid");
			    String pswd = request.getParameter("pswd");
				
			    if(validate(uid, pswd)){  
			    	HttpSession session = request.getSession();
			    	session.setAttribute("uid", uid);
			    	request.getRequestDispatcher("scripts.jsp").include(request, response);
			    }  
			    else{
			    	out.println("Invalid Username or Password");
			    	RequestDispatcher rd = request.getRequestDispatcher("index.html");  
			        rd.include(request,response);  
			    }
			}
			catch(Exception e) {System.out.println(e);}
		}
	}

}
