package ingen.html.head;

import ingen.html.*;
import java.util.*;

public class Title extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Title Html Tag, which specify the text to display
          in the titlebar of the browser window.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TITLE&gt; and &lt;/TITLE&gt;<BR>
      */

      public Title()
      {
         startTag="TITLE";
         endTag="/TITLE";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Title Html Tag, which specify the text to display
          in the titlebar of the browser window.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String title : the text to display in the titlebar of the browser window.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TITLE&gt; title &lt;/TITLE&gt;<BR>
      */

      public Title(String title)
      {
         this();
         if( title != null )
           add(title);
      }
}