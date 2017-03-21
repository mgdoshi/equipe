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

public class OrdManDtlsEntry extends HttpServlet
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
      String rOrdDtls[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      int nColCount= 0;

      String pvMode = request.getParameter("pvMode");
      String pnOrderID = request.getParameter("pnOrderID");
      String pnOrderDtlsID = request.getParameter("pnOrderDtlsID");
      String vTemplName = request.getParameter("vTemplName");
      String Browser = request.getParameter("Browser");
      Object avItemCapt[] = new Object[13];
      HtmlTag avItemTag[] = new HtmlTag[13];

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      String vUnitTypeQry = " SELECT Attrib, Attrib_Desc "+
                            " FROM   T_Domain " +
                            " WHERE  Domain = 'UNITTYPE' "+
                            " AND    Fk_Lang_ID = " + nLangID +
                            " ORDER  BY Sequence_Nr";

      String vItemQry = " SELECT itm.Item_ID, itm.Item_Name || '-' || itm.Item_Desc "+
                        " FROM   T_Item itm, T_UserItem uit   "+
                        " WHERE  uit.Fk_Item_ID = itm.Item_ID "+
                        " AND    uit.Fk_User_ID = "+ nUserID +
                        " AND    itm.InActive = '1' "+
                        " ORDER BY itm.Item_Name ";
  
      String vItmPackQry = " SELECT ItemPack_ID, ItemPack_Name ||'-' || ItemPack_Desc "+
                           " FROM T_ItemPack ";

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "WD_FORM_INSERT", "Order Details / I");
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "WD_FORM_UPDATE", "Order Details / U");

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_OrdDtls/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if( pnOrderDtlsID!=null && !pnOrderDtlsID.equals("") && !pnOrderDtlsID.equalsIgnoreCase("null"))
      { 
        rOrdDtls = db.getRecord( pnOrderDtlsID, "OrderDtls" );
      }  
      else
        rOrdDtls = new String[25];

   /* Note : Be careful while handelling the following array, any small mistake may 
            mislead in creating a dynamic custom field table. */

      avItemCapt[0]  = util.GetBoilerPlate( vImgOption, vImagePath+"num1.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_NUM1", "Cust Num1" ), vLabelAttrib );
      avItemCapt[1]  = util.GetBoilerPlate( vImgOption, vImagePath+"text1.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_TEXT1", "Cust Text1" ), vLabelAttrib );
      avItemCapt[2]  = util.GetBoilerPlate( vImgOption, vImagePath+"num2.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_NUM2", "Cust Num2" ), vLabelAttrib );
      avItemCapt[3]  = util.GetBoilerPlate( vImgOption, vImagePath+"text2.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_TEXT2", "Cust Text2" ), vLabelAttrib );
      avItemCapt[4]  = util.GetBoilerPlate( vImgOption, vImagePath+"num3.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_NUM3", "Cust Num3" ), vLabelAttrib );
      avItemCapt[5]  = util.GetBoilerPlate( vImgOption, vImagePath+"text3.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_TEXT3", "Cust Text3" ), vLabelAttrib );
      avItemCapt[6]  = util.GetBoilerPlate( vImgOption, vImagePath+"num4.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_NUM4", "Cust Num4" ), vLabelAttrib );
      avItemCapt[7]  = util.GetBoilerPlate( vImgOption, vImagePath+"text4.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_TEXT4", "Cust Text4" ), vLabelAttrib );
      avItemCapt[8]  = util.GetBoilerPlate( vImgOption, vImagePath+"num5.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_NUM5", "Cust Num5" ), vLabelAttrib );
      avItemCapt[9] = util.GetBoilerPlate( vImgOption, vImagePath+"text5.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_TEXT5", "Cust Text5" ), vLabelAttrib );
      avItemCapt[10] = util.GetBoilerPlate( vImgOption, vImagePath+"rem1.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_REM1", "Cust Remarks1" ), vLabelAttrib );
      avItemCapt[11] = util.GetBoilerPlate( vImgOption, vImagePath+"rem2.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_REM2", "Cust Remarks2" ), vLabelAttrib );
      avItemCapt[12] = util.GetBoilerPlate( vImgOption, vImagePath+"rem3.gif", cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_REM3", "Cust Remarks3" ), vLabelAttrib );
 
      avItemTag[0]  = util.createTextItem( nUserID, "TR_ORDDTLS", "nNum1", pvMode, "10", "40", rOrdDtls[13], "onBlur=\"top.check_num(this)\"", vTextAttrib );
      avItemTag[1]  = util.createTextItem( nUserID, "TR_ORDDTLS", "vText1", pvMode, "20", "100", rOrdDtls[8], null, vTextAttrib );
      avItemTag[2]  = util.createTextItem( nUserID, "TR_ORDDTLS", "nNum2", pvMode, "10", "40", rOrdDtls[14], "onBlur=\"top.check_num(this)\"", vTextAttrib );
      avItemTag[3]  = util.createTextItem( nUserID, "TR_ORDDTLS", "vText2", pvMode, "20", "100", rOrdDtls[9], null, vTextAttrib );
      avItemTag[4]  = util.createTextItem( nUserID, "TR_ORDDTLS", "nNum3", pvMode, "10", "40", rOrdDtls[15], "onBlur=\"top.check_num(this)\"", vTextAttrib );
      avItemTag[5]  = util.createTextItem( nUserID, "TR_ORDDTLS", "vText3", pvMode, "20", "100", rOrdDtls[10], null, vTextAttrib );
      avItemTag[6]  = util.createTextItem( nUserID, "TR_ORDDTLS", "nNum4", pvMode, "10", "40", rOrdDtls[16], "onBlur=\"top.check_num(this)\"", vTextAttrib );
      avItemTag[7]  = util.createTextItem( nUserID, "TR_ORDDTLS", "vText4", pvMode, "20", "100", rOrdDtls[11], null, vTextAttrib );
      avItemTag[8]  = util.createTextItem( nUserID, "TR_ORDDTLS", "nNum5", pvMode, "10", "40", rOrdDtls[17], "onBlur=\"top.check_num(this)\"", vTextAttrib );
      avItemTag[9]  = util.createTextItem( nUserID, "TR_ORDDTLS", "vText5", pvMode, "20", "100", rOrdDtls[12], null, vTextAttrib );
      avItemTag[10] = util.createTextItem( nUserID, "TR_ORDDTLS", "vRemarks1", pvMode, "50", "500", rOrdDtls[18], null, vTextAttrib );
      avItemTag[11] = util.createTextItem( nUserID, "TR_ORDDTLS", "vRemarks2", pvMode, "50", "500", rOrdDtls[19], null, vTextAttrib );
      avItemTag[12] = util.createTextItem( nUserID, "TR_ORDDTLS", "vRemarks3", pvMode, "50", "500", rOrdDtls[20], null, vTextAttrib );

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/OrdManDtlsEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add( new FormHidden("pnTransID", nTransID, null ) );
      form.add( new FormHidden("pnOrderID", pnOrderID, null ) );
      form.add( new FormHidden("pnOrderDtlsID", pnOrderDtlsID, null ) );
      form.add( new FormHidden("vTemplName", vTemplName, null ) );
      form.add( new FormHidden("Browser", Browser, null ) );
      form.add( new FormHidden("vAction", null, null ) );
      form.add(new NL(1));

      Table tab = new Table("1","center","Border=\"0\" width=\"90%\" COLS=4");
 
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_POS_NR", "Pos No" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"posnr.gif", vBPLate, vLabelAttrib ),null, null, null, "width=\"17%\"");
      TableCol col1 = new TableCol( util.createTextItem( nUserID, "TR_ORDDTLS", "nPos_No", pvMode, "5", "20", rOrdDtls[2], "onBlur=\"top.check_num(this)\"", vTextAttrib ), null, null, "3", null );
      col.add( WebUtil.NotNull );
      row.add(col);
      row.add(col1);

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_FK_ITEM_ID", "Item" );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"item.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel =  util.createList( nUserID, "TR_ORDDTLS", "nFk_Item_ID", pvMode, vItemQry, rOrdDtls[3], "onChange=\"parent.show_UnitType( this.options[this.selectedIndex].value )\"", vListAttrib);
      TableCol col3 = new TableCol( sel, null, null, "3", null );
      col2.add( WebUtil.NotNull );
      row1.add(col2);
      row1.add(col3);
  
      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_FK_ITEMPACK_ID", "Item Pack" );
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"itmpak.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel1 =  util.createList( nUserID, "TR_ORDDTLS", "nFk_ItemPack_ID", pvMode, vItmPackQry, rOrdDtls[4], null, vListAttrib);
      TableCol col5 = new TableCol( sel1, null, null, "3", null );
      col4.add( WebUtil.NotNull );
      row2.add(col4);
      row2.add(col5);

      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_UNITPRICE", "Unit Price" );
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unitprice.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      TableCol col7 = new TableCol( util.createTextItem( nUserID, "TR_ORDDTLS", "nUnitPrice", pvMode, "10", "40", rOrdDtls[7], "onBlur=\"top.check_num(this)\"", vTextAttrib ), null, null, "3", null );
      col6.add( WebUtil.NotNull );
      row3.add(col6);
      row3.add(col7);

      TableRow row4 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_QUANTITY", "Quantity" );
      TableCol col8 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"quant.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      TableCol col9 = new TableCol( util.createTextItem( nUserID, "TR_ORDDTLS", "nQuantity", pvMode, "10", "40", rOrdDtls[5], "onBlur=\"top.check_num(this)\"", vTextAttrib ), null, null, null, null );
      col8.add( WebUtil.NotNull );
      vBPLate = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_DM_UNITTYPE", "Unit Type" );
      TableCol col10 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unittype.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      col10.add( WebUtil.NotNull );
      TableCol col11 = new TableCol( util.createTextItem( nUserID, "TR_ORDDTLS", "vUnitType", pvMode, "10", "40", Domain.getDomainDescFrmAttrib("UNITTYPE", rOrdDtls[6], nLangID), "onFocus=\"document.forms[0].nNum1.focus()\"", vTextAttrib ), null, null, null, null );
      col11.add( new FormHidden("cDM_UnitType", rOrdDtls[6], null ) );
      row4.add(col8);
      row4.add(col9);
      row4.add(col10);
      row4.add(col11);

      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row4);

      TableRow row5 = new TableRow("Left",null,null);
      for( int i=0; i<10; i++)
       {
         if( (avItemTag[i].toString()).indexOf( "<FONT" )  >= 0 )
         {
            TableCol col12 = new TableCol( avItemCapt[i], null, null, null,null);
            TableCol col13 = new TableCol( avItemTag[i], null, null, null,null);
            row5.add( col12 );
            row5.add( col13 );
            nColCount = nColCount + 2;
            if( nColCount == 4 )
            {
              tab.add(row5);
              row5 = null;
              row5 = new TableRow("Left",null,null);
              nColCount = 0;
            } 
         }
         else 
            tab.add( avItemTag[i] );
       }

      for( int i=10; i<13; i++)
      {
         if( (avItemTag[i].toString()).indexOf( "<FONT" )  >= 0 )
         {
            TableRow row6 = new TableRow("Left",null,null);
            TableCol col12 = new TableCol( avItemCapt[i], null, null, null,null);
            TableCol col13 = new TableCol( avItemTag[i], null, null, "3", null);
            row6.add( col12 );
            row6.add( col13 );
            tab.add(row6);
         }
         else
            tab.add( avItemTag[i] );
      }
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
      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();
  
      nUserID = Parse.GetValueFromString( vPID, "UserID" );
      nLangID = Parse.GetValueFromString( vPID, "LangID" );

      String pnTransID = request.getParameter("pnTransID");
      String pnOrderID = request.getParameter("pnOrderID");
      String pnOrderDtlsID = request.getParameter("pnOrderDtlsID");
      String vTemplName = request.getParameter("vTemplName");
      String Browser = request.getParameter("Browser");
      String nPos_No = request.getParameter("nPos_No");
      String nFk_Item_ID = request.getParameter("nFk_Item_ID");
      String nFk_ItemPack_ID = request.getParameter("nFk_ItemPack_ID");
      String nUnitPrice = request.getParameter("nUnitPrice");
      String nQuantity = request.getParameter("nQuantity");
      String cDM_UnitType = request.getParameter("cDM_UnitType");
      String nNum1 = request.getParameter("nNum1");
      String nNum2 = request.getParameter("nNum2");
      String nNum3 = request.getParameter("nNum3");
      String nNum4 = request.getParameter("nNum4");
      String nNum5 = request.getParameter("nNum5");
      String vText1 = request.getParameter("vText1");
      String vText2 = request.getParameter("vText2");
      String vText3 = request.getParameter("vText3");
      String vText4 = request.getParameter("vText4");
      String vText5 = request.getParameter("vText5");
      String vRemarks1 = request.getParameter("vRemarks1");
      String vRemarks2 = request.getParameter("vRemarks2");
      String vRemarks3 = request.getParameter("vRemarks3");
      String vAction  = request.getParameter("vAction"); 

      String usr = db.getName( nUserID, "User" );      
      vStatus    = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction.equalsIgnoreCase("Insert") )
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            int orderdtls_id = Integer.parseInt(db.getNextVal("S_OrderDtls"));
            query = " INSERT INTO T_OrderDtls( OrderDtls_ID, Fk_Order_ID, Pos_Nr, "+
                    "            Fk_ItemPack_ID, Fk_Item_ID, Quantity, DM_UnitType, "+
                    "            UnitPrice, Text1, Text2, Text3, Text4, Text5, "+
                    "            Num1, Num2, Num3, Num4, Num5, Remarks1, Remarks2, "+
                    "            Remarks3, Modifier, Change_Dt ) "+
                    " VALUES( "+ orderdtls_id+ ", "+ pnOrderID +", " + nPos_No +", "+
                    " "+ nFk_ItemPack_ID + ", "+ nFk_Item_ID +", "+ nQuantity +", "+
	  	    " '"+ cDM_UnitType + "', "+ nUnitPrice + ", "+ val.IsNull(vText1) +", "+
     		    " "+ val.IsNull(vText2) + ", "+ val.IsNull(vText3) +", "+ val.IsNull(vText4) +", "+
		    " "+ val.IsNull(vText5) +", "+ val.IsNull(nNum1)  + ", "+ val.IsNull(nNum2) +", "+
		    " "+ val.IsNull(nNum3) +", "+ val.IsNull(nNum4) +", "+ val.IsNull(nNum5) +", "+
		    " "+ val.IsNull(vRemarks1) +", "+ val.IsNull(vRemarks2) +", "+
                    " "+ val.IsNull(vRemarks3) +",'"+ usr + "','"+ dt +"' )";
            stmt.executeUpdate(query);
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
        else if( vAction.equalsIgnoreCase("Update"))
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            query = " UPDATE T_OrderDtls "+
                    " SET Pos_Nr = "+nPos_No+", Fk_ItemPack_ID = "+nFk_ItemPack_ID+","+
                    "     Fk_Item_ID = "+nFk_Item_ID+", Quantity = "+nQuantity+ ","+
                    "     DM_UnitType = '"+cDM_UnitType+"', UnitPrice ="+nUnitPrice+ ","+
                    "     Text1 = "+val.IsNull(vText1)+", Text2 ="+val.IsNull(vText2)+","+
                    "     Text3 = "+val.IsNull(vText3)+", Text4 ="+val.IsNull(vText4)+","+
                    "     Text5 = "+val.IsNull(vText5)+", Num1 = "+val.IsNull(nNum1)+","+
                    "     Num2 = "+val.IsNull(nNum2)+", Num3 = "+val.IsNull(nNum3)+","+
                    "     Num4 = "+val.IsNull(nNum4)+", Num5 ="+val.IsNull(nNum5) +","+
                    "     Remarks1 ="+val.IsNull(vRemarks1)+", Remarks2 ="+val.IsNull(vRemarks2)+","+
                    "     Remarks3 ="+val.IsNull(vRemarks3)+","+
                    "     Modifier ='"+ usr + "', Change_Dt = '"+dt+"'"+
                    " WHERE OrderDtls_ID ="+pnOrderDtlsID+" AND Fk_Order_ID="+pnOrderID;
            stmt.executeUpdate(query);
            nMsgID = 5;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=8;}
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
      }
     if( nMsgID <= 5 )
       response.sendRedirect("/JOrder/servlets/OrdManFrame?pvMode=D&pnOrderID="+pnOrderID+"&vTemplName="+vTemplName+"&Browser="+Browser+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}