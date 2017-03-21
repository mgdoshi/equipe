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
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DelRepTable extends HttpServlet
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
        String nRecSecID=null;
        nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
        nUserID   = Parse.GetValueFromString( vPID, "UserID"  );
        nLangID   = Parse.GetValueFromString( vPID, "LangID"  );
        nRecSecID = Parse.GetValueFromString( vPID, "RecSecID");

        String vFrom_Order_Nr = request.getParameter("vFrom_Order_Nr");
        String vTo_Order_Nr = request.getParameter("vTo_Order_Nr");
        String dFrom_Order_Dt = request.getParameter("dFrom_Order_Dt");
        String dTo_Order_Dt = request.getParameter("dTo_Order_Dt");
        String cDM_OrdStat = request.getParameter("cDM_OrdStat");
        String pnClientID = request.getParameter("pnClientID");

        String vWhereClause = " WHERE ord.fk_Client_ID = '"+pnClientID+"' ";
        if(nRecSecID!=null && !nRecSecID.equalsIgnoreCase("0"))
          vWhereClause += " AND ( RecSecID = '"+nRecSecID+"' " +
                          " OR  ( RecSecID IN ( Select rsc.Managed_RecSec_ID "+
                          " From T_RecSecPriv rsc " +
                          " Where rsc.RecSec_ID  = '"+nRecSecID+"' ) )";
        if(vFrom_Order_Nr!=null && !vFrom_Order_Nr.equalsIgnoreCase("") && !vFrom_Order_Nr.equalsIgnoreCase("null")) 
          vWhereClause += " AND ord.Order_ID >= '"+vFrom_Order_Nr+"' " ;
        if(vTo_Order_Nr!=null && !vTo_Order_Nr.equalsIgnoreCase("") && !vTo_Order_Nr.equalsIgnoreCase("null")) 
           vWhereClause += " AND ord.Order_ID <= '"+vTo_Order_Nr+"' ";
        if(dFrom_Order_Dt!=null && !dFrom_Order_Dt.equalsIgnoreCase("") && !dFrom_Order_Dt.equalsIgnoreCase("null")) 
           vWhereClause += " AND ord.Order_Dt >= '"+dFrom_Order_Dt+"' ";
        if(dTo_Order_Dt!=null && !dTo_Order_Dt.equalsIgnoreCase("") && !dTo_Order_Dt.equalsIgnoreCase("null")) 
           vWhereClause += " AND ord.Order_Dt <= '"+dTo_Order_Dt+"' ";
        if(cDM_OrdStat!=null && !cDM_OrdStat.equalsIgnoreCase("") && !cDM_OrdStat.equalsIgnoreCase("null")) 
           vWhereClause += " AND ord.DM_OrdStat = '"+cDM_OrdStat+"' ";
       vWhereClause += "  AND dom.attrib=ord.DM_OrdStat AND dom.domain='ORDSTAT' ORDER BY ord.Order_ID ";

       ConfigData cdata = new ConfigData();
       vFormType = cdata.GetConfigValue( "TR_ORDER", nLangID, "WD_TABLE", " Order Report / T" );
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
       Form form = new Form("PrintOrderList", "POST", "_parent",null,null);
       String vCols = "ord.Order_ID, ord.Order_Nr, ord.Order_Dt, dom.attrib_desc";
       String vTitles = "Order ID, Order No, Order Date, Order Status";
       form.add( new FormHidden("nPrint", "",null));
       form.add(new NL(1));
       form.add(DelRepForm.createDelRepTable(vCols, vWhereClause, vTitles));
       body.add(form);
       page.add(head);
       page.add(body);
       page.printPage(out);
    }
}
