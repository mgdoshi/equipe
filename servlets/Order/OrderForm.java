import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class OrderForm
{
  public static Script ShowOrderScript(HttpServletRequest request,String pvMode, String vPID )
  {
    ConfigData config = new ConfigData();
    Message msg = new Message();
    DBConnect db = new DBConnect();
    Connection conn = null;
    Statement  stmt = null;
    ResultSet  rs = null;
    String query = null;

    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    String nUserID  = Parse.GetValueFromString( vPID, "UserID" );
    String vOrderNo   = config.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDER_NR", "Order No" );
    String vOrderDt   = config.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDER_DT", "Order Date" );
    String vPosNr     = config.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_POS_NR", "Pos No" );
    String vItemPack  = config.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_FK_ITEMPACK_ID", "Item Pack" );
    String vItemName  = config.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_FK_ITEM_ID", "Item Name" );
    String vQuantity  = config.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_QUANTITY", "Quantity" );
    String vUnitType  = config.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_DM_UNITTYPE", "Unit Type" );
    String vUnitPrice = config.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_UNITPRICE", "Unit Price" );
    String vOrdDtlsCnt = config.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDDTLSCOUNT", "Order Dtls Count" );
    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("I") )
    {
     scrdata.add(" var avItemID = new Array()                                   \n"+
                 " var acUnitType = new Array()                                 \n"+
                 " var avUnitType = new Array()                                 \n"+

                 " function show_UnitType( pnItemID, nPos ) {                   \n"+
                 "   var i = 0                                                  \n"+
                 "   with( this.right_frame.document.forms[0] ) {               \n"+
                 "    if ( pnItemID == \"\" ) {                                 \n"+
                 "       vUnitType[nPos].value = \"\"; cDM_UnitType[nPos].value = \"\" \n"+
                 "     }\n" );
     try
     {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();
        query = " SELECT itm.Item_ID, DM_UnitType "+
                " FROM   T_Item itm, T_UserItem uit "+
                " WHERE  uit.Fk_Item_ID = itm.Item_ID "+
                " AND    uit.Fk_User_ID = "+ nUserID +
                " AND    itm.InActive = '1' "+
                " ORDER BY itm.Item_Name ";
        rs = stmt.executeQuery(query);
        while( rs.next() )
        {
          String itemid = rs.getString(1);
          String unittype =  rs.getString(2);
          scrdata.add("   avItemID[i] = \""+ itemid   +"\"; \n"+
                      " acUnitType[i] = \""+ unittype +"\"; \n"+
                      " avUnitType[i++] = \""+ Domain.getDomainDescFrmAttrib( "UNITTYPE", unittype, nLangID ) +"\" \n" );
        }
     }catch(SQLException sexe){}
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
 
     scrdata.add("  for( var i=0; i<avItemID.length; i++ )  \n"+
                 "     if ( avItemID[i] == pnItemID ) {     \n"+
                 "       vUnitType[nPos].value = avUnitType[i]; cDM_UnitType[nPos].value = acUnitType[i] \n"+
                 "      }                                   \n"+
                 "  } } \n");

     scrdata.add("  function clear_record( i ) {  \n"+
                 "    with( this.right_frame.document.forms[0] ) { \n"+
                 "    if ( confirm( \""+ msg.GetMsgDesc( 140, nLangID ) + "\" ) ) { \n"+
                 "      nPos_No[i].value = \"\"; nQuantity[i].value =\"\"; nUnitPrice[i].value = \"\" \n"+
                 "      if ( nFk_Item_ID[i].type == \"select-one\" ) nFk_Item_ID[i].selectedIndex = 0 \n"+
                 "      else nFk_Item_ID[i].value = \"\"                                              \n"+
                 "      if ( nFk_ItemPack_ID[i].type == \"select-one\" ) nFk_ItemPack_ID[i].selectedIndex = 0 \n"+
                 "      else nFk_ItemPack_ID[i].value = \"\"                     \n"+
                 "      cDM_UnitType[i].value = \"\";  vUnitType[i].value = \"\" \n"+
                 "      vText1[i].value = \"\"; vText2[i].value  = \"\"; vText3[i].value = \"\"; vText4[i].value = \"\"; vText5[i].value = \"\" \n"+
                 "      nNum1[i].value = \"\";  nNum2[i].value = \"\";   nNum3[i].value = \"\";  nNum4[i].value = \"\";  nNum5[i].value = \"\"; \n"+
                 "      vRemarks1[i].value = \"\"; vRemarks2[i].value = \"\"; vRemarks3[i].value = \"\"; nPos_No[i].focus() \n"+
                 "   } } }\n" );

     scrdata.add("function submit_form( pvAction ){				                \n"+
                 "  var aFormFields                                             \n"+ 
                 "  var nIdx = 0                                                \n"+
                 "  var vErrMsg                                                 \n"+ 
                 "  with( this.right_frame.document.forms[0] ) {                \n"+ 
             	 "   if ( pvAction == \"ShpItm\" ) {                         	\n"+              
                 "     vURL = \"/JOrder/servlets/ShowShopCartItms\"             \n"+ 
                 "     ShpItemWindow = window.open( vURL,\"ShpItemWindow\", \"menubar=0,scrollbars=1,resizable=1,width=450,height=400\")\n"+  
                 "     ShpItemWindow.oldWindow = top; return                    \n"+ 
                 "  }                                                           \n"+ 
               	 "  vAction.value = pvAction                                    \n"+
              	 "  aField = new Array( \""+ vOrderNo +"\", vOrder_No.value, \""+ vOrderDt +"\", dOrder_Dt.value ) \n"+
             	 "  vErrMsg = top.check_fields( aField )                        \n"+
             	 "   if ( vErrMsg != \"\" ) {                          	        \n"+
             	 "     vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg + \".\" \n"+
             	 "     alert( vErrMsg ); return                                 \n"+
             	 "   }                                                          \n"+
             	 "   for ( var i=0; i<nPos_No.length-1; i++ ) {                 \n"+
                 "    aFormFields = new Array( \"A\", nPos_No[i].value, \"B\", ( nFk_Item_ID[i].type == \"select-one\" ? nFk_Item_ID[i].options[nFk_Item_ID[i].selectedIndex].value : nFk_Item_ID[i].value ), \"C\", ( nFk_ItemPack_ID[i].type == \"select-one\" ? nFk_ItemPack_ID[i].options[nFk_ItemPack_ID[i].selectedIndex].value : nFk_ItemPack_ID[i].value ), \"D\", nQuantity[i].value, \"E\", ( cDM_UnitType[i].value == \"\" ? \"\" : vUnitType[i].value ), \"F\", nUnitPrice[i].value, \"G\", vText1[i].value, \"H\", vText2[i].value, \"I\", vText3[i].value, \"J\", vText4[i].value, \"K\", vText5[i].value, \"L\", nNum1[i].value, \"M\", nNum2[i].value, \"N\", nNum3[i].value, \"O\", nNum4[i].value, \"P\", nNum5[i].value, \"Q\", vRemarks1[i].value, \"R\", vRemarks2[i].value, \"S\", vRemarks3[i].value ) \n"+
             	 "    vErrMsg = top.check_fields( aFormFields )                 \n"+
             	 "    if ( top.isRowBlank( vErrMsg, 19 ) ) {                    \n"+                
                 "      nIdx++; continue;                           	        \n"+               
             	 "    }                                                         \n"+
             	 "    else {                                                    \n"+                
              	 "      aFormFields = new Array( \""+ vPosNr    +"\", nPos_No[i].value, \n"+
                 "                               \""+ vItemPack +"\", ( nFk_ItemPack_ID[i].type == \"select-one\" ? nFk_ItemPack_ID[i].options[nFk_ItemPack_ID[i].selectedIndex].value : nFk_ItemPack_ID[i].value ),  \n"+
                 "                               \""+ vItemName +"\", ( nFk_Item_ID[i].type == \"select-one\" ? nFk_Item_ID[i].options[nFk_Item_ID[i].selectedIndex].value : nFk_Item_ID[i].value ), \n"+
                 "                               \""+ vQuantity +"\", nQuantity[i].value, \n"+
                 "                               \""+ vUnitType +"\", ( cDM_UnitType[i].value == \"\" ? \"\" : vUnitType[i].value ), \n"+
                 "                               \""+ vUnitPrice+"\", nUnitPrice[i].value  )\n"+
             	 "      vErrMsg = top.check_fields( aFormFields )                           \n"+
             	 "      if ( vErrMsg != \"\" ) {                                            \n"+
             	 "         vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID ) +"\" + vErrMsg + \""+ msg.GetMsgDesc( 18, nLangID ) +"\"+(i+1)+\".\" \n"+
             	 "         alert( vErrMsg ); nPos_No[i].focus(); nPos_No[i].select(); return \n"+
             	 "       }                                                                   \n"+
             	 "    }                                                                      \n"+
             	 "   }                                                                       \n"+
             	 "   if ( nIdx == nPos_No.length-1 ) {                                       \n"+
             	 "     alert( \""+ msg.GetMsgDesc( 17, nLangID)+"\" ); nPos_No[0].focus(); nPos_No[0].select(); return \n"+
             	 "   }                                                                       \n"+   
             	 "   for ( var j=0; j<nFk_Item_ID.length-2; j++ )                            \n"+
             	 "     for ( var k=j+1; k<nFk_Item_ID.length-1; k++ ) {                      \n"+
             	 "       if( nFk_Item_ID[j].type == \"select-one\" && nFk_Item_ID[j].selectedIndex != 0 && nFk_Item_ID[j].selectedIndex == nFk_Item_ID[k].selectedIndex ) { \n"+
             	 "         alert( \""+ msg.GetMsgDesc( 35, nLangID )+"\" ); nFk_Item_ID[k].focus(); nFk_Item_ID[k].select(); return \n"+
                 "         }					              	            \n"+         
             	 "       if( !top.IsNull(nPos_No[j].value) && nPos_No[j].value == nPos_No[k].value ) { \n"+
             	 "         alert( \""+ msg.GetMsgDesc( 34, nLangID ) +"\" ); nPos_No[k].focus(); nPos_No[k].select(); return \n"+
                 "       }                                                                  \n"+
                 "      }                                                                   \n"+
             	 "   if ( ( pvAction == \"Insert\" ) && confirm( \""+ msg.GetMsgDesc( 10, nLangID ) +"\" ) ) { \n"+
             	 "     if ( confirm( \""+ msg.GetMsgDesc( 37, nLangID) +"\" ) ) cDM_OrdStat.value = \"D\"  \n"+
             	 "       submit()                                               \n"+
                 "   }      						        \n"+
             	 "   if ( (pvAction == \"Update\") && confirm( \"" + msg.GetMsgDesc( 10, nLangID ) + "\" ) ) \n"+
             	 "     submit()                                                 \n"+
                 " }}     						        \n");
    }
     
    if( pvMode!=null && pvMode.equalsIgnoreCase("T") )
    {
     scrdata.add("function submit_form( pvAction ) {                                \n"+
		             "  var aField                                          \n"+
		             "  var vErrMsg                                         \n"+
                 "  with( this.right_frame.document.forms[0] ) {                    \n"+
              	 "    if ( vTemplName.options.length>1 && vTemplName.selectedIndex == 0 ) {                        \n"+
              	 "      alert( \""+ msg.GetMsgDesc( 33, nLangID )+"\" ); vTemplName.focus(); return  \n"+
                 "  }					              	            \n"+             
              	 "  aField = new Array( \""+vOrdDtlsCnt+"\", nOrdDtlsCount.value ) \n"+
              	 "  vErrMsg = top.check_fields( aField )                           \n"+
              	 "   if ( vErrMsg != \"\" ) {                          	        \n"+
              	 "     vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg + \".\" \n"+
              	 "     alert( vErrMsg ); return                                 \n"+
              	 "   }                                                          \n"+
                 "  if( navigator.appName == \"Netscape\")                      \n" +
                 "     Browser.value = \"1\"                                    \n" +
                 "  else                                                        \n" +
                 "     Browser.value = \"0\"                                    \n" +
              	 "  if ( confirm( \""+ msg.GetMsgDesc( 15, nLangID ) +"\" ) )  submit() \n"+
                 "  } } \n" );
    }

    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

  public static synchronized Table createItemList(String Columns, String WhereClause, String Titles)
  {
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
          if( Titles != null )
          {
            String []tokens = Parse.parse( Titles, ",");
            count = tokens.length;
            for( int i=1; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol );
              headrow.add(th);
              th.setFormat(f);
            }
            th = new TableHeader( "Select", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }
          else
          {
            String []tokens = Parse.parse( Columns, ",");
            count = tokens.length-2;
            for( int i=1; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol);
              headrow.add(th);
              th.setFormat(f);
            }
            th = new TableHeader( "Select", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }

          conn = obj.GetDBConnection();
          stmt = conn.createStatement();

          String query = "Select " + Columns + " From T_Item itm, T_UserItem uit, T_Lang lng " + WClause;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
			String cUnitType = rs.getString(6);
    		String nLangID = rs.getString(7);
            for( int i=2; i<=count; i++)
            {
              String data = rs.getString(i);
			  if( i==4 && !( data == null || data.equals("") || data.equalsIgnoreCase("null") ) )
			     data = obj.getName( data, "ItemClass");
 			  if( i==5 && !( data == null || data.equals("") || data.equalsIgnoreCase("null") ) )
			     data = obj.getName( data, "ItemGroup");
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";                 
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }

            FormCheckBox chkbox = new FormCheckBox("nShpItem", id, null, null);
            TableCol td2 =  new TableCol( chkbox , "center", null, null, null);
            datarow.add(td2);
			table.add( " <INPUT TYPE=\"Hidden\" NAME=\"cUnitType\" VALUE=\""+ cUnitType +"\">" ); 
			table.add( " <INPUT TYPE=\"Hidden\" NAME=\"vUnitType\" VALUE=\""+ Domain.getDomainDescFrmAttrib( "UNITTYPE", cUnitType, nLangID ) +"\">");
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
}

