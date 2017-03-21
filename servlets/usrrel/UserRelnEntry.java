import ingen.html.*;
import ingen.html.character.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UserRelnEntry extends HttpServlet
{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
    throws IOException, ServletException
    {

      /*-------CHECK FOR ILLEGAL ENTRY---------*/
      response.setContentType("text/html");
      PrintWriter out = response.getWriter(); 
      CookieUtil PkCookie = new CookieUtil();
      String vPID = PkCookie.getCookie(request,"PID");
      if(vPID==null)
      {
        out.println(WebUtil.IllegalEntry());
        return;
      }
    
      int flag = 0;
 
      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;
      String rItmCls[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vBPLate1=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String query=null;
      String data=null;
      String vMembers="";
      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      String pvUserID = request.getParameter("pvUserID");
      String pvUserRel = request.getParameter("pvUserRel");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      User usr = new User();

      String vEmpName = db.getName(usr.getEmployeeIDForUser(pvUserID),"Employee");
      String vClientName = db.getName(usr.getClientIDForUser(pvUserID),"Client");

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID");
      nUserID   = Parse.GetValueFromString( vPID, "UserID");
      nLangID    = Parse.GetValueFromString( vPID, "LangID");
      nSchemeID  = Parse.GetValueFromString( vPID, "SchemeID");
      vImgOption = Parse.GetValueFromString( vPID, "Image");

      vFormType = cdata.GetConfigValue( "ST_USERREL", nLangID, "WD_FORM_INSERT","UserRel / I");                      

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_UserRel/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if( pvUserRel!= null && pvUserRel.equalsIgnoreCase("Client") )
        query =  " SELECT * FROM T_UserClient WHERE Fk_User_ID = "+pvUserID;
      else if( pvUserRel!= null && pvUserRel.equalsIgnoreCase("Employee") )
        query =  " SELECT * FROM T_UserEmployee WHERE Fk_User_ID = "+pvUserID;
      else if( pvUserRel!= null && pvUserRel.equalsIgnoreCase("Item") )
        query =  " SELECT * FROM T_UserItem WHERE Fk_User_ID = "+pvUserID;

      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();       
        rs = stmt.executeQuery(query);
        if( rs.next() )
        {
          flag = 1;
        } 
        rs.close();
        stmt.close();
        conn.close();
      }catch(SQLException sexe){ System.out.println(sexe); }


      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/UserRelnEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add(new FormHidden("pvUserID", pvUserID, null ));
      form.add(new FormHidden("pvUserRel", pvUserRel, null ));
      form.add(new FormHidden("Assign", null, null ));
      form.add(new FormHidden("Assign", null, null ));
      form.add(new FormHidden("DeAssign", null, null ));
      form.add(new FormHidden("DeAssign", null, null ));
      form.add(new FormHidden("vAction", null, null ));
      form.add(new NL());
      vBPLate = cdata.GetConfigValue("ST_USERREL", nLangID, "BL_LABEL.B_USERREL_USER","RelationShip of User");
      vBPLate1 = cdata.GetConfigValue("ST_USERREL", nLangID, "BL_LABEL.B_USERREL_RELATION_WITH","With");
      Center cen = new Center();
      Bold b = new Bold();
      b.add( vBPLate + util.getBlankSpaces(2) + db.getName( pvUserID,"User") + util.getBlankSpaces(2) + vBPLate1 + util.getBlankSpaces(2) +  pvUserRel );
      cen.add(b);
      form.add(cen);
      form.add(new NL()); 
      
      Table tab = new Table(null,"center","Border=\"0\" width=\"70%\" cols=\"4\"");
 
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_USERREL", nLangID, "BL_LABEL.B_EMPLOYEE_EMPLOYEE_NAME","Employee Name");
      row.add(new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"emp.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"20%\""));
      row.add(new TableCol( util.createLabelItem( vEmpName, vLabelAttrib ), null, null, null,"WIDTH=\"20%\"")); 
      vBPLate = cdata.GetConfigValue("ST_USERREL", nLangID, "BL_LABEL.B_CLIENT_CLIENT_NAME","Client Name"); 
      row.add(new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"client.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"20%\""));
      row.add(new TableCol( util.createLabelItem( vClientName, vLabelAttrib ), null, null, null,"WIDTH=\"20%\"")); 
      tab.add(row);  
      form.add(new NL());

      Table tab1 = new Table(null,"center","Border=\"0\" width=\"70%\" cols=\"2\"");

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_GROUP", "Groups assigned to User" );
      row1.add(new TableHeader( util.GetBoilerPlate(vImgOption, vImagePath+"assgrp.gif", vBPLate + util.getBlankSpaces(2) + db.getName( pvUserID,"User"), vLabelAttrib ),null, null, null, "WIDTH=\"55%\"")) ;

      query = "Select Fk_Group_ID From T_UserGroup Where  Fk_User_ID = "+ pvUserID;

      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();       
        rs = stmt.executeQuery(query);
        while( rs.next() )
        {
          vMembers += db.getName( rs.getInt(1), "Group")+", ";
        } 
      }catch(SQLException sexe){ System.out.println(sexe); }
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
        }catch(SQLException sexe){ System.out.println(sexe); }
      }

      row1.add(new TableCol( vMembers, null, null, null, null) );
      tab1.add(row1);

      form.add(tab);
      form.add(tab1);
      form.add(new NL());

      if( pvUserRel!= null && pvUserRel.equalsIgnoreCase("Client") )
      {
        Table tab2 = new Table(null,"center","Border=\"0\"");

        TableRow row2 = new TableRow("Left",null, "BGCOLOR=\"#666666\"");
        vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_LABEL.B_CLIENT_CLIENT_NAME", "Client Name" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"clnname.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_ASSIGN", "Assign" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"assign.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        if( flag > 0 )
        {
          vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_DEASSIGN", "DeAssign" );
          row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"deassgn.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        }
        vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_LABEL.B_CLIENT_CLIENT_DESC", "Client Desc" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"clndesc.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        tab2.add(row2);

        query = " Select cln.Client_ID, cln.Client_Name, cln.Client_Desc "+
                " From   T_Client cln "+
                " WHERE  cln.Client_ID NOT IN ( SELECT ucl.Fk_Client_ID "+
                "                               FROM   T_UserClient ucl "+
                "                               WHERE  ucl.Fk_User_ID = "+pvUserID+")";
        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();       
          rs = stmt.executeQuery(query);
          while( rs.next() )
          {
             TableRow row3 = new TableRow("Left",null, "BGCOLOR=\"#FFFFCA\"");
             row3.add(new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ), null, null, null, null)) ;
             row3.add(new TableCol( new FormCheckBox( "Assign", rs.getString(1), null, null) , "center", null, null, null)) ;
             if( flag > 0 )
               row3.add(new TableCol( util.createLabelItem( "N", vLabelAttrib ), "center", null, null, null)) ;
             data = rs.getString(3);
             if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
               data = "&nbsp;";
             row3.add(new TableCol( util.createLabelItem( data, vLabelAttrib), null, null, null, null));
             tab2.add(row3);
          } 
        }catch(SQLException sexe){ System.out.println(sexe); }
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
          }catch(SQLException sexe){ System.out.println(sexe); }
        }

        query = " Select cln.Client_ID, cln.Client_Name, cln.Client_Desc "+
                " From   T_UserClient ucl, T_Client cln "+
                " Where  ucl.Fk_Client_ID = cln.Client_ID "+
                " AND    ucl.Fk_User_ID   = "+pvUserID; 
        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();       
          rs = stmt.executeQuery(query);
          while( rs.next() )
          {
             TableRow row3 = new TableRow("Left",null, "BGCOLOR=\"#FFFFCA\"");
             row3.add(new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib), null, null, null, null)) ;
             row3.add(new TableCol( util.createLabelItem( "Y", vLabelAttrib), "center", null, null, null)) ;
             if( flag > 0 )
               row3.add(new TableCol( new FormCheckBox( "DeAssign", rs.getString(1), null, null) , "center", null, null, null)) ;
             data = rs.getString(3);
             if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
               data = "&nbsp;";
             row3.add(new TableCol( util.createLabelItem( data, vLabelAttrib ), null, null, null, null));
             tab2.add(row3);
          } 
        }catch(SQLException sexe){ System.out.println(sexe); }
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
          }catch(SQLException sexe){ System.out.println(sexe); }
        }
        form.add(tab2);
      }
      else if( pvUserRel!= null && pvUserRel.equalsIgnoreCase("Employee") )
      {
        Table tab2 = new Table(null,"center","Border=\"0\"");

        TableRow row2 = new TableRow("Left",null, "BGCOLOR=\"#666666\"");
        vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_EMPLOYEE_NAME", "Employee Name" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"empname.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_ASSIGN", "Assign" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"assign.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        if( flag > 0 )
        {
          vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_DEASSIGN", "DeAssign" );
          row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"deassgn.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        }
        vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_EMPLOYEE_DESC", "Employee Desc" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"empdesc.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        tab2.add(row2);

        query = " Select emp.Employee_ID, emp.Employee_Name, emp.Employee_Desc "+
                " From   T_Employee emp "+
                " WHERE  emp.Employee_ID NOT IN ( SELECT uem.Fk_Employee_ID "+
                "                                 FROM   T_UserEmployee uem "+
                "                                 WHERE  uem.Fk_User_ID = "+pvUserID+")";
        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();       
          rs = stmt.executeQuery(query);
          while( rs.next() )
          {
             TableRow row3 = new TableRow("Left",null, "BGCOLOR=\"#FFFFCA\"");
             row3.add(new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ), null, null, null, null)) ;
             row3.add(new TableCol( new FormCheckBox( "Assign", rs.getString(1), null, null) , "center", null, null, null)) ;
             if( flag > 0 )
               row3.add(new TableCol( util.createLabelItem( "N", vLabelAttrib ), "center", null, null, null)) ;
             data = rs.getString(3);
             if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
               data = "&nbsp;";
             row3.add(new TableCol( util.createLabelItem( data, vLabelAttrib ), null, null, null, null));
             tab2.add(row3);
          } 
        }catch(SQLException sexe){ System.out.println(sexe); }
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
          }catch(SQLException sexe){ System.out.println(sexe); }
        }

        query = " Select emp.Employee_ID, emp.Employee_Name, emp.Employee_Desc "+
                " From   T_UserEmployee uem, T_Employee emp "+
                " Where  uem.Fk_Employee_ID = emp.Employee_ID "+
                "  AND    uem.Fk_User_ID   = "+pvUserID;

        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();       
          rs = stmt.executeQuery(query);
          while( rs.next() )
          {
             TableRow row3 = new TableRow("Left",null, "BGCOLOR=\"#FFFFCA\"");
             row3.add(new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ), null, null, null, null)) ;
             row3.add(new TableCol( util.createLabelItem( "Y", vLabelAttrib ), "center", null, null, null)) ;
             if( flag > 0 )
               row3.add(new TableCol( new FormCheckBox( "DeAssign", rs.getString(1), null, null) , "center", null, null, null)) ;
             data = rs.getString(3);
             if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
               data = "&nbsp;";
             row3.add(new TableCol( util.createLabelItem( data, vLabelAttrib ), null, null, null, null));
             tab2.add(row3);
          } 
        }catch(SQLException sexe){ System.out.println(sexe); }
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
          }catch(SQLException sexe){ System.out.println(sexe); }
        }
        form.add(tab2);
      }
      else if( pvUserRel!= null && pvUserRel.equalsIgnoreCase("Item") )
      {
        Table tab2 = new Table(null,"center","Border=\"0\"");

        TableRow row2 = new TableRow("Left",null, "BGCOLOR=\"#666666\"");
        vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_NAME", "Item Name" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"itmname.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_ASSIGN", "Assign" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"assign.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        if( flag > 0 )
        {
          vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_DEASSIGN", "DeAssign" );
          row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"deassgn.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        }
        vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_DESC", "Item Desc" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"itmdesc.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEMCLASS_NAME", "ItemClass Name" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"itmcls.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEMGROUP_NAME", "ItemGroup Name" );
        row2.add(new TableHeader( util.GetBoilerPlate( vImgOption, vImagePath+"itmgrp.gif", vBPLate, "color=\"white\"" + vLabelAttrib ),null, null, null, null)) ;
        tab2.add(row2);

        query = " Select itm.item_ID, itm.item_name, itm.item_desc, itm.FK_ItemClass_ID, itm.FK_ItemGroup_ID "+
                " From   T_Item itm "+
                " WHERE  itm.Item_ID NOT IN ( SELECT uit.Fk_Item_ID "+
                "                             FROM   T_UserItem uit "+
                "                             WHERE  uit.Fk_User_ID = "+pvUserID +")";

        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();       
          rs = stmt.executeQuery(query);
          while( rs.next() )
          {
             TableRow row3 = new TableRow("Left",null, "BGCOLOR=\"#FFFFCA\"");
             row3.add(new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ), null, null, null, null)) ;
             row3.add(new TableCol( new FormCheckBox( "Assign", rs.getString(1), null, null) , "center", null, null, null)) ;
             if( flag > 0 )
               row3.add(new TableCol( util.createLabelItem( "N", vLabelAttrib ), "center", null, null, null)) ;
             data = rs.getString(3);
             if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
               data = "&nbsp;";
             row3.add(new TableCol( util.createLabelItem( data, vLabelAttrib ), null, null, null, null));
             data = db.getName( rs.getInt(4), "ItemClass" );
             if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
               data = "&nbsp;";
             row3.add(new TableCol( util.createLabelItem( data, vLabelAttrib ), null, null, null, null));
             data = db.getName( rs.getInt(5), "ItemGroup" );
             if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
               data = "&nbsp;";
             row3.add(new TableCol( util.createLabelItem( data, vLabelAttrib ), null, null, null, null));
             tab2.add(row3);
          } 
        }catch(SQLException sexe){ System.out.println(sexe); }
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
          }catch(SQLException sexe){ System.out.println(sexe); }
        }
        query = " Select itm.item_ID, itm.item_name, itm.item_desc, itm.FK_ItemClass_ID, itm.FK_ItemGroup_ID"+
                " From   T_UserItem uit, T_Item itm "+
                " Where  uit.Fk_Item_ID = itm.Item_ID "+
                " AND    uit.Fk_User_ID = "+pvUserID;

        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();       
          rs = stmt.executeQuery(query);
          while( rs.next() )
          {
             TableRow row3 = new TableRow("Left",null, "BGCOLOR=\"#FFFFCA\"");
             row3.add(new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ), null, null, null, null)) ;
             row3.add(new TableCol( util.createLabelItem( "Y", vLabelAttrib ), "center", null, null, null)) ;
             if( flag > 0 )
               row3.add(new TableCol( new FormCheckBox( "DeAssign", rs.getString(1), null, null) , "center", null, null, null)) ;
             data = rs.getString(3);
             if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
               data = "&nbsp;";
             row3.add(new TableCol( util.createLabelItem( data, vLabelAttrib ), null, null, null, null));
             data = db.getName( rs.getInt(4), "ItemClass" );
             if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
               data = "&nbsp;";
             row3.add(new TableCol( util.createLabelItem( data, vLabelAttrib ), null, null, null, null));
             data = db.getName( rs.getInt(5), "ItemGroup" );
             if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
               data = "&nbsp;";
             row3.add(new TableCol( util.createLabelItem( data, vLabelAttrib ), null, null, null, null));
             tab2.add(row3);
          } 
        }catch(SQLException sexe){ System.out.println(sexe); }
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
          }catch(SQLException sexe){ System.out.println(sexe); }
        }
        form.add(tab2);
      }

      body.add(form);
      head.add(scr);
      page.add(head);
      page.add(body);
      out.println(page);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
    throws IOException, ServletException
    {
       /*-------CHECK FOR ILLEGAL ENTRY---------*/
       response.setContentType("text/html");
       PrintWriter out = response.getWriter(); 
       CookieUtil PkCookie = new CookieUtil();
       String vPID = PkCookie.getCookie(request,"PID");
       if(vPID==null)
       {
         out.println(WebUtil.IllegalEntry());
         return;
       }

      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      int nMsgID=-1;
      
      String errMsg=null;  
      String nTransID=null;
      String vStatus=null;
      String temp=null;
      String mode="";
      String query = null;

      String pnTransID = request.getParameter("pnTransID");
      String pvUserID = request.getParameter("pvUserID");
      String pvUserRel = request.getParameter("pvUserRel");
      String Assign[] = request.getParameterValues("Assign");
      String DeAssign[] = request.getParameterValues("DeAssign");
      String vAction = request.getParameter("vAction");
      String vIDArray[]=null;

      Statement stmt = null;
      PreparedStatement pstmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
      Message msg = new Message(); 

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID  = Parse.GetValueFromString( vPID, "LangID" );

      String usr = db.getName( nUserID, "User" );
      vStatus = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( !(DeAssign[0].trim()==null || DeAssign[0].trim().equals("") || DeAssign[0].trim().equalsIgnoreCase("null") ) )
        { 
          try
          {
              conn = db.GetDBConnection();
              vIDArray=Parse.parse(DeAssign[0],"~");
              if( pvUserRel!=null && pvUserRel.equalsIgnoreCase("Client") )
                query = " Delete From T_UserClient Where Fk_User_ID = "+pvUserID+
                        "  AND    Fk_Client_ID = ?";
              if( pvUserRel!=null && pvUserRel.equalsIgnoreCase("Employee") )
                query = " Delete From T_UserEmployee Where Fk_User_ID = "+pvUserID+
                        "  AND    Fk_Employee_ID = ?";
              if( pvUserRel!=null && pvUserRel.equalsIgnoreCase("Item") )
                query = " Delete From T_UserItem Where Fk_User_ID = "+pvUserID+
                        "  AND    Fk_Item_ID = ?";
              pstmt = conn.prepareStatement(query);
              for(int i=0;i<vIDArray.length;i++)
              {  
                String id = vIDArray[i];
                pstmt.setInt(1,Integer.parseInt(id));
                pstmt.executeUpdate();
              }
              nMsgID = 4;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=7;}
          finally
          {
            try
            {
              if(pstmt!=null)
                pstmt.close();
              if(conn!=null)
                conn.close();
            }catch(SQLException sexe){ System.out.println(sexe); }
          }
        }
        if( !(Assign[0].trim()==null || Assign[0].trim().equals("") || Assign[0].trim().equalsIgnoreCase("null") ) )
        { 
           try
           {
              conn = db.GetDBConnection();
              vIDArray=Parse.parse(Assign[0],"~");
              if( pvUserRel!=null && pvUserRel.equalsIgnoreCase("Client") )
                query = " INSERT INTO T_UserClient( Fk_User_ID, Fk_Client_ID)"+
                        " VALUES ( "+pvUserID+" , ?)";
              if( pvUserRel!=null && pvUserRel.equalsIgnoreCase("Employee") )
                query = " INSERT INTO T_UserEmployee( Fk_User_ID, Fk_Employee_ID)"+
                        " VALUES ( "+pvUserID+" , ?)";
              if( pvUserRel!=null && pvUserRel.equalsIgnoreCase("Item") )
                query = " INSERT INTO T_UserItem( Fk_User_ID, Fk_Item_ID)"+
                        " VALUES ( "+pvUserID+" , ?)";
              pstmt = conn.prepareStatement(query);
              for(int i=0;i<vIDArray.length;i++)
              {  
                String id = vIDArray[i];
                pstmt.setInt(1,Integer.parseInt(id));
                pstmt.executeUpdate();
              }
              nMsgID = 3;
           }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
           finally
           {
             try
             {
               if(pstmt!=null)
                 pstmt.close();
               if(conn!=null)
                 conn.close();
             }catch(SQLException sexe){ System.out.println(sexe); }
           }
           Trans.setTransID(pnTransID);
        }
      } 
      if( nMsgID <= 5 )
         response.sendRedirect("/JOrder/servlets/UserRelnFrame?pvMode=D&pvUserID=&pvUserRel=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
      else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}
