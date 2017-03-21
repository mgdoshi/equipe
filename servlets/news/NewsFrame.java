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

public class NewsFrame extends HttpServlet
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
        String pnNewsID = request.getParameter("pnNewsID");
        String pnStartPos = request.getParameter("pnStartPos");
        String pnRows = request.getParameter("pnRows");
        String pvMessage = request.getParameter("pvMessage");

        Page page = new Page();
        Head head = new Head();
        Frame right=null;
        Title title = new Title("Order Tracking System");
        head.add(title);
        head.add(NewsForm.ShowNewsScript(request,pvMode,pnNewsID,vPID,pnStartPos,pnRows));
        head.add(Info.ShowStatus(pvMessage));
        head.add(Info.ShowFormInfo(request, "SY_NEWS", vPID));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        Frame left  = new Frame("/JOrder/servlets/NewsToolBar?pvMode="+pvMode,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if(pvMode==null || pvMode.equalsIgnoreCase("") || pvMode.equalsIgnoreCase("null"))
           right = new Frame("/JOrder/servlets/NewsTable?pvMode="+pvMode+"&pnStartPos="+pnStartPos+"&pnRows="+pnRows,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else
           right = new Frame("/JOrder/servlets/NewsEntry?pvMode="+pvMode+"&pnNewsID="+pnNewsID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        NoFrame nof = new NoFrame();
        set.add(left);
        set.add(right);
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}