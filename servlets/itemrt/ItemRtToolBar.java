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

public class ItemRtToolBar extends HttpServlet
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

        TableRow blank = new TableRow();
        blank.add(new TableCol( "&nbsp;", null, null, null, null ));
        tab.add(blank);
       
        if(pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
        {
          vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_ITEMRATE.BU_NEW", "Create New ItemRate" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_ITEMRATE", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );
          vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_ITEMRATE.BU_DELETE", "Delete ItemRate" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_ITEMRATE", "Delete", "U", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          row2.add( new TableCol( anc1, null, null, null, null ) );
          tab.add( row1 );
          tab.add( blank );
          tab.add( row2 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("Q"))
        {
          vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_ITEMRATE.BU_QUERY", "ItemRate Query Details" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_ITEMRATE", "Query", "Q", "JavaScript:parent.submit_form('Query')", "QRYTAB", vBPLate, msg.GetMsgDesc( 126, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ));
          vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_ITEMRATE.BU_NEW", "Create New ItemRate" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_ITEMRATE", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row2.add( new TableCol( anc1, null, null, null, null ));
          tab.add( row1 );
          tab.add( blank );
          tab.add( row2 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("U"))
        {
          vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_ITEMRATE.BU_UPDATE", "Update ItemRate" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_ITEMRATE", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ));
          tab.add( row1 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("I"))
        {
          TableRow row1 = new TableRow();
          vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_ITEMRATE.BU_SAVE", "Save ItemRate" );
          Anchor anc = util.createLink( nUserID, "ST_ITEMRATE", "Insert", "I", "JavaScript:parent.submit_form()", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );
          tab.add( row1 );
        }
        TableRow row1 = new TableRow();
        vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_ITEMRATE.BU_FORMHELP", "Item Form Information" );
        Anchor anc = util.createLink( nUserID, "ST_ITEMRATE", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID) );
        row1.add( new TableCol( anc, null, null, null, null ) );
        vBPLate = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_ITEMRATE.BU_HELP", "Item Form Help" );
        TableRow row2 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "ST_ITEMRATE", "Help", pvMode, "JavaScript:top.show_systemhelp('325')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID) );
        row2.add( new TableCol( anc1, null, null, null, null ) );
        tab.add(blank);
        tab.add( row1 );  
        tab.add(blank);
        tab.add( row2 );
        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }

}
