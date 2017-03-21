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

public class BranchToolBar extends HttpServlet
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
        WebUtil.IllegalEntry().printPage(out);
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

      if(pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
      {
          vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_BRANCH.BU_NEW", "Create New Branch" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_BRANCH", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row1.add(  new TableCol( anc, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_BRANCH.BU_DELETE", "Delete Branch" );
          TableRow row3 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_BRANCH", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          row3.add(  new TableCol( anc1, null, null, null, null ));
          tab.add( row1 );
          tab.add( rw );
          tab.add( row3 );
        }
        else
        {
          if( pvMode.equals("U") )
          {
            vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_BRANCH.BU_UPDATE", "Update Branch" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "ST_BRANCH", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
            row1.add( new TableCol( anc, null, null, null, null ) );
         
            vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_BRANCH.BU_INS_ADDRESS", "Create New Address" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "ST_BRANCH", "NewAddr", "I", "JavaScript:parent.submit_form('Create')", "NOADDR", vBPLate, msg.GetMsgDesc( 115, nLangID ));
            row3.add(new TableCol( anc1, null, null, null, null ));
         
            vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_BRANCH.BU_UPD_ADDRESS", "Update/Delete Address" );
            TableRow row5 = new TableRow();
            Anchor anc2 = util.createLink( nUserID, "ST_BRANCH", "UpdDelAddr", "U", "JavaScript:parent.submit_form('Address')", "MOADDR", vBPLate, msg.GetMsgDesc( 116, nLangID ));
            row5.add( new TableCol( anc2, null, null, null, null ) );

            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
            tab.add( rw );
            tab.add( row5 );
          }
          else
          {
            vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_BRANCH.BU_SAVE", "Save Branch" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "ST_BRANCH", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
            row1.add(  new TableCol( anc, null, null, null, null ) );
           
            vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_BRANCH.BU_SAVE1", "Save Branch And Create New Branch" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "ST_BRANCH", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
            row3.add( new TableCol( anc1, null, null, null, null ) );
           
            vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_BRANCH.BU_INS_ADDRESS", "Create New Address" );
            TableRow row5 = new TableRow();
            Anchor anc2 = util.createLink( nUserID, "ST_BRANCH", "NewAddr", "I", "JavaScript:parent.submit_form('Create')", "NOADDR", vBPLate, msg.GetMsgDesc( 115, nLangID ));
            row5.add( new TableCol( anc2, null, null, null, null ) );

            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
            tab.add( rw );
            tab.add( row5 );
          }
        }
        vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_BRANCH.BU_FORMHELP", "Branch Form Information" );
        TableRow row2 = new TableRow();
        Anchor anc = util.createLink( nUserID, "ST_BRANCH", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        TableCol col2 = new TableCol( anc, null, null, null, null );
        row2.add( col2 );

        vBPLate = cdata.GetConfigValue( "ST_BRANCH", nLangID, "BL_BRANCH.BU_HELP", "Branch Form Help" );
        TableRow row4 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "ST_BRANCH", "Help", pvMode, "JavaScript:top.show_systemhelp('211')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
        TableCol col4 = new TableCol( anc1, null, null, null, null );
        row4.add( col4 );

        tab.add( rw );
        tab.add( row2 );
        tab.add( rw );
        tab.add( row4 );

        body.add(tab);
        page.add(head);
        page.add(body);
        page.printPage(out);
    }

}
