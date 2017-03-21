import java.util.*;
import java.sql.*;
import java.lang.*;

public class MySqlTest{

    public static void main(String args[])
    {
        try
        { 
             Driver D = (Driver)Class.forName("postgresql.Driver").newInstance(); 
             Connection conn = DriverManager.getConnection ("jdbc:postgresql://ntsvr3:6666/ingdb","x","x");
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("select * from emp");
             while(rs.next())
             {
                System.out.println(rs.getString(1));
             }
             rs.close();
             st.close();
             conn.close(); 
        }catch(Exception sexe){System.out.println(sexe);}
    }
}

          
          