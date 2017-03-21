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

public class Msg
{
  public static Script ShowMsgScript(HttpServletRequest request,String pvMode, String pvWhereClause , String vPID)
  {
    String nLangID=null;
    String vMsgID=null;
    String vMsgDesc=null;
    String vLangName=null;

    ConfigData config = new ConfigData();
    Message msg = new Message();

    nLangID   = Parse.GetValueFromString( vPID, "LangID" );

    vMsgID    = config.GetConfigValue( "SY_MSG", nLangID, "BL_LABEL.B_MESSAGE_MSG_ID", "Msg ID" );
    vMsgDesc  = config.GetConfigValue( "SY_MSG", nLangID, "BL_LABEL.B_MESSAGE_DESCRIPTION", "Description" );
    vLangName = config.GetConfigValue( "SY_MSG", nLangID, "BL_LABEL.B_MESSAGE_LANG", "Language" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
    {
     scrdata.add("  function submit_form( pvAction ) { \n"  +
                 "  var nIdx = 0\n"                         +
                 "  if ( pvAction == \"New\") {\n"          +
                 "  if ( confirm(\"" + msg.GetMsgDesc( 15,nLangID) + "\"))\n"  +
                    "    this.location.href = \"/JOrder/servlets/MsgFrame?pvMode=I&pnMsgID=&pvWhereClause="+pvWhereClause+"\"\n" +
                    "}\n" +
                    " with( this.right_frame.document.forms[0] ) { \n" +
                    "  nDelete[0].value=\"\"              \n"  + 
                    "   if ( pvAction == \"Delete\") {             \n" +
                    "     for( var i=1; i<nDelete.length; i++ )  { \n" +
                    "       if( nDelete[i].checked ) {             \n" +
                    "         nIdx ++                              \n" +
                    "         nDelete[0].value += nDelete[i].value + \"~\"\n" +
                    "       } }\n" +
                    "    if( nIdx == 0 ) {\n"  +
                    "      alert(\"" + msg.GetMsgDesc( 62, nLangID ) + "\")\n" +
                    "      return\n" +
                    "    }\n" +
                    "    if ( confirm( \"" + msg.GetMsgDesc(11,nLangID) + "\")){\n" +
                    "      submit()\n" +
                    "    } } } } \n"
                 );
      scrdata.add( " function call_form( pnMsgID ){ \n"+
                   "   top.mid_frame.location.href = \"/JOrder/servlets/MsgFrame?pvMode=U&pnMsgID=pnMsgID&pvWhereClause="+pvWhereClause+"\" \n" +
                   " } \n ");
    }
    else if( pvMode!=null && pvMode.equalsIgnoreCase("Q"))
    {
      scrdata.add( " function submit_form( pvAction )  { \n"+
                   "   with( this.right_frame.document.forms[0] ) { \n"+
                   "     vAction.value = pvAction     \n"+
                   "     if ( confirm( \"" + msg.GetMsgDesc( 15, nLangID ) + "\" )  ) \n"+
                   "       submit()  \n"+
                   "   } \n"+
                   " } \n");
    }
    else if (pvMode!=null && (pvMode.equalsIgnoreCase("I") || pvMode.equalsIgnoreCase("U")))
    {
      scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "     var aField           		\n" +
                  "     var vErrMsg          		\n" +
                  "     with( this.right_frame.document.forms[0] ){\n" +
                  "   	  vAction.value = pvAction\n" +
                  "       var nMsgID  = nMsg_ID.value  \n"+
                  "       var nLangID = ( nFk_Lang_ID.type == \"select-one\" ? nFk_Lang_ID.options[nFk_Lang_ID.selectedIndex].value : nFk_Lang_ID.value ) \n"+
                  "    	  aField = new Array( \""+ vMsgID + "\", nMsgID, \"" + vLangName + "\",nLangID,\""+vMsgDesc+"\",vMsg_Desc.value)\n" +
                  "   	  vErrMsg = top.check_fields( aField ) \n" +
                  "   	  if ( vErrMsg == \"\" ) {             \n" +
                  "   	    if ( ( pvAction == \"SaveInsert\" || pvAction == \"Insert\" || pvAction == \"Update\" ) && confirm( \"" + msg.GetMsgDesc( 10, nLangID ) + "\"))\n" +
                  "   	      submit() }\n" +
                  "   	  else {\n" +
                  "         vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                  "   	    alert( vErrMsg )\n" +
                  "   	    return          \n " +
                  "   }	} }  \n"
                 );
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

  public static synchronized Table createMsgTable(String Columns, String WhereClause, String Titles)
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
          String query = " Select " + Columns + " From T_Msg " + WClause;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            TableCol td2 = null;
            String id = rs.getString(1);
            String URL = "/JOrder/servlets/MsgFrame?pvMode=U&pnMsgID="+id+"&pvWhereClause="+URLEncoder.encode(WClause);
            Anchor anc = new Anchor( URL, "Msg # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            for( int i=2; i<=count; i++)
            { 
              String data = rs.getString(i);
              if( i == 2 )
                data = obj.getDesc( Integer.parseInt(data), "Lang");
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
            td2 =  new TableCol( chkbox , "center", null, null, null);
            datarow.add(td2);
            table.add( datarow );
         }
      }catch(Exception sexe){}
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
      return table; 
  }
}

