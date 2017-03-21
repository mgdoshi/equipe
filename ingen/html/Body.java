package ingen.html;

import java.util.*;

public class Body extends HtmlTag
{
      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Body Html Tag, which mark the body section
          of an HTML document. <BR><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BODY&gt; and &lt;/BODY&gt;<BR>
      */ 

      public Body()
      {
         startTag="BODY";
         endTag="/BODY";
      }  

      /**
      <B><U>SUMMARY:</U></B><BR>
          Creates Object equivalent of Body Html Tag, which mark the body section
          of an HTML document. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String BackGround : the value for the BACKGROUND attribute, which specifies a
                             graphic file to use for the background of the document.
      2) String Attribs    : other attributes to be included as-is in the tag.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;BODY BACKGROUND="BackGround" Attribs&gt; and &lt;/BODY&gt;<BR>
      */
      
      public Body(String BackGround,String Attribs) 
      {  
           this();
           String param ="";
           if(BackGround!=null)
             param = " BACKGROUND=\""+BackGround  + "\"";
           if(Attribs!=null)
             param += " "+Attribs;
           setAttribute(param);
      }       
 
}