package ingen.html.list;

import ingen.html.*;
import java.util.*;

public class Menu extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Menu Html Tag, which create a list that presents
          one line per item. The items in the list appear more compact than an unordered list.
          The <a href="ingen.html.list.ListItem.html#_top_">ListItem</a> class defines the
          list items in a menu list. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;MENU&gt; and &lt;/MENU&gt;<BR>
      */
      public Menu()
      {
         startTag="MENU";
         endTag="/MENU";
      }
}