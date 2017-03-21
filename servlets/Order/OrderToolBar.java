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

public class OrderToolBar extends HttpServlet
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

        if( pvMode.equalsIgnoreCase("I") )
        {
          vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_ORDER.BU_SAVE", "Save Order and Order Details" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "TR_ORDER", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );         
          vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_ORDERDTLS.BU_SHOWITEMS", "Show Shopping Cart List" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "TR_ORDDTLS", "ShowItems", "I", "JavaScript:parent.submit_form('ShpItm')", "SELITM", vBPLate, msg.GetMsgDesc( 112, nLangID ));
          row2.add( new TableCol( anc1, null, null, null, null ) );
          tab.add( row1 );
          tab.add( rw );
          tab.add( row2 );
        }
        else if( pvMode.equalsIgnoreCase("T") )
        {
          vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_ORDER.BU_ORDFORTEMPL", "Show Order Form " );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "TR_ORDER", "OrdFormTempl", "I", "JavaScript:parent.submit_form('Insert')", "TEMPORD", vBPLate, msg.GetMsgDesc( 108, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );         
          tab.add( row1 );
        }
        vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_ORDER.BU_FORMHELP", "Order Form Information" );
        TableRow row2 = new TableRow();
        Anchor anc = util.createLink( nUserID, "TR_ORDER", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        TableCol col2 = new TableCol( anc, null, null, null, null );
        row2.add( col2 );

        vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_ORDER.BU_HELP", "Order Form Help" );
        TableRow row4 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "TR_ORDER", "Help", pvMode, "JavaScript:top.show_systemhelp('211')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
        TableCol col4 = new TableCol( anc1, null, null, null, null );
        row4.add( col4 );

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
