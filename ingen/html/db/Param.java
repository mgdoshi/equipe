package ingen.html.db;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.io.*;

public class Param
{

  public static String getParamClass(String paramID)
  {
    String retval=null; 
     try
     { 
       int paramid = Integer.parseInt(paramID.trim());
       retval = getParamClass(paramid);
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retval;
  }

  public static String getParamClass(int paramid)
  {
    Statement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    String retval=null;
    String query ="Select DM_ParClass " + 
                  "From   T_Param " +
                  "Where  Param_ID ="+paramid;
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

  public static String getParamValue(String paramName)
  {
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval=null;
     String query = "  Select par.Param_Value "+
                    "  From   T_Param par"+
                    "  Where  par.Param_Name = '"+paramName+"'";
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

}
