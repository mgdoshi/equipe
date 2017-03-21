import ingen.html.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Delivery extends HttpServlet
{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
    throws IOException, ServletException
    {

      /*-------CHECK FOR ILLEGAL ENTRY---------*/
      response.setContentType("text/html");
      PrintWriter out = response.getWriter(); 
      CookieUtil PkCookie = new CookieUtil();
      String vPID = PkCookie.getCookie(request,"PID");
      if(vPID==null)
      {
        out.println(WebUtil.IllegalEntry());
        return;
      }
 
      String Flag = request.getParameter("Flag");
      out.println("<HTML>");
      out.println("<HEAD>");
      out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
      out.println(" function GetBrowser() { ");
      out.println("   if( navigator.appName == \"Netscape\") ");         
      out.println("      this.location.href = \"/JOrder/servlets/CreDelTempl?Browser=1&Flag="+Flag+"\" ");
      out.println("   else ");
      out.println("      this.location.href = \"/JOrder/servlets/CreDelTempl?Browser=0&Flag="+Flag+"\" ");
      out.println("   return ");
      out.println(" } ");
      out.println(" </SCRIPT> ");
      out.println(" </HEAD> ");
      out.println(" <BODY BACKGROUND=\"/ordimg/BACKGR2.GIF\" onLoad=\"GetBrowser()\"> ");
      out.println(" </BODY> ");
      out.println(" </HTML> ");
   }
}