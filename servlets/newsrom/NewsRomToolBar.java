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

public class NewsRomToolBar extends HttpServlet
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

        TableRow blank = new TableRow();
        TableCol cl = new TableCol( "&nbsp;", null, null, null, null );
        blank.add( cl );
        tab.add(blank);

        if( pvMode==null || pvMode.equals("") ||
            pvMode.equalsIgnoreCase("Null")   || pvMode.equalsIgnoreCase("W") )
        {
          vBPLate = cdata.GetConfigValue( "SY_NEWS", nLangID, "BL_NEWS.BU_NEXT", "Next Records" );
          TableRow row3 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "SY_NEWS", "Next1", pvMode, "JavaScript:parent.NextRecords()", "NEXT", vBPLate, msg.GetMsgDesc( 128, nLangID ));
          TableCol col3 = new TableCol( anc1, null, null, null, null );
          row3.add( col3 );

          vBPLate = cdata.GetConfigValue( "SY_NEWS", nLangID, "BL_NEWS.BU_PREV", "Previous Records" );
          TableRow row4 = new TableRow();
          Anchor anc2 = util.createLink( nUserID, "SY_NEWS", "Prev1", pvMode, "JavaScript:parent.PreviousRecords()", "PREV", vBPLate, msg.GetMsgDesc( 129, nLangID ));
          row4.add( new TableCol( anc2, null, null, null, null ));

          tab.add( row3 );
          tab.add( blank);
          tab.add( row4 );
        }
        tab.add(blank);
       
        TableRow row1 = new TableRow();
        vBPLate = cdata.GetConfigValue( "SY_NEWS", nLangID, "BL_NEWS.BU_FORMHELP", "News Form Information" );
        Anchor anc = util.createLink( nUserID, "SY_NEWS", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID) );
        row1.add( new TableCol( anc, null, null, null, null ) );

        vBPLate = cdata.GetConfigValue( "SY_NEWS", nLangID, "BL_NEWS.BU_HELP", "News Form Help" );
        TableRow row3 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "SY_NEWS", "Help", pvMode, "JavaScript:top.show_systemhelp('227')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID) );
        row3.add( new TableCol( anc1, null, null, null, null ) );

        tab.add( row1 );
        tab.add( blank );
        tab.add( row3 );

        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }

}
