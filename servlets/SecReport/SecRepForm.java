
import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SecRepForm
{
  public static Script ShowSecRepScript(String pvMode,String vPID )
  {
    Message msg = new Message();
    ConfigData cdata = new ConfigData();  
    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    String  pnClientID = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_SELECT_CLIENT_NAME","Select Client");
    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "  with( this.right_frame.document.forms[0] ){\n" +
                  "    	  aField = new Array( \""+pnClientID+"\",pnClientID.options[pnClientID.selectedIndex].value)\n" +
                  "   	  vErrMsg = top.check_fields( aField ) \n" +
                  "   	  if ( vErrMsg == \"\"  &&  confirm( \""+msg.GetMsgDesc( 15, nLangID )+"\" ) )\n" +
                  "   	     submit()              \n" +
                  "   	  else {                   \n" +
                  "         vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                  "   	    alert( vErrMsg ); return; }\n " +
                  "  } }\n");
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }
}

