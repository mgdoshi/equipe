package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Variable extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Variable ie VAR Html Tag, which direct the browser
          to render the text they surround in italics.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;VAR&gt; and &lt;/VAR&gt;<BR>
      */

      public Variable()
      {
         startTag="VAR";
         endTag="/VAR";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Variable ie VAR Html Tag, which direct the browser
          to render the text they surround in italics.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    :  the text to render in italics.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;VAR Attribs&gt; Text &lt;/VAR&gt;<BR>
      */

      public Variable( String Text, String Attribs)
      {
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}