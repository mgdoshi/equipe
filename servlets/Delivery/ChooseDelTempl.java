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

public class ChooseDelTempl extends HttpServlet
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
      String vOrdTemplQry = null;
      String vClientQry = null;

      String pvMode = request.getParameter("pvMode");
      String pvDefVals = request.getParameter("pvDefVals");

      String temp[] = new String[11];
      temp[] =  Parse.parse( pvDefVals, "~");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "TR_DELIVERY", nLangID, "WD_QUERY", "Delivery / Q" );

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Delivery/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vOrdTemplQry = " SELECT Alias_Name, Alias_Name FROM T_ClsTemplDel "+
                     " WHERE Ref_ID = "+ nUserID;

      if( nUserID.equalsIgnoreCase("0")
        vClientQry = " SELECT cln.Client_ID, cln.Client_Name   "+
                     " FROM   T_Client cln                     "+
                     " Order By cln.Client_Name";
      else
        vClientQry = " SELECT cln.Client_ID, cln.Client_Name   "+
                     " FROM   T_Client cln, T_UserClient ucl   "+
                     " WHERE  ucl.Fk_Client_ID = cln.Client_ID "+
                     " AND    ucl.Fk_User_ID = " + nUserID +
                     " ORDER BY cln.Client_Name";

      Page page = new Page();
      Head head = new Head();
      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      head.add(scr);
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ChooseDelTempl","POST","_parent",null,null);
      form.add(new FormHidden("Browser", "", null) );
      form.add(new NL(5));
      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=3");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_TEMPLATE", "Select Delivery Order Template" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"delvtemp.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"");
      HtmlTag sel =  util.createList( nUserID, "TR_DELIVERY", "vTemplName", "Q", vOrdTemplQry, null, null, vListAttrib);
      TableCol col1 = new TableCol( sel,null, null, "2", null );
      row.add(col);
      row.add(col1);
      tab.add(row);
      vBPLate = cdata.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_SELCLIENT", "Select Client" );
      TableRow row1 = new TableRow("Left",null,null);
      TableCol col2 = new TableCol( util.GetBoilerPlate( vImgOption, vImagePath+"selcnt.gif", vBPLate, vLabelAttrib ), null, null, null, null );
      HtmlTag sel1 =  util.createList( nUserID, "TR_DELIVERY", "nClientID", "Q", vClientQry, temp[0], null, vListAttrib);
      TableCol col3 = new TableCol( sel1,null, null, "2", null );
      row1.add(col2);
      row1.add(col3);
      tab.add(row1);
      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_DELIVERY", nLangID, "BL_LABEL.B_DELIVERY_ORDER_ID", "Order ID" );
      TableCol col4 = new TableCol( util.GetBoilerPlate( vImgOption, vImagePath+"ordnr.gif", vBPLate, vLabelAttrib ), null, null, null, null );
      TableCol col5 = new TableCol( util.createTextItem( nUserID, "TR_DELIVERY", "vOrderID", "Q", "10", "30", temp[1], null, vTextAttrib ), null, null, null, null );
      TableCol col6 = new TableCol( util.createTextItem( nUserID, "TR_DELIVERY", "vOrderID", "Q", "10", "30", temp[2], null, vTextAttrib ), null, null, null, null );
      row2.add( col4 );
      row2.add( col5 );
      row2.add( col6 );
      tab.add(row2);
      for( i = 0; i<4; i++)
      {
        TableRow row3 = new TableRow("Left",null,null);
        TableCol col7 = new TableCol( "&nbsp;", null, null, null, null );
        TableCol col8 = new TableCol( util.createTextItem( nUserID, "TR_DELIVERY", "vOrderID", "Q", "10", "30", temp[2*i+3], null, vTextAttrib ), null, null, null, null );
        TableCol col9 = new TableCol( util.createTextItem( nUserID, "TR_DELIVERY", "vOrderID", "Q", "10", "30", temp[2*i+4], null, vTextAttrib ), null, null, null, null );
        row3.add( col4 );
        row3.add( col5 );
        row3.add( col6 );
        tab.add(row3);
      }
      form.add(tab);
      body.add(form);
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

      String Browser = request.getParameter("Browser");
      String vTemplName = request.getParameter("vTemplName");
      String pnClientID = request.getParameter("pnClientID");
      String vOrderID[] = request.getParameterValues("vOrderID");

      String vOrdID = null;
      String nOrdID = null;
      String cDMOrdStat = null;
      String vDefVals = pnClientID;
      String query = null;
      String vErrMsg = null;
      String vErrMsg1 = null;
      String vErrMsg2 = null;
      DBConnect db = new DBConnect();
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      int nIdx = 1;

      while( nIdx i<=10 )
      {
         vDefVals = vDefVals + vOrderNo[ nIdx -1 ];
         if( vOrderID[nIdx-1]!=null || !vOrderID[nIdx-1].equals("") || !vOrderID[nIdx-1].equalsIgnoreCase("null") )
         {
            vOrdID = vOrdID + vOrderID[nIdx-1] + "~";
            query = " SELECT ord.Order_ID, ord.DM_OrdStat FROM T_Order ord "+
                    " WHERE  ord.Order_ID = "+vOrderID[nIdx-1]+
                    " AND    ord.Fk_Client_ID = "+pnClientID;
            try
            {
              conn = db.GetDBConnection();
              stmt = conn.createStatement();
              rs  =  stmt.executeQuery();
              while(rs.next())
              {
                nOrdID = rs.getString(1);
                cDMOrdStat = rs.getString(2);
              } 
            }catch(SQLException sexe){ vErrMsg1 = vErrMsg1 + vOrderID[nIdx-1] + ",";}

            if( cDMOrdStat.equalsIgnoreCase("D") )
            {
               vErrMsg2 = vErrMsg2 + vOrderID[nIdx-1] + ",";
            }
         }
         nIdx = nIdx + 1;
      } 
      
      if( vErrMsg1!=null || !vErrMsg1.equals("") || !vErrMsg1.equalsIgnoreCase("null") )
        vErrMsg = msg.GetMsgDesc( 40, nLangID) + " " + vErrMsg1 + " "+ msg.GetMsgDesc( 43, nLangID) || ' '|| db.getName( pnClientID );
      else if( vErrMsg2!=null || !vErrMsg2.equals("") || !vErrMsg2.equalsIgnoreCase("null") )
        vErrMsg = msg.GetMsgDesc( 41, nLangID) + " " + vErrMsg2 + " "+ msg.GetMsgDesc( 42, nLangID);

      if( vTemplName==null || vTemplName.equals("") || vTemplName.equalsIgnoreCase("null") )
        response.sendRedirect("/JOrder/servlets/Delivery?Flag=0");
      if( vErrMsg==null || vErrMsg.equals("") || vErrMsg.equalsIgnoreCase("null") )
        response.sendRedirect("/JOrder/servlets/DelFrame?pvMode=I&pnDelvieryID=&vTemplName="+vTemplName+"&Browser="+Browser+"&pnClientID="+pnClientID+"&pvMessage=");
      else
        response.sendRedirect("/JOrder/servlets/DelFrame?pvMode=I&pnDelvieryID=&vTemplName="+vTemplName+"&Browser="+Browser+"&pnClientID="+pnClientID+"&pvMessage=");
   }
}

