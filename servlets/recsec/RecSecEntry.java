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

public class RecSecEntry extends HttpServlet
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
      String rRecSec[] = null;
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
      String pnRecSecID = request.getParameter("pnRecSecID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "SY_RECSEC", nLangID, "WD_FORM_INSERT", "Record Security / I" );
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "SY_RECSEC", nLangID, "WD_FORM_UPDATE", "Record Security / U" );

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_RecSec/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnRecSecID!=null && !pnRecSecID.equals("") && !pnRecSecID.equalsIgnoreCase("null"))
        rRecSec = db.getRecord(pnRecSecID,"RecSec");
      else
        rRecSec = new String[5];
 
      Page page = new Page();
      Head head = new Head(); 

      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/RecSecEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add(new FormHidden("pnRecSecID", pnRecSecID, null ));
      form.add(new FormHidden("vAction",null, null));
      form.add(new FormHidden("nDelete",null, null));
      form.add(new FormHidden("vRecSecPriv",null, null));
      form.add(new NL(5));
      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_LABEL.B_RECSEC_RECSEC_NAME", "Record Security Name" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.gif", vBPLate, vLabelAttrib ),null, null, null,"Width=\"25%\"");
      col.add(WebUtil.NotNull); 
      TableCol col1 = null;
      if( pvMode!=null && pvMode.equalsIgnoreCase("U"))
      {
        col1 = new TableCol( util.createLabelItem( rRecSec[1], vLabelAttrib), null,null,null,null);
        form.add( new FormHidden("vRecSec_Name", rRecSec[1], null) );
      } 
      else            
        col1 = new TableCol(util.createTextItem( nUserID, "SY_RECSEC", "vRecSec_Name", pvMode, "20", "30", rRecSec[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,null);
      row.add(col) ;
      row.add(col1);

      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_RECSEC", nLangID, "BL_LABEL.B_RECSEC_RECSEC_DESC", "Description" );
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Type.gif", vBPLate, vLabelAttrib ),null, null, null,"Width=\"25%\"");
      TableCol col5 = new TableCol(util.createTextItem( nUserID, "SY_RECSEC", "vRecSec_Desc", pvMode, "30", "100", rRecSec[2], null, vTextAttrib),null,null,null,null);
      row2.add(col4) ;
      row2.add(col5);
      tab.add(row);
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
      int nMsgID=-1;
      
      String errMsg=null;  
      String vStatus=null;
      String query = null;
      String nLangID=null;
      String nUserID=null;
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
 
      String pnTransID = request.getParameter("pnTransID");
      String pnRecSecID = request.getParameter("pnRecSecID");
      String vName = request.getParameter("vRecSec_Name");
      String vDesc = request.getParameter("vRecSec_Desc");
      String rPriv = request.getParameter("vRecSecPriv");
      String vDelete = request.getParameter("nDelete");
      String vAction = request.getParameter("vAction");
      String vIDArray[]=null;


      Statement stmt = null;
      PreparedStatement pstmt = null;
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
            int recsec_id = Integer.parseInt(db.getNextVal("S_RecSec"));
            query = "INSERT INTO T_RecSec ( RecSec_ID, RecSec_Name, RecSec_Desc, Modifier, Change_Dt ) " +
                    "VALUES ("+recsec_id+", "+val.IsNull(vName)+","+val.IsNull(vDesc)+",'"+usr+"', '"+dt+"')";
            stmt.executeUpdate(query);
            stmt.close();

            if( !(rPriv.trim()==null || rPriv.trim().equals("") || rPriv.trim().equalsIgnoreCase("null") ) )
            { 
              vIDArray=Parse.parse(rPriv,"~");
              query = "INSERT INTO T_RecSecPriv(RecSec_ID, Managed_RecSec_ID, Modifier, Change_Dt) "+
                      "  VALUES( "+recsec_id+", ?,'"+usr+"' , '"+dt+"') ";
              pstmt = conn.prepareStatement(query);
              for(int i=0;i<vIDArray.length;i++)
              {  
                String id = vIDArray[i];
                pstmt.setInt(1,Integer.parseInt(id));
                pstmt.executeUpdate();
              }
              pstmt.close();
            }
            nMsgID = 3;
         }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
         finally
         {
           try
           {
             if(stmt!=null)
               stmt.close();
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

           query = " UPDATE T_RecSec SET RecSec_Name = "+val.IsNull(vName)+"," +
                   " RecSec_Desc   = "+val.IsNull(vDesc)+"," + 
                   " Modifier    = '"+usr+"'," + 
                   " Change_Dt   = '"+dt+"'" + 
                   " WHERE RecSec_ID = "+pnRecSecID;
           stmt.executeUpdate(query);
           stmt.close();
           if( !(vDelete.trim()==null || vDelete.trim().equals("") || vDelete.trim().equalsIgnoreCase("null") ) )
           {
             vIDArray=Parse.parse(vDelete,"~");
             query = "Delete From T_RecSecPriv Where Managed_RecSec_ID = ? AND RecSec_ID = " + pnRecSecID;
       	     pstmt = conn.prepareStatement(query);
             for(int i=0;i<vIDArray.length;i++)
             {  
               String id = vIDArray[i];
               pstmt.setInt(1,Integer.parseInt(id));
               pstmt.executeUpdate();
             }
             pstmt.close();
           } 

           if( !(rPriv.trim()==null || rPriv.trim().equals("") || rPriv.trim().equalsIgnoreCase("null") ) )
           { 
             vIDArray=Parse.parse(rPriv,"~");
             query = "INSERT INTO T_RecSecPriv(RecSec_ID, Managed_RecSec_ID, Modifier, Change_Dt) "+
                     "  VALUES( "+pnRecSecID+", ?,'"+usr+"' , '"+dt+"') ";
             pstmt = conn.prepareStatement(query);
             for(int i=0;i<vIDArray.length;i++)
             {  
               String id = vIDArray[i];
               pstmt.setInt(1,Integer.parseInt(id));
               pstmt.executeUpdate();
             }
             pstmt.close();
           }
           nMsgID = 5;
         }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=8;}
         finally
         {
           try
           {
             if(stmt!=null)
               stmt.close();
             if(pstmt!=null)
               pstmt.close();
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
         response.sendRedirect("/JOrder/servlets/RecSecFrame?pvMode=I&pnRecSecID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         response.sendRedirect("/JOrder/servlets/RecSecFrame?pvMode=&pnRecSecID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     }
     else
        out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}