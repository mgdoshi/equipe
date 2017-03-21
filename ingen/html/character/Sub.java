package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Sub extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of SubScript ie Sub Html Tag, which direct the browser
          to render the text they surround as subscript.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SUB&gt; and &lt;/SUB&gt;<BR>
      */

      public Sub()
      {
         startTag="SUB";
         endTag="/SUB";     
      }

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of SubScript ie Sub Html Tag, which direct the browser
          to render the text they surround as subscript.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to render in subscript.
      2) String Align   : the value for the ALIGN attribute.
      3) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SUB ALIGN="Align" Attribs&gt; Text &lt;/SUB&gt;<BR>
      */

      public Sub( String Text, String Align, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Align != null )
           param += " Align=\""+Align+"\"";
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}