import ingen.html.*;
import ingen.html.db.*;
import ingen.html.util.*;
import ingen.html.frame.*;
import ingen.html.head.*;
import java.sql.*;
import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class ShowChilds
{

   public synchronized static HtmlTag ShowParent( String pvParentID )
   { 
     DBConnect obj = new DBConnect();
     Connection conn = null;
     HtmlTag retVal = new HtmlTag();
     try
     {  
       conn = obj.GetDBConnection();
       ShowTree(conn,pvParentID,retVal);
     }catch(Exception sexe){}
     finally
     {
       try
       {
         if(conn!=null)
           conn.close();
       }catch(Exception sexe){}
     } 
     return retVal; 
   }

   public static HtmlTag ShowTree( Connection conn,String pvParentID, HtmlTag tag)
   {
     ResultSet rs = null;  
     Statement stmt = null; 
     try
     {
       String query = " Select * From T_Help "+
                      " Where  Parent_ID = "+pvParentID+
                      " Order  By Help_ID";
       stmt = conn.createStatement(); 
       rs = stmt.executeQuery(query);
       while(rs.next())
       {
         String data = rs.getString(4);
         String cap = rs.getString(3);
         if( data!=null && data.equals("*") )
         {
            tag.add( " Menu[i] = \"POPUPBEGIN, "+ cap + ", *, *\" ");
            tag.add( " i++ ");
            ShowTree(conn,rs.getString(1),tag);
            tag.add( " Menu[i] = \"POPUPEND, *, *, *\" ");
            tag.add( " i++ ");
         } 
         else
         {
            tag.add( " Menu[i] = \"MENUITEM, "+ cap+","+cap+","+ data +"\" ");
            tag.add( " i++ ");
         }
       }
       return tag;
     }catch(Exception sexe){System.out.println(sexe);}
     finally
     {
       try
       {
         if(stmt!=null)
           stmt.close();
         if(rs!=null)
           rs.close();
       }catch(Exception sexe){}
     } 
     return tag;
  }
}
