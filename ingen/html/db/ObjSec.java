package ingen.html.db;

import java.sql.*;
import java.lang.*;
import java.util.*;
import java.io.*;

public class ObjSec
{
  public static int chkObjSec( String userID, String objParName, String objName, String cMode )
  {
    int nInsFlg = -2;
    int nUpdFlg = -2;
    int nDisFlg = -2;
    int nUpNFlg = -2;
    int nCnt = 0;
    int nAllowCnt = 0;
    int nDisAllowCnt = 0;
    String cObjType = null;
    String vObjParName = null;
    String vObjName = null;
    int retVal=-2; 

    vObjParName = objParName;
    vObjName    = objName;
    DBConnect obj = new DBConnect();
    Connection conn = obj.GetDBConnection();
    Statement stmt = null;
    ResultSet rs = null;
    try
    {

      stmt = conn.createStatement();
      if( userID == null || objParName == null || objName == null || cMode == null ) 
        return -1;
  
      String query =" SELECT obs.DM_ObjType " +
                    " FROM   T_ObjSec obs " +
                    " WHERE  obs.ObjParent_Name = '"+ vObjParName +"'"+
                    " AND    obs.Obj_Name = '"+ vObjName + "'" ;

    try
     {
       rs = stmt.executeQuery(query);
       if(rs.next())
       {
           cObjType = rs.getString(1);
       }
     }catch(Exception sexe){System.out.println(sexe);retVal=-2;throw sexe;}

    if( cObjType == null )
       return 0;

    query =  " SELECT obs.Display_Flg, obs.Insert_Flg, obs.UpdateTime_Flg, obs.UpdNull_Flg " +
             " FROM   T_ObjSec obs " +
             " WHERE  obs.ObjParent_Name = '"+ vObjParName +"'"+
             " AND    obs.Obj_Name = '"+ vObjName + "'" +
             " AND    obs.DM_SecLevel = 'S' ";

    try
     {
       rs = stmt.executeQuery(query);
       if(rs.next())
         {
           nDisFlg = rs.getInt(1);
           nInsFlg = rs.getInt(2);
           nUpdFlg = rs.getInt(3);
           nUpNFlg = rs.getInt(4);
         }
     }catch(Exception sexe){System.out.println(sexe);retVal=-2;throw sexe;}

    if( cMode.equalsIgnoreCase("Q") )
    {
      if( nDisFlg == 0 )
       return 1;
    }
    else if( cMode.equalsIgnoreCase("I") )
    {
      if( ( cObjType.equalsIgnoreCase("K") && nDisFlg == 0 ) || ( !cObjType.equalsIgnoreCase("K") && ( nDisFlg == 0 || nInsFlg == 0 ) ) )
       return 1;
    }
    else if( cMode.equalsIgnoreCase("U") )
    {
      if( cObjType.equalsIgnoreCase("K") )
      {
        if( nDisFlg == 0 )
          return 1;
      }
      else
      {
        if( nDisFlg == 0 ) 
          return 1;
        else if( nUpdFlg == 0 && nUpNFlg == 0 )
          return 2;
        else if( nUpdFlg == 0 && nUpNFlg == 1 )
          return 3;
      }
    }


    query =  " SELECT obs.Display_Flg, obs.Insert_Flg, obs.UpdateTime_Flg, obs.UpdNull_Flg " +
             " FROM   T_ObjSec obs " +
             " WHERE  obs.ObjParent_Name = '"+ vObjParName +"'"+
             " AND    obs.Obj_Name = '"+ vObjName + "'" +
             " AND    obs.DM_SecLevel = 'U' " +
             " AND    obs.Ref_ID = "+ Integer.parseInt(userID);

    try
     {
       rs = stmt.executeQuery(query);
       if(rs.next())
         {
           nDisFlg = rs.getInt(1);
           nInsFlg = rs.getInt(2);
           nUpdFlg = rs.getInt(3);
           nUpNFlg = rs.getInt(4);
         }
     }catch(Exception sexe){System.out.println(sexe);retVal=-2;throw sexe;}

    if( cMode.equalsIgnoreCase("Q") )
    {
      if( nDisFlg == 0 )
        return 1;
    }
    else if(cMode.equalsIgnoreCase("I"))
    {
      if( ( cObjType.equalsIgnoreCase("K") && nDisFlg == 0 ) || ( !cObjType.equalsIgnoreCase("K") && ( nDisFlg == 0 || nInsFlg == 0 ) ) )
       return 1;
    }
    else if(cMode.equalsIgnoreCase("U") )
    {
      if( cObjType.equalsIgnoreCase("K") )
      {
        if( nDisFlg == 0 )
          return 1;
      }
      else
      {
        if( nDisFlg == 0 ) 
          return 1;
        else if( nUpdFlg == 0 && nUpNFlg == 0 )
          return 2;
        else if( nUpdFlg == 0 && nUpNFlg == 1 )
          return 3;
      }
    }

    query =  " SELECT Display_Flg, Insert_Flg,UpdateTime_Flg,UpdNull_Flg" +
             " FROM   T_ObjSec " +
             " WHERE  ObjParent_Name = '"+vObjParName+"'"+
             " AND    Obj_Name = '"+vObjName+"'" +
             " AND    DM_SecLevel = 'G' " +
             " AND    Ref_ID IN "+
                         "(Select Fk_Group_ID "+
                         " From   T_UserGroup "+
                         " Where  FK_User_ID = "+Integer.parseInt( userID )+")";
    try
     {
       rs = stmt.executeQuery(query);
       int entry=0;
       while(rs.next())
         {
           if(entry==0) 
           {
             nDisFlg =0 ;
             nInsFlg =0 ;
             nUpdFlg =0 ;
             nUpNFlg =0;
             entry++;
           }  
           nDisFlg += rs.getInt(1);
           nInsFlg += rs.getInt(2);
           nUpdFlg += rs.getInt(3);
           nUpNFlg += rs.getInt(4);
         }
     }catch(Exception sexe){System.out.println(sexe);retVal=-2;throw sexe;}

    if(cMode.equalsIgnoreCase("Q") )
    {
      if( nDisFlg == 0 )
       return 1;
    }
    else if(cMode.equalsIgnoreCase("I"))
    {
      if( ( cObjType.equalsIgnoreCase("K") && nDisFlg == 0 ) || ( !cObjType.equalsIgnoreCase("K") && ( nDisFlg == 0 || nInsFlg == 0 ) ) )
       return 1;
    }
    else if(cMode.equalsIgnoreCase("U") )
    {
      if( cObjType.equalsIgnoreCase("K") )
      {
        if( nDisFlg == 0 )
          return 1;
      }
      else
      {
        if( nDisFlg == 0 ) 
          return 1;
        else if( nUpdFlg == 0 && nUpNFlg == 0 )
          return 2;
        else if( nUpdFlg == 0 && nUpNFlg >= 1 )
          return 3;
      }
    }
    retVal = -2;
    }catch(Exception e){retVal = -2;}
    finally
    {
      try
      {
        if(rs!=null)
          rs.close(); 
        if(stmt!=null)
          stmt.close(); 
        if(conn!=null)
         conn.close(); 
      }catch(Exception e){}
    }
    return retVal;
  }
}
