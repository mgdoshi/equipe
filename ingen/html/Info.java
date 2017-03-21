package ingen.html;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import ingen.html.db.*;
import ingen.html.util.*;

public class Info
{
  public static Script ShowFormInfo( HttpServletRequest request, String pvFormName, String vPID )
  {
      int nLangID=0;
      int nEmpID=0;
      int nClnID=0;
      int nUserID=0;
      int nRecSecID=0;
      int nSchemeID=0;
      String temp = null;
      DBConnect db = new DBConnect();      
       
      if((temp=Parse.GetValueFromString( vPID, "UserID" ))!=null && !temp.equals("null"))     
         nUserID   = Integer.parseInt(temp);
      if((temp=Parse.GetValueFromString( vPID, "LangID" ))!=null && !temp.equals("null"))     
        nLangID    = Integer.parseInt(temp);
      if((temp=Parse.GetValueFromString( vPID, "EmployeeID" ))!=null && !temp.equals("null"))     
        nEmpID  = Integer.parseInt(temp);
      if((temp=Parse.GetValueFromString( vPID, "ClientID" ))!=null && !temp.equals("null"))     
        nClnID  = Integer.parseInt(temp);
      if((temp=Parse.GetValueFromString( vPID, "RecSecID" ))!=null && !temp.equals("null"))     
        nRecSecID  = Integer.parseInt(temp);
      if((temp=Parse.GetValueFromString( vPID, "SchemeID" ))!=null && !temp.equals("null"))     
        nSchemeID  = Integer.parseInt(temp);

      String vCurrUser    = db.getName( nUserID, "User" ); 
      String vCurrEmpl    = db.getName( nEmpID, "Employee" ); 
      String vCurrClnt    = db.getName( nClnID, "Client" );
      String vCurrLang    = db.getDesc( nLangID, "Lang" ); 
      String vCurrRecSec  = db.getName( nRecSecID, "RecSec" ); 
      String vCurrScheme  = db.getName( nSchemeID, "Scheme" );


      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();

      scrdata.add( " <!-- Start Hidding                \n"+
 
                   " function show_FormInfo()  {       \n"+ 
                   "    FormInfo = window.open( \"\", \"FormInfoWin\", \"menubar=0,scrollbars=0,resizable=0,width=325,height=260\") \n"+
                   "    FormInfo.document.open()  \n"+
                   "    FormInfo.document.writeln( \"<HTML><HEAD><TITLE>Form Information</TITLE></HEAD>\" ) \n"+
                   "    FormInfo.document.writeln( \"<BODY BGCOLOR=#D0D0D0>\" ) \n"+
                   "    FormInfo.document.writeln( \"<FONT FACE=Arial SIZE=2>\" ) \n"+
                   "    FormInfo.document.writeln( \"<FORM><BR><TABLE ALIGN=center COLS=2 WIDTH=90% BGCOLOR=#E6FFFF>\" ) \n"+

                   "    FormInfo.document.writeln( \"<TR><TD><B>Form Name</B></TD>\" )  \n"+
                   "    FormInfo.document.writeln( \"<TD>"+pvFormName+"</TD></TR>\" ) \n"+
   
                   "    FormInfo.document.writeln( \"<TR><TD><B>User</B></TD>\" ) \n"+
                   "    FormInfo.document.writeln( \"<TD>"+vCurrUser+"</TD></TR>\" ) \n"+

                   "    FormInfo.document.writeln( \"<TR><TD><B>Employee</B></TD>\" ) \n"+
                   "    FormInfo.document.writeln( \"<TD>"+vCurrEmpl+"</TD></TR>\" ) \n"+

                   "    FormInfo.document.writeln( \"<TR><TD><B>Client</B></TD>\" ) \n"+
                   "    FormInfo.document.writeln( \"<TD>"+vCurrClnt+"</TD></TR>\" ) \n"+

                   "    FormInfo.document.writeln( \"<TR><TD><B>Language</B></TD>\" ) \n"+
                   "    FormInfo.document.writeln( \"<TD>"+vCurrLang+"</TD></TR>\" ) \n"+

                   "    FormInfo.document.writeln( \"<TR><TD><B>Rec Security</B></TD>\" ) \n"+
                   "    FormInfo.document.writeln( \"<TD>"+vCurrRecSec+"</TD></TR>\" )\n"+

                   "    FormInfo.document.writeln( \"<TR><TD><B>Scheme</B></TD>\" ) \n"+
                   "    FormInfo.document.writeln( \"<TD>"+vCurrScheme+"</TD></TR>\" ) \n"+

                   "    FormInfo.document.writeln( \"</TABLE><BR>\" ) \n"+
                   "    FormInfo.document.writeln( \"<CENTER><INPUT TYPE=Button Value=Ok onClick=top.close()></CENTER>\") \n"+
                   "    FormInfo.document.writeln( \"</FONT></FORM></BODY></HTML>\" ) \n"+
                   "    FormInfo.document.close() \n"+
                   " } \n"+

                   " // End Hidding --> ");
      scr.add(scrdata);
      return scr;
    }

    public static Script ShowStatus(String pvMessage)
    {
      if(pvMessage==null || pvMessage.equals("") || pvMessage.equalsIgnoreCase("null"))
         pvMessage = " Order Tracking And Entry System";  
      Script scr = new Script();  
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding \n" +
                  " top.show_message('"+pvMessage+"')" +     
                  "\n// End Hidding -->");
      scr.add(scrdata);
      return scr;
    }
}