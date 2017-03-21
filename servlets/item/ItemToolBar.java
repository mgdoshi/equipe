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

public class ItemToolBar extends HttpServlet
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
        String vBPLate = null;

        String pvMode = request.getParameter("pvMode"); 

        WebUtil util = new WebUtil();
        Message msg = new Message();
        ConfigData cdata = new ConfigData();

        nUserID   = Parse.GetValueFromString( vPID, "UserID" );
        nLangID   = Parse.GetValueFromString( vPID, "LangID" );

        Page page = new Page();
        Head head = new Head();
        Body body = new Body(null, "BGCOLOR=#666666");
        Table tab = new Table("1", "center", "Border=\"0\" width=\"8%\" COLS=1" );

        TableRow blank = new TableRow();
        blank.add( new TableCol( "&nbsp;", null, null, null, null ) );
        
        if( pvMode!=null && pvMode.equalsIgnoreCase("Q") )
        {
          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_QUERY", "Query Item" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_ITEM", "Query", "Q", "JavaScript:parent.submit_form('Query')", "QRYTAB", vBPLate, msg.GetMsgDesc( 126, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_NEW", "Create New Item" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_ITEM", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row2.add( new TableCol( anc1, null, null, null, null ) );

          tab.add( row1 );
          tab.add( blank );
          tab.add( row2 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
        {
          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_NEW", "Create New Item" );
          TableRow row1 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_ITEM", "Insert", "I", "JavaScript:parent.submit_form('New')", "NEW", vBPLate, msg.GetMsgDesc( 103, nLangID ));
          row1.add( new TableCol( anc1, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_DELETE", "Delete Item" );
          TableRow row2 = new TableRow();
          Anchor anc2 = util.createLink( nUserID, "ST_ITEM", "Delete", "I", "JavaScript:parent.submit_form('Delete')", "DELETE", vBPLate, msg.GetMsgDesc( 102, nLangID ));
          row2.add( new TableCol( anc2, null, null, null, null ) );

          tab.add( row1 );
          tab.add( blank );
          tab.add( row2 );
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("U"))
        {
          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_UPDATE", "Update Item" );
          TableRow row1 = new TableRow();
          Anchor anc = util.createLink( nUserID, "ST_ITEM", "Update", pvMode, "JavaScript:parent.submit_form('Update')", "SAVE", vBPLate, msg.GetMsgDesc( 101, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_ASSIGN", "Assign Item Rates" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_ITEM", "AssignRate", "I", "JavaScript:parent.submit_form('Assign1')", "NITMRT", vBPLate, msg.GetMsgDesc( 113, nLangID ));
          row2.add( new TableCol( anc1, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_ASSIGN2", "Update/Delete Item Rate" );
          TableRow row3 = new TableRow();
          Anchor anc2 = util.createLink( nUserID, "ST_ITEM", "UpdDelRate", "I", "JavaScript:parent.submit_form('Assign2')", "MITMRT", vBPLate, msg.GetMsgDesc( 114, nLangID ));
          row3.add( new TableCol( anc2, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_ASSIGN1", "Assign Clients" );
          TableRow row4 = new TableRow();
          Anchor anc3 = util.createLink( nUserID, "ST_ITEM", "AssignCln", "I", "JavaScript:parent.submit_form('Assign3')", "ASSCLNT", vBPLate, msg.GetMsgDesc( 105, nLangID ));
          row4.add( new TableCol( anc3, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_ASSIGN3", "Assign Users to Item" );
          TableRow row5 = new TableRow();
          Anchor anc4 = util.createLink( nUserID, "ST_ITEM", "AssignUsr", "I", "JavaScript:parent.submit_form('Assign4')", "ASSUSER", vBPLate, msg.GetMsgDesc( 137, nLangID ));
          row5.add( new TableCol( anc4, null, null, null, null ) );

          tab.add( row1 );
          tab.add( blank );
          tab.add( row2 );
          tab.add( blank );
          tab.add( row3 );
          tab.add( blank );
          tab.add( row4 );
          tab.add( blank );
          tab.add( row5 );
        }
        else
        {
          TableRow row1 = new TableRow();
          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_SAVE", "Save Item" );
          Anchor anc = util.createLink( nUserID, "ST_ITEM", "Insert", "I", "JavaScript:parent.submit_form('Insert')", "SAVE", vBPLate, msg.GetMsgDesc( 104, nLangID ));
          row1.add( new TableCol( anc, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_SAVE1", "Save Item And Create New Item" );
          TableRow row2 = new TableRow();
          Anchor anc1 = util.createLink( nUserID, "ST_ITEM", "SaveInsert", "I", "JavaScript:parent.submit_form('SaveInsert')", "NSAVE", vBPLate, msg.GetMsgDesc( 125, nLangID ));
          row2.add( new TableCol( anc1, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_ASSIGN", "Assign Item Rates" );
          TableRow row3 = new TableRow();
          Anchor anc2 = util.createLink( nUserID, "ST_ITEM", "AssignRate", "I", "JavaScript:parent.submit_form('Assign1')", "NITMRT", vBPLate, msg.GetMsgDesc( 113, nLangID ));
          row3.add( new TableCol( anc2, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_ASSIGN1", "Assign Clients" );
          TableRow row4 = new TableRow();
          Anchor anc3 = util.createLink( nUserID, "ST_ITEM", "AssignCln", "I", "JavaScript:parent.submit_form('Assign3')", "ASSCLNT", vBPLate, msg.GetMsgDesc( 105, nLangID ));
          row4.add( new TableCol( anc3, null, null, null, null ) );

          vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_ASSIGN3", "Assign Users to Item" );
          TableRow row5 = new TableRow();
          Anchor anc4 = util.createLink( nUserID, "ST_ITEM", "AssignUsr", "I", "JavaScript:parent.submit_form('Assign4')", "ASSUSER", vBPLate, msg.GetMsgDesc( 137, nLangID ));
          row5.add( new TableCol( anc4, null, null, null, null ) );

          tab.add( row1 );
          tab.add( blank );
          tab.add( row2 );
          tab.add( blank );
          tab.add( row3 );
          tab.add( blank );
          tab.add( row4 );
          tab.add( blank );
          tab.add( row5 );
        }

        TableRow row1 = new TableRow();
        vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_FORMHELP", "Item Form Information" );
        Anchor anc = util.createLink( nUserID, "ST_ITEM", "FormHelp", pvMode, "JavaScript:parent.show_FormInfo()", "FRMHLP", vBPLate, msg.GetMsgDesc( 121, nLangID) );
        row1.add( new TableCol( anc, null, null, null, null ) );

        vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_HELP", "Item Form Help" );
        TableRow row2 = new TableRow();
        Anchor anc1 = util.createLink( nUserID, "ST_ITEM", "Help", pvMode, "JavaScript:top.show_systemhelp('323')", "HELP", vBPLate, msg.GetMsgDesc( 139, nLangID) );
        row2.add( new TableCol( anc1, null, null, null, null ) );

        tab.add(blank);
        tab.add( row1 );
        tab.add(blank);
        tab.add( row2 );
        body.add(tab);
        page.add(head);
        page.add(body);
        out.println(page);
    }

}
