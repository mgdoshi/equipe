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

public class OrdManFrame extends HttpServlet
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
        String pnOrderID = request.getParameter("pnOrderID");
        String pnOrderDtlsID = request.getParameter("pnOrderDtlsID");
        String pvWhereClause = request.getParameter("pvWhereClause");
        String vTemplName = request.getParameter("vTemplName");
        String Browser = request.getParameter("Browser");

        Page page = new Page();
        Head head = new Head();
        Frame right=null;
        Frame left=null;
        Title title = new Title("Order Tracking System");
        head.add(title); 
        head.add(OrdManForm.ShowOrdManScript(pvMode,pnOrderID, vPID));
        head.add(Info.ShowFormInfo(request, "TR_ORDDTLS", vPID));
        head.add(Info.ShowStatus(pvMessage));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        left  = new Frame("/JOrder/servlets/OrdManToolBar?pvMode="+pvMode+"","left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if(pvMode!=null && pvMode.equalsIgnoreCase("T"))
          right = new Frame("/JOrder/servlets/OrdManTable?vTemplName="+vTemplName+"&Browser="+Browser+"&pvWhereClause="+URLEncoder.encode(pvWhereClause),"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if(pvMode!=null && pvMode.equalsIgnoreCase("Q"))
          right = new Frame("/JOrder/servlets/OrdManQryForm?pvMode="+pvMode+"","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if(pvMode!=null && pvMode.equalsIgnoreCase("D"))
          right = new Frame("/JOrder/servlets/OrderUpdEntry?pvMode="+pvMode+"&pnOrderID="+pnOrderID+"&vTemplName="+vTemplName+"&Browser="+Browser,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if(pvMode!=null && ( pvMode.equalsIgnoreCase("U") || pvMode.equalsIgnoreCase("I")) )
          right = new Frame("/JOrder/servlets/OrdManDtlsEntry?pvMode="+pvMode+"&pnOrderID="+pnOrderID+"&pnOrderDtlsID="+pnOrderDtlsID+"&vTemplName="+vTemplName+"&Browser="+Browser,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        set.add(left);
        set.add(right);
        NoFrame nof = new NoFrame();
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}