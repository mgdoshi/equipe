import java.sql.*;
import java.sql.Statement.*;
import java.io.*;

class ReadData
{

   public static void main(String args[])
   {
          int BranchID=0;
          PreparedStatement ps = null;
          Connection conn = null;
          ResultSet rs = null;
          try
          {
            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
            conn = DriverManager.getConnection ("jdbc:oracle:oci7:@oradb01", "wfweb", "wf");
            ps = conn.prepareStatement("Select GetBranchID( ? ) From dual");
            ps.setString(1, "INTPUN");
            ps.executeQuery();
            rs = ps.getResultSet();
            if(rs.next())
            {
               BranchID = rs.getInt(1);
            }
            System.out.println( "Branch ID :" + BranchID );
          }catch(Exception sexe){System.out.println(sexe);}

   }

}