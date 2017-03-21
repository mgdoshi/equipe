package ingen.html.para;

import ingen.html.*;
import java.util.*;

public class Div extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Div Html Tag, which creates document divisions.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DIV&gt; and &lt;/DIV&gt;<BR>
      */
      public Div()
      {
         startTag="DIV";
         endTag="/DIV";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Div Html Tag, which creates document divisions.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Align   :  the value for the ALIGN attribute.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DIV ALIGN="Align" Attribs&gt; and &lt;/DIV&gt;<BR>
      */

      public Div( String Align, String Attribs)
      {
         this();  
         String param ="";
         if( Align != null )
           param = " Align=\""+Align + "\"";
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}