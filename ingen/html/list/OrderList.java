package ingen.html.list;

import ingen.html.*;
import java.util.*;

public class OrderList extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of OrderList ie OL Html Tag, which define an ordered list.
          An ordered list presents a list of numbered items. The numbered items are added 
          using <a href="ingen.html.list.ListItem.html#_top_">ListItem</a> class. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;OL&gt; and &lt;/OL&gt;<BR>
      */

      public OrderList()
      {
         startTag="OL";
         endTag="/OL";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of OrderList ie OL Html Tag, which define an ordered list.
          An ordered list presents a list of numbered items. The numbered items are added 
          using <a href="ingen.html.list.ListItem.html#_top_">ListItem</a> class. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String Attribs : other attributes to be included as-is in the tag.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;OL Attribs&gt; and &lt;/OL&gt;<BR>
      */

      public OrderList( String Attribs)
      {
         this();  
         String param ="";
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}