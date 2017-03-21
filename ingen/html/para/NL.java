package ingen.html.para;

import ingen.html.*;
import java.util.*;

public class NL extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of BR Html Tag.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BR&gt;<BR>
      */
      public NL()
      {
         startTag="";
         endTag="";
         add("<BR>");
      }  


      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of BR Html Tag.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) int no         : No of line breaks.
      </PRE><BR>
      */
      public NL(int no)
      {
         for(int i=0;i<no;i++)
           add("<BR>");  
      }

}