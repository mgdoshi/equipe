import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UserRelnForm
{
  public static Script ShowUserRelnScript(HttpServletRequest request, String pvMode, String vPID)
  {
    String vUserName=null;

    ConfigData config = new ConfigData();
    Message msg = new Message();

    String nLangID=null;
    String nUserID=null;
    nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    nUserID  = Parse.GetValueFromString( vPID, "UserID" );

    vUserName = config.GetConfigValue( "ST_USERREL", nLangID, "BL_LABEL.B_USERREL_USER_NAME", "User Name" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if(pvMode!=null && pvMode.equalsIgnoreCase("I"))
    {
     scrdata.add("  function submit_form( pvAction ) {                     \n" +
                 "  with( this.right_frame.document.forms[0] ) {           \n" +
                 "    Assign[0].value = \"\"                               \n" + 
                 "    DeAssign[0].value = \"\"                             \n" + 
                 "    for( var i=2; i<Assign.length; i++ )  {              \n" +
                 "      if( Assign[i].checked ) {                          \n" +
                 "        Assign[0].value += Assign[i].value + \"~\"       \n" +
                 "    } }                                                  \n" +
                 "    for( var j=2; j<DeAssign.length; j++ )  {            \n" +
                 "      if( DeAssign[j].checked ) {                        \n" +
                 "        DeAssign[0].value += DeAssign[j].value + \"~\"   \n" +
                 "    } }                                                  \n" +
                 "    if ( confirm( \""+msg.GetMsgDesc(10,nLangID)+ "\")){ \n" +
                 "      submit()  } } }                                    \n");
    }
    else if(pvMode!=null && pvMode.equalsIgnoreCase("D"))
    {
      scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "     var aField           		\n" +
                  "     var vErrMsg          		\n" +
                  "     with( this.right_frame.document.forms[0] ){\n" +
        	  "       aField = new Array( \"" + vUserName + "\", pnUserID.options[pnUserID.selectedIndex].value)\n" +
                  "   	  vErrMsg = top.check_fields( aField ) \n" +
                  "   	  if ( vErrMsg == \"\" ) {             \n" +
                  "   	    if ( confirm( \"" + msg.GetMsgDesc( 15, nLangID ) + "\"))\n" +
                  "   	      submit() }\n" +
                  "   	  else {\n" +
                  "         vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                  "   	    alert( vErrMsg )\n" +
                  "   	    return          \n " +
                  "    } } } \n");
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }
 
}

