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

public class RecSecToolBar extends HttpServlet
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
       nUserID   = Parse.GetValueFromString( vPID, "UserID" );
       nLangID   = Parse.GetValueFromString( vPID, "LangID" );
       String vBPLate = null;

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
          vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_RECSEC.BU_NEW", "Create New Record Security" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "SY_RECSEC", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_RECSEC.BU_DELETE", "Delete Record Security" );
          TableRow row3 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "SY_RECSEC", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          row3.add( new TableCol( anc1, null, null, null, null ) );
          tab.add( row1 );
          tab.add( rw );
          tab.add( row3 );
        }
        else
        {
          if( pvMode!=null && pvMode.equalsIgnoreCase("U") )
          {
            vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_RECSEC.BU_UPDATE", "Update Record Security" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "SY_RECSEC", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
            row1.add(  new TableCol( anc, null, null, null, null ) );

            vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_RECSEC.BU_ASSIGN", "Assign Record Security Priviledge" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "SY_RECSEC", "Assign", pvMode, "JavaScript:parent.submit_form('Assign')", "RECSEC", vBPLate, msg.GetMsgDesc( 130, nLangID ));
            row3.add( new TableCol( anc1, null, null, null, null ) );
            tab.add( row1 );
            tab.add( rw);
            tab.add( row3 );
          }
          else
          {
            vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_RECSEC.BU_SAVE", "Save Record Security" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "SY_RECSEC", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
            row1.add( new TableCol( anc, null, null, null, null ) );

            vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_RECSEC.BU_SAVE1", "Save Record Security And Create New RecSec" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "SY_RECSEC", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
            row3.add( new TableCol( anc1, null, null, null, null ) );

            vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_GROUP.BU_ASSIGN", "Assign Users to Group" );
            TableRow row5 = new TableRow();
            Anchor anc3 = util.createLink( nUserID, "SY_RECSEC", "Assign", pvMode, "JavaScript:parent.submit_form('Assign')", "RECSEC", vBPLate, msg.GetMsgDesc( 130, nLangID ));
            row5.add(  new TableCol( anc3, null, null, null, null ) );
            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
            tab.add( rw );
            tab.add( row5 );
          }
        }

        vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_RECSEC.BU_FORMHELP", "Record Security Form Information" );
        TableRow row = new TableRow();
        Anchor anc = util.createLink( nUserID, "SY_RECSEC", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        row.add( new TableCol( anc, null, null, null, null ) );

        vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_RECSEC.BU_HELP", "Record Security Form Help" );
        TableRow row2 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "SY_RECSEC", "Help", pvMode, "JavaScript:top.show_systemhelp('304')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
        row2.add( new TableCol( anc1, null, null, null, null ) );
        tab.add(rw);
        tab.add(row);
        tab.add(rw);
        tab.add(row2);
        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }

}
