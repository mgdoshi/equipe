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

public class ItemRtUpdDelForm extends HttpServlet
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
      String nTransID=null;
      String vImagePath=null;
      String rItmRt[]=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFromDate = null;
      String vToDate   = null;
      String vMinQty   = null;
      String vMaxQty   = null;
      String vUnitPrice = null;
      String vFormType = null;
      String istatus = null; 
      String vItemName = null;

      DBConnect db = new DBConnect();
      Message msg = new Message();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      String pnItemRateID = request.getParameter("pnItemRateID");
      String pvWhereClause = request.getParameter("pvWhereClause");

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ItemRate/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vFormType  = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FROM_DT", "From Date" );
      vFromDate  = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FROM_DT", "From Date" );
      vToDate    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_TO_DT", "To Date" );
      vMinQty    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MINQTY", "Min Qty" );
      vMaxQty    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MAXQTY", "Max Qty" );
      vUnitPrice = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_UNITPRICE", "Unit Price" );
      vItemName  = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FK_ITEM_ID", "Item Name" );

      String vItemQry = " SELECT itm.Item_ID, itm.Item_Name   " +
                        " FROM   T_Item itm, T_UserItem uit   " +
                        " WHERE  uit.Fk_Item_ID = itm.Item_ID " +
                        " AND    uit.Fk_User_ID = " + nUserID +
                        " AND    itm.InActive = '1'" +
                        " ORDER BY itm.Item_Name";

      if(pnItemRateID!=null && !pnItemRateID.equals("") && !pnItemRateID.equalsIgnoreCase("null"))
      { 
        rItmRt = db.getRecord( pnItemRateID, "ItemRate" );
      }  
      else
        rItmRt = new String[8];

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null);
      Form form = new Form("ItemRtUpdDelForm","POST","_parent",null,null);
      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form( '"+vFormType+"' ) \n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add( new FormHidden("pnTransID", nTransID, null ) );
      form.add( new FormHidden("pnItemRateID", pnItemRateID, null ) );
      form.add( new FormHidden("nInActive", null, null ) );
      form.add( new FormHidden("pvWhereClause", pvWhereClause, null ) );
      form.add(new NL(3));

      TableCol blank = new TableCol( "&nbsp;",null, null, null,null);

      Table tab = new Table("1","center","Border=\"0\" width=\"45%\"");

      TableRow row0 = new TableRow("Left",null,null);
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"fromdt.gif", vItemName, vLabelAttrib ),null, null, null,null);
      col.add( WebUtil.NotNull );
      row0.add(col);
      row0.add(new TableCol(util.createList( nUserID, "ST_ITEMRATE", "nFk_Item_ID", "U", vItemQry, rItmRt[1], null, vListAttrib ),null,null,null,null));

      TableRow row = new TableRow("Left",null,null);
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"fromdt.gif", vFromDate, vLabelAttrib ),null, null, null,null);
      col1.add( WebUtil.NotNull );
      TableCol col2 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dFrom_Dt", "U", "10", "10", IngDate.dateToStr(rItmRt[2]), "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null);
      col2.add( util.createLabelItem( "DD.MM.YYYY", vLabelAttrib ) );
      row.add(col1);
      row.add(col2);

      TableRow row1 = new TableRow("Left",null,null);
      TableCol col3 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"todt.gif", vToDate, vLabelAttrib ),null, null, null,null);
      col3.add( WebUtil.NotNull );
      TableCol col4 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dTo_Dt", "U", "10", "10", IngDate.dateToStr(rItmRt[3]), "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null);
      col4.add( util.createLabelItem( "DD.MM.YYYY", vLabelAttrib ) );
      row1.add(col3);
      row1.add(col4);

      TableRow row2 = new TableRow("Left",null,null);
      TableCol col5 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"minqty.gif", vMinQty, vLabelAttrib ),null, null, null,null);
      col5.add( WebUtil.NotNull );
      row2.add(col5);
      row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMinQty", "U", "10", "100", rItmRt[4], "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null) );

      TableRow row3 = new TableRow("Left",null,null);
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"maxqty.gif", vMaxQty, vLabelAttrib ),null, null, null,null);
      col6.add( WebUtil.NotNull );
      row3.add( col6 );
      row3.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMaxQty", "U", "10", "100", rItmRt[5], "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null) );

      TableRow row4 = new TableRow("Left",null,null);
      TableCol col7 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unit.gif", vUnitPrice, vLabelAttrib ),null, null, null,null);
      col7.add( WebUtil.NotNull );
      row4.add( col7 );
      row4.add(new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nUnitPrice", "U", "10", "100", rItmRt[6], "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null));

      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_INACTIVE", "In Active" );
      if(rItmRt[7].equals("1"))
      {
         istatus = "CHECKED";
      }
      row5.add( new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"InActive.gif", vBPLate, vLabelAttrib ),null, null, null,null) );
      row5.add( new TableCol(new FormCheckBox( "nInActive", null, istatus, null ),null, null, null, null) );

      tab.add(row0);
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
      int nMsgID=-1;
      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nTransID=null;
      String errMsg=null;
      String vStatus=null;
      String mode=""; 
      String query = null;

      String pvWhereClause = request.getParameter("pvWhereClause");
      String pnTransID = request.getParameter("pnTransID");
      String nFk_Item_ID = request.getParameter("nFk_Item_ID");
      String pnItemRateID = request.getParameter("pnItemRateID");
      String dFromDt = request.getParameter("dFrom_Dt");
      String dToDt = request.getParameter("dTo_Dt");
      String nMinQty = request.getParameter("nMinQty");
      String nMaxQty = request.getParameter("nMaxQty");
      String nUnitPrice = request.getParameter("nUnitPrice");
      String nInActive[] = request.getParameterValues("nInActive");

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      Valid val = new Valid();
      Message msg = new Message(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();
  
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String usr = db.getName( nUserID, "User" );
      vStatus = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();
          query = " UPDATE T_ItemRate "+
                  " SET From_Dt = '"+ IngDate.strToDate(dFromDt) +"',"+
                  " Fk_Item_ID  = '"+ nFk_Item_ID +"',"+
                  " To_Dt   = '"+ IngDate.strToDate(dToDt)   +"',"+
                  " MinQty  = "+nMinQty+","+
                  " MaxQty  = "+nMaxQty+","+
                  " UnitPrice = "+nUnitPrice+","+
                  " InActive  = "+nInActive[0]+","+ 
                  " Modifier  = '"+usr+"',"+
                  " Change_Dt = '"+dt+"'"+
                  " WHERE ItemRate_ID = "+pnItemRateID;
          stmt.executeUpdate(query);
          nMsgID = 5;
       }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=8;}
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
       Trans.setTransID(pnTransID);
     }
     if( nMsgID <= 5 )
       response.sendRedirect("/JOrder/servlets/ItemRtFrame?pvMode=&pnItemRateID=&pvWhereClause="+pvWhereClause+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

