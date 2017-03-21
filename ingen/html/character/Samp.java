package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Samp extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Sample ie SAMP Html Tag, which direct the browser
          to render the text they surround in monospace font.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SAMP&gt; and &lt;/SAMP&gt;<BR>
      */

      public Samp()
      {
         startTag="SAMP";
         endTag="/SAMP";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Sample ie SAMP Html Tag, which direct the browser
          to render the text they surround in monospace font.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to render in monospace font.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SAMP Attribs&gt; Text &lt;/SAMP&gt;<BR>
      */

      public Samp( String Text, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}