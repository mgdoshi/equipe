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

public class PrefToolBar extends HttpServlet
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

       TableRow blank = new TableRow();
       TableCol blankcol = new TableCol("&nbsp;", null, null, null, null );
       blank.add( blankcol);

       TableRow row1 = new TableRow();
       vBPLate = cdata.GetConfigValue( "SY_SCHEME", nLangID, "BL_SCHEME.BU_SAVE", "Save Scheme" );
       Anchor anc = util.createLink( nUserID, "SY_SCHEME", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
       row1.add(new TableCol( anc, null, null, null, null ));
        
       TableRow row2 = new TableRow();
       vBPLate = cdata.GetConfigValue( "SY_SCHEME", nLangID, "BL_SCHEME.BU_PREVIEW", "Preview Scheme" );
       Anchor anc1 = util.createLink( nUserID, "SY_SCHEME", "Preview", pvMode, "JavaScript:parent.show_preview()", "NEW", vBPLate, msg.GetMsgDesc( 136, nLangID ));
       row2.add( new TableCol( anc1, null, null, null, null ));

       vBPLate = cdata.GetConfigValue( "SY_SCHEME", nLangID, "BL_SCHEME.BU_FORMHELP", "Preferences Form Information" );
       TableRow row3 = new TableRow();
       Anchor anc2 = util.createLink( nUserID, "SY_SCHEME", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID) );
       row3.add( new TableCol( anc2, null, null, null, null ) );

       vBPLate = cdata.GetConfigValue( "SY_SCHEME", nLangID, "BL_SCHEME.BU_HELP", "Preferences Form Help" );
       TableRow row4 = new TableRow();
       Anchor anc3 = util.createLink( nUserID, "SY_SCHEME", "Help", pvMode, "JavaScript:top.show_systemhelp('222')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID) );
       row4.add(  new TableCol( anc3, null, null, null, null ) );

       tab.add( row1 );
       tab.add( blank );
       tab.add( row2 );
       tab.add( blank );
       tab.add( row3 );
       tab.add( blank );
       tab.add( row4 );
       body.add(tab);
       page.add(head);
       page.add(body);
       out.println(page);
   }

}
