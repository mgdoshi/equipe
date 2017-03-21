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

public class DelRepFrame extends HttpServlet
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
        String vFrom_Delivery_NR = request.getParameter("vFrom_Delivery_NR");
        String vTo_Delivery_NR = request.getParameter("vTo_Delivery_NR");
        String dFrom_Delivery_Dt = request.getParameter("dFrom_Delivery_Dt");
        String dTo_Delivery_Dt = request.getParameter("dTo_Delivery_Dt");
        String cDM_DelStat = request.getParameter("cDM_DelStat");

        Page page = new Page();
        Head head = new Head();
        Frame right=null;
        Frame left=null;
        Title title = new Title("Order Tracking System");
        head.add(title); 
        head.add(DelRepForm.ShowDelRepScript(pvMode,vPID));
        head.add(Info.ShowFormInfo(request, "TR_DELVREP", vPID));
        head.add(Info.ShowStatus(pvMessage));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        left  = new Frame("/JOrder/servlets/DelRepToolBar?pvMode="+pvMode+"","left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if(pvMode!=null && pvMode.equalsIgnoreCase("T"))
          right = new Frame("/JOrder/servlets/DelRepTable?vFrom_Delivery_NR="+vFrom_Delivery_NR+"&vTo_Delivery_NR="+vTo_Delivery_NR+"&dFrom_Delivery_Dt="+dFrom_Delivery_Dt+"&dTo_Delivery_Dt="+dTo_Delivery_Dt+"&cDM_DelStat="+cDM_DelStat,
                            "right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if(pvMode!=null && pvMode.equalsIgnoreCase("Q"))
          right = new Frame("/JOrder/servlets/DelRepQryForm?pvMode="+pvMode+"","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        set.add(left);
        set.add(right);
        NoFrame nof = new NoFrame();
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}