import ingen.html.*;
import ingen.html.character.*;
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

public class NewsRomWin extends HttpServlet
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
      String rNews[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
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

      String pnNewsID = request.getParameter("pnNewsID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
 
      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_News/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnNewsID!=null && !pnNewsID.equals("") && !pnNewsID.equalsIgnoreCase("null"))
      { 
        rNews = db.getRecord(pnNewsID,"News");
      }  
      else
        rNews = new String[7];  

      Page page = new Page();
      Head head = new Head();
      Title title = new Title("Order - Delivery System : News Information ");
      head.add(title); 
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form();
   
      form.add(new NL());
      form.add(new Center(util.GetBoilerPlate("OFF",null,rNews[1]," COLOR=\"Black\" " + vLabelAttrib )));
      form.add(new NL());
      form.add(util.GetBoilerPlate("OFF",null,WebUtil.getBlankSpaces(10)+rNews[3]," COLOR=\"Black\" "+vLabelAttrib ));
      form.add(new NL(3));
      form.add(new Center(new FormButton(null," Ok ","onClick=\"top.close()\"")));
      body.add(form);
      page.add(head);
      page.add(body);
      out.println(page);
    }
}


