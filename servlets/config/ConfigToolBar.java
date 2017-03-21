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

public class ConfigToolBar extends HttpServlet
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

       if( pvMode!=null && pvMode.equalsIgnoreCase("Q"))
       {
          vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_CONFIG.BU_QUERY", "Query Config Details");
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "SY_CONFIG", "Query", "Q", "JavaScript:parent.submit_form('Query')", "QRYTAB", vBPLate, msg.GetMsgDesc( 126, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );

          vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_CONFIG.BU_NEW", "Create New Config" );
          TableRow row3 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "SY_CONFIG", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          TableCol col3 = new TableCol( anc1, null, null, null, null );
          row3.add( col3 );
          tab.add( row1 );
          tab.add( rw );
          tab.add( row3 );            
        }
        if( pvMode!=null && pvMode.equalsIgnoreCase("I"))
        {
            vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_CONFIG.BU_SAVE", "Save Config" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "SY_CONFIG", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
            TableCol col1 = new TableCol( anc, null, null, null, null );
            row1.add( col1 );
         
            vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_CONFIG.BU_SAVE1", "Save Config And Create New Config" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "SY_CONFIG", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
            TableCol col3 = new TableCol( anc1, null, null, null, null );
            row3.add( col3 );
            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
        }
        else if(pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
        {
          vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_CONFIG.BU_NEW", "Create New Config" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "SY_CONFIG", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
         
          vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_CONFIG.BU_DELETE", "Delete Config" );
          TableRow row3 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "SY_CONFIG", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          TableCol col3 = new TableCol( anc1, null, null, null, null );
          row3.add( col3 );
          tab.add( row1 );
          tab.add( rw );
          tab.add( row3 );
        }
        else if( pvMode.equals("U") )
        {
          vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_CONFIG.BU_UPDATE", "Update Config" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "SY_CONFIG", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          tab.add( row1 );
        }
        
        vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_CONFIG.BU_FORMHELP", "Configuration Form Information" );
        TableRow row2 = new TableRow();
        Anchor anc = util.createLink( nUserID, "SY_CONFIG", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        TableCol col2 = new TableCol( anc, null, null, null, null );
        row2.add( col2 );
 
        vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_CONFIG.BU_HELP", "Configuration Form Help" );
        TableRow row4 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "SY_CONFIG", "Help", pvMode, "JavaScript:top.show_systemhelp('206')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
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
