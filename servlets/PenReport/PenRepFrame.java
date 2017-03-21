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

public class PenRepFrame extends HttpServlet
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
        String vTemplName = request.getParameter("vTemplName");
        String nFk_ItemGroup_ID = request.getParameter("nFk_ItemGroup_ID");
        String nFk_ItemClass_ID = request.getParameter("nFk_ItemClass_ID");
        String dFrom_Order_Dt = request.getParameter("dFrom_Order_Dt");
        String dTo_Order_Dt = request.getParameter("dTo_Order_Dt");
        String pnClientID = request.getParameter("pnClientID");       

        Page page = new Page();
        Head head = new Head();
        Frame right=null;
        Frame left=null;
        Title title = new Title("Order Tracking System");
        head.add(title); 
        head.add(PenRepForm.ShowPenRepScript(pvMode,vPID));
        head.add(Info.ShowFormInfo(request, "TR_PENDREP", vPID));
        head.add(Info.ShowStatus(pvMessage));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        left  = new Frame("/JOrder/servlets/PenRepToolBar?pvMode="+pvMode+"","left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if(pvMode!=null && pvMode.equalsIgnoreCase("T"))
          right = new Frame("/JOrder/servlets/PenRepTable?nFk_ItemGroup_ID="+nFk_ItemGroup_ID+"" +
                  "&nFk_ItemClass_ID="+nFk_ItemClass_ID+""+
                  "&dFrom_Order_Dt="+dFrom_Order_Dt+"&dTo_Order_Dt="+dTo_Order_Dt+""+
                  "&pnClientID="+pnClientID+"&vTemplName="+vTemplName,
                  "right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if(pvMode!=null && pvMode.equalsIgnoreCase("Q"))
          right = new Frame("/JOrder/servlets/PenRepQryForm?pvMode="+pvMode+"","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        set.add(left);
        set.add(right);
        NoFrame nof = new NoFrame();
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}