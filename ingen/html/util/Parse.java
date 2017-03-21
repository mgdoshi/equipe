package ingen.html.util;
import java.util.*;

public class Parse
{
   public static String GetValueFromString(String str,String keyword)
   {
      String retval=null;
      if(str!=null && keyword!=null)
      { 
        String arr[] = parse(str,"~");       
        for(int i=0;i<arr.length;i++)
        {
           String val[] = parse(arr[i],"=");
           if(val[0].equalsIgnoreCase(keyword))
             return val[1];
        }
      }
      return retval; 
   }

   public static String GetSubString( String s, String sep, int pos )
   {
     String result = null;
     if(s!=null && sep!=null)
     {
         StringTokenizer st = new StringTokenizer(s,sep);
         int count = st.countTokens();
         if(count > pos)
         {  
	  for (int i=0; i<pos; i++)
            st.nextToken();
          result = st.nextToken();
         }
     }
     return result;         
   }
                                      
   public static String NVL(String str,String keyword)
   {  
     String retval=keyword;  
     if(str!=null)
       retval=str;
     return retval;  
   }
  
   public static int[] parseInt(String s, String sep)
   {  
     if(s!=null && sep!=null)
     {
       StringTokenizer st = new StringTokenizer(s, sep);
	 int[] result = new int[st.countTokens()];
	 for (int i=0; i<result.length; i++)
	 {
	   result[i] = Integer.parseInt(st.nextToken());
	 }
	 return result;
     }
     return null;
   }

   public static String[] parse(String s, String sep)
   {
     if(s!=null && sep!=null)
     {
         StringTokenizer st = new StringTokenizer(s, sep);
	 String result[] = new String[st.countTokens()];
	 for (int i=0; i<result.length; i++)
	 {
	   result[i] = st.nextToken();
	 }
	 return result;
     }
     return null;
   }

   public static String formatStr(String str)
   {
      StringBuffer retVal=null;
      if(str==null || str.equals("") || str.equalsIgnoreCase("null"))
        return null;
      else
      {
        String test[] = Parse.parse(str,"'");
        if(test!=null)
        {
          retVal = new StringBuffer();  
          for(int i=0;i<test.length;i++)
          {  
            retVal.append(test[i]);
            if(i+1!=test.length)
              retVal.append("\\'");   
          }
          if(str.endsWith("'"))
            retVal.append("\\'"); 
          return retVal.toString();   
        }
      }
      return null;
   }

 public static String replaceStr(String str, String ch, String nr)
 {
    int sindex = 0;
    int eindex = 0;
    String retVal=null;
    int j = 0;
    if(str==null || str.equals("") || str.equalsIgnoreCase("null"))
      return null;
    else
    {
       StringBuffer tmp  = new StringBuffer();
       for( int i=0; i< str.length(); i++)
	   {
	      if( ch.charAt(j) == str.charAt(i) )
		  {
   		     tmp.append( str.charAt(i) );
             if( ch.equalsIgnoreCase(tmp.toString()) )
             {
                eindex = i;
                sindex = i-tmp.toString().length();
                retVal = str.substring( 0, sindex+1 );
                retVal = retVal+nr;
                retVal = retVal+str.substring( eindex+1, str.length() );
                break;
             } 
    	     j++; 
	      }
		  else
		  {
		     j=0;
			 tmp = new StringBuffer();
          }
     	}  
     }
     if(retVal==null)
       return str;  
     return retVal;
   }
}