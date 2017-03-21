import javax.servlet.*;
import javax.servlet.http.*;
import ingen.html.util.*;
import ingen.html.*;
import ingen.html.db.*;
import java.sql.*;

public class Audit
{
  public static String getCurrentUser( int auditID )
  {
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval=null;

     String query = " SELECT Fk_User_ID "+
                    " FROM   T_Audit "+
                    " WHERE  Audit_ID = " + auditID;
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       stmt.executeQuery(query);
       rs = stmt.getResultSet();
       if(rs.next())
       {
          retval = rs.getString(1);
       }
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       
  }

  public static String GetLoginInfo( char info, int auditID)
  {
    Statement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    String retval=null;
    String query=null;  
    try
    {
       if (info == 'T')
       {
         query = "SELECT Login_Dt " +
                 "FROM   T_Audit WHERE Audit_ID = " + auditID;
       } 
       else
       {
          query = "SELECT Login_dt " +
                  "FROM   T_Audit WHERE Audit_ID = " + auditID;
       } 
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       stmt.executeQuery(query);
       rs = stmt.getResultSet();
       if(rs.next())
       {
          retval = rs.getString(1);
       }
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       

  }  

 public static String GetLogoutInfo( char info, int auditID )
 {
    Statement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    String retval=null;
    String query=null;  
    try
    {
       if (info == 'T')
       {
         query = "SELECT Logout_dt" +
                 "FROM   T_Audit WHERE Audit_ID = " + auditID;
       } 
       else
       {
          query = "SELECT Logout_dt " +
                  "FROM   T_Audit WHERE Audit_ID = " + auditID;
       } 
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       stmt.executeQuery(query);
       rs = stmt.getResultSet();
       if(rs.next())
       {
          retval = rs.getString(1);
       }
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       
  }  
    

}

