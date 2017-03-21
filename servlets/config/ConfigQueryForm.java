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

public class ConfigQueryForm extends HttpServlet
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
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vLangQry=null;
      String vParentObjQry=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;


      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );


      String pvMode = request.getParameter("pvMode");
      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      vFormType = cdata.GetConfigValue( "SY_CONFIG", nLangID, "WD_QUERY", "Config / Q");

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Config/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vLangQry = "Select Lang_ID, Lang_Desc From T_Lang";
      vParentObjQry = "Select DISTINCT Parent_Obj, Parent_Obj From T_Config";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ConfigQueryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add( new NL(3));
      Table tab = new Table("1","center","Border=\"0\" width=\"55%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_PARENT_OBJ", "Parent Object" );
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"parobj.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"")) ;
      HtmlTag sel1 =  util.createList( nUserID, "SY_CONFIG", "vParent_Obj", "Q", vParentObjQry, null, null, vListAttrib);
      row.add(new TableCol( sel1 ,null,null,null,"WIDTH=\"25%\""));
      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_MSG", nLangID, "BL_LABEL.B_MESSAGE_LANG", "Language" );
      HtmlTag sel =  util.createList( nUserID, "SY_MSG", "nFk_Lang_ID", "Q", vLangQry, null, null, vListAttrib);
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"lang.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row1.add(new TableCol( sel, null, null, null,null));
      tab.add(row);
      tab.add(row1);
      form.add(tab);
      form.add( new FormHidden("vAction", null, null));
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

      String vParentObj = request.getParameter("vParent_Obj");
      String vLangID = request.getParameter("nFk_Lang_ID");
      String vAction = request.getParameter("vAction"); 

      if(vAction.equalsIgnoreCase("Query"))
         response.sendRedirect("/JOrder/servlets/ConfigFrame?pvMode=&pnConfigID=&pvParentObj="+vParentObj+"&pnLangID="+vLangID+"&pvMessage=");
      else
         response.sendRedirect("/JOrder/servlets/ConfigFrame?pvMode=I&pnConfigID=&pvParentObj="+vParentObj+"&pnLangID="+vLangID+"&pvMessage=");
    }
}

