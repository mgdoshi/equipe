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

public class ClientTable extends HttpServlet
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
  
        String nTransID=null;
        String vFormType=null;
        String nLangID=null;
        String nAuditID=null;

        nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
        nLangID   = Parse.GetValueFromString( vPID, "LangID" );

        ConfigData cdata = new ConfigData();
  
        vFormType = cdata.GetConfigValue( "ST_CLIENT", nLangID, "WD_TABLE", "Client / T" );
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
        Form form = new Form("ClientTable", "POST", "_parent",null,null);
        String vCols = "Client_ID,Client_Code,DM_ClnType, Client_Name,Client_Desc";
        String vTitles = "Client ID,Client Code,Client Type, Client Name,Client Desc";
        form.add(new FormHidden("pnTransID",nTransID,null)); 
        form.add(new FormHidden("nDelete",null,null));
        form.add(new NL(3));
        form.add(ClientForm.createClientTable(vCols, null, vTitles, vPID));
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
       String vClientID[]=null;
       String vStatus=null;
       String nLangID=null;
       String sTransID=null; 

       nLangID   = Parse.GetValueFromString( vPID, "LangID" );
       DBConnect db = new DBConnect(); 
       Message msg = new Message();
       Connection conn=null;
       PreparedStatement stmt=null;        

       sTransID=request.getParameter("pnTransID");
       vStatus = Trans.checkTransValidity( sTransID );

       if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
       {
         vClientID=request.getParameterValues("nDelete");
         vIDArray=Parse.parse(vClientID[0],"~");
         try
         {    
           conn = db.GetDBConnection();
           stmt = conn.prepareStatement("DELETE FROM T_Client WHERE Client_Id=?"); 
           for(int i=0;i<vIDArray.length;i++)
           {  
             int bid = Integer.parseInt(vIDArray[i]);  
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
         response.sendRedirect("/JOrder/servlets/ClientFrame?pvMode=&pnClientID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   } 

}
