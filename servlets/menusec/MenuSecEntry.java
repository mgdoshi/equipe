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
import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MenuSecEntry extends HttpServlet
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
 
      int nCount=0;
      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;
      String nGroupID=null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vMembers = "";
      String query = null;
      String vName = "";
      String vGName = "";
      String menuID = null;
      String menuName = null;
      String cDMSecLevel  = null;

      String nAllowFlg  = null;
      String vStatus  = "CHECKED";

      String pvMode = request.getParameter("pvMode");
      String pvSecLevel = request.getParameter("pvSecLevel");
      String pnRefID = request.getParameter("pnRefID");
     
      DBConnect db = new DBConnect();
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
      
      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );
      vFormType = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "WD_FORM_INSERT", "MenuSec / U OR I " );
   
      nTransID = Trans.getTransID( nAuditID, 'M');
    
      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_MenuSec/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/MenuSecEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      body.add( new NL(2) );

      query = " SELECT COUNT(*)"+
              " FROM   T_MenuSec"+
              " WHERE  DM_SecLevel = '"+pvSecLevel+"'"+
              " AND    ( ( "+pnRefID+" IS NULL AND Ref_ID IS NULL ) OR Ref_ID = "+pnRefID +")";
      try
      {
         conn = db.GetDBConnection();
         stmt = conn.createStatement();
         rs = stmt.executeQuery(query);
         while(rs.next())
         {
            nCount = rs.getInt(1);
         }
      }
      catch(Exception e) { 
        System.out.println("Exception Occures	"+e);
	System.out.println("Exception Occures	"+e.getMessage()); }
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
      form.add( new FormHidden("pnTransID", nTransID, null ) );
      form.add( new FormHidden("cDM_SecLevel", pvSecLevel, null ) );
      form.add( new FormHidden("nRef_ID", pnRefID, null ) );
      form.add( new FormHidden("nAllow_Flg", null, null ) );
      if( pvSecLevel!=null && pvSecLevel.equalsIgnoreCase("G") )
      {
         vName = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_LABEL.B_MENUSEC_GROUP_NAME", "Group Name" );
         vGName = db.getName( Integer.parseInt( pnRefID ), "Group" );
         query = " SELECT usr.User_Name" +
                 " FROM   T_User usr, T_UserGroup usg "+
                 " WHERE  usr.User_ID = usg.Fk_User_ID "+
                 " AND    usg.Fk_Group_ID = "+ pnRefID +
                 " ORDER BY usr.User_Name";
      }
      else if( pvSecLevel!=null && pvSecLevel.equalsIgnoreCase("U") )
      {
         vName = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_LABEL.B_MENUSEC_USER_NAME", "User Name" );
         vGName = db.getName( Integer.parseInt( pnRefID ), "User" );
         query = " SELECT grp.Group_Name"+
                 " FROM   T_Group grp, T_UserGroup usg "+
                 " WHERE  grp.Group_ID = usg.Fk_Group_ID "+
                 " AND    usg.Fk_User_ID = "+ pnRefID +
                 " ORDER BY grp.Group_Name";
      } 
      try
      {
         conn = db.GetDBConnection();
         stmt = conn.createStatement();
         rs = stmt.executeQuery(query);
         while(rs.next())
         {
            vMembers += rs.getString(1)+ ", "; 
         }
      }
      catch(Exception e) { 
        System.out.println("Exception Occures	"+e);
	System.out.println("Exception Occures	"+e.getMessage()); }
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

      vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_LABEL.B_MENUSEC_DM_SECLEVEL", "Security Level" );
      Para p = new Para();
      Bold b = new Bold();
      Bold b1 = new Bold();
      Bold b2 = new Bold();
      b.add( vBPLate );
      b1.add( "Members" );  
      b2.add( vName );
      Center cen = new Center();
      p.add( b + WebUtil.getBlankSpaces(5)+ Domain.getDomainDescFrmAttrib( "SECLEVEL", pvSecLevel, nLangID) + WebUtil.getBlankSpaces(8)+  b2 + vGName + WebUtil.getBlankSpaces(8)+ b1 + WebUtil.getBlankSpaces(5) + vMembers );

      Table tab1 = new Table("1","center","Border=\"0\" width=\"70%\" BGCOLOR=\"#FFFFCA\"");

      TableRow row2 = new TableRow("Left",null,"BGCOLOR=\"#666666\"");
      vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_LABEL.B_MENUSEC_MENU_NAME", "Menu Name" );
      TableHeader head1 = new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"Type.gif", vBPLate, "COLOR=\"White\""+vLabelAttrib ),null, null, null, null);
      vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_LABEL.B_MENUSEC_ALLOW_FLAG", "Allow Flag" );
      TableHeader head2 = new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"Name.gif", vBPLate, "COLOR=\"White\""+vLabelAttrib ),null, null, null, null);
      row2.add(head1);
      row2.add(head2);
      try
      {
         Font f = new Font("White", "Arial", "3", null);
         conn = db.GetDBConnection();
         stmt = conn.createStatement();         
         rs = stmt.executeQuery(query);
         while(rs.next())
         {
           TableHeader head8 = new TableHeader( util.createLabelItem( rs.getString(1), vLabelAttrib ),null, null, null, null);
           head8.setFormat(f); 
           row2.add(head8);
         }
      }
      catch(Exception e) { 
        System.out.println("Exception Occures	"+e);
	System.out.println("Exception Occures	"+e.getMessage());  }
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
      tab1.add(row2);

      try
      {
         conn = db.GetDBConnection();
         stmt = conn.createStatement();
         query = " Select Menu_Name, Parent_ID, Menu_ID "+
                 " From   T_MenuSecRef "+
                 " Where  SetAble = 1 "+
                 " Order By Menu_Name ";

         rs = stmt.executeQuery(query);
         while(rs.next())
         {
           if( rs.getString(2)==null || rs.getString(2).equals("") || rs.getString(2).equalsIgnoreCase("null") )
           { 
             menuID = rs.getString(3);
             menuName = rs.getString(1);
             TableRow row3 = new TableRow("Left",null,null);
             TableCol col1 = new TableCol( "*"+ WebUtil.getBlankSpaces(1)+util.createLabelItem( menuName, vLabelAttrib ) + new FormHidden( "vMenu_Name", menuName, null),null, null, null, null);
             row3.add( col1 );
             query =  " Select Allow_Flg " +
                      " From   T_MenuSec "+
                      " Where  ("+pnRefID+" IS NULL OR Ref_ID = "+pnRefID+")"+
                      " AND    Menu_Name = '"+ menuName +"'"+
                      " AND    DM_SECLEVEL = '"+ pvSecLevel +"'";
             ResultSet rs1 = stmt.executeQuery(query);
             while( rs1.next() )
             {
               int i = rs1.getInt(1);
               if( i == 0 )
                 vStatus = null;
               else if( i == 1 )
                 vStatus = "CHECKED";
               nAllowFlg = Integer.toString(i);
             }
             if( nAllowFlg==null || nAllowFlg.equals("") || nAllowFlg.equalsIgnoreCase("null") )
             {
               TableCol c1 = new TableCol( new FormCheckBox("nAllow_Flg",null,vStatus,null),"Center", null, null, null);
               row3.add( c1 );
             }
             else
             {
               TableCol c1 = new TableCol( new FormCheckBox("nAllow_Flg",nAllowFlg,vStatus,null),"Center", null, null, null);
               row3.add( c1 );
             }
             if( pvSecLevel!=null && pvSecLevel.equalsIgnoreCase("U") )
             {
                query = " SELECT grp.Group_Name"+
                        " FROM   T_Group grp, T_UserGroup usg "+
                        " WHERE  grp.Group_ID = usg.Fk_Group_ID "+
                        " AND    usg.Fk_User_ID = "+ pnRefID +
                        " ORDER BY grp.Group_Name";             
                ResultSet rs2 = stmt.executeQuery(query);
                while(rs2.next())
                {
                  nGroupID = db.getID( rs2.getString(1),"Group" );
                  query = " Select Allow_Flg "+
                          " From   T_MenuSec "+
                          " Where  Ref_ID =  "+ nGroupID +
                          " AND    Menu_Name = '"+menuName+"'"+
                          " AND    DM_SECLEVEL = 'G'";
                  ResultSet rs3 = stmt.executeQuery(query);
                  while(rs3.next())
                  {
                    TableCol c1 = new TableCol( util.createLabelItem( rs3.getString(1), vLabelAttrib ),"Center", null, null, null);
                    row3.add( c1 );
                  }
                }
             }  
             tab1.add(row3);
             query = "  Select Menu_Name, Menu_ID "+
                     "  From t_MenuSecRef "+
                     "  Where Parent_ID = "+menuID+ 
                     " AND  SetAble = 1";
             ResultSet rs4 = stmt.executeQuery(query);
             while( rs4.next() )
             {
                menuID = rs4.getString(2);
                menuName = rs4.getString(1);
                TableRow row4 = new TableRow("Left",null,null);
                TableCol col2 = new TableCol( WebUtil.getBlankSpaces(7)+ util.createLabelItem( menuName, vLabelAttrib ) + new FormHidden( "vMenu_Name", menuName, null),null, null, null, null);
                row4.add(col2);
                query =  " Select Allow_Flg " +
                         " From   T_MenuSec "+
                         " Where  ("+pnRefID+" IS NULL OR Ref_ID = "+pnRefID+")"+
                         " AND    Menu_Name = '"+ menuName +"'"+
                         " AND    DM_SECLEVEL = '"+ pvSecLevel +"'";
                ResultSet rs5 = stmt.executeQuery(query);
                while( rs5.next() )
                {
                  int i = rs5.getInt(1);
                  if( i == 0 )
                    vStatus = null;
                  else if( i == 1 )
                    vStatus = "CHECKED";
                  nAllowFlg = Integer.toString(i);
                }
                if( nAllowFlg==null || nAllowFlg.equals("") || nAllowFlg.equalsIgnoreCase("null") )
                {
                  TableCol c1 = new TableCol( new FormCheckBox("nAllow_Flg",null,vStatus,null),"Center", null, null, null);
                  row4.add( c1 );
                }
                else
                {
                  TableCol c1 = new TableCol( new FormCheckBox("nAllow_Flg",nAllowFlg,vStatus,null),"Center", null, null, null);
                  row4.add( c1 );
                }
                if( pvSecLevel!=null && pvSecLevel.equalsIgnoreCase("U") )
                {
                  query = " SELECT grp.Group_Name"+
                          " FROM   T_Group grp, T_UserGroup usg "+
                          " WHERE  grp.Group_ID = usg.Fk_Group_ID "+
                          " AND    usg.Fk_User_ID = "+ pnRefID +
                          " ORDER BY grp.Group_Name";
                  ResultSet rs6 = stmt.executeQuery(query);
                  while(rs6.next())
                  {
                    nGroupID = db.getID( rs6.getString(1),"Group" );
                    query = " Select Allow_Flg "+
                            " From   T_MenuSec "+
                            " Where  Ref_ID =  "+ nGroupID +
                            " AND    Menu_Name = '"+menuName+"'"+
                            " AND    DM_SECLEVEL = 'G'";
                    ResultSet rs7 = stmt.executeQuery(query);
                    while(rs7.next())
                    {
                      TableCol c1 = new TableCol( util.createLabelItem( rs7.getString(1), vLabelAttrib ),"Center", null, null, null);
                      row4.add( c1 );
                    }
                  }
               }
               tab1.add(row4);
               query = "  Select Menu_Name, Menu_ID "+
                       "  From t_MenuSecRef "+
                       "  Where Parent_ID = "+menuID+ 
                       " AND  SetAble = 1";
               ResultSet rs8 = stmt.executeQuery(query);
               while( rs8.next() )
               {
                  menuID = rs8.getString(2);
                  menuName = rs8.getString(1);
                  TableRow row5 = new TableRow("Left",null,null);
                  TableCol col3 = new TableCol( WebUtil.getBlankSpaces(14)+util.createLabelItem( menuName, vLabelAttrib ) + new FormHidden( "vMenu_Name", menuName, null),null, null, null, null);
                  row5.add(col3);
                  query =  " Select Allow_Flg " +
                           " From   T_MenuSec "+
                           " Where  ("+pnRefID+" IS NULL OR Ref_ID = "+pnRefID+")"+
                           " AND    Menu_Name = '"+ menuName +"'"+
                           " AND    DM_SECLEVEL = '"+ pvSecLevel +"'";
                  ResultSet rs9 = stmt.executeQuery(query);
                  while( rs9.next() )
                  {
                    int i = rs9.getInt(1);
                    if( i == 0 )
                      vStatus = null;
                    else if( i == 1 )
                      vStatus = "CHECKED";
                    nAllowFlg = Integer.toString(i);
                  }
                  if( nAllowFlg==null || nAllowFlg.equals("") || nAllowFlg.equalsIgnoreCase("null") )
                  {
                    TableCol c1 = new TableCol( new FormCheckBox("nAllow_Flg",null,vStatus,null),"Center", null, null, null);
                    row5.add( c1 );
                  }
                  else
                  {
                    TableCol c1 = new TableCol( new FormCheckBox("nAllow_Flg",nAllowFlg,vStatus,null),"Center", null, null, null);
                    row5.add( c1 );
                  }
                  if( pvSecLevel!=null && pvSecLevel.equalsIgnoreCase("U") )
                  {
                    query = " SELECT grp.Group_Name"+
                            " FROM   T_Group grp, T_UserGroup usg "+
                            " WHERE  grp.Group_ID = usg.Fk_Group_ID "+
                            " AND    usg.Fk_User_ID = "+ pnRefID +
                            " ORDER BY grp.Group_Name";             
                    ResultSet rs10 = stmt.executeQuery(query);
                    while(rs10.next())
                    {
                      nGroupID = db.getID( rs10.getString(1),"Group" );
                      query = " Select Allow_Flg "+
                              " From   T_MenuSec "+
                              " Where  Ref_ID =  "+ nGroupID +
                              " AND    Menu_Name = '"+menuName+"'"+
                              " AND    DM_SECLEVEL = 'G'";
                      ResultSet rs11 = stmt.executeQuery(query);
                      while(rs11.next())
                      {
                        TableCol c1 = new TableCol( util.createLabelItem( rs11.getString(1), vLabelAttrib ),"Center", null, null, null);
                        row5.add( c1 );
                      }
                    }
                  }
                  tab1.add(row5);
                  query = "  Select Menu_Name, Menu_ID "+
                          "  From t_MenuSecRef "+
                          "  Where Parent_ID = "+menuID+ 
                          " AND  SetAble = 1";
                  ResultSet rs12 = stmt.executeQuery(query);
                  while( rs12.next() )
                  {
                    menuID = rs12.getString(2);
                    menuName = rs12.getString(1);
                    TableRow row6 = new TableRow("Left",null,null);
                    TableCol col4 = new TableCol( WebUtil.getBlankSpaces(21)+ util.createLabelItem( menuName, vLabelAttrib ) + new FormHidden( "vMenu_Name", menuName, null),null, null, null, null);
                    row6.add(col4);
                    query =  " Select Allow_Flg " +
                             " From   T_MenuSec "+
                             " Where  ("+pnRefID+" IS NULL OR Ref_ID = "+pnRefID+")"+
                             " AND    Menu_Name = '"+ menuName +"'"+
                             " AND    DM_SECLEVEL = '"+ pvSecLevel +"'";
                    ResultSet rs13 = stmt.executeQuery(query);
                    while( rs13.next() )
                    {
                      int i = rs13.getInt(1);
                      if( i == 0 )
                        vStatus = null;
                      else if( i == 1 )
                        vStatus = "CHECKED";
                      nAllowFlg = Integer.toString(i);
                    }
                    if( nAllowFlg==null || nAllowFlg.equals("") || nAllowFlg.equalsIgnoreCase("null") )
                    {
                      TableCol c1 = new TableCol( new FormCheckBox("nAllow_Flg",null,vStatus,null),"Center", null, null, null);
                      row6.add( c1 );
                    }
                    else
                    {
                      TableCol c1 = new TableCol( new FormCheckBox("nAllow_Flg",nAllowFlg,vStatus,null),"Center", null, null, null);
                      row6.add( c1 );
                    }
                    if( pvSecLevel!=null && pvSecLevel.equalsIgnoreCase("U") )
                    {
                      query = " SELECT grp.Group_Name"+
                              " FROM   T_Group grp, T_UserGroup usg "+
                              " WHERE  grp.Group_ID = usg.Fk_Group_ID "+
                              " AND    usg.Fk_User_ID = "+ pnRefID +
                              " ORDER BY grp.Group_Name";             
                      ResultSet rs14 = stmt.executeQuery(query);
                      while(rs14.next())
                      {
                        nGroupID = db.getID( rs14.getString(1),"Group" );
                        query = " Select Allow_Flg "+
                                " From   T_MenuSec "+
                                " Where  Ref_ID =  "+ nGroupID +
                                " AND    Menu_Name = '"+menuName+"'"+
                                " AND    DM_SECLEVEL = 'G'";
                        ResultSet rs15 = stmt.executeQuery(query);
                        while(rs15.next())
                        {
                          TableCol c1 = new TableCol( util.createLabelItem( rs15.getString(1), vLabelAttrib ), "Center", null, null, null);
                          row6.add( c1 );
                        }
                      }
                    }
                   tab1.add(row6);
                 }
               }       
             }
           }
         }
      }
      catch(Exception e) { 
        System.out.println("Exception Occures	"+e);
	System.out.println("Exception Occures	"+e.getMessage());  }
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

      form.add( new FormHidden("pnCount", Integer.toString(nCount), null ) );
      form.add( new FormHidden("vAction", null, null ) );
      form.add( tab1 );
      cen.add( p ); 
      body.add( cen );
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

      int nMsgID =-1;
      String errMsg=null;
      String vStatus=null;
      String query = null;
      String nLangID=null;
      String nUserID=null;
      nUserID = Parse.GetValueFromString( vPID, "UserID" );
      nLangID = Parse.GetValueFromString( vPID, "LangID" );

      String pnTransID = request.getParameter("pnTransID");
      String vMenuName[] = request.getParameterValues("vMenu_Name");
      String cDMSecLevel = request.getParameter("cDM_SecLevel");
      String nRefID = request.getParameter("nRef_ID");
      String nAllowFlg[] = request.getParameterValues("nAllow_Flg");
      int nCount = Integer.parseInt( request.getParameter("pnCount") );
      String vAction = request.getParameter("vAction"); 

      DBConnect db = new DBConnect();
      PreparedStatement pstmt = null;
      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      Message msg = new Message(); 
  
      String usr = db.getName( nUserID, "User" );      
      vStatus = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction.equalsIgnoreCase("Insert")  )
        {
          try
          {
            conn = db.GetDBConnection();
            query = " INSERT INTO T_MenuSec( MenuSec_ID, Menu_Name, DM_SECLEVEL, Ref_ID, Allow_Flg ) "+
                    " VALUES( ?, ?, '"+cDMSecLevel+"', "+nRefID+", ? )";
            pstmt = conn.prepareStatement(query);
            for( int i = 0; i< nCount-1; i++ )
            {
               int mensec_id = Integer.parseInt(db.getNextVal("S_MenuSec"));               
               int allow = Integer.parseInt(Parse.GetSubString( nAllowFlg[0], "~", i ).trim());
               pstmt.setInt( 1, mensec_id);
               pstmt.setString( 2, vMenuName[i]);
               pstmt.setInt( 3, allow );
               pstmt.executeUpdate(); 
             }
             nMsgID = 3;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
          finally
          {
            try
            {
             if(pstmt!=null)
               pstmt.close();
             if(conn!=null)  
               conn.close();
            }catch(SQLException sexe){}
          }
          Trans.setTransID(pnTransID);
        }
        else if( vAction.equalsIgnoreCase("Update"))
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            /*out.println("IN UPDATE");
            out.println("pnTransID" + pnTransID);
            out.println("vstatus" + vStatus);
            out.println("vaction" + vAction);
            out.println("nLangID" + nLangID);
            out.println("UserID" + nUserID);
            out.println("nCount" + nCount);*/
            for( int i = 0; i< nCount-1; i++ )
            {
              //out.println("allow" + Integer.parseInt(Parse.GetSubString( nAllowFlg[0], "~", i ).trim())+"<BR>");
              //out.println("vMenuName + vMenuName[i]+"<BR>");
              int allow = Integer.parseInt(Parse.GetSubString( nAllowFlg[0], "~", i ).trim());
              query = " UPDATE T_MenuSec " +
                      " SET    Allow_Flg = "+allow+
                      " WHERE  Menu_Name = '"+vMenuName[i]+"'"+
                      " AND    DM_SecLevel = '"+cDMSecLevel+"'"+
                      " AND    Ref_ID = " +nRefID;
              stmt.executeUpdate(query);
            }
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
       response.sendRedirect("/JOrder/servlets/MenuSecFrame?pvMode=N&pvSecLevel="+cDMSecLevel+"&pnRefID="+nRefID+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}
