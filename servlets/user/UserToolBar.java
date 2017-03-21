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

public class UserToolBar extends HttpServlet
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
        TableCol cl = new TableCol( "&nbsp;", null, null, null, null );
        rw.add( cl );
        tab.add(rw);

        if( pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
        {
          vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_USER.BU_NEW", "Create New User" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "SY_USER", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row1.add(  new TableCol( anc, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_USER.BU_DELETE", "Delete User" );
          TableRow row3 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "SY_USER", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          row3.add( new TableCol( anc1, null, null, null, null ) );
          tab.add( row1 );
          tab.add( rw );
          tab.add( row3 );
        }
        else
        {
          if( pvMode!=null && pvMode.equalsIgnoreCase("U") )
          {
            vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_USER.BU_UPDATE", "Update User" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "SY_USER", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
            row1.add( new TableCol( anc, null, null, null, null ) );

            vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_USER.BU_ASSIGN", "Assign Groups To User" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "SY_USER", "Assign", "I", "JavaScript:parent.submit_form('Assign')", "GRPUSR", vBPLate, msg.GetMsgDesc( 118, nLangID ));
            row3.add(new TableCol( anc1, null, null, null, null ) );
            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
          }
          else
          {
            vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_USER.BU_SAVE", "Save User" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "SY_USER", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
            row1.add(new TableCol( anc, null, null, null, null ));

            vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_USER.BU_SAVE1", "Save User And Create New User" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "SY_USER", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
            row3.add( new TableCol( anc1, null, null, null, null ) );

            vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_USER.BU_ASSIGN", "Assign Groups" );
            TableRow row5 = new TableRow();
            Anchor anc3 = util.createLink( nUserID, "SY_USER", "Assign", "I", "JavaScript:parent.submit_form('Assign')", "GRPUSR", vBPLate, msg.GetMsgDesc( 118, nLangID ));
            row5.add(new TableCol( anc3, null, null, null, null ));
            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
            tab.add( rw );
            tab.add( row5 );
          }
        }
        vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_USER.BU_FORMHELP", "User Form Information" );
        TableRow row2 = new TableRow();
        Anchor anc = util.createLink( nUserID, "SY_USER", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        row2.add(  new TableCol( anc, null, null, null, null ) );

        vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_USER.BU_HELP", "User Form Help" );
        TableRow row4 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "SY_USER", "Help", pvMode, "JavaScript:top.show_systemhelp('301')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
        row4.add( new TableCol( anc1, null, null, null, null ) );
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
