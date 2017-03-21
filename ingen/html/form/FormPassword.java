package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormPassword extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormPassword i.e Input Html Tag with Type="PASSWORD", 
          which creates a single-line text entry field. When the user enters text in the
          field, each character is represented by one asterisk. This is usually used for
          entering passwords. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Name      : the value for the NAME attribute.
      2) String size      : the value for the SIZE attribute.
      3) String maxlength : the value for the MAXLENGTH attribute.
      4) String value     : the value for the VALUE attribute.
      5) String Attribs   : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;INPUT TYPE="PASSWORD" NAME="Name" VALUE="value" SIZE="size" MAXLENGTH="maxlength" Attribs&gt;<BR>
      */ 
      
      public FormPassword( String Name, String size, String maxlength, String value, String Attribs)
      {  
         startTag="INPUT Type=\"PASSWORD\"";
         endTag="";
         String param ="";
         if( Name != null )
           param = " Name=\""+Name+"\"";
         if( size != null )
           param += " Size="+size;
         if( maxlength != null )
           param += " MaxLength="+maxlength;
         if( value != null )
           param += " Value=\""+value+"\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}