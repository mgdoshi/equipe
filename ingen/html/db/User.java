package ingen.html.db;

import java.sql.*;
import java.util.*;
import java.lang.*;

public class User
{

  public boolean ChangeUserPassword(String userid,String oldpass,String newpass)
  {
     boolean retVal=false; 
     try
     {
       int id = Integer.parseInt(userid.trim());
       retVal = ChangeUserPassword ( id,oldpass,newpass );
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retVal; 

  }  
  public boolean ChangeUserPassword(int userid,String oldpass,String newpass)
  {
    boolean retval = false;
    if(isUserValid(userid) && getUserPass(userid).equals(oldpass))
    {
      DBConnect db = new DBConnect();
      Connection conn= db.GetDBConnection();
      String query = " UPDATE T_USER SET User_Pass = '"+newpass+"' " +
                     " WHERE user_id = "+userid+"";
      try
      {
        Statement stmt = conn.createStatement();
        if(stmt.executeUpdate(query)==1)
         retval = true;
        stmt.close();
        conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
    }  
    return retval;
  }


  public boolean isUserValid (int userid)
  {
    
    boolean retval = false;
    DBConnect db = new DBConnect();
    if(db.getName(userid, "User")!=null)
     retval = true;
    return retval; 
  } 

  public boolean isLoginValid ( String userName, String userPass )
  {
    boolean retval = false;
    DBConnect db = new DBConnect();
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
    String query = " SELECT User_ID "+
                   " FROM   T_User "+
                   " WHERE  User_Name = '"+userName+ "'" +
                   " AND    User_Pass = '"+userPass+ "'";
    try
    {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       stmt.executeQuery(query);
       rs = stmt.getResultSet();
       if(rs.next())
       {
          retval = true;
       }
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;
  } 


  public String getUserPass(String userid)
  {
     String retVal=null; 
     try
     {
       int id = Integer.parseInt(userid.trim());
       retVal = getUserPass ( id );
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retVal; 

  }

  public String getUserPass ( int userid )
  {
    String retval=null;
    String query = "Select User_Pass From T_User " + 
                   "Where  User_ID = "+userid;
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
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

  public String getLangIDForUser ( String userid )
  {
     String retVal=null; 
     try
     {
       int id = Integer.parseInt(userid.trim());
       retVal = getLangIDForUser ( id );
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retVal; 
  }
  public String getLangIDForUser( int userid )
  {
    String retval=null;
    String query = "Select Fk_Lang_ID From T_User " + 
                   "Where  User_ID = "+userid;
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
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


  public String getSchemeIDForUser ( String userid )
  {
     String retVal=null; 
     try
     {
       int id = Integer.parseInt(userid.trim());
       retVal = getSchemeIDForUser ( id );
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retVal; 
  }
  public String getSchemeIDForUser ( int userid )
  {
    String retval=null;
    String query = "Select Fk_Scheme_ID From T_User " + 
                   "Where  User_ID = "+userid;
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
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



  public String getClientIDForUser ( String userid )
  {
     String retVal=null; 
     try
     {
       int id = Integer.parseInt(userid.trim());
       retVal = getClientIDForUser( id );
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retVal; 
  }
  public String getClientIDForUser ( int userid )
  {
    String retval=null;
    String query = "Select Fk_Client_ID From T_User " + 
                   "Where  User_ID = "+userid;
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
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


  public String getEmployeeIDForUser ( String userid )
  {
     String retVal=null; 
     try
     {
       int id = Integer.parseInt(userid.trim());
       retVal = getEmployeeIDForUser( id );
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retVal; 
  }
  public String getEmployeeIDForUser ( int userid )
  {
    String retval=null;
    String query = "Select Fk_Employee_ID From T_User " + 
                   "Where  User_ID = "+userid;
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
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


  public String getRecSecIDForUser ( String userid )
  {
     String retVal=null; 
     try
     {
       int id = Integer.parseInt(userid.trim());
       retVal = getRecSecIDForUser( id );
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retVal; 
  }
  public String getRecSecIDForUser ( int userid )
  {
    String retval=null;
    String query = "Select Fk_RecSec_ID From T_User " + 
                   "Where  User_ID = "+userid;
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
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

  public void EncryptString (String  pass)
  {
  }

  String DecryptString ( String  pass)
  {
     return null;
  }
}

