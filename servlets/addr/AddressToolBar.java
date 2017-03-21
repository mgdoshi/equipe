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

public class AddressToolBar extends HttpServlet
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

        String nLangID=null;
        String nUserID=null;
        String vBPLate = null;

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

        if( pvMode!=null && pvMode.equalsIgnoreCase("D") )
        {
          vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_ADDRESS.BU_MAINTAIN", "Maintain Address" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_ADDRESS", "Query", "Q", "JavaScript:parent.GetRefType()", "QRYTAB", vBPLate, msg.GetMsgDesc( 126, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          tab.add( row1 );
        }  
        else if( pvMode!=null && pvMode.equalsIgnoreCase("I"))
        {
          vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_ADDRESS.BU_SAVE", "Save Address" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_ADDRESS", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_ADDRESS.BU_SAVE1", "Save Address And Create New Address" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_ADDRESS", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
          TableCol col2 = new TableCol( anc1, null, null, null, null );
          row2.add( col2 );
          tab.add( row1 );
          tab.add( rw );
          tab.add( row2 );
        } 
        else if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
        {
          vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_ADDRESS.BU_NEW", "Create New Address" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_ADDRESS", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_ADDRESS.BU_DELETE", "Delete Address" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_ADDRESS", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          TableCol col2 = new TableCol( anc1, null, null, null, null );
          row2.add( col2 );
          tab.add( row1 );
          tab.add( rw );
          tab.add( row2 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("U"))
        {
          vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_ADDRESS.BU_UPDATE", "Update Address" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_ADDRESS", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          tab.add( row1 );
        }
        vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_ADDRESS.BU_FORMHELP", "Address Form Information" );
        TableRow row1 = new TableRow();
        Anchor anc = util.createLink( nUserID, "ST_ADDRESS", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        TableCol col1 = new TableCol( anc, null, null, null, null );
        row1.add( col1 );
        vBPLate = cdata.GetConfigValue( "ST_ADDRESS", nLangID, "BL_ADDRESS.BU_HELP", "Address Form Help" );
        TableRow row2 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "ST_ADDRESS", "Help", pvMode, "JavaScript:top.show_systemhelp('214')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
        TableCol col2 = new TableCol( anc1, null, null, null, null );
        row2.add( col2 );
        tab.add( rw );
        tab.add( row1 );
        tab.add( rw );
        tab.add( row2 );

        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }

}
