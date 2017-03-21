
package ingen.html.db;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.lang.*;
import java.util.*;

public class Menu
{
  public static boolean isMenuEnabled( String userID, String menuName )
  {
     int nCnt=0;
     int nAllowCnt=0;
     int nDisAllowCnt=0;
     Statement stmt = null;
     PreparedStatement ps = null;
     Connection conn = null;
     ResultSet rs = null;
     boolean retval=false;
     if( userID == null )
       retval = false;
     String query = " SELECT count(*)" +
                    " FROM   T_MenuSec msc " +
                    " WHERE  msc.Menu_Name ='"+ menuName +"'" +
                    " AND    msc.DM_SecLevel = 'S' " +
                    " AND    msc.Allow_Flg = 0 ";
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();       
       rs = stmt.executeQuery(query);
       if(rs.next())
         {
           nDisAllowCnt = Integer.parseInt( rs.getString(1) );
         }
         stmt.close();
         conn.close();
     }catch(Exception sexe){System.out.println(sexe);}

     if( nDisAllowCnt > 0 )
        retval = false;

     query = " SELECT count(*)" +
             " FROM   T_MenuSec msc " +
             " WHERE  msc.Ref_Id = " + Integer.parseInt( userID ) +
             " AND    msc.Menu_Name ='"+ menuName +"'" +
             " AND    msc.DM_SecLevel = 'U' " +
             " AND    msc.Allow_Flg = 1 ";

     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();       
       rs = stmt.executeQuery(query);
       if(rs.next())
         {
           nAllowCnt = Integer.parseInt( rs.getString(1) );
         }
         stmt.close();
         conn.close();
     }catch(Exception sexe){System.out.println(sexe);}

     if( nAllowCnt > 0 )
        retval = true;

     query = " SELECT count(*), SUM( msc.Allow_Flg )" +
             " FROM   T_MenuSec msc " +
             " WHERE  msc.Menu_Name ='"+ menuName +"'" +
             " AND    msc.DM_SecLevel = 'G' " +
             " AND    msc.Ref_ID IN ( " +
                                 "  Select usg.Fk_Group_ID " +
                                 "  From   T_UserGroup usg " +
                                 "  Where  usg.Fk_User_ID = " + userID +")";

     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();       
       rs = stmt.executeQuery(query);
       if(rs.next())
         {
           nCnt = Integer.parseInt( rs.getString(1) );
           nAllowCnt = Integer.parseInt( rs.getString(2) );
         }
         stmt.close();
         conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     
     if( ( nAllowCnt > 0 ) || ( nCnt == 0 ) ) 
       retval = true;
     else
       retval = false;

     return retval;
  }
}

