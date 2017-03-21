package ingen.html.table;

import ingen.html.*;
import java.util.*;

public class TableRow extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of TableRow ie TR Html Tag, which inserts
          a new row in an HTML table. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TR&gt; and &lt;/TR&gt;<BR>
      */

      public TableRow()
      {
         startTag="TR";
         endTag="/TR";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of TableRow ie TR Html Tag, which inserts
          a new row in an HTML table. <BR><BR>
      <B><U>PARAMETERS</B></U><BR>
      <PRE>
      1) String CAlign  : the value for the ALIGN attribute.
      2) String VAlign  : the value for the VALIGN attribute.
      3) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TR ALIGN="CAlign" VAlign="VAlign" Attribs&gt; and &lt;/TR&gt;<BR>
      */

      public TableRow(String CAlign,String VAlign,String Attribs)
      {
         this();  
         String param ="";
         if(CAlign!=null)
           param = "Align=\""+CAlign + "\"";
         if(VAlign!=null)
           param += " VAlign=\""+VAlign + "\"";
         if(Attribs!=null)
           param += " " + Attribs;
         setAttribute(param);
      }
}