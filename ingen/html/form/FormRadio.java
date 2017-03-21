package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormRadio extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormRadio i.e Input Html Tag with Type="RADIO",
          which creates a radio button on the HTML form. Within a set of radio buttons,
          the user can select only one button. Each radio button in the same set
          should have the same name, but different value. The selected radio button
          generates a name/value pair. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Name    : the value for the NAME attribute.
      2) String Value   : the value for the VALUE attribute.
      3) String Checked : if the value for this parameter is not NULL, the CHECKED
                          attribute is added to the tag. 
      4) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;INPUT TYPE="RADIO" NAME="Name" VALUE="Value" Checked Attribs&gt;<BR>
      */ 
     
      public FormRadio( String Name, String Value, String Checked, String Attribs)
      {  
         startTag="INPUT Type=\"RADIO\"";
         endTag="";
         String param ="";
         if( Name != null )
           param = " Name=\""+Name+"\"";
         if( Value != null )
           param += " Value=\""+Value+"\"";
         if( Checked != null )
           param += " "+Checked;
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}