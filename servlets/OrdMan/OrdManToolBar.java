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

public class OrdManToolBar extends HttpServlet
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

        if(pvMode!=null && pvMode.equalsIgnoreCase("T"))
        {
          vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_ORDER.BU_DELETE", "Delete Order" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "TR_ORDER", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          row1.add(new TableCol( anc, null, null, null, null ));
          tab.add( row1 );
        }
        else if(pvMode!=null && pvMode.equalsIgnoreCase("Q"))
        {
          vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_ORDER.BU_QUERY", "Order Query Details" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "TR_ORDER", "Query", "Q", "JavaScript:parent.submit_form('Query')", "QRYTAB", vBPLate, msg.GetMsgDesc( 126, nLangID ));
          row1.add(new TableCol( anc, null, null, null, null ));
          tab.add( row1);
        }
        else if(pvMode!=null && pvMode.equalsIgnoreCase("D"))
        {
          vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_ORDER.BU_SAVE", "Save Order Entry" );
          TableRow rw1 = new TableRow();
          Anchor an = util.createLink( nUserID, "TR_ORDER", "Update", "U", "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
          rw1.add( new TableCol( an, null, null, null, null ) );         

          vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_ORDERDTLS.BU_NEW", "Create New Order Details" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "TR_ORDDTLS", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row1.add(new TableCol( anc, null, null, null, null ));

          vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_ORDERDTLS.BU_DELETE", "Delete OrderDtls" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "TR_ORDDTLS", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          row2.add(new TableCol( anc1, null, null, null, null ));

          tab.add( rw1 );
          tab.add(rw);
          tab.add( row1);
          tab.add(rw);
          tab.add( row2);
        }
        else if(pvMode!=null && pvMode.equalsIgnoreCase("U"))
        {
          vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_ORDERDTLS.BU_UPDATE", "Update Order Details" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "TR_ORDDTLS", "Update", "U", "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
          row1.add(new TableCol( anc, null, null, null, null ));
          tab.add( row1);
        }
        else
        {
          vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_ORDERDTLS.BU_SAVE", "Save Order Details" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "TR_ORDDTLS", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
          row1.add(new TableCol( anc, null, null, null, null ));
          tab.add( row1);
        }

        vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_ORDERDTLS.BU_FORMHELP", "Order Maintenance Form Information" );
        TableRow row2 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "TR_ORDDTLS", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        row2.add( new TableCol( anc1, null, null, null, null ) );

        vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_ORDERDTLS.BU_HELP", "Order Maintenance Form Help" );
        TableRow row4 = new TableRow();
        Anchor anc2 = util.createLink( nUserID, "TR_ORDDTLS", "Help", pvMode, "JavaScript:top.show_systemhelp('210')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
        row4.add( new TableCol( anc2, null, null, null, null ) );

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
