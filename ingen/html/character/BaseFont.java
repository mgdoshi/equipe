package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class BaseFont extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of BaseFont Html Tag,which specifies the base
          font size for a Web page. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String Size : the value for the SIZE attribute.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BASEFONT Size="Size"&gt;<BR>
      */
      
      public BaseFont( String Size )
      {  
         startTag="BASEFONT";
         endTag="";
         String param ="";
         if( Size != null )
           param = " Size="+Size;
         setAttribute(param);
      }
}