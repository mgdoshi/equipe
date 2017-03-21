package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Strike extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Strike Html Tag, which direct the browser to render
          the text they surround in strikethrough type.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;STRIKE&gt; and &lt;/STRIKE&gt;<BR>
      */

      public Strike()
      {
         startTag="STRIKE";
         endTag="/STRIKE";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Strike Html Tag, which direct the browser to render
          the text they surround in strikethrough type.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to be rendered in strikethrough type.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;STRIKE Attribs&gt; Text &lt;/STRIKE&gt;<BR>
      */

      public Strike( String Text, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}