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

public class AddressFrame extends HttpServlet
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
        String pvRefType = request.getParameter("pvRefType");
        String pnRefID = request.getParameter("pnRefID");
        String pnAddressID = request.getParameter("pnAddressID");
        String pvMessage = request.getParameter("pvMessage");

        Page page = new Page();
        Head head = new Head();
        Frame right;
        Title title = new Title("Order Tracking System");
        head.add(title); 
        head.add(AddressForm.ShowAddressScript( request, pvMode, pvRefType, pnRefID, pnAddressID,vPID));
        head.add(Info.ShowStatus(pvMessage));
        head.add(Info.ShowFormInfo(request, "ST_ADDRESS", vPID));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        Frame left  = new Frame("/JOrder/servlets/AddressToolBar?pvMode="+pvMode,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if( pvMode!=null && pvMode.equalsIgnoreCase("D"))
           right = new Frame("/JOrder/servlets/DefAddrSecurity","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
           right = new Frame("/JOrder/servlets/AddressTable?pvRefType="+pvRefType+"&pnRefID="+pnRefID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else
           right = new Frame("/JOrder/servlets/AddressEntry?pvMode="+pvMode+"&pvRefType="+pvRefType+"&pnRefID="+pnRefID+"&pnAddressID="+pnAddressID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        NoFrame nof = new NoFrame();
        set.add(left);
        set.add(right);
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }

}