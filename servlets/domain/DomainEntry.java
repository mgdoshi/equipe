import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DomainEntry extends HttpServlet
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

      int count=5;
      
      String vLangQry=null;
      String vDomainName=null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String query=null;
      String nAuditID=null;
      String nLangID=null;
      String nUserID=null;
      String nSchemeID=null;
      String nTransID=null;

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      String pvDomain = request.getParameter("pvDomain");

      DBConnect db = new DBConnect();
      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      vLangQry = "Select Lang_ID, Lang_Desc From T_Lang";
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "WD_FORM_UPDATE","Domain / U");
      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Domain/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();       
        query = "Select Domain_Name From T_Domain " +
                " WHERE Domain = '"+pvDomain+"'";
        rs = stmt.executeQuery(query);
        if( rs.next() )
        {
            vDomainName = rs.getString(1);           
        }  
      }catch(SQLException sexe){System.out.println(sexe);}
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
      
      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/DomainEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden("vAction",null, null));
      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add(new FormHidden("pvDomain", pvDomain, null ));
      form.add(new FormHidden("pvDomainName", vDomainName, null ));
      form.add(new NL(1));
      Table tab1 = new Table("1","center","Border=\"0\" width=\"60%\" COLS=6");
      TableRow row = new TableRow(null,null,null);
      vBPLate = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_DOMAIN", "Domain" );
      TableHeader head1 = new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"DOMAIN.gif", vBPLate, vLabelAttrib ),null, null, null,null); 
      TableCol col = new TableCol(util.createLabelItem(pvDomain,vLabelAttrib), null, null, null,null); 
      vBPLate = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_DOMAIN_NAME", "Description" );
      TableHeader head2 = new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null); 
      TableCol col1 = new TableCol(util.createLabelItem(vDomainName,vLabelAttrib),null, null, null,null); 
      row.add(head1);
      row.add(col);
      row.add(head2);
      row.add(col1);
      tab1.add(row);
      form.add(tab1);

      form.add( new NL(1) );
      Table tab = new Table("1","center","Border=\"0\" width=\"85%\" COLS=4");
      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_SEQNO","Seq No");
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"ParObj.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"12%\""); 
      col2.add(WebUtil.NotNull);  
      vBPLate = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_ATTRIBUTE","Attribute");
      TableCol col3 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"ParObj.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"14%\""); 
      col3.add(WebUtil.NotNull);  
      vBPLate = cdata.GetConfigValue("ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_ATTRIB_DESC","Attrib Desc");
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"ParObj.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"37%\""); 
      col4.add(WebUtil.NotNull);  
      vBPLate = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_LANGUAGE","Language");
      TableCol col5 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"ParObj.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"17%\""); 
      col5.add(WebUtil.NotNull);  
      row1.add(col2) ;
      row1.add(col3);
      row1.add(col4) ;
      row1.add(col5);
      tab.add(row1);
      
      try
      {
        conn = db.GetDBConnection();
        stmt = conn.createStatement();
        rs = stmt.executeQuery(" Select * FROM T_Domain" +
                               " Where Domain = '"+pvDomain+"' order by sequence_nr");
        ResultSetMetaData rmdata = rs.getMetaData();
        int ccount = rmdata.getColumnCount(); 
        while(rs.next())
        {
          TableRow row2 = new TableRow("Left",null,null);	
          row2.add(new TableCol(util.createTextItem( nUserID, "ST_DOMAIN", "nSequence_Nr", "U", "4", "5",rs.getString(3), "onBlur=\"top.check_num(this)\"", vTextAttrib),null, null, null,null));   
          row2.add(new TableCol(util.createTextItem( nUserID, "ST_DOMAIN", "vAttrib", "U", "5", "1",rs.getString(4), "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null));   
          row2.add(new TableCol(util.createTextItem( nUserID, "ST_DOMAIN", "vAttrib_Desc", "U", "30", "100",rs.getString(5), null, vTextAttrib),null, null, null,"WIDTH=\"5%\""));   
          row2.add(new TableCol(util.createList( nUserID, "ST_DOMAIN", "nFk_Lang_ID", "I", vLangQry, rs.getString(6), null, vListAttrib),null, null, null,null));
          tab.add(row2);
        }  
      }catch(Exception sexe){System.out.println(sexe);}  
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
 
      HtmlTag list = util.createList( nUserID, "ST_DOMAIN", "nFk_Lang_ID", "I", vLangQry, null, null, vListAttrib);  
      for(int i=0;i<5;i++)
      {
          TableRow row2 = new TableRow("Left",null,null);	
          row2.add(new TableCol(util.createTextItem( nUserID, "ST_DOMAIN", "nSequence_Nr", "U", "4", "5",null, "onBlur=\"top.check_num(this)\"", vTextAttrib),null, null, null,null));   
          row2.add(new TableCol(util.createTextItem( nUserID, "ST_DOMAIN", "vAttrib", "U", "5", "1",null, "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null));   
          row2.add(new TableCol(util.createTextItem( nUserID, "ST_DOMAIN", "vAttrib_Desc", "U", "30", "100",null, null, vTextAttrib),null, null, null,"WIDTH=\"5%\""));   
          row2.add(new TableCol(list,null, null, null,null));
          tab.add(row2);   
      } 
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
      int nMsgID=-1; 

      String errMsg=null; 
      String query = null;
      String vDomain=null;
      String vDomainName=null;
      String vStatus=null;
      String nLangID=null;
      String nUserID=null;
      
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      
      Statement stmt = null;
      PreparedStatement pstmt=null;
      Connection conn = null;
      ResultSet rs = null;

      String pnTransID = request.getParameter("pnTransID");
      vDomain = request.getParameter("pvDomain");
      vDomainName = Parse.formatStr(request.getParameter("pvDomainName"));
      String nSeqNr[] = request.getParameterValues("nSequence_Nr");
      String vAttrib[] = request.getParameterValues("vAttrib");
      String vAttribDesc[] = request.getParameterValues("vAttrib_Desc");
      String vLangID[] = request.getParameterValues("nFk_Lang_ID");
      String vAction = request.getParameter("vAction");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      Message msg = new Message(); 
      IngDate dt = new IngDate();
  
     String usr = db.getName( nUserID, "User" );
     vStatus = Trans.checkTransValidity( pnTransID );
     if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
     {
        if( vAction.equalsIgnoreCase("Update"))
        {
          try
          {
              conn = db.GetDBConnection();
              stmt = conn.createStatement();       
              query = "Delete From T_Domain " +
                      " WHERE Domain = '"+vDomain+"'";
              stmt.executeUpdate(query);

              query = " INSERT INTO T_Domain( Domain, Domain_Name, Sequence_Nr, Attrib, "+
                      "                       Attrib_Desc, Fk_Lang_Id, Modifier, Change_Dt ) "+
                      " VALUES( '"+vDomain+"','"+vDomainName+"', ?, ?, ?, ?,'"+usr+"', '"+dt+"')";
              pstmt = conn.prepareStatement(query); 
              for( int i=0; i<nSeqNr.length ; i++ )
              {
                if( !( vAttrib[i]==null || vAttrib[i].equals("") || vAttrib[i].equalsIgnoreCase("null") ) )
                {
                   pstmt.setInt(1,Integer.parseInt(nSeqNr[i]));
                   pstmt.setString(2, vAttrib[i] );
                   pstmt.setString(3, Parse.formatStr(vAttribDesc[i]));
                   pstmt.setInt(4,Integer.parseInt(vLangID[i]));
                   pstmt.executeUpdate();
                }
             }
             nMsgID = 3;
          }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
          finally
          {
            try
            {
              if(pstmt!=null)
                pstmt.close();
              if(stmt!=null)
                stmt.close();
              if(conn!=null)
                conn.close(); 
            }catch(SQLException sexe){}
          }
          Trans.setTransID(pnTransID);
       }
     }
     if( nMsgID <=5 )
        response.sendRedirect("/JOrder/servlets/DomainFrame?pvMode=N&pvDomain="+vDomain+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
  }
}