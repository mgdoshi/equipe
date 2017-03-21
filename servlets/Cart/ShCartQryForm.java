import ingen.html.*;
import ingen.html.para.*;
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

public class ShCartQryForm extends HttpServlet
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
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vCartDefQry=null;
      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;


      nAuditID   = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );


      String pvMode = request.getParameter("pvMode");
      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      vFormType = cdata.GetConfigValue( "TR_SHOPCART", nLangID, "WD_QUERY", "Shopping Cart / Q");

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ShopCart/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );
      vCartDefQry = "Select CartDef_ID, CartDef_Name From T_CartDef";
      nTransID = Trans.getTransID( nAuditID, 'M');

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ShCartQryForm","POST","_parent",null,null);
      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add( new NL(6));
      form.add(new FormHidden("pnTransID",nTransID,null));
      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_SHOPCART", nLangID, "BL_LABEL.B_SHOPCART_DEFN", "Select Shopping Cart Defination" );
      HtmlTag sel1 =  util.createList( nUserID, "TR_SHOPCART", "vCartDef_Name", "Q", vCartDefQry, null, null, vListAttrib);
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"35%\"")) ;
      row.add(new TableCol( sel1 ,null,null,null,null));
      tab.add(row);
      form.add(tab);
      body.add(form);
      head.add(scr);
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
      int nMsgID = -1;
      String errMsg=null; 
      String vStatus = null; 
      String vCartDef_Name=null;
      String nTransID=null;
      String nLangID=null;
      String nUserID=null;
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nTransID = request.getParameter("pnTransID");
      vCartDef_Name = request.getParameter("vCartDef_Name");
      Message msg = new Message();
      DBConnect db = new DBConnect();
      Connection conn = null;
      Statement stmt = null;

      vStatus = Trans.checkTransValidity( nTransID );
       if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
       {
         try
         {    
           conn = db.GetDBConnection();
           stmt = conn.createStatement();
           stmt.executeUpdate(" DELETE FROM T_CartDef" +
                              " WHERE CartDef_ID = "+vCartDef_Name+"");         
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
         Trans.setTransID( nTransID );
       }  
       if(nMsgID <=5 )
           response.sendRedirect("/JOrder/servlets/ShCartFrame?pvMode=Q&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
          out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
    }
}

