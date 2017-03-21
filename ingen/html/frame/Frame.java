package ingen.html.frame;

import ingen.html.*;
import java.util.*;

public class Frame extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Frame Html Tag, which defines the characteristics
          of a frame created by a &lt;FRAMESET&gt; tag. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String src     : the URL to display in the frame.
      2) String name    : the value for the NAME attribute.
      3) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;FRAME SRC="src" NAME="Name" Attribs&gt;<BR>
      */
      
      public Frame( String src, String name, String Attribs)
      {  
         startTag="FRAME";
         endTag="";
         String param ="";
         if( src != null )
           param = " src=\""+src+"\"";
         if( name != null )
           param += " name=\""+name+"\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}