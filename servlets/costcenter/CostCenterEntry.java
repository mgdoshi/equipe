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

public class CostCenterEntry extends HttpServlet
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
      String rCostCenter[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
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
      String pnCostCentreID = request.getParameter("pnCostCentreID");


      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "ST_COSTCENTRE", nLangID, "WD_FORM_INSERT","CostCenter / I");                      
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "ST_COSTCENTRE", nLangID, "WD_FORM_UPDATE","CostCenter / U");                      

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_CostCenter/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnCostCentreID!=null && !pnCostCentreID.equals("") && !pnCostCentreID.equalsIgnoreCase("null"))
      { 
        rCostCenter = db.getRecord(pnCostCentreID,"CostCentre");
      }  
      else
        rCostCenter = new String[5];  

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/CostCenterEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      FormHidden transid = new FormHidden("pnTransID", nTransID, null ); 
      FormHidden itmclsid = new FormHidden("pnCostCentreID", pnCostCentreID, null ); 
      FormHidden actid = new FormHidden("vAction", null, null ); 
      form.add(transid);
      form.add(itmclsid);
      form.add(actid);

      NL blines = new NL(5);
      form.add(blines);

      Table tab = new Table("1","center","Border=\"0\" width=\"60%\" COLS=2");
 
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_COSTCENTRE", nLangID, "BL_LABEL.B_COSTCENTRE_COSTCENTRE_NAME","CostCenter Name");
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"NAME.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"40%\""); 
      col.add(WebUtil.NotNull);
      TableCol col1 = new TableCol(util.createTextItem( nUserID, "ST_COSTCENTRE", "vCostCenter_Name", pvMode, "15", "30", rCostCenter[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,"WIDTH=\"25%\"");
      row.add(col) ;
      row.add(col1) ;

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("ST_COSTCENTRE", nLangID, "BL_LABEL.B_COSTCENTRE_COSTCENTRE_DESC","Description");
      TableCol col2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null, null); 
      TableCol col3 = new TableCol(util.createTextItem( nUserID, "ST_COSTCENTRE", "vCostCenter_Desc", pvMode, "30", "100", rCostCenter[2], null, vTextAttrib),null,null,null,null);
      row1.add(col2) ;
      row1.add(col3) ;

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
      int nMsgID=-1;
      String errMsg=null; 
      String vStatus=null;
      String query = null;
      String nLangID=null;
      String nUserID=null;

      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      DBConnect db = new DBConnect();
      Valid val = new Valid();
      Message msg = new Message(); 
      IngDate dt = new IngDate();

 
      String pnTransID = request.getParameter("pnTransID");
      String pnCostCentreID = request.getParameter("pnCostCentreID");
      String vName     = request.getParameter("vCostCenter_Name");
      String vDesc    = request.getParameter("vCostCenter_Desc");
      String vAction   = request.getParameter("vAction"); 

      String usr       = db.getName( nUserID, "User" );      
      vStatus          = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction.equalsIgnoreCase("Insert") || vAction.equalsIgnoreCase("SaveInsert") )
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            int ccenter_id = Integer.parseInt(db.getNextVal("S_CostCentre"));
            query = "INSERT INTO t_CostCentre (  CostCentre_ID, CostCentre_Name, CostCentre_Desc, Modifier, Change_Dt  ) " +
                    " VALUES ("+ccenter_id+", "+val.IsNull(vName)+","+val.IsNull(vDesc)+",'"+usr+"', '"+dt+"')";
            stmt.executeUpdate(query);
            nMsgID=3; 
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
            query = "UPDATE t_CostCentre SET CostCentre_Name = "+val.IsNull(vName)+"," +
                    " CostCentre_Desc = "+val.IsNull(vDesc)+"," + 
                    " Modifier    = '"+usr+"'," + 
                    " Change_Dt   = '"+dt+"'" + 
                    " WHERE CostCentre_ID = "+pnCostCentreID;
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
      if(nMsgID <= 5)
      { 
        if(vAction.equalsIgnoreCase("SaveInsert"))
          response.sendRedirect("/JOrder/servlets/CostCenterFrame?pvMode=I&pnCostCentreID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
        else
          response.sendRedirect("/JOrder/servlets/CostCenterFrame?pvMode=&pnCostCentreID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
      }
      else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

