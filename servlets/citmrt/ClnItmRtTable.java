import ingen.html.*;
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

public class ClnItmRtTable extends HttpServlet
{
 
     public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {

        /*-------CHECK FOR ILLEGAL ENTRY---------*/
        response.setContentType("text/html");
        PrintWriter out = response.getWriter(); 
        CookieUtil PkCookie = new CookieUtil();
        DBConnect db = new DBConnect();
        String vPID = PkCookie.getCookie(request,"PID");
        String pnClientID = request.getParameter("pnClientID");
        String pvWhereClause = request.getParameter("pvWhereClause");
        if(vPID==null)
        {
          out.println(WebUtil.IllegalEntry());
          return;
        }
        String nAuditID=null;
        String nLangID=null;
        String nUserID=null;
        String nSchemeID=null;
        String temp=null;
        String vBPLate=null;
        String nTransID=null;
        String vFormType=null;
        String vLabelAttrib=null;

        ConfigData cdata = new ConfigData();
        WebUtil util = new WebUtil();
        Message msg = new Message();

        nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
        nUserID   = Parse.GetValueFromString( vPID, "UserID" );
        nLangID   = Parse.GetValueFromString( vPID, "LangID" );
        nSchemeID   = Parse.GetValueFromString( vPID, "SchemeID" );
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
        vFormType  = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "WD_TABLE", "Client Item Rate / T" );
        nTransID = Trans.getTransID( nAuditID, 'M');

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
        Form form = new Form("ClnItmRtTable", "POST", "_parent",null,null);
        form.add(new FormHidden("pnTransID",nTransID,null)); 
        form.add(new FormHidden("pnClientID",pnClientID,null));
        form.add(new FormHidden("pnCount","",null));
        form.add(new FormHidden("nDelete","",null));
        form.add(new FormHidden("pvWhereClause", pvWhereClause, null));
           
        pvWhereClause = pvWhereClause + " AND cit.Fk_Client_ID = "+ pnClientID +" ORDER BY itm.Item_Name";   
    
        Center cen = new Center();
        vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_CLIENT", "Client :" );
        cen.add( util.createLabelItem("<B>"+vBPLate+"</B>&nbsp;&nbsp;"+db.getName(pnClientID,"Client"),vLabelAttrib) );

        String vCols = "cit.Fk_Client_ID, cir.ClientItemRate_ID, cit.Fk_Item_ID, cir.From_Dt, cir.To_Dt, cir.MinQty, cir.MaxQty, cir.UnitPrice, cir.InActive";
        String vTitles  = "ClientID, ClientItemRate ID, Item Name, From Date, To Date, Min Qty, Max Qty, Price, InActive";
        Table tab = ClnItmRtForm.createClnItmRtTable(vCols, pvWhereClause, vTitles);
        form.add(new NL(1));
        form.add(cen);
        form.add(new NL(2));
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
       int nMsgID=-1;
       String errMsg=null;
       String vIDArray[]=null;
       String vItemID[]=null;
       String vStatus=null;
       String sTransID=null;
       String pvWhereClause=null;
       String nUserID=null;
       String nLangID=null;

       nUserID   = Parse.GetValueFromString( vPID, "UserID" );
       nLangID   = Parse.GetValueFromString( vPID, "LangID" );

       DBConnect db = new DBConnect();
       Message msg = new Message(); 
       Connection conn=null;
       PreparedStatement stmt=null;        

       sTransID=request.getParameter("pnTransID");
       String pnClientID =request.getParameter("pnClientID");
       int pnCount = Integer.parseInt( request.getParameter("pnCount") );
       pvWhereClause = URLEncoder.encode( request.getParameter("pvWhereClause"));
       String nDelete[] = request.getParameterValues("nDelete");

       vStatus = Trans.checkTransValidity( sTransID );

       if(vStatus!=null && !vStatus.equalsIgnoreCase("error"))
       {
         try
         {    
           conn = db.GetDBConnection();
           stmt = conn.prepareStatement("DELETE FROM T_ClientItemRate WHERE ClientItemRate_ID=?");
           for( int i=1; i<=pnCount; i++ )
           {
             int bid = Integer.parseInt(nDelete[i]);
             stmt.setInt(1,bid);
             stmt.executeUpdate();         
           }
           nMsgID = 4;
         }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=7;}
         finally
         {
           try
           {
             if(stmt!=null)
               stmt.close();
             if(conn!=null)
              conn.close(); 
           }catch(SQLException sexe){}
         }
         Trans.setTransID( sTransID );
       }
       if(nMsgID <= 5 )
       { 
         response.sendRedirect("/JOrder/servlets/ClnItmRtFrame?pvMode=T&pnClientID="+pnClientID+"&pnClientItemRateID=&pvWhereClause="+pvWhereClause+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       }
       else
          out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   } 

}
