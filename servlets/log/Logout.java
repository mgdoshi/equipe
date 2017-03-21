import ingen.html.db.*;
import ingen.html.util.*;
import ingen.html.para.*;
import ingen.html.table.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.character.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class Logout extends HttpServlet
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

        int nLangID=0;
        int nUserID=0;

        String temp=null;
        String vFormType=null;


        ConfigData cdata = new ConfigData();
        WebUtil util = new WebUtil();

        if((temp=Parse.GetValueFromString( vPID, "UserID" ))!=null && !temp.equals("null"))     
           nUserID   = Integer.parseInt(temp);
        if((temp=Parse.GetValueFromString( vPID, "LangID" ))!=null && !temp.equals("null"))     
           nLangID    = Integer.parseInt(temp);

        vFormType = cdata.GetConfigValue( "SY_USER", nLangID, "WD_LOGOUT", "Logout Form" );
       
        Page page = new Page();
        Head head = new Head();

        Body body = new Body("/ordimg/BACKGR2.GIF",null);
        Form form = new Form("Logout","POST",null,null,null);
  
        Script scr = new Script( "JavaScript", null );
        HtmlTag scrdata = new HtmlTag();
        scrdata.add("<!-- Start Hidding" + "\n" +
                    "top.show_form(\""+vFormType+"\")" + "\n" +
                    "// End Hidding -->");
        scr.add(scrdata);

        Center cen = new Center();
        NL nl = new NL(5);
        Font f = new Font("green",null,"4",null);
        HtmlTag tem = new HtmlTag();
        Message msg = new Message();
        Bold b = new Bold( "Do you want to LOGOUT of the Order Tracking System...? " ,null);
        tem.add(b);
        NL nl1 = new NL(5);
        FormSubmit sub = new FormSubmit("iAction"," OK ", null);
        FormSubmit sub1 = new FormSubmit("iAction", "Cancel",null);
        cen.add( nl );
        cen.add( tem );
        cen.add( nl1 );
        cen.add(sub + "&nbsp;&nbsp;&nbsp;" + sub1);
        form.add(cen);
        head.add( scr );
        body.add( form );
        page.add( head );
        page.add( body );
        out.println(page);
    }       

   public void doPost(HttpServletRequest request,
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

      int nLangID=0;
      int nAuditID=0;
      String temp=null;

      IngDate dt = new IngDate();
      DBConnect db = new DBConnect();     
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      String iAction = request.getParameter("iAction");
      if( iAction!=null && iAction.equalsIgnoreCase(" OK "))
      {
        if((temp=Parse.GetValueFromString( vPID, "AuditID" ))!=null && !temp.equals("null"))
           nAuditID   = Integer.parseInt(temp);
        if((temp=Parse.GetValueFromString( vPID, "LangID" ))!=null && !temp.equals("null"))     
           nLangID    = Integer.parseInt(temp);

        try
        {
          Connection conn = db.GetDBConnection();
          Statement stmt = conn.createStatement();
          String query = " UPDATE T_Audit "+
                         " SET Logout_Dt = '"+dt+"'"+
                         " WHERE Audit_ID = "+nAuditID;
          stmt.executeUpdate(query);
          stmt.close();
          conn.close();              
        }catch(Exception sexe){System.out.println(sexe);}
        PkCookie.removeCookie(response,"PID");
        Page page = new Page();
        Head head = new Head();
        Body body = new Body("/ordimg/BACKGR2.GIF",null);
        Script scr = new Script( "JavaScript", null );
        HtmlTag scrdata = new HtmlTag();
        scrdata.add("<!-- Start Hidding" + "\n" +
                    "    top.bot_frame.enableMenuApplet(0)           \n" +
                    "    top.bot_frame.document.MenuApplet.Logout()  \n" +
                    "// End Hidding -->");
        scr.add(scrdata);
        Form form = new Form(null,null,null,null,null);
        Center cen = new Center();
        Font f = new Font("black",null,"5",null);
        HtmlTag tem = new HtmlTag();
        Message msg = new Message();
        NL nl = new NL(4);
        Bold b = new Bold( " Thanks For Using... ",null);
        b.setFormat(f);
        tem.add(nl);
        tem.add(b);
        Font f1 = new Font("blue",null,"6",null);
        NL nl1 = new NL(2);
        tem.add(nl1);
        Bold b1 = new Bold(" Order Tracking System ",null);
        b1.setFormat(f1);
        tem.add(b1);
        Font f2 = new Font("black",null,"3",null);
        NL nl2 = new NL(1);
        tem.add(nl2);
        Bold b2 = new Bold(" Please Click on LOGIN to re-enter...",null);
        b2.setFormat(f2);
        tem.add(b2);
        NL nl3 = new NL(1);
        tem.add(nl3);
        cen.add(tem);
        form.add(cen);
        Script scr1 = new Script( "JavaScript", null );
        HtmlTag scrdata1 = new HtmlTag();
        scrdata1.add("<!-- Start Hidding" + "\n" +
                    "  top.show_status(\""+ msg.GetMsgDesc( 50 , nLangID) + "\")  "+
                    "// End Hidding -->");
        scr1.add(scrdata1);
        head.add(scr);
        head.add(scr1);
        body.add(form);
        page.add(head);
        page.add(body);
        out.println(page); 
     }
     else
     {
        Page page = new Page();
        Body body = new Body("/ordimg/BACKGR2.GIF",null);
        Center cen = new Center();
        Font f = new Font("blue",null,"6",null);
        HtmlTag tem = new HtmlTag();
        Message msg = new Message();
        NL nl = new NL(4);
        Bold b = new Bold(" Order Entry " +"&"+" Tracking System",null);
        b.setFormat(f);
        tem.add(nl);
        tem.add(b);
        Font f1 = new Font("black",null,"3",null);
        NL nl1 = new NL(2);
        tem.add(nl1);
        f1.add(" Copyright "+"&"+"copy; 1999");
        tem.add(f1); 
        Font f2 = new Font("black",null,"4",null);
        NL nl2 = new NL(2);
        tem.add(nl2);
        f2.add(" Ingenium Computing Private Limited, Pune - INDIA");
        tem.add(f2);
        Font f3 = new Font("black",null,"3",null);
        NL nl3 = new NL(2);
        tem.add(nl3);
        f3.add("Email : ingenium@wminet.net");
        tem.add(f3);
        cen.add(tem);
        body.add(cen);
        page.add(body);
        out.println(page); 
     }
   }

}
