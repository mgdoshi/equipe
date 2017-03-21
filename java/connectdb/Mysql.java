import java.awt.*;
import java.net.*;
import java.util.*;
import java.sql.DriverManager;
import org.gjt.mm.mysql.*;

public class Mysql 
{
	public Mysql()
	{
		try
		{
			 /* 
			  * Register the driver using Class.forName() 
			  */
			 System.out.println("Driver Registration starts");              
			// Class.forName("org.gjt.mm.mysql.Driver") ;

			/* 
			 * Register the driver, avoiding a bug in some JDK1.1 
			 * (like Netscape) implementations
			 */

			
			 Driver D =(Driver)Class.forName("org.gjt.mm.mysql.Driver").newInstance(); 
			 System.out.println("Driver Registration Complete Driver ="+D);
			 //System.out.println("Registred Driver ="+D);

			 /*
			  * Register the driver via the system properties variable 
			  * "jdbc.drivers"
			  */ 
              
			  //Properties P = System.getProperties(); 
			  //P.put("jdbc.drivers", "org.gjt.mm.mysql.Driver"); 
			  //System.setProperties(P);

			  


                          Connection conn = null; 
			  String host="91.212.25.13";
			  int port=3306;
			  String user="ordadm";
			  String pass="ordadm";
			  
			  Properties P = System.getProperties(); 
			  P.put("jdbc.drivers",D); 
			  P.put("user","ordadm");
			  P.put("password","ordadm");	
			  // System.setProperties(P);	
		          // Socket s=new Socket("NTSVR3",3306);
			
			  System.out.println("Trying to connect "+host);
			
			  conn=(org.gjt.mm.mysql.Connection)D.connect("jdbc:mysql://amartya:3306/ingdb",P); 
			  System.out.println("Connection established to "+host);

	
			  Statement stmt=(org.gjt.mm.mysql.Statement)conn.createStatement();
                       //   String query = "INSERT INTO t_address (address1,address2,address3,address4) values('hdfjksf','kkdfkjjsf','hndkjhsg','hdfjksf')";
			  ResultSet rest=(org.gjt.mm.mysql.ResultSet)stmt.executeQuery("Select * from t_address");		
			  System.out.println();
			  System.out.println();
			  System.out.println("Data From Table t_address is as Follows ");	
			  System.out.println();
			  while(rest.next())
			  {
				System.out.print(" "+rest.getString(1));
				System.out.print("    "+rest.getString(2));
				System.out.print("    "+rest.getString(3));
				System.out.print("    "+rest.getString(4));
				System.out.print("    "+rest.getString(5));
				System.out.print("    "+rest.getString(6));
				System.out.print("    "+rest.getString(7));
				System.out.println("    "+rest.getString(8));
			  }
  			  System.out.println();		
       //       	stmt.executeUpdate(query);
                stmt.close();
                conn.close();
                          
		}
		catch(Exception e)
		{
			System.out.println("Exception Occures	"+e);
			System.out.println("Exception Occures	"+e.getMessage());
		}
	 }
	 public static void main(String [] args)
	 {
		Mysql m=new Mysql();
	 }

}