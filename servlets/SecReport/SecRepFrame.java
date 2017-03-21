import ingen.html.*;
import ingen.html.util.*;
import ingen.html.frame.*;
import ingen.html.head.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SecRepFrame extends HttpServlet
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

        String pvMode = request.getParameter("pvMode");
        String pvMessage = request.getParameter("pvMessage");

        Page page = new Page();
        Head head = new Head();
        Frame right=null;
        Frame left=null;
        Title title = new Title("Order Tracking System");
        head.add(title); 
        head.add(SecRepForm.ShowSecRepScript(pvMode,vPID));
        head.add(Info.ShowFormInfo(request, "TR_REPORT", vPID));
        head.add(Info.ShowStatus(pvMessage));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        if(pvMode!=null && pvMode.equalsIgnoreCase("Q"))
        {
          left  = new Frame("/JOrder/servlets/SecRepToolBar","left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
          right = new Frame("/JOrder/servlets/SecRep","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
          set.add(left);
          set.add(right);
        }
        NoFrame nof = new NoFrame();
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}