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

public class UserRelnToolBar extends HttpServlet
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

        String pvMode = request.getParameter("pvMode"); 

        WebUtil util = new WebUtil();
        Message msg = new Message();
        ConfigData cdata = new ConfigData();

        nUserID = Parse.GetValueFromString( vPID, "UserID" );
        nLangID = Parse.GetValueFromString( vPID, "LangID" );

        Page page = new Page();
        Head head = new Head();
        Body body = new Body(null, "BGCOLOR=#666666");
        Table tab = new Table("1", "center", "Border=\"0\" width=\"8%\" COLS=1" );

        TableRow rw = new TableRow();
        TableCol cl = new TableCol( "&nbsp;", null, null, null, null );
        rw.add( cl );
        tab.add(rw);

        if( pvMode!=null && pvMode.equalsIgnoreCase("D"))
        {
          vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_USERREL.BU_DEFINE", "Define User Relationship" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_USERREL", "Define", "I", "JavaScript:parent.submit_form()", "NEW", vBPLate, msg.GetMsgDesc( 135, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );
          tab.add( row1 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("I"))
        {
          TableRow row1 = new TableRow();
          vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_USERREL.BU_SAVE", "Save User RelationShip" );
          Anchor anc = util.createLink( nUserID, "ST_USERREL", "Save", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );
          tab.add( row1 );
        }
        TableRow row1 = new TableRow();
        vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_USERREL.BU_FORMHELP", "User RelationShip Form Information" );
        Anchor anc = util.createLink( nUserID, "ST_USERREL", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID) );
        row1.add( new TableCol( anc, null, null, null, null ) );
        vBPLate = cdata.GetConfigValue( "ST_USERREL", nLangID, "BL_USERREL.BU_HELP", "User RelationShip Form Help" );
        TableRow row2 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "ST_USERREL", "Help", pvMode, "JavaScript:top.show_systemhelp('216')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID) );
        row2.add( new TableCol( anc1, null, null, null, null ) );
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
