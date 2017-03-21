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

public class ClnItmRtUpdForm extends HttpServlet
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

      String nTransID = null; 
      String vImagePath=null;
      String rClnItmRt[]=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String temp=null;
      String vFromDate = null;
      String vToDate   = null;
      String vMinQty   = null;
      String vMaxQty   = null;
      String vUnitPrice = null;
      String vFormType = null;
      String istatus = null; 
      String vItemName = null;

      String nAuditID = null;
      String nUserID  = null;
      String nLangID  = null;
      String nSchemeID  = null;

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID  = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      DBConnect db = new DBConnect();
      Message msg = new Message();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      String pnClientID = request.getParameter("pnClientID");
      String pnClientItemRateID = request.getParameter("pnClientItemRateID");
      String pvWhereClause = request.getParameter("pvWhereClause");

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ItemRate/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vFormType  = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "WD_FORM_UPDATE", "Client Item Rate / U" );
      vFromDate  = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_FROM_DT", "From Date" );
      vToDate    = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_TO_DT", "To Date" );
      vMinQty    = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_MINQTY", "Min Qty" );
      vMaxQty    = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_MAXQTY", "Max Qty" );
      vUnitPrice = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_UNITPRICE", "Unit Price" );
      vItemName  = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_INACTIVE", "Item Name" );

      if(pnClientItemRateID!=null && !pnClientItemRateID.equals("") && !pnClientItemRateID.equalsIgnoreCase("null"))
      { 
        rClnItmRt = db.getRecord(pnClientItemRateID,"ClientItemRate");
      }  
      else
        rClnItmRt = new String[8];

      Page page = new Page();
      Head head = new Head();
      Title title = new Title( "Order Entry & Tracking System :- Update/Delete Item Rates " );
      head.add(title);
      Body body = new Body("/ordimg/BACKGR2.GIF",null);
      Form form = new Form("/JOrder/servlets/ClnItmRtUpdForm","POST","_parent",null,null);
      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form( '"+vFormType+"' ) \n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add( new FormHidden("pnTransID", nTransID, null ) );
      form.add( new FormHidden("pnClientID", pnClientID, null ) );
      form.add( new FormHidden("nClientItemRate_ID", pnClientItemRateID, null ) );
      form.add( new FormHidden("pvWhereClause", pvWhereClause, null ) );
      form.add( new FormHidden("nInActive", null, null ) );
      form.add(new NL(3));

      TableCol blank = new TableCol( "&nbsp;",null, null, null,null);

      Table tab = new Table("1","center","Border=\"0\" width=\"45%\"");

      TableRow row = new TableRow("Left",null,null);
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"fromdt.gif", vFromDate, vLabelAttrib ),null, null, null,null);
      col.add(WebUtil.NotNull);
      TableCol col1 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEMRATE", "dFrom_Dt", "U", "10", "10", IngDate.dateToStr(rClnItmRt[2]), "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null);
      col1.add(util.createLabelItem("DD.MM.YYYY",vLabelAttrib));
      row.add(col);
      row.add(col1);

      TableRow row1 = new TableRow("Left",null,null);
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"todt.gif", vToDate, vLabelAttrib ),null, null, null,null);
      col2.add(WebUtil.NotNull);
      TableCol col3 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEMRATE", "dTo_Dt", "U", "10", "10", IngDate.dateToStr(rClnItmRt[3]), "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null);
      col3.add(util.createLabelItem("DD.MM.YYYY",vLabelAttrib));
      row1.add(col2);
      row1.add(col3);

      TableRow row2 = new TableRow("Left",null,null);
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"minqty.gif", vMinQty, vLabelAttrib ),null, null, null,null);
      col4.add(WebUtil.NotNull);
      TableCol col5 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEMRATE", "nMinQty", "U", "10", "100", rClnItmRt[4], "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null);
      row2.add(col4);
      row2.add(col5);

      TableRow row3 = new TableRow("Left",null,null);
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"maxqty.gif", vMaxQty, vLabelAttrib ),null, null, null,null);
      col6.add(WebUtil.NotNull);
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEMRATE", "nMaxQty", "U", "10", "100", rClnItmRt[5], "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null);
      row3.add(col6);
      row3.add(col7);

      TableRow row4 = new TableRow("Left",null,null);
      TableCol col8 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unit.gif", vUnitPrice, vLabelAttrib ),null, null, null,null);
      col8.add(WebUtil.NotNull);
      TableCol col9 = new TableCol(util.createTextItem( nUserID, "ST_CLIENTITEMRATE", "nUnitPrice", "U", "10", "100", rClnItmRt[6], "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null);
      row4.add(col8);
      row4.add(col9);

      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_INACTIVE", "In Active" );
      TableCol col10 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"InActive.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      if(rClnItmRt[7].equals("1"))
      {
         istatus = "CHECKED";
      }
      TableCol col11 = new TableCol(new FormCheckBox( "nInActive", null, istatus, null ),null, null, null, null);
      row5.add(col10);
      row5.add(col11);

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

      String nLangID=null;
      String nUserID=null;
      String errMsg=null;  
      String vStatus=null;
      String query = null;

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );


      String pvWhereClause = URLEncoder.encode(request.getParameter("pvWhereClause"));
      String pnTransID = request.getParameter("pnTransID");
      String pnClientID = request.getParameter("pnClientID");
      String nClientItemRate_ID = request.getParameter("nClientItemRate_ID");
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
  
      String usr = db.getName( nUserID, "User" );
      vStatus = Trans.checkTransValidity( pnTransID );
      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();
          query = " UPDATE T_ClientItemRate "+
                     " SET From_Dt = '"+ IngDate.strToDate(dFromDt) +"',"+
                     " To_Dt   = '"+ IngDate.strToDate(dToDt)   +"',"+
                     " MinQty  = "+nMinQty+","+
                     " MaxQty  = "+nMaxQty+","+
                     " UnitPrice = "+nUnitPrice+","+
                     " InActive  = "+nInActive[0]+","+ 
                     " Modifier  = '"+usr+"',"+
                     " Change_Dt = '"+dt+"'"+
                     " WHERE ClientItemRate_ID = "+nClientItemRate_ID;
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
     if(nMsgID <= 5 )
     { 
       response.sendRedirect("/JOrder/servlets/ClnItmRtFrame?pvMode=T&pnClientID="+pnClientID+"&pnClientItemRateID=&pvWhereClause="+pvWhereClause+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     }
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

