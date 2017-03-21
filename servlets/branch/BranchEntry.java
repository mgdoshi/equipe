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

public class BranchEntry extends HttpServlet
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

      String rBranch[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
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

      String pvMode = request.getParameter("pvMode");
      String pnBranchID = request.getParameter("pnBranchID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();


      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "ST_BRANCH", nLangID, "WD_FORM_INSERT","Branch / I");                      
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "ST_BRANCH", nLangID, "WD_FORM_UPDATE","Branch / U");                      

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Branch/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnBranchID!=null && !pnBranchID.equals("") && !pnBranchID.equalsIgnoreCase("null"))
      { 
        rBranch = db.getRecord(pnBranchID,"Branch");
      }  

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/BranchEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add(new FormHidden("pnBranchID", pnBranchID, null ));
      form.add(new NL(5));

      Table tab = new Table("1","center","Border=\"0\" width=\"60%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_BRANCH", nLangID, "BL_LABEL.B_BRANCH_BRANCH_NAME","Branch Name");
      TableCol col1 = null;
      if(rBranch!=null) 
        col1 = new TableCol(util.createTextItem( nUserID, "ST_BRANCH", "vBranch_Name", pvMode, "15", "30", rBranch[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,"WIDTH=\"25%\"");
      else
        col1 = new TableCol(util.createTextItem( nUserID, "ST_BRANCH", "vBranch_Name", pvMode, "15", "30", null, "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,"WIDTH=\"25%\"");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.jpg", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"");
      col.add(WebUtil.NotNull) ;
      row.add(col);
      row.add(col1);
      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_LABEL.B_BRANCH_BRANCH_DESC","Description");
      TableCol col3 = null;
      if(rBranch!=null) 
         col3 = new TableCol(util.createTextItem( nUserID, "ST_BRANCH", "vBranch_Desc", pvMode, "15", "30",rBranch[2], null, vTextAttrib),null, null, null,"WIDTH=\"25%\""); 
      else
         col3 = new TableCol(util.createTextItem( nUserID, "ST_BRANCH", "vBranch_Desc", pvMode, "15", "30",null, null, vTextAttrib),null, null, null,"WIDTH=\"25%\""); 
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"")) ;
      row1.add(col3);
      tab.add(row);
      tab.add(row1);
      form.add(tab);
      FormHidden ncount = new FormHidden("nCount","0", null); 
      form.add(ncount);
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
      page.printPage(out);
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
    
      String errMsg=null; 
      String vStatus=null;
      String query = null;
      String nLangID=null;
      String nUserID=null;

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String pnTransID = request.getParameter("pnTransID");
      String pnBranchID = request.getParameter("pnBranchID");
      String vBranchName = Parse.formatStr(request.getParameter("vBranch_Name"));
      String vBranchDesc = Parse.formatStr(request.getParameter("vBranch_Desc"));
      String cDMAddType[] =  request.getParameterValues("cDM_AddType");
      String vAddress1[] =  request.getParameterValues("vAddress1");
      String vAddress2[] =  request.getParameterValues("vAddress2");
      String vAddress3[] =  request.getParameterValues("vAddress3");
      String vAddress4[] =  request.getParameterValues("vAddress4");
      String vCity[] =  request.getParameterValues("vCity");
      String vState[] = request.getParameterValues("vState");
      String vPin[] =   request.getParameterValues("vPin");
      String vTelNr[] =  request.getParameterValues("vTelNr");
      String vFaxNr[] =  request.getParameterValues("vFaxNr");
      String vTelexNr[] = request.getParameterValues("vTelexNr");
      String vMailID[] =  request.getParameterValues("vMailID");
      String vCPerson[] = request.getParameterValues("vContactPerson");

      int nCount = Integer.parseInt( request.getParameter("nCount") );
      String vAction = request.getParameter("vAction"); 

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
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
            int branch_id = Integer.parseInt(db.getNextVal("S_Branch"));
            query = "INSERT INTO T_Branch ( Branch_ID, Branch_Name, Branch_Desc, Modifier, Change_Dt ) " +
                    "VALUES ("+branch_id+", "+val.IsNull(vBranchName)+","+val.IsNull(vBranchDesc)+",'"+usr+"', '"+dt+"')";
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
                      "VALUES( "+addrref_id+", 'Branch', "+addr_id+","+branch_id+",'T','"+
                               usr+"', '"+dt+"')";
                 stmt.executeUpdate(query);
              }             
            } 
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
            query = "UPDATE T_Branch SET Branch_Name = "+val.IsNull(vBranchName)+"," +
                    " Branch_Desc = "+val.IsNull(vBranchDesc)+"," + 
                    " Modifier    = '"+usr+"'," + 
                    " Change_Dt   = '"+dt+"'" + 
                    " WHERE Branch_ID = "+pnBranchID;
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
                        "VALUES( "+addrref_id+", 'Branch', "+addr_id+","+Integer.parseInt(pnBranchID)+",'T','"+
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
     }
     if(nMsgID <= 5 )
     {
       if(vAction.equalsIgnoreCase("SaveInsert"))
         response.sendRedirect("/JOrder/servlets/BranchFrame?pvMode=I&pnBranchID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         response.sendRedirect("/JOrder/servlets/BranchFrame?pvMode=&pnBranchID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     }
     else
        WebUtil.ShowException(errMsg, nMsgID, nLangID).printPage(out);
   }
}

