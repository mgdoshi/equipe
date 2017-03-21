package ingen.html.para;

import ingen.html.*;
import java.util.*;

public class Address extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of ADDRESS Html Tag, which specify the address, 
          author and signature of a document. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;ADDRESS&gt; and &lt;/ADDRESS&gt;<BR>
      */
      public Address()
      {
         startTag="ADDRESS";
         endTag="/ADDRESS";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of ADDRESS Html Tag, which specify the address, 
          author and signature of a document. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the string that goes between the tags.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;ADDRESS Attribs&gt; Text &lt;/ADDRESS&gt;<BR>
      */

      public Address(String Text, String Attribs)
      {
         this();  
         String param ="";
         if( Text != null)
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}