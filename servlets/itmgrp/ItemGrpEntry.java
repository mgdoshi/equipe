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

public class ItemGrpEntry extends HttpServlet
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
      String rItmGrp[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;

      String pvMode = request.getParameter("pvMode");
      String pnItemGroupID = request.getParameter("pnItemGroupID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "ST_ITEMGROUP", nLangID, "WD_FORM_INSERT","ItemGroup / I");                      
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "ST_ITEMGROUP", nLangID, "WD_FORM_UPDATE","ItemGroup / U");                      

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_ItemGroup/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnItemGroupID!=null && !pnItemGroupID.equals("") && !pnItemGroupID.equalsIgnoreCase("null"))
      { 
        rItmGrp = db.getRecord( pnItemGroupID, "ItemGroup" );
      }  
      else
        rItmGrp = new String[5];  

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("ItemGrpEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add( new FormHidden("pnTransID", nTransID, null ) );
      form.add( new FormHidden("pnItemGroupID", pnItemGroupID, null ) );
      form.add( new FormHidden("vAction", null, null ) );
      form.add(new NL(5));

      Table tab = new Table("1","center","Border=\"0\" width=\"65%\" COLS=2");
 
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_ITEMGROUP", nLangID, "BL_LABEL.B_ITEMGROUP_ITEMGROUP_NAME","ItemGroup Name");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"NAME.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"30%\""); 
      TableCol col1 = new TableCol(util.createTextItem( nUserID, "ST_ITEMGROUP", "vItemGroup_Name", pvMode, "15", "30", rItmGrp[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib), null, null, null, null);
      col.add( WebUtil.NotNull);
      row.add(col);
      row.add(col1);

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_ITEMGROUP", nLangID, "BL_LABEL.B_ITEMGROUP_ITEMGROUP_DESC","Description");
      row1.add( new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null, null) );
      row1.add( new TableCol(util.createTextItem( nUserID, "ST_ITEMGROUP", "vItemGroup_Desc", pvMode, "30", "100", rItmGrp[2], null, vTextAttrib),null,null,null,null) );

      tab.add(row);  
      tab.add(row1);  
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

      int nMsgID =-1;
      String nLangID=null;
      String nUserID=null;
      String nTransID=null;
      String errMsg=null;
      String vStatus=null;
      String mode=""; 
      String query = null;

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();
  
      nUserID = Parse.GetValueFromString( vPID, "UserID" );
      nLangID = Parse.GetValueFromString( vPID, "LangID" );

      String pnTransID = request.getParameter("pnTransID");
      String pnItemGroupID = request.getParameter("pnItemGroupID");
      String vName   = Parse.formatStr( request.getParameter("vItemGroup_Name") );
      String vDesc   = Parse.formatStr( request.getParameter("vItemGroup_Desc") );
      String vAction = request.getParameter("vAction"); 

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
            int itmcls_id = Integer.parseInt(db.getNextVal("S_ItemGroup"));
            query = "INSERT INTO T_ItemGroup (  ItemGroup_ID, ItemGroup_Name, ItemGroup_Desc, Modifier, Change_Dt  ) " +
                    " VALUES ("+itmcls_id+", "+val.IsNull(vName)+","+val.IsNull(vDesc)+",'"+usr+"', '"+dt+"')";
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
            query = "UPDATE T_ItemGroup SET ItemGroup_Name = "+val.IsNull(vName)+"," +
                    " ItemGroup_Desc = "+val.IsNull(vDesc)+"," + 
                    " Modifier    = '"+usr+"'," + 
                    " Change_Dt   = '"+dt+"'" + 
                    " WHERE ItemGroup_ID = "+pnItemGroupID;
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
          mode=null;
        response.sendRedirect("/JOrder/servlets/ItemGrpFrame?pvMode="+mode+"&pnItemGroupID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
      }
      else
         out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

