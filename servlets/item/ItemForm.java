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

public class ItemForm
{
  public static Script ShowItemScript(HttpServletRequest request, String pvMode, String vPID, String pvWhereClause )
  {
    String nLangID=null;
    ConfigData cdata = new ConfigData();
    Message msg = new Message();
    nLangID   = Parse.GetValueFromString( vPID, "LangID" );

    String ItemName = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_ITEM_NAME", "Item Name" );
    String UnitType = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_LABEL.B_ITEM_DM_UNITTYPE", "Unit Type" );
    
    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
    {
     scrdata.add("  function submit_form( pvAction ) { \n"  +
                 "  var nIdx = 0\n"                         +
                 "  if ( pvAction == \"New\") {\n"          +
                 "    if ( confirm(\""+msg.GetMsgDesc( 15,nLangID)+"\" ))\n"  +
                 "     this.location.href = \"/JOrder/servlets/ItemFrame?pvMode=I&pnItemID=&pvWhereClause="+pvWhereClause+"\" }\n" +
                 "  with( this.right_frame.document.forms[0] ) { \n" +
                 "    nDelete[0].value=\"\"              \n"  + 
                 "    if ( pvAction == \"Delete\") {             \n" +
                 "       for( var i=1; i<nDelete.length; i++ )  { \n" +
                 "         if( nDelete[i].checked ) {             \n" +
                 "           nIdx ++                              \n" +
                 "             nDelete[0].value += nDelete[i].value + \"~\"\n" +
                 "         } }\n" +
                 "       if( nIdx == 0 ) {\n"  +
                 "         alert(\"" + msg.GetMsgDesc( 62, nLangID )+ "\")\n" +
                 "         return } \n" +
                 "       if ( confirm( \""+msg.GetMsgDesc(11,nLangID)+ "\")){\n" +
                 "         submit()\n" +
                 "       } } } } \n\n"+

                  "  function Assign( pvAction, pnItemID )             \n"+
                  "  {                                                 \n"+
                  "	if ( pvAction == \"Assign1\" ) {               \n"+
                  "       vURL = \"/JOrder/servlets/ShowAgnItmRtForm?pnItemID=\"+ pnItemID \n"+
                  "       ItemRateWindow1 = window.open( vURL, \"ItemRateWindow1\", \"menubar=0,scrollbars=1,resizable=0,width=600,height=325\")\n"+
                  "       ItemRateWindow1.oldWindow = top; return                         \n"+       
                  "     }       			              	                  \n"+           
                  "	if ( pvAction == \"Assign3\" ) {                        	  \n"+            
                  "       vURL = \"/JOrder/servlets/ShowAgnClnsForm?pnItemID=\"+ pnItemID \n"+
                  "       ClientItemWindow = window.open( vURL, \"ClientItemWindow\", \"menubar=0,scrollbars=1,resizable=0,width=450,height=300\") \n"+
                  "       ClientItemWindow.oldWindow = top; return                              \n"+
                  "      }    \n"+   			              	                             
                  " }    \n\n"+

                  "  function call_form( pnItemID )  {    	        \n"+
                  "    top.mid_frame.location.href = \"/JOrder/servlets/ItemFrame?pvMode=U&pnItemID=\"+pnItemID+\"&pvWhereClause="+pvWhereClause+"\" \n"+
                  "  } \n\n");

    }
    else if( pvMode!=null && pvMode.equalsIgnoreCase("Q"))
    {
     scrdata.add(" function submit_form( pvAction ) {             \n"+
                 "   with( this.right_frame.document.forms[0] ) { \n"+
                 "     vAction.value = pvAction                   \n"+
                 "     if ( confirm( \""+ msg.GetMsgDesc( 15, nLangID ) + "\" ) ) \n"+
                 "	 submit()                                                 \n"+
                 "   }	      					                  \n"+
                 "   }  \n");
    }
    else if( pvMode!=null && ( pvMode.equalsIgnoreCase("I") || pvMode.equalsIgnoreCase("U") ) )
    {
      scrdata.add(" function submit_form( pvAction )  {                                   \n" +
                  "     var aField           		                                  \n" +
                  "     var vErrMsg          		                                  \n" +
                  "     with( this.right_frame.document.forms[0] ){                       \n" +
                  "       var vURL                                                        \n" +
                  "       var ItemRateWindow1  		                                  \n" +
                  "       var ItemRateWindow2  		                                  \n" +
                  "       var ClientItemWindow  		                          \n" +
                  "       if ( pvAction == \"Assign1\" ) {                                \n" +
                  "         vURL = \"/JOrder/servlets/ShowItmRtEntry\"                    \n" +
                  "         ItemRateWindow1 = window.open( vURL, \"ItemRateWindow1\", \"menubar=0,scrollbars=1,resizable=0,width=600,height=325\") \n" +
                  "         ItemRateWindow1.oldWindow = top; return                       \n" +
                  "       }       			              	                  \n" +
                  "	  if ( pvAction == \"Assign2\" ) {                        	  \n" +
                  "         vURL = \"/JOrder/servlets/ShowItmRtTable?pnItemID=\"+ pnItemID.value \n" +
                  "         ItemRateWindow2 = window.open( vURL, \"ItemRateWindow2\", \"menubar=0,scrollbars=1,resizable=0,width=550,height=300\") \n" +
                  "         ItemRateWindow2.oldWindow = top; return                       \n" +
                  "       }       			              	                  \n" +
                  "	  if ( pvAction == \"Assign3\" ) {                        	  \n" +
                  "         vURL = \"/JOrder/servlets/ShowClnItmForm?pnItemID=\"+ pnItemID.value \n" +
                  "         if( vAssignAllCln[1].checked ) {                        	  \n" +
                  "           alert( \"Item is already assigned to all clients.Please UnCheck it and then Proceed\" );return \n" +
                  "	    }                                                             \n" +
                  "         else {                                                        \n" +
                  "           ClientItemWindow = window.open( vURL, \"ClientItemWindow\", \"menubar=0,scrollbars=1,resizable=0,width=400,height=250\") \n" +
                  "           ClientItemWindow.oldWindow = top; return                    \n" +
                  "	      }                                                           \n" +
                  "       }       			              	                  \n" +
                  "       if ( pvAction == \"Assign4\" ) {                        	  \n" +
                  "         vURL = \"/JOrder/servlets/ShowUsrItmForm?pnItemID=\"+ pnItemID.value \n" +
                  "         if( vAssignAllUser[1].checked ) {                      	  \n" +
                  "            alert( \"Item is already assigned to all users.Please UnCheck it and then Proceed\" );return \n" +
                  "	      }                                                           \n" +
                  "         else {                                                        \n" +
                  "           UserItemWindow = window.open( vURL, \"UserItemWindow\", \"menubar=0,scrollbars=1,resizable=0,width=400,height=250\") \n" +
                  "           UserItemWindow.oldWindow = top; return                      \n" +
                  "	      }                                                           \n" +
                  "       }       			              	                  \n" +
                  "	  vAction.value = pvAction                                        \n" +
                  "	  if ( pvAction == \"SaveInsert\" || pvAction == \"Insert\" || pvAction == \"Update\" ) {\n" +
               	  "          aField = new Array( \""+ItemName+"\", vItem_Name.value, \""+UnitType+"\", cDM_UnitType.type == \"select-one\" ? cDM_UnitType.options[cDM_UnitType.selectedIndex].value : cDM_UnitType.value ) \n"+
              	  "          vErrMsg = top.check_fields( aField )                        \n"+
              	  "          if ( vErrMsg != \"\" ) {                          	        \n"+
              	  "            vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg + \".\" \n"+
              	  "            alert( vErrMsg ); return                                 \n"+
              	  "          }                                                          \n"+
                  "	    else {                                                        \n" +
                  "	      if ( confirm( \""+ msg.GetMsgDesc( 10, nLangID )+"\" ) ) {  \n" +
                  "             if( vAssignAllCln[1].checked )                               \n" +
                  "                vAssignAllCln[0].value=\"All\"                            \n" +
                  "             else                                                      \n" +
                  "                vAssignAllCln[0].value =\"None\"                          \n" +
                  "	        vAssignAllUser[0].value = ( vAssignAllUser[1].checked ? \"All\" : \"None\" )\n" +
                  "	        nInActive[0].value = ( nInActive[1].checked ? \"1\" : \"0\" );submit()\n" +
                  "	      }                                                           \n" +
                  "	    }                                                             \n" +
                  "       }      						          \n" +
                  "     }      						                  \n" +
                  "   } \n");
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

  public static synchronized Table createItemTable(String Columns, String WhereClause, String Titles, String vPID )
  {
     String nLangID=null;
     String nUserID=null;
     String WClause=""; 
     String vBPLate="";
     Anchor vAssignCln=null;
     Anchor vAssignRt=null;
      
     ConfigData cdata = new ConfigData();
     Message msg = new Message();
     WebUtil util = new WebUtil();
 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";

     nUserID   = Parse.GetValueFromString( vPID, "UserID" );
     nLangID   = Parse.GetValueFromString( vPID, "LangID" );

     vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_ASSIGN", "Assign Item Rates" );
     vAssignRt = util.createLink( nUserID, "ST_ITEM", "AssignRate", "I", "JavaScript:parent.submit_form('Assign1')", "NITMRT", vBPLate, msg.GetMsgDesc( 113, nLangID ));
     vBPLate = cdata.GetConfigValue( "ST_ITEM", nLangID, "BL_ITEM.BU_ASSIGN1", "Assign Clients to Item" );
     vAssignCln = util.createLink( nUserID, "ST_ITEM", "AssignCln", "I", "JavaScript:parent.submit_form('Assign3')", "ASSCLNT", vBPLate, msg.GetMsgDesc( 105, nLangID ));

     if( WhereClause != null )
       WClause = WhereClause;

     DBConnect obj = new DBConnect();
     Connection conn = null;
     Statement stmt = null;
     ResultSet rs = null;

     Table table = new Table( null, "Center", Default);
     Font f = new Font("white", "Arial", "3",null);
     Font f1 = new Font(null, "Arial", "3",null);
     TableRow headrow = new TableRow("left", null, null);
     TableHeader th = null;
     TableRow datarow = null;
     int count = 0;

     try
      {
          if( Titles != null )
          {
            String []tokens = Parse.parse( Titles, ",");
            count = tokens.length;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol );
              th.setFormat(f);
              headrow.add(th);
            }
            if( vAssignRt!=null )
            {
              th = new TableHeader( "Assign ItemRates", null, null, null, DefHeadCol );
              th.setFormat(f);
              headrow.add(th);
            }
            if( vAssignCln!=null )
            {
              th = new TableHeader( "Assign Clients", null, null, null, DefHeadCol );
              th.setFormat(f);
              headrow.add(th);
            }
            th = new TableHeader( "Delete", null, null, null, DefHeadCol );
            th.setFormat(f);
            headrow.add(th);
            table.add( headrow );
          }
          else
          {
            String []tokens = Parse.parse( Columns, ",");
            count = tokens.length;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol);
              th.setFormat(f);
              headrow.add(th);
            }
            if( vAssignRt!=null )
            {
              th = new TableHeader( "Assign ItemRates", null, null, null, DefHeadCol );
              th.setFormat(f);
              headrow.add(th);
            }
            if( vAssignCln!=null )
            {
              th = new TableHeader( "Assign Clients", null, null, null, DefHeadCol );
              th.setFormat(f);
              headrow.add(th);
            }
            th = new TableHeader( "Delete", null, null, null, DefHeadCol );
            th.setFormat(f);
            headrow.add(th);
            table.add( headrow );
          }

          conn = obj.GetDBConnection();
          stmt = conn.createStatement();
          String query = " Select " + Columns + " From T_Item itm, T_UserItem uit " + WClause;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            String URL = "JavaScript:top.mid_frame.call_form('"+id+"')";
            Anchor anc = new Anchor( URL, "Item # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            for( int i=2; i<=count; i++)
            {
              String data = rs.getString(i);
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";                 
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            if( vAssignRt!=null )
            {
              URL = "JavaScript:top.mid_frame.Assign( 'Assign1', '"+id+"')";
              Anchor anc1 = new Anchor( URL, new Img( "/ordimg/BP_Button/NITMRT1.gif", null, "Assign ItemRates", null, "BORDER=\"0\" NAME=\"Insert\"" ), null, null, null);
              TableCol td3 =  new TableCol( anc1, "Center", null, null, null);
              datarow.add(td3);
            }
            if( vAssignCln!=null )
            {
              URL = "JavaScript:top.mid_frame.Assign( 'Assign3', '"+id+"')";
              Anchor anc2 = new Anchor( URL, new Img( "/ordimg/BP_Button/ASSCLNT1.gif", null, "Assign Clients", null, "BORDER=\"0\" NAME=\"Insert\"" ), null, null, null);
              TableCol td4 =  new TableCol( anc2, "Center", null, null, null);
              datarow.add(td4);
            }
            FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
            TableCol td2 =  new TableCol( chkbox , "Center", null, null, null);
            datarow.add(td2);
            table.add( datarow );
         }
      }catch(Exception sexe){}
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

  public static synchronized Table createItmRtTable(String Columns, String WhereClause, String Titles)
  {
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";
     DBConnect obj = new DBConnect();
     Connection conn = null;
     Statement stmt = null;
     ResultSet rs = null;

     if( WhereClause != null )
       WClause = WhereClause;

     Table table = new Table( null, "Center", Default);
     Font f = new Font("white", "Arial", "3",null);
     Font f1 = new Font(null, "Arial", "3",null);
     TableRow headrow = new TableRow("left", null, null);
     TableHeader th = null;
     TableRow datarow = null;
     int count = 0;

     try
      {
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

          conn = obj.GetDBConnection();
          stmt = conn.createStatement();
          String query = " Select " + Columns + " From T_ItemRate " + WClause;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            String URL = "JavaScript:call_form('"+id+"')";
            Anchor anc = new Anchor( URL, "ItemRate # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            for( int i=2; i<=count; i++)
            {
              String data = rs.getString(i);
              if( i==2 || i==3)
              {
                 data = IngDate.dateToStr( data );
              }
              if( i==7)
              {
                 if( data.equalsIgnoreCase("0") )
                   data = "No";
                 else if( data.equalsIgnoreCase("1") )
                   data = "Yes";
              } 
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";                 
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
            TableCol td2 =  new TableCol( chkbox , "Center", null, null, null);
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