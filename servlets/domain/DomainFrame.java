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

public class DomainFrame extends HttpServlet
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
        String pvDomain = request.getParameter("pvDomain");
        String pnHelpID = request.getParameter("pnHelpID");
        String pvMessage = request.getParameter("pvMessage");
        Page page = new Page();
        Head head = new Head();
        Frame right = null;
        Title title = new Title("Order Tracking System");
        head.add(title); 
        head.add(DomainForm.ShowDomainScript(request, pvMode, pvDomain, vPID));
        head.add(Info.ShowFormInfo(request, "ST_DOMAIN", vPID));
        head.add(Info.ShowStatus(pvMessage));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        Frame left  = new Frame("/JOrder/servlets/DomainToolBar?pvMode="+pvMode+"&pnHelpID="+pnHelpID,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if(pvMode!=null && pvMode.equalsIgnoreCase("N"))
           right = new Frame("/JOrder/servlets/DomainTable?pvDomain="+pvDomain,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if(pvMode!=null && pvMode.equalsIgnoreCase("U"))
           right = new Frame("/JOrder/servlets/DomainEntry?pvDomain="+pvDomain,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        NoFrame nof = new NoFrame();
        set.add(left);
        set.add(right);
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}