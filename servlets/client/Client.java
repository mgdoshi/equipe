
import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Client
{
  public static Script ShowClientScript(HttpServletRequest request,String pvMode, String pnClientID, String vPID )
  {

    int nLangID=0;
    String vClientName;
    String vClientCode;
    ConfigData config = new ConfigData();
    Message msg = new Message();

    nLangID  = Integer.parseInt(Parse.GetValueFromString( vPID, "LangID" ));

    vClientCode = config.GetConfigValue( "ST_CLIENT", nLangID, "BL_LABEL.B_CLIENT_CLIENT_CODE", "Client Code" );
    vClientName = config.GetConfigValue( "ST_CLIENT", nLangID, "BL_LABEL.B_CLIENT_CLIENT_NAME", "Client Name" );
    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
    {
     scrdata.add("  function submit_form( pvAction ) { \n"  +
                 "  var nIdx = 0\n"                         +
                 "  if ( pvAction == \"New\") {\n"          +
                 "  if ( confirm(\"" + msg.GetMsgDesc( 15,nLangID) + "\"))\n"  +
                    "    this.location.href = \"/JOrder/servlets/ClientFrame?pvMode=I&pnClientID=\"   \n" +
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
                  "     if ( pvAction == \"Create\" ) { \n" +
                  "          var vURL = \"/JOrder/servlets/ClientInsAddress\"\n" +
                  "          FuncWindow = window.open( vURL, \"FuncWindow\", \"menubar=0,scrollbars=1,resizable=0,width=600,height=400\")\n" +
                  "          FuncWindow.oldWindow = top;\n"  +
                  "          return }      		\n"  +
                  "     if ( pvAction == \"Address\" ) {\n " +
                  "       var vURL = \"/JOrder/servlets/ClientAddrTable?pnClientID=" + pnClientID + "\"\n" +
                  "       FuncWindow = window.open( vURL, \"FuncWindow\", \"menubar=0,scrollbars=1,resizable=0,width=600,height=400\")\n" +
                  "       FuncWindow.topWindow = top;\n" +
                  "       return  }\n"  +
                  "     with( this.right_frame.document.forms[0] ){\n" +
                  "   	  vAction.value = pvAction\n" +
                  "    	  aField = new Array( \""+ vClientCode + "\", vClient_Code.value, \"" + vClientName + "\",vClient_Name.value)\n" +
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

  public static synchronized Table createClientTable(String Columns, String WhereClause, String Titles, String vPID)
  {
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";
     String clnname = null;
     String nLangID  = Parse.GetValueFromString( vPID, "LangID");


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

          DBConnect obj = new DBConnect();
          Connection conn = obj.GetDBConnection();
          Statement stmt = conn.createStatement();
          String query = " Select " + Columns + " From T_Client cln, T_UserClient ucl " +
                         " WHERE cln.Client_ID = ucl.FK_Client_ID " + 
                         " UNION " +
                         " Select " + Columns + " From T_Client cln, T_User usr " +
                         " WHERE NOT EXISTS ( Select 1 "+
                         "                    From T_UserClient ucl " + 
                         "                    Where ucl.FK_User_ID = usr.User_ID ) ";  

          ResultSet rs = stmt.executeQuery(query);

          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            TableCol td2 = null;
            String id = rs.getString(1);
            String URL = "/JOrder/servlets/ClientFrame?pvMode=U&pnClientID="+id;
            Anchor anc = new Anchor( URL, "Client # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            for( int i=2; i<=count; i++)
            {
              String data =  rs.getString(i);
              if( i == 4)
                clnname = data;
              if( i==3 )
                 data = Domain.getDomainDescFrmAttrib("CLNTYPE",data, nLangID);
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            if( clnname.equals("INTERNAL") )
              td2 =  new TableCol( "&nbsp;" , null, null, null, null);
            else
            {
              FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
              td2 =  new TableCol( chkbox , null, null, null, null);
            }
            datarow.add(td2);
            table.add( datarow );
         }
         stmt.close();
         conn.close();
         return table; 
      }catch(Exception sexe){System.out.println(sexe);}  
      return null;  
  }

  public static synchronized Table createClientAddrTable(HttpServletRequest request, String Columns, String WhereClause, String Titles,String vPID)
  {
   
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";
     String nLangID  = Parse.GetValueFromString( vPID, "LangID");

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
          DBConnect obj = new DBConnect();
          Connection conn = obj.GetDBConnection();
          Statement stmt = conn.createStatement();
          String query = "Select " + Columns + " From T_Address adr, T_AddressRef adf " + WClause;
          ResultSet rs = stmt.executeQuery(query);

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
            count = tokens.length-2;
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
            String refid = rs.getString(6);
            String URL = "/JOrder/servlets/ClientUpdAddress?pnAddressID="+id+"&pnRefID="+refid;
            Anchor anc = new Anchor( URL, "Address # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            String addtype = Domain.getDomainDescFrmAttrib("ADDTYPE",rs.getString(2), nLangID);
            TableCol td3 = new TableCol( addtype, null, null, null, null);
            datarow.add(td3);
            td3.setFormat(f1);
            for( int i=3; i<=count; i++)
            {
              String data = rs.getString(i);
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";                 
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
            TableCol td2 =  new TableCol( chkbox , null, null, null, null);
            datarow.add(td2);
            table.add( datarow );
         }
         stmt.close();
         conn.close();
         return table; 
      }catch(Exception sexe){System.out.println(sexe);}  
      return null;  
  }

}

