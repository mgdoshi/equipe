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

public class ItemTable extends HttpServlet
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
        String pvWhereClause = request.getParameter("pvWhereClause");
        if(vPID==null)
        {
          WebUtil.IllegalEntry().printPage(out);
          return;
        }

        String nAuditID=null;
        String nLangID=null;
        String nUserID=null;
        String vBPLate=null;
        String nTransID=null;
        String vFormType=null;

        ConfigData cdata = new ConfigData();
        WebUtil util = new WebUtil();
        Message msg = new Message();

        nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
        nUserID   = Parse.GetValueFromString( vPID, "UserID" );
        nLangID   = Parse.GetValueFromString( vPID, "LangID" );
        vFormType = cdata.GetConfigValue( "ST_ITEM", nLangID, "WD_TABLE", "Item / T" );
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
        Form form = new Form("ItemTable", "POST", "_parent", null, null);
        String vCols = "Item_ID, Item_Name, Item_Desc";
        String vTitles  = "Item ID, Item Name, Item Desc";
        form.add( new FormHidden("pnTransID",nTransID,null) );
        form.add( new FormHidden("nDelete","",null) );
        form.add( new FormHidden("pvWhereClause", pvWhereClause, null) );
        form.add( new NL(3) );
        form.add( ItemForm.createItemTable(vCols, pvWhereClause, vTitles, vPID) );
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

       String nLangID=null;
       String nUserID=null;
       String errMsg=null;
       String vIDArray[]=null;
       String vItemID[]=null;
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
       pvWhereClause = URLEncoder.encode( request.getParameter("pvWhereClause"));
       vStatus = Trans.checkTransValidity( sTransID );
       if(vStatus!=null && !vStatus.equalsIgnoreCase("error"))
       {
         vItemID=request.getParameterValues("nDelete");
         vIDArray=Parse.parse(vItemID[0],"~");
         try
         {    
           conn = db.GetDBConnection();
           pstmt = conn.prepareStatement("DELETE FROM T_Item WHERE Item_Id=?"); 
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
       if( nMsgID <=5 )
         response.sendRedirect("/JOrder/servlets/ItemFrame?pvMode=N&pnItemID=&pvWhereClause="+pvWhereClause+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         WebUtil.ShowException(errMsg, nMsgID, nLangID).printPage(out);
   } 

}
