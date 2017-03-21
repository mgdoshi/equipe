
package ingen.html.db;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.lang.*;
import java.util.*;

public class Domain
{
  public static String GetDomainName(String domain)
  {
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval=null; 
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       rs = stmt.executeQuery(" Select Domain_Name From T_Domain" +
                              " Where Domain = '"+domain+"'");
       if(rs.next())
       {
          retval = rs.getString(1);
       }
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;
  }

  public static String getDomainAttribFrmDesc ( String vDomain, String attribDesc, String langID )
  {
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval=null; 
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       rs = stmt.executeQuery(" Select Attrib From T_Domain" +
                              " Where Domain = '"+vDomain+"'" +
                              " AND   Attrib_Desc = '"+attribDesc+"'" +
                              " AND   Fk_Lang_ID = " + Integer.parseInt( langID.trim()) );
       if(rs.next())
       {
          retval = rs.getString(1);
       }
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;
  }

  public static String getDomainDescFrmAttrib ( String vDomain, String attrib, String langID )
  {
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval=null; 
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       rs = stmt.executeQuery(" Select Attrib_desc From T_Domain" +
                              " Where Domain = '"+vDomain+"'" +
                              " AND   Attrib = '"+attrib+"'" +
                              " AND   Fk_Lang_ID = " + Integer.parseInt( langID.trim() ) );
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

