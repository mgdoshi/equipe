import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ClnItmForm
{
  public static Script ShowClnItmScript(HttpServletRequest request, String pvMode, String vPID )
  {
    String nLangID=null;
    String nUserID=null;
    nUserID   = Parse.GetValueFromString( vPID, "UserID" );
    nLangID   = Parse.GetValueFromString( vPID, "LangID" );

    ConfigData cdata = new ConfigData();
    Message msg = new Message();

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("D"))
    {
     scrdata.add("  function submit_form( pvAction ) {           \n" +
                 "  with( this.right_frame.document.forms[0] ) { \n" +
                 "    if ( nFk_Client_ID.selectedIndex == 0) {   \n" +
                 "      alert(\"" + msg.GetMsgDesc( 16, nLangID )+ "\") \n" +
                 "      nFk_Client_ID.focus(); return }          \n" +
                 "       if ( confirm( \""+msg.GetMsgDesc(15,nLangID)+ "\")){\n" +
                 "         this.location.href = \"/JOrder/servlets/ClnItmFrame?pvMode=S&pnClientID=\"+nFk_Client_ID.options[nFk_Client_ID.selectedIndex].value+\"&pvItemName=\"+escape(vItem_Name.value)\n" + 
                 "       } } } \n");
    }
    else if( pvMode!=null && pvMode.equalsIgnoreCase("S"))
    {
     scrdata.add(" function submit_form( pvAction ) {             \n"+
                 "   var nCount = 2                               \n"+ 
                 "   var i = 0                                    \n"+
                 "   with( this.right_frame.document.forms[0] ) { \n"+
                 "     for( i=2; i<vAssign.length; i++ )          \n"+ 
                 "       if( vAssign[i].checked ) vAssign[0].value = ++nCount \n"+
                 "     nCount = 2  				              \n"+    
                 "     for( i=2; i<vDeAssign.length; i++ )		      \n"+
                 "       if( vDeAssign[i].checked ) vDeAssign[0].value = ++nCount \n"+
                 "     if ( confirm( \""+ msg.GetMsgDesc( 10, nLangID ) + "\" ) ) \n"+
                 "	 submit()                                                 \n"+
                 "   }  }  \n");
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }
}

