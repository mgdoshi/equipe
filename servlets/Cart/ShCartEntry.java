import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ShCartEntry extends HttpServlet
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
      String rCartDef[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
 
      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      String vItemClassQry = " Select ItemClass_ID, ItemClass_Name ||'-'|| ItemClass_Desc " +
                             " From T_ItemClass Order By ItemClass_Name"; 

      String vItemGroupQry = " Select ItemGroup_ID, ItemGroup_Name ||'-'|| ItemGroup_Desc " +
                             " From T_ItemGroup Order By ItemGroup_Name";

      String pvMode = request.getParameter("pvMode");
      String pnCartDefID = request.getParameter("pnCartDefID");
      int cartID=-1;
      try
      {  
       cartID = Integer.parseInt(pnCartDefID);  

      }catch(Exception e){}


      if(cartID > 0)
         vFormType = cdata.GetConfigValue( "TR_SHOPCART", nLangID, "WD_FORM_UPDATE","Shopping Cart / U");                      
      else
         vFormType = cdata.GetConfigValue( "TR_SHOPCART", nLangID, "WD_FORM_INSERT","Shopping Cart / I");                      

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ShopCart/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnCartDefID!=null && !pnCartDefID.equals("") && !pnCartDefID.equalsIgnoreCase("null"))
      { 
        rCartDef = db.getRecord( pnCartDefID, "CartDef" );
      }  
      else
        rCartDef = new String[7];  

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ShCartEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add( new FormHidden("pnTransID", nTransID, null ) );
      form.add( new FormHidden("nCartDef_ID", pnCartDefID, null ) );
      form.add( new FormHidden("vAction", null, null ) );
      form.add( new NL(4) );
      Table tab = new Table("1","center","Border=\"0\" width=\"80%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("TR_SHOPCART", nLangID, "BL_LABEL.B_SHOPCART_SHOPCART_NAME","Shopping Cart Name");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"20%\"");
      col.add(WebUtil.NotNull);
      row.add(col) ;
      row.add(new TableCol(util.createTextItem( nUserID, "TR_SHOPCART", "vCartDef_Name", pvMode, "20", "40", rCartDef[2], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,null)) ;

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("TR_SHOPCART", nLangID, "BL_LABEL.B_SHOPCART_ITEM_NAME","Item Name");
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row1.add(new TableCol(util.createTextItem( nUserID, "TR_SHOPCART", "vItem_Name", pvMode, "20", "40", rCartDef[3], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,null)) ;

      TableRow row4 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("TR_SHOPCART", nLangID, "BL_LABEL.B_SHOPCART_ITEM_DESC","Item Desc");
      row4.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null));
      row4.add(new TableCol(util.createTextItem( nUserID, "TR_SHOPCART", "vItem_Desc", pvMode, "20", "40", rCartDef[4],null, vTextAttrib),null,null,null,null)) ;
 
      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("TR_SHOPCART", nLangID, "BL_LABEL.B_SHOPCART_FK_ITEMCLASS_ID","Item Class");
      row2.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif.gif", vBPLate, vLabelAttrib ),null, null, null, null)); 
      row2.add(new TableCol(util.createList( nUserID, "TR_SHOPCART", "nFk_ItemClass_ID", pvMode, vItemClassQry, rCartDef[5], null, vListAttrib),null,null,null,null));

      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("TR_SHOPCART", nLangID, "BL_LABEL.B_SHOPCART_FK_ITEMGROUP_ID","Item Group");
      row3.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif.gif", vBPLate, vLabelAttrib ),null, null, null, null)); 
      row3.add(new TableCol(util.createList( nUserID, "TR_SHOPCART", "nFk_ItemGroup_ID", pvMode, vItemGroupQry, rCartDef[6], null, vListAttrib),null,null,null,null));

      tab.add(row);  
      tab.add(row1);  
      tab.add(row4);
      tab.add(row2);  
      tab.add(row3);  

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

      int nMsgID=-1;

      String nLangID=null;
      String nUserID=null;
      String errMsg=null;  
      String nTransID=null;
      String vStatus=null;
      String mode=""; 
      String query = null;
      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      Message msg = new Message(); 
      Valid val = new Valid(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String pnTransID = request.getParameter("pnTransID");
      String nCartDef_ID = request.getParameter("nCartDef_ID");
      String vCartDef_Name = Parse.formatStr(request.getParameter("vCartDef_Name"));
      String vItem_Name  = Parse.formatStr(request.getParameter("vItem_Name"));
      String vItem_Desc  = Parse.formatStr(request.getParameter("vItem_Desc"));
      String nFk_ItemClass_ID = request.getParameter("nFk_ItemClass_ID");
      String nFk_ItemGroup_ID = request.getParameter("nFk_ItemGroup_ID");
      String vAction   = request.getParameter("vAction"); 

      String usr = db.getName( nUserID, "User" );      
      vStatus    = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction.equalsIgnoreCase("Insert"))
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            int cart_id = Integer.parseInt(db.getNextVal("S_CartDef"));
            query = " INSERT INTO T_CartDef ( CartDef_ID, Fk_User_ID, CartDef_Name, Item_Name, Item_Desc, Fk_ItemClass_ID,"+
                    " Fk_ItemGroup_ID, Modifier, Change_Dt )"+
                    " VALUES ("+cart_id+", '"+nUserID+"',"+val.IsNull( vCartDef_Name) +","+val.IsNull( vItem_Name ) +","+val.IsNull( vItem_Desc )+","+val.IsNull( nFk_ItemClass_ID )+","+val.IsNull( nFk_ItemGroup_ID )+",'"+usr+"', '"+dt+"')";
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
            query = "UPDATE T_CartDef SET Fk_User_ID = "+nUserID+","+
                    " CartDef_Name = "+val.IsNull( vCartDef_Name )+"," + 
                    " Item_Name  = "+val.IsNull( vItem_Name )+"," + 
                    " Item_Desc = "+val.IsNull( vItem_Desc )+"," + 
                    " Fk_ItemClass_ID = "+val.IsNull( nFk_ItemClass_ID ) +"," +
                    " Fk_ItemGroup_ID = "+val.IsNull( nFk_ItemGroup_ID ) +"," +
                    " Modifier    = '"+usr+"'," + 
                    " Change_Dt   = '"+dt+"'" + 
                    " WHERE CartDef_ID = "+nCartDef_ID;
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
      { 
        response.sendRedirect("/JOrder/servlets/ShCartFrame?pvMode=Q&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
      }
      else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

