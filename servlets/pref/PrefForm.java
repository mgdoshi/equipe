import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PrefForm
{
  public static Script ShowPrefScript( String vPID )
  {
    String vPrefType=null;
    String vPrefClass=null;
    String vSchemeName=null;
    String vPrefValue=null;
    String nLangID=null;
    String nUserID=null;
    nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    nUserID  = Parse.GetValueFromString( vPID, "UserID" );
    ConfigData config = new ConfigData();
    Message msg = new Message();

    vSchemeName  = config.GetConfigValue( "SY_SCHEME", nLangID, "BL_LABEL.B_SCHEME_SCHEME_NAME", "Scheme Name" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "     var aField           		\n" +
                  "     var vErrMsg          		\n" +
                  "     with( this.right_frame1.document.forms[0] ){\n" +
        	  "       aField = new Array( \"" + vSchemeName  + "\", vSchemeName.value )\n" +
                  "   	  vErrMsg = top.check_fields( aField ) \n" +
                  "   	  if ( vErrMsg == \"\" ) {             \n" +
                  "   	    for( var i=0; i<vObjType.length; i++ ) { \n" +
                  "   	      for( var j=0; j<vFontFace[i].length; j++ ) \n" +
                  "   	        if( vFontFace[i].selectedIndex == 0 && vFontFace[i].options[j].value == \"Arial\"  ) vFontFace[i].selectedIndex = j \n" +
                  "   	      for( var j=0; j<vFontSize[i].length; j++ ) \n" +
                  "   	        if( vFontSize[i].selectedIndex == 0 && vFontSize[i].options[j].value == \"3\"  ) vFontSize[i].selectedIndex = j \n" +
                  "   	      for( var j=0; j<vFontColor[i].length; j++ ) \n" +
                  "   	        if( vFontColor[i].selectedIndex == 0 && vFontColor[i].options[j].value == \"Black\"  ) vFontColor[i].selectedIndex = j \n" +
                  "   	    } \n" +
                  "   	    if ( confirm( \"" + msg.GetMsgDesc( 10, nLangID ) + "\"))\n" +
                  "   	      submit() }\n" +
                  "   	  else {\n" +
                  "         vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                  "   	    alert( vErrMsg )\n" +
                  "   	    return          \n " +
                  "    } } }   \n" + 
                  "    function show_preview() {\n" +
                  "    with( this.right_frame1.document.forms[0] ) {\n" +
                  "      var vLabelFace  = ( vFontFace[0].selectedIndex  == 0 ? \"Arial\" : vFontFace[0].options[ vFontFace[0].selectedIndex ].value )\n" +
                  "      var vLabelSize  = ( vFontSize[0].selectedIndex  == 0 ? \"2\"     : vFontSize[0].options[ vFontSize[0].selectedIndex ].value )\n" +
                  "      var vLabelColor = ( vFontColor[0].selectedIndex == 0 ? \"Black\" : vFontColor[0].options[ vFontColor[0].selectedIndex ].value )\n" +
                  "      var vTextFace   = ( vFontFace[1].selectedIndex  == 0 ? \"Arial\" : vFontFace[1].options[ vFontFace[1].selectedIndex ].value )\n" +
                  "      var vTextSize   = ( vFontSize[1].selectedIndex  == 0 ? \"2\"     : vFontSize[1].options[ vFontSize[1].selectedIndex ].value )\n" +
                  "      var vTextColor  = ( vFontColor[1].selectedIndex == 0 ? \"Black\" : vFontColor[1].options[ vFontColor[1].selectedIndex ].value )\n" +
                  "      var vListFace   = ( vFontFace[2].selectedIndex  == 0 ? \"Arial\" : vFontFace[2].options[ vFontFace[2].selectedIndex ].value )\n" +
                  "      var vListSize   = ( vFontSize[2].selectedIndex  == 0 ? \"1\"     : vFontSize[2].options[ vFontSize[2].selectedIndex ].value )\n" +
                  "      var vListColor  = ( vFontColor[2].selectedIndex == 0 ? \"Black\" : vFontColor[2].options[ vFontColor[2].selectedIndex ].value )\n" +
                  "      var vTextAreaFace  = ( vFontFace[3].selectedIndex  == 0 ? \"Arial\" : vFontFace[3].options[ vFontFace[3].selectedIndex ].value )\n" +
                  "      var vTextAreaSize  = ( vFontSize[3].selectedIndex  == 0 ? \"2\"     : vFontSize[3].options[ vFontSize[3].selectedIndex ].value )\n" +
                  "      var vTextAreaColor = ( vFontColor[3].selectedIndex == 0 ? \"Black\" : vFontColor[3].options[ vFontColor[3].selectedIndex ].value )\n" +
                  "    }\n" +
                  "    with( this.right_frame2 ) {\n" +
                  "      document.open()         \n" +
                  "      document.writeln( \"<HTML><BODY BGCOLOR=#D0D0D0>\" )\n" +
                  "      document.writeln( \"<FORM><TABLE ALIGN=Center COLS=4 WIDTH=80%>\" )\n" +
                  "      document.writeln( '<TR><TD><FONT FACE=' + vLabelFace + ' SIZE='+ vLabelSize + ' COLOR=' + vLabelColor + ' >Employee Name</FONT></TD>')\n" +
                  "      document.writeln( '<TD><FONT FACE=' + vLabelFace + ' SIZE=' + vLabelSize + ' COLOR=' + vLabelColor + ' >Mr S M Diwan</FONT></TD>' )\n" +
                  "      document.writeln( '<TD><FONT FACE=' + vLabelFace + ' SIZE='+ vLabelSize + ' COLOR=' + vLabelColor + ' >Company</FONT></TD>' )\n" +
                  "      document.writeln( '<TD><FONT FACE=' + vTextFace  + ' SIZE=' + vTextSize + ' COLOR='+ vTextColor + ' ><INPUT TYPE=TEXT ></FONT></TD>' )\n" +
                  "      document.writeln( '<TR><TD><FONT FACE=' + vLabelFace + ' SIZE=' + vLabelSize + ' COLOR=' + vLabelColor + ' >Department</FONT></TD>' )\n" +
                  "      document.writeln( '<TD><FONT FACE=' + vListFace + ' SIZE=' + vListSize + ' COLOR=' + vListColor + ' ><SELECT><OPTION SELECTED>Mechanical<OPTION>Electrical<OPTION>Computer<OPTION>Production<OPTION>Dispatch</SELECT></FONT></TD>' )\n" +
                  "      document.writeln( '<TD><FONT FACE=' + vLabelFace+ ' SIZE=' + vLabelSize + ' COLOR='+ vLabelColor + ' >Res Address</FONT></TD>' )\n" +
                  "      document.writeln( '<TD><FONT FACE=' + vTextAreaFace + ' SIZE=' + vTextAreaSize + ' COLOR= ' + vTextAreaColor + ' ><TEXTAREA ROWS=2 COLS=10 ></TEXTAREA></FONT></TD></TR>' )\n" +
                  "      document.writeln( '</TABLE></FORM>')\n" +
                  "      document.writeln( '</BODY></HTML>')\n" +
                  "      document.close()\n" +
                  "   }  }\n");
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }
 
}

