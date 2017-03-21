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

public class ClnItmToolBar extends HttpServlet
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
       TableCol cl = new TableCol( "&nbsp;", null, null, null, null );
       rw.add( cl );
       tab.add(rw);  
       
       if( pvMode!=null && pvMode.equalsIgnoreCase("D"))
       {
          vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_CLIENTITEM.BU_NEW", "Show Client - Items" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_CLIENTITEM", "Show", "I", "JavaScript:parent.submit_form()", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          tab.add( row1 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("S"))
        {
          vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_CLIENTITEM.BU_SAVE", "Save Client - Items" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_CLIENTITEM", "Save", "I", "JavaScript:parent.submit_form()", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );
          tab.add( row1 );
        }
        TableRow row1 = new TableRow();
        vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_CLIENTITEM.BU_FORMHELP", "Client Item Form Information" );
        Anchor anc = util.createLink( nUserID, "ST_CLIENTITEM", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID) );
        TableCol col1 = new TableCol( anc, null, null, null, null );
        row1.add( col1 );
        vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_CLIENTITEM.BU_HELP", "Client Item Form Help" );
        TableRow row3 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "ST_CLIENTITEM", "Help", pvMode, "JavaScript:top.show_systemhelp('324')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID) );
        TableCol col3 = new TableCol( anc1, null, null, null, null );
        row3.add( col3 );

        tab.add( rw );
        tab.add( row1 );  
        tab.add( rw );
        tab.add( row3 );
        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }

}
