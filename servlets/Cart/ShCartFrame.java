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

public class ShCartFrame extends HttpServlet
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
        String pnCartDefID = request.getParameter("pnCartDefID");
        String pvMessage = request.getParameter("pvMessage");
 
        Page page = new Page();
        Head head = new Head();
        Frame right=null;
        Title title = new Title("Order Tracking System");
        head.add(title); 
        head.add(ShCartForm.ShowShCartScript(pvMode,vPID));
        head.add(Info.ShowStatus(pvMessage));
        head.add(Info.ShowFormInfo(request, "TR_SHOPCART", vPID));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        Frame left  = null;
        if(pvMode!=null && pvMode.equalsIgnoreCase("Q"))
        {
           left  = new Frame("/JOrder/servlets/ShCartToolBar?pvMode="+pvMode,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
           right = new Frame("/JOrder/servlets/ShCartQryForm","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        }
        else if(pnCartDefID==null || pnCartDefID.equalsIgnoreCase("null") ||
                pnCartDefID.equalsIgnoreCase("") || pnCartDefID.equalsIgnoreCase("0"))
        {
           left  = new Frame("/JOrder/servlets/ShCartToolBar?pvMode=I","left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
           right = new Frame("/JOrder/servlets/ShCartEntry?pvMode=I&pnCartDefID="+pnCartDefID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        } 
        else if(Integer.parseInt(pnCartDefID) > 0)
        {
           left  = new Frame("/JOrder/servlets/ShCartToolBar?pvMode=U","left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
           right = new Frame("/JOrder/servlets/ShCartEntry?pvMode=U&pnCartDefID="+pnCartDefID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        } 
        NoFrame nof = new NoFrame();
        set.add(left);
        set.add(right);
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}