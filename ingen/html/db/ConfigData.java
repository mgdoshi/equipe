package ingen.html.db;

import java.sql.*;
import java.io.*;
import java.util.*;

public class ConfigData
{
   private Properties list = null;  
   private String pObject = null;
   public String GetConfigValue( String parentObj, String langID, String objName, String defName )
   {
     String retval=defName; 
     try
     { 
       int id = Integer.parseInt(langID.trim());
       retval = GetConfigValue(parentObj,id,objName,defName);
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retval;
   }

   public String GetConfigValue( String parentObj, int langID, String objName, String defName )
   {
      String objDesc = null;
      if(parentObj==null || objName==null)
        return defName; 
      if(list==null || pObject==null || !pObject.equalsIgnoreCase(parentObj))
      {
        list = new Properties();
        pObject = parentObj;
        Statement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try
        {
          DBConnect obj = new DBConnect();
          conn = obj.GetDBConnection();
          String sql = " SELECT Obj_name,Property_Value" +
                       " FROM   T_Config"+
                       " WHERE  parent_obj = '"+parentObj+"'"+
                       " AND    fk_lang_id = "+langID;
          stmt = conn.createStatement();
          rs = stmt.executeQuery(sql);
          while(rs.next())
          {
            String key = rs.getString(1);
            String val = rs.getString(2);  
            list.put(key,val);
            if(key.equals(objName))
             objDesc = val;
          }
          if( objDesc != null) 
            return objDesc;
        }catch(Exception sexe)
        {
          System.out.println(sexe);
          list=null;
          pObject=null;
          return defName;
        }
        finally
        {
          try
          {
            if(stmt!=null)
              stmt.close();
            if(conn!=null)
              conn.close();
          }catch(SQLException sexe){}
        } 
      }
      else if(list!=null)
        return list.getProperty(objName,defName);
      return defName;
    }
}

