import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DomainForm
{
 
  public static synchronized Script ShowDomainScript(HttpServletRequest request,String pvMode,String pvDomain, String vPID)
  {
    String vSeqNr=null;
    String vAttrib=null;
    String vAttribDesc=null;
    String vLangName=null;   

    ConfigData config = new ConfigData();
    Message msg = new Message();

    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );

    vSeqNr      =  config.GetConfigValue( "ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_SEQNO", "Seq Nr" );
    vAttrib     =  config.GetConfigValue( "ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_ATTRIBUTE" , "Attribute" );
    vAttribDesc =  config.GetConfigValue( "ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_ATTRIB_DESC", "Attrib Desc" );
    vLangName   =  config.GetConfigValue( "ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_LANGUAGE" , "Language" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
    {
     scrdata.add("  function show_DomainForm() { \n"  +
                 "  with( this.right_frame.document.forms[0] ) {\n"+
                 "  if ( confirm(\"" + msg.GetMsgDesc( 15,nLangID) + "\"))\n"  +
                    "    this.location.href = \"/JOrder/servlets/DomainFrame?pvMode=U&pvDomain=\"+pvDomain.value\n" +
                    "} }\n");

    }
    else if (pvMode!=null && pvMode.equalsIgnoreCase("U"))
    {
      scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "     var aFormFields        		\n" +
                  "     var nIdx = 0          		\n" +
		  "     var vErrMsg          		\n" +
		  "     var nSeqNo = new Array()	\n" +
		  "     var vAttrb = new Array()	\n" +
		  "     var vAttribDesc = new Array()	\n" +
		  "     var nLangID = new Array()	\n" +
                  "     with( this.right_frame.document.forms[0] ){\n" +
                  "      vAction.value = pvAction \n"+
               	  "      for ( var i=0; i<nSequence_Nr.length; i++ ) { \n"+
                  "    	  aFormFields = new Array( \"" + vSeqNr+ "\" , nSequence_Nr[i].value, \"" + vAttrib+ "\" , vAttrib[i].value, \"" + vAttribDesc+ "\" , vAttrib_Desc[i].value, \"" + vLangName+ "\" , nFk_Lang_ID[i].options[nFk_Lang_ID[i].selectedIndex].value )\n" +
                  "   	  vErrMsg = top.check_fields( aFormFields ) \n" +
                  "       if ( top.isRowBlank( vErrMsg, 4 ) ) { nIdx++; continue; }\n" +
                  "   	  else {  if ( vErrMsg != \"\" ) {\n" +
                  "         vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \" "+ msg.GetMsgDesc( 18, nLangID ) +"\" + (i+1)\n"+
                  "   	    alert( vErrMsg )\n" +
                  "   	    nSequence_Nr[i].focus(); nSequence_Nr[i].select(); return\n " +
                  "   }	} }  \n" + 
                  "  if ( nIdx == nSequence_Nr.length ) {\n " +
             	  "    alert( \"" +msg.GetMsgDesc( 17, nLangID)+ "\"  ); nSequence_Nr[0].focus(); nSequence_Nr[0].select(); return } \n" +
         	  "  for ( var i=0; i<vAttrib.length-1; i++ ) {\n " +
             	  "    nSeqNo[i] = nSequence_Nr[i].value; vAttrb[i] = vAttrib[i].value; vAttribDesc[i] = vAttrib_Desc[i].value; nLangID[i] = nFk_Lang_ID[i].options[nFk_Lang_ID[i].selectedIndex].value\n " +
             	  "    for ( var j=i+1; j<vAttrib.length; j++ ) {\n " +
             	  "      nSeqNo[j] = nSequence_Nr[j].value; vAttrb[j] = vAttrib[j].value; vAttribDesc[j] = vAttrib_Desc[j].value; nLangID[j] = nFk_Lang_ID[j].options[nFk_Lang_ID[j].selectedIndex].value\n " +
               	  "      if ( !top.IsNull( nSeqNo[i] ) && nLangID[i] == nLangID[j] ) {\n " +
             	  "        if ( nSeqNo[i] == nSeqNo[j] ) {\n " +
             	  "           alert( \" "+ msg.GetMsgDesc(19, nLangID) + "\"  ); nSequence_Nr[j].focus(); nSequence_Nr[j].select(); return }\n " +
                  "        if ( vAttrb[i] == vAttrb[j] ) {\n " +
             	  "          alert( \" "+ msg.GetMsgDesc(20, nLangID) + "\"  ); vAttrib[j].focus(); vAttrib[j].select(); return }\n " +
             	  "        if ( vAttribDesc[i] == vAttribDesc[j] ) {\n " +
             	  "          alert( \" "+ msg.GetMsgDesc(21, nLangID) + "\"  ); vAttrib_Desc[j].focus(); vAttrib_Desc[j].select(); return } \n " +                       
                  "      }\n " +
             	  "    }  \n " +
             	  "  }    \n " +
              	  "  if ( confirm( \" "+  msg.GetMsgDesc( 10, nLangID ) + "\" ) )\n " +
           	  "    submit() \n "+
                  "} }\n");
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

  public static synchronized Table createDomTable(String Columns, String pvDomain, String Titles)
  {
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";
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
          String query = " Select " + Columns + " From T_Domain " +
                         " WHERE Domain = '"+pvDomain+"' Order By Sequence_Nr";
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
            table.add( headrow );
          }

          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            for( int i=1; i<=count; i++)
            {
              String data = rs.getString(i);
              if( i==4 )
                 data = obj.getDesc( Integer.parseInt(data), "Lang");
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
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