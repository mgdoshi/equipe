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

public class CostCenterToolBar extends HttpServlet
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

        int nLangID = 0;
        int nUserID = 0;
        String vBPLate = null;
        String temp = null;

        String pvMode = request.getParameter("pvMode"); 

        WebUtil util = new WebUtil();
        Message msg = new Message();
        ConfigData cdata = new ConfigData();

        if((temp=Parse.GetValueFromString( vPID, "UserID" ))!=null && !temp.equals("null"))     
           nUserID   = Integer.parseInt(temp);
        if((temp=Parse.GetValueFromString( vPID, "LangID" ))!=null && !temp.equals("null"))     
          nLangID    = Integer.parseInt(temp);

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
          vBPLate = cdata.GetConfigValue( "ST_COSTCENTRE", nLangID, "BL_COSTCENTRE.BU_NEW", "Create New CostCentre" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_COSTCENTRE", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );

          TableRow row2 = new TableRow();
          TableCol col2 = new TableCol( "&nbsp;", null, null, null, null );
          row2.add( col2 );

          vBPLate = cdata.GetConfigValue( "ST_COSTCENTRE", nLangID, "BL_COSTCENTRE.BU_DELETE", "Delete CostCentre" );
          TableRow row3 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_COSTCENTRE", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          TableCol col3 = new TableCol( anc1, null, null, null, null );
          row3.add( col3 );

          tab.add( row1 );
          tab.add( row2 );
          tab.add( row3 );
        }
        else if( pvMode.equalsIgnoreCase("U"))
        {
            vBPLate = cdata.GetConfigValue( "ST_COSTCENTRE", nLangID, "BL_COSTCENTRE.BU_UPDATE", "Update CostCentre" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "ST_COSTCENTRE", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
            TableCol col1 = new TableCol( anc, null, null, null, null );
            row1.add( col1 );
            tab.add( row1 );

        }
        else
        {
            TableRow row1 = new TableRow();
            vBPLate = cdata.GetConfigValue( "ST_COSTCENTRE", nLangID, "BL_COSTCENTRE.BU_SAVE", "Save CostCentre" );
            Anchor anc = util.createLink( nUserID, "ST_COSTCENTRE", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
            TableCol col1 = new TableCol( anc, null, null, null, null );
            row1.add( col1 );

            TableRow row2 = new TableRow();
            TableCol col2 = new TableCol( "&nbsp;", null, null, null, null );
            row2.add( col2 );

            vBPLate = cdata.GetConfigValue( "ST_COSTCENTRE", nLangID, "BL_COSTCENTRE.BU_SAVE1", "Save CostCentre And Create New CostCentre" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "ST_COSTCENTRE", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
            TableCol col3 = new TableCol( anc1, null, null, null, null );
            row3.add( col3 );
            tab.add( row1 );
            tab.add( row2 );
            tab.add( row3 );

        }

        TableRow row = new TableRow();
        TableCol col = new TableCol( "&nbsp;", null, null, null, null );
        row.add( col );
        TableRow row1 = new TableRow();
        vBPLate = cdata.GetConfigValue( "ST_COSTCENTRE", nLangID, "BL_COSTCENTRE.BU_FORMHELP", "CostCentre Form Information" );
        Anchor anc = util.createLink( nUserID, "ST_COSTCENTRE", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID) );
        TableCol col1 = new TableCol( anc, null, null, null, null );
        row1.add( col1 );
        TableRow row2 = new TableRow();
        TableCol col2 = new TableCol( "&nbsp;", null, null, null, null );
        row2.add( col2 );
        vBPLate = cdata.GetConfigValue( "ST_COSTCENTRE", nLangID, "BL_COSTCENTRE.BU_HELP", "CostCentre Form Help" );
        TableRow row3 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "ST_COSTCENTRE", "Help", pvMode, "JavaScript:top.show_systemhelp('212')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID) );
        TableCol col3 = new TableCol( anc1, null, null, null, null );
        row3.add( col3 );
        tab.add( row );  
        tab.add( row1 );  
        tab.add( row2 );
        tab.add( row3 );
        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }

}
