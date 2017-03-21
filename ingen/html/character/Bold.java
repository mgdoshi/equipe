package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Bold extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Bold ie B Html Tag, which direct the browser to
          display the text in boldface.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;B&gt; and &lt;/B&gt;<BR>
      */

      public Bold()
      {
         startTag="B";
         endTag="/B";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Bold ie B Html Tag, which direct the browser to
          display the text in boldface.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the text that goes between the tags.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;B Attribs&gt; Text &lt;/B&gt;<BR>
      */

      public Bold( String Text, String Attribs)
      {
         this();
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}