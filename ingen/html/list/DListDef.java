package ingen.html.list;

import ingen.html.*;
import java.util.*;

public class DListDef extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
         Creates Object equivalent of DListDef ie DD Html Tag, which is used to insert
         definitions of terms. This tag is used in the context of the definition list
         &lt;DL&gt;, where terms are tagged with &lt;DT&gt; and definitions are tagged
         with &lt;DD&gt;.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DD&gt;<BR>
      */

      public DListDef()
      {
         startTag="DD";
         endTag="";
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
         Creates Object equivalent of DListDef ie DD Html Tag, which is used to insert
         definitions of terms. This tag is used in the context of the definition list
         &lt;DL&gt;, where terms are tagged with &lt;DT&gt; and definitions are tagged with &lt;DD&gt;.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String Text    : the definition for the term.
      2) String Attribs : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DD Attribs&gt; Text<BR>
      */

      public DListDef( String Text, String Attribs)
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