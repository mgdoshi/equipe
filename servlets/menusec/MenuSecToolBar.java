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

public class MenuSecToolBar extends HttpServlet
{
 
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        String nLangID=null;
        String nUserID=null;
        String vBPLate = null;
        String vPID = null;

        String pvMode = request.getParameter("pvMode"); 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        WebUtil util = new WebUtil();
        Message msg = new Message();
        ConfigData cdata = new ConfigData();
        CookieUtil PkCookie = new CookieUtil();
        vPID = PkCookie.getCookie(request,"PID");

        if(vPID==null)
        {
          out.println(WebUtil.IllegalEntry());
          return;
        }

        nUserID   = Parse.GetValueFromString( vPID, "UserID" );
        nLangID   = Parse.GetValueFromString( vPID, "LangID" );

        Page page = new Page();
        Head head = new Head();
        Body body = new Body(null, "BGCOLOR=#666666");
        Table tab = new Table("1", "center", "Border=\"0\" width=\"8%\" COLS=1" );

        TableRow blank = new TableRow();
        blank.add( new TableCol( "&nbsp;", null, null, null, null ) );
        tab.add(blank);

        if( pvMode!=null && pvMode.equalsIgnoreCase("I"))
        {
          TableRow row1 = new TableRow();
          vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_MENUSEC.BU_SAVE", "Save Menu Security" );
          Anchor anc = util.createLink( nUserID, "SY_MENUSEC", "Save", "I", "JavaScript:parent.submit_form()", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );
          tab.add( row1 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
        {
          vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_MENUSEC.BU_NEW", "Create New Menu Security" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "SY_MENUSEC", "Query", "I", "JavaScript:parent.submit_form()", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );
          tab.add( row1 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("D"))
        {
          vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_MENUSEC.BU_INSERT", "Create New Menu Security" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "SY_MENUSEC", "Insert", "I", "JavaScript:parent.GetSecLevelPriv()", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );
          tab.add( row1 );
        }

        TableRow row1 = new TableRow();
        vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_MENUSEC.BU_FORMHELP", "Menu Security Form Information" );
        Anchor anc = util.createLink( nUserID, "SY_MENUSEC", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID) );
        row1.add( new TableCol( anc, null, null, null, null ) );
        vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_MENUSEC.BU_HELP", "Menu Security Form Help" );
        TableRow row2 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "SY_MENUSEC", "Help", pvMode, "JavaScript:top.show_systemhelp('401')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID) );
        row2.add( new TableCol( anc1, null, null, null, null ) );
        tab.add( blank );
        tab.add( row1 );
        tab.add( blank );
        tab.add( row2 );
        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }

}
