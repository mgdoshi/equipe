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

public class DefAddrSecurity extends HttpServlet
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
      String vClientQry=null;
      String vBranchQry=null;
      String vEmpQry=null;
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

      String vClientName = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_LABEL.B_CLIENT_CLIENT_NAME", "Client Name" );
      String vBranchName = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_LABEL.B_BRANCH_BRANCH_NAME", "Branch Name" );
      String vEmpName    = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_EMPLOYEE_NAME", "Employee Name" );
      String vFormType   = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "WD_FORM_DEFINE", "Address / D" );

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Address/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vClientQry = "SELECT Client_ID, Client_Name FROM T_Client";
      vBranchQry = "SELECT Branch_ID, Branch_Name FROM T_Branch";
      vEmpQry = "SELECT Employee_ID, Employee_Name FROM T_Employee";

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
      Form form = new Form("DefAddrSecurity","POST","_parent",null,null);
      NL blines = new NL(2);
      form.add(blines);
      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=4");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_CLIENT", "Client" );
      HtmlTag sel =  util.createList( nUserID, "ST_ADDRESS", "pvClientName", "Q", vClientQry, null, null, vListAttrib);
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"client.jpg", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"15%\"")) ;
      row.add(new TableCol( new FormRadio( "vRefType", "Client", "CHECKED", null ), null, null, null, "WIDTH=\"15%\""));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"clnname.jpg", vClientName, vLabelAttrib ),null, null, null,"WIDTH=\"15%\""));
      row.add(new TableCol( sel, null, null, null, null));
      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_BRANCH", "Branch" );
      HtmlTag sel1 =  util.createList( nUserID, "ST_ADDRESS", "pvBranchName", "Q", vBranchQry, null, null, vListAttrib);
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"branch.jpg", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"15%\""));
      row1.add(new TableCol( new FormRadio( "vRefType", "Branch", null, null ), null, null, null, null));
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"brname.jpg", vBranchName, vLabelAttrib ),null, null, null,"WIDTH=\"15%\""));
      row1.add(new TableCol( sel1, null, null, null, null));
      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_EMPLOYEE", "Employee" );
      HtmlTag sel2 =  util.createList( nUserID, "ST_ADDRESS", "pvEmpName", "Q", vEmpQry, null, null, vListAttrib);
      row2.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"emp.jpg", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"15%\""));
      row2.add(new TableCol( new FormRadio( "vRefType", "Employee", null, null ), null, null, null, null));
      row2.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"empname.jpg", vEmpName, vLabelAttrib ),null, null, null,"WIDTH=\"15%\""));
      row2.add(new TableCol( sel2, null, null, null, null));

      tab.add(row);
      tab.add(row1);
      tab.add(row2);

      form.add(tab);
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

      int flag = 0;
      String mode=""; 
      String nRefID = null;
      String nLangID=null;
      String nUserID=null;
     
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String vRefType = request.getParameter("vRefType");
      String pvBranchName = Parse.formatStr(request.getParameter("pvBranchName"));
      String pvClientName = Parse.formatStr(request.getParameter("pvClientName"));
      String pvEmpName = Parse.formatStr(request.getParameter("pvEmpName"));

      if( vRefType != null && vRefType.equalsIgnoreCase("Client") )
        nRefID = pvClientName;
      if( vRefType != null && vRefType.equalsIgnoreCase("Branch") )
        nRefID = pvBranchName;
      if( vRefType != null && vRefType.equalsIgnoreCase("Employee") )
        nRefID = pvEmpName;

      String query = " SELECT adr.Address_ID "+
                     " FROM   T_Address adr  "+
                     " WHERE  adr.Address_ID IN ( SELECT adf.Fk_Address_ID "+
                     "        From   T_AddressRef adf "+
                     "        Where  adf.Ref_ID  = "+nRefID+
                     "        AND    adf.RefType = '"+vRefType+"')";

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();
        rs = stmt.executeQuery(query);
        if( rs.next() )
          flag = 1;   
      }catch(SQLException sexe){System.out.println(sexe);}
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
        }catch(SQLException sexe){System.out.println(sexe);}
      }
      if( flag > 0 )
        response.sendRedirect("/JOrder/servlets/AddressFrame?pvMode=N&pvRefType="+vRefType+"&pnRefID="+nRefID+"&pnAddressID=&pvMessage=");
      else
        response.sendRedirect("/JOrder/servlets/AddressFrame?pvMode=I&pvRefType="+vRefType+"&pnRefID="+nRefID+"&pnAddressID=&pvMessage=");
    }
}

