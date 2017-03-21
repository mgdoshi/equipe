package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormButton extends HtmlTag
{
     /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormButton i.e Input Html Tag with Type="BUTTON",
          which inserts a Button in a form.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Name     : the value for the NAME attribute.
      2) String Value    : the value for the VALUE attribute.
      3) String Attribs  : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;INPUT TYPE="BUTTON" NAME="Name" VALUE="Value" Attribs&gt;<BR>
      */ 
       
      public FormButton( String Name, String Value, String Attribs)
      {  
         startTag="INPUT Type=\"BUTTON\"";
         endTag="";
         String param ="";
         if( Name != null )
           param = " Name=\""+Name+"\"";
         if( Value != null )
           param += " Value=\""+Value+"\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}