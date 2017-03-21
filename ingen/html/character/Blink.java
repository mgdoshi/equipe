package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Blink extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Blink Html Tag. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BLINK&gt; and &lt;/BLINK&gt;<BR>
      */
      public Blink()
      {
         startTag="BLINK";
         endTag="/BLINK";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Blink Html Tag. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String Text : the string that goes between the &lt;/BLINK&gt; and &lt;/BLINK&gt; tags.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BLINK&gt; Text &lt;/BLINK&gt;<BR>
      */

      public Blink(String Text)
      {
         this();  
         if( Text != null)
           add(Text);
      }
}