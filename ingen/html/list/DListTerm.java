package ingen.html.list;

import ingen.html.*;
import java.util.*;

public class DListTerm extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
         Creates Object equivalent of DListTerm ie DT Html Tag, which defines a term in a
         definition list &lt;DL&gt;.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DT&gt;<BR>
      */
      public DListTerm()
      {
         startTag="DT";
         endTag="";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
         Creates Object equivalent of DListTerm ie DT Html Tag, which defines a term in a
         definition list &lt;DL&gt;.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the term.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DT Attribs&gt; Text<BR>
      */

      public DListTerm( String Text, String Attribs)
      {
         this();  
         String param ="";
         if( Text != null )
           add(Text);
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}