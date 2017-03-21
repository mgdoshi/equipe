import ingen.html.*;
import ingen.html.character.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.net.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PassEntryForm extends HttpServlet
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
      String vBPLate=null;
      String vImagePath=null;
      String vImgOption=null;
      String vTextAttrib=null;
      String vLabelAttrib=null;
      String vFormType=null;

      String pvMode = request.getParameter( "pvMode");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
 
      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "SY_USER", nLangID, "WD_FORM_UPD_PASSWD","Change Password / U");
      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_User/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );

      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      Page page = new Page();
      Head head = new Head();
      Body body = new Body( "/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/PassEntryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding\n" +
                  "top.show_form(\""+vFormType+"\")\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden( "pnTransID", nTransID,null ));
      form.add(new NL(2));

      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2");

      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_USER_NAME", "User Name" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"name.gif",vBPLate,vLabelAttrib ),null, null, null,"width=20%");
      col.add(WebUtil.NotNull);
      row.add(col);
      row.add(new TableCol(util.createTextItem( nUserID, "SY_USER", "vUser_Name", pvMode, "15", "30",null, "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null)); 
      tab.add(row);

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_OLD_PASSWORD", "Old PassWord" );
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"opass.gif",vBPLate,vLabelAttrib ),null, null, null, null);
      col1.add(WebUtil.NotNull);
      row1.add(col1);
      row1.add(new TableCol(util.createPasswdItem( nUserID, "SY_USER", "vUser_OldPass", pvMode, "15", "30",null, null, vTextAttrib),null, null, null,null)); 
      tab.add(row1);

      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_NEW_PASSWORD", "New PassWord" );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"npass.gif",vBPLate,vLabelAttrib ),null, null, null, null);
      col2.add(WebUtil.NotNull);
      row2.add(col2);
      row2.add(new TableCol(util.createPasswdItem( nUserID, "SY_USER", "vUser_NewPass", pvMode, "15", "30",null, null, vTextAttrib),null, null, null,null)); 
      tab.add(row2);

      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_CONFIRM_PASSWD", "Confirm PassWord" );
      TableCol col3 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"cpass.gif",vBPLate,vLabelAttrib ),null, null, null, null);
      col3.add(WebUtil.NotNull);
      row3.add(col3);
      row3.add(new TableCol(util.createPasswdItem( nUserID, "SY_USER", "vUser_ConfPass", pvMode, "15", "30",null, null, vTextAttrib),null, null, null,null)); 
      tab.add(row3);

      form.add(tab);
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
      response.setContentType("text/html");
      PrintWriter out = response.getWriter(); 
      CookieUtil PkCookie = new CookieUtil();
      String vPID = PkCookie.getCookie(request,"PID");
      if(vPID==null)
      {
        out.println(WebUtil.IllegalEntry());
        return;
      }
      int nMsgID =-1;

      String nLangID=null;
      String nUserID=null;
      String errMsg = null; 
      String vStatus=null;
      String temp=null;
 
      User usr = new User();
      Message msg = new Message();

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );              
      String pnTransID = request.getParameter("pnTransID");
      String vUser_Name = Parse.formatStr( request.getParameter("vUser_Name") );
      String vUser_OldPass = Parse.formatStr( request.getParameter("vUser_OldPass") );
      String vUser_NewPass = Parse.formatStr( request.getParameter("vUser_NewPass") );

      vStatus  = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
         if(!usr.ChangeUserPassword(nUserID,vUser_OldPass,vUser_NewPass))
           nMsgID = 8;
      }
      Trans.setTransID(pnTransID);
      if( nMsgID <= 5 )
        response.sendRedirect("/JOrder/servlets/PassFrame?pvMode=U&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
      else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
    }
}

