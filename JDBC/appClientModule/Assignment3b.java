/*
 * 130050010, Suyash A. Bhatkar
 */
import java.sql.*;
import java.util.*;

public class Assignment3b {
	/*
	 * The function printSchema regenerates the create table statements but without constraints.
	 */
	public static void printSchema(Connection conn){
		try{
			DatabaseMetaData dbmd = conn.getMetaData();
			String catalog = null;
			String schemaPattern = null;
			String tableNamePattern = null;
			String[] types = {"TABLE"};

			ResultSet tableNames = dbmd.getTables(catalog, schemaPattern, tableNamePattern, types);
			while(tableNames.next()) { 
			    catalog = null;
				schemaPattern = null;
				tableNamePattern = tableNames.getString(3);
				String columnNamePattern = null;
				ResultSet columnNames = dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
				System.out.println("CREATE TABLE "+tableNamePattern+"(");
				while(columnNames.next()){
					System.out.println("\t"+columnNames.getString(4)+" "+columnNames.getString("TYPE_NAME")+"("+columnNames.getInt("COLUMN_SIZE")+"),");
				}
				System.out.println(");\n");
			}
		}
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
	}
	
	/*
	 * The function named injection  repeatedly takes a word or pattern from terminal using a scanner
	 * and displays ID and name of all students in the bigstudent relation where the student name 
	 * matches the given word using the ILIKE pattern match command.
	 * This is done in two ways, using the Prepared Statement method and using normal query string 
	 * method. Also, writing 'exit' in the terminal exits terminates	 the program.
	 * Uncomment lines 58-79 and comment out the lines 76-95 for the Prepared Statement Method
	 * Uncomment lines 81-101 and comment out the lines for the normal Query String Method
	 * A query like " Isenh%'; delete from bigstudent where name like '% " deletes the whole table
	 * A query like " %'; update bigstudent set tot_cred = 50 where name ilike '%Isenh " updates tot_cred to 50 for name = 'Isenh'
	 * named bigstudent, or any other table we can specify! Prepared statement avoids this and doesnt
	 * let stray commands edit the database!
	 */
	public static void injection(){
		try(
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5100/postgres", "suyash", "");
			Statement stmt = conn.createStatement();
			){
				String str = null;
				Scanner in = new Scanner(System.in);
				String sql = "select id, name from bigstudent where name ilike ?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				while(true){
					str = in.nextLine();
					if(str.equals("exit")) break;
					String st = "%"+str+"%";
					pstmt.setString(1, st);
					ResultSet rs = pstmt.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();

		    		int columnCount = rsmd.getColumnCount();
					if(rs.next()){
			    		int i = columnCount;
						while(i > 0) {
							System.out.println(rsmd.getColumnName(1)+"\t");
							i--;
						}
						do{
				    		i = columnCount;
							while(i > 0) {
								System.out.println(rs.getString(i)+"\t");
								i--;
							}
						}
						while(rs.next());
					}
				}
				in.close();
				
//				String sql = "select id, name from bigstudent where name ilike ";
//				while(true){
//					str = in.nextLine();
//					if(str.equals("exit")) break;
//					String st = sql+"'%"+str+"%'";
//					ResultSet rs = stmt.executeQuery(st);
//					System.out.println(st);
//					ResultSetMetaData rsmd = rs.getMetaData();
//					if(rs.next()){
//						System.out.println(rsmd.getColumnName(1)+"\t"+rsmd.getColumnName(2));
//						do{
//							String id = rs.getString(1);
//							String name = rs.getString(2);
//							System.out.println(id+"\t"+name);
//						}
//						while(rs.next());
//					}
//				}
//				in.close();
		}
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
		
		
	}
	/*
	 * Uncomment respective functions for use.
	 */
	public static void main(String[] args){	
		try(
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5100/postgres", "suyash", "");
			Statement stmt = conn.createStatement();
			){
			printSchema(conn);
//			injection();
		}
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
	}
}