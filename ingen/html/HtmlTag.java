package ingen.html;
import java.util.*;
import java.io.*;

/**
<B><U>Base Class HtmlTag</U></B> : &nbsp;&nbsp;which implements basic functionality of any HTML tag.
All of the classes provided in this packages are extended from this base class.
Developer can derive his own class from this base class to add new html tags.
*/
public class HtmlTag extends Object
{

      /**<B><U>startTag</U></B> default value is empty string. Developer can assign a 
         value to this varibale in dervied class. This value is used as
         start tag in representation of this object.<BR><BR>
      */  
      protected String startTag="";

      /**<B><U>endTag</U></B> default value is empty string. Developer can assign a 
         value to this varibale in dervied class. This value is used as
         end tag in representation of this object.<BR><BR>
      */  
      protected String endTag="";


      /**<B><U>starter</U></B> default value is '<'. Developer can assign a 
         value to this varibale in dervied class. This value is used as
         starter in representation of this object.<BR><BR>
      */  
      protected String starter="<";


      /**<B><U>delimitor</U></B> default value is '>'. Developer can assign a 
         value to this varibale in dervied class. This value is used as
         delimitor in representation of this object.<BR><BR>
      */  
      protected String delimitor=">";


      /**<B><U>attribute</U></B> default value is empty string. Developer can assign a
         value to this varibale in dervied class. This value is used as extra attributes
         in start tag.<BR><BR>
      */  
      protected Object attribute="";


      /**<B><U>content</U></B> Maintains a list of content objects.<BR><BR>
      */  
      protected Vector content=new Vector();


      /**<B><U>prefix</U></B> Maintains a list of starttags, which specifies extra formating
         information about this tag.<BR><BR>
      */  
      protected Vector prefix=new Vector();

     
      /**<B><U>suffix</U></B> Maintains a list of endtags corresponding to formating tags added
         in prfix list.<BR>
      */  
      protected Vector suffix=new Vector();


      /**
      <B><U>SUMMARY:</U></B><BR>
       Return String representation of start tag<BR>
       i.e return value = starter + starttag + attributes + delimitor;<BR><BR>
      */
      protected String getStartTag()
      {
             String result="";  
             if(startTag!="")
                 result = starter + startTag + getAttributeData() + delimitor;
             return result;    
      }


      /**
      <B><U>SUMMARY:</U></B><BR>
       Return String representation of end tag<BR>
       i.e return value = starter + endTag + delimitor;<BR><BR>
      */
      protected String getEndTag()
      {
             String result="";  
             if(endTag!="")
                 result = starter + endTag + delimitor;
             return result;    
      }


      /**
      <B><U>SUMMARY:</U></B><BR>   
       Return attribute Object assosiated with start tag.<BR><BR>
      */
      protected Object getAttribute()
      {
            return attribute;
      }


      /**
      <B><U>SUMMARY:</U></B><BR>   
       Set attribute assosiated with start tag.<BR><BR>
      */
      protected void setAttribute(Object attribute)
      {
             if(attribute!=null)
                this.attribute = attribute;
      }


      /**
      <B><U>SUMMARY:</U></B><BR>   
       Return String representation of attribute assosiated with start tag.<BR><BR>
      */
      protected String getAttributeData()
      {
            String result="";
            if(attribute!=null && attribute!="")
                result = " " + attribute.toString() + " "; 
            return result;
      }


      /**
      <B><U>SUMMARY:</U></B><BR>   
       Return String representation of contents added in this htmltag.<BR><BR>
      */
      protected String getContentData()
      {
        String result="";
        for(int i=0;i<content.size();i++)
        {
           if(i==0)
             result+= "\n"; 
           result += content.elementAt(i).toString()+"\n"; 
        }
        return result;
      }


      /**
      <B><U>SUMMARY:</U></B><BR>   
         Developer can create Object of any class dervied from HtmlTag class or
         simple String and add it to any other object of class derived from HtmlTag
         component class using this method. Each HtmlTag object maintains a list
         of objects added in it.<BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      &nbsp;&nbsp;<B><U>Name</U></B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;content<BR>
      &nbsp;&nbsp;<U><B>Type</U></B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Object<BR>
      &nbsp;&nbsp;<U><B>Desc</U></B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Add html component in this component.
      <BR><BR>
      */ 
      public void add(Object data)
      {
        if(data!=null)
          content.addElement(data);
      }


      /**
      <B><U>SUMMARY:</U></B><BR>   
       Return String representation of prefix tag.<BR><BR>
      */
      protected String getPrefix()
      {
          String result="";
          for(int i=0;i<prefix.size();i++)
          {  
             result += prefix.elementAt(i).toString(); 
          }  
          return result;
      }


      /**
      <B><U>SUMMARY:</U></B><BR>   
       Return String representation of suffix tag.<BR><BR>
      */
      protected String getSuffix()
      {
            String result="";
            for(int i=0;i<suffix.size();i++)
            { 
                result += suffix.elementAt(i).toString(); 
            }
            return result;
      }


      /**
      <B><U>SUMMARY:</U></B><BR>   
       set extra formating tag for this tag.<BR><BR>
      */
      public void setFormat(Object Obj)
      {
          if(Obj!=null)
          {
             prefix.addElement(((HtmlTag)Obj).getStartTag());
             suffix.addElement(((HtmlTag)Obj).getEndTag());
          } 
      }

      /**
      <B><U>SUMMARY:</U></B><BR>   
      Specifies how this object is represented in string format.<BR>
      i.e   return value = startTag + prefix\s + content + suffix\s + endtag;<BR><BR>
      */
      public String toString()
      {
        String str = getStartTag()+getPrefix()+getContentData()+getSuffix()+getEndTag(); 
        return str;
      }

      public void printPage(PrintWriter out)
      {
        if(!startTag.trim().equals(""))
        {        
          out.print(starter + startTag);
          if(attribute!=null && attribute!="")
            out.print(" " + attribute + " ");
          out.println(delimitor);
        }
        for(int i=0;i<prefix.size();i++)
        {  
          out.println(prefix.elementAt(i).toString()); 
        }
        prefix.removeAllElements();    
        prefix.setSize(0);

        for(int i=0;i<content.size();i++)
        {
          if(content.elementAt(i) instanceof String)    
            out.println((String)content.elementAt(i)); 
          else
            ((HtmlTag)content.elementAt(i)).printPage(out); 
        }
        content.removeAllElements();    
        content.setSize(0);

        for(int i=0;i<suffix.size();i++)
        { 
          out.print(suffix.elementAt(i).toString()); 
        }
        suffix.removeAllElements();    
        suffix.setSize(0);
        if(!endTag.trim().equals(""))        
          out.println(starter + endTag + delimitor);
      }
}