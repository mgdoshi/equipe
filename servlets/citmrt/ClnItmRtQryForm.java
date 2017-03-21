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

public class ClnItmRtQryForm extends HttpServlet
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
      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;

      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vClientQry=null;

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID    = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID  = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "WD_QUERY", "Client Item Rate / Q" );

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ClnItmRt/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if( nUserID!=null && nUserID.equalsIgnoreCase("0") )
         vClientQry = " SELECT cln.Client_ID, cln.Client_Name   "+
                      " FROM   T_Client cln                     "+
                      " Order By cln.Client_Name";
      else
         vClientQry = " SELECT cln.Client_ID, cln.Client_Name   "+
                      " FROM   T_Client cln, T_UserClient ucl   "+
                      " WHERE  ucl.Fk_Client_ID = cln.Client_ID "+
                      " AND    ucl.Fk_User_ID = "+ nUserID +
                      " Order By cln.Client_Name";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ClnItmRtQryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      NL blines = new NL(3);
      form.add(blines);
      Table tab = new Table("1","center","Border=\"0\" width=\"55%\" COLS=2");

      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_FK_CLIENT_ID", "Client Name");
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"clnname.gif", vBPLate, vLabelAttrib ),null, null, null, "WIDTH=\"30%\"");
      HtmlTag sel =  util.createList( nUserID, "ST_CLIENTITEMRATE", "nFk_Client_ID", "Q", vClientQry, null, null, vListAttrib);
      TableCol col2 = new TableCol( sel, null, null, "2", null);
      col1.add(WebUtil.NotNull);
      row.add(col1);
      row.add(col2);

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_FK_ITEM_ID", "Item Name" );
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"itmname.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col5 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEMRATE", "vItem_Name", "Q", "15", "30", "%", "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,null);
      row1.add(col4) ;
      row1.add(col5) ;

      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_FROM_DT", "From Date" );
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"fromdt.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEMRATE", "dFrom_Dt", "Q", "10", "10", null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null);
      col7.add(util.createLabelItem("DD.MM.YYYY", vLabelAttrib));
      row2.add(col6);
      row2.add(col7);

      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_TO_DT", "To Date" );
      TableCol col8 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"todt.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col9 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEMRATE", "dTo_Dt", "Q", "10", "10", null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null);
      col9.add(util.createLabelItem("DD.MM.YYYY", vLabelAttrib));
      row3.add(col8);
      row3.add(col9);

      TableRow row4 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_MINQTY", "Min Qty" );
      TableCol col10 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Filter.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col11 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEMRATE", "nMinQty", "Q", "10", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null);

      row4.add(col10);
      row4.add(col11);

      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_MAXQTY", "Max Qty" );
      TableCol col12 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Filter.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col13 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEMRATE", "nMaxQty", "Q", "10", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null);

      row5.add(col10);
      row5.add(col11);

      tab.add(row);
      tab.add(row1); 
      tab.add(row2); 
      tab.add(row3); 
      tab.add(row4); 
      tab.add(row5); 

      FormHidden action = new FormHidden("vAction", null, null);
      form.add(action);
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
      String vWhereClause = null;
      if(vPID==null)
      {
        out.println(WebUtil.IllegalEntry());
        return;
      }

      String temp=null;
      String mode=""; 

      String pnClientID = request.getParameter("nFk_Client_ID");
      String vItemName = request.getParameter("vItem_Name");
      String dFrom_Dt = request.getParameter("dFrom_Dt");
      String dTo_Dt = request.getParameter("dTo_Dt");
      String nMaxQty = request.getParameter("nMaxQty");
      String nMinQty = request.getParameter("nMinQty");
      String vAction = request.getParameter("vAction"); 

      vWhereClause = " WHERE itm.item_id=cit.fk_item_id AND cit.clientitem_id = cir.fk_clientitem_id ";
      if( !( vItemName==null || vItemName.equals("") || vItemName.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND itm.Item_Name LIKE '"+vItemName+"'";
      if( !( dFrom_Dt==null || dFrom_Dt.equals("") || dFrom_Dt.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND cir.From_Dt >= '"+dFrom_Dt.trim()+"'";
      if( !( dTo_Dt==null || dTo_Dt.equals("") || dTo_Dt.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND cir.To_Dt <= '"+ dTo_Dt.trim()+"'";
      if( !( nMinQty==null || nMinQty.equals("") || nMinQty.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND cir.MinQty >= "+nMinQty;
      if( !( nMaxQty==null || nMaxQty.equals("") || nMaxQty.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND cir.MaxQty <= '"+nMaxQty+"'";
    
      vWhereClause = URLEncoder.encode( vWhereClause );

      if( vAction!=null && vAction.equalsIgnoreCase("New"))
        response.sendRedirect("/JOrder/servlets/ClnItmRtFrame?pvMode=N&pnClientID="+pnClientID+"&pnClientItemRateID=&pvWhereClause="+vWhereClause+"&pvMessage=");
      else
        response.sendRedirect("/JOrder/servlets/ClnItmRtFrame?pvMode=T&pnClientID="+pnClientID+"&pnClientItemRateID=&pvWhereClause="+vWhereClause+"&pvMessage=");
    }
}

