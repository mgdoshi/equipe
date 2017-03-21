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

public class Config
{
  public static Script ShowConfigScript( HttpServletRequest request, String pvMode , String vPID)
  {
    String vParentObj;
    String vLangName;
    String vObjName;
    String vObjProperty;
    String vPropertyValue;

    ConfigData config = new ConfigData();
    Message msg = new Message();

    String nLangID = Parse.GetValueFromString( vPID, "LangID" );
    vParentObj     = config.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_PARENT_OBJ", "Parent Object" );
    vLangName      = config.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_FK_LANG_ID", "Language Name" );
    vObjName       = config.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_OBJ_NAME", "Object Name" );
    vObjProperty   = config.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_OBJ_PROPERTY", "Object Property" );
    vPropertyValue = config.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_PROPERTY_VALUE", "Property Value" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
    {
     scrdata.add("  function submit_form( pvAction ) { \n"  +
                 "  var nIdx = 0\n"                         +
                 "  if ( pvAction == \"New\") {\n"          +
                 "  if ( confirm(\"" + msg.GetMsgDesc( 15,nLangID) + "\"))\n"  +
                    "    this.location.href = \"/JOrder/servlets/ConfigFrame?pvMode=I&pnConfigID=&pvParentObj=&pnLangID=\"\n" +
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
    else
    {
      scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "     var aField           		\n" +
                  "     var vErrMsg          		\n" +
                  "     with( this.right_frame.document.forms[0] ){\n" +
                  "   	  vAction.value = pvAction\n" +
                  "       var vLang = ( nFk_Lang_ID.type == \"select-one\" ? nFk_Lang_ID.options[nFk_Lang_ID.selectedIndex].value : nFk_Lang_ID.value ) \n"+
                  "    	  aField = new Array( \""+ vParentObj + "\", vParent_Obj.value, \"" + vLangName + "\",vLang,\"" + vObjName + "\",vObj_Name.value,\"" + vObjProperty + "\", vObj_Property.value,\""+vPropertyValue+"\", vProperty_Value.value)\n" +
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

  public static synchronized Table createConfigTable(String Columns, String WhereClause, String Titles)
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
          String query = " Select " + Columns + " From T_Config " + WClause;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            TableCol td2 = null;
            String id = rs.getString(1);
            String URL = "/JOrder/servlets/ConfigFrame?pvMode=U&pnConfigID="+id+"&pvParentObj=&pnLangID=";
            Anchor anc = new Anchor( URL, "Config # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            for( int i=2; i<=count; i++)
            { 
              String data = rs.getString(i);
              if( i ==  3)
                data = obj.getDesc( Integer.parseInt(data), "Lang");
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
            td2 =  new TableCol( chkbox , null, null, null, null);
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

