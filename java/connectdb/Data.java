import java.sql.*;
import java.sql.Statement.*;
import java.io.*;

class Data
{

   public static void main(String args[])
   {
          CallableStatement cs = null;
          PreparedStatement ps = null;
          Connection conn = null;
          ResultSet rs = null;
          try
          {
            Driver D = (Driver)Class.forName("postgresql.Driver").newInstance(); 
            conn = DriverManager.getConnection ("jdbc:postgresql://ntsvr3:6666/ingdb","postgres","cetni");
            ps = conn.prepareStatement("Select NEXTVAL('s_branch')");
            ps.executeQuery();
            rs = ps.getResultSet();
            if(rs.next())
            {
               String BranchID = rs.getString(1);
               System.out.println( "Branch ID :" + BranchID );
            }
          }catch(Exception sexe){System.out.println(sexe);}

   }

}

