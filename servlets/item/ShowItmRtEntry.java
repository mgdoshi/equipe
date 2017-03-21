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

public class ShowItmRtEntry extends HttpServlet
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

      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String vImagePath=null;
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

      DBConnect db = new DBConnect();
      Message msg = new Message();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ItemRate/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vFromDate  = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FROM_DT", "From Date" );
      vToDate    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_TO_DT", "To Date" );
      vMinQty    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MINQTY", "Min Qty" );
      vMaxQty    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MAXQTY", "Max Qty" );
      vUnitPrice = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_UNITPRICE", "Unit Price" );

      Page page = new Page();
      Head head = new Head();
      Title title = new Title( "Order Entry & Tracking System :- Item Rates " );
      head.add(title);
      Body body = new Body("/ordimg/BACKGR2.GIF",null);
      Form form = new Form();

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +

                  " var oldWin = this.opener.right_frame.document.forms[0]             \n"+
                  " function submit_form()  {           	      	               \n"+
                  "   with( document.forms[0] )  {                                     \n"+
                  "     for ( var i=0; i<5; i++ ) {                                    \n"+
                  " 	  aField = new Array( \""+ vFromDate +"\", dFrom_Dt[i].value, \""+ vToDate +"\", dTo_Dt[i].value,"+
                                      " \""+ vMinQty +"\", nMinQty[i].value, \""+ vMaxQty +"\", nMaxQty[i].value, \""+ vUnitPrice +"\", nUnitPrice[i].value ) \n"+
                  "       vErrMsg = this.opener.top.check_fields( aField )             \n"+
                  "	  if ( vErrMsg == \"\" || this.opener.top.isRowBlank( vErrMsg, 5 ) ) { \n"+
                  "         continue                                                   \n"+
                  "	  }                                                            \n"+
                  "       else {                                                       \n"+
                  "         vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID ) +"\" + vErrMsg + \".\" \n"+
                  "	    alert( vErrMsg ); return                                   \n"+
                  "       }                                                            \n"+
                  "     }      						               \n"+
                  "     for ( var i=0; i<5;  i++ ) {                                   \n"+
                  "       oldWin.nUnitPrice[i].value = nUnitPrice[i].value             \n"+
                  "       oldWin.dFrom_Dt[i].value   = dFrom_Dt[i].value               \n"+
                  "       oldWin.dTo_Dt[i].value     = dTo_Dt[i].value                 \n"+
                  "       oldWin.nMinQty[i].value    = nMinQty[i].value                \n"+
                  "       oldWin.nMaxQty[i].value    = nMaxQty[i].value                \n"+
                  "       oldWin.nActive[i].value    = ( nInActive[i].checked ? 1 : 0 )\n"+
                  "     }       						       \n"+
                  "     top.close()   						       \n"+
                  "   }					              	               \n"+
                  " }					              	               \n"+
                  "// End Hidding -->");
      scr.add(scrdata);
      NL blines = new NL(1);
      form.add(blines);
      TableCol blank = new TableCol( "&nbsp;",null, null, null,null);
      Table tab = new Table("1","center","Border=\"0\" width=\"85%\" COLS=4");
      TableRow row = new TableRow("Left",null,null);
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"fromdt.gif", vFromDate, vLabelAttrib ),null, null, null,null);
      col.add( WebUtil.NotNull );
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"todt.gif", vToDate, vLabelAttrib ),null, null, null,null);
      col1.add( WebUtil.NotNull );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"minqty.gif", vMinQty, vLabelAttrib ),null, null, null,null);
      col2.add( WebUtil.NotNull );
      TableCol col3 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"maxqty.gif", vMaxQty, vLabelAttrib ),null, null, null,null);
      col3.add( WebUtil.NotNull );
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unit.gif", vUnitPrice, vLabelAttrib ),null, null, null,null);
      col4.add( WebUtil.NotNull );
      vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_INACTIVE", "Active" );
      TableCol col5 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"active.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      row.add(col);
      row.add(col1);
      row.add(col2);
      row.add(col3);
      row.add(col4);
      row.add(col5);
      TableRow row1 = new TableRow("Left",null,null);
      TableCol col6 = new TableCol( util.createLabelItem( "DD.MM.YYYY", vLabelAttrib ), null, null,null,null);
      row1.add(col6);
      row1.add(col6);
      row1.add(blank);
      row1.add(blank);
      row1.add(blank);
      row1.add(blank);
      tab.add(row);
      tab.add(row1);
      TableRow row2 = new TableRow("Left",null,null);
      row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dFrom_Dt", "I", "6", "10", null, "onBlur=\"oldWindow.check_date(this)\"", vTextAttrib),null,null,null,null) );
      row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dTo_Dt", "I", "6", "10", null, "onBlur=\"oldWindow.check_date(this)\"", vTextAttrib),null,null,null,null) );
      row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMinQty", "I", "6", "100", null, "onBlur=\"oldWindow.check_num(this)\"", vTextAttrib),null,null,null,null) );
      row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMaxQty", "I", "6", "100", null, "onBlur=\"oldWindow.check_num(this)\"", vTextAttrib),null,null,null,null) );
      row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nUnitPrice", "I", "8", "100", null, "onBlur=\"oldWindow.check_num(this)\"", vTextAttrib),null,null,null,null) );
      row2.add( new TableCol(new FormCheckBox( "nInActive", null, null, null ), "Center", null, null, null) );
      for( int i=0; i<5; i++ )
        tab.add(row2);
      Center cen = new Center();
      form.add(tab);
      form.add(new NL(1));
      cen.add(new FormButton( null, " OK ", "onClick=\"submit_form()\"" ));
      cen.add(new FormButton( null, "Cancel", "onClick=\"top.close()\"" ));
      form.add(cen);
      body.add(form);
      head.add(scr);
      page.add(head);
      page.add(body);
      out.println(page);
    }
}

