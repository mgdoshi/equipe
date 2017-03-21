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

public class PrefEntry extends HttpServlet
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
      String rPref[] = null;
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

      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      WebUtil util = new WebUtil();

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Pref/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      if(pvMode.equalsIgnoreCase("I"))
         vFormType = cdata.GetConfigValue( "SY_SCHEME", nLangID, "WD_FORM_INSERT","Preference / I");                      
      else if(pvMode.equalsIgnoreCase("U"))
         vFormType = cdata.GetConfigValue( "SY_SCHEME", nLangID, "WD_FORM_UPDATE","Preference / U");

      String vColorQry = " SELECT Attrib_Desc, Attrib_Desc FROM T_Domain WHERE Domain = 'FONTCOLOR' AND Fk_Lang_ID = " + nLangID +
                            " ORDER BY Sequence_Nr";

      String vSizeQry = " SELECT Attrib_Desc, Attrib_Desc FROM T_Domain WHERE Domain = 'FONTSIZE' AND Fk_Lang_ID = " + nLangID +
                            " ORDER BY Sequence_Nr";

      String vFaceQry = " SELECT Attrib_Desc, Attrib_Desc FROM T_Domain WHERE Domain = 'FONTFACE' AND Fk_Lang_ID = " + nLangID +
                            " ORDER BY Sequence_Nr";

      nTransID = Trans.getTransID( nAuditID, 'M');
    
      if(nSchemeID!=null)
        rPref = db.getRecord(nSchemeID,"Scheme");
      else
        rPref = new String[3]; 

      Page page = new Page();
      Head head = new Head();
      Body body = new Body("/ordimg/BACKGR2.GIF",null); 
      Form form = new Form("/JOrder/servlets/PrefEntry","POST","_parent",null,null);

      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);

      form.add(new FormHidden("pnTransID", nTransID, null ));

      Table tab = new Table("1","center","Border=\"0\" width=\"80%\" COLS=2");
      TableRow row = new TableRow("Left",null,null);

      vBPLate = cdata.GetConfigValue("SY_SCHEME", nLangID, "BL_SCHEME.B_SCHEME_SCHEME_NAME","Scheme Name");
      row.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"SchName.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"")) ;
      row.add(new TableCol(util.createTextItem( nUserID, "SY_SCHEME", "vSchemeName", pvMode, "20", "30", rPref[1], "onBlur=\"this.value=this.value.toUpperCase()\"", vTextAttrib),null,null,null,null)) ;

      TableRow row1 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_SCHEME", nLangID, "BL_SCHEME.B_SCHEME_SCHEME_DESC","Scheme Description");
      row1.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"SchDesc.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"")) ;
      row1.add( new TableCol(util.createTextItem( nUserID, "SY_SCHEME", "vSchemeDesc", pvMode, "30", "100", rPref[2],null, vTextAttrib),null,null,null,null)) ;

      tab.add(row);
      tab.add(row1);

      Table tab1 = new Table("1","center","Border=\"0\" width=\"80%\" COLS=4");  
      TableRow row3 = new TableRow("Left",null,null);
      vBPLate = cdata.GetConfigValue("SY_SCHEME", nLangID, "BL_LABEL.B_SCHEME_OBJTYPE","Object Type");
      row3.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TYPE.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"20%\"")); 
      vBPLate = cdata.GetConfigValue("SY_SCHEME", nLangID, "BL_LABEL.B_SCHEME_FONTSIZE","Font Size");
      row3.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TYPE.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"20%\"")); 
      vBPLate = cdata.GetConfigValue("SY_SCHEME", nLangID, "BL_LABEL.B_SCHEME_FONTFACE","Font Face");
      row3.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TYPE.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"")); 
      vBPLate = cdata.GetConfigValue("SY_SCHEME", nLangID, "BL_LABEL.B_SCHEME_COLOR","Font Color");
      row3.add(new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"TYPE.gif", vBPLate, vLabelAttrib ),null, null, null,"WIDTH=\"25%\"")); 
      tab1.add(row3); 

      Connection conn = null;
      Statement stmt = null; 
      Statement stmt1 = null; 
      ResultSet rsouter=null;
      ResultSet rs =null;
      try
      { 

        String query = " SELECT Attrib, Attrib_Desc FROM T_Domain WHERE Domain = 'OBJTYPE' AND Fk_Lang_ID = " + nLangID +
                       " ORDER BY Sequence_Nr";

        conn = db.GetDBConnection();
        stmt = conn.createStatement(); 
        stmt1 = conn.createStatement(); 

        TableCol col12 = new TableCol(util.createList( nUserID, "SY_SCHEME", "vFontSize", pvMode, vSizeQry, null, null, vListAttrib),null,null,null,null);
        TableCol col13 = new TableCol(util.createList( nUserID, "SY_SCHEME", "vFontFace", pvMode, vFaceQry, null, null, vListAttrib),null,null,null,null);
        TableCol col14 = new TableCol(util.createList( nUserID, "SY_SCHEME", "vFontColor", pvMode, vColorQry, null, null, vListAttrib),null,null,null,null);
        rsouter = stmt.executeQuery(query);  
        while(rsouter.next())
        {
           if(nSchemeID!=null)
           {             
             String query1 = " SELECT Prop1,Prop2,Prop3 FROM   T_SchemeRef WHERE  Fk_Scheme_ID = " + nSchemeID +
                            " AND DM_OBJTYPE = '"+rsouter.getString(1)+"'";
             rs = stmt1.executeQuery(query1);
             while(rs.next())
             { 
               TableRow row4 = new TableRow("Left",null,null);
               TableCol col10 = new TableCol( util.createLabelItem( rsouter.getString(2), vLabelAttrib ),null, null, null,null);
               col10.add(new FormHidden("vObjType", rsouter.getString(1), null ));
               row4.add(col10); 
               row4.add(new TableCol(util.createList( nUserID, "SY_SCHEME", "vFontSize", pvMode, vSizeQry, Parse.GetSubString(rs.getString(1),"\"",1), null, vListAttrib),null,null,null,null)); 
               row4.add(new TableCol(util.createList( nUserID, "SY_SCHEME", "vFontFace", pvMode, vFaceQry, Parse.GetSubString(rs.getString(2),"\"",1), null, vListAttrib),null,null,null,null)); 
               row4.add(new TableCol(util.createList( nUserID, "SY_SCHEME", "vFontColor", pvMode, vColorQry, Parse.GetSubString(rs.getString(3),"\"",1), null, vListAttrib),null,null,null,null)); 
               tab1.add(row4); 
            }
            rs.close(); 
            stmt1.close();            
          }     
          else
          {
      	     TableRow row4 = new TableRow("Left",null,null);
             TableCol col10 = new TableCol( util.createLabelItem( rsouter.getString(2), vLabelAttrib ),null, null, null,null);
             col10.add(new FormHidden("vObjType", rsouter.getString(1), null ));
             row4.add(col10); 
             row4.add(col12); 
             row4.add(col13); 
             row4.add(col14); 
	     tab1.add(row4); 
          } 
        }
      }catch(Exception e){System.out.println(e);}  
      finally
      {
        try
        {
          if(rsouter!=null)
           rsouter.close();
          if(rs!=null)
           rs.close();
          if(stmt1!=null)
           stmt1.close();
          if(stmt!=null)
           stmt.close();
          if(conn!=null)
           conn.close(); 
        }catch(SQLException sexe){}
      }
      form.add(tab); 
      form.add(tab1); 
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
      String nSchemeID=null;
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      PreparedStatement pstmt=null;
      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      Message msg = new Message(); 
      WebUtil util = new WebUtil();
      IngDate dt = new IngDate();

      String pnTransID   = request.getParameter("pnTransID");
      String vSchemeName = request.getParameter("vSchemeName");
      String vSchemeDesc = request.getParameter("vSchemeDesc");
      String []vObjType    = request.getParameterValues("vObjType");
      String []vFontFace   = request.getParameterValues("vFontFace");
      String []vFontSize   = request.getParameterValues("vFontSize");
      String []vFontColor  = request.getParameterValues("vFontColor"); 

      if(nSchemeID==null)
      {
	  try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            int scheme_id = Integer.parseInt(db.getNextVal("S_Scheme"));
            query = "INSERT INTO T_Scheme ( Scheme_ID, Scheme_Name, Scheme_Desc ) " +
                    " VALUES ("+scheme_id+", '"+vSchemeName+"', '"+vSchemeDesc+"')";
            stmt.executeUpdate(query);

            String query1 = "INSERT INTO T_SchemeRef ( Fk_Scheme_ID, DM_OpMode, DM_ObjType, Prop1, Prop2, Prop3 ) " +
                            " VALUES ("+scheme_id+", 'N', ?, ? ,?, ?)";
            pstmt = conn.prepareStatement(query1);   
            for(int i=0;i<vObjType.length;i++)
            {
               pstmt.setString(1, vObjType[i] );
               pstmt.setString(2,"SIZE=\"" + vFontSize[i]+"\"");
               pstmt.setString(3,"FACE=\"" + vFontFace[i]+"\"");
               pstmt.setString(4,"COLOR=\"" + vFontColor[i]+"\"");
               pstmt.executeUpdate();
            } 
            pstmt.close();
            query = " Update T_User " +
                    " Set Fk_Scheme_ID = " + scheme_id +
                    " Where User_ID = " + nUserID ;
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
      else
      {
         try
         {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();
            query = " UPDATE T_Scheme " +
                    " SET    Scheme_Name = '"+vSchemeName+"'," + 
                    " Scheme_Desc = '"+vSchemeDesc+"' " +  
                    " WHERE  Scheme_ID = " + nSchemeID;
            stmt.executeUpdate(query); 

            query = " DELETE FROM T_SCHEMEREF " +
                    " Where  Fk_Scheme_ID =" + nSchemeID;
            stmt.executeUpdate(query); 

            String query1 = "INSERT INTO T_SchemeRef ( Fk_Scheme_ID, DM_OpMode, DM_ObjType, Prop1, Prop2, Prop3 ) " +
                            " VALUES ("+nSchemeID+", 'N', ?, ? ,?, ?)";
            pstmt = conn.prepareStatement(query1);   
            for(int i=0;i<vObjType.length;i++)
            {
               pstmt.setString(1, vObjType[i] );
               pstmt.setString(2,"SIZE=\"" + vFontSize[i]+"\"");
               pstmt.setString(3,"FACE=\"" + vFontFace[i]+"\"");
               pstmt.setString(4,"COLOR=\"" + vFontColor[i]+"\"");
               pstmt.executeUpdate();
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
      if(nMsgID <=5 )
        response.sendRedirect("/JOrder/servlets/PrefFrame?pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
      else
         out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}

