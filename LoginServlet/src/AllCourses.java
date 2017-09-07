

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AllCourses
 */
@WebServlet("/AllCourses")
public class AllCourses extends HttpServlet {
	

	public static String host = Login.host;
	public static String user = Login.user;
	public static String password = Login.password;
	public static String port = Login.port;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllCourses() {
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
	    out.println("<style>table, th, td {border: 1px solid black;}</style></head>");
	    out.println("<body>");
	    out.println("<h1>Courses Taken</h1>");
	    
	    Connection conn = null;
	    Statement stmt = null;
	    try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:"+port+"/postgres", user, password);
			stmt = conn.createStatement();
			
			HttpSession session = request.getSession();
			if(session != null){
				PreparedStatement ps = conn.prepareStatement("select * from course;");
				
				ResultSet rs = ps.executeQuery();
				out.print("<table>");
				out.print("<tr><th>Course_ID</th><th>Title</th><th>Credits</th></tr>");
				while (rs.next()) {
		          String course_id = rs.getString("course_id");
		          out.print("<tr><th> " + course_id + "</th>");
		          String title = rs.getString("title");
		          out.print("<th>" + title + "</th>");
		          String credits = rs.getString("credits");
		          out.print("<th>" + credits + "</th></tr>");
		        }
				out.print("</table>");
			}
			else {
	        	out.print("Please login first");  
	            request.getRequestDispatcher("index.html").include(request, response);  
	        }
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
	      out.println("<form action=\"Logout\" method=\"post\"><input type = \"submit\" value = \"logout\"></form>");
	      out.println("</body>");
	      out.println("</html>");
	      out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
