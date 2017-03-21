package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormSelectOption extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormSelectOption i.e Option Html Tag, which represents
          one choice in a Select element.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Value    : the value for the VALUE attribute.
      2) String Text     : the text for option.
      3) String Selected : if the value for this parameter is not NULL, the SELECTED
                           attribute is added to the tag.
      4) String Attribs  : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;OPTION VALUE="Value" SELECTED Attribs&gt; Text<BR>
      */ 

      public FormSelectOption( String Value, String Text, String Selected, String Attribs)
      {  
         startTag="OPTION";
         endTag="";
         String param ="";
         if( Text != null )
           add(Text);
         if( Value != null )
           param += " Value=\""+Value+"\"";
         if( Selected != null )
           param += " "+Selected;
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}