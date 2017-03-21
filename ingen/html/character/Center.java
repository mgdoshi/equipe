package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Center extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Center Html Tag, which center a section
          of text within a Web page.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;CENTER&gt; and &lt;/CENTER&gt;<BR>
      */

      public Center()
      {
         startTag="CENTER";
         endTag="/CENTER";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Center Html Tag, which center a section
          of text within a Web page.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) Object data    : The Object which goes between the center tag. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;CENTER&gt; data &lt;/CENTER&gt;<BR>
      */
      public Center(Object data)
      {
         this();  
         if( data != null)
           add(data);
      }
}