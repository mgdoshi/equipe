import ingen.html.*;
import ingen.html.frame.*;
import ingen.html.para.*;
import ingen.html.character.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import java.awt.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class OrdTemplForm extends HttpServlet
{
  public static Script ShowNetsScript( String vPID )
  {
     Message msg = new Message();
     String nLangID   = Parse.GetValueFromString( vPID, "LangID" );

     Script scr = new Script( "JavaScript", null );
     HtmlTag scrdata = new HtmlTag();
     scrdata.add("<!-- Start Hidding" + "\n");

     scrdata.add(" function callForm(flag){       \n"+
                 "  var url = \"/JOrder/servlets/NewAddr?nFlag=\"+flag \n"+
                 "  var AddrWindow = window.open(url,\"AddrWindow\",\"menubar=0,scrollbars=1,resizable=0,width=600,height=400\") \n"+
                 "} \n"+

                 "function callTable(flag) {\n"+
                 "  var url = \"/JOrder/servlets/AddrTable?nFlag=\"+flag \n"+
                 "  var AddrWindow = window.open(url,\"AddrWindow\",\"menubar=0,scrollbars=1,resizable=0,width=500,height=300\") \n"+
                 "} \n"+
 
                 "function getLogo() {\n"+
                 "  var url = \"/JOrder/servlets/UploadByServlet\" \n"+
                 "  var LogoWindow = window.open(url,\"LogoWindow\",\"menubar=0,scrollbars=1,resizable=0,width=400,height=200\") \n"+
                 "} \n"+
 
                 "function getCompanyName(pvMessage,pvValue){\n"+
                 "  var retVal; \n"+
                 "  retVal=window.prompt(pvMessage,pvValue)\n"+
                 "  if(retVal!=null)\n"+
                 "  {\n"+
                 "    document.forms[0].text1.value = retVal;\n"+
                 "    document.clayer.document.open();\n"+
                 "    document.clayer.document.writeln('<font face=\"Arial,Helvetica\">'); \n"+
                 "    document.clayer.document.writeln('<font color=\"#3333FF\" size=+2>');\n"+
                 "    document.clayer.document.writeln('<A href=\"JavaScript:getCompanyName(\\'Enter Company Name : \\',document.forms[0].text1.value)\">'); \n"+
                 "    document.clayer.document.writeln(document.forms[0].text1.value);\n"+
                 "    document.clayer.document.writeln('</A></font></font>');\n"+
                 "    document.clayer.document.close(); \n"+
                 "  }\n"+
                 "}\n"+
 
                 "function getChallanTitle(pvMessage,pvValue){ \n"+
                 "  var retVal; \n"+
                 "  retVal=window.prompt(pvMessage,pvValue)\n"+
                 "  if(retVal!=null)\n"+
                 "  { \n"+
                 "    document.forms[0].text2.value = retVal; \n"+
                 "    document.player.document.open(); \n"+
                 "    document.player.document.writeln('<B><font face=\"Arial,Helvetica\">'); \n"+
                 "    document.player.document.writeln('<font color=\"#3333FF\" size=+2>'); \n"+
                 "    document.player.document.writeln('<CENTER><A href=\"JavaScript:getChallanTitle(\\'Enter Challan Title : \\',document.forms[0].text2.value)\">'); \n"+
                 "    document.player.document.writeln(document.forms[0].text2.value); \n"+
                 "    document.player.document.writeln('</A></CENTER></font></font></b>'); \n"+
                 "    document.player.document.close(); \n"+
                 "  } \n"+
                 "} \n"+

                 "function submit_form(){ \n"+
                 "  with( document.forms[0]) \n"+
                 "  { \n"+
                 "    note[0].value = \"\"      \n"+
                 "    Instruct[0].value = \"\"  \n"+
                 "    rem1[0].value = \"\"      \n"+
                 "    rem2[0].value = \"\"      \n"+
                 "    rem3[0].value = \"\"      \n"+
                 "    note[0].value = note[1].checked ? \"1\" : \"0\" \n"+
                 "    Instruct[0].value = Instruct[1].checked ? \"1\" : \"0\"  \n"+  
                 "    rem1[0].value = rem1[1].checked ? \"1\" : \"0\"  \n"+
                 "    rem2[0].value = rem2[1].checked ? \"1\" : \"0\"  \n"+
                 "    rem3[0].value = rem3[1].checked ? \"1\" : \"0\"  \n"+
                 "    aField = new Array( \"TemplateName\",  vAliasName.value )  \n"+
                 "    vErrMsg = top.check_fields( aField )                       \n" +
             	 "    if ( vErrMsg != \"\" ) {                          	        \n"+
             	 "      vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg + \".\" \n"+
             	 "      alert( vErrMsg ); return                                 \n"+
             	 "    }                                                          \n"+
                 "    if( confirm(\"Do You Want to Continue\") )  \n"+
                 "      submit() \n"+
                 "  } \n"+
                 "} \n"+

                 "function getAddress(pvMessage,pvValue){ \n"+
                 "  var retVal; \n"+
                 "  var field = new Array()  \n"+
                 "  retVal=window.prompt(pvMessage,pvValue) \n"+
                 "  if(retVal!=null) \n"+
                 "  { \n"+
                 "    document.forms[0].text3.value = retVal; \n"+
                 "    document.alayer.document.open();     \n"+
                 "    document.alayer.document.writeln('<A href=\"JavaScript:getAddress(\\'Enter Company Address:\\',document.forms[0].text3.value)\" STYLE=\"text-decoration:none\">'); \n"+
                 "    document.alayer.document.writeln('<font face=\"Arial,Helvetica\" color=\"#000000\" size=-1>'); \n"+
                 "    document.alayer.document.writeln(document.forms[0].text3.value); \n"+
                 "    document.alayer.document.writeln('</font></A>'); \n"+
                 "    document.alayer.document.close(); \n"+
                 "  } \n"+
                 "} \n");

     scrdata.add("// End Hidding -->");
     scr.add(scrdata);
     return scr;
  }

  public static Script ShowIntExplScript( String vPID )
  {
     Message msg = new Message();
     String nLangID   = Parse.GetValueFromString( vPID, "LangID" );

     Script scr = new Script( "JavaScript", null );
     HtmlTag scrdata = new HtmlTag();
     scrdata.add("<!-- Start Hidding" + "\n");

     scrdata.add(" function callForm(flag){       \n"+
                 "  var url = \"/JOrder/servlets/NewAddr?nFlag=\"+flag \n"+
                 "  var AddrWindow = window.open(url,\"AddrWindow\",\"menubar=0,scrollbars=1,resizable=0,width=600,height=400\") \n"+
                 "} \n"+

                 "function callTable(flag) {\n"+
                 "  var url = \"/JOrder/servlets/AddrTable?nFlag=\"+flag \n"+
                 "  var AddrWindow = window.open(url,\"AddrWindow\",\"menubar=0,scrollbars=1,resizable=0,width=500,height=300\") \n"+
                 "} \n"+
 
                 "function getLogo() {\n"+
                 "  var url = \"/JOrder/servlets/UploadByServlet1\" \n"+
                 "  var LogoWindow = window.open(url,\"LogoWindow\",\"menubar=0,scrollbars=1,resizable=0,width=400,height=200\") \n"+
                 "} \n"+


                 "function getChallanTitle(pvMessage, pvValue){ \n"+
                 "  var retVal; \n"+
                 "  retVal=window.prompt(pvMessage,pvValue)\n"+
                 "  if(retVal!=null)                       \n"+
                 "  {                                      \n"+
                 "    document.forms[0].text2.value = retVal;           \n"+
                 "    var text = '<b><font face=\"Arial,Helvetica\">' + \n"+
                 "               '<font color=\"#3333FF\" size=+2>' + \n"+
                 "               '<CENTER><A href=\"JavaScript:getChallanTitle(\\'Enter Challan Title : \\',document.forms[0].text2.value)\">'+\n"+
                 "               document.forms[0].text2.value +   \n"+
                 "               '</A></font></font></b></CENTER>';\n"+
                 "    document.all(\"player\").innerHTML = text;   \n"+
                 "  } \n"+
                 "}\n"+

                 "function getCompanyName(pvMessage,pvValue){\n"+
                 "  var retVal;                              \n"+
                 "  retVal=window.prompt(pvMessage,pvValue)  \n"+
                 "  if(retVal!=null)                         \n"+
                 "  {                                        \n"+
                 "    document.forms[0].text1.value = retVal;\n"+
                 "    var text =  '<font face=\"Arial,Helvetica\">' + \n"+
                 "                '<font color=\"#3333FF\" size=+2>'+ \n"+
                 "                '<A href=\"JavaScript:getCompanyName(\\'Enter Company Name :\\',document.forms[0].text1.value)\">' +\n"+
                 "                document.forms[0].text1.value +\n"+
                 "                '</A></font></font>';\n"+
                 "    document.all(\"clayer\").innerHTML = text;\n"+
                 "  } \n"+
                 "}\n"+

                 "function submit_form(){ \n"+
                 "  with( document.forms[0]) \n"+
                 "  { \n"+
                 "    note[0].value = \"\"      \n"+
                 "    Instruct[0].value = \"\"  \n"+
                 "    rem1[0].value = \"\"      \n"+
                 "    rem2[0].value = \"\"      \n"+
                 "    rem3[0].value = \"\"      \n"+
                 "    note[0].value = note[1].checked ? \"1\" : \"0\" \n"+
                 "    Instruct[0].value = Instruct[1].checked ? \"1\" : \"0\"  \n"+  
                 "    rem1[0].value = rem1[1].checked ? \"1\" : \"0\"  \n"+
                 "    rem2[0].value = rem2[1].checked ? \"1\" : \"0\"  \n"+
                 "    rem3[0].value = rem3[1].checked ? \"1\" : \"0\"  \n"+
                 "    aField = new Array( \"TemplateName\",  vAliasName.value )  \n"+
                 "    vErrMsg = top.check_fields( aField )                       \n" +
             	 "    if ( vErrMsg != \"\" ) {                          	        \n"+
             	 "      vErrMsg = \""+ msg.GetMsgDesc( 9, nLangID )+"\" + vErrMsg + \".\" \n"+
             	 "      alert( vErrMsg ); return                                 \n"+
             	 "    }                                                          \n"+
                 "    if( confirm(\"Do You Want to Continue\") )  \n"+
                 "      submit() \n"+
                 "  } \n"+
                 "} \n"+

                 "function getAddress(pvMessage,pvValue){ \n"+
                 "  var retVal; \n"+
                 "  var field = new Array()  \n"+
                 "  retVal=window.prompt(pvMessage,pvValue) \n"+
                 "  if(retVal!=null) \n"+
                 "  { \n"+
                 "    document.forms[0].text3.value = retVal; \n"+
                 "    var text = '<A href=\"JavaScript:getAddress(\\'Enter Company Address:\\',document.forms[0].text3.value)\" STYLE=\"text-decoration:none\">' + \n"+
                 "               '<font face=\"Arial,Helvetica\" color=\"#000000\" size=-1>' + \n"+
                 "               document.forms[0].text3.value + \n"+
                 "               '</font></A>'  \n"+
                 "    document.all(\"alayer\").innerHTML = text;\n"+
                 "  } \n"+
                 "} \n");

     scrdata.add("// End Hidding -->");
     scr.add(scrdata);
     return scr;
  }
}