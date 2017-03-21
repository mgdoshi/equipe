package ingen.html.db;

import java.sql.*;
import java.lang.*;
import java.util.*;

public class Scheme
{
  public static String getSchemeProperty( String schemeID, char objType, char opMode)
  {
     String retval=""; 
     try
     { 
       int schemeid = Integer.parseInt(schemeID.trim());
       retval = getSchemeProperty(schemeid,objType,opMode);
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retval;
  }

  public static String getSchemeProperty( int scheme_id, char objType, char opMode)
  {
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval="";
     String query = "Select Prop1, Prop2, Prop3, Prop4, Prop5, "+
                          " Prop6, Prop7, Prop8, Prop9, Prop10, " + 
                          " Prop11, Prop12, Prop13, Prop14, Prop15, " + 
                          " Prop16, Prop17, Prop18, Prop19, Prop20" +                
                    " FROM   T_SchemeRef " +
                    " WHERE  FK_Scheme_ID = "+scheme_id+
                    " AND    DM_ObjType   = '"+objType+"'"+
                    " AND    DM_OpMode    = '"+opMode+"'";
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       rs = stmt.executeQuery(query);
       if(rs.next())
       { 
         for( int i = 0; i< 20; i++) 
         {
           if( rs.getString(i+1) != null )
           {
              retval += rs.getString(i+1) + " "; 
           }
         }
       }    
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       
  }
 
}

