package ingen.html.table;

import ingen.html.*;
import java.util.*;

public class TableCol extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of TableCol ie TD Html Tag, which insert data into a 
          cell of an HTML table.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TD&gt; and &lt;/TD&gt;<BR>
      */

      public TableCol()
      {
         startTag="TD";
         endTag="/TD";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of TableCol ie TD Html Tag, which insert data into a 
          cell of an HTML table.<BR><BR>
      <B><U>PARAMETERS</B></U><BR>
      <PRE>
      1) Object value   : the data for the cell in the table.
      2) String Align   : the value for the ALIGN attribute.
      3) String rowspan : the value for the ROWSPAN attribute.
      4) String colspan : the value for the COLSPAN attribute.
      5) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TD ALIGN="Align" ROWSPAN="rowspan" COLSPAN="colspan" Attribs&gt; value &lt;/TD&gt;<BR>
      */

      public TableCol(Object value,String Align,String rowspan,String colspan,String Attribs)
      {
         this();  
         if(value!=null) 
           add(value);
         String param ="";
         if(Align!=null)
           param = "Align=\""+Align + "\"";
         if(rowspan!=null)
           param += " RowSpan="+rowspan;
         if(colspan!=null)
           param += " ColSpan="+colspan;
         if(Attribs!=null)
           param += " " + Attribs;
         setAttribute(param);
      }
}