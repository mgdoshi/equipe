import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ParamForm
{
  public static Script ShowParamScript(HttpServletRequest request,String pvMode, String pnParamID , String vPID)
  {
    String nLangID=null;
    String nUserID=null;
    String vParamType=null;
    String vParamClass=null;
    String vParamName=null;
    String vParamValue=null;

    ConfigData config = new ConfigData();
    Message msg = new Message();

    nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    nUserID  = Parse.GetValueFromString( vPID, "UserID" );

    vParamType  = config.GetConfigValue( "SY_PARAM", nLangID, "BL_LABEL.B_PARAM_DM_PARTYPE", "Param Type" );
    vParamClass = config.GetConfigValue( "SY_PARAM", nLangID, "BL_LABEL.B_PARAM_DM_PARCLASS", "Param Class" );
    vParamName  = config.GetConfigValue( "SY_PARAM", nLangID, "BL_LABEL.B_PARAM_PARAM_NAME", "Param Name" );
    vParamValue = config.GetConfigValue( "SY_PARAM", nLangID, "BL_LABEL.B_PARAM_PARAM_VALUE", "Param Value" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
    {
     scrdata.add("  function submit_form( pvAction ) { \n"  +
                 "  var nIdx = 0\n"                         +
                 "  if ( pvAction == \"New\") {\n"          +
                 "    if ( confirm(\""+msg.GetMsgDesc( 15,nLangID)+"\" ))\n"  +
                 "       this.location.href = \"/JOrder/servlets/ParamFrame?pvMode=I&pnParamID=\" }\n" +
                 "  with( this.right_frame.document.forms[0] ) { \n" +
                 "    nDelete[0].value=\"\"              \n"  + 
                 "    if ( pvAction == \"Delete\") {             \n" +
                 "       for( var i=1; i<nDelete.length; i++ )  { \n" +
                 "         if( nDelete[i].checked ) {             \n" +
                 "           nIdx ++                              \n" +
                 "           if("+nUserID+" != 0 && cDMParClass[i-1].value == \"A\") {\n" +
                 "             alert( \"" + msg.GetMsgDesc(64,nLangID) + "\" ); return }\n" +
                 "           else                                 \n" +   
                 "             nDelete[0].value += nDelete[i].value + \"~\"\n" +
                 "         } }\n" +
                 "       if( nIdx == 0 ) {\n"  +
                 "         alert(\"" + msg.GetMsgDesc( 62, nLangID )+ "\")\n" +
                 "         return } \n" +
                 "       if ( confirm( \""+msg.GetMsgDesc(11,nLangID)+ "\")){\n" +
                 "         submit()\n" +
                 "       } } } } \n");
    }
    else if (pvMode!=null && (pvMode.equalsIgnoreCase("I") || pvMode.equalsIgnoreCase("U")))
    {
      scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "     var aField           		\n" +
                  "     var vErrMsg          		\n" +
                  "     with( this.right_frame.document.forms[0] ){\n" +
                  "       vAction.value = pvAction \n"+
                  "       if ( pvAction == \"SaveInsert\" || pvAction == \"Insert\" || pvAction == \"Update\") { \n" +                  
                  "         var vType = ( cDMType.type == \"select-one\" ? cDMType.options[cDMType.selectedIndex].value : cDMType.value ) \n" +
                  "         var vGroup = ( cDMGroup.type == \"select-one\" ? cDMGroup.options[cDMGroup.selectedIndex].value : cDMGroup.value )  \n" +
        	  "         aField = new Array( \"" + vParamType  + "\", vType, \"" + vParamClass + "\", vGroup,\"" + vParamName  + "\", vName.value, \"" + vParamValue + "\", vValue.value )\n" +
                  "   	  vErrMsg = top.check_fields( aField ) \n" +
                  "   	  if ( vErrMsg == \"\" ) {             \n" +
                  "   	    if ( confirm( \"" + msg.GetMsgDesc( 10, nLangID ) + "\"))\n" +
                  "   	      submit() }\n" +
                  "   	  else {\n" +
                  "         vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                  "   	    alert( vErrMsg )\n" +
                  "   	    return          \n " +
                  "    } } } }  \n");
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

  public static synchronized Table createParamTable(String Columns, String WhereClause, String Titles,String vPID)
  {
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";
     DBConnect obj = new DBConnect();
     Connection conn = null;
     Statement stmt = null;
     ResultSet rs = null;

     String nLangID = Parse.GetValueFromString( vPID, "LangID" );

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
          String query = "Select " + Columns + " From T_Param " + WClause;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            String URL = "/JOrder/servlets/ParamFrame?pvMode=U&pnParamID="+id;
            Anchor anc = new Anchor( URL, "Param # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            for( int i=2; i<=count; i++)
            {
              String data = rs.getString(i);
              if(i==3) 
              {
                data = Domain.getDomainDescFrmAttrib("PARCLASS",data,nLangID);  
              }
              else if (i==4)
              {
                data = Domain.getDomainDescFrmAttrib("PARTYPE",data,nLangID);
              } 
              TableCol td = new TableCol(data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
            TableCol td2 =  new TableCol( chkbox , "center", null, null, null);
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
 
}

