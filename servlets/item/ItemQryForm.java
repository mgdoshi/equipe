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

public class ItemQryForm extends HttpServlet
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
      String nSchemeID=null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vItemGroupQry=null;
      String vItemClassQry=null;
      String vUnitTypeQry=null;

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "ST_ITEM", nLangID, "WD_QUERY", "Item / Q " );

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Item/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vUnitTypeQry = " SELECT Attrib, Attrib_Desc "+
                     " FROM   T_Domain "+
                     " WHERE  Domain = 'UNITTYPE'"+
                     " AND    Fk_Lang_ID = "+ nLangID +
                     " ORDER  BY Sequence_Nr";

      vItemGroupQry = "SELECT ItemGroup_ID, ItemGroup_Name FROM T_ItemGroup ";
      vItemClassQry = "SELECT ItemClass_ID, ItemClass_Name FROM T_ItemClass ";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ItemQryForm","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add( new NL(3) );

      Table tab = new Table("1","center","Border=\"0\" width=\"63%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_DM_UNITTYPE", "Unit Type" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"untype.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"20%\"");
      HtmlTag sel1 =  util.createList( nUserID, "ST_ITEM", "cDM_UnitType", "Q", vUnitTypeQry, null, null, vListAttrib);
      TableCol col1 = new TableCol( sel1 ,null,null,null,"WIDTH=\"25%\"");
      row.add(col) ;
      row.add(col1);
      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_FK_ITEMGROUP_ID", "Item Group" );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"itmgrp.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel =  util.createList( nUserID, "ST_ITEM", "nFk_ItemGroup_ID", "Q", vItemGroupQry, null, null, vListAttrib);
      TableCol col3 = new TableCol( sel, null, null, null,null); 
      row1.add(col2);
      row1.add(col3);
      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_FK_ITEMCLASS_ID", "Item Class" );
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"itmcls.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      HtmlTag sel2 =  util.createList( nUserID, "ST_ITEM", "nFk_ItemClass_ID", "Q", vItemClassQry, null, null, vListAttrib);
      TableCol col5 = new TableCol( sel2, null, null, null,null); 
      row2.add(col4);
      row2.add(col5);
      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_NAME", "Item Name" );
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"itmname.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "ST_ITEM", "vItem_Name", "Q", "20", "30", "%", "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib), null, null, null,null); 
      row3.add(col6);
      row3.add(col7);
      TableRow row4 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_DESC", "Description" );
      TableCol col8 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"itmdesc.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      TableCol col9 = new TableCol(util.createTextItem( nUserID, "ST_ITEM", "vItem_Desc", "Q", "30", "100", "%", null, vTextAttrib), null, null, null,null);
      row4.add(col8);
      row4.add(col9);
      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row4);
      form.add(tab);
      form.add( new FormHidden("vAction", null, null) );
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
      String vWhereClause = "";
      if(vPID==null)
      {
        out.println(WebUtil.IllegalEntry());
        return;
      }

      String nLangID=null;
      String nUserID=null;
      String mode=""; 
      String vItemName = request.getParameter("vItem_Name");
      String vItemDesc = request.getParameter("vItem_Desc");
      String nItemGroupID = request.getParameter("nFk_ItemGroup_ID");
      String nItemClassID = request.getParameter("nFk_ItemClass_ID");
      String cDMUnitType = request.getParameter("cDM_UnitType");
      String vAction = request.getParameter("vAction"); 

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      vWhereClause = "WHERE itm.Item_ID = uit.FK_Item_ID ";
      if( !( vItemName==null || vItemName.equals("") || vItemName.equalsIgnoreCase("null") || vItemName.equalsIgnoreCase("%")) )
        vWhereClause = vWhereClause + " AND itm.Item_Name LIKE '"+ vItemName +"'";
      if( !( vItemDesc==null || vItemDesc.equals("") || vItemDesc.equalsIgnoreCase("null") || vItemDesc.equalsIgnoreCase("%") ) )
        vWhereClause = vWhereClause + " AND itm.Item_Desc LIKE '"+ vItemDesc +"'";
      if( !( nItemGroupID==null || nItemGroupID.equals("") || nItemGroupID.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND itm.Fk_ItemGroup_ID = "+ nItemGroupID;
      if( !( nItemClassID==null || nItemClassID.equals("") || nItemClassID.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND itm.Fk_ItemClass_ID = "+ nItemClassID;
      if( !( cDMUnitType==null || cDMUnitType.equals("") || cDMUnitType.equalsIgnoreCase("null") ) )
        vWhereClause = vWhereClause + " AND itm.DM_UnitType = '"+ cDMUnitType + "'";
 
     vWhereClause = vWhereClause + " AND uit.Fk_User_ID = " + nUserID;
      vWhereClause = URLEncoder.encode( vWhereClause );
      if( vAction!=null && vAction.equalsIgnoreCase("Query"))
        mode="N";
      else
        mode="I";
      response.sendRedirect("/JOrder/servlets/ItemFrame?pvMode="+mode+"&pnItemID=&pvWhereClause="+vWhereClause );
    }
}

