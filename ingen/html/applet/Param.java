package ingen.html.applet;

import ingen.html.*;
import java.util.*;

public class Param extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Param Html Tag, which specifies parameter values for Java
          applets. The values can reference HTML variables.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String name    : the value for the NAME attribute.
      2) String value   : the value for the VALUE attribute.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;PARAM NAME="name" VALUE="value"&gt;<BR>
      */
      
      public Param( String name, String value)
      {  
         startTag="PARAM";
         endTag="";
         String param ="";
         if( name != null )
           param = " Name=\""+name+"\"";
         if( value != null )
           param += " value=\""+value+"\"";
         setAttribute(param);
      }
}