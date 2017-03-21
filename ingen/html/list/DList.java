package ingen.html.list;

import ingen.html.*;
import java.util.*;

public class DList extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of DList ie DL Html Tag, which create a definition list.
          A definition list looks like a glossary: it contains terms and definitions. Terms
          are inserted using <a href="ingen.html.list.DListTerm.html#_top_">DListTerm</a> 
          class, and definitions are inserted using <a href="ingen.html.list.DListDef.html#_top_">
          DListDef</a> class.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DL&gt; and &lt;/DL&gt;<BR>
      */

      public DList()
      {
         startTag="DL";
         endTag="/DL";     
      } 

      /**
      <B><U>SUMMARY:</U></B><BR>
         Creates Object equivalent of DList ie DL Html Tag, which create a definition list.
         A definition list looks like a glossary: it contains terms and definitions. Terms
         are inserted using <a href="ingen.html.list.DListTerm.html#_top_">DListTerm</a> 
         class, and definitions are inserted using <a href="ingen.html.list.DListDef.html#_top_">
         DListDef</a> class.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      1) String Attribs : other attributes to be included as-is in the tag.<BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;DL Attribs&gt; and &lt;/DL&gt;<BR>
      */

      public DList( String Attribs)
      {
         this();  
         String param ="";
         if( Attribs != null )
           param = " "+Attribs;
         setAttribute(param);
      }
}