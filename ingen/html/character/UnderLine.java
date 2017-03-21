package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class UnderLine extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of UnderLine ie U Html Tag, which direct the browser to
          render the text they surround with an underline.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;U&gt; and &lt;/U&gt;<BR>
      */

      public UnderLine()
      {
         startTag="U";
         endTag="/U";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of UnderLine ie U Html Tag, which direct the browser to
          render the text they surround with an underline.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to render with an underline.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;U Attribs&gt; Text &lt;/U&gt;<BR>
      */

      public UnderLine( String Text, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}