import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import ingen.html.character.*;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SecRep extends HttpServlet
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
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vClientQry=null;
      String vUserQry=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      DBConnect db = new DBConnect(); 
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      vFormType = cdata.GetConfigValue( "SY_SECREP", nLangID, "WD_QUERY","Security Report / Q");                      

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Order/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );
    
      vClientQry = " SELECT cln.Client_ID, cln.Client_Name ||'-'|| cln.Client_Desc "+
                   " FROM T_Client cln ";
      vUserQry   = " SELECT usr.User_ID, usr.User_Name ||'-'|| usr.User_Desc "+
                   " FROM T_User usr ";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/SecRep","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add( new FormHidden("vOrdStat", null, null ));
      form.add(new NL(4));

      Table tab = new Table("1","center","Border=\"0\" width=\"60%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_SELECT_CLIENT_NAME","Select Client");
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"OrdStat.gif", vBPLate, vLabelAttrib ),null, null, null," WIDTH=\"20%\""));
      row.add(new TableCol(util.createList( nUserID, "SY_SECREP", "pnClientID","Q", vClientQry,null, null, vListAttrib), null,null,null,null));

      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_SECREP", nLangID, "BL_LABEL.B_REPORT_DATE_FROM","Date From");
      row1.add(new TableCol(util.getBlankSpaces(2),null, null, null,null)) ;
			
      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_SECREP", nLangID, "BL_LABEL.B_SECREP_SELECT_USER_NAME","Select User");
      row2.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null)) ;
      row2.add(new TableCol(util.createList( nUserID, "SY_SECREP", "pnUserID","Q", vUserQry,null, null, vListAttrib), null,null,null,null));

      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      form.add(tab);
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
      String vBPLate=null; 
      String vBPLate1=null;
      String nLangID=null;
      String nUserID=null;
      String nTransID=null;
      String nSchemeID=null;
      String vLabelAttrib=null;
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );

      String pnClientID = request.getParameter("pnClientID");
      String pnUserID = request.getParameter("pnUserID");


      DBConnect db = new DBConnect();
      Connection conn = null;
      Statement stmt = null;
      PreparedStatement pstmt = null;
      PreparedStatement pstmt1 = null;
      PreparedStatement pstmt2 = null;
      ResultSet rs = null;
      ResultSet rs1 = null;
      ResultSet rs2 = null;
      ResultSet rs3 = null;
      WebUtil util = new WebUtil();
      ConfigData cdata = new ConfigData(); 

      String cClient = " SELECT cln.Client_ID, cln.Client_Code, cln.Client_Name,cln.Client_Desc,cln.DM_ClnType " +
                       " FROM T_Client cln ";
      if(!(pnClientID==null || pnClientID.equalsIgnoreCase("null")
         || pnClientID.equalsIgnoreCase("")) ) 
            cClient += " WHERE cln.Client_ID = "+pnClientID+" ";

      String cUser = " SELECT usr.User_ID,usr.User_Name,usr.User_Desc " +
                     " FROM   T_User usr " +
                     " WHERE  usr.Fk_Client_ID = ? ";
      if(!(pnUserID==null || pnUserID.equalsIgnoreCase("null")
         || pnUserID.equalsIgnoreCase("")) ) 
         cUser += " AND usr.User_ID = "+pnUserID+" ";


      String cEmployee = " SELECT emp.Employee_ID,emp.Employee_Name,emp.Employee_Desc " +
                         " FROM   T_Employee emp, T_User usr " +
                         " WHERE  emp.Employee_ID = usr.Fk_Employee_ID " +
                         " AND    usr.User_ID = ? ";

      String cEmplFunc = " SELECT emf.DM_FuncType " + 
                         " FROM   T_Employee emp, T_EmployeeFunc emf " + 
                         " WHERE  emp.Employee_ID = emf.Fk_Employee_ID " + 
                         " AND    emp.Employee_ID = ?";

      String cRecSec = " SELECT rec.RecSec_ID, rec.RecSec_Name,rec.RecSec_Desc " +
                       " FROM   T_RecSec rec, T_User usr " + 
                       " WHERE  usr.Fk_RecSec_ID = rec.RecSec_ID " +
                       " AND    usr.User_ID = ?";
 
      String cAssgClient = " SELECT cln.Client_ID,cln.Client_Name,cln.Client_Desc " +
                       " FROM   T_Client cln, T_UserClient ucl " +
                       " WHERE  ucl.Fk_Client_ID = cln.Client_ID " +
                       " AND    ucl.Fk_User_ID = ?";

      String cAssgEmployee = " SELECT emp.Employee_ID,emp.Employee_Name,emp.Employee_Desc " +
                           " FROM   T_Employee emp, T_UserEmployee uem " +
                           " WHERE  uem.Fk_Employee_ID = emp.Employee_ID " +
                           " AND    uem.Fk_User_ID = ?";

      String cAssgItem = " SELECT itm.Item_ID,itm.Item_Name,itm.Item_Desc " +
                             " FROM   T_Item itm, T_UserItem uit " +
                             " WHERE  uit.Fk_Item_ID = itm.Item_ID " +
                             " AND    uit.Fk_User_ID = ? ";

      String cAssgRecSec = " SELECT rec.RecSec_Name,rec.RecSec_Desc " +
                           " FROM   T_RecSec rec " + 
                           " WHERE  rec.RecSec_ID IN ( SELECT Managed_RecSec_ID " +
                           " FROM   T_RecSecPriv " + 
                           " WHERE  RecSec_ID = ? )";

      String vFormType = cdata.GetConfigValue( "SY_SECREP", nLangID, "WD_LIST","Security Report / L");                      

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      page.add(head); 
      body.add(new NL());
      Center cen = new Center();
      vBPLate = cdata.GetConfigValue( "SY_SECREP", nLangID, "BL_LABEL.B_SECREP_SYSTEM","System Security Report" );
      cen.setFormat(new Bold()); 
      cen.add(util.createLabelItem(vBPLate," COLOR=\"Green\" SIZE=\"+1\" " + vLabelAttrib));
      body.add(cen);  
      body.add(new NL());

      try
      {
        Table tab = new Table("1","center","Border=\"0\" width=\"100%\" BGCOLOR=\"#FFFFCA\" COLS=2");
        conn = db.GetDBConnection(); 
        stmt = conn.createStatement();
        rs = stmt.executeQuery(cClient);
        while(rs.next())
        {   
          TableRow row = new TableRow("Left",null,null);
          vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_CLIENT_NAME","Client Name");
          row.add(new TableCol(util.createLabelItem(vBPLate, "COLOR=\"Blue\" " + vLabelAttrib),null, null, null,"WIDTH=\"25%\""));
          String ClnDtls = rs.getString(2) + "/" + rs.getString(3) + "/" +
                           rs.getString(4) + "/" +
          Domain.getDomainDescFrmAttrib("CLNTYPE",rs.getString(5),nLangID);  
          row.add(new TableCol(util.createLabelItem(ClnDtls, "COLOR=\"Blue\" " + vLabelAttrib),null, null, null,null));
          tab.add(row); 
          body.add(tab);
          body.add("<HR>");

          Table tab1 = new Table("1","center","Border=\"0\" width=\"100%\" BGCOLOR=\"#FFFFCA\" COLS=2");
          pstmt = conn.prepareStatement(cUser);
          pstmt.setString(1,rs.getString(1));  
          rs1 = pstmt.executeQuery();
          while(rs1.next())
          {    
            TableRow row1 = new TableRow("Left",null,null);
            vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_USER_NAME","User Name");
            TableCol col = new TableCol(util.getBlankSpaces(5),null,null,null," WIDTH=\"25%\"");   
            col.setFormat(new Bold()); 
            col.add(util.createLabelItem(vBPLate, "COLOR=\"Brown\" " + vLabelAttrib));  
            row1.add(col);
            row1.add(new TableCol(util.createLabelItem(rs1.getString(2) + "/" + rs1.getString(3), "COLOR=\"Blue\" " + vLabelAttrib),null, null, null,null));
            tab1.add(row1); 

            pstmt1 = conn.prepareStatement(cEmployee);
            pstmt1.setString(1,rs1.getString(1));  
            rs2 = pstmt1.executeQuery();
            while(rs2.next())
            {    
              TableRow row2 = new TableRow("Left",null,null);
              vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_EMPLOYEE_NAME","Employee Name");
              TableCol col2 = new TableCol(util.getBlankSpaces(10),null,null,null," WIDTH=\"25%\"");   
              col2.setFormat(new Bold()); 
              col2.add(util.createLabelItem(vBPLate,vLabelAttrib));  
              row2.add(col2);
              row2.add(new TableCol(util.createLabelItem(rs2.getString(2) + "/" + rs2.getString(3), "COLOR=\"Blue\" " + vLabelAttrib),null, null, null,null));
              tab1.add(row2); 

              pstmt2 = conn.prepareStatement(cEmplFunc);
              pstmt2.setString(1,rs2.getString(1));  
              rs3 = pstmt2.executeQuery();
              boolean bFlag= true;
              int nCount   = 1;
              while(rs3.next())
              {    
                TableRow row3 = new TableRow("Left",null,null);
                TableCol col3 = null;
                if(bFlag)  
                {
                 vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_EMPLOYEE_FUNCT","Functions Assigned");
                 col3 = new TableCol(util.getBlankSpaces(15),null,null,null," WIDTH=\"25%\"");   
                 col3.setFormat(new Bold()); 
                 col3.add(util.createLabelItem(vBPLate,vLabelAttrib));
                 bFlag=false;   
                }
                else
                  col3 = new TableCol("&nbsp;",null,null,null,null);        
                row3.add(col3);
                TableCol col4 = new TableCol(Integer.toString(nCount++) + "&nbsp;",null, null, null,null);
                col4.add(util.createLabelItem(Domain.getDomainDescFrmAttrib("FUNCTYPE",rs3.getString(1),nLangID),vLabelAttrib)); 
                row3.add(col4);
                tab1.add(row3); 
              }
              if(bFlag)
              {
                 TableRow row4 = new TableRow();
                 vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_EMPLOYEE_FUNCT","Functions Assigned");
                 TableCol col4 = new TableCol(util.getBlankSpaces(15),null,null,null," WIDTH=\"25%\"");   
                 col4.setFormat(new Bold()); 
                 col4.add(util.createLabelItem(vBPLate,vLabelAttrib));
                 row4.add(col4);
                 row4.add(new TableCol(util.createLabelItem("None",vLabelAttrib),null,null,null,null));
                 tab1.add(row4); 
              }  
            }
            TableRow row5 = new TableRow();
            row5.add(new TableCol("&nbsp;",null,null,null,null));
            row5.add(new TableCol("&nbsp;",null,null,null,null));
            tab1.add(row5); 

            pstmt1 = conn.prepareStatement(cRecSec);
            pstmt1.setString(1,rs1.getString(1));  
            rs2 = pstmt1.executeQuery();
            while(rs2.next())
            {
              TableRow row6 = new TableRow("Left",null,null);
              vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_RECSEC_NAME","Record Security Name");
              TableCol col5 = new TableCol(util.getBlankSpaces(10),null,null,null," WIDTH=\"25%\"");   
              col5.setFormat(new Bold()); 
              col5.add(util.createLabelItem(vBPLate,vLabelAttrib));  
              row6.add(col5);
              row6.add(new TableCol(util.createLabelItem(rs2.getString(2) + "/" + rs2.getString(3), vLabelAttrib),null, null, null,null));
              tab1.add(row6); 

              pstmt2 = conn.prepareStatement(cAssgRecSec);
              pstmt2.setString(1,rs2.getString(1));  
              rs3 = pstmt2.executeQuery();
              boolean bFlag= true;
              int nCount   = 1;
              while(rs3.next())
              {    
                TableRow row3 = new TableRow("Left",null,null);
                TableCol col3 = null;
                if(bFlag)  
                {
                 vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_RECSEC_ASSGN","Record Security Assigned");
                 col3 = new TableCol(util.getBlankSpaces(15),null,null,null," WIDTH=\"25%\"");   
                 col3.setFormat(new Bold()); 
                 col3.add(util.createLabelItem(vBPLate,vLabelAttrib));
                 bFlag=false;   
                }
                else
                  col3 = new TableCol("&nbsp;",null,null,null,null);        
                row3.add(col3);
                TableCol col4 = new TableCol(Integer.toString(nCount++) + "&nbsp;",null, null, null,null);
                col4.add(util.createLabelItem(rs3.getString(1) + "/" + rs3.getString(2),vLabelAttrib)); 
                row3.add(col4);
                tab1.add(row3); 
              }
              if(bFlag)
              {
                 TableRow row4 = new TableRow();
                 vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_RECSEC_ASSGN","Record Security Assigned");
                 TableCol col4 = new TableCol(util.getBlankSpaces(15),null,null,null," WIDTH=\"25%\"");   
                 col4.setFormat(new Bold()); 
                 col4.add(util.createLabelItem(vBPLate,vLabelAttrib));
                 row4.add(col4);
                 row4.add(new TableCol(util.createLabelItem("None",vLabelAttrib),null,null,null,null));
                 tab1.add(row4); 
              }  
              tab1.add(row5); 
            }
 
            pstmt1 = conn.prepareStatement(cAssgClient);
            pstmt1.setString(1,rs1.getString(1));  
            rs2 = pstmt1.executeQuery();
            boolean bFlag= true;
            int nCount   = 1;
            while(rs2.next())
            {    
              TableRow row3 = new TableRow("Left",null,null);
              TableCol col3 = null;
              if(bFlag)  
              {
                vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_CLIENT_ASSGN","Clients Assigned");
                col3 = new TableCol(util.getBlankSpaces(10),null,null,null," WIDTH=\"25%\"");   
                col3.setFormat(new Bold()); 
                col3.add(util.createLabelItem(vBPLate,vLabelAttrib));
                bFlag=false;   
              }
              else
                col3 = new TableCol("&nbsp;",null,null,null,null);        
              row3.add(col3);
              TableCol col4 = new TableCol(Integer.toString(nCount++) + "&nbsp;",null, null, null,null);
              col4.add(util.createLabelItem(rs2.getString(2) + "/" + rs2.getString(3),vLabelAttrib)); 
              row3.add(col4);
              tab1.add(row3); 
            }
            if(bFlag)
            {
              TableRow row4 = new TableRow();
              vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_CLIENT_ASSGN","Clients Assigned");
              TableCol col4 = new TableCol(util.getBlankSpaces(15),null,null,null," WIDTH=\"25%\"");   
              col4.setFormat(new Bold()); 
              col4.add(util.createLabelItem(vBPLate,vLabelAttrib));
              row4.add(col4);
              row4.add(new TableCol(util.createLabelItem("None",vLabelAttrib),null,null,null,null));
              tab1.add(row4); 
            }  
            tab1.add(row5); 

            pstmt1 = conn.prepareStatement(cAssgEmployee);
            pstmt1.setString(1,rs1.getString(1));  
            rs2 = pstmt1.executeQuery();
            bFlag= true;
            nCount   = 1;
            while(rs2.next())
            {    
              TableRow row3 = new TableRow("Left",null,null);
              TableCol col3 = null;
              if(bFlag)  
              {
                vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_EMPLOYEE_ASSGN","Employees Assigned");
                col3 = new TableCol(util.getBlankSpaces(10),null,null,null," WIDTH=\"25%\"");   
                col3.setFormat(new Bold()); 
                col3.add(util.createLabelItem(vBPLate,vLabelAttrib));
                bFlag=false;   
              }
              else
                col3 = new TableCol("&nbsp;",null,null,null,null);        
              row3.add(col3);
              TableCol col4 = new TableCol(Integer.toString(nCount++) + "&nbsp;",null, null, null,null);
              col4.add(util.createLabelItem(rs2.getString(2) + "/" + rs2.getString(3),vLabelAttrib)); 
              row3.add(col4);
              tab1.add(row3); 
            }
            if(bFlag)
            {
              TableRow row4 = new TableRow();
              vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_EMPLOYEE_ASSGN","Employees Assigned");
              TableCol col4 = new TableCol(util.getBlankSpaces(15),null,null,null," WIDTH=\"25%\"");   
              col4.setFormat(new Bold()); 
              col4.add(util.createLabelItem(vBPLate,vLabelAttrib));
              row4.add(col4);
              row4.add(new TableCol(util.createLabelItem("None",vLabelAttrib),null,null,null,null));
              tab1.add(row4); 
            }  
            tab1.add(row5); 

            pstmt1 = conn.prepareStatement(cAssgItem);
            pstmt1.setString(1,rs1.getString(1));  
            rs2 = pstmt1.executeQuery();
            bFlag= true;
            nCount   = 1;
            while(rs2.next())
            {    
              TableRow row3 = new TableRow("Left",null,null);
              TableCol col3 = null;
              if(bFlag)  
              {
                vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_ITEM_ASSGN","Items Assigned");
                col3 = new TableCol(util.getBlankSpaces(10),null,null,null," WIDTH=\"25%\"");   
                col3.setFormat(new Bold()); 
                col3.add(util.createLabelItem(vBPLate,vLabelAttrib));
                bFlag=false;   
              }
              else
                col3 = new TableCol("&nbsp;",null,null,null,null);        
              row3.add(col3);
              TableCol col4 = new TableCol(Integer.toString(nCount++) + "&nbsp;",null, null, null,null);
              col4.add(util.createLabelItem(rs2.getString(2) + "/" + rs2.getString(3),vLabelAttrib)); 
              row3.add(col4);
              tab1.add(row3); 
            }
            if(bFlag)
            {
              TableRow row4 = new TableRow();
              vBPLate = cdata.GetConfigValue("SY_SECREP", nLangID, "BL_LABEL.B_SECREP_ITEM_ASSGN","Items Assigned");
              TableCol col4 = new TableCol(util.getBlankSpaces(15),null,null,null," WIDTH=\"25%\"");   
              col4.setFormat(new Bold()); 
              col4.add(util.createLabelItem(vBPLate,vLabelAttrib));
              row4.add(col4);
              row4.add(new TableCol(util.createLabelItem("None",vLabelAttrib),null,null,null,null));
              tab1.add(row4); 
            }  
            tab1.add(row5); 
          }
          body.add(tab1);
        }
      }catch(SQLException sexe){System.out.println(sexe);}
      finally
      {
        try
        {
          if(rs!=null)
            rs.close();
          if(rs1!=null)
            rs1.close();
          if(pstmt!=null)
            pstmt.close();
          if(pstmt1!=null)
            pstmt1.close();
          if(pstmt2!=null)
            pstmt2.close();
          if(conn!=null)
            conn.close();
        }catch(SQLException sexe){System.out.println(sexe);}
      }
      page.add(body);
      out.println(page);
   }
}

