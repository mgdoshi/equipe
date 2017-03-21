package ingen.html.para;

import ingen.html.*;
import java.util.*;

public class MailTo extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates a link with email address(MailTo)<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Address : Email Address.
      2) String Text    : Link text.
      3) String Name    : Name of link.
      4) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      <PRE> 
      &lt;A href="Address" Name="Name" Attribs&gt;Text&lt;/A&gt;
      </PRE><BR> 
      */
      public MailTo( String Address, String Text, String Name, String Attribs )
      {  
         startTag="A";
         endTag="/A";
         String param ="";
         if( Text != null )
           add(Text);
         if( Address != null )
           param = " Href=\""+Address + "\"";
         if( Name != null )
           param += " Name=\""+Name + "\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}