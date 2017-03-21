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

public class ParamEntry extends HttpServlet
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
      String pnParamID = request.getParameter("pnParamID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();
 
      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      String vParClassQry = " SELECT Attrib, Attrib_Desc FROM T_Domain WHERE Domain = 'PARCLASS' AND Fk_Lang_ID = " + nLangID +
                            " ORDER BY Sequence_Nr";
      String vParTypeQry =  " SELECT Attrib, Attrib_Desc FROM T_Domain WHERE Domain = 'PARTYPE'  AND Fk_Lang_ID = " + nLangID +
                            " ORDER BY Sequence_Nr";

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "SY_PARAM", nLangID, "WD_FORM_INSERT","Parameter / I");                      
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "SY_PARAM", nLangID, "WD_FORM_UPDATE","Parameter / U");                      

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Param/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnParamID!=null && !pnParamID.equals("") && !pnParamID.equalsIgnoreCase("null"))
      { 
        rParam = db.getRecord( pnParamID, "Param" );
      }  
      else
        rParam = new String[5];  

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ParamEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add( new FormHidden("pnTransID", nTransID, null ) );
      form.add( new FormHidden("pnParamID", pnParamID, null ) );
      form.add( new FormHidden("vAction", null, null ) );
      form.add( new NL(5) );

      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_PARAM", nLangID, "BL_LABEL.B_PARAM_DM_PARCLASS","Parameter Class");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"GROUP.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"30%\"");
      col.add(WebUtil.NotNull);
      TableCol col1 = null;   
      if( Integer.parseInt(nUserID) == 0 )
      {
        HtmlTag sel =  util.createList( nUserID, "SY_PARAM", "cDMGroup", pvMode, vParClassQry, rParam[3], null, vListAttrib);
        col1 = new TableCol( sel, null, null, "2", null);
      }
      else
      { 
         if ( pvMode!=null && pvMode.equalsIgnoreCase("I"))
         {
            col1 = new TableCol( uti.createLabelItem( Domain.getDomainDescFrmAttrib("PARCLASS", "C", nLangID ), vLabelAttrib ), null, null, "2", null);
            FormHidden grp = new FormHidden("cDMGroup", "C", null );
            form.add( grp );
         }
         else if( pvMode!=null && pvMode.equalsIgnoreCase("U")) 
         {
            col1 = new TableCol( uti.createLabelItem( Domain.getDomainDescFrmAttrib("PARCLASS", rParam[3], nLangID ), vLabelAttrib ), null, null, "2", null); 
            FormHidden grp = new FormHidden("cDMGroup", rParam[3], null );
            form.add( grp );
         }
      } 
      row.add(col) ;
      row.add(col1) ;

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_PARAM", nLangID, "BL_LABEL.B_PARAM_DM_PARTYPE","Parameter Type");
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TYPE.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      col2.add(WebUtil.NotNull);
      TableCol col3 = new TableCol(util.createList( nUserID, "SY_PARAM", "cDMType", pvMode, vParTypeQry, rParam[2], null, vListAttrib),null,null,null,"WIDTH=\"25%\"");
      row1.add(col2) ;
      row1.add(col3) ;

      TableRow row2 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_PARAM", nLangID, "BL_LABEL.B_PARAM_PARAM_NAME","Parameter Name");
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"NAME.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      col4.add(WebUtil.NotNull);
      TableCol col5 = new TableCol(util.createTextItem( nUserID, "SY_PARAM", "vName", pvMode, "15", "30", rParam[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,"WIDTH=\"25%\"");
      row2.add(col4) ;
      row2.add(col5) ;

      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_PARAM", nLangID, "BL_LABEL.B_PARAM_PARAM_VALUE","Parameter Value");
      TableCol col6 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"VALUE.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      col6.add(WebUtil.NotNull);
      TableCol col7 = new TableCol(util.createTextItem( nUserID, "SY_PARAM", "vValue", pvMode, "15", "30", rParam[4], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,"WIDTH=\"25%\"");
      row3.add(col6) ;
      row3.add(col7) ;

      tab.add(row);  
      tab.add(row1);  
      tab.add(row2);  
      tab.add(row3);  

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

      String nLangID=null;
      String nUserID=null;
      String errMsg=null;  
      String nTransID=null;
      String vStatus=null;
      String mode=""; 
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
      String pnParamID = request.getParameter("pnParamID");
      String cDMType   = request.getParameter("cDMType");
      String cDMGroup  = request.getParameter("cDMGroup");
      String vName     = Parse.formatStr(request.getParameter("vName"));
      String vValue    = Parse.formatStr(request.getParameter("vValue"));
      String vAction   = request.getParameter("vAction"); 

      String usr = db.getName( nUserID, "User" );      
      vStatus    = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction.equalsIgnoreCase("Insert") || vAction.equalsIgnoreCase("SaveInsert") )
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            int param_id = Integer.parseInt(db.getNextVal("S_Param"));
            query = "INSERT INTO T_Param ( Param_ID, Param_Name, DM_ParType, DM_ParClass, Param_Value , Modifier, Change_Dt ) " +
                    " VALUES ("+param_id+", '"+vName+"', '"+cDMType+"','"+cDMGroup+"','"+vValue+"','"+usr+"', '"+dt+"')";
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
        else if( vAction.equalsIgnoreCase("Update"))
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();       
            query = "UPDATE T_Param SET Param_Name = '"+vName+"'," +
                    " Param_Value = '"+vValue+"'," + 
                    " DM_ParType  = '"+cDMType+"'," + 
                    " DM_ParClass = '"+cDMGroup+"'," + 
                    " Modifier    = '"+usr+"'," + 
                    " Change_Dt   = '"+dt+"'" + 
                    " WHERE Param_ID = "+pnParamID;
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
        if(vAction.equalsIgnoreCase("SaveInsert"))
          mode="I";
        else
          mode="N";
        response.sendRedirect("/JOrder/servlets/ParamFrame?pvMode="+mode+"&pnParamID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
      }
      else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

