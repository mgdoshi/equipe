package ingen.html;

import java.util.*;

public class Map extends HtmlTag
{
     /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Map i.e MAP Html Tag, which mark a set of regions
          in a client-side image map.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Name    : the value for the NAME attribute. 
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;MAP NAME="Name" Attribs&gt; and &lt;/MAP&gt;<BR>
      */

      public Map( String Name, String Attribs)
      {
         startTag="MAP";
         endTag="/MAP";     
         String param ="";
         if( Name != null )
           param = " Name=\""+Name + "\"";
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}