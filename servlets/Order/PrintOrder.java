import ingen.html.*;
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

public class PrintOrder extends HttpServlet
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

      String pnOrderID = request.getParameter("pnOrderID");
      String vTemplName = request.getParameter("vTemplName");
      String OrderDt = null;
      String OrderStat = null;
      String nClientID = null;
      DBConnect db = new DBConnect();
      Connection conn = null;
	  Statement stmt = null;
	  ResultSet rs = null;

      try
      {
        conn =  db.GetDBConnection();
        stmt = conn.createStatement();
        String query =  " SELECT Order_Nr, Order_Dt, DM_OrdStat, Fk_Client_ID "+
                        " FROM   T_Order ord "+
                        " WHERE  ord.Order_ID = "+ pnOrderID;

        rs = stmt.executeQuery(query);
        if(rs.next())
        {
          OrderDt = rs.getString(2);
          OrderStat = rs.getString(3);
          nClientID = rs.getString(4);
        }
 
      }catch(SQLException sexe){System.out.println(sexe.getMessage());}
      finally 
      {
        try
        {
          if(stmt!=null)
            stmt.close();
          if(conn!=null)
            conn.close();
        }catch(SQLException sexe){System.out.println(sexe.getMessage());}
      }

      response.sendRedirect("/JOrder/servlets/PrintOrderList?vFrom_Order_ID="+pnOrderID+"&vTo_Order_ID="+pnOrderID+"&dFrom_Order_Dt="+IngDate.dateToStr(OrderDt)+"&dTo_Order_Dt="+IngDate.dateToStr(OrderDt)+"&cDM_OrdStat="+OrderStat+"&pnClientID="+nClientID+"&vTemplName="+vTemplName);
  }
}