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

public class MsgQueryForm extends HttpServlet
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
      String vFormType=null;
      String vLangQry=null;

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "SY_MSG", nLangID, "WD_QUERY", "Message / Q ");

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Msg/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vLangQry = "Select Lang_ID, Lang_Desc From T_Lang";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/MsgQueryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new NL(3));

      Table tab = new Table("1","center","Border=\"0\" width=\"55%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_MSG", nLangID, "BL_LABEL.B_MESSAGE_MSG_NO_FROM", "Msg No From");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"msgfrom.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"");
      TableCol col1 = new TableCol(util.createTextItem( nUserID, "SY_MSG", "nMsgNo_From", "Q", "10", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,"WIDTH=\"25%\"");
      row.add(col) ;
      row.add(col1);
      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_MSG", nLangID, "BL_LABEL.B_MESSAGE_MSG_NO_TO", "Msg No To");
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"msgto.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col3 = new TableCol(util.createTextItem( nUserID, "SY_MSG", "nMsgNo_To", "Q", "10", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null);
      row1.add(col2) ;
      row1.add(col3);
      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_MSG", nLangID, "BL_LABEL.B_MESSAGE_LANG", "Language" );
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"lang.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel =  util.createList( nUserID, "SY_MSG", "nFk_Lang_ID", "Q", vLangQry, null, null, vListAttrib);
      TableCol col5 = new TableCol( sel, null, null, null,null); 
      row2.add(col4) ;
      row2.add(col5);
      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      form.add(tab);
      form.add(new FormHidden("vAction", null, null));
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

      String vWhereClause=null;
      String mode=""; 

      String nMsgNoFrom = request.getParameter("nMsgNo_From");
      String nMsgNoTo = request.getParameter("nMsgNo_To");
      String vLangID = request.getParameter("nFk_Lang_ID");
      String vAction = request.getParameter("vAction"); 

      if( (nMsgNoTo== null || nMsgNoTo.equals("")|| nMsgNoTo.equalsIgnoreCase("null") ) && (nMsgNoFrom== null || nMsgNoFrom.equals("")|| nMsgNoFrom.equalsIgnoreCase("null") ) )
        vWhereClause = " WHERE 1 = 1 ";
      else if( (nMsgNoTo== null || nMsgNoTo.equals("")|| nMsgNoTo.equalsIgnoreCase("null") ) && !(nMsgNoFrom== null || nMsgNoFrom.equals("")|| nMsgNoFrom.equalsIgnoreCase("null") ))
        vWhereClause = " WHERE Msg_ID >= "+ nMsgNoFrom;
      else if( !(nMsgNoTo== null || nMsgNoTo.equals("")|| nMsgNoTo.equalsIgnoreCase("null") ) && (nMsgNoFrom== null || nMsgNoFrom.equals("")|| nMsgNoFrom.equalsIgnoreCase("null") ))
        vWhereClause = " WHERE Msg_ID <= "+ nMsgNoTo;
      else if( !(nMsgNoTo== null || nMsgNoTo.equals("")|| nMsgNoTo.equalsIgnoreCase("null") ) && !(nMsgNoFrom== null || nMsgNoFrom.equals("")|| nMsgNoFrom.equalsIgnoreCase("null") ))
        vWhereClause = " WHERE ( Msg_ID <= "+ nMsgNoTo + " AND Msg_ID >= " + nMsgNoFrom + " ) ";
           
      if( !( vLangID== null || vLangID.equals("")|| vLangID.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND Fk_Lang_ID = " + vLangID;

     vWhereClause = vWhereClause + " Order By Msg_ID";

     vWhereClause = URLEncoder.encode( vWhereClause );  
     if(vAction.equalsIgnoreCase("Query"))
       mode="N";
     else
       mode="I";
     response.sendRedirect("/JOrder/servlets/MsgFrame?pvMode="+mode+"&pnMsgID=&pvWhereClause="+vWhereClause+"&pvMessage=");
   }
}

