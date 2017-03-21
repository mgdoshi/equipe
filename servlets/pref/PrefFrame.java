import ingen.html.*;
import ingen.html.db.*;
import ingen.html.util.*;
import ingen.html.frame.*;
import ingen.html.head.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PrefFrame extends HttpServlet
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

       String nSchemeID=null;
       nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
       String pvMessage = request.getParameter("pvMessage");

       ConfigData config = new ConfigData();
       Message msg = new Message();

       String pvMode="";
       if(nSchemeID!=null)
         pvMode = "I";
       else
         pvMode = "U";

       Page page = new Page();
       Head head = new Head();
       Title title = new Title("Order Tracking System");
       head.add(title); 
       head.add(PrefForm.ShowPrefScript(vPID));
       head.add(Info.ShowFormInfo(request, "SY_SCHEME", vPID));
       head.add(Info.ShowStatus(pvMessage));
       FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"YES\" BORDER=\"0\" scrolling=\"no\"");
       Frame left  = new Frame("/JOrder/servlets/PrefToolBar?pvMode="+pvMode,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
       FrameSet set1 = new FrameSet("70%,*",null,"FRAMEBORDER=\"YES\" BORDER=\"0\" scrolling=\"no\"");
       Frame right1 = new Frame("/JOrder/servlets/PrefEntry?pvMode="+pvMode,"right_frame1","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
       Frame right2 = new Frame("/JOrder/servlets/PrefPreviewForm","right_frame2","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
       NoFrame nof = new NoFrame();
       set1.add(right1);
       set1.add(right2);
       set1.add(nof);
       set.add(left);
       set.add(set1);
       set.add(nof);
       page.add(head);
       page.add(set);
       out.println(page);
    }

}