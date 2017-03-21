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

public class ClnItmRtToolBar extends HttpServlet
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

        String nLangID = null;
        String nUserID = null;
        String vBPLate = null;
        String temp = null;

        String pvMode = request.getParameter("pvMode"); 

        WebUtil util = new WebUtil();
        Message msg = new Message();
        ConfigData cdata = new ConfigData();

        nUserID   = Parse.GetValueFromString( vPID, "UserID" );
        nLangID   = Parse.GetValueFromString( vPID, "LangID" );

        Page page = new Page();
        Head head = new Head();
        Body body = new Body(null, "BGCOLOR=#666666");
        Table tab = new Table("1", "center", "Border=\"0\" width=\"8%\" COLS=1" );

        TableRow rw = new TableRow();
        TableCol cl = new TableCol( "&nbsp;", null, null, null, null );
        rw.add( cl );
        tab.add(rw);
       
        if( pvMode!=null && pvMode.equalsIgnoreCase("T"))
        {
          vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_CLIENTITEMRATE.BU_NEW", "Create New Client Item Rates" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_CLIENTITEMRATE", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_CLIENTITEMRATE.BU_DELETE", "Delete Client Item Rates" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_CLIENTITEMRATE", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          TableCol col2 = new TableCol( anc1, null, null, null, null );
          row2.add( col2 );
          tab.add( row1 );
          tab.add(rw);
          tab.add( row2 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("Q"))
        {
          vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_CLIENTITEMRATE.BU_QUERY", "Client Item Rates Query Details" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_CLIENTITEMRATE", "Query", "Q", "JavaScript:parent.submit_form('Query')", "QRYTAB", vBPLate, msg.GetMsgDesc( 126, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_CLIENTITEMRATE.BU_NEW", "Create New Client Item Rates" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_CLIENTITEMRATE", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          TableCol col2 = new TableCol( anc1, null, null, null, null );
          row2.add( col2 );
          tab.add( row1 );
          tab.add(rw);
          tab.add( row2 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("U"))
        {
          vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_CLIENTITEMRATE.BU_UPDATE", "Update Client Item Rate" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_CLIENTITEMRATE", "Update", pvMode, "JavaScript:parent.submit_form()", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          tab.add( row1 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
        {
          TableRow row1 = new TableRow();
          vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_CLIENTITEMRATE.BU_SAVE", "Save Client Item Rates" );
          Anchor anc = util.createLink( nUserID, "ST_CLIENTITEMRATE", "Insert", "I", "JavaScript:parent.submit_form()", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          tab.add( row1 );
        }
        TableRow row1 = new TableRow();
        vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_CLIENTITEMRATE.BU_FORMHELP", "Client Item Rate Form Information" );
        Anchor anc = util.createLink( nUserID, "ST_CLIENTITEMRATE", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID) );
        TableCol col1 = new TableCol( anc, null, null, null, null );
        row1.add( col1 );
        vBPLate = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_CLIENTITEMRATE.BU_HELP", "Client Item Rate Form Help" );
        TableRow row2 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "ST_CLIENTITEMRATE", "Help", pvMode, "JavaScript:top.show_systemhelp('326')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID) );
        TableCol col2 = new TableCol( anc1, null, null, null, null );
        row2.add( col2 );
        tab.add(rw);
        tab.add( row1 );
        tab.add(rw);
        tab.add( row2 );
        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }

}
