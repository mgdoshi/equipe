package ingen.html;

import java.util.*;

public class HRule extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
      Creates Object equivalent of Horizontal Rule i.e HR Html Tag, which generates a
      line in the HTML document. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;HR&gt;<BR> 
      */ 

      public HRule()
      {
         startTag="HR";
         endTag="";
      }  

      /**
      <B><U>SUMMARY:</U></B><BR>
      Creates Object equivalent of Horizontal Rule i.e HR Html Tag, which generates a
      line in the HTML document. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String Attribs    : other attributes to be included as-is in the tag<BR><BR>
      <B><U>GENERATES</U></B><BR><BR>
      &lt;HR Attribs &gt;<BR> 
      */

      public HRule(String Attrib)
      {
         this();
         if( Attrib != null )
           setAttribute(" "+Attrib);
      }  
}