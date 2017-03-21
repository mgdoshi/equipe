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

public class ClnItmQryForm extends HttpServlet
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
      String vClientQry=null;
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

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      vFormType = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "WD_QUERY", "ClientItem / Q " );
      if(nUserID.equals("0"))
      {  
        vClientQry = " SELECT cln.Client_ID, cln.Client_Name" + 
                     " FROM   T_Client cln" +   
                     " Order By cln.Client_Name";      
      }
      else
      {
        vClientQry = " SELECT cln.Client_ID, cln.Client_Name" + 
                     " FROM   T_Client cln, T_UserClient ucl" +   
                     " WHERE  ucl.Fk_Client_ID = cln.Client_ID" +   
                     " AND    ucl.Fk_User_ID = " + nUserID +  
                     " Order By cln.Client_Name";      
      }

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Client/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ClnItmQryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      NL blines = new NL(4);
      form.add(blines);
      Table tab = new Table("1","center","Border=\"0\" width=\"53%\" COLS=2");

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_CLIENTITEM", nLangID, "BL_LABEL.B_CLIENTITEM_FK_CLIENT_ID","Client Name");
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.gif", vBPLate, vLabelAttrib ),null, null, null, "WIDTH=\"40%\""); 
      col4.add(WebUtil.NotNull); 
      TableCol col5 = new TableCol(util.createList( nUserID, "ST_CLIENTITEM", "nFk_Client_ID", "Q", vClientQry, null, null, vListAttrib ),null,null,null,null);
      row1.add(col4) ;
      row1.add(col5) ;

      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_LABEL.B_CLIENTITEM_FK_ITEM_ID", "Item Name" );
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Filter.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEM", "vItem_Name", "Q", "15", "100", "%", "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,null);
      row2.add(col6);
      row2.add(col7);

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

