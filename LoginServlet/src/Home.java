import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Home
 */
@WebServlet(description = "After Logging in", urlPatterns = { "/Home" })
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		try{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();  
	        HttpSession session=request.getSession(false);
	        String uid = (String) session.getAttribute("uid");
	        if(uid != null){  
		        out.print("Hello \""+uid+"\", Welcome to your HomePage </br>");  
		        
		        out.println("<a href=http://localhost:8080/LoginServlet/CoursePerUser>Courses Taken</a> &nbsp; &nbsp; <a href=http://localhost:8080/LoginServlet/AllCourses>All Courses</a></p>");
//		        out.println("<a href=http://localhost:8080/LoginServlet/Logout>Logout</a>") ;
		        out.println("<form action=\"Logout\" method=\"get\"><input type = \"submit\" value = \"logout\"></form>");
			}
	        else {
	        	out.print("Please login first");  
	            request.getRequestDispatcher("index.html").include(request, response);  
//	            response.sendRedirect("index.html");
	        }
		        
	        out.close();
		}
		catch(Exception e) {System.out.println(e);}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
