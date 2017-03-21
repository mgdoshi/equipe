package ingen.html.list;

import ingen.html.*;
import java.util.*;

public class DirList extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of DirList ie DIR Html Tag, which create a directory list
          section.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DIR&gt; and &lt;/DIR&gt;<BR>
      */

      public DirList()
      {
         startTag="DIR";
         endTag="/DIR";
      }
}