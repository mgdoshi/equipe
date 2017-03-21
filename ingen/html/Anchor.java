package ingen.html;

import java.util.*;

public class Anchor extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Anchor Html Tag, which specify the
          source or destination of a hypertext link. HREF specifies to where
          to link. NAME allows this tag to be a target of a hypertext link.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Url     : the value for the HREF attribute.
      2) Object Text    : the string that goes between the &lt;A&gt; and &lt;/A&gt; tags.
      3) String Name    : the value for the NAME attribute.
      4) String Target  : the value for the TARGET attribute.
      5) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;A HREF="Url" NAME="Name" TARGET="Target" Attribs&gt;Text&lt;/A&gt;<BR>
      */
      
      public Anchor( String Url, Object Text, String Name, String Target, String Attribs )
      {  
         startTag="A";
         endTag="/A";
         String param ="";
         if( Url != null )
           param = " Href=\""+Url + "\"";
         if( Text != null )
           add(Text);
         if( Name != null )
           param += " Name=\""+Name + "\"";
         if( Target != null )
           param += " Target=\""+Target + "\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}