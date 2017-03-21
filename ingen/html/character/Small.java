package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Small extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Small Html Tag, which direct the browser to render
          the text they surround using a small font.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SMALL&gt; and &lt;/SMALL&gt;<BR>
      */

      public Small()
      {
         startTag="SMALL";
         endTag="/SMALL";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Small Html Tag, which direct the browser to render
          the text they surround using a small font.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to render in a small font.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SMALL Attribs&gt; Text &lt;/SMALL&gt;<BR>
      */

      public Small( String Text, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}