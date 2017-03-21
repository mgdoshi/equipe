import ingen.html.*;
import ingen.html.para.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ConfigEntry extends HttpServlet
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
      String rConfig[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vLangQry=null;
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

      String pvMode = request.getParameter("pvMode");
      String pnConfigID = request.getParameter("pnConfigID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "SY_CONFIG", nLangID, "WD_FORM_INSERT", "Config / I");
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "SY_CONFIG", nLangID, "WD_FORM_UPDATE", "Config / U");

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Config/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnConfigID!=null && !pnConfigID.equals("") && !pnConfigID.equalsIgnoreCase("null"))
      { 
        rConfig = db.getRecord(pnConfigID,"config");
      }  
      else
        rConfig = new String[6]; 
 
      vLangQry = "Select Lang_ID, Lang_Desc From T_Lang";

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/ConfigEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add(new FormHidden("pnConfigID", pnConfigID, null ));
      form.add(new NL(5));

      Table tab = new Table("1","center","Border=\"0\" width=\"80%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_PARENT_OBJ", "Parent Object");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"parobj.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"");
      col.add(WebUtil.NotNull);
      row.add(col) ;
      row.add(new TableCol(util.createTextItem( nUserID, "SY_CONFG", "vParent_Obj", pvMode, "20", "100", rConfig[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,"WIDTH=\"25%\""));
      TableRow row1 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_FK_LANG_ID", "Language" );
      HtmlTag sel =  util.createList( nUserID, "SY_CONFIG", "nFk_Lang_ID", pvMode, vLangQry, rConfig[2], null, vListAttrib);
      TableCol col1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"lang.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col1.add(WebUtil.NotNull);
      row1.add(col1) ;
      row1.add(new TableCol( sel, null, null, null,null));
      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_OBJ_NAME", "Object Name" );
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"objname.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col2.add(WebUtil.NotNull);
      row2.add(col2) ;
      row2.add(new TableCol(util.createTextItem( nUserID, "SY_CONFIG", "vObj_Name", pvMode, "20", "100",rConfig[3], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null));
      TableRow row3 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_OBJ_PROPERTY", "Object Property" );
      TableCol col3 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"objprop.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col3.add(WebUtil.NotNull);
      row3.add(col3) ;
      row3.add(new TableCol(util.createTextItem( nUserID, "SY_CONFIG", "vObj_Property", pvMode, "20", "100",rConfig[4], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null, null, null,null));
      TableRow row4 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_CONFIG", nLangID, "BL_LABEL.B_CONFIG_PROPERTY_VALUE", "Property Value" );
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"propval.gif", vBPLate, vLabelAttrib ),null, null, null,null);
      col4.add(WebUtil.NotNull);
      row4.add(col4) ;
      row4.add(new TableCol(util.createTextItem( nUserID, "SY_CONFIG", "vProperty_Value", pvMode, "20", "100", rConfig[5], null, vTextAttrib),null, null, null,null));
      tab.add(row);
      tab.add(row1);
      tab.add(row2);
      tab.add(row3);
      tab.add(row4);
      form.add(tab);
      form.add(new FormHidden("vAction", null, null));
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
      String vStatus=null;
      String query = null;
      String nLangID=null;
      String nUserID=null;
     
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
     
      String pnTransID = request.getParameter("pnTransID");
      String pnConfigID = request.getParameter("pnConfigID");
      String vParentObj = Parse.formatStr(request.getParameter("vParent_Obj"));
      String vLangID = request.getParameter("nFk_Lang_ID");
      String vObjName = Parse.formatStr(request.getParameter("vObj_Name"));
      String vObjProperty = Parse.formatStr(request.getParameter("vObj_Property"));
      String vPropertyValue = Parse.formatStr(request.getParameter("vProperty_Value"));
      String vAction = request.getParameter("vAction"); 

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      IngDate dt = new IngDate();
  
      String usr = db.getName( nUserID, "User" );      
      vStatus = Trans.checkTransValidity( pnTransID );
     
      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction.equalsIgnoreCase("Insert") || vAction.equalsIgnoreCase("SaveInsert") )
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            String configid = db.getNextVal("S_Config");
            query = "INSERT INTO T_Config ( Config_ID, Parent_Obj, Fk_Lang_ID, Obj_Name, Obj_Property, Property_Value ) " +
                    "VALUES ("+configid+", "+val.IsNull(vParentObj)+","+val.IsNull(vLangID)+","+val.IsNull(vObjName)+","+val.IsNull(vObjProperty)+","+val.IsNull(vPropertyValue)+")";
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
            query = "UPDATE T_Config SET Parent_Obj = "+val.IsNull(vParentObj)+"," +
                    " Fk_Lang_ID   = "+val.IsNull(vLangID)+"," +
                    " Obj_Name     = "+val.IsNull(vObjName)+"," + 
                    " Obj_Property = "+val.IsNull(vObjProperty)+"," +
                    " Property_Value = "+val.IsNull(vPropertyValue)+ 
                    " WHERE Config_ID = "+pnConfigID;
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
     if(nMsgID <=5 )
     {
        if(vAction.equalsIgnoreCase("SaveInsert"))
          response.sendRedirect("/JOrder/servlets/ConfigFrame?pvMode=I&pnConfigID=&pvParentObj="+vParentObj+"&pnLangID="+vLangID+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
        else 
          response.sendRedirect("/JOrder/servlets/ConfigFrame?pvMode=&pnConfigID=&pvParentObj="+vParentObj+"&pnLangID="+vLangID+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     }
     else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
    }
}

