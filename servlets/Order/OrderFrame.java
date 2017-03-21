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

public class OrderFrame extends HttpServlet
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
        String pnOrderID = request.getParameter("pnOrderID");
        String vTemplName = request.getParameter("vTemplName");
        String pnOrdDtlsCount = request.getParameter("pnOrdDtlsCount");
        String pvMessage = request.getParameter("pvMessage");
        String Browser = request.getParameter("Browser");
        Page page = new Page();
        Head head = new Head();
        Frame right = null;
        Title title = new Title("Order Tracking System");
        head.add(title); 
        head.add(OrderForm.ShowOrderScript(request,pvMode,vPID));
        head.add(Info.ShowStatus(pvMessage));
        head.add(Info.ShowFormInfo(request, "TR_ORDER", vPID));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        Frame left  = new Frame("/JOrder/servlets/OrderToolBar?pvMode="+pvMode,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if( pvMode!= null && pvMode.equalsIgnoreCase("T") )
           right = new Frame("/JOrder/servlets/ChooseOrdTempl?pvMode=I","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if( pvMode!= null && pvMode.equalsIgnoreCase("I") )
           right = new Frame("/JOrder/servlets/OrderEntry?pvMode="+pvMode+"&pnOrderID="+pnOrderID+"&vTemplName="+vTemplName+"&Browser="+Browser+"&pnOrdDtlsCount="+pnOrdDtlsCount,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" ");
        else if( pvMode!= null && pvMode.equalsIgnoreCase("P") )
           right = new Frame("/JOrder/servlets/PrintOrder?pnOrderID="+pnOrderID+"&vTemplName="+vTemplName,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" ");
        NoFrame nof = new NoFrame();
        set.add(left);
        set.add(right);
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}