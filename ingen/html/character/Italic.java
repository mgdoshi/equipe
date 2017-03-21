package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Italic extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Italic ie I Html Tag, which direct the browser
          to render the text in italics.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;I&gt; and &lt;/I&gt;<BR>
      */

      public Italic()
      {
         startTag="I";
         endTag="/I";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Italic ie I Html Tag, which direct the browser
          to render the text in italics.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to be rendered in italics.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;I Attribs&gt; Text &lt;/I&gt;<BR>
      */

      public Italic( String Text, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}