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
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmpAddrTable extends HttpServlet
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
        String vBPLate      = null;
        String vImagePath   = null;
        String vColumns     = null;
        String vTitles      = null;
        String vWhereClause = null;
        String vName        = null;
        String nAuditID     = null;
        String nLangID      = null;
        String nUserID      = null;
        String nTransID     = null;

        nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
        nUserID   = Parse.GetValueFromString( vPID, "UserID" );
        nLangID   = Parse.GetValueFromString( vPID, "LangID" );
        
        DBConnect db = new DBConnect();
        ConfigData cdata = new ConfigData();
        Message msg = new Message();
        String pnEmployeeID = request.getParameter("pnEmployeeID");
        vName = db.getName( pnEmployeeID, "Employee");
        nTransID = Trans.getTransID( nAuditID, 'M');
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Emp/";
        Page page = new Page();
        Head head = new Head();        
        Title title = new Title("Order - Delivery System : Update/Delete Address ");
        Script scr = new Script( "JavaScript", null );
        HtmlTag scrdata = new HtmlTag();
        scrdata.add("<!-- Start Hidding" + "\n");
        scrdata.add(" function submit_form( pvAction ) {                                \n"+
                    "    var nIdx = 0                                                   \n"+
                    "    if ( pvAction == \"Cancel\" ) {                                \n"+
                    "      top.close()                                                  \n"+
                    "      return }                                                     \n"+
                    "    if ( pvAction == \"Delete\" ) {                                \n"+
                    "      with ( document.forms[0] ) {                                 \n"+
                    "        nDelete[0].value=\"\"                                      \n"+
                    "        for( var i=1; i<nDelete.length; i++ )  {                   \n"+
                    "          if( nDelete[i].checked )     {                           \n"+
                    "            nIdx ++                                                \n"+
                    "            nDelete[0].value += nDelete[i].value + \"~\"           \n"+
                    "          } }                                                      \n"+
                    "       if( nIdx == 0 ) {                                           \n"+
                    "         alert( \" "+ msg.GetMsgDesc( 62, nLangID ) + " \" ); return }  \n"+
                    "	    if ( confirm( \" "+msg.GetMsgDesc( 11, nLangID )+ " \" ) ) {\n"+
                    "	      submit() }                                                \n"+
                    "      }                                                            \n"+				 	                   
                    "    }                                                              \n"+
                    "  }	");
        scrdata.add("// End Hidding -->");
        scr.add(scrdata);
        Body body = new Body("/ordimg/BACKGR2.GIF",null);
        Form form = new Form("EmpAddrTable", "POST", "_parent",null,null);
        Center cen = new Center();
        vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS", "Address of" );
        HtmlTag tag = new HtmlTag();
        tag.add( vBPLate+"&nbsp;&nbsp;"+"Employee"+"&nbsp;&nbsp;"+vName);
        vColumns = " adr.ADDRESS_ID, adr.DM_AddType, adr.Address1, adr.City, adf.RefType, adf.Ref_ID ";
        vTitles  = " Address ID,Address Type,Address,City";
        vWhereClause = " WHERE adr.Address_ID = adf.Fk_Address_ID AND adf.RefType = 'Employee' AND adf.Ref_ID = "+ pnEmployeeID;
        NL nl = new NL(1);
        cen.add(new FormHidden( "pnTransID", nTransID, null ));
        cen.add(new FormHidden( "pnEmployeeID", pnEmployeeID, null ));
        cen.add(new FormHidden( "nDelete", null, null ));
        form.add(cen);        
        body.add(form);
        page.add(head);         
        page.add(body);
        head.add(title);
        head.add(scr);
        cen.add(tag);
        cen.add(nl);
        cen.add(EmployeeForm.createEmpAddrTable(request,vColumns, vWhereClause, vTitles , vPID));
        cen.add(nl);
        cen.add(new FormButton( null, " Delete ",  "onClick=\"submit_form('Delete')\"" ));
        cen.add(new FormButton( null, "Cancel",  "onClick=\"submit_form('Cancel')\"" ));
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
       String nEmpID=null;
       String nLangID=null;
       String nUserID=null;
  
       nUserID   = Parse.GetValueFromString( vPID, "UserID" );
       nLangID   = Parse.GetValueFromString( vPID, "LangID" );

       DBConnect db = new DBConnect();
       Message msg = new Message(); 
       Connection conn=null;
       PreparedStatement stmt=null;        

       sTransID=request.getParameter("pnTransID");
       nEmpID=request.getParameter("pnEmployeeID");
       vStatus = Trans.checkTransValidity( sTransID );

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
           }catch(SQLException sexe){}
         }        
         Trans.setTransID( sTransID );
     }  
     if(nMsgID <= 5 )
       response.sendRedirect("/JOrder/servlets/EmpAddrTable?pnEmployeeID="+nEmpID+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   } 

}


