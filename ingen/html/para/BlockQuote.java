package ingen.html.para;

import ingen.html.*;
import java.util.*;

public class BlockQuote extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of BlockQuote Html Tag, which mark a section of quoted
          text.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BLOCKQUOTE&gt; and &lt;/BLOCKQUOTE&gt;<BR>
      */
      public BlockQuote()
      {
         startTag="BLOCKQUOTE";
         endTag="/BLOCKQUOTE";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of BlockQuote Html Tag, which mark a section of quoted
          text.<BR><BR>
      <B><U>PARAMETERS</U></B>
      1) String Text : the String goes between the tags.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BLOCKQUOTE&gt; Text &lt;/BLOCKQUOTE&gt;<BR>
      */
      public BlockQuote(String Text)
      {
         this();  
         if( Text != null)
           add(Text);
      }
}