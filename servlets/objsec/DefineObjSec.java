import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DefineObjSec extends HttpServlet
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

      int nRefID = 0;
      int nCount = 0;

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
      String vFormType=null;
      String vObjParQry = null;
      String vGroupQry = null;
      String vUserQry = null;

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "WD_FORM_DEFINE", "Object Security / D" );
	
      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ObjSec/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vObjParQry = "SELECT DISTINCT ObjParent_Name, ObjParent_Name FROM T_ObjSecRef";
      vGroupQry = "Select Group_ID, Group_Name From T_Group";
      vUserQry = "Select User_ID, User_Name From T_User";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/DefineObjSec","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add(new FormHidden("vSecLevel", "dummy", null ));
      form.add(new NL(2));

      Table tab = new Table("1","center","Border=\"0\" width=\"35%\" COLS=3");
 
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_SYSTEM", "System" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"System.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col1 = new TableCol( new FormRadio( "vSecLevel", "S", "CHECKED", null ),null,null,null,"WIDTH=\"15%\"");
      row.add(col) ;
      row.add(col1) ;

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_GROUP", "Group" );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Grp.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col3 = new TableCol( new FormRadio( "vSecLevel", "G", null, null ),null,null,null,null);
      HtmlTag sel =  util.createList( nUserID, "SY_OBJSEC", "vGroup", "Q", vGroupQry, null, null, vListAttrib);
      TableCol col4 = new TableCol( sel, null, null, null,null);
      row1.add(col2) ;
      row1.add(col3) ;
      row1.add(col4) ;

      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_USER", "User" );
      TableCol col5 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Usr.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col6 = new TableCol( new FormRadio( "vSecLevel", "U", null, null ),null,null,null,null);
      HtmlTag sel1 =  util.createList( nUserID, "SY_OBJSEC", "vUser", "Q", vUserQry, null, null, vListAttrib);
      TableCol col7 = new TableCol( sel1, null, null, null,null);
      row2.add(col5) ;
      row2.add(col6) ;
      row2.add(col7) ;

      tab.add(row);
      tab.add(row1);
      tab.add(row2);

      Table tab1 = new Table("1","center","Border=\"0\" width=\"50%\" COLS=3");

      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_OBJSEC", nLangID, "BL_LABEL.B_OBJSEC_SELECT", "Select Form Name" );
      TableCol col8 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"select.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      col8.add(WebUtil.NotNull);
      HtmlTag sel2 =  util.createList( nUserID, "SY_OBJSEC", "vFormName", "Q", vObjParQry, null, null, vListAttrib);
      TableCol col9 = new TableCol( sel2, null, null, null,null);

      row3.add(col8) ;
      row3.add(col9) ;
      tab1.add(row3);

      form.add(new FormHidden("vAction", null, null ));
      form.add(tab); 
      form.add(tab1); 
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

      String nRefID = null;
      String mode = null;
 
      String vSecLevel[] = request.getParameterValues("vSecLevel");
      String vGroup = request.getParameter("vGroup");
      String vUser = request.getParameter("vUser");
      String vFormName = request.getParameter("vFormName");
      String vAction = request.getParameter("vAction");

      if ( vSecLevel[0].equalsIgnoreCase("G") )
          nRefID = vGroup;
      if ( vSecLevel[0].equalsIgnoreCase("U") )
          nRefID = vUser;

      if(vAction.equalsIgnoreCase("New"))
        mode="I";
      else
        mode="N";
      response.sendRedirect("/JOrder/servlets/ObjSecFrame?pvMode="+mode+"&pvParentObj="+vFormName+"&pvObjType=&pvSecLevel="+vSecLevel[0]+"&pnRefID="+nRefID+"&pvMessage=");
   }
}