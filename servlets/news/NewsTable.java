import ingen.html.*;
import ingen.html.jwa.*;
import ingen.html.para.*;
import ingen.html.character.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class NewsTable extends HttpServlet
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
      String count="0"; 
      String vFormType=null;
      String temp=null;
      String pnStartPos = "1";
      ConfigData cdata = new ConfigData();
      DBConnect obj = new DBConnect();
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      vFormType = cdata.GetConfigValue( "SY_NEWS", nLangID, "WD_TABLE", "News / T" );

      if((temp=request.getParameter("pnStartPos"))!=null && !temp.equals("") && !temp.equalsIgnoreCase("null"))
         pnStartPos = temp;

      try
      {
        conn = obj.GetDBConnection();
        stmt = conn.createStatement();
        String query = "Select count(*) from t_news";
        rs = stmt.executeQuery(query);
        if(rs.next())
        {
          count = rs.getString(1);
        } 
      }catch(SQLException sexe){}
      finally
      {
        try
        {
          if(rs!=null)
            rs.close();
          if(stmt!=null)
            stmt.close();
          if(conn!=null)
            conn.close(); 
        }catch(SQLException sexe){}
      }

      Page page = new Page();
      Head head = new Head();
      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      head.add(scr);
      Body body = new Body("/ordimg/BACKGR2.GIF",null);
      Form form = new Form("NewsTable", "GET", null,null,null);
      String vCols = "News_ID, Caption, News_Dt, Matter, Orginator";
      form.add(new FormHidden("pnStartPos",pnStartPos,null)); 
      form.add(new FormHidden("pnRows",count,null)); 
      form.add(new NL(1));
      form.add(NewsForm.createNewsTable(vCols,Integer.parseInt(pnStartPos)));
      body.add(form);
      page.add(head);
      page.add(body);
      out.println(page);
    } 
}
