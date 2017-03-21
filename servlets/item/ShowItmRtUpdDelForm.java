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

public class ShowItmRtUpdDelForm extends HttpServlet
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
      String vStatus="CHECKED";
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

      String pnItemRateID = request.getParameter("pnItemRateID");
      String pnItemID = request.getParameter("pnItemID");

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

      vFromDate  = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FROM_DT", "From Date" );
      vToDate    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_TO_DT", "To Date" );
      vMinQty    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MINQTY", "Min Qty" );
      vMaxQty    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MAXQTY", "Max Qty" );
      vUnitPrice = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_UNITPRICE", "Unit Price" );

      if(pnItemRateID!=null && !pnItemRateID.equals("") && !pnItemRateID.equalsIgnoreCase("null"))
      { 
        rItmRt = db.getRecord( pnItemRateID, "ItemRate" );
      }  
      else
        rItmRt = new String[8];

      Page page = new Page();
      Head head = new Head();
      Title title = new Title( "Order Entry & Tracking System :- Update/Delete Item Rates " );
      head.add(title);
      Body body = new Body("/ordimg/BACKGR2.GIF",null);
      Form form = new Form("ShowItmRtUpdDelForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "   function IsNull( pvField ) {                                     \n"+
                  "     var nCount = 0                                                 \n"+
                  "     if ( pvField == \"\" )                                         \n"+
                  "       return true                                                  \n"+
                  "     else  {                                                        \n"+
                  "       for( var i=0; i<pvField.length; i++ )                        \n"+
                  "         if ( pvField.charAt(i) == \" \" )                          \n"+
                  "           nCount++                                                 \n"+
                  "       if ( nCount == pvField.length )                              \n"+
                  "         return true                                                \n"+
                  "       else                                                         \n"+
                  "         return false                                               \n"+
                  "     }                                                              \n"+
                  "   }                                                                \n"+

                  "   function check_fields( pavField ) {                              \n"+
                  "      var vErrMsg = \"\"                                            \n"+
                  "      for ( var i=1; i<pavField.length; i=i+2 ) {                   \n"+
                  "        if( IsNull( pavField[i] ) )                                 \n"+
                  "          vErrMsg = vErrMsg + \"     \" + pavField[i-1] + \",\";  \n"+
                  "        else                                                        \n"+
                  "          continue;                                                 \n"+
                  "      }                                                             \n"+
                  "      return vErrMsg                                                \n"+
                  "   }                                                                \n"+

                  "   function check_date( pvDate ) {                                          \n"+
                  "       var vDate                                                            \n"+
                  "       var vMonth                                                           \n"+
                  "       var vYear                                                            \n"+
                  "       var nDate                                                            \n"+ 
                  "       var nMonth                                                           \n"+
                  "       var nYear                                                            \n"+
                  "       var noDaysInFeb                                                      \n"+
                  "       if ( !top.IsNull( pvDate.value ) ) {                                 \n"+
                  "          vDate = pvDate.value.substring(0, pvDate.value.indexOf('.', 1));  \n"+
                  "          vMonth = pvDate.value.substring(pvDate.value.indexOf('.', 2)+1, pvDate.value.indexOf('.', 3)); \n"+
                  "          vYear = pvDate.value.substring(pvDate.value.indexOf('.', 3)+1);   \n"+
                  "          nDate  = vDate; nMonth = vMonth; nYear  = vYear;                  \n"+
                  "          if ( pvDate.value.length > 10 || pvDate.value.length < 8 ) {      \n"+ 
                  "             pvDate.select(); pvDate.focus(); top.show_status( 'Please, Enter the date in \"DD.MM.YYYY\" Format Only' ); \n"+
                  "          }                                                                 \n"+
                  "          else if( nMonth>12 || nMonth<1 ) {                                \n"+
                  "            pvDate.select(); pvDate.focus(); top.show_status( 'Please, Enter the valid Month ( Date Format : \"DD.MM.YYYY\" )' );\n"+
                  "          }                                                                 \n"+
                  "          else if( vYear.length!=4) {                                       \n"+ 
                  "            pvDate.select(); pvDate.focus(); top.show_status( 'Please, Enter the valid Year ( Date Format : \"DD.MM.YYYY\" )' );\n"+
                  "          }                                                                 \n"+
                  "          else {                                                            \n"+
                  "            if ( (nYear%4)==0 && ( (nYear%400)==0 || (nYear%100)!=0 ) )     \n"+
                  "              noDaysInFeb = 29                                              \n"+ 
                  "            else                                                            \n"+ 
                  "              noDaysInFeb = 28                                              \n"+
                  "            if ( (nMonth==4 || nMonth==6 || nMonth==9 || nMonth==11) &&  nDate>30 ) { \n"+
                  "              pvDate.select(); pvDate.focus(); top.show_status( 'Please, Enter the valid Date ( Date Format : \"DD.MM.YYYY\" )' ); \n"+
                  "            }                                                               \n"+  
                  "            else if ( (nMonth==1 || nMonth==3 || nMonth==5 || nMonth==7 || nMonth==8 || nMonth==10 || nMonth==12) &&  nDate>31 ) {  \n"+
                  "               pvDate.select(); pvDate.focus(); top.show_status( 'Please, Enter the valid Date ( Date Format : \"DD.MM.YYYY\" )' ); \n"+
                  "            }                                                               \n"+
                  "            else if ( nMonth==2 && nDate>noDaysInFeb) {                     \n"+
                  "               pvDate.select(); pvDate.focus(); top.show_status( 'Please, Enter the valid Date ( Date Format : \"DD.MM.YYYY\" )' ); \n"+
                  "            }                                                               \n"+
                  "        }                                                                   \n"+
                  "        setTimeout('top.show_status(\"\")', 5000);                          \n"+
                  "     }                                                                      \n"+
                  "   }                                                                      \n\n"+

                  "   function check_num( pvNum ) {                                    \n"+
                  "     var cCharCode                                                  \n"+
                  "     if ( !IsNull( pvNum.value ) ) {                                \n"+
                  "       for( var i=0; i<pvNum.value.length; i++ ) {                  \n"+
                  "         cCharCode = pvNum.value.charCodeAt(i)                      \n"+
                  "         if( cCharCode<48 || cCharCode>57 ) {                       \n"+
                  "           alert( \""+ msg.GetMsgDesc( 24, nLangID)+ "\"+pvNum.value.charAt(i)+\" "+msg.GetMsgDesc( 25, nLangID) +"\" );\n"+
                  "           pvNum.select(); pvNum.focus();return                     \n"+
                  "         }                                                          \n"+
                  "      }                                                             \n"+
                  "    }                                                               \n"+
                  "   }                                                                \n"+

                  "   function submit_form()  {          	      	               \n"+
                  "     with( document.forms[0] ) {                                    \n"+
                  " 	  aField = new Array( \""+ vFromDate +"\", dFrom_Dt.value, \""+ vToDate +"\", dTo_Dt.value,  "+
                                             " \""+ vMinQty  +"\", nMinQty.value,  \""+ vMaxQty +"\", nMaxQty.value, \""+ vUnitPrice +"\", nUnitPrice.value ) \n"+
                  "	  vErrMsg = check_fields( aField )                             \n"+
                  "	  nInActive[0].value = ( nInActive[1].checked ? \"1\" : \"0\" )\n"+
                  "	  if ( vErrMsg == \"\" ) {                         	       \n"+
                  "	    if ( confirm( \"" + msg.GetMsgDesc( 10, nLangID ) +"\" ) ) \n"+
                  "	      submit()                                                 \n"+
                  "	  }                                                            \n"+
                  "	  else {                                                       \n"+
                  "	    vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg + \".\"\n"+
                  "	    alert( vErrMsg ); return                                   \n"+
                  "	  }                                                            \n"+
                  "     }      						               \n"+
                  "   }					              	               \n"+

                  "// End Hidding -->");
      scr.add(scrdata);

      form.add( new NL(1) );
      form.add( new FormHidden("pnTransID", nTransID, null ) );
      form.add( new FormHidden("pnItemID", pnItemID, null ) );
      form.add( new FormHidden("pnItemRateID", pnItemRateID, null ) );
      form.add( new FormHidden("nInActive", null, null ) );

      TableCol blank = new TableCol( "&nbsp;",null, null, null,null);

      Table tab = new Table("1","center","Border=\"0\" width=\"85%\" COLS=4");
 
      TableRow row = new TableRow("Left",null,null);
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"fromdt.gif", vFromDate, vLabelAttrib ),null, null, null,null);
      col.add( WebUtil.NotNull );         
      TableCol col1 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dFrom_Dt", "I", "6", "10", IngDate.dateToStr(rItmRt[2]), "onBlur=\"check_date(this)\"", vTextAttrib),null,null,null,null);
      col1.add( util.createLabelItem( "DD.MM.YYYY", vLabelAttrib ) ); 
      row.add(col);
      row.add(col1);

      TableRow row1 = new TableRow("Left",null,null);
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"todt.gif", vToDate, vLabelAttrib ),null, null, null,null);
      col2.add( WebUtil.NotNull );
      TableCol col3 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dTo_Dt", "I", "6", "10", IngDate.dateToStr(rItmRt[3]), "onBlur=\"check_date(this)\"", vTextAttrib),null,null,null,null);
      col1.add( util.createLabelItem( "DD.MM.YYYY", vLabelAttrib ) ); 
      row1.add(col2);
      row1.add(col3);

      TableRow row2 = new TableRow("Left",null,null);
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"minqty.gif", vMinQty, vLabelAttrib ),null, null, null,null);
      col4.add( WebUtil.NotNull );
      TableCol col5 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMinQty", "I", "6", "100", rItmRt[4], "onBlur=\"check_num(this)\"", vTextAttrib),null,null,null,null);
      row2.add(col4);
      row2.add(col5);

      TableRow row3 = new TableRow("Left",null,null);
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"maxqty.gif", vMaxQty, vLabelAttrib ),null, null, null,null);
      col6.add( WebUtil.NotNull );
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMaxQty", "I", "6", "100", rItmRt[5], "onBlur=\"check_num(this)\"", vTextAttrib),null,null,null,null);
      row3.add(col6);
      row3.add(col7);

      TableRow row4 = new TableRow("Left",null,null);
      TableCol col8 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unit.gif", vUnitPrice, vLabelAttrib ),null, null, null,null);
      col8.add( WebUtil.NotNull );
      TableCol col9 = new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nUnitPrice", "I", "8", "100", rItmRt[6], "onBlur=\"check_num(this)\"", vTextAttrib),null,null,null,null);
      row4.add(col8);
      row4.add(col9);

      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_INACTIVE", "Active" );
      TableCol col10 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"active.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      if( rItmRt[7]!=null && rItmRt[7].equalsIgnoreCase("1") )
         vStatus = "CHECKED";
      else if( rItmRt[7]!=null && rItmRt[7].equalsIgnoreCase("0") )
         vStatus = null;
      TableCol col11 = new TableCol(new FormCheckBox( "nInActive", null, vStatus, null ), null, null, null, null);
      row5.add(col10);
      row5.add(col11);

      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row4);
      tab.add(row5);

      Center cen = new Center();
      FormButton but1 = new FormButton( null, " OK ", "onClick=\"submit_form()\"" );
      FormButton but2 = new FormButton( null, "Cancel", "onClick=\"top.close()\"" );
      form.add( tab );
      form.add( new NL(1) );
      cen.add(but1+"&nbsp;&nbsp;&nbsp;"+but2);
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

      int nMsgID=-1;
      String errMsg=null;
      String vStatus=null;
      String query = null;
      String nLangID=null;
      String nUserID=null;
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String pnTransID = request.getParameter("pnTransID");
      String pnItemID = request.getParameter("pnItemID");
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
      IngDate dt = new IngDate();
      Message msg = new Message();

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
                  "     To_Dt   = '"+ IngDate.strToDate(dToDt)   +"',"+
                  "     MinQty  = "+nMinQty+","+
                  "     MaxQty  = "+nMaxQty+","+
                  "     UnitPrice = "+nUnitPrice+","+
                  "     InActive  = "+nInActive[0]+","+ 
                  "     Modifier  = '"+usr+"',"+
                  "     Change_Dt = '"+dt+"'"+
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
       response.sendRedirect("/JOrder/servlets/ShowItmRtTable?pnItemID="+pnItemID);
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
    }
}

