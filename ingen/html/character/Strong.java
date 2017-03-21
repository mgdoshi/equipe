package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Strong extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Strong Html Tag, which direct the browser to render
          the text they surround in bold.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;STRONG&gt; and &lt;/STRONG&gt;<BR>
      */

      public Strong()
      {
         startTag="STRONG";
         endTag="/STRONG";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Strong Html Tag, which direct the browser to render
          the text they surround in bold.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to be emphasized.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;STRONG Attribs&gt; Text &lt;/STRONG&gt;<BR>
      */

      public Strong( String Text, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}