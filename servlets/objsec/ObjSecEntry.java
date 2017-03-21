import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import ingen.html.character.*;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ObjSecEntry extends HttpServlet
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

      int nCount=0;

      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;
      String vImagePath=null;
      String vBPLate=null;
      String tmp=null;
      String flg = "&nbsp;";
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vMembers = "";
      String vSec = "";
      String query = null;
      String objName = null;
      String cDMSecLevel  = null;

      String vDispStatus  = "CHECKED";
      String vInsrStatus  = "CHECKED";
      String vUpdtStatus  = "CHECKED";
      String vUpdNStatus  = "CHECKED";
      String vDeltStatus  = "CHECKED";

      String pvMode = request.getParameter("pvMode");
      String pvSecLevel = request.getParameter("pvSecLevel");
      String pnRefID = request.getParameter("pnRefID");
      String pvParentObj = request.getParameter("pvParentObj");

      DBConnect db = new DBConnect();
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;

      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "WD_FORM_INSERT", "Object Security / I" );
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "WD_FORM_UPDATE", "Object Security / U" );
     
      nTransID = Trans.getTransID( nAuditID, 'M');
    
      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ObjSec/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ObjSecEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add( new FormHidden("pnTransID", nTransID, null ) );
      body.add( new NL(2) );

      query = " SELECT COUNT(*)"+
              " FROM   T_ObjSec obs"+
              " WHERE  obs.ObjParent_Name = '"+pvParentObj+"'"+
              " AND    obs.DM_SecLevel = '"+pvSecLevel+"'"+
              " AND    obs.Ref_ID = "+pnRefID;
      try
      {
         conn = db.GetDBConnection();
         stmt = conn.createStatement();         
         rs = stmt.executeQuery(query);
         while(rs.next())
         {
            nCount = rs.getInt(1);
         }
      } catch(Exception e) {}
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
        }catch(SQLException sexe){}
      }

      if( pvSecLevel!=null && pvSecLevel.equalsIgnoreCase("G") )
      {
         vSec = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_USERS", "Users" )+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
         query = " SELECT usr.User_Name, usr.User_ID " +
                 " FROM   T_User usr, T_UserGroup usg "+
                 " WHERE  usr.User_ID = usg.Fk_User_ID "+
                 " AND    usg.Fk_Group_ID = "+ pnRefID;
         try
         {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next())
            {
              vMembers += rs.getString(1)+ ", "; 
            }
          }catch(Exception e) { }
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
           }catch(SQLException sexe){}
         }
      }
      else if( pvSecLevel!=null && pvSecLevel.equalsIgnoreCase("U") )
      {
         vSec = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_GROUPS", "Groups" ) +"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
         query = " SELECT grp.Group_Name, grp.Group_ID "+
                 " FROM   T_Group grp, T_UserGroup usg "+
                 " WHERE  grp.Group_ID = usg.Fk_Group_ID "+
                 " AND    usg.Fk_User_ID = "+ pnRefID;
         try
         {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next())
            {
              vMembers += rs.getString(1)+ ", "; 
            }
            stmt.close();
            conn.close();
           }catch(Exception sexe){}
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
           }catch(SQLException sexe){}
         }
      } 

      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_SEC_LEVEL", "Secuirty Level" );
      Para p = new Para();
      Bold b = new Bold();
      Bold b1 = new Bold();
      b.add( util.createLabelItem( vBPLate, vLabelAttrib ) );
      b1.add( util.createLabelItem( vSec, vLabelAttrib ) );
      Center cen = new Center();
      p.add( b + util.getBlankSpaces(5)+ util.createLabelItem( Domain.getDomainDescFrmAttrib( "SECLEVEL", pvSecLevel, nLangID), vLabelAttrib ) + util.getBlankSpaces(8)+  b1 + util.createLabelItem( vMembers, vLabelAttrib) );

      Table tab = new Table("1","center","Border=\"0\" width=\"40%\" ");

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_OBJPARENTNAME", "Parent Object" );
      row1.add( new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"parobj.gif", vBPLate, vLabelAttrib ),null, null, null, null) );
      row1.add( new TableCol( util.createLabelItem( pvParentObj, vLabelAttrib) , null, null, null,null) );
      tab.add(row1);
      form.add( new FormHidden("vObjParent_Name", pvParentObj, null ) );
      form.add( new FormHidden( "cDM_SecLevel", pvSecLevel, null ) );
      form.add( new FormHidden( "nRef_ID", pnRefID, null ) );
      form.add( new FormHidden( "nDisplay_Flg", null, null ) );
      form.add( new FormHidden( "nInsert_Flg", null, null ) );
      form.add( new FormHidden( "nUpdate_Flg", null, null ) );
      form.add( new FormHidden( "nUpdNull_Flg", null, null ) );

      Table tab1 = new Table("1","center","Border=\"0\" width=\"90%\" BGCOLOR=\"#FFFFCA\"");

      TableRow row2 = new TableRow("Left",null,"BGCOLOR=\"#666666\"");
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_DM_OBJTYPE", "Object Type" );
      row2.add(new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"Type.gif", vBPLate, "COLOR=\"White\""+vLabelAttrib ),null, null, null, null));
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_OBJNAME", "Object Name" );
      row2.add(new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"Name.gif", vBPLate, "COLOR=\"White\""+vLabelAttrib ),null, null, null, null));
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_OBJDESC", "Object Desc" );
      row2.add(new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, "COLOR=\"White\""+vLabelAttrib ),null, null, null, null));
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_DISPLAY", "Display" );
      row2.add(new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"Display.gif", vBPLate, "COLOR=\"White\""+vLabelAttrib ),null, null, null, null));
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_INSERT", "Insert" );
      row2.add(new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"Insert.gif", vBPLate, "COLOR=\"White\""+vLabelAttrib ),null, null, null, null));
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_UPDATE", "Update" );
      row2.add(new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"Update.gif", vBPLate, "COLOR=\"White\""+vLabelAttrib ),null, null, null, null));
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_UPDNULL", "UpdNull" );
      row2.add(new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"UpdNull.gif", vBPLate, "COLOR=\"White\""+vLabelAttrib ),null, null, null, null));

      if( pvSecLevel!=null && pvSecLevel.equalsIgnoreCase("G") )
      {
         vSec = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_USERS", "Users" )+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
         query = " SELECT usr.User_Name, usr.User_ID " +
                 " FROM   T_User usr, T_UserGroup usg "+
                 " WHERE  usr.User_ID = usg.Fk_User_ID "+
                 " AND    usg.Fk_Group_ID = "+ pnRefID;
         try
         {
            Font f = new Font("White", "Arial", "3", null);
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next())
            {
              TableHeader head8 = new TableHeader( util.createLabelItem( rs.getString(1), vLabelAttrib), null, null, null, null);
              head8.setFormat(f); 
              row2.add(head8);
            }
            stmt.close();
            conn.close();
         }catch(Exception e) {} 
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
          }catch(SQLException sexe){}
        }
      }
      else if( pvSecLevel!=null && pvSecLevel.equalsIgnoreCase("U") )
      {
         vSec = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_GROUPS", "Groups" ) +"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
         query = " SELECT grp.Group_Name, grp.Group_ID "+
                 " FROM   T_Group grp, T_UserGroup usg "+
                 " WHERE  grp.Group_ID = usg.Fk_Group_ID "+
                 " AND    usg.Fk_User_ID = "+ pnRefID;
         try
         {
            Font f = new Font("White", "Arial", "3", null);
            conn = db.GetDBConnection();
            stmt = conn.createStatement();            
            rs = stmt.executeQuery(query);
            while(rs.next())
            {
              TableHeader head8 = new TableHeader( util.createLabelItem( rs.getString(1), vLabelAttrib ), null, null, null, null);
              head8.setFormat(f); 
              row2.add(head8);
            }
            stmt.close();
            conn.close();
         }catch(Exception e) { }
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
          }catch(SQLException sexe){}
        }
      }

      tab1.add(row2);

      try
      {
         conn = db.GetDBConnection();
         stmt = conn.createStatement();
         ResultSet rs1 = null; 
         query = " SELECT *"+
                 " FROM   T_ObjSecRef osr "+
                 " WHERE  osr.ObjParent_Name = '"+ pvParentObj + "'" +
                 " ORDER BY osr.Obj_Name";
         rs1 = stmt.executeQuery(query);
         while(rs1.next())
         {
           TableRow row3 = new TableRow("Left",null,null);
           String objtype = rs1.getString(4);
           tmp = Domain.getDomainDescFrmAttrib( "OBJTYPE", objtype, nLangID);
           TableCol col3 = new TableCol( util.createLabelItem( tmp, vLabelAttrib ),null, null, null, null);
           col3.add( new FormHidden("cDM_ObjType", objtype ,null));
           objName = rs1.getString(2);
           TableCol col4 = new TableCol( util.createLabelItem( objName, vLabelAttrib ),null, null, null, null);
           col4.add( new FormHidden( "vObj_Name", objName, null));
           tmp = rs1.getString(3);
           TableCol col5 = new TableCol( util.createLabelItem( tmp, vLabelAttrib),null, null, null, null);
           col5.add( new FormHidden( "vObj_Desc", tmp, null));
           row3.add( col3 );
           row3.add( col4 );
           row3.add( col5 );
           query =  " SELECT * "+
                    " FROM   T_ObjSec obs "+
                    " WHERE  obs.ObjParent_Name = '"+pvParentObj+"'"+
                    " AND    obs.Obj_Name = '"+ objName + "'"+
                    " AND    obs.DM_SecLevel = '"+pvSecLevel+"'"+
                    " AND    obs.Ref_ID = "+pnRefID;
           ResultSet rs2 = stmt.executeQuery(query);
           while(rs2.next())
           {
             int i = rs2.getInt(8);
             if( i == 0 )
                vDispStatus = null;
             else if( i == 1 )
                vDispStatus = "CHECKED";
             i = rs2.getInt(9);
             if( i == 0 )
                vInsrStatus = null;
             else if( i == 1 )
                vInsrStatus = "CHECKED";              
             i = rs2.getInt(10);
             if( i == 0 )
                vUpdtStatus = null;
             else if( i == 1 )
                vUpdtStatus = "CHECKED";
             i = rs2.getInt(11);
             if( i == 0 )
                vUpdNStatus = null;
             else if( i == 1 )
                vUpdNStatus = "CHECKED";
          }
          if( objtype != null && objtype.equalsIgnoreCase("K") )
          {
             TableCol c1 = new TableCol( new FormCheckBox("nDisplay_Flg","1",vDispStatus,null),"Center", null, null, null);
             TableCol c2 = new TableCol( util.createLabelItem( "NA", vLabelAttrib ),"Center", null, null, null);
             c2.add( new FormHidden( "nInsert_Flg", "1", null) );
             TableCol c3 = new TableCol( util.createLabelItem( "NA", vLabelAttrib ),"Center", null, null, null);
             c3.add( new FormHidden( "nUpdate_Flg", "1", null) );
             TableCol c4 = new TableCol( util.createLabelItem( "NA", vLabelAttrib ),"Center", null, null, null);
             c4.add( new FormHidden( "nUpdNull_Flg", "1", null) );
             row3.add( c1 );
             row3.add( c2 );
             row3.add( c3 );
             row3.add( c4 );
          }
          else
          {
             TableCol c1 = new TableCol( new FormCheckBox( "nDisplay_Flg", "1", vDispStatus, null),"Center", null, null, null);
             TableCol c2 = new TableCol( new FormCheckBox( "nInsert_Flg", "1", vInsrStatus, null),"Center", null, null, null);
             TableCol c3 = new TableCol( new FormCheckBox( "nUpdate_Flg", "1", vUpdtStatus, null),"Center", null, null, null);
             TableCol c4 = new TableCol( new FormCheckBox( "nUpdNull_Flg", "1", vUpdNStatus, null),"Center", null, null, null);
             row3.add( c1 );
             row3.add( c2 );
             row3.add( c3 );
             row3.add( c4 );
           }

           if( pvSecLevel!= null && pvSecLevel.equalsIgnoreCase( "U" ) )
           { 
              query = " SELECT obs.Display_Flg, obs.Insert_Flg, obs.updatetime_flg, obs.UpdNull_Flg "+
                      " FROM   T_ObjSec obs "+
                      " WHERE  obs.ObjParent_Name = '"+pvParentObj +"'"+
                      " AND    obs.Obj_Name = '"+objName+"'"+
                      " AND    obs.DM_SecLevel = 'G' "+
                      " AND    obs.Ref_ID IN ( SELECT usg.FK_Group_ID "+
                      "                        FROM   T_UserGroup usg "+
                      "                        WHERE  usg.FK_User_ID = "+pnRefID+")";
              ResultSet rs3 = stmt.executeQuery(query);
              while(rs3.next())
              {
                flg = rs3.getString(1)+"/"+rs3.getString(2)+"/"+rs3.getString(3)+"/"+rs3.getString(4);
                TableCol c1 = new TableCol( util.createLabelItem( flg, vLabelAttrib ), "Center", null, null, null);
                row3.add( c1 );
              }

           }
           else if( pvSecLevel!= null && pvSecLevel.equalsIgnoreCase( "G" ) )
           {
              query = " SELECT obs.Display_Flg, obs.Insert_Flg, obs.updatetime_flg, obs.UpdNull_Flg "+
                      " FROM   T_ObjSec obs "+
                      " WHERE  obs.ObjParent_Name = '"+pvParentObj +"'"+
                      " AND    obs.Obj_Name = '"+objName+"'"+
                      " AND    obs.DM_SecLevel = 'U' "+
                      " AND    obs.Ref_ID IN ( SELECT usg.Fk_User_ID "+
                      "                        FROM   T_UserGroup usg "+
                      "                        WHERE  usg.FK_Group_ID = "+pnRefID+")";
              ResultSet rs3 = stmt.executeQuery(query);
              while(rs3.next())
              {
                flg = rs3.getString(1)+"/"+rs3.getString(2)+"/"+rs3.getString(3)+"/"+rs3.getString(4);
                TableCol c1 = new TableCol( util.createLabelItem( flg, vLabelAttrib), "Center", null, null, null);
                row3.add( c1 );
              }
           }
           tab1.add(row3);
         }
         stmt.close();
         conn.close();
      }catch(Exception e) { }
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
      form.add(new FormHidden( "pnCount", Integer.toString(nCount), null ));
      form.add(new FormHidden( "vAction", null, null ));
      form.add( tab1 );
      cen.add( p ); 
      body.add( cen );
      body.add(tab);   
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

      int nMsgID=-1;

      String nLangID=null;
      String nUserID=null;
      String errMsg=null; 
      String vStatus=null;
      String query = null;

      String pnTransID = request.getParameter("pnTransID");
      String vObjParent_Name = request.getParameter("vObjParent_Name");
      String cDM_SecLevel = request.getParameter("cDM_SecLevel");
      String nRef_ID = request.getParameter("nRef_ID");
      String cDM_ObjType[] = request.getParameterValues("cDM_ObjType");
      String vObj_Name[] = request.getParameterValues("vObj_Name");
      String vObj_Desc[] = request.getParameterValues("vObj_Desc");
      String nDisplay_Flg[] = request.getParameterValues("nDisplay_Flg");
      String nInsert_Flg[] = request.getParameterValues("nInsert_Flg");
      String nUpdate_Flg[] = request.getParameterValues("nUpdate_Flg");
      String nUpdNull_Flg[] = request.getParameterValues("nUpdNull_Flg");
      int pnCount = Integer.parseInt( request.getParameter("pnCount") );
      String vAction = request.getParameter("vAction"); 

      PreparedStatement pstmt = null;
      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();
  
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String usr = db.getName( nUserID, "User" );      
      vStatus = Trans.checkTransValidity( pnTransID );
      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction.equalsIgnoreCase("Delete") )
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            query = " DELETE FROM T_ObjSec "+
                    " WHERE  ObjParent_Name = '"+vObjParent_Name+"'";
            stmt.executeUpdate(query);
            nMsgID = 4;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=7;}
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
          Trans.setTransID(pnTransID);
        }
        else if( vAction.equalsIgnoreCase("Insert") || vAction.equalsIgnoreCase("SaveInsert") )
        {
          try
          {
            conn = db.GetDBConnection();
            query = " INSERT INTO T_ObjSec( ObjSec_ID, ObjParent_Name, Obj_Name, Obj_Desc, "+
                    "         DM_ObjType, DM_SecLevel, Ref_ID, Display_Flg, Insert_Flg, "+
                    "         UpdateTime_Flg, UpdNull_Flg )"+
                    " VALUES( ?,"+val.IsNull(vObjParent_Name)+", ?, ? ,"+
                    "         ?,"+val.IsNull(cDM_SecLevel)+","+nRef_ID+","+
                    "         ?,?,?,?)";
            pstmt = conn.prepareStatement(query);
            for( int i = 0; i< pnCount; i++ )
            {
               int objsec_id = Integer.parseInt(db.getNextVal("S_ObjSec"));
               int dis = Integer.parseInt(Parse.GetSubString( nDisplay_Flg[0], "~", i ).trim());
               int ins = Integer.parseInt(Parse.GetSubString( nInsert_Flg[0], "~", i ).trim());
               int upd = Integer.parseInt(Parse.GetSubString( nUpdate_Flg[0], "~", i ).trim());
               int updn = Integer.parseInt(Parse.GetSubString( nUpdNull_Flg[0], "~", i ).trim());               
               pstmt.setInt( 1, objsec_id);
               pstmt.setString( 2, vObj_Name[i]);
               pstmt.setString( 3, vObj_Desc[i]);
               pstmt.setString( 4, cDM_ObjType[i]);
               pstmt.setInt( 5, dis );
               pstmt.setInt( 6, ins );
               pstmt.setInt( 7, upd );
               pstmt.setInt( 8, updn );
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
            }catch(SQLException sexe){}
          }
          Trans.setTransID(pnTransID);
        }
        else if( vAction.equalsIgnoreCase("Update"))
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            for( int i = 0; i< pnCount; i++ )
            {
              int dis = Integer.parseInt(Parse.GetSubString( nDisplay_Flg[0], "~", i ).trim());
              int ins = Integer.parseInt(Parse.GetSubString( nInsert_Flg[0], "~", i ).trim());
              int upd = Integer.parseInt(Parse.GetSubString( nUpdate_Flg[0], "~", i ).trim());
              int updn = Integer.parseInt(Parse.GetSubString( nUpdNull_Flg[0], "~", i ).trim());               
              query = " UPDATE T_ObjSec "+
                      " SET Display_Flg = "+dis+","+
                      "     Insert_Flg  = "+ins+","+
                      "     UpdateTime_Flg  = "+upd+","+
                      "     UpdNull_Flg = "+updn+
                      " WHERE Obj_Name = '"+vObj_Name[i]+"'"+
                      " AND   ObjParent_Name = '"+vObjParent_Name+"'"+
                      " AND   DM_SecLevel = '"+cDM_SecLevel+"'"+
                      " AND   Ref_ID = "+nRef_ID;
               stmt.executeUpdate(query);
             }
             nMsgID = 5;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=8;}
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
          Trans.setTransID(pnTransID);
        }
     }
     if( nMsgID <= 5 )
       response.sendRedirect("/JOrder/servlets/ObjSecFrame?pvMode=N&pvParentObj="+vObjParent_Name+"&pvObjType=&pvSecLevel="+cDM_SecLevel+"&pnRefID="+nRef_ID+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}
