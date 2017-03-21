package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class Form extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Form Html Tag, which create a form
          section in an HTML document.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;FORM&gt; and &lt;/FORM&gt;<BR>
      */ 

      public Form()
      {
         startTag="FORM";
         endTag="/FORM";
      }  

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Form Html Tag, which create a form
          section in an HTML document.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Action    : the URL of the WRB cartridge or CGI script to which the
                            contents of the form is sent. This parameter is required.
      2) String Method    : the value for the METHOD attribute. The value can be "GET" or "POST".
      3) String Target    : the value for the TARGET attribute.
      4) String EncType   : the value for the ENCTYPE attribute.
      5) String Attribs   : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;FORM ACTION="Action" METHOD="Method" TARGET="Target" ENCTYPE="EncType" Attribs&gt; and &lt;/FORM&gt;<BR>
      */ 
      
      public Form( String Action, String Method, String Target, String EncType, String Attribs)
      {  
         this();
         String param ="";
         if( Action != null )
           param = " Action=\""+Action+"\"";
         if( Method != null )
           param += " Method=\""+Method+"\"";
         if( Target != null )
           param += " Target=\""+Target+"\"";
         if( EncType != null )
           param += " EncType="+EncType;
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }       
 
}