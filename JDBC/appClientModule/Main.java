//130050010 Suyash A. Bhatkar
//The timings of the different function calls can be reviewed by uncommenting the calls and time statements at the bottom.
//The results (time taken or error message observed) can be viewed after the definition of every function.
import java.sql.*;
import java.io.*;

public class Main {
	public static void ConnectionLeakage(){
		try {
			FileReader fr = new FileReader("student.txt");
			BufferedReader br = new BufferedReader(fr);
			String line;
			String str = "insert into bigstudent values(?, ?, ?, ?)";
			while((line = br.readLine()) != null){
			    try{
					Connection conn = DriverManager.getConnection(
				    		"jdbc:postgresql://localhost:5100/postgres", "suyash", "");
				    //Statement stmt = conn.createStatement();

			        String line2 = line.replaceAll("'", "");
					String delims = "[,]+";
					String[] tokens = line2.split(delims);
//					System.out.println(tokens[0]+" "+tokens[1]+" "+tokens[2]+" "+tokens[3]);
					PreparedStatement pstmt = conn.prepareStatement(str);
					pstmt.setString(1, tokens[0]);
					pstmt.setString(2, tokens[1]);
					pstmt.setString(3, tokens[2]);
					pstmt.setInt(4, Integer.parseInt(tokens[3].trim()));
					pstmt.executeUpdate();
				}
			    catch(SQLException sqle){
			    	System.out.println(sqle);
			    	break;
			    }
			}
			br.close();
		}
		catch(IOException fnfe){
			System.out.println(fnfe);
		}
	}
//	Table dropped successfully.
//	Table created successfully.
//	org.postgresql.util.PSQLException: FATAL: sorry, too many clients already
//	Time taken for ConnectionLeakage function = 1508 milliseconds

	public static void ConnectionPerRow(){
		try {
			FileReader fr = new FileReader("student.txt");
			BufferedReader br = new BufferedReader(fr);
			String line;
			String str = "insert into bigstudent values(?, ?, ?, ?)";
			while((line = br.readLine()) != null){
			    try{
					Connection conn = DriverManager.getConnection(
				    		"jdbc:postgresql://localhost:5100/postgres", "suyash", "");
				    //Statement stmt = conn.createStatement();
					String line2 = line.replaceAll("'", "");
					String delims = "[,]+";
					String[] tokens = line2.split(delims);
//					System.out.println(tokens[0]+" "+tokens[1]+" "+tokens[2]+" "+tokens[3]);
					PreparedStatement pstmt = conn.prepareStatement(str);
					pstmt.setString(1, tokens[0]);
					pstmt.setString(2, tokens[1]);
					pstmt.setString(3, tokens[2]);
					pstmt.setInt(4, Integer.parseInt(tokens[3].trim()));
					pstmt.executeUpdate();
					conn.close();
				}
			    catch(SQLException sqle){
			    	System.out.println(sqle);
			    	break;
			    }
			}
			br.close();
		}
		catch(IOException fnfe){
			System.out.println("File not found.");
		}
	}
//	Table dropped successfully.
//	Table created successfully.
//	Time taken for ConnectionPerRow function = 210416 milliseconds

	public static void SingleConnection(){
		try (
				Connection conn = DriverManager.getConnection(
			    		"jdbc:postgresql://localhost:5100/postgres", "suyash", "");
			)
		{
			FileReader fr = new FileReader("student.txt");
			BufferedReader br = new BufferedReader(fr);
			String line;
			String str = "insert into bigstudent values(?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(str);
			while((line = br.readLine()) != null){
			    try{
					
				    //Statement stmt = conn.createStatement();
					
			        
			        String line2 = line.replaceAll("'", "");
					String delims = "[,]+";
					String[] tokens = line2.split(delims);
//					System.out.println(tokens[0]+" "+tokens[1]+" "+tokens[2]+" "+tokens[3]);
					pstmt.setString(1, tokens[0]);
					pstmt.setString(2, tokens[1]);
					pstmt.setString(3, tokens[2]);
					pstmt.setInt(4, Integer.parseInt(tokens[3].trim()));
					pstmt.executeUpdate();
				}
				catch(SQLException sqle){
			    	System.out.println(sqle);
			    	break;
			    }			    
			}
			br.close();
		}
		catch(IOException fnfe){
			System.out.println("File not found.");
		}
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
	}
//	Table dropped successfully.
//	Table created successfully.
//	Time taken for SingleConnection function = 249174 milliseconds

	public static void GroupCommit(){
		try (
				Connection conn = DriverManager.getConnection(
			    		"jdbc:postgresql://localhost:5100/postgres", "suyash", "");
			)
		{	
			conn.setAutoCommit(false);
			
			FileReader fr = new FileReader("student.txt");
			BufferedReader br = new BufferedReader(fr);
			String line; int Count=0;
			String str = "insert into bigstudent values(?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(str);
			while((line = br.readLine()) != null){
			    try{
			        String line2 = line.replaceAll("'", "");
					String delims = "[,]+";
					String[] tokens = line2.split(delims);
//					System.out.println(tokens[0]+" "+tokens[1]+" "+tokens[2]+" "+tokens[3]);
					pstmt.setString(1, tokens[0]);
					pstmt.setString(2, tokens[1]);
					pstmt.setString(3, tokens[2]);
					pstmt.setInt(4, Integer.parseInt(tokens[3].trim()));
					pstmt.executeUpdate();
					Count++;
					if(Count % 1000 == 0) conn.commit();					
				}
				catch(SQLException sqle){
			    	System.out.println(sqle);
			    	break;
			    }			    
			}
			br.close();
		}
		catch(IOException fnfe){
			System.out.println("File not found.");
		}
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
	}
//	Table dropped successfully.
//	Table created successfully.
//	Time taken for GroupCommit function = 3996 milliseconds
	
	public static void BatchedInsert(){
		try (
				Connection conn = DriverManager.getConnection(
			    		"jdbc:postgresql://localhost:5100/postgres", "suyash", "");
			)
		{	
			FileReader fr = new FileReader("student.txt");
			BufferedReader br = new BufferedReader(fr);
			String line; int Count=0;
			String str = "insert into bigstudent values(?, ?, ?, ?)";;
			PreparedStatement pstmt = conn.prepareStatement(str);
			while((line = br.readLine()) != null){
			    try{
					
			        String line2 = line.replaceAll("'", "");
					String delims = "[,]+";
					String[] tokens = line2.split(delims);
//					System.out.println(tokens[0]+" "+tokens[1]+" "+tokens[2]+" "+tokens[3]);
					pstmt.setString(1, tokens[0]);
					pstmt.setString(2, tokens[1]);
					pstmt.setString(3, tokens[2]);
					pstmt.setInt(4, Integer.parseInt(tokens[3].trim()));
					pstmt.addBatch();
					Count++;
					if(Count % 1000 == 0) pstmt.executeBatch();
					
				}
				catch(SQLException sqle){
			    	System.out.println(sqle);
			    	break;
			    }
			}
			br.close();
		}
		catch(IOException fnfe){
			System.out.println("File not found.");
		}
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
	}
//	Table dropped successfully.
//	Table created successfully.
//	Time taken for BatchedInsert function = 2486 milliseconds
	
	public static void main(String[] args) {
		 
		// In the JDBC API 4.0, the DriverManager.getConnection method loads
		// JDBC drivers automatically. As a result you do not need to call the 
		// Class.forName method 
		// try {
		// 	  Class.forName ("org.postgresql.Driver");
		//  }
		//  catch (Exception e) {
		// 	System.out.println("Could not load driver: " + e);
		// }
		
	
		// The following syntax is called try with resources which can be used with any resource
		// that supports the java.lang.AutoCloseable interface. 
		// It ensures that the resources get closed at the end of the try block.  
		// It is **MUCH** preferred to the old style to avoid connection leakage.
		// Note the URL syntax below:  jdbc:postgresql tells the DriverManager to use the 
		// postgresql JDBC driver.  
		// localhost can be replaced with a host name if the postgresql is running on a remote machine.
		// Replace 6432 with the port number you are using, and dbis with your database name
		// Similarly, replace sudarsha with the user name you are using for your database.  
		//Class.forName("org.postgresql.Driver");
		try(
		    Connection conn = DriverManager.getConnection(
		    		"jdbc:postgresql://localhost:5100/postgres", "suyash", "");
		    Statement stmt = conn.createStatement();
		    )
		{
		    	try{
		    		String del = "drop table bigstudent";
		    		stmt.executeUpdate(del);
		    		System.out.println("Table dropped successfully.");
		    	}
		    	catch (SQLException sqle){
		    		System.out.println("Table could not be deleted");
		    	}
		    	
		    	try {
		    		String str = "CREATE TABLE bigstudent("+
		    				  "id varchar(5) NOT NULL,"+
		    				  "name varchar(20) NOT NULL,"+
		    				  "dept_name varchar(20),"+
		    				  "tot_cred numeric(3,0),"+
		    				  "PRIMARY KEY (id))";
		    		stmt.executeUpdate(str);
		    		System.out.println("Table created successfully.");
		    		
				} 
		    	catch (SQLException sqle) {
					System.out.println("Could not create table.");
				}
				
			    
			    long starttime = System.currentTimeMillis();
//			    ConnectionLeakage();
//			    ConnectionPerRow();
//			    SingleConnection();
//			    GroupCommit();
			    BatchedInsert();
			    long endtime = System.currentTimeMillis();
//			    System.out.println("Time taken for ConnectionLeakage function = " + (endtime - starttime) + " milliseconds");
//			    System.out.println("Time taken for ConnectionPerRow function = " + (endtime - starttime) + " milliseconds");
//			    System.out.println("Time taken for SingleConnection function = " + (endtime - starttime) + " milliseconds");
//			    System.out.println("Time taken for GroupCommit function = " + (endtime - starttime) + " milliseconds");
			    System.out.println("Time taken for BatchedInsert function = " + (endtime - starttime) + " milliseconds");
			    
			    // The following are not required anymore since the connections were opened
			    // with the try with resources feature:
			    // stmt.close();
			    // conn.close();
			}
		catch (Exception sqle)
		{
		System.out.println("Exception : " + sqle);
		}
	}
}