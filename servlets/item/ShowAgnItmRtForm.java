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

public class ShowAgnItmRtForm extends HttpServlet
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
      String temp=null;
      String vFromDate = null;
      String vToDate   = null;
      String vMinQty   = null;
      String vMaxQty   = null;
      String vUnitPrice = null;

      DBConnect db = new DBConnect();
      Message msg = new Message();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      String pnItemID = request.getParameter("pnItemID");

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
      Form form = new Form( "ShowAgnItmRtForm", "POST", "_parent", null, null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +

                  "   function submit_form()   {                        	                     \n"+
                  "     var nRowCount = 0    			                                     \n"+
                  "     with( document.forms[0] ) {                     	                     \n"+
                  "	  for ( var i=0; i<dFrom_Dt.length; i++ ) {                                  \n"+
                  " 	    var aField = new Array( \"" + vFromDate + "\", dFrom_Dt[i].value, \"" + vToDate +"\", dTo_Dt[i].value,  "+
                                          " \"" + vMinQty +"\", nMinQty[i].value, \""+ vMaxQty +"\", nMaxQty[i].value, \"" + vUnitPrice +"\", nUnitPrice[i].value ) \n"+
                  "	    var vErrMsg = this.opener.top.check_fields( aField )                     \n"+
                  "	    if ( vErrMsg == \"\" ) continue                                          \n"+
                  "	    if ( this.opener.top.isRowBlank( vErrMsg, 5 ) ) {                        \n"+
                  "	       nRowCount++; continue                                                 \n"+
                  "	    }                                                                        \n"+
                  "         else {                                                                   \n"+
                  "	      vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg + \".\"      \n"+
                  "           alert( vErrMsg ); dFrom_Dt[i].focus(); dFrom_Dt[i].select(); return    \n"+
                  "	    }                                                                        \n"+
                  "	 }                                                                           \n"+
                  "	 if ( nRowCount == dFrom_Dt.length ) {                                       \n"+
                  "	   alert( \"" + msg.GetMsgDesc(17,nLangID) + "\" ); dFrom_Dt[0].focus(); dFrom_Dt[0].select(); return \n"+
                  "      }  					                                     \n"+
                  "      nInActive[0].value = \"\"  		                                     \n"+
                  "      for( var i=1; i<nInActive.length; i++ )                                     \n"+
                  "	   nInActive[0].value += ( nInActive[i].checked ? \"1\" : \"0\" ) +\"~\"     \n"+
                  "	 if ( confirm( \""+ msg.GetMsgDesc( 10, nLangID ) + "\" ) ) {                \n"+
                  "         submit()					                             \n"+
                  "      }  					                                     \n"+
                  "    }						                             \n"+
                  "  }  						                             \n"+

                  "// End Hidding -->");
      scr.add(scrdata);

      form.add( new NL(1) );
      form.add( new FormHidden( "pnItemID", pnItemID, null ) );
      form.add( new FormHidden( "nInActive", null, null ) );

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
      TableCol col6 = new TableCol( util.createLabelItem("DD.MM.YYYY", vLabelAttrib ),null,null,null,null);
      row1.add(col6);
      row1.add(col6);
      row1.add(blank);
      row1.add(blank);
      row1.add(blank);
      row1.add(blank);

      tab.add(row);
      tab.add(row1);

      for( int i=0; i<5; i++ )
      {
        TableRow row2 = new TableRow("Left",null,null);
        row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dFrom_Dt", "I", "6", "10", null, "onBlur=\"oldWindow.check_date(this)\"", vTextAttrib),null,null,null,null) );
        row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dTo_Dt", "I", "6", "10", null, "onBlur=\"oldWindow.check_date(this)\"", vTextAttrib),null,null,null,null) );
        row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMinQty", "I", "6", "100", null, "onBlur=\"oldWindow.check_num(this)\"", vTextAttrib),null,null,null,null) );
        row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMaxQty", "I", "6", "100", null, "onBlur=\"oldWindow.check_num(this)\"", vTextAttrib),null,null,null,null) );
        row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nUnitPrice", "I", "8", "100", null, "onBlur=\"oldWindow.check_num(this)\"", vTextAttrib),null,null,null,null) );
        row2.add( new TableCol(new FormCheckBox( "nInActive", null, null, null ), "Center", null, null, null));
        tab.add(row2);
      }

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

      int nMsgID =-1;
 
      String nLangID=null;
      String nUserID=null;
      String errMsg = null; 
      String query = null;
      Statement stmt = null;
      Connection conn = null;

      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();
  
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String pnItemID = request.getParameter("pnItemID");
      String dFromDt[] = request.getParameterValues("dFrom_Dt");
      String dToDt[] = request.getParameterValues("dTo_Dt");
      String nMinQty[] = request.getParameterValues("nMinQty");
      String nMaxQty[] = request.getParameterValues("nMaxQty");
      String nUnitPrice[] = request.getParameterValues("nUnitPrice");
      String nInActive[] = request.getParameterValues("nInActive");

      String usr  = db.getName( nUserID, "User" );      

      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();
        
        /* Insert T_ItemRate Data */
        for( int i=0; i<5; i++ )
        {
          if( !( nUnitPrice[i]==null || nUnitPrice[i].equals("") || nUnitPrice[i].equalsIgnoreCase("null") ) )
          {
            int itmrt_id = Integer.parseInt(db.getNextVal("S_ItemRate"));
            query = " INSERT INTO T_ItemRate( ItemRate_ID, Fk_Item_ID, From_Dt, "+
                    "             To_Dt, MinQty, MaxQty, UnitPrice, InActive, "+
                    "             Modifier, Change_Dt ) "+
                    " VALUES( "+itmrt_id+","+pnItemID+",'"+IngDate.strToDate(dFromDt[i])+"','"+IngDate.strToDate(dToDt[i])+"',"+nMinQty[i]+","+nMaxQty[i]+","+nUnitPrice[i]+",'"+Parse.GetSubString( nInActive[0], "~", i )+"','"+usr+"','"+dt+"')";
            stmt.executeUpdate(query);
          }          
        }
        stmt.close();
        conn.close();
        nMsgID = 3;
      }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=8;}

      if( nMsgID <= 5 )
      {
        Script scr = new Script();  
        HtmlTag scrdata = new HtmlTag();
        scrdata.add( " <!-- Start Hidding  \n"+
                     "   top.close()       \n"+
                     " // End Hidding -->  \n"); 
        scr.add(scrdata);
        out.println(scr);
      }
      else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

