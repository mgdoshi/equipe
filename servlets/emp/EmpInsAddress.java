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

public class EmpInsAddress extends HttpServlet
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
      String nTransID=null;

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
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Address/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vAddTypeQry = "SELECT Attrib, Attrib_Desc FROM T_Domain WHERE Domain = 'ADDTYPE' AND Fk_Lang_ID = "+nLangID+
                    "Order By Sequence_Nr";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form();
      NL blines = new NL(2);
      form.add(blines);
      Table tab = new Table("1","center","Border=\"0\" width=\"80%\" COLS=4");
      TableRow row = new TableRow("Left",null,null);
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Type.jpg", vAddType, vLabelAttrib ),null, null, null,"WIDTH=\"25%\""); 
      col.add(WebUtil.NotNull);
      HtmlTag sel =  util.createList( nUserID, "ST_ADDRESS", "cDM_AddType", "I", vAddTypeQry, null, null, vListAttrib);
      TableCol col1 = new TableCol( sel, null, null, "2", null);
      row.add(col) ;
      row.add(col1);
      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_ADDRESS", "Address" );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null); 
      TableCol col3 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress1", "I", "20", "100", null, null, vTextAttrib), null, null, "2", null); 
      row1.add(col2) ;
      row1.add(col3);
      TableRow row2 = new TableRow("Left",null,null);
      TableCol col4 = new TableCol(null,null, null, null,null); 
      TableCol col5 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress2", "I", "20", "100", null, null, vTextAttrib), null, null, "2", null); 
      row2.add(col4) ;
      row2.add(col5);
      TableRow row3 = new TableRow("Left",null,null);
      TableCol col6 = new TableCol(null,null, null, null,null); 
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress3", "I", "20", "100", null, null, vTextAttrib), null, null, "2", null);
      row3.add(col6) ;
      row3.add(col7);
      TableRow row9 = new TableRow("Left",null,null);
      TableCol col24 = new TableCol(null,null, null, null,null); 
      TableCol col25 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress4", "I", "20", "100", null, null, vTextAttrib), null, null, "2", null);
      row9.add(col24);
      row9.add(col25);
      TableRow row4 = new TableRow("Left",null,null);
      TableCol col8 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"City.gif", vCity, vLabelAttrib ),null, null, null,null); 
      col8.add(WebUtil.NotNull);
      TableCol col9 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vCity", "I", "10", "100", null, "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null); 
      TableCol col10 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"State.gif", vState, vLabelAttrib ),null, null, null,null);
      col10.add(WebUtil.NotNull);
      TableCol col11 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vState", "I", "10", "100", null, "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null); 
      row4.add(col8);
      row4.add(col9);
      row4.add(col10);
      row4.add(col11);
      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_PIN", "Pin" );
      TableCol col12 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Pin.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col13 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vPin", "I", "10", "10", null, null, vTextAttrib),null, null, null, null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_TELNR", "Tele No" );
      TableCol col14 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TelNr.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col15 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vTelNr", "I", "10", "100", null, null, vTextAttrib),null, null, null, null);
      row5.add(col12);
      row5.add(col13);
      row5.add(col14);
      row5.add(col15);
      TableRow row6 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_FAXNO", "Fax No" );
      TableCol col16 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"FaxNo.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      TableCol col17 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vFaxNr", "I", "10", "100", null, null, vTextAttrib),null, null, null, null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_TELEXNR", "Telex No" );
      TableCol col18 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TelexNr.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      TableCol col19 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vTelexNr", "I", "10", "100", null, null, vTextAttrib),null, null, null, null);
      row6.add(col16);
      row6.add(col17);
      row6.add(col18);
      row6.add(col19);
      TableRow row7 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_MAILID", "Mail ID" );
      TableCol col20 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"MailID.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      TableCol col21 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vMailID", "I", "15", "100", null, null, vTextAttrib),null, null, "2",null);
      row7.add(col20);
      row7.add(col21);
      TableRow row8 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_CONTACT_PERSON", "Contact Person" );
      TableCol col22 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"CPerson.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      TableCol col23 = new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vContactPerson", "I", "20", "100", null, null, vTextAttrib),null, null, "2",null);
      row8.add(col22);
      row8.add(col23);

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
      cen.add( new FormButton( null, " OK ", "onClick=\"submit_form('OK')\"" ) );
      cen.add( new FormButton( null, "More", "onClick=\"submit_form('More')\"" ) );
      cen.add( new FormButton( null, "Cancel", "onClick=\"submit_form('Cancel')\"" ) );
      form.add(cen);
      head.add( ShowEmployeeInsAddrScript(vPID ) );
      body.add(form);
      page.add(body);
      page.add(head);
      out.println(page);
    }

  public Script ShowEmployeeInsAddrScript( String vPID )
  {
    String vAddType = null;
    String vCity = null;
    String vState = null;
    Message msg = new Message();
    ConfigData cdata = new ConfigData();  

    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    vAddType = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_DM_ADDTYPE", "Address Type" );
    vCity    = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_CITY", "City" );
    vState   = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_STATE", "State" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n"); 
    scrdata.add("  var ObjOldWin = oldWindow.mid_frame.right_frame.document.forms[0]  \n" +
                "  var nCount = ObjOldWin.nCount.value                              \n\n" + 
                "  function clear_form() {                                            \n" +
                "     with ( document.forms[0] ) {                                    \n" +
                "       cDM_AddType.selectedIndex = 0                                 \n "+
                "       vAddress1.value = \"\"; vAddress2.value = \"\"                \n "+
                "       vAddress3.value = \"\"                                        \n" +
                "       vAddress4.value = \"\"                                        \n" +
                "       vCity.value = \"\"                                            \n" +
                "       vState.value = \"\"                                           \n" +
                "       vPin.value = \"\"                                             \n" +
                "       vTelNr.value = \"\"                                           \n" +
                "       vFaxNr.value = \"\"                                           \n" +
                "       vTelexNr.value = \"\"                                         \n" +
                "       vMailID.value = \"\"                                          \n" +
                "       vContactPerson.value = \"\"                                   \n" +
                "    }                                                                \n" +
                " }                                                                 \n\n" +
 
                "   function assign_fields( pnTarget ) {                              \n" +
                "     with ( document.forms[0] ) {                                    \n" +
                "       ObjOldWin.cDM_AddType[pnTarget].value = cDM_AddType.options[cDM_AddType.selectedIndex].value \n"+
                "       ObjOldWin.vAddress1[pnTarget].value = vAddress1.value         \n" +
                "       ObjOldWin.vAddress2[pnTarget].value = vAddress2.value         \n" +
                "       ObjOldWin.vAddress3[pnTarget].value = vAddress3.value         \n" +
                "       ObjOldWin.vAddress4[pnTarget].value = vAddress4.value         \n" +
                "       ObjOldWin.vCity[pnTarget].value = vCity.value                 \n" +
                "       ObjOldWin.vState[pnTarget].value = vState.value               \n" +
                "       ObjOldWin.vPin[pnTarget].value = vPin.value                   \n" +
                "       ObjOldWin.vTelNr[pnTarget].value = vTelNr.value               \n" +
                "       ObjOldWin.vFaxNr[pnTarget].value = vFaxNr.value               \n" +
                "       ObjOldWin.vTelexNr[pnTarget].value = vTelexNr.value           \n" +
                "       ObjOldWin.vMailID[pnTarget].value = vMailID.value             \n" +
                "       ObjOldWin.vContactPerson[pnTarget].value = vContactPerson.value \n"+
                "     }                                                               \n" +
                "   }                                                               \n\n" +

                "  function submit_form( pvAction ) {                                 \n" +
                "    with ( document.forms[0] )  {                                    \n" +
                "      var cDMAddType = ( cDM_AddType.type == \"select-one\" ? cDM_AddType.options[cDM_AddType.selectedIndex].value : cDM_AddType.value ) \n"+
                "      aField = new Array( \""+ vAddType +"\",  cDMAddType,           \n"+ 
                                         " \""+ vCity    +"\",  vCity.value,          \n"+
                                         " \""+ vState   +"\",  vState.value )        \n"+
                "      vErrMsg = oldWindow.check_fields( aField )                     \n" +
                "      if ( pvAction == \"OK\" ) {                                    \n" +
                "	  if ( vErrMsg == \"\" ) {                                    \n" +
                "          assign_fields( nCount++ )                                  \n" +
                "          ObjOldWin.nCount.value = nCount                            \n" +
                "          top.close()                                                \n" +
                "          return  }                                                  \n" +
                "	  else {                                                      \n" +
                "	     vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                "	     alert( vErrMsg )                                         \n" +
                "	     return }                                                 \n" +
                "      }                                                              \n" +
                "      if ( pvAction == \"More\" ) {                                  \n" +
                "	 if ( vErrMsg == \"\" ) {                                     \n" +
                "          assign_fields( nCount++ )                                  \n" +
                "          clear_form() }                                             \n" +
                "	 else {                                                       \n" +
                "	   vErrMsg = \" " + msg.GetMsgDesc( 9, nLangID ) +" \" + vErrMsg + \".\" \n"+
                "	   alert( vErrMsg )                                           \n" +
                "	   return  }                                                  \n" +
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

}

