package ingen.html.db;

import ingen.html.util.*;
import java.sql.*;
import java.util.*;
import java.lang.*;

public class Trans
{

  public static String getTransID(String auditID,char type)
  {
     String retval=null; 
     try
     { 
       int auditid = Integer.parseInt(auditID.trim());
       retval = getTransID(auditid,type);
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retval;
  }
  
  public static String getTransID(int audit_id,char type)
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
       IngDate dt = new IngDate();
       int trans_id = Integer.parseInt(obj.getNextVal("S_Trans"));
       String query = "INSERT INTO T_Trans ( Trans_ID, Trans_Type, Trans_Status, Fk_Audit_ID, Time_Stamp ) " +    
                      "VALUES ("+trans_id+", '"+type+"', 'O','"+audit_id+"', '"+dt.toString()+"')";
       stmt.executeUpdate(query);
       stmt.close();
       conn.close();
       retval=Integer.toString(trans_id);
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       
  }

  public static String setTransID(String transid)
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
       String query = "UPDATE T_Trans " +
                      " SET  Trans_Status = 'F'"+    
                      "WHERE Trans_ID ="+Integer.parseInt(transid);
       stmt.executeUpdate(query);
       stmt.close();
       conn.close();
       retval= transid;
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       
  }

  public static String checkTransValidity(String transid)
  {
     String vTransType=null;
     String vTransStat=null;
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval="ERROR"; 
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       String query = "Select Trans_Type, Trans_Status " +
                      " From T_trans"+    
                      " WHERE Trans_ID = " + Integer.parseInt(transid) ;
       rs = stmt.executeQuery(query);
       if(rs.next()) 
       {
           vTransType = rs.getString(1);
           vTransStat = rs.getString(2);     
       } 
       stmt.close();
       conn.close();
       if( vTransType.equals("Q") )
          retval = "OK";
       else if( vTransType.equals("M"))
           {
              if( vTransStat.equals("O"))
                retval = "OK";
              else if( vTransStat.equals("F"))
                retval = "ERROR";
           }         
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       
  }

  public static String getRptTableID(String transid)
  {
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     String retval="ERROR"; 
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       stmt = conn.createStatement();
       String query = "Select RptTableID " +
                      "From T_trans"+    
                      "WHERE Trans_ID ="+Integer.parseInt(transid);
       rs = stmt.executeQuery(query);
       if(rs.next()) 
       {
           retval = rs.getString(1);
       } 
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       
  }

  public static String SetRptTableID( String transid, String rptTableId )
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
       String query = "UPDATE T_Trans " +
                      " SET  RptTableID = "+Integer.parseInt( rptTableId)+    
                      "WHERE Trans_ID ="+Integer.parseInt(transid);
       stmt.executeUpdate(query);
       stmt.close();
       conn.close();
       retval= transid;
     }catch(Exception sexe){System.out.println(sexe);}
     return retval;       
  }


}

