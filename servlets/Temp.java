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

public class Temp extends HttpServlet
{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
    throws IOException, ServletException
    {
      long starttime = System.currentTimeMillis();
      long endtime;
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
      String temp=null;

      String pvWhereClause = request.getParameter("pvWhereClause");

      DBConnect db = new DBConnect();
      WebUtil1 util = new WebUtil1();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID    = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID  = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      ConfigData cdata = new ConfigData();
      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      Properties list = new Properties();  
      try
      {
         conn = db.GetDBConnection();
         conn.setReadOnly(true);
         String sql = " SELECT Obj_name,Property_Value" +
                      " FROM   T_Config"+
                      " WHERE  parent_obj = 'ST_ITEMRATE'"+
                      " AND    fk_lang_id = " + nLangID;
         stmt = conn.createStatement();
         rs = stmt.executeQuery(sql);
         while(rs.next())
         {
           list.put(rs.getString(1),rs.getString(2));
         }
         stmt.close();
         conn.close();
      }catch(Exception sexe){System.out.println(sexe);}
      String vItemName = list.getProperty("BL_LABEL.B_ITEMRATE_FK_ITEM_ID", "Item Name"  );
      String vFromDate  = list.getProperty("BL_LABEL.B_ITEMRATE_FROM_DT", "From Date" );
      String vToDate    = list.getProperty("BL_LABEL.B_ITEMRATE_TO_DT", "To Date" );
      String vMinQty    = list.getProperty("BL_LABEL.B_ITEMRATE_MINQTY", "Min Qty" );
      String vMaxQty    = list.getProperty("BL_LABEL.B_ITEMRATE_MAXQTY", "Max Qty" );
      String vUnitPrice = list.getProperty("BL_LABEL.B_ITEMRATE_UNITPRICE", "Unit Price" );
      String viActive = list.getProperty("BL_LABEL.B_ITEMRATE_INACTIVE", "Active" );

      vFormType = cdata.GetConfigValue( "ST_ITEMRATE", nLangID, "WD_FORM_INSERT","ItemRate / I");
      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ItemRate/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

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
      Table tab = new Table("1","center","Border=\"0\" width=\"85%\"");
 
      TableRow row = new TableRow("Left",null,null);
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"name.gif", vItemName, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"fromdt.gif", vFromDate, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"todt.gif", vToDate, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"minqty.gif", vMinQty, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"maxqty.gif", vMaxQty, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unit.gif", vUnitPrice, vLabelAttrib ),null, null, null,null));
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Inactive.gif", viActive, vLabelAttrib ),null, null, null,null));

      TableRow row1 = new TableRow("Left",null,null);
      TableCol col6 = new TableCol("DD.MM.YYYY",null,null,null,null);
      row1.add(blank);
      row1.add(col6);
      row1.add(col6);
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
      endtime = System.currentTimeMillis();
      page.add(endtime + " " + starttime);
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

      int nAuditID=0;
      int nLangID=0;
      int nUserID=0;
      int nMsgID =-1;
 
      String errMsg = null; 
      String nTransID=null;
      String vStatus=null;
      String temp=null;
      String vIDArray[]=null;
      String mode=""; 
      String query = null;
      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();
  
      if((temp=Parse.GetValueFromString( vPID, "UserID" ))!=null && !temp.equals("null"))     
         nUserID   = Integer.parseInt(temp);
      if((temp=Parse.GetValueFromString( vPID, "LangID" ))!=null && !temp.equals("null"))     
        nLangID    = Integer.parseInt(temp);

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
            stmt = conn.createStatement();
           
            /* Insert T_ItemRate Data */
            for( int i=0; i<10; i++ )
            {
               if( !( nFk_Item_ID[i]==null || nFk_Item_ID[i].equals("") || nFk_Item_ID[i].equalsIgnoreCase("null") ) )
               {
                  int itmrt_id = Integer.parseInt(db.getNextVal("S_ItemRate"));
                  query = " INSERT INTO T_ItemRate( ItemRate_ID, Fk_Item_ID, From_Dt, "+
                          "             To_Dt, MinQty, MaxQty, UnitPrice, InActive, "+
                          "             Modifier, Change_Dt ) "+
                          " VALUES( "+itmrt_id+","+nFk_Item_ID[i]+",'"+IngDate.strToDate(dFromDt[i])+"','"+IngDate.strToDate(dToDt[i])+"',"+nMinQty[i]+","+nMaxQty[i]+","+nUnitPrice[i]+","+Parse.GetSubString(nInActive[0],"~",i)+",'"+usr+"','"+dt+"')";
                  stmt.executeUpdate(query);
               }
            }
            nMsgID = 3;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
          Trans.setTransID(pnTransID);
       }
       if( nMsgID <= 5 )
       {
         Script scr = new Script();  
         HtmlTag scrdata = new HtmlTag();
         scrdata.add("<!-- Start Hidding \n" +
                     " this.location.href=\"/JOrder/servlets/ItemRtFrame?pvMode=&pnItemRateID=&pvWhereClause="+vWhereClause+"\" \n"+
                     " top.show_status(\""+msg.GetMsgDesc(nMsgID,nLangID)+"\")" +     
                     "\n// End Hidding -->");
        scr.add(scrdata);
        out.println(scr);
     }
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}
