package ingen.html.head;

import ingen.html.*;
import java.util.*;

public class MetaTag extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Meta Html Tag, which enables you to embed 
          meta-information about the document and also specify values for HTTP headers.
          For example, you can specify the expiration date, keywords, and author name.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Equiv   : the value for the HTTP-EQUIV attribute.
      2) String Name    : the value for the NAME attribute.
      3) String Content : the value for the CONTENT attribute.
      4) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;META HTTP-EQUIV="Equiv" NAME="Name" CONTENT="Content" Attribs&gt;<BR>
      */

      public MetaTag(String Equiv, String Name, String Content, String Attribs)
      {  
         startTag="META";
         endTag="";
         String param ="";
         if( Equiv != null )
           param = " HTTP-EQUIV=\""+Equiv+"\"";
         if( Name != null )
           param += " Name=\""+Name+"\"";
         if( content != null )
           param += " content=\""+Content+"\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}