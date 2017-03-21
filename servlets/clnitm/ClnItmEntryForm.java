import ingen.html.*;
import ingen.html.character.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.net.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ClnItmEntryForm extends HttpServlet
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

      int flag=0;

      String vBPLate=null;
      String vImagePath=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vFormType=null;
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

      String pvItemName = request.getParameter( "pvItemName");
      String pnClientID = request.getParameter( "pnClientID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      Connection conn =null;
      Statement stmt = null;
      ResultSet rs = null;
      String query = null;

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ClientItem/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );

      vFormType = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "WD_FORM_INSERT","ClientItem / I");
      nTransID = Trans.getTransID( nAuditID, 'M');

      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();
        query = " SELECT itm.Item_ID "+
                  " FROM   T_ClientItem cit, T_Item itm "+
                  " WHERE  cit.Fk_Item_ID = itm.Item_ID "+
                  " AND    cit.Fk_Client_ID = "+pnClientID+
                  " AND    itm.Item_Name LIKE '"+pvItemName+"'";
        rs = stmt.executeQuery(query);
        if(rs.next())
         flag=1; 
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

      Font f = new Font("White", "Arial", "3", null);
      Page page = new Page();
      Head head = new Head();
      Body body = new Body( "/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ClnItmEntryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add(new FormHidden( "pnTransID", nTransID,null ));
      form.add(new FormHidden( "nClient_ID", pnClientID, null ));
      form.add(new FormHidden( "vAssign", null,null ));
      form.add(new FormHidden( "vAssign", null, null ));
      form.add(new FormHidden( "vDeAssign", null,null ));
      form.add(new FormHidden( "vDeAssign", null, null ));
      form.add(new NL(2));

      Table tab = new Table("0","center","Border=\"0\" width=\"20%\"");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_LABEL.B_CLIENTITEM_CLIENT", "Client : " );
      row.add(new TableCol(util.createLabelItem("<B>"+vBPLate+"</B>&nbsp;" + db.getName(pnClientID,"CLIENT"),vLabelAttrib),null, null, null,null));
      tab.add(row);

      Table tab1 = new Table("0","center","Border=\"0\" width=\"40%\" BGCOLOR=\"#FFFFCA\"");
      TableRow row1 = new TableRow("Left",null,"BGCOLOR=\"#666666\"");

      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_LABEL.B_CLIENTITEM_FK_ITEM_ID", "Item Name" );
      TableHeader th1 = new TableHeader( vBPLate,null, null, null, null);
      th1.setFormat(f);
      row1.add(th1);

      vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_LABEL.B_CLIENTITEM_ASSIGN", "Assign" );
      TableHeader th2 = new TableHeader( vBPLate,null, null, null, null);
      th2.setFormat(f);
      row1.add(th2);

      if( flag > 0 )
      { 
        vBPLate = cdata.GetConfigValue( "ST_CLIENTITEM", nLangID, "BL_LABEL.B_CLIENTITEM_DEASSIGN", "DeAssign" );
        TableHeader th3 = new TableHeader( vBPLate,null, null, null, null);
        th3.setFormat(f);
        row1.add(th3);
      }
      tab1.add(row1);

      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();
        query = " SELECT itm.Item_ID, itm.Item_Name "+
                  " FROM   T_ClientItem cit, T_Item itm "+
                  " WHERE  cit.Fk_Item_ID = itm.Item_ID " +
                  " AND    cit.Fk_Client_ID = "+pnClientID +
                  " AND    itm.Item_Name LIKE '"+pvItemName+"'";
        rs = stmt.executeQuery(query);
        while(rs.next())
        {
           TableRow row2 = new TableRow("Left",null,null);
           row2.add(new TableCol(util.createLabelItem(rs.getString(2), vLabelAttrib ),null, null, null,null));
           row2.add(new TableCol(util.createLabelItem("Y",vLabelAttrib),null, null, null,"ALIGN=\"center\""));
           if( flag > 0 )  
             row2.add(new TableCol(new FormCheckBox( "vDeAssign",rs.getString(1), null, null),null, null, null,"ALIGN=\"center\""));
           tab1.add(row2);
        }
        rs.close();
        stmt.close();
        stmt = conn.createStatement();
        query = " SELECT itm.Item_ID, itm.Item_Name "+
                   "  FROM   T_Item itm "+
                   "  WHERE  itm.Item_ID NOT IN "+
                   "        ( SELECT cit.Fk_Item_ID "+
                   "          FROM   T_ClientItem cit "+
                   "          WHERE  cit.Fk_Client_ID = "+pnClientID+")"+
                   " AND itm.Item_Name LIKE '"+pvItemName+"'";
        rs = stmt.executeQuery(query);
        while(rs.next())
        {
          TableRow row3 = new TableRow("Left",null,null);
          row3.add(new TableCol(util.createLabelItem(rs.getString(2), vLabelAttrib ),null, null, null,null));
          row3.add(new TableCol(new FormCheckBox( "vAssign",rs.getString(1), null, null),null, null, null,"ALIGN=\"center\""));
          if(flag > 0 )
            row3.add(new TableCol(util.createLabelItem("N", vLabelAttrib ),null, null, null, "ALIGN=\"center\""));
          tab1.add(row3);
        }
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
           if(conn!=null)
             conn.close();
           if(stmt!=null)
             stmt.close(); 
        } 
        catch(SQLException e) { }
      }
      form.add(tab);
      form.add(tab1);
      body.add(form);
      head.add(scr);
      page.add(head);
      page.add(body);
      out.println(page);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
    throws IOException, ServletException
    {
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
 
      String errMsg = null; 
      String vStatus=null;
      String query = null;
      String nLangID=null;
      String nUserID=null;
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      IngDate dt = new IngDate();
  
      String pnTransID = request.getParameter("pnTransID");
      String nClient_ID = request.getParameter("nClient_ID"); 
      String vAssign[] = request.getParameterValues("vAssign");
      String vDeAssign[] = request.getParameterValues("vDeAssign");
      String usr       = db.getName( nUserID, "User" );      
      vStatus          = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            for( int i=2; i<vAssign.length; i++ )
            {
                  int citem_id = Integer.parseInt(db.getNextVal("S_ItemRate"));
                  query = " INSERT INTO T_ClientItem ( ClientItem_ID, Fk_Client_ID, Fk_Item_ID,Modifier, Change_Dt ) "+
                          " VALUES( "+citem_id+","+nClient_ID+","+Integer.parseInt(vAssign[i])+",'"+usr+"','"+dt+"')";
                  stmt.executeUpdate(query);
            }

            stmt.close();
            stmt = conn.createStatement();
            for( int i=2; i<vDeAssign.length; i++ )
            {
               query = " DELETE FROM T_ClientItem " + 
                       " WHERE Fk_Client_ID = "+nClient_ID+" AND Fk_Item_ID = "+Integer.parseInt(vDeAssign[i])+"";
               stmt.executeUpdate(query);
            }
            nMsgID = 3;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
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
          Trans.setTransID(pnTransID);
       }
       if( nMsgID <= 5 )
           response.sendRedirect("/JOrder/servlets/ClnItmFrame?pvMode=D&pnClientID=&pvItemName=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
    }
}

