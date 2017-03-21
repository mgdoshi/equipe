package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormHidden extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormHidden i.e Input Html Tag with Type="HIDDEN",
          which inserts a hidden form element. This element is not seen by the user and is
          used to submit additional values to the script. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Name     : the value for the NAME attribute.
      2) String Value    : the value for the VALUE attribute.
      3) String Attribs  : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;INPUT TYPE="HIDDEN" NAME="Name" VALUE="Value" Attribs&gt;<BR>
      */ 
      
      public FormHidden( String Name, String Value, String Attribs)
      {  
         startTag="INPUT Type=\"HIDDEN\"";
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