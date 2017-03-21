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

public class GroupToolBar extends HttpServlet
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
      TableCol cl = new TableCol( "&nbsp;", null, null, null, null );
      rw.add( cl );
      tab.add(rw);

      if( pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
      {
          vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_GROUP.BU_NEW", "Create New Group" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "SY_GROUP", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          TableCol col1 = new TableCol( anc, null, null, null, null );
          row1.add( col1 );

          vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_GROUP.BU_DELETE", "Delete Group" );
          TableRow row3 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "SY_GROUP", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          TableCol col3 = new TableCol( anc1, null, null, null, null );
          row3.add( col3 );
          tab.add( row1 );
          tab.add( rw );
          tab.add( row3 );
        }
        else
        {
          if( pvMode!=null && pvMode.equalsIgnoreCase("U") )
          {
            vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_GROUP.BU_UPDATE", "Update Group" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "SY_GROUP", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
            TableCol col1 = new TableCol( anc, null, null, null, null );
            row1.add( col1 );
         
            vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_GROUP.BU_ASSIGN", "Assign Users To Group" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "SY_GROUP", "Assign", "I", "JavaScript:parent.submit_form('Assign')", "USRGRP", vBPLate, msg.GetMsgDesc( 117, nLangID ));
            TableCol col3 = new TableCol( anc1, null, null, null, null );
            row3.add( col3 );
            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
          }
          else
          {
            vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_GROUP.BU_SAVE", "Save Group" );
            TableRow row1 = new TableRow();
            Anchor anc = util.createLink( nUserID, "SY_GROUP", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
            TableCol col1 = new TableCol( anc, null, null, null, null );
            row1.add( col1 );

            vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_GROUP.BU_SAVE1", "Save Group And Create New Group" );
            TableRow row3 = new TableRow();
            Anchor anc1 = util.createLink( nUserID, "SY_GROUP", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
            TableCol col3 = new TableCol( anc1, null, null, null, null );
            row3.add( col3 );

            vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_GROUP.BU_ASSIGN", "Assign Users to Group" );
            TableRow row5 = new TableRow();
            Anchor anc3 = util.createLink( nUserID, "SY_GROUP", "Assign", "I", "JavaScript:parent.submit_form('Assign')", "USRGRP", vBPLate, msg.GetMsgDesc( 117, nLangID ));
            TableCol col5 = new TableCol( anc3, null, null, null, null );
            row5.add( col5 );
            tab.add( row1 );
            tab.add( rw );
            tab.add( row3 );
            tab.add( rw );
            tab.add( row5 );
          }
        }
        vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_GROUP.BU_FORMHELP", "Group Form Information" );
        TableRow row = new TableRow();
        Anchor anc = util.createLink( nUserID, "SY_GROUP", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID ));
        TableCol col = new TableCol( anc, null, null, null, null );
        row.add( col );

        vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_GROUP.BU_HELP", "Group Form Help" );
        TableRow row2 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "SY_GROUP", "Help", pvMode, "JavaScript:top.show_systemhelp('302')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID ));
        TableCol col2 = new TableCol( anc1, null, null, null, null );
        row2.add( col2 );
        tab.add(rw);
        tab.add(row);
        tab.add(rw);
        tab.add(row2);
        body.add(tab);
        page.add(head);
        page.add(body);
        page.printPage(out);
    }

}
