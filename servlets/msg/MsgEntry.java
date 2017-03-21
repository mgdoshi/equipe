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

public class MsgEntry extends HttpServlet
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

      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;
      String rMsg[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vLangQry=null;

      String pvMode = request.getParameter("pvMode");
      String pnMsgID = request.getParameter("pnMsgID");
      String pvWhereClause = request.getParameter("pvWhereClause");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "SY_MSG", nLangID, "WD_FORM_INSERT", "Message / I");
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "SY_MSG", nLangID, "WD_FORM_UPDATE", "Message / U");

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Msg/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnMsgID!=null && !pnMsgID.equals("") && !pnMsgID.equalsIgnoreCase("null"))
        rMsg = db.getRecord( pnMsgID, "msg" );
      else
        rMsg = new String[5]; 
 
      vLangQry = "Select Lang_ID, Lang_Desc From T_Lang";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/MsgEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add( new FormHidden("pnTransID", nTransID, null ) );
      form.add( new FormHidden("pvWhereClause", pvWhereClause, null ) );
      form.add(new NL(3));

      Table tab = new Table("1","center","Border=\"0\" width=\"98%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_MSG", nLangID, "BL_LABEL.B_MESSAGE_MSG_ID", "Msg ID");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"msgid.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"16%\"");
      col.add( WebUtil.NotNull );
      TableCol col1 = null;
      if( pvMode!=null && pvMode.equalsIgnoreCase("I"))
        col1 = new TableCol(util.createTextItem( nUserID, "SY_MSG", "nMsg_ID", pvMode, "5", "30", rMsg[0], null, vTextAttrib),null,null,null,null);
      else
      {
        col1 = new TableCol( rMsg[0],null,null,null,null);
        FormHidden msgid = new FormHidden("nMsg_ID", rMsg[0], null);
        form.add(msgid); 
      }
      row.add(col) ;
      row.add(col1);
      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_MSG", nLangID, "BL_LABEL.B_MESSAGE_LANG", "Language" );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"lang.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col2.add( WebUtil.NotNull );
      HtmlTag sel =  util.createList(nUserID, "SY_MSG", "nFk_Lang_ID", pvMode,vLangQry, rMsg[1], null,vListAttrib);
      TableCol col3 = new TableCol( sel, null, null, null,null); 
      row1.add(col2) ;
      row1.add(col3);
      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_MSG", nLangID, "BL_LABEL.B_MESSAGE_DESCRIPTION", "Description" );
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"desc.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col4.add( WebUtil.NotNull );
      TableCol col5 = new TableCol(util.createTextItem( nUserID, "SY_MSG", "vMsg_Desc", pvMode, "55", "250",rMsg[2], null, vTextAttrib),null, null, null,null); 
      row2.add(col4) ;
      row2.add(col5);
      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      form.add(tab);
      form.add( new FormHidden("vAction", null, null) );
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
      String mode=""; 
      String query = null;

      String pnTransID = request.getParameter("pnTransID");
      String pnMsgID = request.getParameter("nMsg_ID");
      String vLangID = request.getParameter("nFk_Lang_ID");
      String vMsgDesc = request.getParameter("vMsg_Desc");
      String vWhereClause = URLEncoder.encode( request.getParameter("pvWhereClause") );
      String vAction = request.getParameter("vAction"); 

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
      Message msg = new Message(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();
  
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

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
            query = "INSERT INTO T_Msg ( Msg_ID, Msg_Desc, Fk_Lang_ID, Modifier, Change_Dt ) " +
                    "VALUES ("+pnMsgID+", '"+vMsgDesc+"', "+vLangID+",'"+usr+"', '"+dt+"')";
            stmt.executeUpdate(query);
            nMsgID = 3;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
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
        else if( vAction.equalsIgnoreCase("Update"))
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            query = "UPDATE T_Msg SET Msg_Desc = '"+vMsgDesc+"'," +
                    " Fk_Lang_ID   = "+vLangID+"," +
                    " Modifier    = '"+usr+"'," + 
                    " Change_Dt   = '"+dt+"'" + 
                    " WHERE Msg_ID = "+pnMsgID;
            stmt.executeUpdate(query);
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
     if( nMsgID <= 5)
     { 
       if(vAction.equalsIgnoreCase("SaveInsert"))
         mode="I";
       else
         mode="N";
       response.sendRedirect("/JOrder/servlets/MsgFrame?pvMode="+mode+"&pnMsgID=&pvWhereClause="+vWhereClause+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));      
     }
     else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

