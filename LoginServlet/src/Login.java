import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.sql.*;

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
				PreparedStatement ps = conn.prepareStatement("select * from password where id = ? and pswd = ?");  
				ps.setString(1, uid);  
				ps.setString(2, pswd);  
				      
				ResultSet rs = ps.executeQuery();  
				status = rs.next();
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		    	HttpSession session = request.getSession(true);
		    	session.setAttribute("uid", uid);
		    	response.sendRedirect("Home");
		    }  
		    else{
		        out.print("Sorry username or password incorrect");  
		        RequestDispatcher rd=request.getRequestDispatcher("index.html");  
		        rd.include(request,response);  
		    }
		}
		catch(Exception e) {System.out.println(e);}
	}

}
