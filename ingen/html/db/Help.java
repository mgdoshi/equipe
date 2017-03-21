
package ingen.html.db;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.lang.*;
import java.util.*;

public class Help
{
  public static String GetColumnMenuPath( String helpID )
  {
     int nParentID = 1;
     String vCaption = null;
     String vColPath = null;
     int nCurrID = Integer.parseInt( helpID );
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     DBConnect obj = new DBConnect();
     try
     {
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       while( nParentID != 0 )
       {  
         String query = " SELECT Parent_ID, Caption " +
                      " FROM   T_Help " +
                      " WHERE  Help_ID ="+ nCurrID;
         rs = stmt.executeQuery(query);
         if(rs.next())
         {
           nParentID = Integer.parseInt( rs.getString(1) );
           vColPath = rs.getString(2) + "|"+ vColPath;
           nCurrID = nParentID;
         }
       }
       if(vColPath!=null)
         vColPath = vColPath.substring(0,vColPath.lastIndexOf("|"));  
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return vColPath;
  }
}

