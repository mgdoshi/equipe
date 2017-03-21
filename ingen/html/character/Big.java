package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Big extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Big Html Tag, which direct the browser to render
          the text in a bigger font. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BIG&gt; and &lt;/BIG&gt;<BR>
      */

      public Big()
      {
         startTag="BIG";
         endTag="/BIG";
      }  

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Big Html Tag, which direct the browser to render
          the text in a bigger font. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text that goes between the tags.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BIG Attribs&gt; Text &lt;/BIG&gt;<BR>
      */
      
      public Big( String Text, String Attribs )
      {  
         this();
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}