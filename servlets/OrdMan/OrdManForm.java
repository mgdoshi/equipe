import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class OrdManForm
{
  public static Script ShowOrdManScript(String pvMode,String pnOrderID, String vPID )
  {
    DBConnect db = new DBConnect();
    Connection conn = null;
    Statement  stmt = null; 
    ResultSet  rs = null;
    String query = null;
    Message msg = new Message();
    ConfigData cdata = new ConfigData();

    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    String nUserID  = Parse.GetValueFromString( vPID, "UserID" );
    String vTemplName = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_TEMPLATE", "Select Order Template" );
    String vOrderStat  = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_DM_ORDSTAT", "Order Status" );
    String vExpDelDt   = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_EXPDEL_DT", "Exp Del Date" );
    String vClientName = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_CLIENT_NAME" , "Client Name" );
    String vPosNo      = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_POS_NR" , "Pos No" );
    String vItemName   = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_FK_ITEM_ID", "Item Name" );
    String vQuantity   = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_QUANTITY" , "Quantity" );
    String vUnitType   = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_DM_UNITTYPE", "Unit Type" );
    String vUnitPrice  = cdata.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_UNITPRICE", "Unit Price" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if(pvMode!=null && pvMode.equalsIgnoreCase("T"))
    {  
      scrdata.add(" function submit_form( ) {	\n" +
                  "  var nIdx = 0               \n" +
                  "  with( this.right_frame.document.forms[0] ){\n" +
                  "    for( var i=1; i<nDelete.length; i++ ){    \n" +
                  "      if( nDelete[i].checked ) {              \n" +
                  "        nIdx ++                              \n" +
                  "        nDelete[0].value += nDelete[i].value + \"~\" \n" +
                  "  } }                                        \n" +
                  "  if( nIdx == 0 ) {                          \n" +
                  "   alert( \""+msg.GetMsgDesc( 62, nLangID )+"\" ); return }    \n" +
                  "  if ( confirm( \""+msg.GetMsgDesc( 11, nLangID )+"\" )) submit()\n" +
                  "  } }                                                          \n");
    }
    else if(pvMode!=null && pvMode.equalsIgnoreCase("Q"))
    {
      scrdata.add(" function submit_form( pvAction ) {  \n" +
                  "  var vErrMsg=\"\"                          \n" +
                  "  with(this.right_frame.document.forms[0]){ \n" +
                  "  vAction.value = pvAction                  \n" +
                  "  if( navigator.appName == \"Netscape\")    \n" +
                  "     Browser.value = \"1\"                  \n" +
                  "  else                                      \n" +
                  "     Browser.value = \"0\"                  \n" +
               	  "   if ( vTemplName.options.length>1 ) {     \n"+
                  "  aField = new Array( '"+vClientName+"', pnClientID.options[pnClientID.selectedIndex].value, '"+vTemplName+"', vTemplName.options[vTemplName.selectedIndex].value  )\n" +
                  "  vErrMsg = top.check_fields( aField ) }    \n" +
                  "  if( vErrMsg == \"\" ) {                          	                 \n" +
                  "    if( confirm( \""+msg.GetMsgDesc( 15, nLangID )+"\" ) ) submit() } \n" +
                  "  else {                                                              \n" +
                  "    vErrMsg = \""+msg.GetMsgDesc(9, nLangID )+"\" + vErrMsg           \n" +
                  "    alert( vErrMsg )    \n" +
                  "    return              \n" +
                  "  } } }   \n" );
    }
    else if(pvMode!=null && pvMode.equalsIgnoreCase("D"))
    {  
      scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "  var nIdx = 0               \n" +
                  "  with( this.right_frame.document.forms[0] ) { \n" +
                  "   vAction.value = pvAction                  \n" + 
                  "   if ( pvAction == \"New\") {               \n" + 
                  "     if ( confirm( \""+msg.GetMsgDesc( 15, nLangID )+"\") ) \n"+
                  "      this.location.href = \"/JOrder/servlets/OrdManFrame?pvMode=I&pnOrderID="+pnOrderID+"&pnOrderDtlsID=&vTemplName=\"+vTemplName.value+\"&Browser=\"+Browser.value+\"&pvWhereClause=\" \n"+
                  "   }                                           \n" +
                  "   if ( pvAction == \"Delete\") {              \n" + 
                  "     for( var i=1; i<nDelete.length; i++ ){    \n" +
                  "       if( nDelete[i].checked ) {              \n" +
                  "         nIdx ++                               \n" +
                  "         nDelete[0].value += nDelete[i].value + \"~\" \n" +
                  "     } }                                       \n" +
                  "     if( nIdx == 0 ) {                         \n" +
                  "       alert( \""+msg.GetMsgDesc( 62, nLangID )+"\" ); return }    \n" +
                  "     if ( confirm( \""+msg.GetMsgDesc( 11, nLangID )+"\" ))  submit()\n" +
                  "   }                                                               \n" +
                  "   if ( pvAction == \"Update\") {                                  \n" + 
                  "     var cDMOrdStat = ( cDM_OrdStat.type == \"select-one\" ? cDM_OrdStat.options[cDM_OrdStat.selectedIndex].value : cDM_OrdStat.value ) \n"+
              	  "     aField = new Array( \""+ vOrderStat +"\", cDMOrdStat, \""+ vExpDelDt +"\", dExpDel_Dt.value ) \n"+
               	  "     vErrMsg = top.check_fields( aField )                          \n"+
             	  "     if ( vErrMsg != \"\" ) {                          	      \n"+
             	  "       vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg + \".\" \n"+
             	  "       alert( vErrMsg ); return                                    \n"+
             	  "     }                                                             \n"+
                  "     if ( pvAction == \"Update\" && confirm(\""+msg.GetMsgDesc( 10, nLangID )+"\") ) \n" +
                  "       submit()                                                    \n" +
                  "   }                                                               \n" +
                  " } }                                                               \n");
    }
    else
    {
     scrdata.add(" var avItemID = new Array()                                   \n"+
                 " var acUnitType = new Array()                                 \n"+
                 " var avUnitType = new Array()                                 \n"+

                 " function show_UnitType( pnItemID ) {                         \n"+
                 "   var i = 0                                                  \n"+
                 "   with( this.right_frame.document.forms[0] ) {               \n"+
                 "    if ( pnItemID == \"\" ) {                                 \n"+
                 "       vUnitType.value = \"\"; cDM_UnitType.value = \"\"     \n"+
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
                 "       vUnitType.value = avUnitType[i]; cDM_UnitType.value = acUnitType[i] \n"+
                 "      }                                   \n"+
                 "  } } \n");

      scrdata.add("  function submit_form( pvAction ) { \n" +
                  "    with(this.right_frame.document.forms[0]) { \n" +
                  "      vAction.value = pvAction                 \n" +
                  "      var nItemID = ( nFk_Item_ID.type == \"select-one\" ? nFk_Item_ID.options[nFk_Item_ID.selectedIndex].value : nFk_Item_ID.value ) \n" +
                  "      var cDMUnitType = ( cDM_UnitType.type == \"select-one\" ? cDM_UnitType.options[cDM_UnitType.selectedIndex].value : cDM_UnitType.value ) \n" +
                  "      aField = new Array( '"+vPosNo+"',  nPos_No.value, " +
                  "                          '"+vItemName+"',  nItemID,    " +
                  "                          '"+vQuantity+"',  nQuantity.value, " +
                  "                          '"+vUnitType+"',  cDMUnitType, " +
                  "                          '"+vUnitPrice+"',  nUnitPrice.value ) \n" +
                  " vErrMsg = top.check_fields( aField )                           \n" +
                  " if ( vErrMsg == \"\" ) {                                       \n" +
                  "   if ( ( pvAction == \"Insert\" || pvAction == \"Update\" ) && confirm(\""+msg.GetMsgDesc( 10, nLangID )+"\") ) \n" +
                  "     submit()                                              \n" +
                  " }                                                         \n" +
                  " else {                                                    \n" +
                  "   vErrMsg = \""+msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg \n" +
                  "   alert( vErrMsg ) \n" +
                  "   return \n" +
                  " } } } \n");
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

   public static synchronized Table createOrdManTable(String Columns, String WhereClause, String Titles )
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
          conn = obj.GetDBConnection();
          stmt = conn.createStatement();
          String query = "Select " + Columns + " From T_Order ord, T_Domain dom " + WClause;
          rs = stmt.executeQuery(query);
          if( Titles != null )
          {
            String []tokens = Parse.parse( Titles, ",");
            count = tokens.length;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol );
              headrow.add(th);
              th.setFormat(f);
            }
            th = new TableHeader( "Delete", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }
          else
          {
            String []tokens = Parse.parse( Columns, ",");
            count = tokens.length;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol);
              headrow.add(th);
              th.setFormat(f);
            }
            th = new TableHeader( "Delete", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }

          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            String URL = "JavaScript:call_form('"+id+"')";
            Anchor anc = new Anchor( URL, "Order # "+id, null, null, null);
            datarow.add(new TableCol( anc, null, null, null, null));
            datarow.add(new TableCol( rs.getString(2), null, null, null, null));
            datarow.add(new TableCol( IngDate.dateToStr(rs.getString(3)), null, null, null, null));
            datarow.add(new TableCol( rs.getString(4), null, null, null, null));
            FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
            datarow.add(new TableCol( chkbox , "center", null, null, null));
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
      return table;  
  }

   public static synchronized Table createOrdDtlsManTable(String Columns, String WhereClause, String Titles)
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
            th = new TableHeader( "Delete", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }
          else
          {
            String []tokens = Parse.parse( Columns, ",");
            count = tokens.length;
            for( int i=1; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol);
              headrow.add(th);
              th.setFormat(f);
            }
            th = new TableHeader( "Delete", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }

          conn = obj.GetDBConnection();
          stmt = conn.createStatement();
          String query = "Select " + Columns + " From T_OrderDtls " + WClause;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String oid = rs.getString(1);
            String id = rs.getString(2);
            String URL = "JavaScript:call_form('"+oid+"','"+id+"')";
            Anchor anc = new Anchor( URL, "OrderDtls ID # " + id, null, null, null);
            datarow.add(new TableCol( anc, null, null, null, null));

            datarow.add(new TableCol( rs.getString(3), null, null, null, null));
            datarow.add(new TableCol( obj.getName( rs.getString(4),"Item"), null, null, null, null));
            datarow.add(new TableCol( obj.getName( rs.getString(5),"ItemPack"), null, null, null, null));

            FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
            datarow.add(new TableCol( chkbox , "center", null, null, null));
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
      return table;  
  }

}

