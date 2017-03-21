import ingen.html.*;
import ingen.html.frame.*;
import ingen.html.para.*;
import ingen.html.character.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChnStatusTable extends HttpServlet
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
        String vFormType=null;
        String nLangID=null;
        String nUserID=null;
        String nAuditID=null;
        String pnClientID=null;
        nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
        nUserID   = Parse.GetValueFromString( vPID, "UserID"  );
        nLangID   = Parse.GetValueFromString( vPID, "LangID"  );
        pnClientID = Parse.GetValueFromString( vPID, "ClientID");

        
        String vFrom_Order_ID = request.getParameter("vFrom_Order_ID");
        String vTo_Order_ID = request.getParameter("vTo_Order_ID");
        String vTemplName = request.getParameter("vTemplName");

        String vWhereClause = " WHERE ord.fk_Client_ID = '"+pnClientID+"' ";

        if(vFrom_Order_ID!=null && !vFrom_Order_ID.equalsIgnoreCase("") 
           && !vFrom_Order_ID.equalsIgnoreCase("null")) 
           vWhereClause += " AND ord.Order_ID >= '"+vFrom_Order_ID+"' ";
        if(vTo_Order_ID!=null && !vTo_Order_ID.equalsIgnoreCase("") 
           && !vTo_Order_ID.equalsIgnoreCase("null")) 
           vWhereClause += " AND ord.Order_ID <= '"+vTo_Order_ID+"' ";

       vWhereClause += " AND dom.attrib=ord.DM_OrdStat AND dom.domain='ORDSTAT' ORDER BY ord.Order_ID ";

       ConfigData cdata = new ConfigData();
       vFormType = cdata.GetConfigValue( "TR_ORDER", nLangID, "WD_FORM_TABLE2", "Change Order Status / T" );
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
       Form form = new Form("ChnStatusFrame", "Get", "_parent",null,null);
       String vCols = " ord.Order_ID, ord.Order_Nr, ord.Order_Dt, dom.attrib_desc ";
       String vTitles = " Order ID, Order No, Order Date, Order Status ";
       form.add( new FormHidden("vFrom_Order_ID", vFrom_Order_ID,null)); 
       form.add( new FormHidden("vTo_Order_ID", vTo_Order_ID,null)); 
       form.add( new FormHidden("vTemplName",vTemplName,null)); 
       form.add( new FormHidden("pvMode","L",null)); 
       form.add( new FormHidden("nPrint", null,null)); 
       form.add(new NL(1));
       form.add(ChnStatusForm.createChnStatusTable(vCols, vWhereClause, vTitles));
       form.add(new NL(2));
       body.add(form);
       page.add(head);
       page.add(body);
       page.printPage(out);
    }
}
