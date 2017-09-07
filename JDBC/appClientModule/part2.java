/*
 * 130050010, Suyash A. Bhatkar
 * The Function prepared(str) takes in a string str and uses the prepared statement to query the database according to the question
 * The Function Injection(str) takes in a string str and does not use the prepared statement to query the database according to the question
 * Comments are written after the Injection function too.
 */
import java.sql.*;
import java.util.*;

public class part2{
	public static void prepared(String str){
		try(
		    Connection conn = DriverManager.getConnection(
		    		"jdbc:postgresql://localhost:5100/postgres", "suyash", "");
		    Statement stmt = conn.createStatement();
			    )
		{
			String sql = "select * from bigstudent where name like ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			String st = "%"+str+"%";
			pstmt.setString(1, st); 
//			System.out.println(st);
//			System.out.println(pstmt);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println(rsmd.getColumnName(1)+"\t"+rsmd.getColumnName(2)+"\t"+rsmd.getColumnName(3)+"\t"+rsmd.getColumnName(4));
			while(rs.next()){
				String id = rs.getString(1);
				String name = rs.getString(2);
				String dept = rs.getString(3);
				int cre = rs.getInt(4);
				System.out.println(id+"\t"+name+"\t"+dept+"\t"+cre);
			}
		}
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
	}
	
	public static void Injection(String str){
		try(
		    Connection conn = DriverManager.getConnection(
		    		"jdbc:postgresql://localhost:5100/postgres", "suyash", "");
		    Statement stmt = conn.createStatement();
			    )
		{
			String sql = "select * from bigstudent where name like "+"'%"+str+"%';";
//			String sql = "select * from bigstudent"+str;
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println(rsmd.getColumnName(1)+"\t"+rsmd.getColumnName(2)+"\t"+rsmd.getColumnName(3)+"\t"+rsmd.getColumnName(4));
			while(rs.next()){
				String id = rs.getString(1);
				String name = rs.getString(2);
				String dept = rs.getString(3);
				int cre = rs.getInt(4);
				System.out.println(id+"\t"+name+"\t"+dept+"\t"+cre);
			}
		}
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
	}
/*	This function is prone to SqlInjection  
 * 	For example, this query: "Isenh%'; delete from bigstudent where name like '%"
 * 	will delete all the rows in the table student!
 * 	Prepared Statement is used in the Prepared Function
 */
	
	public static void main(String[] args){	
			System.out.println("Enter a substring: ");
			Scanner in = new Scanner(System.in);
			String str = in.nextLine();
			in.close();
//			prepared(str);
			Injection(str);
	}
}