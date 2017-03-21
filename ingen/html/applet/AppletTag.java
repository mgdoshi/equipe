package ingen.html.applet;

import ingen.html.*;
import java.util.*;

public class AppletTag extends HtmlTag
{   
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Applet Html Tag, which invokes a Java applet. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String name    : the value for the NAME attribute, which specifies the name of the applet.
      2) String code    : the value for the CODE attribute, which specifies the name of the applet class.
      3) String height  : the value for the HEIGHT attribute.
      4) String width   : the value for the WIDTH attribute.
      5) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;APPLET NAME="name" CODE="code" Height="height" Width="width" Attribs&gt; and &lt;/APPLET&gt;<BR>
      */

      public AppletTag( String name, String code, String height, String width, String Attribs)
      {  
         startTag="APPLET";
         endTag="/APPLET";
         String param ="";
         if( name != null )
           param = " Name=\""+name+"\"";
         if( code != null )
           param += " Code=\""+code+"\"";
         if( height != null )
           param += " Height="+height;
         if( width != null )
           param += " Width="+width;
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}