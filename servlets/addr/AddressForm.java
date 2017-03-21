import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AddressForm
{
  public static Script ShowAddressScript(HttpServletRequest request, String pvMode, String pvRefType, String pnRefID, String pnAddressID, String vPID )
  {
    ConfigData config = new ConfigData();
    Message msg = new Message();

    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );

    String vAddType = config.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_DM_ADDTYPE", "Address Type" );
    String vCity    = config.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_CITY", "City" );
    String vState   = config.GetConfigValue( "ST_ADDRESS", nLangID, "BL_LABEL.B_ADDRESS_STATE", "State" );
    String vClientName = config.GetConfigValue( "ST_CLIENT", nLangID, "BL_LABEL.B_CLIENT_CLIENT_NAME", "Client Name" );
    String vBranchName = config.GetConfigValue( "ST_BRANCH", nLangID, "BL_LABEL.B_BRANCH_BRANCH_NAME", "Branch Name" );
    String vEmpName    = config.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_EMPLOYEE_NAME", "Employee Name" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("D") )
    {
      scrdata.add(" function GetRefType() {                                                   \n"+
                  "    var pValue                                                             \n"+
                  "    var aField                                                             \n"+
                  "    var vErrMsg                                                            \n"+
                  "     with( this.right_frame.document.forms[0] ){                           \n"+
                  "      for( var i=0; i< vRefType.length; i++ ) {                            \n"+
                  "        if(vRefType[i].checked)    {                                       \n"+
                  "           pValue = vRefType[i].value                                      \n"+
                  "        }                                                                  \n"+
                  "     }                                                                     \n"+
                  "     if( pValue == \"Client\" )   {                                        \n"+
                  " 	  aField = new Array( \""+ vClientName+"\",  pvClientName.options[pvClientName.selectedIndex].value  ) \n"+
                  "	  vErrMsg = top.check_fields( aField )                                \n"+
                  "	  if ( vErrMsg == \"\" ) {                         	              \n"+
                  "         if ( confirm( \""+ msg.GetMsgDesc( 15, nLangID ) + "\" ) )submit()\n"+
                  "       }                                                                   \n"+
                  "	  else {                                                              \n"+
                  "	    vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID ) +"\" + vErrMsg +\".\" \n"+
                  "         alert( vErrMsg )                                                  \n"+
                  "         return                                                            \n"+
                  "	  }                                                                   \n"+
                  "    }                                                                      \n"+
                  "    if( pValue == \"Branch\" )  {                                          \n"+
                  " 	 aField = new Array( \""+ vBranchName +"\",  pvBranchName.options[pvBranchName.selectedIndex].value  ) \n"+
                  "	 vErrMsg = top.check_fields( aField )                                 \n"+
                  "	 if ( vErrMsg == \"\" ) {                          	              \n"+
                  "        if ( confirm( \"" + msg.GetMsgDesc( 15, nLangID )+ "\" ) ) submit()\n"+
                  "      }                                                                    \n"+
                  "	 else {                                                               \n"+
                  "        vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg + \".\"  \n"+
                  "	   alert( vErrMsg )                                                   \n"+
                  "	   return                                                             \n"+
                  "	 }                                                                    \n"+
                  "   }                                                                       \n"+
                  "   if( pValue == \"Employee\" )   {                                        \n"+
                  "     aField = new Array( \""+ vEmpName +"\",  pvEmpName.options[pvEmpName.selectedIndex].value  ) \n"+
                  "	vErrMsg = top.check_fields( aField )                                  \n"+
                  "	if ( vErrMsg == \"\" ) {                          	              \n"+
                  "       if ( confirm( \""+ msg.GetMsgDesc( 15, nLangID ) + "\" ) ) submit() \n"+
                  "     }                                                                     \n"+
                  "     else {                                                                \n"+
                  "	  vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID ) +"\" + vErrMsg + \".\"  \n"+
                  "	  alert( vErrMsg )                                                    \n"+
                  "       return                                                              \n"+
                  "     }                                                                     \n"+
                  "   }                                                                       \n"+
                  "  }                                                                        \n"+
                  "}  \n\n");   
    }
    else if( pvMode!=null && pvMode.equalsIgnoreCase("N")  )
    {
     scrdata.add("function submit_form( pvAction ) { \n"  +
                 "  var nIdx = 0\n"                         +
                 "  if ( pvAction == \"New\") {\n"          +
                 "    if ( confirm(\"" + msg.GetMsgDesc( 15,nLangID) + "\"))\n"  +
                 "      this.location.href = \"/JOrder/servlets/AddressFrame?pvMode=I&pvRefType="+pvRefType+"&pnRefID="+pnRefID+"&pnAddressID="+pnAddressID+" \"\n" +
                 "  }\n" +
                 "  with( this.right_frame.document.forms[0] ) { \n" +
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
                 "    } } } } \n");
    }
    else
    {
      scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "     var aField           		\n" +
                  "     var vErrMsg          		\n" +
                  "     with( this.right_frame.document.forms[0] ){\n" +
                  "   	  vAction.value = pvAction\n" +
                  "       var cDMAddType = ( cDM_AddType.type == \"select-one\" ? cDM_AddType.options[cDM_AddType.selectedIndex].value : cDM_AddType.value ) \n"+
                  "    	  aField = new Array( \"" + vAddType + "\", cDMAddType, \""+ vCity + "\", vCity.value,\"" + vState +"\", vState.value )\n" +
                  "   	  vErrMsg = top.check_fields( aField ) \n" +
                  "   	  if ( vErrMsg == \"\" ) {             \n" +
                  "   	    if ( ( pvAction == \"SaveInsert\" || pvAction == \"Insert\" || pvAction == \"Update\" ) && confirm( \"" + msg.GetMsgDesc( 10, nLangID ) + "\"))\n" +
                  "   	      submit() }\n" +
                  "   	  else {\n" +
                  "         vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                  "   	    alert( vErrMsg )\n" +
                  "   	    return          \n " +
                  "   }	} }  \n");
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

   public static synchronized Table createAddressTable( HttpServletRequest request, String Columns, String WhereClause, String Titles, String vPID)
   {
     String nLangID =null;
     nLangID  = Parse.GetValueFromString( vPID, "LangID" );
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
            String reftype = rs.getString(5);
            String refid = rs.getString(6);
            String URL = "/JOrder/servlets/AddressFrame?pvMode=U&pvRefType="+reftype+"&pnRefID="+refid+"&pnAddressID="+id;
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

