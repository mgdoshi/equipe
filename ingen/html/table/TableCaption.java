package ingen.html.table;

import ingen.html.*;
import java.util.*;

public class TableCaption extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of CAPTION Html Tag, which place a caption in an HTML table.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String caption : the text for the caption.
      2) String Align   : the value for the ALIGN attribute.
      3) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;CAPTION Align="Align" Attribs&gt; caption &lt;/CAPTION&gt;<BR>
      */
      public TableCaption(String caption, String Align, String Attribs)
      {
         startTag="CAPTION";
         endTag="/CAPTION";
         if(caption!=null) 
           add(caption);
         String param ="";
         if(Align!=null)
           param = "Align=\""+Align + "\"";
         if(Attribs!=null)
           param += " " + Attribs;
         setAttribute(param);
      }
}