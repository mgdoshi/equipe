package ingen.html.table;

import ingen.html.*;
import java.util.*;

public class Table extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of TABLE Html Tag, which define an HTML table.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TABLE&gt; and &lt;/TABLE&gt;<BR>
      */
      public Table()
      {
         startTag="TABLE";
         endTag="/TABLE";
      } 


      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of TABLE Html Tag, which define an HTML table.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Border  : the value for the Border attribute.
      2) String Align   : the value for the Align attribute.
      3) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TABLE "Border" Align="Align" Attribs&gt; and &lt;/TABLE&gt;<BR>
      */
      public Table(String Border,String Align,String Attribs)
      {
         this();  
         String param ="";
         if(Border!=null)
           param = " "+Border;
         if(Align!=null)
           param += " Align=\""+Align  + "\"";
         if(Attribs!=null)
           param += " " + Attribs;
         setAttribute(param);
      }
}