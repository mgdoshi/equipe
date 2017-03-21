import ingen.html.*;
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

public class ShowHelpMenu extends HttpServlet
{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {

        /*-------CHECK FOR ILLEGAL ENTRY---------*/
        response.setContentType("text/html");
        PrintWriter out = response.getWriter(); 

        String pvColPath = request.getParameter("pvColPath");

        Page page = new Page();
        Head head = new Head();
        Script scr = new Script( "JavaScript", null );
        HtmlTag scrdata = new HtmlTag();
        scrdata.add("<!-- Start Hidding" + "\n");
        scrdata.add("  var Menu = new Array() \n" +
                    "  var i = 0 ");
        HtmlTag tag = ShowChilds.ShowParent("0");
        scrdata.add(tag);  
        scrdata.add("  function GetMenu(){                  \n"+
                    "    setTimeout(\"creMenu()\",\"1000\") \n"+
                    "  }                                    \n"+
                    "  function creMenu() {                 \n"+
                    "    if(document.MyMenu)                \n"+
                    "    {                                  \n"+
                    "      if(navigator.appName==\"Netscape\")\n"+ 
                    "         document.MyMenu.createTree(Menu)\n"+
                    "      else                             \n"+
                    "         document.MyMenu.sample(Menu)  \n");
                    if( pvColPath==null || pvColPath.equals("") || pvColPath.equalsIgnoreCase("null") )
                      scrdata.add(" document.MyMenu.startWith(null) ");
                    else
                      scrdata.add(" document.MyMenu.startWith('"+pvColPath+"')");
            scrdata.add("  return                           \n"+
                        " }                                 \n"+
                        " else                              \n"+
                        " {                                 \n"+
                        "   GetMenu()                       \n"+
                        " }                                 \n"+
                     " }                                    \n");
        scrdata.add("// End Hidding -->");
        scr.add(scrdata);
        Body body = new Body("/ordimg/BACKGR2.GIF", "onLoad=\"GetMenu()\"" );
        body.add(" <applet code=\"TreeDemo.class\" archive=\"/ordhtm/treedemo.jar\" width=\"100%\" height=\"100%\" Name=\"MyMenu\" MAYSCRIPT> ");
        body.add(" <PARAM name=\"Background\" value=\"237 224 179\"> ");
        body.add(" <PARAM name=\"HiBackground\" value=\"189 179 144\"> ");
        body.add(" <PARAM name=\"Foreground\" value=\"0 0 0\"> ");
        body.add(" <PARAM name=\"HiForeground\" value=\"0 0 0\"> ");
        body.add(" <PARAM name=\"Font\" value=\"Arial 12\"> ");
        body.add(" <PARAM name=\"Target\" value=\"Right\"> ");
        body.add(" </applet> ");
        head.add(scr);
        page.add(head);
        page.add(body);
        out.println(page);
    }
}
