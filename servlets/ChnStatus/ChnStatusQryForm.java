import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import ingen.html.character.*;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChnStatusQryForm extends HttpServlet
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
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      String pvMode = request.getParameter("pvMode"); 
      
      String vOrdTemplQry = " SELECT Alias_Name, Alias_Name FROM T_ClsTemplDel "+
                           " WHERE Ref_ID = "+ nUserID;



      DBConnect db = new DBConnect(); 
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      vFormType = cdata.GetConfigValue( "TR_ORDER", nLangID, "WD_FORM_QUERY2","Change Order Status / Q");                      

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Order/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ChnStatusQryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add( new FormHidden("vAction", null, null ));
      form.add(new NL(3));

      Table tab = new Table("1","center","Border=\"0\" width=\"60%\" COLS=2");

      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_TEMPLATE", "Select Delivery Template" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"ordtemp.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"");
      HtmlTag sel =  util.createList( nUserID, "TR_ORDER", "vTemplName", "I", vOrdTemplQry, null, null, vListAttrib);
      TableCol col1 = new TableCol( sel,null, null, null, null );
      row.add(col);
      row.add(col1);

      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDER_ID_FROM","Order ID From");
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"FrOrdNo.gif", vBPLate, vLabelAttrib ),null, null, null," WIDTH=\"20%\" ")) ;
      TableCol col2 = new TableCol(util.createTextItem( nUserID, "TR_STATUS", "vFrom_Order_ID", pvMode, "15", "30",null, null, vTextAttrib),null, null, null,null);
      row1.add(col2) ;			

      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDER_ID_TO","Order ID To");
      row2.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"ToOrdNo.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      TableCol col3 = new TableCol(util.createTextItem( nUserID, "TR_STATUS", "vTo_Order_ID", pvMode, "15", "30",null, null,vTextAttrib),null, null, null,null);
      row2.add(col3) ;		

      tab.add(row);	
      tab.add(row1);
      tab.add(row2);
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

      String vFrom_Order_ID = request.getParameter("vFrom_Order_ID");
      String vTo_Order_ID = request.getParameter("vTo_Order_ID");
      String vTemplName = request.getParameter("vTemplName");
      String vAction = request.getParameter("vAction");

      if( vAction.equalsIgnoreCase("List") )
         response.sendRedirect("/JOrder/servlets/PrintOrderList?vFrom_Order_ID="+vFrom_Order_ID+"&vTo_Order_ID="+vTo_Order_ID+"&vTemplName="+vTemplName+"");
      else 
         response.sendRedirect("/JOrder/servlets/ChnStatusFrame?pvMode=T&vFrom_Order_ID="+vFrom_Order_ID+"&vTo_Order_ID="+vTo_Order_ID+"&vTemplName="+vTemplName+"&pvMessage=");
   }
}

