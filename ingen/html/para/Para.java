package ingen.html.para;

import ingen.html.*;
import java.util.*;

public class Para extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of PARA ie P Html Tag.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;P&gt; and &lt;/P&gt;<BR>
      */
      public Para()
      {
         startTag="P";
         endTag="/P";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of PARA ie P Html Tag.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String Attribs : other attributes to be included as-is in the tag.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;P&gt; and &lt;/P&gt;<BR>
      */

      public Para( String Attribs )
      {
         this();  
         String param=""; 
         if(Attribs!=null) 
           param += " " + Attribs;
         setAttribute(param);
      }
}