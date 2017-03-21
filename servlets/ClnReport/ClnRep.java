import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import ingen.html.character.*;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ClnRep extends HttpServlet
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
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vClnTypeQry=null;
      String vClientQry=null;
      String vItemClassQry=null;
      String vItemGroupQry=null;
      String vCostCentreQry=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );


      DBConnect db = new DBConnect();
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      vFormType = cdata.GetConfigValue( "TR_REPORT", nLangID, "WD_QUERY","Client Wise Report / Q");                      

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Report/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );
    
      vClnTypeQry = "SELECT Attrib, Attrib_Desc FROM T_Domain WHERE Domain = 'ORDSTAT' AND Fk_Lang_ID = "+nLangID;
      vItemClassQry = "Select ItemClass_ID, ItemClass_Name From T_ItemClass";
      vItemGroupQry = "Select ItemGroup_ID, ItemGroup_Name From T_ItemGroup";
      vCostCentreQry= "Select CostCentre_ID, CostCentre_Name From T_CostCentre";

      if(Integer.parseInt(nUserID.trim())==0)
      { 
	 vClientQry = " SELECT Client_ID, Client_Name " +
		      " FROM   T_Client Order By Client_Name"; 
      }      
      else
      {
      vClientQry = " SELECT cln.Client_ID, cln.Client_Name   " +
                   " FROM   T_Client cln, T_UserClient ucl   " +
                   " WHERE  ucl.Fk_Client_ID = cln.Client_ID " +
                   " AND    ucl.Fk_User_ID = " + nUserID +
                   " Order By cln.Client_Name";
      }
			 

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ClnRep","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add( new FormHidden("vOrdStat", null, null ));
      form.add(new NL(3));

      Table tab = new Table("1","center","Border=\"0\" width=\"80%\" COLS=4");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_CLIENT_NAME","Client Name");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"clname.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"20%\"");
      col.add(WebUtil.NotNull);
      row.add(col);
      row.add(new TableCol(util.createList( nUserID, "TR_REPORT", "pnClientID","Q", vClientQry,null, null, vListAttrib), null,null,null,null));

      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_DATE_FROM","Date From");
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      row1.add(new TableCol(util.createTextItem( nUserID, "TR_REPORT", "dFrom_Date", "Q", "10", "10",null, "onBlur=\"top.check_date(this)\"", vTextAttrib), null, null, null,null));
      row1.add(new TableCol(util.createLabelItem("DD.MM.YYYY",vLabelAttrib),null,null,null,null));
			
      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_DATE_TO","Date To");
      row2.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      row2.add(new TableCol(util.createTextItem( nUserID, "TR_REPORT", "dTo_Date", "Q", "10", "10",null, "onBlur=\"top.check_date(this)\"", vTextAttrib), null, null, null,null));
      row2.add(new TableCol(util.createLabelItem("DD.MM.YYYY",vLabelAttrib),null,null,null,null));

      TableRow row3 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_REPORT","Report");
      row3.add( new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"clname.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      TableCol col3 = new TableCol(null,null,null,null,"colspan=\"3\""); 
      col3.add(new FormRadio("vYear", "Q", "CHECKED",null)); 
      col3.add(WebUtil.getBlankSpaces(4));
      col3.add(cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_QUART","Quarterly"));
      col3.add(WebUtil.getBlankSpaces(6));
      col3.add(new FormRadio("vYear", "M",null,null)); 
      col3.add(WebUtil.getBlankSpaces(4));
      col3.add(cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_MONTH","Monthly"));
      row3.add(col3);

      TableRow row5 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_ITEM_NAME", "Item Name");
      row5.add(new TableCol(util.GetBoilerPlate( vImgOption, vImagePath+"itmname.gif", vBPLate, vLabelAttrib ),null,null,null,null));
      row5.add(new TableCol(util.createTextItem( nUserID, "TR_REPORT", "vItem_Name", "Q", "10", "30", "%", "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib), null, null, null,null));
      vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_ITEM_CLASS", "Item Class" );
      row5.add(new TableCol(util.GetBoilerPlate( vImgOption, vImagePath+"itmcls.gif", vBPLate, vLabelAttrib ),null,null,null,null));
      row5.add(new TableCol(util.createList( nUserID, "TR_REPORT", "nFk_ItemClass_ID", "Q", vItemClassQry, null, null, vListAttrib ),null,null,null,null));

      TableRow row4 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_ITEM_GROUP", "Item Group");
      row4.add(new TableCol(util.GetBoilerPlate( vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null,null,null,null));
      row4.add(new TableCol(util.createList( nUserID, "TR_REPORT", "nFk_ItemGroup_ID", "Q", vItemGroupQry, null, null, vListAttrib ),null,null,null,null));
      vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_COSTCENTRE" , "CostCentre Name" );
      row4.add(new TableCol(util.GetBoilerPlate( vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null,null,null,null));
      row4.add(new TableCol(util.createList( nUserID, "TR_REPORT", "nFk_CostCentre_ID", "Q", vCostCentreQry, null, null, vListAttrib ),null,null,null,null));

      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row5);
      tab.add(row4);

      vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_ORDER_STATUS", "Order Status");
      tab.add("<TR align=\"Left\"><TD>");
      tab.add(util.GetBoilerPlate( vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ));
      tab.add("</TD>");
      try
      {
	 conn = db.GetDBConnection();
	 stmt = conn.createStatement();
	 rs   = stmt.executeQuery(vClnTypeQry);
	 int rcount=0;
	 while(rs.next())
	 {
          tab.add("<TD>");
          tab.add(new FormCheckBox("vOrdStat",rs.getString(1),null,null));
  	  tab.add(util.getBlankSpaces(4));
	  tab.add(rs.getString(2));
          tab.add("</TD>");
          if(++rcount%3==0)
  	  {
             tab.add("</TR>");
             tab.add("<TR>");
             tab.add("<TD></TD>");
 	  }
         }
	}catch(SQLException sexe){System.out.println(sexe);}
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
    	  }catch(SQLException sexe){System.out.println(sexe);}
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
      int anQtyQry[] = new int[12]; 
      String vBPLate=null; 
      String vBPLate1=null;
      String nLangID=null;
      String nUserID=null;
      String nTransID=null;
      String nSchemeID=null;
      String vLabelAttrib=null;
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );

      String pnClientID = request.getParameter("pnClientID");
      String vItem_Name = Parse.formatStr(request.getParameter("vItem_Name"));
      String nFk_ItemClass_ID = request.getParameter("nFk_ItemClass_ID");
      String nFk_ItemGroup_ID = request.getParameter("nFk_ItemGroup_ID");
      String nFk_CostCentre_ID = request.getParameter("nFk_CostCentre_ID");
      String dFrom_Date = request.getParameter("dFrom_Date");
      String dTo_Date = request.getParameter("dTo_Date");
      String vYear = request.getParameter("vYear");
      String vOrdStat[] = request.getParameterValues("vOrdStat");

      DBConnect db = new DBConnect();
      Connection conn = null;
      Statement stmt = null;
      Statement stmt1 = null;
      ResultSet rs = null;
      ResultSet rs1 = null;
      WebUtil util = new WebUtil();
      ConfigData cdata = new ConfigData(); 

      String query = " SELECT DISTINCT itm.Item_Name " +
                     " FROM  T_Order ord, T_OrderDtls ort, T_Item itm " +
                     " WHERE ord.Fk_Client_ID = "+pnClientID+" " +
                     " AND   ort.Fk_Item_ID = itm.Item_ID " +
                     " AND   ord.Order_ID = ort.Fk_Order_ID "; 
      if(!(vItem_Name==null || vItem_Name.equalsIgnoreCase("null") || vItem_Name.equalsIgnoreCase("")) ) 
        query += " AND   itm.Item_Name LIKE '"+vItem_Name+"' ";
      if(!(nFk_ItemClass_ID==null || nFk_ItemClass_ID.equalsIgnoreCase("null") || nFk_ItemClass_ID.equalsIgnoreCase("")) ) 
        query += " AND   itm.Fk_ItemClass_ID = "+nFk_ItemClass_ID+" ";
      if(!(nFk_ItemGroup_ID==null || nFk_ItemGroup_ID.equalsIgnoreCase("null") || nFk_ItemGroup_ID.equalsIgnoreCase("")) ) 
        query += " AND   itm.Fk_ItemGroup_ID = "+nFk_ItemGroup_ID+" ";
      if(!(dFrom_Date==null || dFrom_Date.equalsIgnoreCase("null") || dFrom_Date.equalsIgnoreCase("")) ) 
        query += " AND   ord.Order_DT >= '"+dFrom_Date+"' ";
      if(!(dTo_Date==null || dTo_Date.equalsIgnoreCase("null") || dTo_Date.equalsIgnoreCase("")) ) 
        query += " AND   Order_DT <= '"+dTo_Date+"' ";
      if(vOrdStat!=null && !(vOrdStat[0]==null || vOrdStat[0].equalsIgnoreCase("null") || vOrdStat[0].equalsIgnoreCase("")) )
      {
         String vIDArray[]=Parse.parse(vOrdStat[0],"~");
         if(vIDArray.length>0)
           query += " AND (";  
         for(int i=0;i<vIDArray.length;i++)
         {  
           query += " ord.DM_OrdStat = '"+vIDArray[i]+"' ";
           if(i+1!=vIDArray.length)
             query += " OR ";    
         }
         if(vIDArray.length>0)
           query += " )";  
      } 
      query += " ORDER BY Item_Name ";
      
      String query1 = " SELECT ord.Order_Dt, ort.Quantity " + 
                     " FROM   T_Order ord, T_OrderDtls ort " +
                     " WHERE    ord.Fk_Client_ID = "+pnClientID+" " +
                     " AND    ort.Fk_Order_ID = ord.Order_ID ";
      if(!(dFrom_Date==null || dFrom_Date.equalsIgnoreCase("null") || dFrom_Date.equalsIgnoreCase("")) ) 
        query1 += " AND   ord.Order_DT >= '"+dFrom_Date+"' ";
      if(!(dTo_Date==null || dTo_Date.equalsIgnoreCase("null") || dTo_Date.equalsIgnoreCase("")) ) 
        query1 += " AND   ord.Order_DT <= '"+dTo_Date+"' ";

     Page page = new Page();
     Body body = new Body("/ordimg/BACKGR2.GIF",null); 
     body.add(new NL());
     Center cen = new Center();
     vBPLate = cdata.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_CLIENTWISE","Report of Client " );
     cen.setFormat(new Bold()); 
     cen.add(vBPLate + WebUtil.getBlankSpaces(4)+ db.getName(pnClientID,"Client"));
     body.add(cen);  
     body.add(new NL());

  
      if(vYear.equals("Q"))
      {
        Table tab = new Table("1","center","Border=\"0\" width=\"80%\" BGCOLOR=\"#FFFFCA\" COLS=6");
        TableRow row = new TableRow("Left",null,"BGCOLOR=\"#666666\"");
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_QUART","Quartely");
        vBPLate1 = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_ITEM","Item");
        TableCol col = new TableCol(null,null, null, null,"WIDTH=\"15%\"");
        col.add(WebUtil.getBlankSpaces(4));
        col.add(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib));
        col.add(WebUtil.getBlankSpaces(4));
        col.add(util.createLabelItem(vBPLate1, "COLOR=\"white\" " + vLabelAttrib));
        row.add(col); 

        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_UNIT","Unit");
        TableCol col1 = new TableCol(null,"Center", null, null,"WIDTH=\"8%\"");
        col1.add(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib));
        row.add(col1); 

        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_QUART1","JAN-MAR");
        TableCol col2 = new TableCol(null,"Center", null, null,"WIDTH=\"8%\"");
        col2.add(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib));
        row.add(col2); 

        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_QUART2","APR-JUN");
        TableCol col3 = new TableCol(null,"Center", null, null,"WIDTH=\"8%\"");
        col3.add(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib));
        row.add(col3); 


        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_QUART3","JUL-SEP");
        TableCol col4 = new TableCol(null,"Center", null, null,"WIDTH=\"8%\"");
        col4.add(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib));
        row.add(col4); 

        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_QUART4","OCT-DEC");
        TableCol col5 = new TableCol(null,"Center", null, null,"WIDTH=\"8%\"");
        col5.add(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib));
        row.add(col5); 
        tab.add(row); 
        try
        {
	 conn = db.GetDBConnection();
	 stmt = conn.createStatement();
	 rs   = stmt.executeQuery(query);
	 while(rs.next())
	 {
           String itmname = rs.getString(1);
           String unittype = null;
           TableRow row1 = new TableRow("Left",null,null);
           TableCol col6 = new TableCol(null,null,null,null,null);
           col6.add(WebUtil.getBlankSpaces(4));
           col6.add(util.createLabelItem( itmname, vLabelAttrib) );
           row1.add(col6);
           query = " SELECT DM_UnitType FROM T_Item "+
                   " WHERE  Item_Name = '"+ itmname + "'";
    	   stmt1 = conn.createStatement();
           rs1 = stmt1.executeQuery( query );
           if( rs1.next() )
           {
              unittype = rs1.getString(1);
              unittype = Domain.getDomainDescFrmAttrib("UNITTYPE", unittype, nLangID);
           }
           row1.add( new TableCol( util.createLabelItem( unittype, vLabelAttrib), null, null, null, null) );

           if(stmt1!=null)
             stmt1.close();
           if(rs1!=null)
             rs1.close();

           anQtyQry[0] = 0;
           anQtyQry[1] = 0;
           anQtyQry[2] = 0;
           anQtyQry[3] = 0;

           String query2= query1 + " AND ort.FK_Item_ID = "+ db.getID( itmname, "Item") +
                          " ORDER BY ord.Order_Dt ";
    	   stmt1 = conn.createStatement();
           rs1 = stmt1.executeQuery( query2 );
           while(rs1.next())
	   {
               int quantity=0;
               int nMonth=0;
               try
               {
                 nMonth = Integer.parseInt(Parse.GetSubString(
                          IngDate.dateToStr(rs1.getString(1)),".",1));
               }catch(Exception e){nMonth=0;}
               try
               {
                 quantity = Integer.parseInt(rs1.getString(2));
               }catch(Exception e){quantity=0;}

               if (nMonth>=1 && nMonth<=3)
                   anQtyQry[0] = anQtyQry[0] + quantity;
               else if (nMonth>=4 && nMonth<=6)
                 anQtyQry[1] = anQtyQry[1] + quantity;
               else if (nMonth>=7 && nMonth<=9)
                 anQtyQry[2] = anQtyQry[2] + quantity;
               else if (nMonth>=10 && nMonth<=12)
                 anQtyQry[3] = anQtyQry[3] + quantity;
           }
           if(stmt1!=null)
             stmt1.close();
           if(rs1!=null)
             rs1.close();
           for(int i=0;i<4;i++)
           { 
              if(anQtyQry[i] != 0)
                row1.add( new TableCol( util.createLabelItem( Integer.toString(anQtyQry[i]), vLabelAttrib ), "right", null, null, null ) );
              else
                row1.add( new TableCol( "&nbsp;", null, null, null, null ));
           }
           tab.add(row1);
         }
        }catch(SQLException sexe){System.out.println(sexe);}
	finally
	{
          try
          {
            if(rs1!=null)
	     rs1.close();
	    if(rs!=null)
	     rs.close();
	    if(stmt!=null)
	     stmt.close();
	    if(conn!=null)
	     conn.close();
    	  }catch(SQLException sexe){System.out.println(sexe);}
	}
        body.add(tab); 
      }
      else if(vYear.equals("M"))
      {
        Table tab = new Table("1","center","Border=\"0\" width=\"95%\" BGCOLOR=\"#FFFFCA\" COLS=6");
        TableRow row = new TableRow("Left",null,"BGCOLOR=\"#666666\"");
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_MONTH","Monthly");
        vBPLate1 = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_ITEM","Item");
        TableCol col = new TableCol(null,null, null, null,"WIDTH=\"22%\"");
        col.add(WebUtil.getBlankSpaces(2));
        col.add(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib));
        col.add(WebUtil.getBlankSpaces(2));
        col.add(util.createLabelItem(vBPLate1, "COLOR=\"white\" " + vLabelAttrib));
        row.add(col); 

        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_UNIT","Unit");
        TableCol col1 = new TableCol(null,"Center", null, null,"WIDTH=\"6%\"");
        col1.add(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib));
        row.add(col1); 

        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_JAN","JAN");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_FEB","FEB");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_MAR","MAR");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_APR","APR");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_MAY","MAY");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_JUN","JUN");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_JUL","JUL");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_AUG","AUG");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_SEP","SEP");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_OCT","OCT");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_NOV","NOV");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        vBPLate = cdata.GetConfigValue("TR_REPORT", nLangID, "BL_LABEL.B_REPORT_DEC","DEC");
        row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"white\" " + vLabelAttrib)
                        ,"Center", null, null,"WIDTH=\"6%\""));
        tab.add(row);
        try
        {
	 conn = db.GetDBConnection();
	 stmt = conn.createStatement();
	 rs   = stmt.executeQuery(query);
	 while(rs.next())
	 {
           String itmname = rs.getString(1);
           String unittype = null;
           TableRow row1 = new TableRow("Left",null,null);
           TableCol col6 = new TableCol(null,null,null,null,null);
           col6.add(WebUtil.getBlankSpaces(4));
           col6.add(util.createLabelItem(rs.getString(1),vLabelAttrib));
           row1.add(col6);

           query = " SELECT DM_UnitType FROM T_Item "+
                   " WHERE  Item_Name = '"+ itmname + "'";
    	   stmt1 = conn.createStatement();
           rs1 = stmt1.executeQuery( query );
           if( rs1.next() )
           {
              unittype = rs1.getString(1);
              unittype = Domain.getDomainDescFrmAttrib("UNITTYPE", unittype, nLangID);
           }
           row1.add( new TableCol( util.createLabelItem( unittype, vLabelAttrib), null, null, null, null) );

           if(stmt1!=null)
             stmt1.close();
           if(rs1!=null)
             rs1.close();

           anQtyQry[0] = 0;
           anQtyQry[1] = 0;
           anQtyQry[2] = 0;
           anQtyQry[3] = 0;
           anQtyQry[4] = 0;
           anQtyQry[5] = 0;
           anQtyQry[6] = 0;
           anQtyQry[7] = 0;
           anQtyQry[8] = 0;
           anQtyQry[9] = 0;
           anQtyQry[10] = 0;
           anQtyQry[11] = 0;

           String query2= query1 + " AND ort.FK_Item_ID = "+ db.getID( itmname, "Item") +
                          " ORDER BY ord.Order_Dt ";
    	   stmt1 = conn.createStatement();
           rs1 = stmt1.executeQuery( query2 );
           while(rs1.next())
	   {
               int quantity=0;
               int nMonth=-1;
               try
               {
                 nMonth = Integer.parseInt(Parse.GetSubString(
                          IngDate.dateToStr(rs1.getString(1)),".",1));
               }catch(Exception e){nMonth=-1;}
               try
               {
                 quantity = Integer.parseInt(rs1.getString(2));
               }catch(Exception e){quantity=0;}
               if(nMonth>=1)
                 anQtyQry[nMonth-1] = anQtyQry[nMonth-1] + quantity;
           }
           if(stmt1!=null)
             stmt1.close();
           if(rs1!=null)
             rs1.close();

           for(int i=0;i<12;i++)
           { 
              if(anQtyQry[i] != 0)
                row1.add( new TableCol( util.createLabelItem( Integer.toString(anQtyQry[i]), vLabelAttrib), "right", null, null, null) );
              else
                row1.add( new TableCol( "&nbsp;", null, null, null, null) );
           }
           tab.add(row1);
         }
	}catch(SQLException sexe){System.out.println(sexe);}
	finally
	{
          try
          {
            if(rs1!=null)
	     rs1.close();
	    if(rs!=null)
	     rs.close();
	    if(stmt!=null)
	     stmt.close();
            if(stmt1!=null)

	     stmt1.close();
	    if(conn!=null)
	     conn.close();
    	  }catch(SQLException sexe){System.out.println(sexe);}
	}
        body.add(tab); 
      }
      page.add(body);
      out.println(page);
   }
}

