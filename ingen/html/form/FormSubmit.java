package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormSubmit extends HtmlTag
{    
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormSubmit i.e Input Html Tag with Type="SUBMIT",
          which creates a button that, when clicked, submits the form.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Name     : the value for the NAME attribute.
      2) String Value    : the value for the VALUE attribute.
      3) String Attribs  : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;INPUT TYPE="SUBMIT" NAME="Name" VALUE="Value" Attribs&gt;<BR>
      */ 
      
      public FormSubmit( String Name, String Value, String Attribs)
      {  
         startTag="INPUT Type=\"SUBMIT\"";
         endTag="";
         String param ="";
         if( Value != null )
           param += " Name=\""+Name+"\"";
         if( Value != null )
           param += " Value=\""+Value+"\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}