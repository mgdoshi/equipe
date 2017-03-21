import ingen.html.*;
import ingen.html.character.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ShowEmplFunc extends HttpServlet
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
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vDMFunc = null;
      String vStatus = null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;

      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      Connection conn =null;
      Statement stmt = null;
      ResultSet rs = null;

      if(!vImgOption.equalsIgnoreCase("ON"))
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      Page page = new Page();
      Head head = new Head();
      Title title = new Title( "Order - Delivery System : Employee Functions ");
      Body body = new Body(null,"BGCOLOR=\"#COCOCO\" onLoad=\"load_form()\""); 
      Form form = new Form();

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "  function load_form() 	                                           \n" +
                  "  {				                                           \n" +
                  "    var ObjOldWin = oldWindow.mid_frame.right_frame.document.forms[0]   \n" +
                  "    with ( document.forms[0] )                                          \n" +
                  "    {				 	                           \n" +
                  "      if ( ObjOldWin.vDMEmpFunc.value != \"\" )                         \n" +
                  "        for ( i=0; i<vEmpFunc.length; i++ )                             \n" + 
                  "          if ( ObjOldWin.vDMEmpFunc.value.indexOf( vEmpFunc[i].value ) > -1 )\n" +
                  "            vEmpFunc[i].checked = true                                  \n" +
                  "    }				 	                           \n" +
                  "  }					                                 \n\n" +

    "  function submit_form( pvAction ) 	                                  \n" +
    "  {				 	                                  \n" +
    "    var ObjOldWin = oldWindow.mid_frame.right_frame.document.forms[0]        \n" +
    "    if ( pvAction == \"Cancel\" ) {                                          \n" +
    "      top.close()  			 	                          \n" +
    "      return  			 	                                  \n" +
    "    }				 	                                  \n" +
    "    if ( pvAction == \"OK\" ) {                                              \n" +
    "      ObjOldWin.vDMEmpFunc.value = \"\"                                      \n" +
    "      with ( document.forms[0] )                                             \n" +
    "      {				 	                                  \n" +
    "        for ( i=0; i<vEmpFunc.length; i++ )                                  \n" +
    "          if ( vEmpFunc[i].checked )                                         \n" +
    "            ObjOldWin.vDMEmpFunc.value += vEmpFunc[i].value + \"~\"          \n" +
    "      }				 	                                  \n" +
    "      top.close()  			 	                          \n" +
    "    }					                                  \n" +
    "  }					                                \n\n" +

                 "// End Hidding -->");
      scr.add(scrdata);

      NL blines = new NL(3);
      Font f = new Font("White", "Arial", "3", null);
      form.add(blines);
      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2 BGCOLOR=\"#D7FFFF\"");
      TableRow row = new TableRow("Left",null,"BGCOLOR=\"#666666\"");
      vBPLate = cdata.GetConfigValue("ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_ASSIGN","Assign");
      TableHeader th = new TableHeader(util.createLabelItem(vBPLate,vLabelAttrib),null, null, null,"WIDTH=\"25%\""); 
      th.setFormat(f); 
      vBPLate = cdata.GetConfigValue("ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_FUNC","Functions");
      TableHeader th1 = new TableHeader(util.createLabelItem(vBPLate,vLabelAttrib),null, null, null,"WIDTH=\"25%\"");
      th1.setFormat(f);
      row.add(th) ;
      row.add(th1);
      tab.add(row);
      try
      {
        String query = "Select Attrib, Attrib_Desc From t_Domain Where Domain='FUNCTYPE'";
        conn = db.GetDBConnection();
        stmt = conn.createStatement();
        rs = stmt.executeQuery(query);
        while(rs.next())
        {
          TableRow row1 = new TableRow("Left",null,null);
          FormCheckBox chk = new FormCheckBox( "vEmpFunc",rs.getString(1), null, null);
          TableCol col = new TableCol(chk,"center", null, null,"WIDTH=\"25%\"");
          TableCol col1 = new TableCol(rs.getString(2),null, null, null,"WIDTH=\"25%\"");
          row1.add(col);
          row1.add(col1);
          tab.add(row1);
        }
      }
      catch(Exception e) { 
        System.out.println("Exception Occured	"+e);
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
      Center cen = new Center();
      form.add(tab);
      form.add(new NL(2));
      cen.add(new FormButton( null, " OK ", "onClick=\"submit_form('OK')\"" ));
      cen.add(new FormButton( null, "Cancel", "onClick=\"submit_form('Cancel')\"" ));
      form.add(cen);
      body.add(form);
      head.add(title);
      head.add(scr);
      page.add(head);
      page.add(body);
      out.println(page);
    }
}

