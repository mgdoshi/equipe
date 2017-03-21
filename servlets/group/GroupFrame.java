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

public class GroupFrame extends HttpServlet
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
      String nLangID=null;
      String nUserID=null;
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      String pvMode = request.getParameter("pvMode");
      String pnGroupID = request.getParameter("pnGroupID");
      String pvMessage = request.getParameter("pvMessage");
      Page page = new Page();
      Head head = new Head();
      Frame right;
      Title title = new Title("Order Tracking System");
      head.add(title); 
      head.add(Group.ShowGroupScript(request, pvMode, vPID));
      head.add(Info.ShowFormInfo(request, "SY_GROUP", vPID));
      head.add(Info.ShowStatus(pvMessage));
      FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
      Frame left  = new Frame("/JOrder/servlets/GroupToolBar?pvMode="+pvMode,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
      if( pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
         right = new Frame("/JOrder/servlets/GroupTable","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
      else
         right = new Frame("/JOrder/servlets/GroupEntry?pvMode="+pvMode+"&pnGroupID="+pnGroupID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
      NoFrame nof = new NoFrame();
      set.add(left);
      set.add(right);
      set.add(nof);
      page.add(head);
      page.add(set);
      page.printPage(out);
    }
}