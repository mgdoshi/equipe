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

public class EmployeeEntry extends HttpServlet
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
      String rEmployee[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vBranchQry=null;
      String dmfunc = "";
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
      String pnEmployeeID = request.getParameter("pnEmployeeID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null; 

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "WD_FORM_INSERT", "Employee / I" );
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "WD_FORM_UPDATE", "Employee / U");

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Emp/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnEmployeeID!=null && !pnEmployeeID.equals("") && !pnEmployeeID.equalsIgnoreCase("null"))
      { 
        rEmployee = db.getRecord(pnEmployeeID,"Employee");
        try
        {
           String query = "SELECT DM_FuncType " +
                          "FROM   T_EmployeeFunc "+
                          "WHERE  Fk_Employee_ID = " + pnEmployeeID;
           conn = db.GetDBConnection();
           stmt = conn.createStatement();
           rs = stmt.executeQuery(query); 
           while(rs.next())
           {
              dmfunc += rs.getString(1)+"~";
           }
        }
        catch(Exception exp)
        {
          System.out.println("Exception:"+exp);
        }
        finally
        {
          try
          {
            if(rs!=null)
              rs.close();
            if(stmt!=null)
             stmt.close();
            if(conn!=null)
             conn.close(); 
          }catch(SQLException sexe){}
        }

      }  
      else
        rEmployee = new String[7];
 
      vBranchQry = "SELECT Branch_ID, Branch_Name FROM T_Branch ";

      Page page = new Page();
      Head head = new Head();
 

      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/EmployeeEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add( new FormHidden("pnEmployeeID", pnEmployeeID, null ));
      NL blines = new NL(5);
      form.add(blines);
      Table tab = new Table("1","center","Border=\"0\" width=\"60%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_EMPLOYEE_NAME", "Employee Name");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Emp.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"30%\"");         
      col.add(WebUtil.NotNull);
      TableCol col1 = null;
      if(rEmployee[0]!=null && rEmployee[0].equals("0"))
      {
        col1 = new TableCol(util.createLabelItem(rEmployee[1], vLabelAttrib),null,null,null,"WIDTH=\"30%\"");
        col1.add(new FormHidden("vEmployee_Name",rEmployee[1],null));
      } 
      else
        col1 = new TableCol(util.createTextItem( nUserID, "ST_EMPLOYEE", "vEmployee_Name", pvMode, "20", "40", rEmployee[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,"WIDTH=\"30%\"");
      row.add(col) ;
      row.add(col1);
      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_DESCRIPTION", "Description");
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null); 
      TableCol col3 = new TableCol(util.createTextItem( nUserID, "ST_EMPLOYEE", "vEmployee_Desc", pvMode, "30", "100",rEmployee[2], null, vTextAttrib),null, null, null,null); 
      row1.add(col2) ;
      row1.add(col3);
      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_BRANCH_NAME", "Branch Name");
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Branch.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col4.add(WebUtil.NotNull); 
      HtmlTag sel =  util.createList( nUserID, "ST_EMPLOYEE", "nFk_Branch_ID", pvMode, vBranchQry, rEmployee[3], null, vListAttrib);
      TableCol col5 = new TableCol( sel, null, null, null,null); 
      row2.add(col4) ;
      row2.add(col5);
      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_DESIGNATION", "Designation");
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null); 
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "ST_EMPLOYEE", "vDesignation", pvMode, "20", "40",rEmployee[4], null, vTextAttrib),null, null, null,null); 
      row3.add(col6) ;
      row3.add(col7);

      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      form.add(tab);
      form.add( new FormHidden("nCount","0", null));
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
      form.add(new FormHidden("vDMEmpFunc", dmfunc, null));
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
      int nMsgID =-1;
      String errMsg=null; 
      String nTransID=null;
      String vStatus=null;
      String mode="N"; 
      String query = null;
      String nLangID=null;
      String nUserID=null;
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String pnTransID = request.getParameter("pnTransID");
      String pnEmployeeID = request.getParameter("pnEmployeeID");
      String vEmployeeName = Parse.formatStr(request.getParameter("vEmployee_Name"));
      String vEmployeeDesc = Parse.formatStr(request.getParameter("vEmployee_Desc"));
      String vDesignation = Parse.formatStr(request.getParameter("vDesignation"));
      String vBranchID = request.getParameter("nFk_Branch_ID");
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

      String vIDArray[]=null;
      String vEmpFunc[]=null;

      int nCount = Integer.parseInt( request.getParameter("nCount") );
      String vAction = request.getParameter("vAction"); 

      Statement stmt = null;
      PreparedStatement pstmt = null;
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
            int emp_id = Integer.parseInt(db.getNextVal("S_Employee"));
            query = "INSERT INTO T_Employee ( Employee_ID, Employee_Name, Employee_Desc, Fk_Branch_ID, "+
                                            " Designation, Modifier, Change_Dt ) " +
                    "VALUES ("+emp_id+", "+val.IsNull(vEmployeeName)+","+val.IsNull(vEmployeeDesc)+","+val.IsNull(vBranchID)+","+val.IsNull(vDesignation)+",'"+usr+"', '"+dt+"')";
            stmt.executeUpdate(query);
            vEmpFunc=request.getParameterValues("vDMEmpFunc");
            vIDArray=Parse.parse(vEmpFunc[0],"~");
            query = "INSERT INTO T_EmployeeFunc( Fk_Employee_ID, DM_FuncType, Modifier, Change_Dt ) "+
                    "  VALUES( ?, ?, ?, '"+dt+"') ";
            pstmt = conn.prepareStatement(query);
            for(int i=0;i<vIDArray.length;i++)
            {  
              String bid = vIDArray[i];
              pstmt.setInt(1,emp_id);
              pstmt.setString(2,bid);
              pstmt.setString(3,usr);
              pstmt.executeUpdate();
            }
            pstmt.close(); 
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
                        "VALUES( "+addrref_id+", 'Employee', "+addr_id+","+emp_id+",'T','"+
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
             if(pstmt!=null)
               pstmt.close();
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

           query = "UPDATE T_Employee SET Employee_Name = "+val.IsNull(vEmployeeName)+"," +
                  " Employee_Desc   = "+val.IsNull(vEmployeeDesc)+"," + 
                  " Designation     = "+val.IsNull(vDesignation)+"," + 
                  " Fk_Branch_ID    = '"+vBranchID+"'," + 
                  " Modifier    = '"+usr+"'," + 
                  " Change_Dt   = '"+dt+"'" + 
                  " WHERE Employee_ID = "+pnEmployeeID;
           stmt.executeUpdate(query);
           query = "Delete From T_EmployeeFunc Where Fk_Employee_ID = ? ";
           pstmt = conn.prepareStatement(query);
           pstmt.setInt(1,Integer.parseInt(pnEmployeeID));
           pstmt.executeUpdate();
           vEmpFunc=request.getParameterValues("vDMEmpFunc");
           vIDArray=Parse.parse(vEmpFunc[0],"~");
           query = "INSERT INTO T_EmployeeFunc( Fk_Employee_ID, DM_FuncType ) "+
                   "  VALUES( ?, ? ) ";
           pstmt = conn.prepareStatement(query);
           pstmt.close();
           for(int i=0;i<vIDArray.length;i++)
           {  
             String bid = vIDArray[i];
             pstmt.setInt(1,Integer.parseInt(pnEmployeeID));
             pstmt.setString(2,bid);
             pstmt.executeUpdate();
           }
           pstmt.close(); 
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
                       "VALUES( "+addrref_id+", 'Employee', "+addr_id+","+Integer.parseInt(pnEmployeeID)+",'T','"+
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
             if(pstmt!=null)
               pstmt.close();
             if(conn!=null)
               conn.close(); 
           }catch(SQLException sexe){}
         }
         Trans.setTransID(pnTransID);
       }
     } 
     if( nMsgID <=5 )
     { 
        if(vAction.equalsIgnoreCase("SaveInsert"))
          response.sendRedirect("/JOrder/servlets/EmployeeFrame?pvMode=I&pnEmployeeID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
        else
          response.sendRedirect("/JOrder/servlets/EmployeeFrame?pvMode=N&pnEmployeeID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     }
     else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

