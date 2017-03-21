package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class KeyBoard extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of KeyBoard ie KBD Html Tag, which direct the browser
          to render the text in monospace. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;KBD&gt; and &lt;/KBD&gt;<BR>
      */

      public KeyBoard()
      {
         startTag="KBD";
         endTag="/KBD";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of KeyBoard ie KBD Html Tag, which direct the browser
          to render the text in monospace. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to render in monospace.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;KBD Attribs&gt; Text &lt;/KBD&gt;<BR>
      */

      public KeyBoard( String Text, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}