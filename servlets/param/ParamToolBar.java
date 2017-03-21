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

public class ParamToolBar extends HttpServlet
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
        String pnParamID = request.getParameter("pnParamID");

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
        blank.add( new TableCol( "&nbsp;", null, null, null, null ) );
        tab.add(blank);

        if( pvMode!=null && pvMode.equalsIgnoreCase("N") )
        {
          vBPLate = cdata.GetConfigValue( "SY_PARAM", nLangID, "BL_PARAM.BU_NEW", "Create New Parameter" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "SY_PARAM", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "SY_PARAM", nLangID, "BL_PARAM.BU_DELETE", "Delete Parameter" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "SY_PARAM", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          row2.add( new TableCol( anc1, null, null, null, null ) );

          tab.add( row1 );
          tab.add( blank );
          tab.add( row2 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("I")) 
        {
          TableRow row1 = new TableRow();
          vBPLate = cdata.GetConfigValue( "SY_PARAM", nLangID, "BL_PARAM.BU_SAVE", "Save Parameter" );
          Anchor anc = util.createLink( nUserID, "SY_PARAM", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "SY_PARAM", nLangID, "BL_PARAM.BU_SAVE1", "Save Parameter And Create New Parameter" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "SY_PARAM", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
          row2.add( new TableCol( anc1, null, null, null, null ) );

          tab.add( row1 );
          tab.add( blank );
          tab.add( row2 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("U") && ( Integer.parseInt(nUserID) == 0 || !Param.getParamClass( Integer.parseInt(pnParamID) ).equalsIgnoreCase("A")))
        {
          vBPLate = cdata.GetConfigValue( "SY_PARAM", nLangID, "BL_PARAM.BU_UPDATE", "Update Parameter" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "SY_PARAM", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );
          tab.add( row1 );
        }

        TableRow row1 = new TableRow();
        vBPLate = cdata.GetConfigValue( "SY_PARAM", nLangID, "BL_PARAM.BU_FORMHELP", "Parameter Form Information" );
        Anchor anc = util.createLink( nUserID, "SY_PARAM", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID) );
        row1.add( new TableCol( anc, null, null, null, null ) );
        vBPLate = cdata.GetConfigValue( "SY_PARAM", nLangID, "BL_PARAM.BU_HELP", "Parameter Form Help" );
        TableRow row2 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "SY_PARAM", "Help", pvMode, "JavaScript:top.show_systemhelp('204')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID) );
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
