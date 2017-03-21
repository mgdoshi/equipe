package ingen.html.para;

import ingen.html.*;
import java.util.*;

public class Area extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of AREA Html Tag, which defines a client-side image map.
          This tag defines areas within the image and destinations for the areas. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Coords  : the value for the COORDS attribute.
      2) String Shape   : the value for the SHAPE attribute.
      3) String Href    : the value for the HREF attribute.
      4) String NoHref  : if the value for this parameter is not NULL, the NOHREF attribute
         is added to the tag.
      5) String Target  : the value for the TARGET attribute.
      6) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;AREA COORDS="Coords" SHAPE="Shape" HREF="Href" NOHREF TARGET="Target" Attribs&gt; Text &lt;/AREA&gt;<BR>
      */
      
      public Area( String Coords, String Shape, String Href, String NoHref, String Target, String Attribs )
      {  
         startTag="AREA";
         endTag="";
         String param ="";
         if( Coords != null )
           param = " Coords="+Coords;
         if( Shape != null )
           param += " Shape="+Shape;
         if( Href != null )
           param += " Href=\""+Href + "\"";
         if( NoHref != null )
           param += " NOHREF";
         if( Target != null )
           param += " Target=\""+Target + "\"";
         if( Attribs != null )
           param += " "+Attribs;
         setAttribute(param);
      }
}