package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Code extends HtmlTag
{
     /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Code Html Tag, which direct the browser to render
          the text in monospace font.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;CODE&gt; and &lt;/CODE&gt;<BR>
      */

      public Code()
      {
         startTag="CODE";
         endTag="/CODE";
      }  

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Code Html Tag, which direct the browser to render
          the text in monospace font.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to render as code. 
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;CODE Attribs&gt; Text &lt;/CODE&gt;<BR>
      */
      
      public Code( String Text, String Attribs )
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