package ingen.html.db;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class DBConnect
{
   /**
   <PRE>
   Connect to database and if successfull then returns connection 
   object else null.
   </PRE>
   */
   public Connection GetDBConnection()
   {
     try
     {  
        Driver D = (Driver)Class.forName("postgresql.Driver").newInstance(); 
        Connection conn = DriverManager.getConnection ("jdbc:postgresql://ntsvr3:6666/ingdb","postgres","cetni");
        return conn;
     }catch(Exception sexe){System.out.println(sexe);}
     return null;  
   }

  public String getNextVal(String seqName)
  {
     PreparedStatement ps = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval=null;
     try
     {
        DBConnect db = new DBConnect();
        conn = db.GetDBConnection();
        ps = conn.prepareStatement("Select NEXTVAL('"+seqName+"')");
        ps.executeQuery();
        rs = ps.getResultSet();
        if(rs.next())
        {
           retval = rs.getString(1);
        }
        ps.close();
        conn.close();
    }catch(Exception sexe){System.out.println(sexe);}
    return retval;
  }


  public String getName(String ID, String TabName)
  {
    String retval=null;
    try
    {
      int id = Integer.parseInt(ID.trim());
      retval = getName(id,TabName);  
    }catch(NumberFormatException nexe){System.out.println(nexe);}
    return retval;
  }
  public String getName(int ID, String TabName)
  {
     String retval=null;
     String query = "select "+ TabName+"_Name From T_"+TabName + " Where "+ TabName+"_ID =" +ID;
     try
     {
        DBConnect db = new DBConnect();
        Connection conn = db.GetDBConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( query );
        if(rs.next())
        {
          retval = rs.getString(1);  
        }
        stmt.close();
        conn.close();
    }catch(Exception sexe){System.out.println(sexe);}
    return retval;
  } 

  public String getDesc(String ID, String TabName)
  {
    String retval=null;
    try
    {
      int id = Integer.parseInt(ID.trim());
      retval = getDesc(id,TabName);  
    }catch(NumberFormatException nexe){System.out.println(nexe);}
    return retval;
  }
  public String getDesc(int ID, String TabName)
  {
     String retval=null;
     String query = "select "+ TabName+"_Desc From T_"+TabName + " Where "+ TabName+"_ID =" +ID;
     try
     {
        DBConnect db = new DBConnect();
        Connection conn = db.GetDBConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( query );
        if(rs.next())
        {
          retval = rs.getString(1);  
        }
        stmt.close();
        conn.close();
    }catch(Exception sexe){System.out.println(sexe);}
    return retval;
  } 

  public String getID(String Name, String TabName)
  {
     String retval=null;
     String query = "select "+ TabName+"_ID From T_"+TabName + " Where "+ TabName+"_Name ='"+Name+"'";
     try
     {
        DBConnect db = new DBConnect();
        Connection conn = db.GetDBConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( query );
        if(rs.next())
        {
          retval = rs.getString(1);  
        }
        stmt.close();
        conn.close();    
    }catch(Exception sexe){System.out.println(sexe);}
    return retval;
  } 

  public String[] getRecord(String pid,String tab_name)
  {
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval[]=null; 
     int id=-1;
     try
     {
       id = Integer.parseInt(pid.trim());
     }catch(NumberFormatException nexe){System.out.println(nexe);return retval;} 
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       stmt.executeQuery("Select * from t_" + tab_name + " where " + tab_name + "_id = " + id);
       rs = stmt.getResultSet();
       ResultSetMetaData rsmd = rs.getMetaData();
       int count = rsmd.getColumnCount();
       retval = new String[count];
       if(rs.next())
       {
          for(int i=0;i<count;i++)
          {
             retval[i] = rs.getString(i+1);
          }  
       }
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       
  }

  public String[] getRecord(String tab_name,int bid)
  {
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval[]=null; 
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       stmt.executeQuery("Select * from t_" + tab_name + " where " + tab_name + "_id = " + bid);
       rs = stmt.getResultSet();
       ResultSetMetaData rsmd = rs.getMetaData();
       int count = rsmd.getColumnCount();
       retval = new String[count];
       if(rs.next())
       {
          for(int i=0;i<count;i++)
          {
             retval[i] = rs.getString(i+1);
          }  
       }
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       
  }

}