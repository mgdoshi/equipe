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

public class ConfigTable extends HttpServlet
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
      String vWhereClause=null;
      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      ConfigData cdata = new ConfigData();
      String pvParentObj = request.getParameter("pvParentObj");
      String pnLangID = request.getParameter("pnLangID");
      vFormType = cdata.GetConfigValue( "SY_CONFIG", nLangID, "WD_TABLE", " Config / T" );
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
      Form form = new Form("ConfigTable", "POST", "_parent",null,null);
      String vCols = "Config_ID, Parent_Obj, FK_Lang_ID, Obj_Name, Obj_Property, Property_Value";
      String vTitles = "Config ID, Parent Object, Language, Object Name, Obj Property, Property Value";

      if( !( pvParentObj==null || pvParentObj.equals("") || pvParentObj.equalsIgnoreCase("null") ) && !( pnLangID==null || pnLangID.equals("") || pnLangID.equals("null") ))
         vWhereClause = "WHERE Parent_Obj LIKE '" +  pvParentObj +
                         "' AND  FK_Lang_ID = " + pnLangID;
      else if( ( pvParentObj==null || pvParentObj.equals("") || pvParentObj.equalsIgnoreCase("null") ) && ( pnLangID==null || pnLangID.equals("") || pnLangID.equals("null") ))
         vWhereClause = null;
      else if( ( pvParentObj==null || pvParentObj.equals("") || pvParentObj.equalsIgnoreCase("null") ) && !( pnLangID==null || pnLangID.equals("") || pnLangID.equals("null") ))
         vWhereClause = "WHERE FK_Lang_ID = "+ pnLangID;
      else if( !( pvParentObj==null || pvParentObj.equals("") || pvParentObj.equalsIgnoreCase("null") ) && ( pnLangID==null || pnLangID.equals("") || pnLangID.equals("null") ))
         vWhereClause = "WHERE  Parent_Obj LIKE '"+ pvParentObj + "'";

      form.add(new FormHidden("pnTransID",nTransID,null));
      form.add(new FormHidden("nDelete","",null));
      form.add(new FormHidden("pvParentObj",pvParentObj,null));
      form.add(new FormHidden("pnLangID",pnLangID,null));
      form.add(new NL(3));
      form.add(Config.createConfigTable(vCols, vWhereClause, vTitles));
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
       String vConfigID[]=null;
       String vStatus=null;
       String sTransID=null; 
       String nLangID=null;
       String nUserID=null;
       nUserID   = Parse.GetValueFromString( vPID, "UserID" );
       nLangID   = Parse.GetValueFromString( vPID, "LangID" );
    
       DBConnect db = new DBConnect();
       Message msg = new Message();
       Connection conn=null;
       PreparedStatement stmt=null;        

       sTransID=request.getParameter("pnTransID");
       String pvParentObj = request.getParameter("pvParentObj");
       String pnLangID = request.getParameter("pnLangID");

       vStatus = Trans.checkTransValidity( sTransID );

       if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
       {
         vConfigID=request.getParameterValues("nDelete");
         vIDArray=Parse.parse(vConfigID[0],"~");
         try
         {    
           conn = db.GetDBConnection();
           stmt = conn.prepareStatement("DELETE FROM T_Config WHERE config_Id=?");
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
       if(nMsgID <=5 )
           response.sendRedirect("/JOrder/servlets/ConfigFrame?pvMode=&pnConfigID=&pvParentObj="+pvParentObj+"&pnLangID="+pnLangID+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
          out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   } 
}

