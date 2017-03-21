package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Cite extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Cite Html Tag, which direct the browser to render
          the text as citation.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;CITE&gt; and &lt;/CITE&gt;<BR>
      */

      public Cite()
      {
         startTag="CITE";
         endTag="/CITE";
      }  

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Cite Html Tag, which direct the browser to render
          the text as citation.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to render as citation.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;CITE Attribs&gt; Text &lt;/CITE&gt;<BR>
      */
      
      public Cite( String Text, String Attribs )
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