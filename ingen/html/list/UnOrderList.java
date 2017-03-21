package ingen.html.list;

import ingen.html.*;
import java.util.*;

public class UnOrderList extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of UnOrderList ie UL Html Tag, which define an unordered list.
          An unordered list presents listed items marked off by bullets. You add list items
          with <a href="ingen.html.list.ListItem.html#_top_">ListItem</a> class.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;UL&gt; and &lt;/UL&gt;<BR>
      */

      public UnOrderList()
      {
         startTag="UL";
         endTag="/UL";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of UnOrderList ie UL Html Tag, which define an unordered list.
          An unordered list presents listed items marked off by bullets. You add list items
          with <a href="ingen.html.list.ListItem.html#_top_">ListItem</a> class.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String Attribs : other attributes to be included as-is in the tag.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;UL Attribs&gt; and &lt;/UL&gt;<BR>
      */

      public UnOrderList( String Attribs)
      {
         this();  
         String param ="";
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}