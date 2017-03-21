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

public class DomainToolBar extends HttpServlet
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
      String vBPLate1 = null;
      String nLangID=null;
      String nUserID=null;

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      
      String pvMode = request.getParameter("pvMode");
      String pnHelpID = request.getParameter("pnHelpID");

      WebUtil util = new WebUtil();
      Message msg = new Message();
      ConfigData cdata = new ConfigData();

      Page page = new Page();
      Head head = new Head();
      Body body = new Body(null, "BGCOLOR=#666666");
      Table tab = new Table("1", "center", "Border=\"0\" width=\"8%\" COLS=1" );

      TableRow rw = new TableRow();
      rw.add( new TableCol( "&nbsp;", null, null, null, null ) );
      tab.add( rw );

        if(pvMode!=null && pvMode.equalsIgnoreCase("N"))
        {
          if( nUserID.equals("0"))
          {  
             vBPLate = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "BL_DOMAIN.BU_NEW", "Update/Delete Domain" );
             TableRow row1 = new TableRow();
             Anchor anc = util.createLink( nUserID, "ST_DOMAIN", "Insert", "I", "JavaScript:parent.show_DomainForm()", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
             row1.add( new TableCol( anc, null, null, null, null ) );
             tab.add(row1);
          }
        }
        else if(pvMode!=null && pvMode.equalsIgnoreCase("U"))
        {
            vBPLate = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "BL_DOMAIN.BU_SAVE", "Save Domain Values" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "ST_DOMAIN", "Save", "U", "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
            row1.add(new TableCol( anc, null, null, null, null ));
            tab.add(row1);
        }
        tab.add(rw); 
        vBPLate = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "BL_DOMAIN.BU_FORMHELP", "Domain Form Information" );
        TableRow row2 = new TableRow();
        Anchor anc2 = util.createLink( nUserID, "ST_DOMAIN", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        row2.add( new TableCol( anc2, null, null, null, null ));
        tab.add(row2);
        tab.add(rw);
        vBPLate = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "BL_DOMAIN.BU_HELP", "Domain Form Help" );
        TableRow row4 = new TableRow();
        Anchor anc3 = util.createLink( nUserID, "ST_DOMAIN", "Help", pvMode, "JavaScript:top.show_systemhelp('"+pnHelpID+"')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
        row4.add( new TableCol( anc3, null, null, null, null ) );
        tab.add(row4);
        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }

}