package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormTextArea extends HtmlTag
{
       /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormTextArea i.e TextArea Html Tag, which creates a text 
          field that has no predefined text in the text area. This field is used to enable
          the user to enter several lines of text.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Name     : the value for the NAME attribute.
      2) String rows     : the value for the ROWS attribute.
      3) String cols     : the value for the COLS attribute.
      4) String align    : the value for the ALIGN attribute.
      5) String Attribs  : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TEXTAREA NAME="Name" ROWS="rows" COLS="cols" ALIGN="align" Attribs&gt; and &lt;/TEXTAREA&gt;<BR>
      */ 

      public FormTextArea( String Name, String rows, String cols, String align, String Attribs)
      {  
         startTag="TEXTAREA";
         endTag="/TEXTAREA";
         String param ="";
         if( Name != null )
           param = " Name=\""+Name+"\"";
         if( rows != null )
           param += " rows="+rows;
         if( cols != null )
           param += " cols="+cols;
         if( align != null )
           param += " Align=\""+align+"\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}