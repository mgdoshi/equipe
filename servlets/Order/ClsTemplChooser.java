import ingen.html.para.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ClsTemplChooser extends HttpServlet
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

     String Browser = request.getParameter("Browser");
     String Flag = request.getParameter("Flag");
     if( Flag.equals("0") )
     {
       out.println("<BR>");
       Center cen = new Center();
       Bold b = new Bold();
       Font f = new Font("Red","Arial","4", null);
       b.add(" There is no existing Order Template For User, Please Create Order Template" );
       b.setFormat(f);
       cen.add(b);
       out.println(cen);
     }
     out.println("  <HTML>");
     out.println("  <BODY BACKGROUND=\"/ordimg/BACKGR2.GIF\">");
     out.println("  <BR><BR>");
     out.println("  <CENTER><FONT SIZE=4 COLOR=\"BLUE\" FACE=\"ARIAL\"><B>CHOOSE ORDER TEMPLATE </B></FONT></CENTER>");
     out.println("  <BR><BR>");
     out.println("  <TABLE align=\"center\" border=\"0\" width=\"80%\">");
     out.println("  <TR><TD><A HREF=\"/JOrder/servlets/OrdTempl?Browser="+Browser+"&ClsTempl=ordtempl1\"><IMG SRC=\"/ordimg/BP_Button/ORDTEMP1.gif\" BORDER=\"0\" ALT=\"Template 1\"></A></TD>");
     out.println("  <TR><TD><A HREF=\"/JOrder/servlets/OrdTempl?Browser="+Browser+"&ClsTempl=ordtempl2\"><IMG SRC=\"/ordimg/BP_Button/ORDTEMP1.gif\" BORDER=\"0\" ALT=\"Template 2\"></A></TD>");
     out.println("  <TR><TD><A HREF=\"/JOrder/servlets/OrdTempl?Browser="+Browser+"&ClsTempl=ordtempl3\"><IMG SRC=\"/ordimg/BP_Button/ORDTEMP1.gif\" BORDER=\"0\" ALT=\"Template 3\"></A></TD>");
     out.println("  </TR></TABLE></BODY></HTML>");
  }
}