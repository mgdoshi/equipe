import ingen.html.*;
import ingen.html.frame.*;
import ingen.html.head.*;
import ingen.html.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DelFrame extends HttpServlet
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
        String pnDeliveryID = request.getParameter("pnDeliveryID");
        String pnClientID = request.getParameter("pnClientID");
        String pvOrderNo = request.getParameter("pvOrderNo");
        String pvMessage = request.getParameter("pvMessage");
        String Browser = request.getParameter("Browser");
        Page page = new Page();
        Head head = new Head();
        Frame right = null;
        Title title = new Title("Order Tracking System");
        head.add(title); 
        head.add(DelForm.ShowDelScript(request,pvMode,vPID));
        head.add(Info.ShowStatus(pvMessage));
        head.add(Info.ShowFormInfo(request, "TR_DELIVERY", vPID));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        Frame left  = new Frame("/JOrder/servlets/DelToolBar?pvMode="+pvMode,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        right = new Frame("/JOrder/servlets/ChooseDelTempl?pvMode=I","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        NoFrame nof = new NoFrame();
        set.add(left);
        set.add(right);
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}