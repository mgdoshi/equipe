package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormSelect extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormSelect i.e Select Html Tag, which create a Select
          form element. A Select form element is a listbox, from which the user can select
          one or more values. The values are inserted using FormSelectOption. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Name     : the value for the NAME attribute.
      2) String Size     : the value for the SIZE attribute.
      3) String Attribs  : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;SELECT NAME="Name" SIZE="Size" Attribs&gt; and &lt;/SELECT&gt;<BR>
      */ 
     
      public FormSelect( String Name, String Size, String Attribs)
      {  
         startTag="SELECT";
         endTag="/SELECT";
         String param ="";
         if( Name != null )
           param = " Name=\""+Name+"\"";
         if( Size != null )
           param += " Size="+Size;
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}