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

public class Temp1 extends HttpServlet
{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
    throws IOException, ServletException
    {
      long starttime = System.currentTimeMillis();
      long endtime;
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

      String nTransID=null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String temp=null;

      String pvWhereClause = request.getParameter("pvWhereClause");

      DBConnect db = new DBConnect();
      WebUtil util = new WebUtil();
      ConfigData cdata = new ConfigData();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID    = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID  = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      String vItemName  = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FK_ITEM_ID", "Item Name" );
      String vFromDate  = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FROM_DT", "From Date" );
      String vToDate    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_TO_DT", "To Date" );
      String vMinQty    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MINQTY", "Min Qty" );
      String vMaxQty    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MAXQTY", "Max Qty" );
      String vUnitPrice = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_UNITPRICE", "Unit Price" );
      String viActive = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_INACTIVE", "Active" );


      vFormType = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "WD_FORM_INSERT","ItemRate / I");
      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ItemRate/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      String vItemQry = " SELECT itm.Item_ID, itm.Item_Name   " +
                 " FROM   T_Item itm, T_UserItem uit   " +
                 " WHERE  uit.Fk_Item_ID = itm.Item_ID " +
                 " AND    uit.Fk_User_ID = " + nUserID +
                 " AND    itm.InActive = '1'" +
                 " ORDER BY itm.Item_Name";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ItemRtEntryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add(new FormHidden("pvWhereClause", pvWhereClause, null ));
      form.add(new FormHidden("nInActive", null, null ));
      form.add(new NL(1));

      TableCol blank = new TableCol( "&nbsp;",null, null, null,null);
      Table tab = new Table("1","center","Border=\"0\" width=\"85%\"");
 
      TableRow row = new TableRow("Left",null,null);
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"name.gif", vItemName, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"fromdt.gif", vFromDate, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"todt.gif", vToDate, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"minqty.gif", vMinQty, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"maxqty.gif", vMaxQty, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unit.gif", vUnitPrice, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Inactive.gif", viActive, vLabelAttrib ),null, null, null,null));

      TableRow row1 = new TableRow("Left",null,null);
      TableCol col6 = new TableCol("DD.MM.YYYY",null,null,null,null);
      row1.add(blank);
      row1.add(col6);
      row1.add(col6);
      row1.add(blank);
      row1.add(blank);
      row1.add(blank);
      row1.add(blank);

      tab.add(row);
      tab.add(row1);

      HtmlTag itemlist = util.createList( nUserID, "ST_ITEMRATE", "nFk_Item_ID", "I", vItemQry, null, null, vListAttrib );
      TableRow row2 = new TableRow("Left",null,null);
      row2.add(new TableCol(itemlist,null,null,null,null));
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dFrom_Dt", "I", "6", "10", null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null));
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dTo_Dt", "I", "6", "10", null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null));
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMinQty", "I", "6", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null));
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMaxQty", "I", "6", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null));
      row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nUnitPrice", "I", "8", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null));
      row2.add(new TableCol(new FormCheckBox( "nInActive", null, null, null ), "Center", null, null, null));
      for( int i=0; i < 10; i++ )
        tab.add(row2);
      form.add(tab); 
      body.add(form);
      head.add(scr);
      page.add(head);
      page.add(body);
      endtime = System.currentTimeMillis();
      page.add(endtime + " " + starttime);
      out.println(page);
    }
}
