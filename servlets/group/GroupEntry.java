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

public class GroupEntry extends HttpServlet
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
      String rGroup[] = null;
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
      String pnGroupID = request.getParameter("pnGroupID");

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "SY_GROUP", nLangID, "WD_FORM_INSERT", "Group / I" );
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "SY_GROUP", nLangID, "WD_FORM_UPDATE", "Group / U" );

      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Group/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pnGroupID!=null && !pnGroupID.equals("") && !pnGroupID.equalsIgnoreCase("null"))
      { 
        rGroup = db.getRecord(pnGroupID,"Group");
      }  
      else
        rGroup = new String[5];
 
      Page page = new Page();
      Head head = new Head(); 

      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/GroupEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      form.add(new FormHidden("pnTransID", nTransID, null ));
      form.add(new FormHidden("pnGroupID", pnGroupID, null ));
      form.add(new FormHidden("vAction",null, null));
      form.add(new FormHidden("nDelete",null, null));
      form.add(new FormHidden("nFk_User_ID",null, null));
      NL blines = new NL(5);
      form.add(blines);

      Table tab = new Table("1","center","Border=\"0\" width=\"70%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_LABEL.B_GROUP_GROUP_NAME", "Group Name" );
      TableCol col = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Name.gif", vBPLate, vLabelAttrib ),null, null, null,"Width=\"25%\"");
      col.add(WebUtil.NotNull);
      TableCol col1 = null;
      if( pvMode!=null && pvMode.equalsIgnoreCase("U"))
      {
        col1 = new TableCol( util.createLabelItem(rGroup[1],vLabelAttrib),null,null,null,null);
        FormHidden hid1 = new FormHidden("vGroup_Name", rGroup[1], null);
        form.add( hid1 );
      } 
      else            
        col1 = new TableCol(util.createTextItem( nUserID, "SY_GROUP", "vGroup_Name", pvMode, "20", "30", rGroup[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,null);
      row.add(col) ;
      row.add(col1);
      TableRow row2 = new TableRow("Left",null,null);      
      vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_LABEL.B_GROUP_GROUP_DESC", "Description");
      TableCol col4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"Type.gif", vBPLate, vLabelAttrib ),null, null, null,"Width=\"25%\"");
      TableCol col5 = new TableCol(util.createTextItem( nUserID, "SY_GROUP", "vGroup_Desc", pvMode, "30", "100", rGroup[2], null, vTextAttrib),null,null,null,null); 
      row2.add(col4) ;
      row2.add(col5);
      tab.add(row);
      tab.add(row2);
      form.add(tab);
      body.add(form);
      head.add(scr);
      page.add(head);
      page.add(body);
      page.printPage(out);
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
      String pnGroupID = request.getParameter("pnGroupID");
      String vName = Parse.formatStr(request.getParameter("vGroup_Name"));
      String vDesc = Parse.formatStr(request.getParameter("vGroup_Desc"));
      String FkUserID = request.getParameter("nFk_User_ID");
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
            int Group_id = Integer.parseInt(db.getNextVal("S_Group"));
            query = "INSERT INTO T_Group ( Group_ID, Group_Name, Group_Desc, Modifier, Change_Dt ) " +
                    "VALUES ("+Group_id+", "+val.IsNull(vName)+","+val.IsNull(vDesc)+",'"+usr+"', '"+dt+"')";
            stmt.executeUpdate(query);
            stmt.close();

            if( !(FkUserID.trim()==null || FkUserID.trim().equals("") || FkUserID.trim().equalsIgnoreCase("null") ) )
            {
              vIDArray=Parse.parse(FkUserID,"~");
              query = "INSERT INTO T_UserGroup(UserGroup_ID, Fk_Group_ID, Fk_User_ID, Modifier, Change_Dt) "+
                      "  VALUES( ?, "+Group_id+", ?,'"+usr+"' , '"+dt+"') ";
              pstmt = conn.prepareStatement(query);
              for(int i=0;i<vIDArray.length;i++)
              {  
                String id = vIDArray[i];
                int UserGroup_id = Integer.parseInt(db.getNextVal("S_UserGroup"));
                pstmt.setInt(1,UserGroup_id);
                pstmt.setInt(2,Integer.parseInt(id));
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
       else if( vAction.equalsIgnoreCase("Update"))
       {
         try
         {
           conn = db.GetDBConnection();
           stmt = conn.createStatement();       

           query = "UPDATE T_Group SET Group_Name = "+val.IsNull(vName)+"," +
                  " Group_Desc   = "+val.IsNull(vDesc)+"," + 
                  " Modifier    = '"+usr+"'," + 
                  " Change_Dt   = '"+dt+"'" + 
                  " WHERE Group_ID = "+pnGroupID;
           stmt.executeUpdate(query);
           stmt.close();
           if( !(vDelete.trim()==null || vDelete.trim().equals("") || vDelete.trim().equalsIgnoreCase("null") ) )
           {
             vIDArray=Parse.parse(vDelete,"~");
             query = "Delete From T_UserGroup Where Fk_User_ID = ? AND Fk_Group_ID = " + pnGroupID;
	     pstmt = conn.prepareStatement(query);
             for(int i=0;i<vIDArray.length;i++)
             {  
                String id = vIDArray[i];
                pstmt.setInt(1,Integer.parseInt(id));
                pstmt.executeUpdate();
             }
             pstmt.close();
           }

           if( !(FkUserID.trim()==null || FkUserID.trim().equals("") || FkUserID.trim().equalsIgnoreCase("null") ) )
           {
             vIDArray=Parse.parse(FkUserID,"~");
             query = "INSERT INTO T_UserGroup(UserGroup_ID, Fk_Group_ID, Fk_User_ID, Modifier, Change_Dt) "+
                     "  VALUES( ?, "+pnGroupID+", ?,'"+usr+"' , '"+dt+"') ";
             pstmt = conn.prepareStatement(query);
             for(int i=0;i<vIDArray.length;i++)
             {  
               String id = vIDArray[i];
               int UserGroup_id = Integer.parseInt(db.getNextVal("S_UserGroup"));
               pstmt.setInt(1,UserGroup_id);
               pstmt.setInt(2,Integer.parseInt(id));
               pstmt.executeUpdate();
             }
           }
           nMsgID = 5;
         }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=8;}
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
     { 
       if(vAction.equalsIgnoreCase("SaveInsert"))
         response.sendRedirect("/JOrder/servlets/GroupFrame?pvMode=I&pnGroupID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
        else
         response.sendRedirect("/JOrder/servlets/GroupFrame?pvMode=&pnGroupID=&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
      }
      else
         WebUtil.ShowException(errMsg, nMsgID, nLangID).printPage(out);
    }
}


