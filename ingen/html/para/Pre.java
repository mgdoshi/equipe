package ingen.html.para;

import ingen.html.*;
import java.util.*;

public class Pre extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of PRE Html Tag.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;PRE&gt; and &lt;/PRE&gt;<BR>
      */
      public Pre()
      {
         startTag="PRE";
         endTag="/PRE";
      }
 
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of PRE Html Tag.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String text  : Text which need to be formated with 
                        pre tag.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;PRE&gt; Text &lt;/PRE&gt;<BR>
      */
      public Pre(String Text)
      {
         this();  
         if( Text != null)
           add(Text);
      }
}