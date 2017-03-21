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

public class ClnRepToolBar extends HttpServlet
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
                
        WebUtil util = new WebUtil();
        Message msg = new Message();
        ConfigData cdata = new ConfigData();

        String pvMode=request.getParameter("pvMode");

        Page page = new Page();
        Head head = new Head();
        Body body = new Body(null, "BGCOLOR=#666666");
        Table tab = new Table("1", "center", "Border=\"0\" width=\"8%\" COLS=1" );

        TableRow rw = new TableRow();
        rw.add( new TableCol( "&nbsp;", null, null, null, null ) );
        tab.add(rw);

        if(pvMode!=null || pvMode.equalsIgnoreCase("D"))
        {
          vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_REPORT.BU_LIST", "Client Wise Report" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "TR_REPORT", "Query", "Q", "JavaScript:parent.submit_form('Table')", "QRYLST", vBPLate, msg.GetMsgDesc( 126, nLangID ));
          row1.add(new TableCol( anc, null, null, null, null ));

          tab.add( row1 );
        }
        vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_REPORT.BU_FORMHELP", "Client Wise Report Form Information" );
        TableRow row2 = new TableRow();
        Anchor anc = util.createLink( nUserID, "TR_REPORT", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        row2.add( new TableCol( anc, null, null, null, null ) );

        vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_REPORT.BU_HELP", "Client Wise Report Form Help" );
        TableRow row4 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "TR_REPORT", "Help", pvMode, "JavaScript:top.show_systemhelp('210')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
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
