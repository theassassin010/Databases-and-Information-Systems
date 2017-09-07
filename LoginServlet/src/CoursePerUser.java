import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.sql.*;

/**
 * Servlet implementation class CoursePerUser
 */
@WebServlet(description = "Displays the registered courses for one user", urlPatterns = { "/CoursePerUser" })
public class CoursePerUser extends HttpServlet {

	public static String host = Login.host;
	public static String user = Login.user;
	public static String password = Login.password;
	public static String port = Login.port;

	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CoursePerUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
	    out.println("<html>");
	    out.println("<head><title>Courses Taken</title>");
	    
	    Connection conn = null;
	    Statement stmt = null;
	    try {
			conn = DriverManager.getConnection(
			"jdbc:postgresql://"+host+":"+port+"/postgres", user, password);
			stmt = conn.createStatement();
			
			
			HttpSession session = request.getSession(false);
			try{
				String id = (String) session.getAttribute("uid");
				if(id != null){
				    out.println("<style>table, th, td {border: 1px solid black;}</style></head>");
				    out.println("<body>");
				    out.println("<h1>Courses Taken</h1>");
					PreparedStatement ps = conn.prepareStatement("select * from takes natural join course where id = ?;");  
					ps.setString(1, id);
		
					ResultSet rs = ps.executeQuery();
					out.print("<table>");
					out.print("<tr><th>Course_ID</th><th>Title</th><th>Year</th><th>Semester</th><th>Sec_ID</th></tr>");
					while (rs.next()) {
			          String course_id = rs.getString("course_id");
			          out.print("<tr><th> " + course_id + "</th>");
			          String title = rs.getString("title");
			          out.print("<th>" + title + "</th>");
			          String year = rs.getString("year");
			          out.print("<th> " + year + "</th>");
			          String semester = rs.getString("semester");
			          out.print("<th> " + semester + "</th>");
			          String sec_id = rs.getString("sec_id");
			          out.print("<th> " + sec_id + "</th></tr>");
					}
					out.print("</table>");	
					out.println("<form action=\"Logout\" method=\"post\"><input type = \"submit\" value = \"logout\"></form>");				
		        }
				else {
		        	out.print("Please login first");  
		            request.getRequestDispatcher("index.html").include(request, response);

		        }
			}
			catch (NullPointerException e) {
				out.print("Please login first");  
	            request.getRequestDispatcher("index.html").include(request, response);
	    	}
				
			out.println("</body>");
		      out.println("</html>");
			
			
	    } 
	    catch (SQLException e) {
	    		out.println(e);
	    	} 
	    finally {
	        try {
	          if (stmt != null) {
	            stmt.close();
	          }
	          if (conn != null) {
	            conn.close();
	          }
	        } 
	        catch (SQLException ex) {
	    		out.println(ex);
	        }
	      }
	      out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
	}

}
