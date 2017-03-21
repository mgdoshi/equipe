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

public class ItemEntry extends HttpServlet
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
      String rItem[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vStatus="CHECKED";
      String vUnitTypeQry = null;
      String vItemGroupQry = null;
      String vItemClassQry = null;
      String vCostCentreQry = null;
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

      String pvMode = request.getParameter("pvMode");
      String pnItemID = request.getParameter("pnItemID");
      String pvWhereClause = request.getParameter("pvWhereClause");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "ST_ITEM", nLangID, "WD_FORM_INSERT","Item / I");
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "ST_ITEM", nLangID, "WD_FORM_UPDATE","Item / U");

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Item/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vUnitTypeQry = " SELECT Attrib, Attrib_Desc " +
                     " FROM   T_Domain " +
                     " WHERE  Domain = 'UNITTYPE' "+
                     " AND    Fk_Lang_ID = " + nLangID +
                     " ORDER  BY Sequence_Nr";
      vItemGroupQry = "SELECT ItemGroup_ID, ItemGroup_Name FROM T_ItemGroup ";
      vItemClassQry = "SELECT ItemClass_ID, ItemClass_Name FROM T_ItemClass ";
      vCostCentreQry = "SELECT CostCentre_ID, CostCentre_Name FROM T_CostCentre ";

      if(pnItemID!=null && !pnItemID.equals("") && !pnItemID.equalsIgnoreCase("null"))
      { 
        rItem = db.getRecord(pnItemID,"Item");
      }  
      else
        rItem = new String[17];

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ItemEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add( new FormHidden("pnTransID", nTransID, null ) );
      form.add( new FormHidden("pnItemID", pnItemID, null ) );
      form.add( new FormHidden("pvWhereClause", pvWhereClause, null ) );
      form.add( new FormHidden("vAssignAllCln", null, null ) );
      form.add( new FormHidden("vAssignAllUser", null, null ) );
      form.add( new FormHidden("vAction", null, null ) );
      form.add( new FormHidden("nInActive", null, null ) );
      form.add( new NL(1) );

      Table tab = new Table("1","center","Border=\"0\" width=\"85%\" COLS=4");
 
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_NAME", "Item Name" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.gif", vBPLate, vLabelAttrib ),null, null, null,null); 
      col.add(WebUtil.NotNull);
      TableCol col1 = new TableCol(util.createTextItem( nUserID, "ST_ITEM", "vItem_Name", pvMode, "15", "30", rItem[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,"WIDTH=\"28%\"");
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_INACTIVE", "Available" );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Active.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"20%\""); 
      if( rItem[14]!=null  && rItem[14].equals("1") )
         vStatus = "CHECKED";
      else if( rItem[14]!=null  && rItem[14].equals("0") )
         vStatus =  null;
      TableCol col3 = new TableCol( new FormCheckBox( "nInActive", null, vStatus, null), null,null,null,"WIDTH=\"28%\"");
      row.add(col);
      row.add(col1);
      row.add(col2);
      row.add(col3);

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_DESC","Description");
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col5 = new TableCol(util.createTextItem( nUserID, "ST_ITEM", "vItem_Desc", pvMode, "40", "100", rItem[2], null, vTextAttrib),null,null,"3",null);
      row1.add(col4) ;
      row1.add(col5) ;

      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_REM1", "Remark 1" );
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Rem1.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "ST_ITEM", "vItem_Rem1", pvMode, "40", "100", rItem[3], null, vTextAttrib),null,null,"3",null);
      row2.add(col6);
      row2.add(col7);

      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_REM2", "Remark 2" );
      TableCol col8 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Rem2.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col9 = new TableCol(util.createTextItem( nUserID, "ST_ITEM", "vItem_Rem2", pvMode, "40", "100", rItem[4], null, vTextAttrib),null,null,"3",null);
      row3.add(col8);
      row3.add(col9);

      TableRow row4 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_REM3", "Remark 3" );
      TableCol col10 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Rem3.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col11 = new TableCol(util.createTextItem( nUserID, "ST_ITEM", "vItem_Rem3", pvMode, "40", "100", rItem[5], null, vTextAttrib),null,null,"3",null);
      row4.add(col10);
      row4.add(col11);

      TableRow row5 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_REM4", "Remark 4" );
      TableCol col12 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Rem4.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col13 = new TableCol(util.createTextItem( nUserID, "ST_ITEM", "vItem_Rem4", pvMode, "40", "100", rItem[6], null, vTextAttrib),null,null,"3",null);
      row5.add(col12);
      row5.add(col13);

      TableRow row6 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_MINORDQTY", "Min Ord Qty" );
      TableCol col14 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"MinQty.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col15 = new TableCol(util.createTextItem( nUserID, "ST_ITEM", "nMinOrdQty", pvMode, "10", "100", rItem[11], "onBlur=\"top.check_num(this)\"", vTextAttrib), null,null,null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_MAXORDQTY", "Max Ord Qty" );
      TableCol col16 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"MaxQty.gif", vBPLate, vLabelAttrib ),null, null, null, null);
      TableCol col17 = new TableCol(util.createTextItem( nUserID, "ST_ITEM", "nMaxOrdQty", pvMode, "10", "100", rItem[12], "onBlur=\"top.check_num(this)\"", vTextAttrib), null,null,null,null);
      row6.add(col14);
      row6.add(col15);
      row6.add(col16);
      row6.add(col17);

      TableRow row7 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_FK_ITEMCLASS_ID", "Item Class" );
      TableCol col18 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"itmcls.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel =  util.createList( nUserID, "ST_ITEM", "nFk_ItemClass_ID", pvMode, vItemClassQry, rItem[7], null, vListAttrib);
      TableCol col19 = new TableCol( sel, null, null, null,null); 
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_FK_ITEMGROUP_ID", "Item Group" );
      TableCol col20 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"itmgrp.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel1 =  util.createList( nUserID, "ST_ITEM", "nFk_ItemGroup_ID", pvMode, vItemGroupQry, rItem[8], null, vListAttrib);
      TableCol col21 = new TableCol( sel1, null, null, null,null); 
      row7.add(col18);
      row7.add(col19);
      row7.add(col20);
      row7.add(col21);

      TableRow row8 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_FK_COSTCENTRE_ID", "CostCentre Name" );
      TableCol col22 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Cost.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel2 =  util.createList( nUserID, "ST_ITEM", "nFk_CostCentre_ID", pvMode, vCostCentreQry, rItem[9], null, vListAttrib);
      TableCol col23 = new TableCol( sel2, null, null, null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_DM_UNITTYPE", "Unit Type" );
      TableCol col24 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Type.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel3 =  util.createList( nUserID, "ST_ITEM", "cDM_UnitType", pvMode, vUnitTypeQry, rItem[10], null, vListAttrib);
      TableCol col25 = new TableCol( sel3, null, null, null,null); 
      row8.add(col22);
      row8.add(col23);
      row8.add(col24);
      row8.add(col25);

      TableRow row9 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_SPLINSTRUCT", "Spl Instruct" );
      TableCol col26 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Spl.gif", vBPLate, vLabelAttrib ),null, null, null,"VALIGN=\"Top\"");
      TableCol col27 = new TableCol(util.createTextAreaItem( nUserID, "ST_ITEM", "vSplInstruct", pvMode, "2", "39", rItem[13], null, vTextAttrib ), null, null, "3", null);
      row9.add(col26);
      row9.add(col27);
     
      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row4);
      tab.add(row5);
      tab.add(row6);
      tab.add(row7);
      tab.add(row8);
      tab.add(row9);

      if( pvMode!=null && pvMode.equalsIgnoreCase("I") )
      {
        TableRow row10 = new TableRow("Left",null,null);
        vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ASSIGN_CLIENTS", "Assign this Item to all Clients" );
        TableCol col28 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"AssCln.gif", vBPLate, vLabelAttrib ),null, null, null, null);
        TableCol col29 = new TableCol(new FormCheckBox( "vAssignAllCln", null, null, null ), null, null, null, null);
        vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ASSIGN_USERS", "Assign this Item to all Users" );
        TableCol col30 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"AssUsr.gif", vBPLate, vLabelAttrib ),null, null, null, null);
        TableCol col31 = new TableCol(new FormCheckBox( "vAssignAllUser", null, null, null ), null, null, null, null);
        row10.add(col28);
        row10.add(col29);
        row10.add(col30);
        row10.add(col31);
        tab.add(row10);
      }
      else
      {
        form.add( new FormHidden("vAssignAllCln", null, null ) );
        form.add( new FormHidden("vAssignAllUser", null, null ) );
      }      

/** T_ItemRate Data **/

      for( int i=0; i<5; i++ )
      {
        form.add( new FormHidden( "dFrom_Dt",null, null) );
        form.add( new FormHidden( "dTo_Dt", null, null) );
        form.add( new FormHidden( "nMinQty", null, null) );
        form.add( new FormHidden( "nMaxQty", null, null) );
        form.add( new FormHidden( "nUnitPrice", null, null) );
        form.add( new FormHidden( "nActive", null, null) );
      }

/** T_ClientItem Data **/

      form.add( new FormHidden( "nFk_Client_ID", null, null) );
      form.add( new FormHidden( "nDelete", null, null) );
      form.add( new FormHidden( "nFk_User_ID", null, null) );
      form.add( new FormHidden( "nDel", null, null) );

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
 
      String errMsg = null; 
      String vStatus=null;
      String vIDArray[]=null;
      String query = null;
      String query1 = null;
      String nLangID=null;
      String nUserID=null;

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      Statement stmt = null;
      PreparedStatement pstmt = null;
      Connection conn = null;
      ResultSet rs = null;

      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      IngDate dt = new IngDate();
  
      String pnTransID = request.getParameter("pnTransID");
      String pnItemID = request.getParameter("pnItemID");
      String vName    = Parse.formatStr( request.getParameter("vItem_Name") );
      String vDesc    = Parse.formatStr( request.getParameter("vItem_Desc") );
      String vItmRem1 = Parse.formatStr( request.getParameter("vItem_Rem1") );
      String vItmRem2 = Parse.formatStr( request.getParameter("vItem_Rem2") );
      String vItmRem3 = Parse.formatStr( request.getParameter("vItem_Rem3") );
      String vItmRem4 = Parse.formatStr( request.getParameter("vItem_Rem4") );
      String nMinOrdQty = request.getParameter("nMinOrdQty");
      String nMaxOrdQty = request.getParameter("nMaxOrdQty");
      String nItmClsID = request.getParameter("nFk_ItemClass_ID");
      String nItmGrpID = request.getParameter("nFk_ItemGroup_ID");
      String nCostID = request.getParameter("nFk_CostCentre_ID");
      String cDMUnitType = request.getParameter("cDM_UnitType");
      String vSplInstruct = Parse.formatStr( request.getParameter("vSplInstruct") );
      String nInActive[] = request.getParameterValues("nInActive");
      String dFromDt[] = request.getParameterValues("dFrom_Dt");
      String dToDt[] = request.getParameterValues("dTo_Dt");
      String nMinQty[] = request.getParameterValues("nMinQty");
      String nMaxQty[] = request.getParameterValues("nMaxQty");
      String nUnitPrice[] = request.getParameterValues("nUnitPrice");
      String nActive[] = request.getParameterValues("nActive");
      String nClientID = request.getParameter("nFk_Client_ID");
      String nDelete = request.getParameter("nDelete");
      String nUsrID = request.getParameter("nFk_User_ID");
      String nDel = request.getParameter("nDel");
      String vAssCln[] = request.getParameterValues("vAssignAllCln");
      String vAssUsr[] = request.getParameterValues("vAssignAllUser");
      String vWhereClause = URLEncoder.encode( request.getParameter("pvWhereClause") );
      String vAction = request.getParameter("vAction"); 

      String usr = db.getName( nUserID, "User" );
      vStatus    = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction.equalsIgnoreCase("Insert") || vAction.equalsIgnoreCase("SaveInsert") )
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            /* Insert T_Item Data */
            int item_id = Integer.parseInt(db.getNextVal("S_Item"));
            query = " INSERT INTO T_Item ( Item_ID, Item_Name, Item_Desc, Item_Rem1, Item_Rem2, "+
                    "    Item_Rem3, Item_Rem4, Fk_ItemClass_ID, Fk_ItemGroup_ID, Fk_CostCentre_ID, "+
                    "    DM_UnitType, MinOrdQty, MaxOrdQty, InActive, SplInstruct, Modifier, Change_Dt ) "+
                    " VALUES ("+item_id+","+val.IsNull(vName)+","+val.IsNull(vDesc)+"," + val.IsNull(vItmRem1)+
                    ","+val.IsNull(vItmRem2)+","+val.IsNull(vItmRem3)+","+val.IsNull(vItmRem4)+
                    ","+val.IsNull(nItmClsID)+","+val.IsNull(nItmGrpID)+","+val.IsNull(nCostID)+
                    ","+val.IsNull(cDMUnitType)+","+val.IsNull(nMinOrdQty)+","+val.IsNull(nMaxOrdQty)+
                    ",'"+nInActive[0]+"',"+val.IsNull(vSplInstruct)+",'"+usr+"', '"+dt+"')";
            stmt.executeUpdate(query);
            stmt.close();
            stmt = conn.createStatement();
            
            /* Insert T_ItemRate Data */
            for( int i=0; i<5; i++ )
            {
              if( !( nUnitPrice[i]==null || nUnitPrice[i].trim().equals("") || nUnitPrice[i].trim().equalsIgnoreCase("null") ) )
              {
                int itmrt_id = Integer.parseInt(db.getNextVal("S_ItemRate"));
                query = " INSERT INTO T_ItemRate( ItemRate_ID, Fk_Item_ID, From_Dt, "+
                        "             To_Dt, MinQty, MaxQty, UnitPrice, InActive, "+
                        "             Modifier, Change_Dt ) "+
                        " VALUES( "+itmrt_id+","+item_id+",'"+IngDate.strToDate(dFromDt[i])+"','"+IngDate.strToDate(dToDt[i])+"',"+nMinQty[i]+","+nMaxQty[i]+","+nUnitPrice[i]+",'"+nActive[i]+"','"+usr+"','"+dt+"')";
                stmt.executeUpdate(query);
              }
            }

            stmt.close();
            stmt = conn.createStatement();
          
            /* Insert T_ClientItem Data */
            if( vAssCln[0]!=null && vAssCln[0].equalsIgnoreCase("All") )
            {
               query = " Select Client_ID From T_Client";
               query1 = " INSERT INTO T_ClientItem ( ClientItem_ID, Fk_Client_ID, "+
                        "                            Fk_Item_ID, Modifier, Change_Dt ) "+
                        " VALUES ( ?, ?,"+item_id+",'"+usr+"','"+dt+"')";
               pstmt = conn.prepareStatement(query1); 
               rs = stmt.executeQuery(query);
               while( rs.next() )
               {
                  int clnitm_id = Integer.parseInt(db.getNextVal("S_ClientItem"));
                  pstmt.setInt(1,clnitm_id);
                  pstmt.setInt(2,rs.getInt(1));
                  pstmt.executeUpdate();
               }
               pstmt.close();
               stmt.close();
               rs.close();
            }
            else if( !(nClientID.trim()==null || nClientID.trim().equals("") || nClientID.trim().equalsIgnoreCase("null") ) )
            { 
               vIDArray=Parse.parse(nClientID,"~");
               query1 = " INSERT INTO T_ClientItem ( ClientItem_ID, Fk_Client_ID, "+
                        "                            Fk_Item_ID, Modifier, Change_Dt ) "+
                        " VALUES ( ?, ?,"+item_id+",'"+usr+"','"+dt+"')";
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
               stmt.close();
            }
            stmt = conn.createStatement();
            /* Insert T_UserItem Data */
            if( vAssUsr[0]!=null && vAssUsr[0].equalsIgnoreCase("All") )
            {
               query =  " Select User_ID From T_User";
               query1 = " INSERT INTO T_UserItem "+
                        " VALUES(?,"+item_id+")";
               pstmt = conn.prepareStatement(query1); 
               rs = stmt.executeQuery(query);
               while( rs.next() )
               {
                  pstmt.setInt(1,rs.getInt(1));
                  pstmt.executeUpdate();
               }
            }
            else if( !( nUsrID.trim()==null || nUsrID.trim().equals("") || nUsrID.trim().equalsIgnoreCase("null") ) )
            { 
               vIDArray=Parse.parse(nUsrID,"~");
               query1 = " INSERT INTO T_UserItem ( Fk_User_ID, Fk_Item_ID) "+
                        " VALUES ( ?, "+item_id+")";
               pstmt = conn.prepareStatement(query1);
               for(int i=0;i<vIDArray.length;i++)
               {  
                  int bid = Integer.parseInt(vIDArray[i]);
                  pstmt.setInt(1,bid);
                  pstmt.executeUpdate();
               }
            }
            nMsgID = 3;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
          finally
          {
            try
            {
              if(rs!=null)
               rs.close();
              if(pstmt!=null)
               pstmt.close();
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
            query = " UPDATE T_Item "+
                    " SET    Item_Name        = '"+vName+"',"+
                    "        Item_Desc        = "+val.IsNull(vDesc)+","+
                    "        Item_Rem1        = "+val.IsNull(vItmRem1)+","+
                    "        Item_Rem2        = "+val.IsNull(vItmRem2)+","+
                    "        Item_Rem3        = "+val.IsNull(vItmRem3)+","+
                    "        Item_Rem4        = "+val.IsNull(vItmRem4)+","+
                    "        Fk_ItemClass_ID  = "+val.IsNull(nItmClsID)+","+
                    "        Fk_ItemGroup_ID  = "+val.IsNull(nItmGrpID)+","+
                    "        Fk_CostCentre_ID = "+val.IsNull(nCostID)+","+
                    "        DM_UnitType      = "+val.IsNull(cDMUnitType)+","+
                    "        MinOrdQty        = "+val.IsNull(nMinOrdQty)+","+
                    "        MaxOrdQty        = "+val.IsNull(nMaxOrdQty)+","+
                    "        InActive         = '"+nInActive[0]+"',"+
                    "        SplInstruct      = "+val.IsNull(vSplInstruct)+","+
                    "        Modifier         = '"+usr+"',"+
                    "        Change_Dt        = '"+dt+"'"+
                    "        WHERE Item_ID = "+pnItemID;
            stmt.executeUpdate(query);

            stmt.close();
            stmt = conn.createStatement();
            /* Insert T_ItemRate Data */
            for( int i=0; i<5; i++ )
            {
               if( !( nUnitPrice[i]==null || nUnitPrice[i].trim().equals("") || nUnitPrice[i].trim().equalsIgnoreCase("null") ) )
               {
                  int itmrt_id = Integer.parseInt(db.getNextVal("S_ItemRate"));
                  query = " INSERT INTO T_ItemRate( ItemRate_ID, Fk_Item_ID, From_Dt,"+
                          " To_Dt, MinQty, MaxQty, UnitPrice, InActive,Modifier, Change_Dt ) "+
                          " VALUES( "+itmrt_id+","+pnItemID+",'"+IngDate.strToDate(dFromDt[i])+"','"+IngDate.strToDate(dToDt[i])+"',"+nMinQty[i]+","+nMaxQty[i]+","+nUnitPrice[i]+",'"+nActive[i]+"','"+usr+"','"+dt+"')";
                  stmt.executeUpdate(query);
               }
            }
            stmt.close();


            /* Delete T_ClientItem Data */
            if( !( nDelete==null || nDelete.trim().equals("") || nDelete.trim().equalsIgnoreCase("null") ) )
            { 
               vIDArray=Parse.parse(nDelete,"~");
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
            if( !(nClientID==null || nClientID.trim().equals("") || nClientID.trim().equalsIgnoreCase("null") ) )
            { 
               vIDArray=Parse.parse(nClientID,"~");
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


            /* Delete T_UserItem Data */
            if( !( nDel==null || nDel.trim().equals("") || nDel.trim().equalsIgnoreCase("null") ) )
            { 
               vIDArray=Parse.parse(nDel,"~");
               query1 = " DELETE FROM T_UserItem "+
                        " WHERE  Fk_User_ID =?"+
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

            /* Insert T_UserItem Data */
            if( !( nUsrID==null || nUsrID.trim().equals("") || nUsrID.trim().equalsIgnoreCase("null") ) )
            { 
               vIDArray=Parse.parse(nUsrID,"~");
               query1 = " INSERT INTO T_UserItem ( Fk_User_ID, Fk_Item_ID) "+
                        " VALUES ( ?, "+pnItemID+")";
               pstmt = conn.prepareStatement(query1);
               for(int i=0;i<vIDArray.length;i++)
               {  
                  int bid = Integer.parseInt(vIDArray[i]);
                  pstmt.setInt(1,bid);
                  pstmt.executeUpdate();
               }
               pstmt.close();
            }
            nMsgID = 5;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=8;}
          finally
          {
            try
            {
              if(pstmt!=null)
               pstmt.close();
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
     {
       if(vAction.equalsIgnoreCase("SaveInsert"))
         response.sendRedirect("/JOrder/servlets/ItemFrame?pvMode=I&pnItemID=&pvWhereClause="+vWhereClause+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         response.sendRedirect("/JOrder/servlets/ItemFrame?pvMode=N&pnItemID=&pvWhereClause="+vWhereClause+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     }
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

