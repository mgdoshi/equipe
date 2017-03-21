package ingen.html.para;

import ingen.html.*;
import java.util.*;

public class BgSound extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of BgSound Html Tag, which includes audio for a Web page.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Src     : the value for the SRC attribute.
      2) String Loop    : the value for the LOOP attribute.
      3) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BGSOUND SRC="Src" LOOP="Loop" Attribs&gt;<BR>
      */
      
      public BgSound( String Src, String Loop, String Attribs )
      {  
         startTag="BGSOUND";
         endTag="";
         String param ="";
         if( Src != null )
           param += " SRC=\""+Src + "\"";
         if( Loop != null )
           param += " LOOP=\""+Loop + "\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}