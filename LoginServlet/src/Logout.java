

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Logout
 */
@WebServlet(description = "Logout Servlet", urlPatterns = { "/Logout" })
public class Logout extends HttpServlet {
	
	public static String host = Login.host;
	public static String user = Login.user;
	public static String password = Login.password;
	public static String port = Login.port;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
