package ingen.html;

import java.util.*;

public class Img extends HtmlTag
{
     /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Image i.e IMG Html Tag, which directs the browser
          to load an image onto the HTML page.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Url     : the value for the SRC attribute.
      2) String Align   : the value for the ALIGN attribute.
      3) String Alt     : the value for the ALT attribute, which specifies alternative 
         text to display if the browser does not support images.
      4) String IsMap   : if the value for this parameter is not NULL, the ISMAP attribute
         is added to the tag. The attribute indicates that the image is an imagemap.
      5) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;IMG SRC="Url" ALIGN="Align" ALT="Alt" ISMAP Attribs&gt;<BR>
      */
 
      public Img( String Url, String Align, String Alt, String IsMap, String Attribs )
      {  
         startTag="IMG";
         endTag="";     
         String param ="";
         if( Url != null )
           param = " SRC=\""+Url + "\"";
         if( Align != null )
           param += " Align=\""+Align + "\"";
         if( Alt != null )
           param += " Alt=\""+Alt + "\"";
         if( IsMap != null )
           param += " "+IsMap;
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}