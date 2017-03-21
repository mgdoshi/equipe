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

public class PenRepQryForm extends HttpServlet
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
      String vItemGroupQry=null;
      String vItemClassQry=null;
      String vOrdTemplQry=null;
      String vClientQry = null;
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

      vFormType  = cdata.GetConfigValue( "TR_ORDER", nLangID, "WD_FORM_QUERY1", "Pending Order Report / Q" );

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Order/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vOrdTemplQry = " SELECT Alias_Name, Alias_Name" + 
                     " FROM T_ClsTempl" +
                     " Where Ref_ID = "+ nUserID;

      vItemGroupQry = "SELECT ItemGroup_ID, ItemGroup_Name FROM T_ItemGroup " ;
      vItemClassQry = "SELECT ItemClass_ID, ItemClass_Name FROM T_ItemClass " ;

      if(nUserID.equalsIgnoreCase("0"))
      {
        vClientQry = " SELECT cln.Client_ID, cln.Client_Name " + 
                     " FROM   T_Client cln                   " +
                     " Order By cln.Client_Name";      
      }
      else
      {
        vClientQry = " SELECT cln.Client_ID, cln.Client_Name " +
                     " FROM   T_Client cln, T_UserClient ucl " +
                     " WHERE  ucl.Fk_Client_ID = cln.Client_ID " +
                     " AND    ucl.Fk_User_ID = "+nUserID+" "+ 
                     " Order By cln.Client_Name";
      }

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/PenRepQryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add( new FormHidden("vAction", null, null ));
      form.add(new NL(3));

      Table tab = new Table("1","center","Border=\"0\" width=\"80%\" COLS=2");

      TableRow rw = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_TEMPLATE", "Select Order Template" );
      TableCol cl = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"ordtemp.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"40%\"");
      HtmlTag sel =  util.createList( nUserID, "TR_ORDER", "vTemplName", "I", vOrdTemplQry, null, null, vListAttrib);
      TableCol cl1 = new TableCol( sel,null, null, null, null );
      rw.add(cl);
      rw.add(cl1);

      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("TR_ORDER", nLangID, "BL_LABEL.B_ORDER_CLIENT_NAME","Client Name");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"clName.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      col.add(WebUtil.NotNull); 
      row.add(col);
      row.add(new TableCol(util.createList( nUserID, "TR_PENDREP", "pnClientID",pvMode, vClientQry,null, null, vListAttrib), null,null,null,null));

      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDER_DT_FROM","Order Date From");
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"FrOdrDt.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      TableCol col2 = new TableCol(util.createTextItem( nUserID, "TR_PENDREP", "dFrom_Order_Dt", pvMode, "10", "10",null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null, null, null,null);
      col2.add(util.createLabelItem("DD.MM.YYYY",vLabelAttrib));
      row1.add(col2) ;			

      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDER_DT_TO","Order Date To");
      row2.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"FrOdrDt.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      TableCol col3 = new TableCol(util.createTextItem( nUserID, "TR_PENDREP", "dTo_Order_Dt", pvMode, "10", "10",null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null, null, null,null);
      col3.add(util.createLabelItem("DD.MM.YYYY",vLabelAttrib));
      row2.add(col3) ;			

      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_ITEM", nLangID, "BL_LABEL.B_ITEM_FK_ITEMGROUP_ID","Item Group");
      row3.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Group.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row3.add(new TableCol(util.createList( nUserID, "TR_PENDREP", "nFk_ItemGroup_ID","Q", vItemGroupQry,null, null, vListAttrib), null,null,null,null));

      TableRow row4 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_ITEM", nLangID, "BL_LABEL.B_ITEM_FK_ITEMCLASS_ID","Item Class Name");
      row4.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Class.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row4.add(new TableCol(util.createList( nUserID, "TR_PENDREP", "nFk_ItemClass_ID","Q", vItemClassQry,null, null, vListAttrib), null,null,null,null));

      tab.add(rw);
      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row4);
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
      String nFk_ItemGroup_ID = request.getParameter("nFk_ItemGroup_ID");
      String nFk_ItemClass_ID = request.getParameter("nFk_ItemClass_ID");
      String dFrom_Order_Dt = request.getParameter("dFrom_Order_Dt");
      String dTo_Order_Dt = request.getParameter("dTo_Order_Dt");
      String nClientID = request.getParameter("pnClientID");
      String vAction = request.getParameter("vAction");
      if( vTemplName==null || vTemplName.equals("") || vTemplName.equalsIgnoreCase("null") )
        response.sendRedirect("/JOrder/servlets/Manual?Flag=0");
      if( vAction.equalsIgnoreCase("List") )
         response.sendRedirect("/JOrder/servlets/PrintPenRepList?nFk_ItemGroup_ID="+nFk_ItemGroup_ID+"&nFk_ItemClass_ID="+nFk_ItemClass_ID+"&dFrom_Order_Dt="+dFrom_Order_Dt+"&dTo_Order_Dt="+dTo_Order_Dt+"&pnClientID="+nClientID+"&vTemplName="+vTemplName);
      response.sendRedirect("/JOrder/servlets/PenRepFrame?pvMode=T&nFk_ItemGroup_ID="+nFk_ItemGroup_ID+"&nFk_ItemClass_ID="+nFk_ItemClass_ID+"&dFrom_Order_Dt="+dFrom_Order_Dt+"&dTo_Order_Dt="+dTo_Order_Dt+"&pnClientID="+nClientID+"&vTemplName="+vTemplName+"&pvMessage=");
   }
}

