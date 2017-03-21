package ingen.html;

import java.util.*;

public class Script extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Script Html Tag, which creates the start and end of Script
          tag for an HTML document. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SCRIPT&gt; and &lt;/SCRIPT&gt;<BR>
      */ 

      public Script()
      {
         startTag="SCRIPT";
         endTag="/SCRIPT";
      }  

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Script Html Tag, which creates the start and end of
          Script tag for an HTML document. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Language : The value for the LANGUAGE attribute, which specifies the name of the
                           scripting language for an HTML Document e.g JavaScript, VBScript etc.
      2) String srcFile  : Altervative to LANGUAGE which specifies value of SRC attribute 
                           as a scripting source file to be included for an HTML document.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SCRIPT LANGUAGE="Language" OR SRC="SrcFile" and &lt;/SCRIPT&gt;<BR>
      */
      
      public Script( String Language, String srcFile )
      {  
           this();
           String param ="";
           if( Language != null )
             param = " LANGUAGE=\"" +Language+ "\"";
           if( srcFile != null )
             param = " SRC=\"" +srcFile+ "\"";
           setAttribute( param );
      }       
 
}