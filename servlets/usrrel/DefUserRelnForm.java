import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DefUserRelnForm extends HttpServlet
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

      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vUserQry=null;
      String vFormType = null;

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vUserQry = "Select User_ID, USer_Name From T_User";
      vFormType = cdata.GetConfigValue( "ST_USERREL", nLangID, "WD_FORM_DEFINE", "UserRel / D" );

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_UsrRel/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      Page page = new Page();
      Head head = new Head();
      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      head.add(scr);
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("DefUserRelnForm","POST","_parent",null,null);
      NL blines = new NL(2);
      form.add(blines);
      Table tab = new Table("1","center","Border=\"0\" width=\"60%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_SELECT", "Select a User Name" );
      HtmlTag sel =  util.createList( nUserID, "ST_USERREL", "pnUserID", "Q", vUserQry, null, null, vListAttrib);
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"usr.jpg", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"30%\""));
      row.add(new TableCol( sel, null, null, null, null));
      TableRow row1 = new TableRow("Left",null,null);
      TableCol col2 = new TableCol( "&nbsp;", null, null, null, null);
      row1.add(col2);
      row1.add(col2);
      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_RELATION", "Select a Relation" );
      row2.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"reln.jpg", vBPLate, vLabelAttrib ),null, null, null,null)); 
      vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_CLIENT", "Client" );
      row2.add(new TableCol( new FormRadio( "pvUserRel", "Client", "CHECKED", null ) + "&nbsp;&nbsp;"+ util.GetBoilerPlate(vImgOption, vImagePath+"client.jpg", vBPLate, vLabelAttrib ), null, null, null, null));
      TableRow row3 = new TableRow("Left",null,null);
      row3.add(new TableCol( "&nbsp;", null, null, null, null)); 
      vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_EMPLOYEE", "Employee" );
      row3.add(new TableCol( new FormRadio( "pvUserRel", "Employee", null, null ) + "&nbsp;&nbsp;"+ util.GetBoilerPlate(vImgOption, vImagePath+"emp.jpg", vBPLate, vLabelAttrib ), null, null, null, null));
      TableRow row4 = new TableRow("Left",null,null);
      row4.add(new TableCol( "&nbsp;", null, null, null, null)); 
      vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_ITEM", "Item" );
      row4.add(new TableCol( new FormRadio( "pvUserRel", "Item", null, null ) + "&nbsp;&nbsp;"+ util.GetBoilerPlate(vImgOption, vImagePath+"item.jpg", vBPLate, vLabelAttrib ), null, null, null, null));
      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row4);

      form.add(tab);
      body.add(form);
      page.add(body);
      page.add(head);
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
      String pnUserID = request.getParameter("pnUserID");
      String pvUserRel = request.getParameter("pvUserRel");
      response.sendRedirect("/JOrder/servlets/UserRelnFrame?pvMode=I&pvUserID="+pnUserID+"&pvUserRel="+pvUserRel+"&pvMessage=");
    }
}

