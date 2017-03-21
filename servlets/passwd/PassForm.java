import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PassForm
{
  public static Script ShowPassScript(HttpServletRequest request, String pvMode, String vPID )
  {
    String nLangID=null;
    String nUserID=null;

    ConfigData cdata = new ConfigData();
    Message msg = new Message();
    User usr = new User(); 
    DBConnect db = new DBConnect();
 
    nUserID   = Parse.GetValueFromString( vPID, "UserID" );
    nLangID   = Parse.GetValueFromString( vPID, "LangID" );	

    String user_name = cdata.GetConfigValue("SY_USER", nLangID,"BL_LABEL.B_USER_USER_NAME", "User Name" );
    String old_pass = cdata.GetConfigValue("SY_USER", nLangID,"BL_LABEL.B_USER_OLD_PASSWORD","Old Password");
    String new_pass = cdata.GetConfigValue("SY_USER", nLangID,"BL_LABEL.B_USER_NEW_PASSWORD","New Password");
    String con_pass = cdata.GetConfigValue("SY_USER", nLangID,"BL_LABEL.B_USER_CONFIRM_PASSWD","Confirm Password");
    String uname = db.getName(nUserID,"USER");
    String upass = usr.getUserPass(nUserID);

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("U"))
    {
     scrdata.add(" function submit_form( pvAction ) { \n"+
                 "   var aField   \n"+ 
                 "   var vErrMsg  \n"+
                 "   with( this.right_frame.document.forms[0] ) { \n"+
                 "   aField = new Array( \""+user_name+"\" ,vUser_Name.value,\""+old_pass+"\" ,vUser_OldPass.value,\""+new_pass+"\" ,vUser_NewPass.value,\""+con_pass+"\",vUser_ConfPass.value) \n" +
                 "   vErrMsg = top.check_fields( aField )                  \n" +
                 "   if ( vErrMsg != \"\" ) {                          	   \n" +
                 "     vErrMsg = \""+msg.GetMsgDesc(9,nLangID)+"\"+vErrMsg \n" +
                 "     alert( vErrMsg ); vUser_Name.focus(); return;       \n" +
                 "   }                                                     \n" +
                 "   var vUserName = '"+uname+"'                           \n" +
                 "   var vPassWord = '"+upass+"'                           \n" +
                 "   if ( vUserName !=  vUser_Name.value )  {              \n" +
                 "     alert( \""+msg.GetMsgDesc(29,nLangID)+"\" );        \n" +
                 "     vUser_Name.focus(); vUser_Name.select(); return     \n" +
                 "   }                                                     \n" +
                 "   if ( vPassWord !=  vUser_OldPass.value )  {           \n" +
                 "     alert( \""+msg.GetMsgDesc(30,nLangID)+"\" );        \n" +
                 "   vUser_OldPass.focus(); vUser_OldPass.select(); return \n" +
                 "   }                                                     \n" +
                 "   if ( vUser_NewPass.value != vUser_ConfPass.value ) {  \n" +
                 "     alert( \""+msg.GetMsgDesc(31,nLangID)+"\" );        \n" +
                 "   vUser_NewPass.focus(); vUser_NewPass.select(); return \n" + 
                 "   }                                                     \n" +
                 "   if ( vErrMsg == \"\" ) {                              \n" +
                 "   if ( confirm( \""+msg.GetMsgDesc(10,nLangID)+"\" ) )  \n" +
                 "    submit()                                             \n" +
                 "   }  }  }                                               \n");
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }
}
