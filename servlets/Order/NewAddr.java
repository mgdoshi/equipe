import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class NewAddr extends HttpServlet
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
      String flag = request.getParameter("nFlag");
      if(vPID==null)
      {
        out.println(WebUtil.IllegalEntry());
        return;
      }
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vAddTypeQry = null;
      String vAddType = null;
      String vCity = null;
      String vState = null;
      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      vAddType = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_DM_ADDTYPE", "Address Type" );
      vCity    = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_CITY", "City" );
      vState   = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_STATE", "State" );

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Branch/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vAddTypeQry = " SELECT Attrib, Attrib_Desc FROM T_Domain WHERE Domain = 'ADDTYPE' AND Fk_Lang_ID = "+nLangID+
                    " Order By Sequence_Nr";

      Page page = new Page();
      Head head = new Head();
      Title title = new Title("Order Entry & Tracking System: Insert New Address");
      head.add(title);
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/NewAddr", "POST", "_parent", null, null);
      form.add(new NL(1));
      Table tab = new Table("1","center","Border=\"0\" width=\"80%\" COLS=4");
      TableRow row = new TableRow("Left",null,null);
      HtmlTag sel =  util.createList( nUserID, "ST_ADDRESS", "cDM_AddType", "I", vAddTypeQry, null, null, vListAttrib);
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.jpg", vAddType, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"");
      col.add(WebUtil.NotNull);
      row.add(col);
      row.add(new TableCol( sel, null, null, "2", null));
      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_ADDRESS", "Address" );
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      row1.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress1", "I", "20", "100", null, null, vTextAttrib), null, null, "2", null));
      TableRow row2 = new TableRow("Left",null,null);
      row2.add(new TableCol(null,null, null, null,null)) ;
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress2", "I", "20", "100", null, null, vTextAttrib), null, null, "2", null));
      TableRow row3 = new TableRow("Left",null,null);
      row3.add(new TableCol(null,null, null, null,null)) ;
      row3.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress3", "I", "20", "100", null, null, vTextAttrib), null, null, "2", null));
      TableRow row9 = new TableRow("Left",null,null);
      row9.add(new TableCol(null,null, null, null,null));
      row9.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress4", "I", "20", "100", null, null, vTextAttrib), null, null, "2", null));
      TableRow row4 = new TableRow("Left",null,null);
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"City.gif", vCity, vLabelAttrib ),null, null, null,null);
      col1.add(WebUtil.NotNull);
      row4.add(col1);
      row4.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vCity", "I", "10", "100", null, "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null));
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"State.gif", vState, vLabelAttrib ),null, null, null,null);
      col2.add(WebUtil.NotNull);
      row4.add(col2);
      row4.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vState", "I", "10", "100", null, "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null));
      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_PIN", "Pin" );
      row5.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Pin.gif", vBPLate, vLabelAttrib ),null, null, null, null));
      row5.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vPin", "I", "10", "10", null, null, vTextAttrib),null, null, null, null));
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_TELNR", "Tele No" );
      row5.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TelNr.gif", vBPLate, vLabelAttrib ),null, null, null, null));
      row5.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vTelNr", "I", "10", "100", null, null, vTextAttrib),null, null, null, null));

      TableRow row6 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_FAXNO", "Fax No" );
      row6.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"FaxNo.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row6.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vFaxNr", "I", "10", "100", null, null, vTextAttrib),null, null, null, null));
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_TELEXNR", "Telex No" );
      row6.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TelexNr.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row6.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vTelexNr", "I", "10", "100", null, null, vTextAttrib),null, null, null, null));
      TableRow row7 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_MAILID", "Mail ID" );
      row7.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"MailID.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row7.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vMailID", "I", "15", "100", null, null, vTextAttrib),null, null, "2",null));
      TableRow row8 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_CONTACT_PERSON", "Contact Person" );
      row8.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"CPerson.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row8.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vContactPerson", "I", "20", "100", null, null, vTextAttrib),null, null, "2",null));

      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row9);
      tab.add(row4);
      tab.add(row5);
      tab.add(row6);
      tab.add(row7);
      tab.add(row8);
      form.add(tab);

      Center cen = new Center();
      form.add(new FormHidden("nFlag",flag,null));
      form.add(new NL(1));
      cen.add( new FormButton( null, " Submit ", "onClick=\"submit_form('Submit')\"" ) );
      cen.add( new FormButton( null, "Cancel", "onClick=\"submit_form('Cancel')\"" ) );
      form.add(cen);
      head.add( ShowInsAddrScript( vPID ) );
      body.add(form);
      page.add(body);
      page.add(head);
      out.println(page);
    }

  public Script ShowInsAddrScript( String vPID )
  {
    String vAddType = null;
    String vCity = null;
    String vState = null;

    ConfigData cdata = new ConfigData();
    Message msg = new Message();
    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );

    vAddType = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_DM_ADDTYPE", "Address Type" );
    vCity    = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_CITY", "City" );
    vState   = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_STATE", "State" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n"); 
    scrdata.add(" var oldWin = this.opener.document.forms[0]                         \n"+
                "  function submit_form( pvAction ) {                                 \n" +
                "    with ( document.forms[0] )  {                                    \n" +
                "      var cDMAddType = ( cDM_AddType.type == \"select-one\" ? cDM_AddType.options[cDM_AddType.selectedIndex].value : cDM_AddType.value ) \n"+
                "      aField = new Array( \""+ vAddType +"\",  cDMAddType,           \n"+ 
                                         " \""+ vCity    +"\",  vCity.value,          \n"+
                                         " \""+ vState   +"\",  vState.value )        \n"+
                "      vErrMsg = this.opener.top.check_fields( aField )               \n" +
                "      if ( pvAction == \"Submit\" ) {                                \n" +
                "	 if ( vErrMsg == \"\" && confirm( \"" + msg.GetMsgDesc( 10, nLangID ) + "\") ) {  \n" +
                "          submit() }                                                 \n" +
                "	  else {                                                      \n" +
                "	     vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                "	     alert( vErrMsg )                                         \n" +
                "	     return }                                                 \n" +
                "      }                                                              \n" +
                "      if ( pvAction == \"Cancel\" ) {                                \n" +
                "        top.close()                                                  \n" +
                "        return  }                                                    \n" +
                "    }                                                                \n" +
                "  }					                              " );

    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
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
      String query = null;
      String nLangID   = Parse.GetValueFromString( vPID, "LangID");
      String nUserID   = Parse.GetValueFromString( vPID, "UserID");
      String nClientID   = Parse.GetValueFromString( vPID, "ClientID");

      String nFlag = request.getParameter("nFlag");
      String pnAddressID = request.getParameter("pnAddressID");
      String cDMAddType =  Parse.formatStr(request.getParameter("cDM_AddType"));
      String vAddress1 =  Parse.formatStr(request.getParameter("vAddress1"));
      String vAddress2 =  Parse.formatStr(request.getParameter("vAddress2"));
      String vAddress3 =  Parse.formatStr(request.getParameter("vAddress3"));
      String vAddress4 =  Parse.formatStr(request.getParameter("vAddress4"));
      String vCity =  Parse.formatStr(request.getParameter("vCity"));
      String vState =  Parse.formatStr(request.getParameter("vState"));
      String vPin =  request.getParameter("vPin");
      String vTelNr=  request.getParameter("vTelNr");
      String vFaxNr =  request.getParameter("vFaxNr");
      String vTelexNr  =  request.getParameter("vTelexNr");
      String vMailID =  Parse.formatStr(request.getParameter("vMailID"));
      String vCPerson =  Parse.formatStr(request.getParameter("vContactPerson"));
      String vAction = request.getParameter("vAction");

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      Valid val = new Valid();
      Message msg = new Message(); 
      IngDate dt = new IngDate();
      int addr_id = -1;

      String usr = db.getName( nUserID, "User" );      
      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();
        addr_id = Integer.parseInt(db.getNextVal("S_Address"));
        query = "INSERT INTO T_Address( Address_ID, DM_AddType, Address1, Address2, Address3, Address4, "+
                                        " City, State, Pin, TelNr, FaxNr, TelexNr, MailID, ContactPerson, "+
                                        " Modifier, Change_Dt)" +
                "VALUES ("+addr_id+", "+val.IsNull(cDMAddType)+","+val.IsNull(vAddress1)+","+val.IsNull(vAddress2)+","+val.IsNull(vAddress3)+","+val.IsNull(vAddress4)+","+
                         val.IsNull(vCity)+","+val.IsNull(vState)+","+val.IsNull(vPin)+","+val.IsNull(vTelNr)+","+val.IsNull(vFaxNr)+","+val.IsNull(vTelexNr)+","+
                         val.IsNull(vMailID)+","+val.IsNull(vCPerson)+",'"+usr+"','"+dt+"')";
        stmt.executeUpdate(query);
        int addrref_id = Integer.parseInt(db.getNextVal("S_AddressRef"));
        query = "INSERT INTO T_AddressRef( AddressRef_ID, RefType, Fk_Address_ID, Ref_ID, "+
                                      " Default_Flag, Modifier, Change_Dt) "+
                "VALUES( "+addrref_id+",'Client', "+addr_id+","+nClientID+",'T','"+
                         usr+"', '"+dt+"')";
        stmt.executeUpdate(query);
        nMsgID = 3;
       }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
       finally
       {
         try
         {
           if(stmt!=null)  
             stmt.close();
           if(conn!=null)  
             conn.close();
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
       }
      if(nMsgID <= 5 )
      {     
        Script scr = new Script( "JavaScript", null );
        HtmlTag scrdata = new HtmlTag();
        scrdata.add("<!-- Start Hidding" + "\n"); 
        if( nFlag.equals("1") )
        {
         scrdata.add(" var oldWin = this.opener.document.forms[0]   \n");
         scrdata.add(" oldWin.billaddr_id.value = "+addr_id+"       \n"+
                     " with( this.opener.document.blayer) {         \n"+
                     "  document.open();                            \n"+
                     "  document.writeln(\"<b>Billing Address :</b>\")\n"+
                     "  document.writeln(\"<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH='75%' >\")\n"+
                     "  document.writeln(\"<tr><td width='30%'>&nbsp;Name</td><td>"+db.getName(nUserID,"User")+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;Address</td><td>"+vAddress1+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;City </td><td>"+vCity+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;State</td><td>"+vState+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;Pin Code</td><td>"+vPin+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;Phone No</td><td>"+vTelNr+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;Fax No</td><td>"+vFaxNr+"</td></tr>\")\n"+
                     "  document.writeln(\"</table>\")              \n"+
                     "  document.close();                           \n"+
                     "  }                                           \n");
        }
        else if( nFlag.equals("2") )
        {
         scrdata.add(" var oldWin = this.opener.document.forms[0]   \n");
         scrdata.add(" oldWin.shipaddr_id.value = "+addr_id+"       \n"+
                     " with( this.opener.document.slayer) {         \n"+
                     "  document.open();                            \n"+
                     "  document.writeln(\"<b>Shipping Address :</b>\")\n"+
                     "  document.writeln(\"<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH='75%' >\")\n"+
                     "  document.writeln(\"<tr><td width='30%'>&nbsp;Name</td><td>"+db.getName(nUserID,"User")+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;Address</td><td>"+vAddress1+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;City </td><td>"+vCity+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;State</td><td>"+vState+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;Pin Code</td><td>"+vPin+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;Phone No</td><td>"+vTelNr+"</td></tr>\")\n"+
                     "  document.writeln(\"<tr><td>&nbsp;Fax No</td><td>"+vFaxNr+"</td></tr>\")\n"+
                     "  document.writeln(\"</table>\")              \n"+
                     "  document.close();                           \n"+
                     "  }                                           \n");
        }
        else if( nFlag.equals("3") )
        {
          scrdata.add(" var oldWin = this.opener.document.forms[0]   \n");
          scrdata.add(" oldWin.billaddr_id.value = "+addr_id+"       \n"+
                      " with( this.opener.document.all(\"blayer\")) {\n"+
                     "   var text = '<b>Billing Address :</b>' +     \n"+
                     "              '<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"75%\" >'+  \n"+
                     "              '<tr><td width=\"30%\">&nbsp;Name</td><td>"+db.getName(nUserID,"User")+"</td></tr>'+ \n"+
                     "              '<tr><td>&nbsp;Address</td><td>"+vAddress1+"</td></tr>'+\n"+
                     "              '<tr><td>&nbsp;City </td><td>"+vCity+"</td></tr>' +      \n"+
                     "              '<tr><td>&nbsp;State</td><td>"+vState+"</td></tr> ' +    \n"+
                     "              '<tr><td>&nbsp;Pin Code</td><td>"+vPin+"</td></tr>' +    \n"+
                     "              '<tr><td>&nbsp;Phone No</td><td>"+vTelNr+"</td></tr>' +  \n"+
                     "              '<tr><td>&nbsp;Fax No</td><td>"+vFaxNr+"</td></tr>' +    \n"+
                     "              '</table>';              \n"+
                     "  innerHTML = text;                    \n"+
                     " }                                     \n");

        }
        else if( nFlag.equals("4") )
        {
          scrdata.add(" var oldWin = this.opener.document.forms[0]   \n");
          scrdata.add(" oldWin.shipaddr_id.value = "+addr_id+"       \n"+
                      " with( this.opener.document.all(\"slayer\")) {\n"+
                     "   var text = '<b>Billing Address :</b>' +     \n"+
                     "              '<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"75%\" >'+  \n"+
                     "              '<tr><td width=\"30%\">&nbsp;Name</td><td>"+db.getName(nUserID,"User")+"</td></tr>'+ \n"+
                     "              '<tr><td>&nbsp;Address</td><td>"+vAddress1+"</td></tr>'+\n"+
                     "              '<tr><td>&nbsp;City </td><td>"+vCity+"</td></tr>' +      \n"+
                     "              '<tr><td>&nbsp;State</td><td>"+vState+"</td></tr> ' +    \n"+
                     "              '<tr><td>&nbsp;Pin Code</td><td>"+vPin+"</td></tr>' +    \n"+
                     "              '<tr><td>&nbsp;Phone No</td><td>"+vTelNr+"</td></tr>' +  \n"+
                     "              '<tr><td>&nbsp;Fax No</td><td>"+vFaxNr+"</td></tr>' +    \n"+
                     "              '</table>';              \n"+
                     "  innerHTML = text;                    \n"+
                     " }                                     \n");

        }
        scrdata.add(" top.close()\n");
        scrdata.add("// End Hidding -->");
        scr.add(scrdata);
        out.println(scr);
      }
      else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }

}

