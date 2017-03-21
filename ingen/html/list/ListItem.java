package ingen.html.list;

import ingen.html.*;
import java.util.*;

public class ListItem extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of ListItem ie LI Html Tag, which indicates
          a list item.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;LI&gt; and &lt;/LI&gt;<BR>
      */

      public ListItem()
      {
         startTag="LI";
         endTag="/LI";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of ListItem ie LI Html Tag, which indicates
          a list item.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String Attribs : other attributes to be included as-is in the tag.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;LI Attribs&gt; and &lt;/LI&gt;<BR>
      */

      public ListItem( String Attribs)
      {
         this();  
         String param ="";
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}