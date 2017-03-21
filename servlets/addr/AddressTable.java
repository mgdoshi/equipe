import ingen.html.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import ingen.html.character.*;
import ingen.html.para.*;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AddressTable extends HttpServlet
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


       String nTransID     = null;
       String vBPLate      = null;
       String vImagePath   = null;
       String vColumns     = null;
       String vTitles      = null;
       String vWhereClause = null;
       String vName        = null;
       String vFormType=null;
       String nAuditID=null;
       String nLangID=null;
       String nUserID=null;

       nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
       nUserID   = Parse.GetValueFromString( vPID, "UserID" );
       nLangID   = Parse.GetValueFromString( vPID, "LangID" );

       DBConnect db = new DBConnect();
       ConfigData cdata = new ConfigData();
       Message msg = new Message();

       String pvRefType = request.getParameter("pvRefType");
       String pnRefID = request.getParameter("pnRefID");

        vFormType = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "WD_TABLE", " Address / T" );

        nTransID = Trans.getTransID( nAuditID, 'M');
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Address/";

        if( pvRefType != null && pvRefType.equalsIgnoreCase("Client") )
           vName = db.getName( pnRefID, "Client" ); 
        if( pvRefType != null && pvRefType.equalsIgnoreCase("Branch") )
           vName = db.getName( pnRefID, "Branch" ); 
        if( pvRefType != null && pvRefType.equalsIgnoreCase("Employee") )
           vName = db.getName( pnRefID, "Employee" ); 

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
        Form form = new Form("AddressTable", "POST", "_parent",null,null);
        Center cen = new Center();
        vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS", "Address of" );
        HtmlTag tag =new HtmlTag() ;
        tag.add( vBPLate+"&nbsp;&nbsp;"+pvRefType+"&nbsp;&nbsp;"+vName);
        vColumns = " adr.ADDRESS_ID, adr.DM_AddType, adr.Address1, adr.City, adf.RefType, adf.Ref_ID ";
        vTitles  = " Address ID,Address Type,Address,City";
        vWhereClause = " WHERE adr.Address_ID = adf.Fk_Address_ID AND adf.RefType LIKE '"+pvRefType+"' AND adf.Ref_ID = "+ pnRefID;
        cen.add(new FormHidden( "pnTransID", nTransID, null ));
        cen.add(new FormHidden( "pvRefType", pvRefType, null ));
        cen.add(new FormHidden( "pnRefID", pnRefID, null ));
        cen.add(new FormHidden( "nDelete", null, null ));
        form.add(cen);        
        body.add(form);
        page.add(head);         
        page.add(body);
        cen.add(tag);
        cen.add(new NL(2));
        cen.add(AddressForm.createAddressTable( request, vColumns, vWhereClause, vTitles , vPID ));
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
       String vAddrID[]=null;
       String vStatus=null;
       String sTransID=null; 
       String nBranchID=null;
       String nLangID=null;
       String nUserID=null;
       String pvRefType=null;
       String pnRefID=null;
     
       nUserID   = Parse.GetValueFromString( vPID, "UserID" );
       nLangID   = Parse.GetValueFromString( vPID, "LangID" );

 
       sTransID=request.getParameter("pnTransID");
       nBranchID=request.getParameter("pnBranchID");
       pvRefType=request.getParameter("pvRefType");
       pnRefID=request.getParameter("pnRefID");

       vStatus = Trans.checkTransValidity( sTransID );

       DBConnect db = new DBConnect();
       Message msg = new Message();
       Connection conn=null;
       PreparedStatement stmt=null;        

       if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
       {
         vAddrID=request.getParameterValues("nDelete");
         vIDArray=Parse.parse(vAddrID[0],"~");
         try
         {    
           conn = db.GetDBConnection();
           stmt = conn.prepareStatement("DELETE FROM T_Address WHERE Address_Id=?"); 
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
           }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
         }
        Trans.setTransID( sTransID );
     }  
     if(nMsgID <= 5)
     {
       response.sendRedirect("/JOrder/servlets/AddressFrame?pvMode=N&pvRefType="+pvRefType+"&pnRefID="+pnRefID+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     }
     else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   } 
}



