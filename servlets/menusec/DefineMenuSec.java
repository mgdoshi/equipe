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
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DefineMenuSec extends HttpServlet
{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
    throws IOException, ServletException
    {
      int nRefID = 0;
      int nCount = 0;

      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String vImagePath=null;
      String vPID=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vGroupQry = null;
      String vUserQry = null;

      response.setContentType("text/html");  
      PrintWriter out = response.getWriter();

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      CookieUtil PkCookie = new CookieUtil();
      vPID = PkCookie.getCookie(request,"PID");
      if(vPID==null)
      {
        out.println(WebUtil.IllegalEntry());
        return;
      }
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "WD_FORM_DEFINE", "MenuSec / D" );
	
      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_MenuSec/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vGroupQry = "Select Group_ID, Group_Name From T_Group";
      vUserQry = "Select User_ID, User_Name From T_User";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form();

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new NL(5));

      Table tab = new Table("1","center","Border=\"0\" width=\"35%\" COLS=3");
 
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_LABEL.B_MENUSEC_SYSTEM", "System" );
      row.add( new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"System.gif", vBPLate, vLabelAttrib ),null, null, null, null) );
      row.add( new TableCol( new FormRadio( "nPriv", "S", "CHECKED", null ),null,null,null,"WIDTH=\"15%\"") );

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_LABEL.B_MENUSEC_GROUP", "Group" );
      row1.add( new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Grp.gif", vBPLate, vLabelAttrib ),null, null, null, null) );
      row1.add( new TableCol( new FormRadio( "nPriv", "G", null, null ),null,null,null,null) );
      HtmlTag sel =  util.createList( nUserID, "SY_MENUSEC", "pvGroupName", "I", vGroupQry, null, null, vListAttrib);
      row1.add( new TableCol( sel, null, null, null,null) ) ;

      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_USER", "User" );
      row2.add( new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Usr.gif", vBPLate, vLabelAttrib ),null, null, null, null) );
      row2.add( new TableCol( new FormRadio( "nPriv", "U", null, null ),null,null,null,null) );
      HtmlTag sel1 =  util.createList( nUserID, "SY_MENUSEC", "pvUserName", "I", vUserQry, null, null, vListAttrib);
      row2.add( new TableCol( sel1, null, null, null,null) );

      tab.add(row);
      tab.add(row1);
      tab.add(row2);

      form.add(tab); 
      body.add(form);
      head.add(scr);
      page.add(head);
      page.add(body);
      out.println(page);
    }
}