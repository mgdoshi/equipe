import ingen.html.*;
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

public class UserEntry extends HttpServlet
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
      String rUser[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vLangQry = null;
      String vClientQry = null;
      String vRecSecQry = null;
      String vEmployeeQry = null;
      String vSchemeQry = null;
      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      String pvMode = request.getParameter("pvMode");
      String pnUserID = request.getParameter("pnUserID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "SY_USER", nLangID, "WD_FORM_INSERT", "User / I" );
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "SY_USER", nLangID, "WD_FORM_UPDATE", "User / U" );

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_User/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vLangQry     = "Select Lang_ID, Lang_Desc From T_Lang";
      vClientQry   = "Select Client_ID, Client_Name From T_Client";
      vRecSecQry   = "Select RecSec_ID, RecSec_Name From T_RecSec";
      vEmployeeQry = "Select Employee_ID, Employee_Name From T_Employee";
      vSchemeQry   = "Select Scheme_ID, Scheme_Name From T_Scheme";

      if(pnUserID!=null && !pnUserID.equals("") && !pnUserID.equalsIgnoreCase("null"))
        rUser = db.getRecord(pnUserID,"User");
      else
        rUser = new String[11];
 
      Page page = new Page();
      Head head = new Head(); 

      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/UserEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add(new FormHidden("pnUserID", pnUserID, null ));
      form.add(new NL(2));

      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_USER_NAME", "User Name" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"usrname.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"30%\"");
      col.add(WebUtil.NotNull);
      TableCol col1 = null;
      if( pvMode!=null && pvMode.equalsIgnoreCase("U") )
      {
        col1 = new TableCol( util.createLabelItem( rUser[1], vLabelAttrib),null,null,null,null);
        FormHidden hid1 = new FormHidden("vUser_Name", rUser[1], null);
        form.add( hid1 );
      } 
      else            
        col1 = new TableCol(util.createTextItem( nUserID, "SY_USER", "vUser_Name", pvMode, "15", "30", rUser[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,null);
      row.add(col) ;
      row.add(col1);
      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_USER_PASS", "User PassWord" );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Pass.gif", vBPLate, vLabelAttrib ),null, null, null,null); 
      col2.add(WebUtil.NotNull);
      TableCol col3 = new TableCol(util.createPasswdItem( nUserID, "SY_USER", "vUser_Pass", pvMode, "15", "30",rUser[2], null, vTextAttrib),null, null, null,null); 
      row1.add(col2) ;
      row1.add(col3);
      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_CONFIRM_PASSWD", "Confirm PassWord" );
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"CPass.gif", vBPLate, vLabelAttrib ),null, null, null,null); 
      col4.add(WebUtil.NotNull);
      TableCol col5 = new TableCol(util.createPasswdItem( nUserID, "SY_USER", "vUser_ConfPass", pvMode, "15", "30",rUser[2], null, vTextAttrib),null, null, null,null);
      row2.add(col4);
      row2.add(col5);
      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_USER_DESC", "Description" );
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"usrdesc.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "SY_USER", "vUser_Desc", pvMode, "30", "100", rUser[3], null, vTextAttrib),null,null,null,null);
      row3.add(col6);
      row3.add(col7);
      TableRow row4 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_FK_LANG_ID", "Language" );
      TableCol col8 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"lang.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col8.add(WebUtil.NotNull);
      HtmlTag sel =  util.createList( nUserID, "SY_USER", "nFk_Lang_ID", pvMode, vLangQry, rUser[4], null, vListAttrib);
      TableCol col9 = new TableCol( sel, null, null, null,null); 
      row4.add(col8);
      row4.add(col9);
      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_FK_CLIENT_ID", "Client Name" );
      TableCol col10 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"client.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col10.add(WebUtil.NotNull);
      HtmlTag sel1 =  util.createList( nUserID, "SY_USER", "nFk_Client_ID", pvMode, vClientQry, rUser[5], null, vListAttrib);
      TableCol col11 = new TableCol( sel1, null, null, null,null); 
      row5.add(col10);
      row5.add(col11);
      TableRow row6 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_FK_RECSEC_ID", "Record Security Name" );
      TableCol col12 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"recsec.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col12.add(WebUtil.NotNull);
      HtmlTag sel2 =  util.createList( nUserID, "SY_USER", "nFk_RecSec_ID", pvMode, vRecSecQry, rUser[6], null, vListAttrib);
      TableCol col13 = new TableCol( sel2, null, null, null,null); 
      row6.add(col12);
      row6.add(col13);
      TableRow row7 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_FK_EMPLOYEE_ID", "Employee Name" );
      TableCol col14 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"emp.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel3 =  util.createList( nUserID, "SY_USER", "nFk_Employee_ID", pvMode, vEmployeeQry, rUser[7], null, vListAttrib);
      TableCol col15 = new TableCol( sel3, null, null, null,null); 
      row7.add(col14);
      row7.add(col15);
      TableRow row8 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_FK_SCHEME_ID", "Scheme Name" );
      TableCol col16 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"scheme.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel4 =  util.createList( nUserID, "SY_USER", "nFk_Scheme_ID", pvMode, vSchemeQry, rUser[8], null, vListAttrib);
      TableCol col17 = new TableCol( sel4, null, null, null,null); 
      row8.add(col16);
      row8.add(col17);
      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row4);
      tab.add(row5);
      tab.add(row6);
      tab.add(row7);
      tab.add(row8);
      form.add(tab);
      FormHidden group = new FormHidden("nFk_Group_ID", null, null);
      FormHidden del = new FormHidden("nDelete", null, null);
      FormHidden action = new FormHidden("vAction",null, null);
      form.add(group);
      form.add(del);
      form.add(action);
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

      String errMsg=null;
      String vStatus=null;
      String query = null;
      String nUserID=null;
      String nLangID=null;
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String pnTransID = request.getParameter("pnTransID");
      String pnUserID = request.getParameter("pnUserID");
      String vUserName = Parse.formatStr(request.getParameter("vUser_Name"));
      String vUserPass = Parse.formatStr(request.getParameter("vUser_Pass"));
      String vUserConfPass = Parse.formatStr(request.getParameter("vUser_ConfPass"));
      String vUserDesc = Parse.formatStr(request.getParameter("vUser_Desc"));
      String vLangID = request.getParameter("nFk_Lang_ID");
      String vClientID = request.getParameter("nFk_Client_ID");
      String vRecSecID = request.getParameter("nFk_RecSec_ID");
      String vEmployeeID = request.getParameter("nFk_Employee_ID");
      String vSchemeID = request.getParameter("nFk_Scheme_ID");
      String vAction = request.getParameter("vAction"); 

      String vIDArray[]=null;
      String vGroup=null;
      String vDelete=null;

      DBConnect db = new DBConnect();
      Statement stmt = null;
      PreparedStatement pstmt = null;
      Connection conn = null;
      ResultSet rs = null;
      Valid val = new Valid();
      Message msg = new Message(); 
      IngDate dt = new IngDate();
  
      String usr = db.getName( nUserID, "User" );      
      vStatus = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction.equalsIgnoreCase("Insert") || vAction.equalsIgnoreCase("SaveInsert") )
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            int user_id = Integer.parseInt(db.getNextVal("S_User"));
            query = " INSERT INTO T_User( User_ID, User_Name, User_Pass, User_Desc, "+
                    " FK_Lang_ID, FK_Client_ID, Fk_RecSec_ID, Fk_Employee_ID, "+
                    " Modifier, Change_Dt, Fk_Scheme_ID ) "+
                    " VALUES ("+user_id+","+val.IsNull(vUserName)+","+val.IsNull(vUserPass)+","+val.IsNull(vUserDesc)+"," +
                    " "+val.IsNull(vLangID)+","+val.IsNull(vClientID)+","+val.IsNull(vRecSecID)+","+val.IsNull(vEmployeeID)+",'"+usr+"', '"+dt+"',"+val.IsNull(vSchemeID)+")";
            stmt.executeUpdate(query);
            stmt.close();

            query = " INSERT INTO T_USERCLIENT "+
                    " ( Fk_User_ID, Fk_Client_ID) "+
                    " VALUES  ( "+user_id+","+vClientID+")";
            stmt.executeUpdate(query);
            stmt.close();

            if( !(vEmployeeID == null || vEmployeeID.equals("") || vEmployeeID.equalsIgnoreCase("null") ) )
            { 
               query = " INSERT INTO T_USEREMPLOYEE "+
                       " ( Fk_User_ID, Fk_Employee_ID) "+
                       " VALUES ( "+user_id+","+vEmployeeID+")";
               stmt.executeUpdate(query);
               stmt.close();
            }

            vGroup=request.getParameter("nFk_Group_ID");
            if( !(vGroup.trim()==null || vGroup.trim().equals("") || vGroup.trim().equalsIgnoreCase("null") ) )
            {
              vIDArray=Parse.parse(vGroup,"~");
              query = " INSERT INTO T_UserGroup( UserGroup_ID, Fk_Group_ID, Fk_User_ID,"+
                      "  Modifier, Change_Dt) "+
                      "  VALUES( ?, ?, ?, ?, '"+dt+"') ";
              out.println( vIDArray[0] );
              pstmt = conn.prepareStatement(query);
              for(int i=0;i<vIDArray.length;i++)
              {  
                out.println( vIDArray[0] );
                String bid = vIDArray[i];
                out.println( vIDArray[0] );
                int usrgrp_id = Integer.parseInt(db.getNextVal("S_UserGroup"));
                pstmt.setInt(1,usrgrp_id);
                pstmt.setString(2,bid);
                pstmt.setInt(3,user_id);
                pstmt.setString(4,usr);
                pstmt.executeUpdate();
              }
              pstmt.close();
            } 
            nMsgID = 3;
         }catch(Exception sexe){errMsg=sexe.getMessage();nMsgID=6;}
         finally
         {
           try
           {
             if(pstmt!=null)
               pstmt.close();
             if(stmt!=null)
               stmt.close();
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
            query = " UPDATE T_User SET User_Name = "+val.IsNull(vUserName)+"," +
                   " User_Pass = "+val.IsNull(vUserPass)+"," +
                   " User_Desc = "+val.IsNull(vUserDesc)+"," +
                   " Fk_Lang_ID = "+val.IsNull(vLangID)+"," +
                   " Fk_Client_ID = "+val.IsNull(vClientID)+"," +
                   " Fk_RecSec_ID = "+val.IsNull(vRecSecID)+"," +
                   " Fk_Employee_ID = "+val.IsNull(vEmployeeID)+"," +
                   " Fk_Scheme_ID = "+val.IsNull(vSchemeID)+"," +
                   " Modifier    = '"+usr+"'," + 
                   " Change_Dt   = '"+dt+"'" + 
                   " WHERE User_ID = "+pnUserID;
            stmt.executeUpdate(query);
            stmt.close();

            vGroup=request.getParameter("nFk_Group_ID");
            vDelete=request.getParameter("nDelete");

            if( !(vDelete.trim()==null || vDelete.trim().equals("") || vDelete.trim().equalsIgnoreCase("null") ) )
            {
              vIDArray=Parse.parse(vDelete,"~");
              query = " DELETE FROM T_UserGroup "+
                      " WHERE  Fk_Group_ID = ?"+
                      " AND    Fk_User_ID  = ?";
              pstmt = conn.prepareStatement(query);
              for(int i=0;i<vIDArray.length;i++)
              {  
                String bid = vIDArray[i];
                pstmt.setInt(1,Integer.parseInt(bid));
                pstmt.setString(2,pnUserID);
                pstmt.executeUpdate();
              }
              pstmt.close();
            }

            if( !(vGroup.trim()==null || vGroup.trim().equals("") || vGroup.trim().equalsIgnoreCase("null") ) )
            {
              vIDArray=Parse.parse(vGroup,"~");
              query = " INSERT INTO T_UserGroup( UserGroup_ID, Fk_Group_ID, Fk_User_ID,"+
                      "  Modifier, Change_Dt) "+
                      "  VALUES( ?, ?, ?, ?, '"+dt+"') ";
              pstmt = conn.prepareStatement(query);
              for(int i=0;i<vIDArray.length;i++)
              {  
                String bid = vIDArray[i];
                int usrgrp_id = Integer.parseInt(db.getNextVal("S_UserGroup"));
                pstmt.setInt(1,usrgrp_id);
                pstmt.setString(2,bid);
                pstmt.setString(3,pnUserID);
                pstmt.setString(4,usr);
                pstmt.executeUpdate();
              }
              pstmt.close(); 
            }
            nMsgID = 5;
         }catch(Exception sexe){errMsg=sexe.getMessage();nMsgID=8;}
         finally
         {
           try
           {
             if(pstmt!=null)
               pstmt.close();
             if(stmt!=null)
               stmt.close();
             if(conn!=null)
               conn.close(); 
           }catch(SQLException sexe){}
         }
         Trans.setTransID(pnTransID);
       }
     } 
     if( nMsgID <=5 )
     { 
       if(vAction.equalsIgnoreCase("SaveInsert"))
         response.sendRedirect("/JOrder/servlets/UserFrame?pvMode=I&pnUserID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         response.sendRedirect("/JOrder/servlets/UserFrame?pvMode=&pnUserID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     }
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

