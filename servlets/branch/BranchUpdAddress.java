import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class BranchUpdAddress extends HttpServlet
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
        WebUtil.IllegalEntry().printPage(out);
        return;
      }
      String nTransID=null;
      String rAddress[] = null;
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


      String pvAddressID = request.getParameter("pnAddressID");
      String pvRefID = request.getParameter("pnRefID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nTransID = Trans.getTransID( nAuditID, 'M');

      vAddType = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_DM_ADDTYPE", "Address Type" );
      vCity    = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_CITY", "City" );
      vState   = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_STATE", "State" );

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Branch/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vAddTypeQry = "SELECT Attrib, Attrib_Desc FROM T_Domain WHERE Domain = 'ADDTYPE' AND Fk_Lang_ID = "+nLangID+
                    "Order By Sequence_Nr";

      if(pvAddressID!=null && !pvAddressID.equals("") && !pvAddressID.equalsIgnoreCase("null"))
        rAddress = db.getRecord(pvAddressID,"Address");
      else
        rAddress = new String[17];

      Page page = new Page();
      Head head = new Head();
      Title title = new Title("Order - Delivery System : Update Address " );
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("BranchUpdAddress","POST","_parent",null,null);
      NL blines = new NL(1);
      form.add(blines);
      Table tab = new Table("1","center","Border=\"0\" width=\"80%\" COLS=4");
      TableRow row = new TableRow("Left",null,null);
      HtmlTag sel =  util.createList( nUserID, "ST_ADDRESS", "cDM_AddType", "I", vAddTypeQry, rAddress[1], null, vListAttrib);
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.jpg", vAddType, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"");
      col.add(WebUtil.NotNull);
      row.add(col);
      row.add(new TableCol( sel, null, null, "2", null));
      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_ADDRESS", "Address" );
      row1.add( new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      row1.add( new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress1", "I", "20", "100", rAddress[2], null, vTextAttrib), null, null, "2", null));
      TableRow row2 = new TableRow("Left",null,null);
      row2.add(new TableCol(null,null, null, null,null)) ;
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress2", "I", "20", "100", rAddress[3], null, vTextAttrib), null, null, "2", null));
      TableRow row3 = new TableRow("Left",null,null);
      row3.add(new TableCol(null,null, null, null,null)) ;
      row3.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress3", "I", "20", "100", rAddress[4], null, vTextAttrib), null, null, "2", null));
      TableRow row9 = new TableRow("Left",null,null);
      row9.add(new TableCol(null,null, null, null,null));
      row9.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress4", "I", "20", "100", rAddress[5], null, vTextAttrib), null, null, "2", null));
      TableRow row4 = new TableRow("Left",null,null);
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"City.gif", vCity, vLabelAttrib ),null, null, null,null);
      col1.add(WebUtil.NotNull);
      row4.add(col1);
      row4.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vCity", "I", "10", "100", rAddress[6], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null));
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"State.gif", vState, vLabelAttrib ),null, null, null,null);
      col2.add(WebUtil.NotNull);
      row4.add(col2);
      row4.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vState", "I", "10", "100", rAddress[7], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null));
      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_PIN", "Pin" );
      row5.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Pin.gif", vBPLate, vLabelAttrib ),null, null, null, null));
      row5.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vPin", "I", "10", "10", rAddress[8], null, vTextAttrib),null, null, null, null));
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_TELNR", "Tele No" );
      row5.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TelNr.gif", vBPLate, vLabelAttrib ),null, null, null, null));
      row5.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vTelNr", "I", "10", "100", rAddress[9], null, vTextAttrib),null, null, null, null));
      TableRow row6 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_FAXNO", "Fax No" );
      row6.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"FaxNo.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row6.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vFaxNr", "I", "10", "100", rAddress[10], null, vTextAttrib),null, null, null, null));
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_TELEXNR", "Telex No" );
      row6.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TelexNr.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row6.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vTelexNr", "I", "10", "100", rAddress[11], null, vTextAttrib),null, null, null, null));

      TableRow row7 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_MAILID", "Mail ID" );
      row7.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"MailID.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row7.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vMailID", "I", "15", "100", rAddress[12], null, vTextAttrib),null, null, "2",null));

      TableRow row8 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_CONTACT_PERSON", "Contact Person" );
      row8.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"CPerson.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row8.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vContactPerson", "I", "20", "100", rAddress[13], null, vTextAttrib),null, null, "2",null));

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
      cen.add( new FormHidden("pnTransID", nTransID, null ));
      cen.add( new FormHidden("pnAddressID", pvAddressID, null ));
      cen.add( new FormHidden("pnBranchID", pvRefID, null ));
      cen.add( new FormButton( null, " OK ", "onClick=\"submit_form('OK')\"" ) );
      cen.add( new FormButton( null, "Cancel", "onClick=\"submit_form('Cancel')\"" ) );
      form.add(cen);
      head.add( ShowBranchUpdAddrScript( vPID ) );
      head.add( title );
      body.add(form);
      page.add(body);
      page.add(head);
      page.printPage(out);
    }

  public Script ShowBranchUpdAddrScript( String vPID )
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
    scrdata.add(   "   function IsNull( pvField ) {                                   \n" +
                   "     var nCount = 0                                               \n" +
                   "     if ( pvField == \"\" )                                       \n" +
                   "       return true                                                \n" +
                   "     else  {                                                      \n" +
                   "       for( var i=0; i<pvField.length; i++ )                      \n" +
                   "         if ( pvField.charAt(i) == \" \" )                        \n" +
                   "           nCount++                                               \n" +
                   "       if ( nCount == pvField.length )                            \n" +
                   "         return true                                              \n" +
                   "       else                                                       \n" +
                   "         return false                                             \n" +
                   "     }                                                            \n" + 
                   "   }                                                            \n\n" +

                   "   function check_fields( pavField ) {                            \n" +
                   "      var vErrMsg = \"\"                                          \n" +
                   "      for ( var i=1; i<pavField.length; i=i+2 ) {                 \n" +
                   "        if( IsNull( pavField[i] ) )                               \n" +
                   "          vErrMsg = vErrMsg + \"     \" + pavField[i-1] + \",\"   \n" +
                   "        else                                                      \n" +
                   "          continue;                                               \n" +
                   "      }                                                           \n" +
                   "      return vErrMsg                                              \n" +
                   "   }                                                            \n\n" +

                " function submit_form( pvAction ) {                                  \n" +
                "   if ( pvAction == \"Cancel\" ) {                                   \n" +
                "      top.close()                                                    \n" +
                "      return  }                                                      \n" +
                "   if ( pvAction == \"OK\" ) {                                       \n" +
                "     with ( document.forms[0] )  {                                   \n" +
                "       var cDMAddType = ( cDM_AddType.type == \"select-one\" ? cDM_AddType.options[cDM_AddType.selectedIndex].value : cDM_AddType.value ) \n"+
                "      aField = new Array( \""+ vAddType +"\",  cDMAddType,           \n" + 
                                         " \""+ vCity    +"\",  vCity.value,          \n" +
                                         " \""+ vState   +"\",  vState.value )        \n" +
                "      vErrMsg = check_fields( aField )                               \n" +
                "      if ( vErrMsg == \"\" ) {                                       \n" +
                "        if ( confirm( \" "+ msg.GetMsgDesc( 10, nLangID )+" \" ) ) { \n" +
                "          submit() } }                                               \n" + 
                "	  else {                                                      \n" +
                "	     vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                "	     alert( vErrMsg )                                         \n" +
                "	     return }                                                 \n" +
                "      }                                                              \n" +
                "    }                                                                \n" +
                "  }					                              \n" );

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
        WebUtil.IllegalEntry().printPage(out);
        return;
      }

      int nMsgID=-1;
      String nTransID=null;
      String vStatus=null;
      String query = null;  
      String nLangID=null;
      String nUserID=null;
      String errMsg=null;
  
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
  
      String pnTransID = request.getParameter("pnTransID");
      String pnBranchID = request.getParameter("pnBranchID");
      String pnAddressID = request.getParameter("pnAddressID");
      String cDMAddType =  request.getParameter("cDM_AddType");
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

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      Valid val = new Valid();
      Message msg = new Message(); 
      IngDate dt = new IngDate();
  
      String usr = db.getName( nUserID, "User" );      
      vStatus = Trans.checkTransValidity( pnTransID );
      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      { 
        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();
          query= " UPDATE T_Address " +
                 " SET DM_ADDTYPE = "+val.IsNull(cDMAddType)+"," +
                 " Address1 = "+val.IsNull(vAddress1)+", Address2 = "+val.IsNull(vAddress2)+"," +
                 " Address3 = "+val.IsNull(vAddress3)+", Address4 = "+val.IsNull(vAddress4)+"," +
                 " City = "+val.IsNull(vCity)+", State = "+val.IsNull(vState)+","               +
                 " Pin = "+val.IsNull(vPin)+", TelNr = "+val.IsNull(vTelNr)+","                 +
                 " FaxNr = "+val.IsNull(vFaxNr)+", TelexNr = "+val.IsNull(vTelexNr)+","         + 
                 " MailID = "+val.IsNull(vMailID)+", ContactPerson = "+val.IsNull(vCPerson)+"," +
                 " Modifier = '"+usr+"', Change_Dt = '"+dt+"'"          +
                 " WHERE Address_ID = "+Integer.parseInt(pnAddressID);  
          stmt.executeUpdate(query);
          nMsgID=5;
         }catch(Exception sexe){System.out.println(sexe);errMsg=sexe.getMessage();nMsgID=8;}
         finally
         {
           try
           {
             if(stmt!=null)
              stmt.close();
             if(conn!=null)
              conn.close(); 
           }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=7;}
         }
         Trans.setTransID(pnTransID);
      } 
       if(nMsgID <= 5 )
       { 
         response.sendRedirect("/JOrder/servlets/BranchAddrTable?pnBranchID="+pnBranchID+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       }
       else
         WebUtil.ShowException(errMsg, nMsgID, nLangID).printPage(out);
    }
}

