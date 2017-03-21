import ingen.html.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import ingen.html.character.*;
import ingen.html.para.*;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AddrTable extends HttpServlet
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
       String nTransID     = null;
       String vBPLate      = null;
       String vImagePath   = null;
       String vColumns     = null;
       String vTitles      = null;
       String vWhereClause = null;
       String vName        = null;
       String nAuditID=null;
       String nLangID=null;
       String nUserID=null;
       String nClientID=null;

       nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
       nUserID   = Parse.GetValueFromString( vPID, "UserID" );
       nLangID   = Parse.GetValueFromString( vPID, "LangID" );
       nClientID   = Parse.GetValueFromString( vPID, "ClientID" );

       DBConnect db = new DBConnect();
       ConfigData cdata = new ConfigData();
       Message msg = new Message();

       String nFlag = request.getParameter("nFlag");
       vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Addr/";

       Page page = new Page();
       Head head = new Head();        
       Title title = new Title("Order - Delivery System : Select Address ");
       Script scr = new Script( "JavaScript", null );
       HtmlTag scrdata = new HtmlTag();
       scrdata.add("<!-- Start Hidding" + "\n");
       scrdata.add(" function submit_form( pvAction ) { \n"+
                   "   if ( pvAction == \"Ok\" ) {           \n"+
                   "     document.forms[0].submit()}         \n"+
                   "    if ( pvAction == \"Cancel\" ) {      \n"+
                   "      top.close()                        \n"+
                   "      return }                           \n"+
                   "  }	");
        scrdata.add(" function setAddress(id){ \n" +
                    " }\n");
        scrdata.add("// End Hidding -->");
        scr.add(scrdata);

        Body body = new Body("/ordimg/BACKGR2.GIF",null);
        Form form = new Form("/JOrder/servlets/AddrTable", "POST", "_parent", null, null);
        form.add(new FormHidden( "nFlag", nFlag, null ));
        vColumns = " adr.ADDRESS_ID, adr.DM_AddType, adr.Address1, adr.City, adr.State ";
        vTitles  = " Address ID,Address Type,Address,City, State";
        vWhereClause = " WHERE  adf.REFTYPE = 'Client' "+
                            " AND    adf.Ref_ID = "+nClientID+
                            " AND    adf.Fk_Address_ID = adr.Address_ID" ;
        Center cen = new Center();
        cen.add(createAddrTable(request,vColumns, vWhereClause, vTitles , vPID));
        cen.add(new NL(1));
        cen.add(new FormButton( null, " Ok ",  "onClick=\"submit_form('Ok')\"" ));
        cen.add(new FormButton( null, " Cancel ",  "onClick=\"submit_form('Cancel')\"" ));
        form.add(cen);
        body.add(form);
        page.add(head);         
        page.add(body);
        head.add(title);
        head.add(scr);
        out.println(page);
    }

   public Table createAddrTable(HttpServletRequest request, String Columns, String WhereClause, String Titles, String vPID)
   {
     String nLangID =null;
     nLangID  = Parse.GetValueFromString( vPID, "LangID" );
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";

     if( WhereClause != null )
       WClause = WhereClause;

     Table table = new Table( null, "Center", Default);
     Font f = new Font("white", "Arial", "3",null);
     Font f1 = new Font(null, "Arial", "3",null);
     TableRow headrow = new TableRow("left", null, null);
     TableHeader th = null;
     TableRow datarow = null;
     int count = 0;

     DBConnect obj = new DBConnect();
     Connection conn = null;
     Statement stmt = null;
     ResultSet rs = null;

     try
      {
          conn = obj.GetDBConnection();
          stmt = conn.createStatement();
          String query = "Select " + Columns + " From T_Address adr, T_AddressRef adf " + WClause;
          rs = stmt.executeQuery(query);

          if( Titles != null )
          {
            String []tokens = Parse.parse( Titles, ",");
            count = tokens.length;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol );
              headrow.add(th);
              th.setFormat(f);
            }
            table.add( headrow );
          }
          else
          {
            String []tokens = Parse.parse( Columns, ",");
            count = tokens.length-2;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol);
              headrow.add(th);
              th.setFormat(f);
            }
            table.add( headrow );
          }

          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            FormRadio rad = new FormRadio( "nAddrID", id, null, null);
            TableCol td1 = new TableCol( "Address # "+id, null, null, null, null);
            td1.add( rad );
            datarow.add(td1);
            td1.setFormat(f1);
            String addtype = Domain.getDomainDescFrmAttrib("ADDTYPE",rs.getString(2), nLangID);
            TableCol td3 = new TableCol( addtype, null, null, null, null);
            datarow.add(td3);
            td3.setFormat(f1);
            for( int i=3; i<=count; i++)
            {
              String data = rs.getString(i);
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";                 
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            
            table.add( datarow );
         }
         return table; 
      }catch(Exception sexe){System.out.println(sexe);}
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
      return null;  
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
      String nLangID=null;
      String nUserID=null;
      String nFlag = request.getParameter("nFlag");
      String pnAddressID = request.getParameter("nAddrID");
      String rAddress[] = null;
      DBConnect db = new DBConnect();

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      if(pnAddressID!=null && !pnAddressID.equals("") && !pnAddressID.equalsIgnoreCase("null"))
        rAddress = db.getRecord(pnAddressID,"Address");
      else
        rAddress = new String[17];

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n"); 
      if( nFlag.equals("1") )
      {
        scrdata.add(" var oldWin = this.opener.document.forms[0]   \n");
        scrdata.add(" oldWin.billaddr_id.value = "+pnAddressID+"   \n"+
                    " with( this.opener.document.blayer) {         \n"+
                    "  document.open();                            \n"+
                    "  document.writeln(\"<b>Billing Address :</b>\")\n"+
                    "  document.writeln(\"<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH='75%' >\")\n"+
                    "  document.writeln(\"<tr><td width='30%'>&nbsp;Name</td><td>"+db.getName(nUserID,"User")+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;Address</td><td>"+rAddress[2]+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;City </td><td>"+rAddress[6]+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;State</td><td>"+rAddress[7]+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;Pin Code</td><td>"+rAddress[8]+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;Phone No</td><td>"+rAddress[9]+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;Fax No</td><td>"+rAddress[10]+"</td></tr>\")\n"+
                    "  document.writeln(\"</table>\")              \n"+
                    "  document.close();                           \n"+
                    "  }                                           \n");
       }
       else if( nFlag.equals("2") )
       {
        scrdata.add(" var oldWin = this.opener.document.forms[0]   \n");
        scrdata.add(" oldWin.shipaddr_id.value = "+pnAddressID+"   \n"+
                    " with( this.opener.document.slayer) {         \n"+
                    "  document.open();                            \n"+
                    "  document.writeln(\"<b>Shipping Address :</b>\")\n"+
                    "  document.writeln(\"<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH='75%' >\")\n"+
                    "  document.writeln(\"<tr><td width='30%'>&nbsp;Name</td><td>"+db.getName(nUserID,"User")+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;Address</td><td>"+rAddress[2]+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;City </td><td>"+rAddress[6]+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;State</td><td>"+rAddress[7]+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;Pin Code</td><td>"+rAddress[8]+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;Phone No</td><td>"+rAddress[9]+"</td></tr>\")\n"+
                    "  document.writeln(\"<tr><td>&nbsp;Fax No</td><td>"+rAddress[10]+"</td></tr>\")\n"+
                    "  document.writeln(\"</table>\")              \n"+
                     "  document.close();                           \n"+
                     "  }                                           \n");
       }
       else if( nFlag.equals("3") )
        {
          scrdata.add(" var oldWin = this.opener.document.forms[0]   \n");
          scrdata.add(" oldWin.billaddr_id.value = "+pnAddressID+"       \n"+
                      " with( this.opener.document.all(\"blayer\")) {\n"+
                     "   var text = '<b>Billing Address :</b>' +     \n"+
                     "              '<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"75%\" >'+  \n"+
                     "              '<tr><td width=\"30%\">&nbsp;Name</td><td>"+db.getName(nUserID,"User")+"</td></tr>'+ \n"+
                     "              '<tr><td>&nbsp;Address</td><td>"+rAddress[2]+"</td></tr>'+\n"+
                     "              '<tr><td>&nbsp;City </td><td>"+rAddress[6]+"</td></tr>' +      \n"+
                     "              '<tr><td>&nbsp;State</td><td>"+rAddress[7]+"</td></tr> ' +    \n"+
                     "              '<tr><td>&nbsp;Pin Code</td><td>"+rAddress[8]+"</td></tr>' +    \n"+
                     "              '<tr><td>&nbsp;Phone No</td><td>"+rAddress[9]+"</td></tr>' +  \n"+
                     "              '<tr><td>&nbsp;Fax No</td><td>"+rAddress[10]+"</td></tr>' +    \n"+
                     "              '</table>';              \n"+
                     "  innerHTML = text;                    \n"+
                     " }                                     \n");


        }
        else if( nFlag.equals("4") )
        {
          scrdata.add(" var oldWin = this.opener.document.forms[0]   \n");
          scrdata.add(" oldWin.shipaddr_id.value = "+pnAddressID+"       \n"+
                      " with( this.opener.document.all(\"slayer\")) {\n"+
                     "   var text = '<b>Shiping Address :</b>' +     \n"+
                     "              '<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"75%\" >'+  \n"+
                     "              '<tr><td width=\"30%\">&nbsp;Name</td><td>"+db.getName(nUserID,"User")+"</td></tr>'+ \n"+
                     "              '<tr><td>&nbsp;Address</td><td>"+rAddress[2]+"</td></tr>'+\n"+
                     "              '<tr><td>&nbsp;City </td><td>"+rAddress[6]+"</td></tr>' +      \n"+
                     "              '<tr><td>&nbsp;State</td><td>"+rAddress[7]+"</td></tr> ' +    \n"+
                     "              '<tr><td>&nbsp;Pin Code</td><td>"+rAddress[8]+"</td></tr>' +    \n"+
                     "              '<tr><td>&nbsp;Phone No</td><td>"+rAddress[9]+"</td></tr>' +  \n"+
                     "              '<tr><td>&nbsp;Fax No</td><td>"+rAddress[10]+"</td></tr>' +    \n"+
                     "              '</table>';              \n"+
                     "  innerHTML = text;                    \n"+
                     " }                                     \n");

        }
       scrdata.add(" top.close()\n");
       scrdata.add("// End Hidding -->");
       scr.add(scrdata);
       out.println(scr);
   }
}


