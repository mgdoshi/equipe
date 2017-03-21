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

public class ShowShopCartItms extends HttpServlet
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
      String vImagePath=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;

      String nShpCartID = request.getParameter("nShpCartID");
	  String nItmClsID = null;
      String nItmGrpID = null;
      String vItmName = null;
      String vItmDesc = null;

      DBConnect db = new DBConnect();
 	  Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      Message msg = new Message();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      String vShopCartName = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_SHOPCART", "Shopping Cart" );
      String vShpCartQry = " SELECT CartDef_ID, CartDef_Name "+
                           " FROM   T_CartDef "+
                           " WHERE  Fk_User_ID = "+ nUserID;

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ShopCart/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if( !( nShpCartID == null || nShpCartID.equals("") || nShpCartID.equals("null") ) ) 
	  {
   	    try
        {
          conn =  db.GetDBConnection();
          stmt = conn.createStatement();
          String query = " SELECT Fk_ItemClass_ID, Fk_ItemGroup_ID, Item_Name, Item_Desc "+
                         " FROM   T_CartDef "+
                         " WHERE  CartDef_ID = "+ nShpCartID +
                         " AND    Fk_User_ID = "+ nUserID;

          rs = stmt.executeQuery(query);
          if(rs.next())
          {
            nItmClsID = rs.getString(1);
            nItmGrpID = rs.getString(2);
            vItmName = rs.getString(3);
            vItmDesc = rs.getString(4);
          }
 
        }catch(SQLException sexe){System.out.println(sexe.getMessage());}
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
          }catch(SQLException sexe){System.out.println(sexe.getMessage());}
        } 
      }
      Page page = new Page();
      Head head = new Head();
      Title title = new Title( "Order Entry "+"&"+ "Tracking System -:- Shopping Cart List");
      Body body = new Body( "/ordimg/BACKGR2.GIF",null); 
      Form form = new Form( "/JOrder/servlets/ShowShopCartItms", "GET", null, null, null );

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" );

      if( nShpCartID == null || nShpCartID.equals("") || nShpCartID.equals("null") ) 
	  {
       scrdata.add( "   function submit_form()                       	    \n" +
                    "   { 			                     			        \n" +
                    "       with( document.forms[0] ) {                   	\n" +
      	            "         aField = new Array( \""+vShopCartName+"\", nShpCartID.options[nShpCartID.selectedIndex].value ) \n"+
              	    "         vErrMsg = this.opener.top.check_fields( aField )\n"+
              	    "         if ( vErrMsg != \"\" ) {                      \n"+
              	    "           vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg + \".\" \n"+
              	    "           alert( vErrMsg ); return                    \n" +
              	    "         }                                             \n" +
                    "         submit()                                      \n" +
				    "       }                                               \n" +
  	                "  }					                              \n\n" );
      }
	  else
      {
       scrdata.add( " function submit_form() {          		              	        \n"+
                    "    var ObjOldWin = this.opener.top.mid_frame.right_frame.document.forms[0] \n"+ 
                    "    var i					              	                        \n"+
                    "    var j  			              	                            \n"+
                    "    var nIdx                                                       \n"+
                    "    var bFlag                                                      \n"+
                    "    var aField = new Array()                                       \n"+
                    "    with( ObjOldWin ) {                                            \n"+
                    "      for( i=0; i<nPos_No.length-1; i++ ) {                        \n"+
                    "         aField = new Array( \"A\", nPos_No[i].value, \"B\", nFk_ItemPack_ID[i].options[nFk_ItemPack_ID[i].selectedIndex].value, \"C\", nFk_Item_ID[i].options[nFk_Item_ID[i].selectedIndex].value, \"D\", nQuantity[i].value, \"E\", vUnitType[i].value, \"F\", nUnitPrice[i].value, \"G\", vText1[i].value, \"H\", vText2[i].value, \"I\", vText3[i].value, \"J\", vText4[i].value, \"K\", vText5[i].value, \"L\", nNum1[i].value, \"M\", nNum2[i].value, \"N\", nNum3[i].value, \"O\", nNum4[i].value, \"P\", nNum5[i].value, \"Q\", vRemarks1[i].value, \"R\", vRemarks2[i].value, \"S\", vRemarks3[i].value ) \n"+
           	        "         vErrMsg = this.opener.top.check_fields( aField )          \n"+
           	        "         if( this.opener.top.isRowBlank( vErrMsg, 19 ) ) {         \n"+
           	        "             nIdx = i; break; }                                    \n"+
           	        "      }                                                            \n"+
                    "    }                                                              \n"+
                    "    with( document.forms[0] ) {                                    \n"+
              	    "      for( j=1; j<nShpItem.length; j++ ) {                         \n"+
                    "        bFlag = false                                               \n"+
           	        "        if( nShpItem[j].checked ) {                                \n"+
                      // Reflect only unique Item names in order details form
                    "          for( i=0; i<ObjOldWin.nFk_Item_ID.length-1; i++ ) {      \n"+
                    "             if ( ObjOldWin.nFk_Item_ID[i].type == \"select-one\" && ObjOldWin.nFk_Item_ID[i].options[ObjOldWin.nFk_Item_ID[i].selectedIndex].value == nShpItem[j].value ) bFlag = true; \n"+
                    "          }                                                        \n"+
                    "        if ( !bFlag ) {                                            \n"+
                    "          for( i=0; i<ObjOldWin.nFk_Item_ID[nIdx].length; i++ ) {  \n"+
                    "             if ( ObjOldWin.nFk_Item_ID[i].type == \"select-one\" && ObjOldWin.nFk_Item_ID[nIdx].options[i].value == nShpItem[j].value ) \n"+
                    "                ObjOldWin.nFk_Item_ID[nIdx].selectedIndex = i;     \n"+
                    "             }                                                     \n"+
                    "             ObjOldWin.cDM_UnitType[nIdx].value = cUnitType[j].value \n"+
                    "             ObjOldWin.vUnitType[nIdx++].value = vUnitType[j].value  \n"+
                    "         }                        		                            \n"+     
                    "       }                         		                            \n"+
                    "     }                         		                            \n"+     
                    "   }       						                                \n"+
                    "  top.close()     				                                    \n"+
                    " }					              	                              \n\n");
	  }

      scrdata.add( "// End Hidding -->");
      scr.add(scrdata);

      form.add(new NL(3));

      Table tab = new Table("1","center","Border=\"0\" width=\"90%\" COLS=2 ");
      TableRow row = new TableRow("Left",null,null);
      TableHeader hd = new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"ShpName.jpg", vShopCartName, vLabelAttrib ),null, null, null,"WIDTH=\"50%\"");
	  hd.add(WebUtil.NotNull);
      HtmlTag sel =  util.createList( nUserID, "TR_ORDER", "nShpCartID", "I", vShpCartQry, nShpCartID, null, vListAttrib);
      TableCol col = new TableCol( sel,null, null, null, null);
      if( !( nShpCartID == null || nShpCartID.equals("") || nShpCartID.equals("null") ) ) 
	  {
         FormSubmit sub = new FormSubmit( null, " Go ", null );
         col.add( "&nbsp;&nbsp;&nbsp;"+ sub );
      }
      row.add(hd);
      row.add(col);
      tab.add(row);

      form.add(tab);

      if( !( nShpCartID == null || nShpCartID.equals("") || nShpCartID.equals("null") ) ) 
	  {
	    form.add( new NL(1) );
        String vColumns = "itm.Item_ID, itm.Item_Name, itm.Item_Desc, itm.Fk_ItemClass_ID, itm.Fk_ItemGroup_ID, itm.DM_UnitType, lng.Lang_ID";
        String vTitles  = "Item ID, Item Name, Item Desc, Item Class, Item Group ";
	  
	    if( vItmName== null || vItmName.equals("") || vItmName.equalsIgnoreCase("null") )
	       vItmName = "%";
        if( vItmDesc== null || vItmDesc.equals("") || vItmDesc.equalsIgnoreCase("null") )
	       vItmDesc = "%";

        String vWhereClause = " WHERE itm.Item_Name LIKE '"+ vItmName + "'"+
                              " AND   itm.Item_Desc LIKE '"+ vItmDesc + "'"+
		    				  " AND   uit.Fk_Item_ID = itm.Item_ID "+
                              " AND   itm.InActive = '1'";

   	    if( !(nItmClsID == null || nItmClsID.equals("") || nItmClsID.equalsIgnoreCase("null") ) )
           vWhereClause = vWhereClause + " AND itm.Fk_ItemClass_ID = "+ nItmClsID;
   	    if( !(nItmGrpID == null || nItmGrpID.equals("") || nItmGrpID.equalsIgnoreCase("null") ) )
           vWhereClause = vWhereClause + " AND itm.Fk_ItemGroup_ID = "+ nItmGrpID; 
 
        vWhereClause = vWhereClause + " AND uit.Fk_User_ID = "+ nUserID + " AND lng.Lang_ID = "+ nLangID;

        form.add( new FormHidden( "nShpItem", null, null ) );
        form.add( new FormHidden( "cUnitType", null, null ) );
	    form.add( new FormHidden( "vUnitType", null, null ) );
      
        Table tb = OrderForm.createItemList( vColumns, vWhereClause, vTitles );
        form.add(tb);
      }

	  NL nl = new NL(2);
      Center cen = new Center();
      FormButton but1 = new FormButton( null, " OK ", "onClick=\"submit_form('Ok')\"" );
      FormButton but2 = new FormButton( null, "Cancel", "onClick=\"top.close()\"" );
      form.add(nl);
      cen.add(but1);
      cen.add(but2);
      form.add(cen);
      body.add(form);
      head.add(title);
      head.add(scr);
      page.add(head);
      page.add(body);
      out.println(page);
    }
}

