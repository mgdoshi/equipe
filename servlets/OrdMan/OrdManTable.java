import ingen.html.*;
import ingen.html.para.*;
import ingen.html.character.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.awt.*;
import java.sql.*;
import java.io.*;
import java.lang.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class OrdManTable extends HttpServlet
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

        String Browser = request.getParameter("Browser");
        String vTemplName = request.getParameter("vTemplName");
        String pvWhereClause = request.getParameter("pvWhereClause");
        ConfigData cdata = new ConfigData();
        vFormType = cdata.GetConfigValue( "TR_ORDER", nLangID, "WD_TABLE", " Order / T" );
        String nTransID = Trans.getTransID( nAuditID, 'M'); 
        Page page = new Page();
        Head head = new Head();
        Script scr = new Script( "JavaScript", null );
        HtmlTag scrdata = new HtmlTag();
        scrdata.add("<!-- Start Hidding" + "\n" +
                   "top.show_form(\""+vFormType+"\")" + "\n" +
                   "// End Hidding -->");
        scr.add(scrdata);
        Script scr1 = new Script( "JavaScript", null );
        HtmlTag scrdata1 = new HtmlTag();
        scrdata1.add("<!-- Start Hidding" + "\n" +
                     " function call_form( id ) { \n"+
                     "  parent.location.href = \"/JOrder/servlets/OrdManFrame?pvMode=D&pnOrderID=\"+id+\"&vTemplName="+vTemplName+"&Browser="+Browser+"\"\n"+
                     " }                     \n"+
                   "// End Hidding -->");
        scr1.add(scrdata1);
        head.add(scr);
        head.add(scr1);
        Body body = new Body("/ordimg/BACKGR2.GIF",null);
        Form form = new Form("/JOrder/servlets/OrdManTable", "POST", "_parent", null, null);
        String vCols = "ord.Order_ID, ord.Order_Nr, ord.Order_Dt, dom.Attrib_Desc";
        String vTitles = "Order ID, Order No, Order Date, Order Status";
        form.add( new FormHidden("pnTransID", nTransID,null));
        form.add( new FormHidden("vTemplName", vTemplName, null));
        form.add( new FormHidden("Browser", Browser, null));
        form.add( new FormHidden("pvWhereClause", pvWhereClause,null));
        form.add( new FormHidden("nDelete", "",null)); 
        form.add(new NL(1));
        form.add(OrdManForm.createOrdManTable(vCols, pvWhereClause, vTitles));
        body.add(form);
        page.add(head);
        page.add(body);
        page.printPage(out);
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

       int nMsgID=-1;
       String errMsg=null;
       String vIDArray[]=null;
       String vMsgID[]=null;
       String vStatus=null;
       String sTransID=null; 
       String nLangID=null;

       nLangID  = Parse.GetValueFromString( vPID, "LangID" );

       DBConnect db = new DBConnect();
       Message msg = new Message();
       Connection conn=null;
       PreparedStatement pstmt=null;        

       sTransID=request.getParameter("pnTransID");
       String vTemplName = request.getParameter("vTemplName");
       String Browser = request.getParameter("Browser");
       String pvWhereClause = URLEncoder.encode( request.getParameter("pvWhereClause") );
       String nDelete[] = request.getParameterValues("nDelete");

       vStatus = Trans.checkTransValidity( sTransID );

       if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
       {
         vMsgID=request.getParameterValues("nDelete");
         vIDArray=Parse.parse(vMsgID[0],"~");
         try
         {    
           conn = db.GetDBConnection();
           pstmt = conn.prepareStatement("DELETE FROM T_OrderDtls WHERE Fk_Order_Id=?");
           for(int i=0;i<vIDArray.length;i++)
           {  
             int bid = Integer.parseInt(vIDArray[i]);  
             pstmt.setInt(1,bid);
             pstmt.executeUpdate();         
           }
           pstmt = conn.prepareStatement("DELETE FROM T_Order WHERE Order_Id=?"); 
           for(int i=0;i<vIDArray.length;i++)
           {  
             int bid = Integer.parseInt(vIDArray[i]);  
             pstmt.setInt(1,bid);
             pstmt.executeUpdate();         
           }
           nMsgID = 4;
         }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=7;}
         finally
         {
           try
           {
             if(pstmt!=null)
               pstmt.close();
             if(conn!=null)
               conn.close(); 
           }catch(SQLException sexe){}
         }
         Trans.setTransID( sTransID );
       }  
       if( nMsgID <= 5 )
         response.sendRedirect("/JOrder/servlets/OrdManFrame?pvMode=T&vTemplName="+vTemplName+"&Browser="+Browser+"&pvWhereClause="+pvWhereClause+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
    }

}
