package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Font extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Font Html Tag, which mark a section of text
          with the specified font characteristics.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;FONT&gt; and &lt;/FONT&gt;<BR>
      */

      public Font()
      {
         startTag="FONT";
         endTag="/FONT";
      }

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Font Html Tag, which mark a section of text
          with the specified font characteristics.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Color   : the value for the COLOR attribute. 
      2) String Face    : the value for the FACE attribute.
      3) String Size    : the value for the SIZE attribute.
      4) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;FONT COLOR="Color" FACE="Face" SIZE="Size" Attribs&gt; and &lt;/FONT&gt;<BR>
      */
      
      public Font( String Color, String Face, String Size, String Attribs )
      {  
         this();
         String param ="";
         if( Color != null )
           param = " Color=\""+Color+"\"";
         if( Face != null )
           param += " Face=\""+Face+"\"";
         if( Size != null )
           param += " Size="+Size;
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}