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

public class ItemRtQryForm extends HttpServlet
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
      String vItemGroupQry=null;
      String vItemClassQry=null;
      String vUnitTypeQry=null;

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID    = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID  = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "ST_ITEMRATERATE", nLangID, "WD_QUERY", "Item / Q " );

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Item/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ItemRtQryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      NL blines = new NL(3);
      form.add(blines);
      Table tab = new Table("1","center","Border=\"0\" width=\"53%\" COLS=2");

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FK_ITEM_ID","Item Name");
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.gif", vBPLate, vLabelAttrib ),null, null, null, "WIDTH=\"25%\""); 
      TableCol col5 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "vItem_Name", "Q", "15", "30", "%", "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,null);
      row1.add(col4) ;
      row1.add(col5) ;

      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FROM_DT", "From Date" );
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Filter.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dFrom_Dt", "Q", "10", "10", null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null);
      col7.add(WebUtil.getBlankSpaces(2)+ util.createLabelItem( "DD.MM.YYYY", vLabelAttrib ) );
      row2.add(col6);
      row2.add(col7);

      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_TO_DT", "To Date" );
      TableCol col8 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Filter.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col9 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dTo_Dt", "Q", "10", "10", null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null);
      col9.add(WebUtil.getBlankSpaces(2)+ util.createLabelItem( "DD.MM.YYYY", vLabelAttrib ));
      row3.add(col8);
      row3.add(col9);

      TableRow row4 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MINQTY", "Min Qty" );
      TableCol col10 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Filter.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col11 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMinQty", "Q", "10", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null);

      row4.add(col10);
      row4.add(col11);

      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MAXQTY", "Max Qty" );
      TableCol col12 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Filter.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col13 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMaxQty", "Q", "10", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null);

      row5.add(col12);
      row5.add(col13);
 
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

      String nLangID=null;
      String nUserID=null;
      String mode=""; 

      String vItemName = request.getParameter("vItem_Name");
      String dFrom_Dt = request.getParameter("dFrom_Dt");
      String dTo_Dt = request.getParameter("dTo_Dt");
      String nMaxQty = request.getParameter("nMaxQty");
      String nMinQty = request.getParameter("nMinQty");
      String vAction = request.getParameter("vAction"); 

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID    = Parse.GetValueFromString( vPID, "LangID" );

      vWhereClause = " WHERE itr.fk_item_id = itm.item_id ";

      if( !( vItemName==null || vItemName.equals("") || vItemName.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND itm.Item_Name LIKE '"+vItemName+"'";
      if( !( dFrom_Dt==null || dFrom_Dt.equals("") || dFrom_Dt.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND itr.From_Dt >= '"+IngDate.strToDate( dFrom_Dt.trim())+"'";
      if( !( dTo_Dt==null || dTo_Dt.equals("") || dTo_Dt.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND itr.To_Dt <= '"+ IngDate.strToDate( dTo_Dt.trim())+"'";
      if( !( nMinQty==null || nMinQty.equals("") || nMinQty.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND itr.MinQty >= "+nMinQty;
      if( !( nMaxQty==null || nMaxQty.equals("") || nMaxQty.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND itr.MaxQty <= "+nMaxQty;
    
      vWhereClause = vWhereClause + " ORDER BY itm.item_name" ;

      vWhereClause = URLEncoder.encode( vWhereClause );

      if( vAction!=null && vAction.equalsIgnoreCase("New"))
        response.sendRedirect("/JOrder/servlets/ItemRtFrame?pvMode=I&pnItemRateID=&pvWhereClause="+vWhereClause+"&pvMessage=");
      else
        response.sendRedirect("/JOrder/servlets/ItemRtFrame?pvMode=&pnItemRateID=&pvWhereClause="+vWhereClause+"&pvMessage=");
    }
}

