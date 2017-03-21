import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ItemRtEntryForm extends HttpServlet
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
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;

      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;

      String pvWhereClause = request.getParameter("pvWhereClause");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID    = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID  = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "WD_FORM_INSERT","ItemRate / I");
      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ItemRate/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      String vItemName  = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FK_ITEM_ID", "Item Name" );
      String vFromDate  = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_FROM_DT", "From Date" );
      String vToDate    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_TO_DT", "To Date" );
      String vMinQty    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MINQTY", "Min Qty" );
      String vMaxQty    = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_MAXQTY", "Max Qty" );
      String vUnitPrice = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_UNITPRICE", "Unit Price" );
      String viActive = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "BL_LABEL.B_ITEMRATE_INACTIVE", "Active" );


      String vItemQry = " SELECT itm.Item_ID, itm.Item_Name   " +
                 " FROM   T_Item itm, T_UserItem uit   " +
                 " WHERE  uit.Fk_Item_ID = itm.Item_ID " +
                 " AND    uit.Fk_User_ID = " + nUserID +
                 " AND    itm.InActive = '1'" +
                 " ORDER BY itm.Item_Name";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ItemRtEntryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add(new FormHidden("pvWhereClause", pvWhereClause, null ));
      form.add(new FormHidden("nInActive", null, null ));
      form.add(new NL(1));

      TableCol blank = new TableCol( "&nbsp;",null, null, null,null);
      Table tab = new Table("1","center","Border=\"0\" width=\"90%\"");
 
      TableRow row = new TableRow("Left",null,null);
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"name.gif", vItemName, vLabelAttrib ),null, null, null,null);
      col.add( WebUtil.NotNull );
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"fromdt.gif", vFromDate, vLabelAttrib ),null, null, null,null);
      col1.add( WebUtil.NotNull );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"todt.gif", vToDate, vLabelAttrib ),null, null, null,null);
      col2.add( WebUtil.NotNull );
      TableCol col3 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"minqty.gif", vMinQty, vLabelAttrib ),null, null, null,null);
      col3.add( WebUtil.NotNull );
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"maxqty.gif", vMaxQty, vLabelAttrib ),null, null, null,null);
      col4.add( WebUtil.NotNull );
      TableCol col5 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unit.gif", vUnitPrice, vLabelAttrib ),null, null, null,null);
      col5.add( WebUtil.NotNull );
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Inactive.gif", viActive, vLabelAttrib ),null, null, null,null);
      row.add(col);
      row.add(col1);
      row.add(col2);
      row.add(col3);
      row.add(col4);
      row.add(col5);
      row.add(col6);

      TableRow row1 = new TableRow("Left",null,null);
      TableCol col7 = new TableCol( util.createLabelItem( "DD.MM.YYYY", vLabelAttrib ),null,null,null,null);
      row1.add(blank);
      row1.add(col7);
      row1.add(col7);
      row1.add(blank);
      row1.add(blank);
      row1.add(blank);
      row1.add(blank);

      tab.add(row);
      tab.add(row1);

      HtmlTag itemlist = util.createList( nUserID, "ST_ITEMRATE", "nFk_Item_ID", "I", vItemQry, null, null, vListAttrib );
      TableRow row2 = new TableRow("Left",null,null);
      row2.add(new TableCol(itemlist,null,null,null,null));
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dFrom_Dt", "I", "6", "10", null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null));
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "dTo_Dt", "I", "6", "10", null, "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null));
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMinQty", "I", "6", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null));
      row2.add(new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nMaxQty", "I", "6", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null));
      row2.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMRATE", "nUnitPrice", "I", "8", "100", null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null,null,null,null));
      row2.add(new TableCol(new FormCheckBox( "nInActive", null, null, null ), "Center", null, null, null));
      for( int i=0; i < 10; i++ )
        tab.add(row2);
      form.add(tab); 
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
      String nTransID=null; 
      String errMsg = null; 
      String vStatus=null;
      String mode=""; 
      String query = null;
      PreparedStatement pstmt = null;
      Connection conn = null;
 
      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      IngDate dt = new IngDate();
  
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID    = Parse.GetValueFromString( vPID, "LangID" );

      String pnTransID = request.getParameter("pnTransID");
      String nFk_Item_ID[] = request.getParameterValues("nFk_Item_ID"); 
      String dFromDt[] = request.getParameterValues("dFrom_Dt");
      String dToDt[] = request.getParameterValues("dTo_Dt");
      String nMinQty[] = request.getParameterValues("nMinQty");
      String nMaxQty[] = request.getParameterValues("nMaxQty");
      String nUnitPrice[] = request.getParameterValues("nUnitPrice");
      String nInActive[] = request.getParameterValues("nInActive");
      String vWhereClause = URLEncoder.encode( request.getParameter("pvWhereClause") );

      String usr       = db.getName( nUserID, "User" );      
      vStatus          = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
          try
          {
            conn = db.GetDBConnection();
            query = " INSERT INTO T_ItemRate( ItemRate_ID, Fk_Item_ID, From_Dt, "+
                    "             To_Dt, MinQty, MaxQty, UnitPrice, InActive, "+
                    "             Modifier, Change_Dt ) "+
                    " VALUES( ?, ?, ?, ?, ?, ?, ?, ?,'"+usr+"','"+dt+"')";
            pstmt = conn.prepareStatement(query);
           
            /* Insert T_ItemRate Data */
            for( int i=0; i<10; i++ )
            {
               if( !( nFk_Item_ID[i]==null || nFk_Item_ID[i].equals("") || nFk_Item_ID[i].equalsIgnoreCase("null") ) )
               {
                  int itmrt_id = Integer.parseInt(db.getNextVal("S_ItemRate"));
                  pstmt.setInt(1,itmrt_id);
                  pstmt.setInt(2,Integer.parseInt(nFk_Item_ID[i]));
                  pstmt.setDate(3,IngDate.strToDate(dFromDt[i]));
                  pstmt.setDate(4,IngDate.strToDate(dToDt[i]));
                  pstmt.setInt(5,Integer.parseInt(nMinQty[i]));
                  pstmt.setInt(6,Integer.parseInt(nMaxQty[i]));
                  pstmt.setInt(7,Integer.parseInt(nUnitPrice[i]));
                  pstmt.setString(8,Parse.GetSubString(nInActive[0],"~",i));
                  pstmt.executeUpdate();
               }
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
          Trans.setTransID(pnTransID);
       }
       if( nMsgID <= 5 )
       {
         response.sendRedirect("/JOrder/servlets/ItemRtFrame?pvMode=&pnItemRateID=&pvWhereClause="+vWhereClause+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       }
       else
         out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}
