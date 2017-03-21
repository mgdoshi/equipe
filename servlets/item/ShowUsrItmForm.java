import ingen.html.*;
import ingen.html.character.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ShowUsrItmForm extends HttpServlet
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
      String nSchemeID=null;
      String vBPLate=null;
      String vImagePath=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vStatus = null;

      String pnItemID = request.getParameter( "pnItemID");
   
      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
 
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_User/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      Page page = new Page();
      Head head = new Head();
      Title title = new Title( "Order Entry "+"&"+ "Tracking System :- User - Item Form");
      Body body = new Body( "/ordimg/BACKGR2.GIF","onLoad=\"load_form()\""); 
      Form form = new Form();

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "  var ObjOldWin = oldWindow.mid_frame.right_frame.document.forms[0]   \n" +

                  "  function load_form() 	                                           \n" +
                  "  {				                                           \n" +
                  "    with ( document.forms[0] )                                          \n" +
                  "    {				 	                           \n" +
                  "       for ( var i=1; i<nFk_User_ID.length; i++ ) {    	           \n" +
                  "         if ( ObjOldWin.nFk_User_ID.value.indexOf( nFk_User_ID[i].value+\"~\" ) > -1 )\n" +
                  "           nFk_User_ID[i].checked = true + \"~\"                        \n" +
                  "       }						                   \n" +
                  "       for ( var j=1; j<nDel.length; j++ ) {     	                   \n" +
                  "         if ( ObjOldWin.nDel.value.indexOf( nDel[j].value+\"~\" ) > -1 )\n" +
                  "           nDel[j].checked = true + \"~\"                               \n" +
                  "       }						                   \n" +
                  "    }				 	                           \n" +
                  "  }					                                 \n\n" +

                  "   function submit_form()                            	           \n" +
                  "   { 						                   \n" +
                  "     with( document.forms[0] ) {                     	           \n" +
                  "       ObjOldWin.nFk_User_ID.value = \"\"                	           \n" +
                  "       ObjOldWin.nDel.value = \"\"                    	           \n" +
                  "       for ( var i=1; i<nFk_User_ID.length; i++ ) {    	           \n" +
                  "         if ( nFk_User_ID[i].checked )                                  \n" +
                  "           ObjOldWin.nFk_User_ID.value += nFk_User_ID[i].value + \"~\"  \n" +
                  "       }						                   \n" +
                  "       for ( var j=1; j<nDel.length; j++ ) {        	                   \n" +
                  "         if ( nDel[j].checked )                                         \n" +
                  "           ObjOldWin.nDel.value += nDel[j].value + \"~\"                \n" +
                  "       }						                   \n" +
                  "     }						                   \n" +
                  "     top.close()					                   \n" +
                  "  }					                                  \n\n" +

                 "// End Hidding -->");
      scr.add(scrdata);

      Font f = new Font("White", "Arial", "3", null);
      form.add( new FormHidden( "nDel", "dummy",null ) );
      form.add( new FormHidden( "nFk_User_ID", "dummy", null ) );
      form.add( new NL(2) );

      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2 BGCOLOR=\"#FFFFCA\"");
      TableRow row = new TableRow("Left",null,"BGCOLOR=\"#666666\"");
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_USER_NAME", "User Name" );
      TableHeader th = new TableHeader( vBPLate,null, null, null, null);
      th.setFormat(f); 
      vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_ASSIGN", "Assign" );
      TableHeader th1 = new TableHeader( vBPLate,null, null, null, null);
      th1.setFormat(f);
      row.add(th) ;
      row.add(th1);
      if(pnItemID != null && !pnItemID.equals("") && !pnItemID.equalsIgnoreCase("null") )
      { 
        vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_DEASSIGN", "DeAssign" );
        TableHeader th2 = new TableHeader( vBPLate,null, null, null, null);
        th2.setFormat(f);
        row.add(th2);
      }
      tab.add(row);
      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();
        String query = null;
        if(pnItemID != null && !pnItemID.equals("") && !pnItemID.equalsIgnoreCase("null") )
        {
          query = " SELECT usr.User_ID, usr.User_Name "+
                  " FROM   T_UserItem uit, T_User usr "+
                  " WHERE  uit.Fk_User_ID = usr.User_ID "+
                  " AND    uit.Fk_Item_ID = "+pnItemID;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            TableRow row1 = new TableRow("Left",null,null);
            TableCol col = new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ), null, null, null,null);
            TableCol col1 = new TableCol( util.createLabelItem( "Y", vLabelAttrib ), null, null, null,"ALIGN=\"center\"");
            row1.add(col);
            row1.add(col1);
            FormCheckBox chk = new FormCheckBox( "nDel",rs.getString(1), null, null);
            TableCol col2 = new TableCol(chk, null, null, null,"ALIGN=\"center\"");
            row1.add(col2);
            tab.add(row1);
          }
          rs.close();
          stmt.close();

          stmt = conn.createStatement();
          query = " SELECT * "+
                  " FROM   T_User "+
                  " WHERE  User_ID NOT IN "+
                  "        ( SELECT Fk_User_ID "+
                  "          FROM   T_UserItem "+
                  "          WHERE  Fk_Item_ID = "+pnItemID+")";
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            TableRow row2 = new TableRow("Left",null,null);
            TableCol col3 = new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib), null, null, null,null);
            FormCheckBox chk1 = new FormCheckBox( "nFk_User_ID",rs.getString(1), null, null);
            TableCol col4 = new TableCol(chk1,null, null, null,"ALIGN=\"center\""); 
            row2.add(col3);
            row2.add(col4);
            TableCol col5 = new TableCol( util.createLabelItem( "N", vLabelAttrib), null, null, null, "ALIGN=\"center\"");
            row2.add(col5);
            tab.add(row2);
          }
          rs.close();
          stmt.close();
        }
        else
        {
          query =" SELECT User_ID, User_Name "+
                 " FROM   T_User";
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            TableRow row1 = new TableRow("Left",null,null);
            TableCol col = new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ),null, null, null,null);
            FormCheckBox chk = new FormCheckBox( "nFk_User_ID",rs.getString(1), null, null);
            TableCol col1 = new TableCol(chk,null, null, null,"ALIGN=\"center\"");
            row1.add(col);
            row1.add(col1);
            tab.add(row1);
          } 
        } 
      }catch(Exception e) { 
        System.out.println("Exception Occures	"+e);
	System.out.println("Exception Occures	"+e.getMessage()); }
      finally
      {
        try
        {
          if(rs!=null)
            rs.close();
          if(stmt!=null)
            stmt.close();
          if(conn!=null)
            conn.close(); 
        }catch(SQLException sexe){}
      }
      Center cen = new Center();
      form.add( tab );
      form.add( new NL(2) );
      cen.add( new FormButton( null, " OK ", "onClick=\"submit_form()\"" ) );
      cen.add( new FormButton( null, "Cancel", "onClick=\"top.close()\"" ) );
      form.add(cen);
      body.add(form);
      head.add(title);
      head.add(scr);
      page.add(head);
      page.add(body);
      out.println(page);
    }
}

