package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Marquee extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Marquee Html Tag. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;MARQUEE&gt; and &lt;/MARQUEE&gt;<BR>
      */

      public Marquee()
      {
         startTag="MARQUEE";
         endTag="/MARQUEE";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Marquee Html Tag. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text that goes between the tags.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;MARQUEE Attribs&gt; Text &lt;/MARQUEE&gt;<BR>
      */

      public Marquee(String Text, String Attribs)
      {
         this();
         String param ="";
         if( Text != null)
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}