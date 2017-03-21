import ingen.html.*;
import ingen.html.db.*;
import ingen.html.util.*;
import ingen.html.frame.*;
import ingen.html.head.*;
import java.sql.*;
import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ShowHelp extends HttpServlet
{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {

        /*-------CHECK FOR ILLEGAL ENTRY---------*/
        response.setContentType("text/html");
        PrintWriter out = response.getWriter(); 

        String pnHelpID = request.getParameter("pnHelpID");
        String vColPath = null;
       
        vColPath = URLEncoder.encode( Help.GetColumnMenuPath(pnHelpID) );

        Page page = new Page();
        Head head = new Head();
        Title title = new Title(" Order Tracking System Help ");
        head.add(title); 
        FrameSet set = new FrameSet(null,"30%,*","FRAMEBORDER=\"NO\" BORDER=\"1\"");
        Frame left  = new Frame("/JOrder/servlets/ShowHelpMenu?pvColPath="+vColPath,"left","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        Frame right = new Frame("/ordhtm/MenuHelp.html","Right","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        NoFrame nof = new NoFrame();
        set.add(left);
        set.add(right);
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }
}
