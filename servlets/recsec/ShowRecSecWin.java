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

public class ShowRecSecWin extends HttpServlet
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
      String vBPLate=null;
      String vImagePath=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vStatus = null;
      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      String pnRecSecID = request.getParameter( "pnRecSecID");
   
      DBConnect db = new DBConnect();
      Statement stmt = null;
      Connection conn=null;
      ResultSet rs = null;
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
 
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_RecSec/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      Page page = new Page();
      Head head = new Head();
      Title title = new Title( "Order Entry "+"&"+ "Tracking System -:- Assign Record Security Priviledge");
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
                  "       for ( var i=1; i<vRecSecPriv.length; i++ ) {    	           \n" +
                  "         if ( ObjOldWin.vRecSecPriv.value.indexOf( vRecSecPriv[i].value+\"~\" ) > -1 )\n" +
                  "           vRecSecPriv[i].checked = true                                \n" +
                  "       }						                   \n" +
                  "       for ( var j=1; j<nDelete.length; j++ ) {            	           \n" +
                  "         if ( ObjOldWin.nDelete.value.indexOf( nDelete[j].value+\"~\" ) > -1 )\n" +
                  "           nDelete[j].checked = true                                    \n" +
                  "       }						                   \n" +
                  "    }				 	                           \n" +
                  "  }					                                 \n\n" +

                  "   function submit_form()                            	           \n" +
                  "   { 						                   \n" +
                  "     with( document.forms[0] ) {                     	           \n" +
                  "       ObjOldWin.vRecSecPriv.value = \"\"                  	           \n" +
                  "       ObjOldWin.nDelete.value = \"\"                    	           \n" +
                  "       for ( var i=1; i<vRecSecPriv.length; i++ ) {    	           \n" +
                  "         if ( vRecSecPriv[i].checked )                                  \n" +
                  "           ObjOldWin.vRecSecPriv.value += vRecSecPriv[i].value + \"~\"  \n" +
                  "       }						                   \n" +
                  "       for ( var j=1; j<nDelete.length; j++ ) {        	           \n" +
                  "         if ( nDelete[j].checked )                                      \n" +
                  "           ObjOldWin.nDelete.value += nDelete[j].value + \"~\"          \n" +
                  "       }						                   \n" +
                  "     }						                   \n" +
                  "     top.close()					                   \n" +
                  "  }					                                  \n\n" +

                 "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden( "nDelete", "dummy",null ));
      form.add(new FormHidden( "vRecSecPriv", "dummy", null ));
      form.add(new NL(3));
      Font f = new Font("White", "Arial", "3", null);
      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2 BGCOLOR=\"#FFFFCA\"");
      TableRow row = new TableRow("Left",null,"BGCOLOR=\"#666666\"");
      vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_LABEL.B_RECSEC_RECSEC_NAME", "Record Security Name" );
      TableHeader th = new TableHeader( vBPLate,null, null, null,"WIDTH=\"25%\""); 
      th.setFormat(f); 
      vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_LABEL.B_RECSEC_ASSIGN", "Assign" );
      TableHeader th1 = new TableHeader( vBPLate,null, null, null,"WIDTH=\"25%\"");
      th1.setFormat(f);
      row.add(th) ;
      row.add(th1);
      if(pnRecSecID != null && !pnRecSecID.equals("") && !pnRecSecID.equalsIgnoreCase("null") )
      { 
        vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_LABEL.B_RECSEC_DEASSIGN", "DeAssign" );
        TableHeader th2 = new TableHeader( vBPLate,null, null, null,"WIDTH=\"25%\"");
        th2.setFormat(f);
        row.add(th2);
      }
      tab.add(row);
      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();
        String query = null;
        if(pnRecSecID != null && !pnRecSecID.equals("") && !pnRecSecID.equalsIgnoreCase("null") )
        {
          query = " SELECT rsc.RecSec_ID, rsc.RecSec_Name "+
                  " FROM   T_RecSecPriv rsp, T_RecSec rsc "+
                  " WHERE  rsp.Managed_RecSec_ID = rsc.RecSec_ID "+
                  " AND    rsp.RecSec_ID = "+pnRecSecID;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            TableRow row1 = new TableRow("Left",null,null);
            TableCol col = new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ), null, null, null,null);
            TableCol col1 = new TableCol( util.createLabelItem( "Y", vLabelAttrib ),null, null, null,"ALIGN=\"center\"");
            row1.add(col);
            row1.add(col1);
            if( pnRecSecID != null )
            {
              FormCheckBox chk = new FormCheckBox( "nDelete",rs.getString(1), null, null);
              TableCol col2 = new TableCol(chk,null, null, null,"ALIGN=\"center\"");
              row1.add(col2);
            } 
            tab.add(row1);
          }

          query = " SELECT * "+
                  " FROM   T_RecSec rsc "+
                  " WHERE  rsc.RecSec_ID NOT IN "+
                  " ( SELECT rsp.Managed_RecSec_ID "+
                  "   FROM   T_RecSecPriv rsp "+
                  "   WHERE  rsp.RecSec_ID = "+pnRecSecID +")";
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            TableRow row2 = new TableRow("Left",null,null);
            TableCol col3 = new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ),null, null, null,null);
            FormCheckBox chk1 = new FormCheckBox( "vRecSecPriv",rs.getString(1), null, null);
            TableCol col4 = new TableCol(chk1,null, null, null,"ALIGN=\"center\""); 
            row2.add(col3);
            row2.add(col4);
            if( pnRecSecID != null )
            {
              TableCol col5 = new TableCol( util.createLabelItem( "N", vLabelAttrib ),null, null, null, "ALIGN=\"center\"");
              row2.add(col5);
            }
            tab.add(row2);
          }
        }
        else
        {
          query =" SELECT RecSec_ID, RecSec_Name "+
                 " FROM   T_RecSec";
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            TableRow row1 = new TableRow("Left",null,null);
            TableCol col = new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ),null, null, null,null);
            FormCheckBox chk = new FormCheckBox( "vRecSecPriv",rs.getString(1), null, null);
            TableCol col1 = new TableCol(chk,null, null, null,"ALIGN=\"center\"");
            row1.add(col);
            row1.add(col1);
            tab.add(row1);
          } 
        } 
        stmt.close();
        conn.close();
      }
      catch(Exception e) { 
        System.out.println("Exception Occures	"+e);
	System.out.println("Exception Occures	"+e.getMessage());
      }
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
      form.add(tab);
      form.add(new NL(2));
      cen.add(new FormButton( null, " OK ", "onClick=\"submit_form()\"" ));
      cen.add(new FormButton( null, "Cancel", "onClick=\"top.close()\"" ));
      form.add(cen);
      body.add(form);
      head.add(title);
      head.add(scr);
      page.add(head);
      page.add(body);
      out.println(page);
    }
}

