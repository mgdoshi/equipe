import ingen.html.*;
import ingen.html.frame.*;
import ingen.html.para.*;
import ingen.html.character.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import java.awt.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DelTempl extends HttpServlet
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
     String ctempl = request.getParameter("ClsTempl");
     String vTemplName = null;

     out = response.getWriter();     
     out.println("<html><head>");
    
     WebUtil util = new WebUtil();
     Script scr = null;
     String str[]= new String[7];
     Rectangle rect[] =  new Rectangle[7]; 
     String tag = null;
     int bid = -1;
     int sid = -1;

     if(ctempl.equalsIgnoreCase("ordtempl1"))
     {
       vTemplName = "OrdTempl1";       
       rect[0] = new Rectangle(0,0,100,25);  
       rect[1] = new Rectangle(20,65,75,75);  
       rect[2] = new Rectangle(120,60,320,25);  
       rect[3] = new Rectangle(120,87,320,75);  
       rect[4] = new Rectangle(500,65,100,75);  
       rect[5] = new Rectangle(15,170,100,100);  
       rect[6] = new Rectangle(350,170,100,100);  
     }
     else if(ctempl.equalsIgnoreCase("ordtempl2"))
     {
       vTemplName = "OrdTempl2";
       rect[0] = new Rectangle(0,0,100,25);  
       rect[1] = new Rectangle(330,70,75,75);  
       rect[2] = new Rectangle(15,60,320,25);  
       rect[3] = new Rectangle(15,90,320,75);  
       rect[4] = new Rectangle(500,65,100,75);  
       rect[5] = new Rectangle(15,170,100,100);  
       rect[6] = new Rectangle(350,170,100,100);  
     }
     else if(ctempl.equalsIgnoreCase("ordtempl3"))
     {
       vTemplName = "OrdTempl3";
       rect[0] = new Rectangle(0,0,100,25);  
       rect[1] = new Rectangle(500,70,75,75);  
       rect[2] = new Rectangle(20,65,350,25);  
       rect[3] = new Rectangle(20,90,350,40);  
       rect[4] = new Rectangle(20,135,100,75);  
       rect[5] = new Rectangle(15,190,100,100);  
       rect[6] = new Rectangle(350,190,100,100);  
     }


     if( Browser.equals("1") )
     {
       scr = DelTemplForm.ShowNetsScript( vPID );
       tag = "</Layer>";
       str[0] = "<Layer id=\"player\" left="+rect[0].x+" top="+rect[0].y+" width="+rect[0].width+"% height="+rect[0].height+" border=\"0\" visibility=\"show\"> ";
       str[1] = "<Layer id=\"ilayer\" left="+rect[1].x+" top="+rect[1].y+" width="+rect[1].width+" height="+rect[1].height+" border=\"0\" visibility=\"show\"> ";
       str[2] = "<Layer id=\"clayer\" left="+rect[2].x+" top="+rect[2].y+" width="+rect[2].width+" height="+rect[2].height+" border=\"0\" visibility=\"show\"> ";
       str[3] = "<Layer id=\"alayer\" left="+rect[3].x+" top="+rect[3].y+" width="+rect[3].width+" height="+rect[3].height+" border=\"0\" visibility=\"show\"> ";
       str[4] = "<Layer id=\"olayer\" left="+rect[4].x+" top="+rect[4].y+" border=\"0\"> ";
       str[5] = "<Layer id=\"blayer\" left="+rect[5].x+" top="+rect[5].y+" width="+rect[5].width+"% height="+rect[5].height+" border=\"2\" visibility=\"show\">";
       str[6] = "<Layer id=\"slayer\" left="+rect[6].x+" top="+rect[6].y+" width="+rect[6].width+"% height="+rect[6].height+" border=\"2\" visibility=\"show\"> ";
       bid = 1;
       sid = 2;
     }
     else
     {
       scr = DelTemplForm.ShowIntExplScript( vPID );
       tag = "</Div>";
       str[0] = "<div id=\"player\" style=\"position:absolute; width:"+rect[0].width+"%; height:"+rect[0].height+"; left: "+rect[0].x+"; top: "+rect[0].y+"; visibility:visible\">";
       str[1] = "<div id=\"ilayer\" style=\"position:absolute; width:"+rect[1].width+"; height:"+rect[1].height+"; left: "+rect[1].x+"; top: "+rect[1].y+"; visibility:visible\">";
       str[2] = "<div id=\"clayer\" style=\"position:absolute; width:"+rect[2].width+"; height:"+rect[2].height+"; left: "+rect[2].x+"; top: "+rect[2].y+"; visibility:visible\">";
       str[3] = "<div id=\"alayer\" style=\"position:absolute; width:"+rect[3].width+"; height:"+rect[3].height+"; left: "+rect[3].x+"; top: "+rect[3].y+"; visibility:visible\">";
       str[4] = "<div id=\"olayer\" style=\"position:absolute; left:"+rect[4].x+"; top:"+rect[4].y+"\"> ";
       str[5] = "<div id=\"blayer\" style=\"position:absolute; width:"+rect[5].width+"%; height:"+rect[5].height+"; left: "+rect[5].x+"; top: "+rect[5].y+"; visibility:visible\">";
       str[6] = "<div id=\"slayer\" style=\"position:absolute; width:"+rect[6].width+"%; height:"+rect[6].height+"; left: "+rect[6].x+"; top: "+rect[6].y+"; visibility:visible\">";
       bid = 3;
       sid = 4;
     }

     out.println(scr);
     out.println("<title>Template 1</title> ");
     out.println("</head> ");
     out.println("<body BACKGROUND=\"/ordimg/BACKGR2.GIF\"><BR> ");

     out.println(str[0]);
     out.println("<b><font face=\"Arial,Helvetica\" color=\"#3333FF\" size=+2>&nbsp; ");
     out.println("<CENTER><A href=\"JavaScript:getChallanTitle('Enter Challan Title : ',document.forms[0].text2.value)\">Delivery Challan</A></font></b></CENTER>");
     out.println(tag);
     out.println("<BR>");

     out.println(str[1]);
     out.println("<A href=\"JavaScript:getLogo()\"><img SRC=\"/ordimg/logo.jpg\" border=\"0\" height=75 width=75 align=ABSCENTER></A> ");
     out.println(tag);

     out.println(str[2]);
     out.println("  <font face=\"Arial,Helvetica\" color=\"#3333FF\" size=+2> ");
     out.println("    <A href=\"JavaScript:getCompanyName('Enter Company Name : ',document.forms[0].text1.value)\">Company Name</A> ");
     out.println("  </font> ");
     out.println(tag);

     out.println(str[3]);
     out.println("  <A href=\"JavaScript:getAddress('Enter Company Address :',document.forms[0].text3.value)\" STYLE=\"text-decoration:none\"> ");
     out.println("   <font face=\"Arial,Helvetica\" color=\"#000000\" size=-1> ");
     out.println("       Company Address, City, State, Pin Code, Phone Number, Fax Number ");
     out.println("   </font> ");
     out.println("  </A> "); 
     out.println(tag);

     out.println(str[4]);
     if(ctempl.equalsIgnoreCase("ordtempl3"))
     {
       out.println("  <b><font color=\"#3333FF\">Delivery Nr.</font></b>&nbsp;&nbsp; ORDTIN121");
       out.println("  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font color=\"#3333FF\">Delivery Dt.</font></b>&nbsp;&nbsp; 12.12.1999 ");
     }
     else
     {
       out.println("  <b><font color=\"#3333FF\">Delivery Nr.</font></b>&nbsp;&nbsp; ORDTIN121<BR> ");
       out.println("  <b><font color=\"#3333FF\">Delivery Dt.</font></b>&nbsp;&nbsp; 12.12.1999 ");
     }
     out.println(tag);

     out.println("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"100%\" >");
     out.println("<tr><td width=50%>");
     out.println(str[5]);
     out.println("<b>Shipment From :</b>");
     out.println("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"80%\" >");
     out.println("<tr><td width=\"30%\">&nbsp;Name </td><td>-----------------------</td></tr>");
     out.println("<tr><td>&nbsp;Address  </td><td>----------------------</td></tr>");
     out.println("<tr><td>&nbsp;City </td><td>----------</td></tr>");
     out.println("<tr><td>&nbsp;State </td><td>------------</td></tr>");
     out.println("<tr><td>&nbsp;Pin Code </td><td>----------------</td></tr>");
     out.println("<tr><td>&nbsp;Phone No </td><td>---------</td></tr>");
     out.println("<tr><td>&nbsp;Fax No  </td><td>----------</td></tr>");
     out.println("</table>");
     out.println(tag);
     out.println("</td><td>");
     out.println(str[6]);
     out.println("<b>Transport Address :</b>");
     out.println("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"80%\" >");
     out.println("<tr><td width=\"30%\">&nbsp;Name </td><td>-----------------------</td></tr>");
     out.println("<tr><td>&nbsp;Address  </td><td>----------------------</td></tr>");
     out.println("<tr><td>&nbsp;City </td><td>----------</td></tr>");
     out.println("<tr><td>&nbsp;State </td><td>------------</td></tr>");
     out.println("<tr><td>&nbsp;Pin Code </td><td>----------------</td></tr>");
     out.println("<tr><td>&nbsp;Phone No </td><td>---------</td></tr>");
     out.println("<tr><td>&nbsp;Fax No  </td><td>----------</td></tr>");
     out.println("</table>");
     out.println(tag);
     out.println("</td></tr>");
     out.println("</table>");

     out.println("<form action=\"/JOrder/servlets/DelTempl\" method=\"POST\" target=\"mid_frame\"> ");
     out.println("<Input type=\"hidden\" name=\"ordtempl\" value=\""+vTemplName+"\"> ");
     out.println("<Input type=\"hidden\" name=\"logoname\" value=\"\"> ");
     out.println("<Input type=\"hidden\" name=\"text1\" value=\"\"> ");
     out.println("<input type=\"hidden\" name=\"text2\" value=\"\"> ");
     out.println("<input type=\"hidden\" name=\"text3\" value=\"\"> ");
     out.println("<input type=\"hidden\" name=\"billaddr_id\"> ");
     out.println("<input type=\"hidden\" name=\"shipaddr_id\"> ");
     out.println("<input type=\"hidden\" name=\"note\"> ");
     out.println("<input type=\"hidden\" name=\"Instruct\"> ");
     out.println("<input type=\"hidden\" name=\"rem1\"> ");
     out.println("<input type=\"hidden\" name=\"rem2\"> ");
     out.println("<input type=\"hidden\" name=\"rem3\"> ");
     if(ctempl.equalsIgnoreCase("ordtempl3"))
     {
       out.println("<BR><BR><BR><BR><BR><BR>");
       out.println("<table width=\"47%\">  ");
       out.println("<tr><td align=\"right\"> ");
       out.println("SysDate<INPUT Type=\"Radio\" name=\"odateflg\" value=\"0\" CHECKED>&nbsp;&nbsp;Enterable<INPUT Type=\"Radio\" name=\"odateflg\" value=\"1\"> ");
       out.println("</tr></td> ");
       out.println("</table> ");
       out.println("<BR><BR><BR><BR><BR><BR><BR><BR><BR>");
     }
     else
     {
       out.println("<BR><BR><BR>"); 
       out.println("<table width=\"85%\">  ");
       out.println("<tr><td align=\"right\"> ");
       out.println("SysDate<INPUT Type=\"Radio\" name=\"odateflg\" value=\"0\" CHECKED>&nbsp;&nbsp;Enterable<INPUT Type=\"Radio\" name=\"odateflg\" value=\"1\"> ");
       out.println("</tr></td> ");
       out.println("</table> ");
       out.println("<BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR> ");
     }
     out.println("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"80%\" > ");
     out.println("<tr><td> ");
     out.println("&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"button\" value=\"Select\" onClick=\"callTable("+bid+")\">&nbsp;<input type=\"button\" value=\"New\" onClick=\"callForm("+bid+")\">&nbsp;&nbsp;Readonly<INPUT Type=\"Radio\" name=\"billid\" value=\"0\" CHECKED>&nbsp;&nbsp;Enterable<INPUT Type=\"Radio\" name=\"billid\" value=\"1\"> ");
     out.println("</td><td> ");
     out.println("<input type=\"button\" value=\"Select\" onClick=\"callTable("+sid+")\">&nbsp;<input type=\"button\" value=\"New\" onClick=\"callForm("+sid+")\">&nbsp;&nbsp;Readonly<INPUT Type=\"Radio\" name=\"shipid\" value=\"0\" CHECKED>&nbsp;&nbsp;Enterable<INPUT Type=\"Radio\" name=\"shipid\" value=\"1\"> ");
     out.println("</td></tr> ");
     out.println("</table> ");
     out.println("<BR> ");
     out.println("<table  BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=3 WIDTH=\"70%\" > ");
     out.println("<TR> ");
     out.println("<TD width=\"10%\"><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Note</Font></b></TD> ");
     out.println("<TD><font face=\"Arial,Helvetica\" size=-1><INPUT Type=\"Text\" name=\"notes\" Size=50 MaxLength=100></Font></TD> ");
     out.println("<TD>&nbsp;&nbsp;<INPUT Type=\"CheckBox\" name=\"note\"></TD> ");
     out.println("</TR> ");
     out.println("</table> ");
     out.println("<BR> ");
     out.println("<hr ALIGN=LEFT SIZE=4 WIDTH=\"80%\"> ");
     out.println("<br><b><i>&nbsp;&nbsp;&nbsp;&nbsp;Delivery Details are Entered here at the time of Creating the Delivery</i></b> ");
     out.println("<BR><BR> ");
     out.println("<hr ALIGN=LEFT SIZE=4 WIDTH=\"80%\"> ");
     out.println("<table  BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=3 WIDTH=\"80%\" > ");
     out.println("<TR> ");
     out.println("<TD width=\"15%\" vAlign=\"Top\"><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Instructions</font></B></TD> ");
     out.println("<TD><TextArea Name=Inst Rows=3 Cols=50></TextArea></TD> ");
     out.println("<TD vAlign=\"Top\">&nbsp;&nbsp;<INPUT Type=\"CheckBox\" name=\"Instruct\"></TD> ");
     out.println("</TR> ");
     out.println("<TR> ");
     out.println("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark1</Font></B></TD> ");
     out.println("<TD><font face=\"Arial,Helvetica\" size=-1><INPUT Type=\"Text\" name=\"remark1\" Size=50 MaxLength=100></Font></TD> ");
     out.println("<TD>&nbsp;&nbsp;<INPUT Type=\"CheckBox\" name=\"rem1\"></TD> ");
     out.println("</TR>");
     out.println("<TR>");
     out.println("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark2</Font></B></TD>");
     out.println("<TD><font face=\"Arial,Helvetica\" size=-1><INPUT Type=\"Text\" name=\"remark2\" Size=50 MaxLength=100></Font></TD>");
     out.println("<TD>&nbsp;&nbsp;<INPUT Type=\"CheckBox\" name=\"rem2\"></TD>");
     out.println("</TR>");
     out.println("<TR>");
     out.println("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark3</Font></B></TD>");
     out.println("<TD><font face=\"Arial,Helvetica\" size=-1><INPUT Type=\"Text\" name=\"remark3\" Size=50 MaxLength=100></Font></TD>");
     out.println("<TD>&nbsp;&nbsp;<INPUT Type=\"CheckBox\" name=\"rem3\"></TD>");
     out.println("</TR>");
     out.println("</table>");
     out.println("<br>&nbsp;");
     out.println("<Center>Template Name "+util.getBlankSpaces(3)+"<Input type=\"text\" name=\"vAliasName\" size=\"20\" maxlength=\"30\" onBlur=\"this.value=this.value.toUpperCase()\">"+util.getBlankSpaces(5)+"<input type=\"button\" value=\"Submit\" onClick=\"submit_form()\"></Center>");
     out.println("</form>");
     out.println("  </body> ");
     out.println("  </html> ");
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
       int nMsgID=-1;
       String errMsg=null;  
       String nLangID=null;
       String nUserID=null;
       nUserID   = Parse.GetValueFromString( vPID, "UserID" );
       nLangID   = Parse.GetValueFromString( vPID, "LangID" );

       String ordtempl = request.getParameter("ordtempl");
       String logoname = request.getParameter("logoname");
       String cname = Parse.formatStr(request.getParameter("text1"));
       String tname = Parse.formatStr(request.getParameter("text2"));
       String addr = Parse.formatStr(request.getParameter("text3"));
       String billaddrid = request.getParameter("billaddr_id");
       String shipaddrid = request.getParameter("shipaddr_id");
       String odateflg = request.getParameter("odateflg");
       String note[] = request.getParameterValues("note");
       String Instruct[] = request.getParameterValues("Instruct");
       String rem1[] = request.getParameterValues("rem1");
       String rem2[] = request.getParameterValues("rem2");
       String rem3[] = request.getParameterValues("rem3");
       String billid = request.getParameter("billid");
       String shipid = request.getParameter("shipid");
       String Inst = Parse.formatStr(request.getParameter("Inst"));
       String remark1 = Parse.formatStr(request.getParameter("remark1"));
       String remark2 = Parse.formatStr(request.getParameter("remark2"));
       String remark3 = Parse.formatStr(request.getParameter("remark3"));
       String notes = Parse.formatStr(request.getParameter("notes"));
       String vAliasName = request.getParameter("vAliasName");

       Statement stmt = null;
       Connection conn = null;
       ResultSet rs = null;
 
       DBConnect db = new DBConnect();
       Valid val = new Valid();
       Message msg = new Message(); 

       try
       {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();          
          String query = "INSERT INTO T_ClsTemplDel( Ref_ID, Templ_Name, Text1, Text2, "+
                  "   Text3, Text4, Text5, Text6, Text7, Text8, Text9, Text10, "+
                  "   Flag1, Flag2, Flag3, Flag4, Flag5, Flag6, Flag7, Flag8, Flag9,"+
                  "   Flag10, num1, num2, num3, Alias_Name ) " +
                  "  VALUES ("+nUserID+","+val.IsNull(ordtempl)+","+
                  " "+val.IsNull(tname)+" ,"+val.IsNull(logoname)+","+
                  " "+val.IsNull(cname)+","+val.IsNull(notes)+","+
                  " "+val.IsNull(remark1)+","+val.IsNull(remark2)+","+
                  " "+val.IsNull(remark3)+",null,"+
                  " "+val.IsNull(addr)+","+val.IsNull(Inst)+","+
                  " "+odateflg+","+billid+","+shipid+","+note[0]+","+
                  " "+Instruct[0]+","+rem1[0]+","+rem2[0]+","+rem3[0]+",null,null,"+
                  " "+val.IsNull(billaddrid)+","+val.IsNull(shipaddrid)+",null,"+
                  " "+val.IsNull(vAliasName)+")";
          stmt.executeUpdate(query);
          nMsgID = 3;
        }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
        finally
        {
          try
          {
            if(stmt!=null)
              stmt.close();
            if(conn!=null)  
              conn.close();
          }catch(SQLException sexe){}
        }
        if( nMsgID <= 5 )
        {
          response.sendRedirect("/ordhtm/del.html");
          
        }
        else
          out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   } 
}
