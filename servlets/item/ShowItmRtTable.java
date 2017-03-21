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

public class ShowItmRtTable extends HttpServlet
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
        String pnItemID = request.getParameter("pnItemID");
        if(vPID==null)
        {
          out.println(WebUtil.IllegalEntry());
          return;
        }

        String nAuditID=null;
        String nLangID=null;
        String nUserID=null;
        String vBPLate=null;
        String nTransID=null;

        ConfigData cdata = new ConfigData();
        WebUtil util = new WebUtil();
        Message msg = new Message();

        nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
        nUserID   = Parse.GetValueFromString( vPID, "UserID" );
        nLangID   = Parse.GetValueFromString( vPID, "LangID" );
        nTransID = Trans.getTransID( nAuditID, 'M');

        Page page = new Page();
        Head head = new Head();
        Title title = new Title("Order Entry & Tracking System :- ItemRate Table ");
        head.add(title); 
        Script scr = new Script( "JavaScript", null );
        HtmlTag scrdata = new HtmlTag();
        scrdata.add("<!-- Start Hidding" + "\n" +
                    "   function submit_form()   {                                 \n"+
                    "     var nCount = 0  				           \n"+
                    "     with( document.forms[0] ) {                     	   \n"+
                    "	    for( var i=1; i<nDelete.length; i++ ) {                \n"+
                    "	      nCount = ( nDelete[i].checked ? ++nCount : nCount )  \n"+
                    "       }						           \n"+
                    "       pnCount.value = nCount        		           \n"+
                    "	    if ( confirm( \""+ msg.GetMsgDesc( 11, nLangID )+"\" ) )  \n"+
                    "	      submit()          \n"+
                    "     }			\n"+
                    "   } 	  	        \n"+
                    "  function call_form( pnItemRateID )  {       \n"+
                    "    this.location.href = \"/JOrder/servlets/ShowItmRtUpdDelForm?pnItemRateID=\"+pnItemRateID+\"&pnItemID=" + pnItemID+"\" \n"+
                    "  } \n"+
                    "// End Hidding -->");
        scr.add(scrdata);
        head.add(scr);
        Body body = new Body("/ordimg/BACKGR2.GIF",null);
        Form form = new Form("/JOrder/servlets/ShowItmRtTable", "POST", null,null,null);

        String vCols = "ItemRate_ID, From_Dt, To_Dt, MinQty, MaxQty, UnitPrice, InActive";
        String vTitles  = "IR ID, From Dt, To Dt, Min Qty, Max Qty, Unit Price, Active";
        String pvWhereClause = "Where Fk_Item_ID="+pnItemID;
        Center cen = new Center();
        form.add( new FormHidden("pnTransID",nTransID,null) ); 
        form.add( new FormHidden("nItemID",pnItemID,null) );
        form.add( new FormHidden("nDelete",null,null) );
        form.add( new FormHidden("pnCount", null, null) );
        form.add( new NL(2) );
        form.add( ItemForm.createItmRtTable(vCols, pvWhereClause, vTitles) );
        form.add( new NL(1) );
        cen.add( new FormButton( null, " Delete ", "onClick=\"submit_form()\"" ) );
        cen.add( new FormButton( null, "Cancel", "onClick=\"top.close()\"" ) );
        form.add(cen);
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
       String nLangID=null;
       String nUserID=null;
       String errMsg=null;
       String vIDArray[]=null;
       String vItmRtID[]=null;
       String vStatus=null;
       String sTransID=null;
       String pvWhereClause=null;

       DBConnect db = new DBConnect();
       Message msg = new Message(); 
       Connection conn=null;
       PreparedStatement pstmt=null;

       nUserID   = Parse.GetValueFromString( vPID, "UserID" );
       nLangID   = Parse.GetValueFromString( vPID, "LangID" );

       sTransID=request.getParameter("pnTransID");
       String pnItemID=request.getParameter("nItemID");
       int pnCount = Integer.parseInt( request.getParameter("pnCount") );
       vItmRtID =  request.getParameterValues("nDelete");

       vStatus = Trans.checkTransValidity( sTransID );

       if(vStatus!=null && !vStatus.equalsIgnoreCase("error"))
       {
         try
         {    
           conn = db.GetDBConnection();
           pstmt = conn.prepareStatement("DELETE FROM T_ItemRate WHERE ItemRate_ID=?");
           for( int i=1; i<pnCount+1; i++ )
           {
             int bid = Integer.parseInt(vItmRtID[i]);
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
         response.sendRedirect("/JOrder/servlets/ShowItmRtTable?pnItemID="+pnItemID);
       else
         out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}
