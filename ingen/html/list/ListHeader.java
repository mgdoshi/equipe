package ingen.html.list;

import ingen.html.*;
import java.util.*;

public class ListHeader extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of ListHeader ie LH Html Tag.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;LH&gt; and &lt;/LH&gt;<BR>
      */

      public ListHeader()
      {
         startTag="LH";
         endTag="/LH";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of ListHeader ie LH Html Tag.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text to place between &lt;LH&gt; and &lt;/LH&gt;.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;LH Attribs&gt; Text &lt;/LH&gt;<BR>
      */

      public ListHeader( String Text, String Attribs)
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