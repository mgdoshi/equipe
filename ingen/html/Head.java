package ingen.html;

import java.util.*;

public class Head extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Head Html Tag, which mark the HTML document head
          section.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;HEAD&gt; and &lt;/HEAD&gt;<BR>
      */ 
      public Head()
      {
         startTag="HEAD";
         endTag="/HEAD";
      }  
}