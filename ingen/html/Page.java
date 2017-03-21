package ingen.html;

import java.util.*;

public class Page extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates a Object of Html page. Generates the &lt;HTML&gt; and &lt;/HTML&gt; tags,
          which mark the beginning and the end of an HTML document. Developer can 
          add Strings or Htmltag derived Objects in this object using add method of 
          base HtmlTag class.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;HTML&gt; and &lt;/HTML&gt;<BR>
      */
      public Page()
      {
         startTag="HTML";
         endTag="/HTML";
      }  
}