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

public class ShowAgnClnsForm extends HttpServlet
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

      String nAuditID=null;
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
 
      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ClientItem/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      Page page = new Page();
      Head head = new Head();
      Title title = new Title( "Order Entry "+"&"+ "Tracking System :- Client - Item Form");
      Body body = new Body( "/ordimg/BACKGR2.GIF", null ); 
      Form form = new Form( "/JOrder/servlets/ShowAgnClnsForm", "POST", "_parent", null, null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding                                                  \n"+
                  "   function submit_form()  {                          	       \n"+
                  "     with( document.forms[0] ) {                     	       \n"+
                  "       nFk_Client_ID[0].value=\"\"                                  \n"+
                  "       nDelete[0].value=\"\"                                        \n"+ 
                  "       for ( var i=1; i<nFk_Client_ID.length; i++ ) {    	       \n"+
                  "         if ( nFk_Client_ID[i].checked )                            \n"+
                  "           nFk_Client_ID[0].value += nFk_Client_ID[i].value + \"~\" \n"+
                  "       }						               \n"+
                  "       for ( var j=1; j<nDelete.length; j++ ) {      	       \n"+
                  "         if ( nDelete[j].checked )                                  \n"+
                  "           nDelete[0].value += nDelete[j].value + \"~\"             \n"+
                  "       }						               \n"+
                  "       submit()      			                       \n"+
                  "     }						               \n"+
                  "   } 						               \n"+
                 "// End Hidding -->");
      scr.add(scrdata);

      Font f = new Font("White", "Arial", "3", null);

      form.add( new FormHidden( "nDelete", null,null ) );
      form.add( new FormHidden( "nDelete", "",null ) );
      form.add( new FormHidden( "nFk_Client_ID", null, null ) );
      form.add( new FormHidden( "nFk_Client_ID", "", null ) );
      form.add( new FormHidden( "pnItemID", pnItemID, null ) );
      form.add(new NL(2));

      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2 BGCOLOR=\"#FFFFCA\"");
      TableRow row = new TableRow("Left",null,"BGCOLOR=\"#666666\"");
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_LABEL.B_CLIENTITEM_FK_CLIENT_ID", "Client Name" );
      TableHeader th = new TableHeader( vBPLate,null, null, null, null);
      th.setFormat(f); 
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_LABEL.B_CLIENTITEM_ASSIGN", "Assign" );
      TableHeader th1 = new TableHeader( vBPLate,null, null, null, null);
      th1.setFormat(f);
      row.add(th) ;
      row.add(th1);
      if(pnItemID != null && !pnItemID.equals("") && !pnItemID.equalsIgnoreCase("null") )
      { 
        vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_LABEL.B_CLIENTITEM_DEASSIGN", "DeAssign" );
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
          query = " SELECT cln.Client_ID, cln.Client_Name "+
                  " FROM   T_ClientItem cit, T_Client cln "+
                  " WHERE  cit.Fk_Client_ID = cln.Client_ID "+
                  " AND    Fk_Item_ID = "+pnItemID;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            TableRow row1 = new TableRow("Left",null,null);
            TableCol col = new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ),null, null, null,null);
            TableCol col1 = new TableCol( util.createLabelItem( "Y", vLabelAttrib ), null, null, null,"ALIGN=\"center\"");
            row1.add(col);
            row1.add(col1);
            FormCheckBox chk = new FormCheckBox( "nDelete",rs.getString(1), null, null);
            TableCol col2 = new TableCol(chk, null, null, null,"ALIGN=\"center\"");
            row1.add(col2);
            tab.add(row1);
          }
          rs.close();
          stmt.close();

          stmt = conn.createStatement();
          query = " SELECT * "+
                  " FROM   T_Client "+
                  " WHERE  Client_ID NOT IN "+
                  "        ( SELECT Fk_Client_ID "+
                  "          FROM   T_CLientItem "+
                  "          WHERE  Fk_Item_ID = "+pnItemID+")";
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            TableRow row2 = new TableRow("Left",null,null);
            TableCol col3 = new TableCol( util.createLabelItem( rs.getString(4), vLabelAttrib ), null, null, null,null);
            FormCheckBox chk1 = new FormCheckBox( "nFk_Client_ID",rs.getString(1), null, null);
            TableCol col4 = new TableCol(chk1, null, null, null,"ALIGN=\"center\""); 
            row2.add(col3);
            row2.add(col4);
            TableCol col5 = new TableCol( util.createLabelItem( "N", vLabelAttrib ), null, null, null, "ALIGN=\"center\"");
            row2.add(col5);
            tab.add(row2);
          }
          rs.close();
          stmt.close();
        }
        else
        {
          query =" SELECT Client_ID, Client_Name "+
                 " FROM   T_Client";
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            TableRow row1 = new TableRow("Left",null,null);
            TableCol col = new TableCol( util.createLabelItem( rs.getString(2), vLabelAttrib ), null, null, null,null);
            FormCheckBox chk = new FormCheckBox( "nFk_Client_ID",rs.getString(1), null, null);
            TableCol col1 = new TableCol(chk, null, null, null,"ALIGN=\"center\"");
            row1.add(col);
            row1.add(col1);
            tab.add(row1);
          } 
        } 
      } catch(Exception e) {}
      finally
      {
        try
        {
          if(stmt!=null)
            stmt.close();
          if(conn!=null)
            conn.close(); 
        }catch(SQLException sexe){}
      }
      Center cen = new Center();
      form.add(tab);
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

    public void doPost(HttpServletRequest request,
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

      int nMsgID =-1;
      String nLangID=null;
      String nUserID=null; 
      String errMsg = null; 
      String vIDArray[]=null;
      String query1 = null;
      PreparedStatement pstmt = null;
      Connection conn = null;

      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();
  
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String pnItemID = request.getParameter("pnItemID");
      String nClientID[] = request.getParameterValues("nFk_Client_ID");
      String nDelete[] = request.getParameterValues("nDelete");

      String usr = db.getName( nUserID, "User" );

      try
      {
        conn = db.GetDBConnection();
     
        /* Delete T_ClientItem Data */

        if( !( nDelete[0].trim()==null || nDelete[0].trim().equals("") || nDelete[0].trim().equalsIgnoreCase("null") ) )
        { 
          vIDArray=Parse.parse(nDelete[0],"~");
          query1 = " DELETE FROM T_ClientItem "+
                   " WHERE  Fk_Client_ID = ?"+
                   " AND    Fk_Item_ID="+pnItemID;
          pstmt = conn.prepareStatement(query1);
          for(int i=0;i<vIDArray.length;i++)
          {
             int bid = Integer.parseInt(vIDArray[i]);  
             pstmt.setInt(1,bid);
             pstmt.executeUpdate();
          }
          pstmt.close();
        }

        /* Insert T_ClientItem Data */

        if( !( nClientID[0].trim()==null || nClientID[0].trim().equals("") || nClientID[0].trim().equalsIgnoreCase("null") ) )
        { 
          vIDArray=Parse.parse(nClientID[0],"~");
          query1 = " INSERT INTO T_ClientItem ( ClientItem_ID, Fk_Client_ID, "+
                   "                            Fk_Item_ID, Modifier, Change_Dt ) "+
                   " VALUES ( ?, ?,"+pnItemID+",'"+usr+"','"+dt+"')";
          pstmt = conn.prepareStatement(query1);
          for(int i=0;i<vIDArray.length;i++)
          {  
             int bid = Integer.parseInt(vIDArray[i]);
             int clnitm_id = Integer.parseInt(db.getNextVal("S_ClientItem"));
             pstmt.setInt(1,clnitm_id);
             pstmt.setInt(2,bid);
             pstmt.executeUpdate();
          }
          pstmt.close();
        }
        nMsgID = 3;
      }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
      finally
      {
        try
        {
          if(pstmt!=null)
            pstmt.close();
          if(conn!=null)
            conn.close(); 
        }catch(SQLException sexe){}
      }
      if( nMsgID <= 5 )
      {
        Script scr = new Script();  
        HtmlTag scrdata = new HtmlTag();
        scrdata.add( " <!-- Start Hidding  \n"+
                     "   top.close()       \n"+
                     " // End Hidding -->  \n"); 
        scr.add(scrdata);
        out.println(scr);
      }
      else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

