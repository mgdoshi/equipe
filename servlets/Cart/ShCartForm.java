import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ShCartForm
{
  public static Script ShowShCartScript(String pvMode,String vPID)
  {
    String nLangID=null;
    String nUserID=null;
    String vCartDefName=null;


    ConfigData config = new ConfigData();
    Message msg = new Message();

    nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    nUserID  = Parse.GetValueFromString( vPID, "UserID" );

    vCartDefName  = config.GetConfigValue( "TR_SHOPCART", nLangID, "BL_LABEL.B_SHOPCART_CART_NAME", "Shopping Cart Name" );
 
    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("Q"))
    {
     scrdata.add("  function submit_form( pvAction ) { \n"  +
                 "  with( this.right_frame.document.forms[0] ) { \n" +
                 "  if ( pvAction == \"New\") {                  \n" +
                 "    var nCartID  = vCartDef_Name.options[vCartDef_Name.selectedIndex].value\n"  +
                 "    if ( nCartID != 0 )  {                     \n" +
                 "      alert( \"" + msg.GetMsgDesc(65,nLangID) + "\" ); return }\n" + 
                 "    if ( confirm(\"" + msg.GetMsgDesc(15,nLangID)+"\"))        \n" +
                 "      this.location.href = \"/JOrder/servlets/ShCartFrame?pvMode=I&pnCartDefID=\"+nCartID \n" +
                 "    } \n"+
                 "    if( pvAction  == \"Update\" )  {                                         \n"+
                 "      var nCartID  = vCartDef_Name.options[vCartDef_Name.selectedIndex].value\n"+
                 "    if ( nCartID == 0 || nCartID == \"\")  {                                   \n"+
                 "      alert( \"" + msg.GetMsgDesc(32,nLangID) + "\" ); return }\n" + 
                 "    if ( confirm(\"" + msg.GetMsgDesc(15,nLangID)+"\"))                  \n"+
                 "      this.location.href = \"/JOrder/servlets/ShCartFrame?pvMode=I&pnCartDefID=\"+nCartID \n" +
                 "    }                                                                        \n"+
                 "    if( pvAction  == \"Delete\" )  {                                         \n"+
                 "      var nCartID  = vCartDef_Name.options[vCartDef_Name.selectedIndex].value\n"+
                 "      var nTransID  = pnTransID.value                                        \n"+
                 "       if ( nCartID == 0 || nCartID == \"\")  {                              \n"+
                 "         alert( \"" + msg.GetMsgDesc(32,nLangID) + "\" ); return }           \n" + 
                 "	 if ( confirm(\"" + msg.GetMsgDesc(11,nLangID)+"\") ) {             \n"+
                 "         submit()\n"+
                 "    } } } }\n");
    }
    else
    {
      scrdata.add(" function submit_form( ) {   \n"+
                  "   with(this.right_frame.document.forms[0]) {\n"+
                  "  aField = new Array('"+vCartDefName+"',vCartDef_Name.value )\n"+
                  "  vErrMsg = top.check_fields( aField )                     \n"+ 
                  "  if ( vErrMsg == \"\" ) {                          	      \n"+ 
                  "   if( nCartDef_ID.value == \"\" || nCartDef_ID.value == 0)\n"+ 
                  "      vAction.value = \"Insert\"                        \n"+    
                  "   else                                                 \n"+ 
                  "      vAction.value = \"Update\"                        \n"+
                  "   if ( confirm(\""+msg.GetMsgDesc( 10, nLangID )+"\") ){\n"+
                  "   submit()					           \n"+
                  "   }  					           \n"+
                  " }   		                                   \n"+
                  " else {                                                 \n"+
                  "   vErrMsg = \""+msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg \n"+
                  "   alert( vErrMsg )                                     \n"+
                  "   return                                               \n"+
                  " } } } \n");

    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }
}

