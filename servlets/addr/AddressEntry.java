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

public class AddressEntry extends HttpServlet
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
      String rAddress[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vAddTypeQry = null;
      String vFormType = null;
      String vName = null;


      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );



      String pvMode = request.getParameter("pvMode");
      String pvRefType = request.getParameter("pvRefType");
      String pnAddressID = request.getParameter("pnAddressID");
      String pnRefID = request.getParameter("pnRefID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "WD_FORM_INSERT", "Address / I" );
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "WD_FORM_UPDATE", "Address / U" );

      nTransID = Trans.getTransID( nAuditID, 'M');

      if( pvRefType!=null && pvRefType.equals("Client") )
         vName = db.getName( pnRefID, "Client");
      if( pvRefType!=null && pvRefType.equals("Branch") )
         vName = db.getName( pnRefID, "Branch");
      if( pvRefType!=null && pvRefType.equals("Employee") )
         vName = db.getName( pnRefID, "Employee");

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Address/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vAddTypeQry = "SELECT Attrib, Attrib_Desc FROM T_Domain WHERE Domain = 'ADDTYPE' AND Fk_Lang_ID = "+nLangID+
                    "Order By Sequence_Nr";

      if(pnAddressID!=null && !pnAddressID.equals("") && !pnAddressID.equalsIgnoreCase("null"))
        rAddress = db.getRecord(pnAddressID,"Address");
      else
        rAddress = new String[17];

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
      Form form = new Form("AddressEntry","POST","_parent",null,null);
      FormHidden transid = new FormHidden("pnTransID", nTransID, null );
      FormHidden addrid = new FormHidden("pnAddressID", pnAddressID, null );
      FormHidden refid = new FormHidden("pnRefID", pnRefID, null );
      FormHidden reftype = new FormHidden("pvRefType", pvRefType, null );
      FormHidden action = new FormHidden("vAction", null, null );
      NL blines = new NL(1);
      form.add(blines);
      Center cen = new Center();
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS", "Address of" );
      HtmlTag tag = new HtmlTag();
      tag.add( vBPLate+"&nbsp;&nbsp;"+pvRefType+"&nbsp;&nbsp;"+vName);
      Table tab = new Table("1","center","Border=\"0\" width=\"80%\" COLS=4");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_DM_ADDTYPE", "Address Type" );
      HtmlTag sel =  util.createList( nUserID, "ST_ADDRESS", "cDM_AddType", "I", vAddTypeQry, rAddress[1], null, vListAttrib);
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Type.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"");
      col.add(WebUtil.NotNull);
      row.add(col);
      row.add(new TableCol( sel, null, null, "2", null));
      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_ADDRESS", "Address" );
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      row1.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vAddress1", "I", "20", "100", rAddress[2], null, vTextAttrib), null, null, "2", null));
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
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_CITY", "City" );
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"City.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col1.add(WebUtil.NotNull);
      row4.add(col1);
      row4.add(new TableCol(util.createTextItem( nUserID, "ST_ADDRESS", "vCity", "I", "10", "100", rAddress[6], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null)); 
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_STATE", "State" );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"State.gif", vBPLate, vLabelAttrib ),null, null, null,null);
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
      cen.add(tag);
      form.add(cen);
      form.add(blines);
      form.add(tab);
      form.add(transid);
      form.add(addrid);
      form.add(refid);
      form.add(reftype);
      form.add(action);
      body.add(form);
      page.add(body);
      page.add(head);
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
      String nLangID   = Parse.GetValueFromString( vPID, "LangID");
      String nUserID   = Parse.GetValueFromString( vPID, "UserID");

      String pnTransID = request.getParameter("pnTransID");
      String pvRefType = request.getParameter("pvRefType");
      String pnRefID = request.getParameter("pnRefID");
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
  

      String usr = db.getName( nUserID, "User" );      
      vStatus = Trans.checkTransValidity( pnTransID );
      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      { 
        if( vAction.equalsIgnoreCase("Insert") || vAction.equalsIgnoreCase("SaveInsert") )
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            int addr_id = Integer.parseInt(db.getNextVal("S_Address"));
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
                    "VALUES( "+addrref_id+",'"+pvRefType+"', "+addr_id+","+pnRefID+",'T','"+
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
         Trans.setTransID(pnTransID);
       }
       else if( vAction.equalsIgnoreCase("Update"))
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
           }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
         }
         Trans.setTransID(pnTransID);
       }
     } 
     if(nMsgID <= 5 )
     {
       if(vAction.equalsIgnoreCase("SaveInsert"))
         response.sendRedirect("/JOrder/servlets/AddressFrame?pvMode=I&pvRefType="+pvRefType+"&pnRefID="+pnRefID+"&pnAddressID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         response.sendRedirect("/JOrder/servlets/AddressFrame?pvMode=N&pvRefType="+pvRefType+"&pnRefID="+pnRefID+"&pnAddressID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     }
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

