import ingen.html.*;
import ingen.html.util.*;
import ingen.html.frame.*;
import ingen.html.head.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ConfigFrame extends HttpServlet
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
        String pvMessage = request.getParameter("pvMessage");
        String pvMode = request.getParameter("pvMode");
        String pnConfigID = request.getParameter("pnConfigID");
        String pvParentObj = request.getParameter("pvParentObj");
        String pnLangID = request.getParameter("pnLangID");

        Page page = new Page();
        Head head = new Head();
        Frame right;
        head.add(Config.ShowConfigScript(request,pvMode,vPID));
        head.add(Info.ShowFormInfo(request, "SY_CONFIG", vPID));
        head.add(Info.ShowStatus(pvMessage));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        Frame left  = new Frame("/JOrder/servlets/ConfigToolBar?pvMode="+pvMode,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if( pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
           right = new Frame("/JOrder/servlets/ConfigTable?pvParentObj="+pvParentObj+"&pnLangID="+pnLangID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if( pvMode!=null && pvMode.equals("Q") )
           right = new Frame("/JOrder/servlets/ConfigQueryForm?pvMode=Q","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else
           right = new Frame("/JOrder/servlets/ConfigEntry?pvMode="+pvMode+"&pnConfigID="+pnConfigID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        NoFrame nof = new NoFrame();
        set.add(left);
        set.add(right);
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }
}