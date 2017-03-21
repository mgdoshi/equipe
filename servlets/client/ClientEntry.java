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

public class ClientEntry extends HttpServlet
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

      String rClient[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vClnTypeQry=null;
      String pvMode=null;
      String pnClientID=null;
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

      pvMode = request.getParameter("pvMode");
      pnClientID = request.getParameter("pnClientID");

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "ST_CLIENT", nLangID, "WD_FORM_INSERT","Client / I");                      
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "ST_CLIENT", nLangID, "WD_FORM_UPDATE","Client / U");                      

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Client/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnClientID!=null && !pnClientID.equals("") && !pnClientID.equalsIgnoreCase("null"))
      { 
        rClient = db.getRecord(pnClientID,"client");
      }  
      else
        rClient = new String[8]; 
 
      vClnTypeQry = "SELECT Attrib, Attrib_Desc FROM T_Domain WHERE Domain = 'CLNTYPE' AND Fk_Lang_ID = "+nLangID;

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ClientEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add( new FormHidden("pnClientID", pnClientID, null ));
      form.add(new NL(5));

      Table tab = new Table("1","center","Border=\"0\" width=\"60%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_CLIENT", nLangID, "BL_LABEL.B_CLIENT_CLIENT_CODE","Client Code");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Code.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"");
      col.add(WebUtil.NotNull);
      row.add(col);
      row.add(new TableCol(util.createTextItem( nUserID, "ST_CLIENT", "vClient_Code", pvMode, "5", "2", rClient[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,"WIDTH=\"25%\""));
      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_LABEL.B_CLIENT_CLIENT_TYPE","Client Type");
      HtmlTag sel =  util.createList( nUserID, "ST_CLIENT", "vClient_Type", pvMode, vClnTypeQry, rClient[2], null, vListAttrib);
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Type.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      row1.add( new TableCol( sel, null, null, null,null));
      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_LABEL.B_CLIENT_CLIENT_NAME","Client Name");
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col1.add(WebUtil.NotNull);
      row2.add(col1);
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_CLIENT", "vClient_Name", pvMode, "15", "30",rClient[3], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null));
      TableRow row3 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_LABEL.B_CLIENT_CLIENT_DESC","Description");
      row3.add( new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      row3.add(new TableCol(util.createTextItem( nUserID, "ST_CLIENT", "vClient_Desc", pvMode, "30", "100",rClient[4], null, vTextAttrib),null, null, null,null));

      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      form.add(tab);
      form.add(new FormHidden("nCount","0", null));
      FormHidden addtype = new FormHidden("cDM_AddType", null, null);
      FormHidden addr1 = new FormHidden("vAddress1", null, null);
      FormHidden addr2 = new FormHidden("vAddress2", null, null);
      FormHidden addr3 = new FormHidden("vAddress3", null, null);
      FormHidden addr4 = new FormHidden("vAddress4", null, null);
      FormHidden city  = new FormHidden("vCity", null, null);
      FormHidden state = new FormHidden("vState", null, null);
      FormHidden pin   = new FormHidden("vPin", null, null);
      FormHidden telno  = new FormHidden("vTelNr", null, null);
      FormHidden faxno = new FormHidden("vFaxNr", null, null);
      FormHidden telex   = new FormHidden("vTelexNr", null, null);
      FormHidden mail = new FormHidden("vMailID", null, null);
      FormHidden cperson   = new FormHidden("vContactPerson", null, null);
      for(int i=0;i<10;i++)
      { 
         form.add(addtype);
         form.add(addr1);
         form.add(addr2);
         form.add(addr3);
         form.add(addr4);
         form.add(city);
         form.add(state);
         form.add(pin);
         form.add(telno);
         form.add(faxno);
         form.add(telex);
         form.add(mail);
         form.add(cperson);
      }
      form.add(new FormHidden("vAction","", null));
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
      String usr=null;
      String nLangID=null;
      String nUserID=null;
      
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      
      String pnTransID = request.getParameter("pnTransID");
      String pnClientID = request.getParameter("pnClientID");
      String vClientName = Parse.formatStr(request.getParameter("vClient_Name"));
      String vClientDesc = Parse.formatStr(request.getParameter("vClient_Desc"));
      String vClientType = Parse.formatStr(request.getParameter("vClient_Type"));
      String vClientCode = Parse.formatStr(request.getParameter("vClient_Code"));
      String cDMAddType[] =  request.getParameterValues("cDM_AddType");
      String vAddress1[] =  request.getParameterValues("vAddress1");
      String vAddress2[] =  request.getParameterValues("vAddress2");
      String vAddress3[] =  request.getParameterValues("vAddress3");
      String vAddress4[] =  request.getParameterValues("vAddress4");
      String vCity[] =  request.getParameterValues("vCity");
      String vState[] =  request.getParameterValues("vState");
      String vPin[] =  request.getParameterValues("vPin");
      String vTelNr[] =  request.getParameterValues("vTelNr");
      String vFaxNr[] =  request.getParameterValues("vFaxNr");
      String vTelexNr[] =  request.getParameterValues("vTelexNr");
      String vMailID[] =  request.getParameterValues("vMailID");
      String vCPerson[] =  request.getParameterValues("vContactPerson");
      int nCount = Integer.parseInt( request.getParameter("nCount") );
      String vAction = request.getParameter("vAction"); 

      Statement stmt = null;
      Connection conn = null;
      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      IngDate dt = new IngDate();
  
      usr = db.getName( nUserID, "User" );      
      vStatus = Trans.checkTransValidity( pnTransID );
      if(vStatus==null || vStatus.equalsIgnoreCase("ERROR"))
        ; //Do Nothing     
      else if( vAction.equalsIgnoreCase("Insert") || vAction.equalsIgnoreCase("SaveInsert") )
      {
        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();
          int Client_id = Integer.parseInt(db.getNextVal("S_Client"));
          query = "INSERT INTO T_Client ( Client_ID, Client_Code, DM_ClnType,Client_Name, Client_Desc, Modifier, Change_Dt ) " +
                  "VALUES ("+Client_id+", "+val.IsNull(vClientCode)+","+val.IsNull(vClientType)+","+val.IsNull(vClientName)+","+val.IsNull(vClientDesc)+",'"+usr+"', '"+dt+"')";
          stmt.executeUpdate(query);
          for( int i=0; i<10; i++ )
          {
            cDMAddType[i] = cDMAddType[i].trim();
            if( cDMAddType[i] != null && !cDMAddType[i].equals("") && !cDMAddType[i].equals("null"))
            {
              int addr_id = Integer.parseInt(db.getNextVal("S_Address"));
              query = "INSERT INTO T_Address( Address_ID, DM_AddType, Address1, Address2, Address3, Address4, "+
                                            " City, State, Pin, TelNr, FaxNr, TelexNr, MailID, ContactPerson, "+
                                            " Modifier, Change_Dt)" +
                      "VALUES ("+addr_id+", "+val.IsNull(cDMAddType[i])+","+val.IsNull(Parse.formatStr(vAddress1[i]))+","+val.IsNull(Parse.formatStr(vAddress2[i]))+","+val.IsNull(Parse.formatStr(vAddress3[i]))+","+val.IsNull(Parse.formatStr(vAddress4[i]))+","+
                              val.IsNull(Parse.formatStr(vCity[i]))+","+val.IsNull(Parse.formatStr(vState[i]))+","+val.IsNull(vPin[i])+","+val.IsNull(vTelNr[i])+","+val.IsNull(vFaxNr[i])+","+val.IsNull(vTelexNr[i])+","+
                              val.IsNull(Parse.formatStr(vMailID[i]))+","+val.IsNull(Parse.formatStr(vCPerson[i]))+",'"+usr+"','"+dt+"')";
              stmt.executeUpdate(query);
              int addrref_id = Integer.parseInt(db.getNextVal("S_AddressRef"));
              query = "INSERT INTO T_AddressRef( AddressRef_ID, RefType, Fk_Address_ID, Ref_ID, "+
                                               " Default_Flag, Modifier, Change_Dt) "+
                      "VALUES( "+addrref_id+", 'Client', "+addr_id+","+Client_id+",'T','"+
                               usr+"', '"+dt+"')";
              stmt.executeUpdate(query);
            }             
          }
          nMsgID=3; 
       }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
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
     else if( vAction.equalsIgnoreCase("Update"))
     {
        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();       
          query = "UPDATE T_Client SET Client_Name = "+val.IsNull(vClientName)+"," +
                  " Client_Code   = "+val.IsNull(vClientCode)+"," + 
                  " DM_ClnType    = "+val.IsNull(vClientType)+"," + 
                  " Client_Desc = "+val.IsNull(vClientDesc)+"," + 
                  " Modifier    = '"+usr+"'," + 
                  " Change_Dt   = '"+dt+"'" + 
                  " WHERE Client_ID = "+pnClientID;
          stmt.executeUpdate(query);
          for( int i=0; i<10; i++ )
          {
            cDMAddType[i] = cDMAddType[i].trim();        
            if( cDMAddType[i] != null && !cDMAddType[i].equals("") && !cDMAddType[i].equals("null"))
            {
              int addr_id = Integer.parseInt(db.getNextVal("S_Address"));
              query = "INSERT INTO T_Address( Address_ID, DM_AddType, Address1, Address2, Address3, Address4, "+
                                            " City, State, Pin, TelNr, FaxNr, TelexNr, MailID, ContactPerson, "+
                                            " Modifier, Change_Dt)" +
                      "VALUES ("+addr_id+", "+val.IsNull(cDMAddType[i])+","+val.IsNull(Parse.formatStr(vAddress1[i]))+","+val.IsNull(Parse.formatStr(vAddress2[i]))+","+val.IsNull(Parse.formatStr(vAddress3[i]))+","+val.IsNull(Parse.formatStr(vAddress4[i]))+","+
                              val.IsNull(Parse.formatStr(vCity[i]))+","+val.IsNull(Parse.formatStr(vState[i]))+","+val.IsNull(vPin[i])+","+val.IsNull(vTelNr[i])+","+val.IsNull(vFaxNr[i])+","+val.IsNull(vTelexNr[i])+","+
                              val.IsNull(Parse.formatStr(vMailID[i]))+","+val.IsNull(Parse.formatStr(vCPerson[i]))+",'"+usr+"','"+dt+"')";
              stmt.executeUpdate(query);
              int addrref_id = Integer.parseInt(db.getNextVal("S_AddressRef"));
              query = "INSERT INTO T_AddressRef( AddressRef_ID, RefType, Fk_Address_ID, Ref_ID, "+
                                               " Default_Flag, Modifier, Change_Dt) "+
                      "VALUES( "+addrref_id+", 'Client', "+addr_id+","+Integer.parseInt(pnClientID)+",'T','"+
                               usr+"', '"+dt+"')";
              stmt.executeUpdate(query);
            }             
          }
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
       if(vAction.equalsIgnoreCase("SaveInsert"))
         response.sendRedirect("/JOrder/servlets/ClientFrame?pvMode=I&pnClientID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         response.sendRedirect("/JOrder/servlets/ClientFrame?pvMode=&pnClientID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     }
     else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

