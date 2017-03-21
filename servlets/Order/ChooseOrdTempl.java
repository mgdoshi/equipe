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

public class ChooseOrdTempl extends HttpServlet
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
      String vFormType=null;
      String vOrdTemplQry = null;

      String pvMode = request.getParameter("pvMode");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "TR_ORDER", nLangID, "WD_QUERY", "Order / Q" );

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Order/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vOrdTemplQry = " SELECT Alias_Name, Alias_Name FROM T_ClsTempl "+
                     " WHERE Ref_ID = "+ nUserID;

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
      Form form = new Form("/JOrder/servlets/ChooseOrdTempl","POST","_parent",null,null);
      form.add(new FormHidden("Browser", "", null) );
      form.add(new NL(5));
      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_TEMPLATE", "Select Order Template" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"ordtemp.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"");
      HtmlTag sel =  util.createList( nUserID, "TR_ORDER", "vTemplName", "Q", vOrdTemplQry, null, null, vListAttrib);
      TableCol col1 = new TableCol( sel,null, null, null, null );
      row.add(col);
      row.add(col1);
      tab.add(row);
      vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDDTLSCOUNT", "Order Dtls Count" );
      TableRow row1 = new TableRow("Left",null,null);
      TableCol col2 = new TableCol( util.GetBoilerPlate( vImgOption, vImagePath+"ordcnt.gif", vBPLate, vLabelAttrib ), null, null, null, null );
      TableCol col3 = new TableCol( util.createTextItem( nUserID, "TR_ORDER", "nOrdDtlsCount", "Q", "5", "10", "10", null, vTextAttrib ), null, null, null, null );
      row1.add(col2);
      row1.add(col3);
      tab.add(row1);
      form.add(tab);
      body.add(form);
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

      String Browser = request.getParameter("Browser");
      String vTemplName = request.getParameter("vTemplName");
      String nOrdDtlsCount = request.getParameter("nOrdDtlsCount");
      if( vTemplName==null || vTemplName.equals("") || vTemplName.equalsIgnoreCase("null") )
        response.sendRedirect("/JOrder/servlets/Manual?Flag=0");
      response.sendRedirect("/JOrder/servlets/OrderFrame?pvMode=I&pnOrderID=&vTemplName="+vTemplName+"&pnOrdDtlsCount="+nOrdDtlsCount+"&Browser="+Browser+"&pvMessage=");
   }
}

