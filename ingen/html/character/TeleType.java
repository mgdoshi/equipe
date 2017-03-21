package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class TeleType extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of TeleType ie TT Html Tag, which direct the browser to
          render the text they surround in a fixed width typewriter font, for example,
          the courier font.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TT&gt; and &lt;/TT&gt;<BR>
      */

      public TeleType()
      {
         startTag="TT";
         endTag="/TT";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of TeleType ie TT Html Tag, which direct the browser to
          render the text they surround in a fixed width typewriter font, for example,
          the courier font.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to render in a fixed width typewriter font.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TT Attribs&gt; Text &lt;/TT&gt;<BR>
      */

      public TeleType( String Text, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}