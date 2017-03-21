package ingen.html.frame;

import ingen.html.*;
import java.util.*;

public class FrameSet extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FrameSet Html Tag, which define a frameset section.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String rows    : the value for the ROWS attribute.
      2) String cols    : the value for the COLS attribute.
      3) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;FRAMESET ROWS="rows" COLS="cols" Attribs&gt; and &lt;/FRAMESET&gt;<BR>
      */
      
      public FrameSet( String rows, String cols, String Attribs)
      {  
         startTag="FRAMESET";
         endTag="/FRAMESET";
         String param ="";
         if( rows != null )
           param = " rows="+rows;
         if( cols != null )
           param += " cols="+cols;
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}