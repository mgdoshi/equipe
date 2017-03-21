
import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UserForm
{
  public static Script ShowUserScript(HttpServletRequest request,String pvMode, String pnEmployeeID, String vPID )
  {
    String vUserName=null;
    String vLanguage=null;
    String vClientName=null;
    String vRecSecName=null;
    String vUserPass=null;

    ConfigData config = new ConfigData();
    Message msg = new Message();

    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    vUserName   = config.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_USER_NAME", "User Name" );
    vLanguage   = config.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_FK_LANG_ID", "Lang Name" );
    vClientName = config.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_FK_CLIENT_ID", "Client Name" );
    vRecSecName = config.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_FK_RECSEC_ID", "RecSec Name" );
    vUserPass   = config.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_USER_PASS", "User PassWord" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode== null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
    {
     scrdata.add("  function submit_form( pvAction ) { \n"  +
                 "  var nIdx = 0\n"                         +
                 "  if ( pvAction == \"New\") {\n"          +
                 "  if ( confirm(\"" + msg.GetMsgDesc( 15,nLangID) + "\"))\n"  +
                    "    this.location.href = \"/JOrder/servlets/UserFrame?pvMode=I&pnUserID=\"   \n" +
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

    }
    else
    {
      scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "     var aField           		\n" +
                  "     var vErrMsg          		\n" +
                  "     var FuncWindow 		 	\n" +
                  "     with( this.right_frame.document.forms[0] ){\n" +
                  "       var vURL = \"/JOrder/servlets/ShowUsrGrpWin?pnUserID=\" +pnUserID.value \n"+
                  "       if ( pvAction == \"Assign\" ) {                        	          \n"+ 
                  "         FuncWindow = window.open( vURL, \"FuncWindow\", \"menubar=0,scrollbars=1,resizable=0,width=450,height=250\") \n"+
                  "         FuncWindow.oldWindow = top; 		                          \n"+
                  "         return       			              	                  \n"+
                  "       }       			              	                          \n"+
                  "   	  vAction.value = pvAction\n" +
                  "       var nLangID = ( nFk_Lang_ID.type == \"select-one\" ?  nFk_Lang_ID.options[nFk_Lang_ID.selectedIndex].value : nFk_Lang_ID.value ) \n"+
                  "       var nClientID = ( nFk_Client_ID.type == \"select-one\" ?  nFk_Client_ID.options[nFk_Client_ID.selectedIndex].value : nFk_Client_ID.value ) \n"+
                  "       var nRecSecID = ( nFk_RecSec_ID.type == \"select-one\" ?  nFk_RecSec_ID.options[nFk_RecSec_ID.selectedIndex].value : nFk_RecSec_ID.value ) \n"+
                  "       if ( vUser_Pass.value != vUser_ConfPass.value) {                          \n"+
                  "         alert( \""+ msg.GetMsgDesc( 31, nLangID) +"\")                          \n"+
                  "	    vUser_ConfPass.focus(); vUser_ConfPass.select(); return }               \n"+
                  "    	  aField = new Array( \""+ vUserName + "\", vUser_Name.value,\""+ vUserPass + "\", vUser_Pass.value, \""+ vLanguage + "\", nLangID, \"" + vRecSecName + "\", nRecSecID, \""+ vClientName + "\", nClientID)\n" +
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

  public static synchronized Table createUserTable(String Columns, String WhereClause, String Titles)
  {
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";

     String usrname = null;

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
          String query = " Select " + Columns + " From T_User "+ WClause;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            TableCol td2 = null;
            String id = rs.getString(1);
            String URL = "/JOrder/servlets/UserFrame?pvMode=U&pnUserID="+id;
            Anchor anc = new Anchor( URL, "User # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            for( int i=2; i<=count; i++)
            {
              String data =  rs.getString(i);
              if( i == 2)
                usrname = data;
              if( i==3 )
              {
                data = obj.getDesc( Integer.parseInt(data), "Lang" );
              }
              if( i==4 )
              {
                data = obj.getName( Integer.parseInt(data), "Client" );
              }
              if( i==5 )
              {
                data = obj.getName( Integer.parseInt(data), "RecSec" );
              }
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            if( usrname.equals("ADMIN") )
              td2 =  new TableCol( "&nbsp;" , null, null, null, null);
            else
            {
              FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
              td2 =  new TableCol( chkbox ,"center", null, null, null);
            }
            datarow.add(td2);
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

