package ingen.html.util;

import javax.servlet.http.*;
import javax.servlet.*;

/**
Classes contain functions for setting,removing client side cookie and getting value of 
client side cookie. 
*/
public class CookieUtil
{
    /**
      <B><U>SUMMARY:</U></B><BR>
      Set Client side cookie. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) HttpServletResponse response : Referance to HttpServletResponse Object.
      2) String name    : Name of cookie to be set.
      3) String value   : Value of cookie to be set.
      4) int exp        : Expiry time of cookie in seconds 
                          -ve value sets cookie life equal to browser session.
                          0 value removes the cookie but using removeCookie function 
                          is better option for removing cookie.  
      5) boolean secure  : secure flag.
      </PRE><BR>
      */
    public Cookie setCookie(HttpServletResponse response,String name,String value,int exp,boolean secure)
    {
       Cookie cookie=null;
       if((name!=null || name!="") && (value!=null || value!="")) 
        cookie = new Cookie(name,value);
       cookie.setMaxAge(exp);
       cookie.setSecure(secure);
       response.addCookie(cookie);
       return cookie;   
    }


    /**
      <B><U>SUMMARY:</U></B><BR>
      Set Client side cookie. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) HttpServletResponse response : Referance to HttpServletResponse Object.
      2) String name    : Name of cookie to be set.
      3) String value   : Value of cookie to be set.
      4) int exp        : Expiry time of cookie in seconds 
                          -ve value sets cookie life equal to browser session.
                          0 value removes the cookie but using removeCookie function 
                          is better option for removing cookie.  
      </PRE> 
    */
    public Cookie setCookie(HttpServletResponse response,String name,String value,int exp)
    {
       Cookie cookie=null;
       if((name!=null || name!="") && (value!=null || value!="")) 
        cookie = new Cookie(name,value);
       cookie.setMaxAge(exp);
       response.addCookie(cookie);
       return cookie;   
    }


    /**
      <B><U>SUMMARY:</U></B><BR>
      Remove Client side cookie. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) HttpServletResponse response : Referance to HttpServletResponse Object.
      2) String name                  : Name of cookie to be removed.
      </PRE> 
    */ 
    public void removeCookie(HttpServletResponse response,String name)
    {
       Cookie cookie=null;
       if((name!=null || name!=""))
         cookie = new Cookie(name,"");
       cookie.setMaxAge(1);
       response.addCookie(cookie);
    }


    /**
      <B><U>SUMMARY:</U></B><BR>
      Get Client side cookie value. <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) HttpServletRequest response : Referance to HttpServletRequest Object.
      2) String name                 : Name of cookie.
      </PRE> 
    */ 
    public String getCookie(HttpServletRequest request,String name)
    {
       String value=null;
       Cookie []co = request.getCookies();
       for(int i=0;i<co.length;i++)
          if(co[i].getName().equals(name))
          {
            value = co[i].getValue();
            break;
          }
       return value;
    }  
       
}