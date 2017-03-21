package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormCheckBox extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormCheckbox i.e Input Html Tag with Type="CHECKBOX",
          which inserts a checkbox element in a form. A checkbox element is a button that the
          user can toggle on or off. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Name     : the value for the NAME attribute.
      2) String Value    : the value for the VALUE attribute.
      3) String Checked  : if the value for this parameter is not NULL, the CHECKED
                           attribute is added to the tag.
      4) String Attribs  : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;INPUT TYPE="CHECKBOX" NAME="Name" VALUE="Value" Checked Attribs&gt;<BR>
      */ 
     
      public FormCheckBox( String Name, String Value, String Checked, String Attribs)
      {  
         startTag="INPUT Type=\"CHECKBOX\"";
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