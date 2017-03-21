package ingen.html.table;

import ingen.html.*;
import java.util.*;

public class TableHeader extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of TableHeader ie TH Html Tag, which insert a header cell
          in an HTML table. The &lt;TH&gt; tag is similar to the &lt;TD&gt; tag, except that
          the text in the rows are usually rendered in bold type. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TH&gt; and &lt;/TH&gt;<BR>
      */
      public TableHeader()
      {
         startTag="TH";
         endTag="/TH";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of TableHeader ie TH Html Tag, which insert a header cell
          in an HTML table. The &lt;TH&gt; tag is similar to the &lt;TD&gt; tag, except that
          the text in the rows are usually rendered in bold type. <BR><BR>
      <B><U>PARAMETERS</B></U><BR>
      <PRE>
      1) Object value   : the data for the cell in the table.
      2) String Align   : the value for the ALIGN attribute.
      3) String rowspan : the value for the ROWSPAN attribute.
      4) String colspan : the value for the COLSPAN attribute.
      5) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TH ALIGN="Align" ROWSPAN="rowspan" COLSPAN="colspan" Attribs&gt; value &lt;/TH&gt;<BR>
      */

      public TableHeader(Object value,String Align,String rowspan,String colspan,String Attribs)
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