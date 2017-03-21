package ingen.html.form;

import ingen.html.*;
import java.util.*;

public class FormImage extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of FormImage i.e Input Html Tag with Type="IMAGE",
          which creates an image field on which the user can click and cause the form
          to be submitted immediately. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Name     : the value for the NAME attribute.
      2) String src      : the value for the SRC attribute, which specifies the image file.
      3) String Align    : the value for the ALIGN attribute.
      4) String Attribs  : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;INPUT TYPE="IMAGE" NAME="Name" SRC="src" ALIGN="Align" Attribs&gt;<BR>
      */ 
      
      public FormImage( String Name, String src, String Align, String Attribs)
      {  
         startTag="INPUT Type=\"IMAGE\"";
         endTag="";
         String param ="";
         if( Name != null )
           param = " Name=\""+Name+"\"";
         if( src != null )
           param += " SRC=\""+src+"\"";
         if( Align != null )
           param += " Align=\""+Align+"\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}