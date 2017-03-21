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

public class ClientToolBar extends HttpServlet
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

        if(pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
        {
          vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_CLIENT.BU_NEW", "Create New Client" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_CLIENT", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row1.add(new TableCol( anc, null, null, null, null ));

          vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_CLIENT.BU_DELETE", "Delete Client" );
          TableRow row3 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_CLIENT", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          row3.add( new TableCol( anc1, null, null, null, null ) );

          tab.add( row1 );
          tab.add( rw );
          tab.add( row3 );
        }
        else
        {
          if( pvMode.equals("U") )
          {
            vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_CLIENT.BU_UPDATE", "Update Client" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "ST_CLIENT", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
            row1.add( new TableCol( anc, null, null, null, null ) );
         
            vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_CLIENT.BU_INS_ADDRESS", "Create New Address" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "ST_CLIENT", "NewAddr", "I", "JavaScript:parent.submit_form('Create')", "NEADDR", vBPLate, msg.GetMsgDesc( 115, nLangID ));
            row3.add(  new TableCol( anc1, null, null, null, null ));

            vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_CLIENT.BU_UPD_ADDRESS", "Update/Delete Address" );
            TableRow row5 = new TableRow();
            Anchor anc2 = util.createLink( nUserID, "ST_CLIENT", "UpdDelAddr", "U", "JavaScript:parent.submit_form('Address')", "MEADDR", vBPLate, msg.GetMsgDesc( 116, nLangID ));
            row5.add( new TableCol( anc2, null, null, null, null ) );

            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
            tab.add( rw );
            tab.add( row5 );
          }
          else
          {
            vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_CLIENT.BU_SAVE", "Save Client" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "ST_CLIENT", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
            TableCol col1 = new TableCol( anc, null, null, null, null );
            row1.add( col1 );

            vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_CLIENT.BU_SAVE1", "Save Client And Create New Client" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "ST_CLIENT", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
            row3.add( new TableCol( anc1, null, null, null, null ) );

            vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_CLIENT.BU_INS_ADDRESS", "Create New Address" );
            TableRow row5 = new TableRow();
            Anchor anc2 = util.createLink( nUserID, "ST_CLIENT", "NewAddr", "I", "JavaScript:parent.submit_form('Create')", "NEADDR", vBPLate, msg.GetMsgDesc( 115, nLangID ));
            row5.add( new TableCol( anc2, null, null, null, null ) );

            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
            tab.add( rw );
            tab.add( row5 );
          }
        }
        vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_CLIENT.BU_FORMHELP", "Client Form Information" );
        TableRow row2 = new TableRow();
        Anchor anc = util.createLink( nUserID, "ST_CLIENT", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        row2.add( new TableCol( anc, null, null, null, null ) );

        vBPLate = cdata.GetConfigValue( "ST_CLIENT", nLangID, "BL_CLIENT.BU_HELP", "Client Form Help" );
        TableRow row4 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "ST_CLIENT", "Help", pvMode, "JavaScript:top.show_systemhelp('210')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
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
