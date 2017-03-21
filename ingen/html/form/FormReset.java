package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormReset extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormReset i.e Input Html Tag with Type="RESET",
          which creates a button that, when clicked, resets all the form fields to their
          initial values.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Value    : the value for the VALUE attribute.
      2) String Attribs  : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;INPUT TYPE="RESET" VALUE="Value" Attribs&gt;<BR>
      */ 
      
      public FormReset( String Value, String Attribs)
      {  
         startTag="INPUT Type=\"RESET\"";
         endTag="";
         String param ="";
         if( Value != null )
           param += " Value=\""+Value+"\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}