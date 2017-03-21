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

public class DelRepQryForm extends HttpServlet
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
      String vOrdTemplQry=null;
      String vDelStatQry = null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );
      
      String pvMode = request.getParameter("pvMode"); 

      DBConnect db = new DBConnect(); 
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      vFormType = cdata.GetConfigValue( "TR_DELIVERY", nLangID, "WD_FORM_QUERY","Delivery Report / Q");                      

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Delivery/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vDelStatQry = " SELECT Attrib, Attrib_Desc FROM T_Domain WHERE Domain = 'DELSTAT' " +
                    " AND Fk_Lang_ID = "+nLangID+"" +
                    " ORDER BY Sequence_NR ";

      vOrdTemplQry = " SELECT Alias_Name, Alias_Name" +
                     " FROM T_ClsTemplDel" +
                     " Where Ref_ID = "+ nUserID;
    
      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/DelRepQryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add( new FormHidden("vAction", null, null ));
      form.add(new NL(3));

      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2");

      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_TEMPLATE", "Select Delivery Order Template" );
      TableCol cl = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"ordtemp.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"30%\"");
      HtmlTag sel =  util.createList( nUserID, "TR_ORDER", "vTemplName", "I", vOrdTemplQry, null, null, vListAttrib);
      TableCol cl1 = new TableCol( sel,null, null, null, null );
      row.add(cl);
      row.add(cl1);

      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_DELIVERY_NR_FROM","Delivery No From");
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"FrDelNo.gif", vBPLate, vLabelAttrib ),null, null, null," WIDTH=\"20%\" ")) ;
      row1.add(new TableCol(util.createTextItem( nUserID, "TR_DELVREP", "vFrom_Delivery_Nr", pvMode, "10", "15",null, null, vTextAttrib),null, null, null,null));

      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_DELIVERY_NR_TO","Delivery No To");
      row2.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TodelNo.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      row2.add(new TableCol(util.createTextItem( nUserID, "TR_DELVREP", "vTo_Delivery_Nr", pvMode, "10", "15",null, null, vTextAttrib),null, null, null,null));

      TableRow row3 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_DELIVERY_DT_TO","Delivery Date From");
      row3.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"FrOdrDt.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      TableCol col2 = new TableCol(util.createTextItem( nUserID, "TR_DELVREP", "dFrom_Delivery_Dt", pvMode, "10", "10",null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null, null, null,null);
      col2.add(util.createLabelItem("DD.MM.YYYY",vLabelAttrib));
      row3.add(col2) ;			

      TableRow row4 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_DELIVERY_DT_TO","Delivery Date To");
      row4.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"FrOdrDt.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      TableCol col3 = new TableCol(util.createTextItem( nUserID, "TR_DELVREP", "dTo_Delivery_Dt", pvMode, "10", "10",null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null, null, null,null);
      col3.add(util.createLabelItem("DD.MM.YYYY",vLabelAttrib));
      row4.add(col3) ;			

      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_DM_DELSTAT","Delivery Status");
      row5.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"OrdStat.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row5.add(new TableCol(util.createList( nUserID, "TR_DELVREP", "cDM_DelStat",pvMode, vDelStatQry,null, null, vListAttrib), null,null,null,null));

      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row4);
      tab.add(row5);
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

      String vTemplName = request.getParameter("vTemplName");
      String vFrom_Delivery_NR = request.getParameter("vFrom_Delivery_NR");
      String vTo_Delivery_NR = request.getParameter("vTo_Delivery_NR");
      String dFrom_Delivery_Dt = request.getParameter("dFrom_Delivery_Dt");
      String dTo_Delivery_Dt = request.getParameter("dTo_Delivery_Dt");
      String cDM_DelStat = request.getParameter("cDM_DelStat");
      String vAction = request.getParameter("vAction");

      if( vTemplName==null || vTemplName.equals("") || vTemplName.equalsIgnoreCase("null") )
        response.sendRedirect("/JOrder/servlets/Delivery?Flag=0");
      if( vAction.equalsIgnoreCase("List") )
        response.sendRedirect("/JOrder/servlets/PrintDeliveryList?vFrom_Delivery_NR="+vFrom_Delivery_NR+"&vTo_Delivery_NR="+vTo_Delivery_NR+"&dFrom_Delivery_Dt="+dFrom_Delivery_Dt+"&dTo_Delivery_Dt="+dTo_Delivery_Dt+"&cDM_DelStat="+cDM_DelStat+"");
      else   
        response.sendRedirect("/JOrder/servlets/DelRepFrame?pvMode=T&vFrom_Delivery_NR="+vFrom_Delivery_NR+"&vTo_Delivery_NR="+vTo_Delivery_NR+"&dFrom_Delivery_Dt="+dFrom_Delivery_Dt+"&dTo_Delivery_Dt="+dTo_Delivery_Dt+"&cDM_DelStat="+cDM_DelStat+"");
   }
}

