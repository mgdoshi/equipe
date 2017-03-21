import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ClnItmRtForm
{
  public static Script ShowClnItmRtScript(HttpServletRequest request, String pvMode, String vPID, String pvWhereClause )
  {

    String nLangID=null;
    String nUserID=null;

    ConfigData cdata = new ConfigData();
    Message msg = new Message();

    nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    nUserID  = Parse.GetValueFromString( vPID, "UserID" );

    String vItemName  = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_FK_ITEM_ID", "Item Name" );
    String vFromDate  = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_FROM_DT", "From Date" );
    String vToDate    = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_TO_DT", "To Date" );
    String vMinQty    = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_MINQTY", "Min Qty" );
    String vMaxQty    = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_MAXQTY", "Max Qty" );
    String vUnitPrice = cdata.GetConfigValue( "ST_CLIENTITEMRATE", nLangID, "BL_LABEL.B_CLIENTITEMRATE_UNITPRICE", "Unit Price" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");

    if( pvMode!=null && pvMode.equalsIgnoreCase("T"))
    {
     scrdata.add("  function submit_form( pvAction ) { \n"  +
                 "  var nCount = 0\n"                       +
                 "  with( this.right_frame.document.forms[0] ) { \n" +
                 "  var url ='/JOrder/servlets/ClnItmRtFrame?pvMode=N&pnClnItmRateID=&pvWhereClause="+pvWhereClause+"&pnClientID='\n" +
                 "  var cid = pnClientID.value               \n"+
                 "  var url = url + cid                      \n"+
                 "  if ( pvAction == \"New\") {\n"          +
                 "    if ( confirm(\""+msg.GetMsgDesc( 15,nLangID)+"\" ))\n"  +
                 "       this.location.href = url }\n" +
                 "    if ( pvAction == \"Delete\") {             \n" +
                 "      for( var i=0; i<nDelete.length; i++ ) {  \n" +
                 "	   nCount = ( nDelete[i].checked ? ++nCount : nCount ) \n" +
                 "      }						       \n" +
                 "      pnCount.value = nCount        		               \n" +
                 "       if( nCount == 0 ) {\n"  +
                 "         alert(\"" + msg.GetMsgDesc( 62, nLangID )+ "\")\n" +
                 "         return } \n" +
                 "       if ( confirm( \""+msg.GetMsgDesc(11,nLangID)+ "\")){\n" +
                 "         submit()\n" +
                 "       } } } }\n");

     scrdata.add("    function call_form( cid, crid )  {  \n"+                
                 "      top.mid_frame.location.href = '/JOrder/servlets/ClnItmRtFrame?pvMode=U&pnClientID=' + cid + '&pnClientItemRateID='+crid + '&pvWhereClause="+pvWhereClause+"' }\n");
    }
    else if( pvMode!=null && pvMode.equalsIgnoreCase("Q"))
    {
     scrdata.add(" function submit_form( pvAction ) {       	                  \n"+
                 "    with( this.right_frame.document.forms[0] ) {    	          \n"+
                 "      vAction.value = pvAction                                  \n"+
                 "      if ( nFk_Client_ID.selectedIndex == 0 ) {                 \n"+
                 "	  alert ( \""+ msg.GetMsgDesc( 16, nLangID )+"\"); nFk_Client_ID.focus(); return \n"+
                 "      }    					                  \n"+  
                 "      if ( confirm(\""+msg.GetMsgDesc( 15,nLangID)+"\" ))       \n"+
                 "        submit()                                                \n"+
                 "    }                                                           \n"+
                 " }\n");
    }
    else if( pvMode!=null && pvMode.equalsIgnoreCase("U") )
    {
      scrdata.add("  function submit_form()  {                        	          \n"+
                  "    var aField = new Array()                                   \n"+
                  "    with( this.right_frame.document.forms[0] ) {               \n"+
                  "      nInActive[0].value = \"\"                                \n"+
                  "	 aField = new Array( \""+ vFromDate +"\", dFrom_Dt.value, \""+ vToDate +"\", dTo_Dt.value, " +
                                       " \""+ vMinQty   +"\", nMinQty.value, \"" + vMaxQty +"\", nMaxQty.value,    " +
                                       " \""+ vUnitPrice+"\", nUnitPrice.value )  \n"+
                  "	 vErrMsg = top.check_fields( aField )                     \n"+
                  "	 if ( vErrMsg != \"\" ) {                                 \n"+
                  "	   vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID ) +"\" + vErrMsg + \".\" \n"+
                  "	   alert( vErrMsg ); return                               \n"+
                  "	 }                                                        \n"+
                  "      else {                                                   \n"+
                  "	   if ( confirm( \""+ msg.GetMsgDesc( 10, nLangID ) +"\" ) ) {  \n"+
                  "	     nInActive[0].value += ( nInActive[1].checked ? \"1\" : \"0\" ); submit() \n"+
                  "	   }                                                      \n"+
                  "	 }                                                        \n"+
                  "    }						          \n"+
                  "  }\n");

    }
    else if( pvMode!=null && pvMode.equalsIgnoreCase("N") )
    {
      scrdata.add( "   function submit_form()   {                       	     \n"+
                   "     var aField = new Array()                                    \n"+
                   "     var nRowCount = 0 	                                     \n"+
                   "     with( this.right_frame.document.forms[0] ) {          	     \n"+
                   "	   for ( var i=0; i<dFrom_Dt.length; i++ ) {                 \n"+
                   " 	     aField = new Array( \""+ vItemName +"\", ( nFk_ClientItem_ID[i].selectedIndex > 0 ? \"1\" : \"\" ), "+
                                         " \""+ vFromDate +"\", dFrom_Dt[i].value, \""+ vToDate +"\", dTo_Dt[i].value, " +
                                         " \""+ vMinQty   +"\", nMinQty[i].value, \""+ vMaxQty +"\", nMaxQty[i].value, " +
                                         " \""+ vUnitPrice+"\", nUnitPrice[i].value )\n"+
                   "  	     vErrMsg = top.check_fields( aField )                    \n"+
                   "	     if ( vErrMsg == \"\" ) continue                         \n"+
                   "	     if ( top.isRowBlank( vErrMsg, 6 ) ) {                   \n"+
                   "	       nRowCount++; continue                                 \n"+
                   "	     }                                                       \n"+
                   "	     else {                                                  \n"+
                   "	       vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID ) +"\" + vErrMsg + \".\" \n"+
                   "	       alert( vErrMsg ); nFk_ClientItem_ID[i].focus(); nFk_ClientItem_ID[i].select(); return \n"+
                   "	     }                                                       \n"+
                   "       }      						     \n"+
                   "	   if ( nRowCount == dFrom_Dt.length ) {                     \n"+
                   "	     alert( \""+ msg.GetMsgDesc(17,nLangID) + "\" ); nFk_ClientItem_ID[0].focus(); nFk_ClientItem_ID[0].select(); return \n"+
                   "       }  					                     \n"+
                   "       nInActive[0].value = \"\"                                 \n"+
                   "       for( var i=1; i<nInActive.length; i++ )                   \n"+
                   "	     nInActive[0].value += ( nInActive[i].checked ? \"1\" : \"0\" ) + \"~\" \n"+
                   "	   if ( confirm( \""+ msg.GetMsgDesc( 10, nLangID )+ "\" ) ) { \n"+
                   "         submit()			                             \n"+
                   "       }				                             \n"+
                   "     }				                             \n"+
                   "   }\n");
    }

    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

  public static synchronized Table createClnItmRtTable(String Columns, String WhereClause, String Titles)
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
          String query = " Select " + Columns + " From T_ClientItem cit, T_ClientItemRate cir, T_Item itm " + WClause;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {

            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            String cid = rs.getString(2);
            String URL = "JavaScript:top.mid_frame.call_form('"+id+"','"+cid+"')";
            Anchor anc = new Anchor( URL, "ClnItmRate # "+cid, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);

            datarow.add(td1);
            td1.setFormat(f1);
            for( int i=3; i<=count; i++)
            {
              String data = rs.getString(i);
              String align=null;
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";
              else if( i==3)
                 data = obj.getName(Integer.parseInt(data.trim()),"ITEM");
              else if( i==4 || i==5 )
                 data = IngDate.dateToStr(data);
              else if (i>=6 && i<=8)
                 align="RIGHT";
              else if( i==9 )
              {
                 if(data.equalsIgnoreCase("1")) 
                   data = "Yes";
                 else if(data.equalsIgnoreCase("0")) 
                   data = "No";
              }
              TableCol td = new TableCol( data, align, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            FormCheckBox chkbox = new FormCheckBox("nDelete", cid, null, null);
            TableCol td2 =  new TableCol( chkbox , null, null, null, null);
            datarow.add(td2);
            table.add( datarow );
         }
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

