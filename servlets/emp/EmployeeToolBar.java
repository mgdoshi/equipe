import ingen.html.*;
import ingen.html.util.*;
import ingen.html.frame.*;
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.db.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmployeeToolBar extends HttpServlet
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
       String vBPLate = null;
       String nLangID=null;
       String nUserID=null;
   
       nUserID   = Parse.GetValueFromString( vPID, "UserID" );
       nLangID   = Parse.GetValueFromString( vPID, "LangID" );

       String pvMode = request.getParameter("pvMode");

       WebUtil util = new WebUtil();
       Message msg = new Message();
       ConfigData cdata = new ConfigData();

       Page page = new Page();
       Head head = new Head();
       Body body = new Body(null, "BGCOLOR=#666666");
       Table tab = new Table("1", "center", "Border=\"0\" width=\"8%\" COLS=1" );

       TableRow rw = new TableRow();
       rw.add( new TableCol( "&nbsp;", null, null, null, null ) );
       tab.add(rw);

       if( pvMode!=null && pvMode.equalsIgnoreCase("N") )
       {
          vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_NEW", "Create New Employee" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_EMPLOYEE", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );

          vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_DELETE", "Delete Employee" );
          TableRow row3 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_EMPLOYEE", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          TableCol col3 = new TableCol( anc1, null, null, null, null );
          row3.add( col3 );
          tab.add( row1 );
          tab.add( rw );
          tab.add( row3 );
        }
        else
        {
          if( pvMode!=null && pvMode.equalsIgnoreCase("U") )
          {
            vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_UPDATE", "Update Employee" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "ST_EMPLOYEE", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
            TableCol col1 = new TableCol( anc, null, null, null, null );
            row1.add( col1 );
         
            vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_ASSIGN", "Assign Functions" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "ST_EMPLOYEE", "Assign", "I", "JavaScript:parent.submit_form('Assign')", "EMPFUN", vBPLate, msg.GetMsgDesc( 120, nLangID ));
            TableCol col3 = new TableCol( anc1, null, null, null, null );
            row3.add( col3 );

            vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_INS_ADDRESS", "Create New Address" );
            TableRow row5 = new TableRow();
            Anchor anc2 = util.createLink( nUserID, "ST_EMPLOYEE", "NewAddr", "I", "JavaScript:parent.submit_form('Create')", "NEADDR", vBPLate, msg.GetMsgDesc( 115, nLangID ));
            TableCol col5 = new TableCol( anc2, null, null, null, null );
            row5.add( col5 );

            vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_UPD_ADDRESS", "Update/Delete Address" );
            TableRow row7 = new TableRow();
            Anchor anc3 = util.createLink( nUserID, "ST_EMPLOYEE", "UpdDelAddr", "U", "JavaScript:parent.submit_form('Address')", "MEADDR", vBPLate, msg.GetMsgDesc( 116, nLangID ));
            TableCol col7 = new TableCol( anc3, null, null, null, null );
            row7.add( col7 );

            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
            tab.add( rw );
            tab.add( row5 );
            tab.add( rw );
            tab.add( row7 );
          }
          else
          {
            vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_SAVE", "Save Employee" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "ST_EMPLOYEE", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
            TableCol col1 = new TableCol( anc, null, null, null, null );
            row1.add( col1 );

            vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_SAVE1", "Save Employee And Create New Employee" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "ST_EMPLOYEE", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
            TableCol col3 = new TableCol( anc1, null, null, null, null );
            row3.add( col3 );

            vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_INS_ADDRESS", "Create New Address" );
            TableRow row5 = new TableRow();
            Anchor anc2 = util.createLink( nUserID, "ST_EMPLOYEE", "NewAddr", "I", "JavaScript:parent.submit_form('Create')", "NEADDR", vBPLate, msg.GetMsgDesc( 115, nLangID ));
            TableCol col5 = new TableCol( anc2, null, null, null, null );
            row5.add( col5 );

            vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_ASSIGN", "Assign Functions" );
            TableRow row7 = new TableRow();
            Anchor anc3 = util.createLink( nUserID, "ST_EMPLOYEE", "Assign", "I", "JavaScript:parent.submit_form('Assign')", "EMPFUN", vBPLate, msg.GetMsgDesc( 120, nLangID ));
            TableCol col7 = new TableCol( anc3, null, null, null, null );
            row7.add( col7 );
            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
            tab.add( rw );
            tab.add( row5 );
            tab.add( rw );
            tab.add( row7 );
          }
        }
        vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_FORMHELP", "Employee Form Information" );
        TableRow row2 = new TableRow();
        Anchor anc = util.createLink( nUserID, "ST_EMPLOYEE", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        TableCol col2 = new TableCol( anc, null, null, null, null );
        row2.add( col2 );

        vBPLate = cdata.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_EMPLOYEE.BU_HELP", "Employee Form Help" );
        TableRow row4 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "ST_EMPLOYEE", "Help", pvMode, "JavaScript:top.show_systemhelp('209')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
        TableCol col4 = new TableCol( anc1, null, null, null, null );
        row4.add( col4 );
        tab.add( rw );
        tab.add( row2 );
        tab.add( rw );
        tab.add( row4 );

        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }
}
