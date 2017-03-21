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

public class ChnStatusFrame extends HttpServlet
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
        String vTemplName = request.getParameter("vTemplName");
        String pvMessage = request.getParameter("pvMessage");
        String vFrom_Order_ID = request.getParameter("vFrom_Order_ID");
        String vTo_Order_ID = request.getParameter("vTo_Order_ID");
        String nPrint[] = request.getParameterValues("nPrint");
    
        Page page = new Page();
        Head head = new Head();
        Frame right=null;
        Frame left=null;
        Title title = new Title("Order Tracking System");
        head.add(title); 
        head.add(ChnStatusForm.ShowChnStatusScript(pvMode,vPID));
        head.add(Info.ShowFormInfo(request, "TR_STATUS", vPID));
        head.add(Info.ShowStatus(pvMessage));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        left  = new Frame("/JOrder/servlets/ChnStatusToolBar?pvMode="+pvMode+"","left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if(pvMode!=null && pvMode.equalsIgnoreCase("T"))
          right = new Frame("/JOrder/servlets/ChnStatusTable?vFrom_Order_ID="+vFrom_Order_ID+"&vTo_Order_ID="+vTo_Order_ID+"&vTemplName="+vTemplName+"",
                  "right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if(pvMode!=null && pvMode.equalsIgnoreCase("Q"))
          right = new Frame("/JOrder/servlets/ChnStatusQryForm?pvMode="+pvMode+"","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if(pvMode!=null && pvMode.equalsIgnoreCase("L"))
          right = new Frame("/JOrder/servlets/ChnOrdList?vFrom_Order_ID="+vFrom_Order_ID+"&vTo_Order_ID="+vTo_Order_ID+"&vTemplName="+vTemplName+"&nPrint="+nPrint[0]+"",
                  "right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        set.add(left);
        set.add(right);
        NoFrame nof = new NoFrame();
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}