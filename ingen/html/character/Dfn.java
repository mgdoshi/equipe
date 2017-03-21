package ingen.html.character;

import ingen.html.*;
import java.util.*;

public class Dfn extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Defination ie DFN Html Tag, which direct the
          browser to render the text in italics.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DFN&gt; and &lt;/DFN&gt;<BR>
      */

      public Dfn()
      {
         startTag="DFN";
         endTag="/DFN";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Defination ie DFN Html Tag, which direct the
          browser to render the text in italics.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String Text    : the text to render in italics.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DFN&gt; Text &lt;/DFN&gt;<BR>
      */

      public Dfn(String Text)
      {
         this();  
         if( Text != null)
           add(Text);
      }
}