package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Sup extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of SuperScript ie Sup Html Tag, which direct the browser
          to render the text they surround as superscript.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SUP&gt; and &lt;/SUP&gt;<BR>
      */

      public Sup()
      {
         startTag="SUP";
         endTag="/SUP";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of SuperScript ie Sup Html Tag, which direct the browser
          to render the text they surround as superscript.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to render in superscript.
      2) String Align   : the value for the ALIGN attribute.
      3) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SUP ALIGN="Align" Attribs&gt; Text &lt;/SUP&gt;<BR>
      */

      public Sup( String Text, String Align, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Align != null )
           param += " Align=\""+Align+"\"";
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}