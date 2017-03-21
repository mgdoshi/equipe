import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class NewsEntry extends HttpServlet
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

      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;

      String rParam[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;

      String pvMode = request.getParameter("pvMode");
      String pnNewsID = request.getParameter("pnNewsID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
 
      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      if(pvMode!=null && pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "SY_NEWS", nLangID, "WD_FORM_INSERT","News / I");                      
      else if(pvMode!=null && pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "SY_NEWS", nLangID, "WD_FORM_UPDATE","News / U");                      

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_News/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnNewsID!=null && !pnNewsID.equals("") && !pnNewsID.equalsIgnoreCase("null"))
      { 
        rParam = db.getRecord( pnNewsID, "News" );
      }  
      else
        rParam = new String[7];  

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/NewsEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add(new FormHidden("pnNewsID", pnNewsID, null ));
      form.add(new FormHidden("vAction", null, null ));
      form.add(new NL(2));

      Table tab = new Table("1","center","Border=\"0\" width=\"75%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_NEWS", nLangID, "BL_LABEL.B_NEWS_CAPTION","Caption");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"caption.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"16%\"");
      col.add(WebUtil.NotNull);
      row.add(col);
      row.add(new TableCol(util.createTextItem( nUserID, "SY_NEWS", "vCaption", pvMode, "30", "100", rParam[1], null, vTextAttrib),null,null,null,null)) ;
      tab.add(row);  

      Table tab1 = new Table("1","center","Border=\"0\" width=\"75%\" COLS=4");
      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_NEWS", nLangID, "BL_LABEL.B_NEWS_ORGINATOR","Orginator");
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"orginator.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"14%\"");
      col1.add(WebUtil.NotNull);
      row1.add(col1);
      row1.add(new TableCol(util.createTextItem( nUserID, "SY_NEWS", "vOrginator", pvMode, "10", "100", rParam[4], null, vTextAttrib),null,null,null,null)) ;

      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_NEWS", nLangID, "BL_LABEL.B_NEWS_NEWS_DATE","Date");
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Date.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"14%\"");
      col2.add(WebUtil.NotNull);
      row2.add(col2);
      row2.add(new TableCol(util.createTextItem( nUserID, "SY_NEWS", "dNews_Date", pvMode, "10", "10",IngDate.dateToStr(rParam[2]), "onBlur=\"top.check_date(this)\"", vTextAttrib),null,null,null,null)) ;
      row2.add(new TableCol( util.createLabelItem( "DD.MM.YYYY", vLabelAttrib), null, null, null,null));

      tab1.add(row1);  
      tab1.add(row2); 

      Table tab2 = new Table("1","center","Border=\"0\" width=\"75%\" COLS=2");
      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_NEWS", nLangID, "BL_LABEL.B_NEWS_MATTER","Matter");
      TableCol col3 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Matter.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"19%\" VALIGN=\"Top\" ");
      col3.add(WebUtil.NotNull);
      row3.add(col3);
      row3.add( new TableCol(util.createTextAreaItem( nUserID, "SY_NEWS", "vMatter", pvMode, "5", "35", rParam[3], null, vTextAttrib),null,null,null,"WIDTH=\"25%\"")) ;
      tab2.add(row3); 

      form.add(tab); 
      form.add(tab1); 
      form.add(tab2); 
      body.add(form);
      head.add(scr);
      page.add(head);
      page.add(body);
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

      String nLangID=null;
      String nUserID=null;
      int nMsgID=-1;

      String errMsg=null;  
      String vStatus=null;
      String query = null;
      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      Message msg = new Message(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();
  
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String pnTransID = request.getParameter("pnTransID");
      String pnNewsID = request.getParameter("pnNewsID");
      String vCaption   = Parse.formatStr(request.getParameter("vCaption"));
      String dNews_Date = request.getParameter("dNews_Date");
      String vMatter    = Parse.formatStr(request.getParameter("vMatter"));
      String vOrginator = Parse.formatStr(request.getParameter("vOrginator"));
      String vAction   = request.getParameter("vAction"); 

      String usr = db.getName( nUserID, "User" );      
      vStatus    = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction!=null &&  (vAction.equalsIgnoreCase("Insert") || vAction.equalsIgnoreCase("SaveInsert") ))
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            int news_id = Integer.parseInt(db.getNextVal("S_Param"));
            query = "INSERT INTO T_News ( News_ID, Caption, News_Dt, Matter, Orginator, Modifier, Change_Dt  ) " +
                    " VALUES ("+news_id+", '"+vCaption+"', '"+IngDate.strToDate(dNews_Date)+"','"+vMatter+"','"+vOrginator+"','"+usr+"', '"+dt+"')";
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
          Trans.setTransID(pnTransID);
        }
        else if( vAction!=null && vAction.equalsIgnoreCase("Delete") )
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            int news_id = Integer.parseInt(db.getNextVal("S_Param"));
            query = "DELETE FROM T_News WHERE News_id = " + pnNewsID;
            stmt.executeUpdate(query);
            nMsgID = 4;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=7;}
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
          Trans.setTransID(pnTransID);
        }
        else if(vAction!=null &&  vAction.equalsIgnoreCase("Update"))
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();       
            query = "UPDATE T_News SET Caption = '"+vCaption+"'," +
                    " News_Dt = '"+IngDate.strToDate(dNews_Date)+"'," + 
                    " Matter = '"+vMatter+"'," + 
                    " Orginator = '"+vOrginator+"'," + 
                    " Modifier    = '"+usr+"'," + 
                    " Change_Dt   = '"+dt+"'" + 
                    " WHERE News_ID = "+pnNewsID;
            stmt.executeUpdate(query);
            nMsgID = 5;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=8;}
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
          Trans.setTransID(pnTransID);
        }
      }
      if( nMsgID <= 5 )
      { 
        if(vAction!=null && vAction.equalsIgnoreCase("SaveInsert"))
          response.sendRedirect("/JOrder/servlets/NewsFrame?pvMode=I&pnNewsID=&pnStartPos=&pnRows=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
        else
          response.sendRedirect("/JOrder/servlets/NewsFrame?pvMode=&pnNewsID=&pnStartPos=1&pnRows=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
      }
      else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

