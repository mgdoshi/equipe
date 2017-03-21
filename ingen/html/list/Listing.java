package ingen.html.list;

import ingen.html.*;
import java.util.*;

public class Listing extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Listing Html Tag, which mark a section of
          fixed-width text in the body of an HTML page. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;LISTING&gt; and &lt;/LISTING&gt;<BR>
      */

      public Listing()
      {
         startTag="LISTING";
         endTag="/LISTING";
      }
}