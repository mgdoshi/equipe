import ingen.html.*;
import ingen.html.para.*;
import ingen.html.character.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;

import java.awt.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class OrderUpdEntry extends HttpServlet
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
      String nClientID=null;
      String nSchemeID=null;
      String nTransID=null;
      String rOrder[] = null;
      String rAddress[] = null;
      String rTempl[] = null;
      String vImagePath=null;
      String vBPLate=null;
      String vBPLate1=null;
      String vImgOption=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vAutoGen=null;
      String vOrderNr=null;
      String vOrdStatQry = null;
      String vClientQry = null;
      String templname=null; 
      String tname=null; 
      String cname=null; 
      String caddr=null; 
      String logoname=null;
      String note=null;
      String inst=null;

      DBConnect db = new DBConnect();
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      String str[]= new String[7];
      Rectangle rect[] =  new Rectangle[7]; 
      String tag = null;
      int bid = -1;
      int sid = -1;

      String pvMode = request.getParameter("pvMode");
      String pnOrderID = request.getParameter("pnOrderID");
      String vTemplName = request.getParameter("vTemplName");
      String Browser = request.getParameter("Browser");

      ConfigData cdata = new ConfigData();
      ConfigData cdata1 = new ConfigData();
      WebUtil util = new WebUtil();
      HtmlTag htag = new HtmlTag();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nClientID   = Parse.GetValueFromString( vPID, "ClientID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
      vImgOption = Parse.GetValueFromString( vPID, "Image" );

      vFormType = cdata.GetConfigValue( "TR_ORDER", nLangID, "WD_ORDER", "Order Entry" );
      nTransID = Trans.getTransID( nAuditID, 'M');

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Order/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
    
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      vOrdStatQry = " SELECT Attrib, Attrib_Desc FROM T_Domain "+
                    " WHERE Domain = 'ORDSTAT' "+
                    " AND Fk_Lang_ID = "+ nLangID +
                    " ORDER BY Sequence_NR";

      if(pnOrderID!=null && !pnOrderID.equals("") && !pnOrderID.equalsIgnoreCase("null"))
      { 
        rOrder = db.getRecord( pnOrderID, "Order" );
      }  
      else
        rOrder = new String[30];

      try
      {
        conn =  db.GetDBConnection();
        stmt = conn.createStatement();
        String query = " SELECT * from T_CLSTEMPL "+
                       " WHERE Alias_Name = '"+ vTemplName +"'"+
                       " AND Ref_ID = "+nUserID;
        rs = stmt.executeQuery(query);
        if(rs.next())
        {
          templname = rs.getString("Templ_Name");
          tname    = rs.getString("text1");
          logoname = rs.getString("text2");
          cname    = rs.getString("text3");
          note     = rs.getString("text4");
          caddr    = rs.getString("text9");
          inst    = rs.getString("text10");         
        }
 
      }catch(SQLException sexe){System.out.println(sexe.getMessage());}
      finally 
      {
        try
        {
          if(stmt!=null)
            stmt.close();
          if(conn!=null)
            conn.close();
        }catch(SQLException sexe){System.out.println(sexe.getMessage());}
      }

      if(templname.equalsIgnoreCase("OrdTempl1"))
      {
        rect[0] = new Rectangle(0,0,100,25);  
        rect[1] = new Rectangle(20,65,75,75);  
        rect[2] = new Rectangle(120,60,320,25);  
        rect[3] = new Rectangle(120,87,320,75);  
        rect[4] = new Rectangle(500,65,100,75);  
        rect[5] = new Rectangle(15,170,350,100);
        rect[6] = new Rectangle(375,170,350,100);  
      }
      else if(templname.equalsIgnoreCase("OrdTempl2"))
      {
        rect[0] = new Rectangle(0,0,100,25);  
        rect[1] = new Rectangle(330,70,75,75);
        rect[2] = new Rectangle(15,60,320,25);  
        rect[3] = new Rectangle(15,90,320,75);  
        rect[4] = new Rectangle(500,65,100,75);  
        rect[5] = new Rectangle(15,170,350,100);
        rect[6] = new Rectangle(375,170,350,100);  
      }
      else if(templname.equalsIgnoreCase("OrdTempl3"))
      {
        rect[0] = new Rectangle(0,0,100,25);  
        rect[1] = new Rectangle(500,70,75,75);  
        rect[2] = new Rectangle(20,65,350,25);  
        rect[3] = new Rectangle(20,90,350,40);  
        rect[4] = new Rectangle(20,135,100,75);  
        rect[5] = new Rectangle(15,190,350,100);  
        rect[6] = new Rectangle(375,190,350,100);  
      }
 
      if( Browser.equals("1") )
      {
        tag = "</Layer>";
        str[0] = "<Layer id=\"player\" left="+rect[0].x+" top="+rect[0].y+" width="+rect[0].width+"% height="+rect[0].height+" border=\"0\" visibility=\"show\"> ";
        str[1] = "<Layer id=\"ilayer\" left="+rect[1].x+" top="+rect[1].y+" width="+rect[1].width+" height="+rect[1].height+" border=\"0\" visibility=\"show\"> ";
        str[2] = "<Layer id=\"clayer\" left="+rect[2].x+" top="+rect[2].y+" width="+rect[2].width+" height="+rect[2].height+" border=\"0\" visibility=\"show\"> ";
        str[3] = "<Layer id=\"alayer\" left="+rect[3].x+" top="+rect[3].y+" width="+rect[3].width+" height="+rect[3].height+" border=\"0\" visibility=\"show\"> ";
        str[4] = "<Layer id=\"olayer\" left="+rect[4].x+" top="+rect[4].y+" border=\"0\"> ";
        str[5] = "<Layer id=\"blayer\" left="+rect[5].x+" top="+rect[5].y+" width="+rect[5].width+" height="+rect[5].height+" border=\"2\" visibility=\"show\">";
        str[6] = "<Layer id=\"slayer\" left="+rect[6].x+" top="+rect[6].y+" width="+rect[6].width+" height="+rect[6].height+" border=\"2\" visibility=\"show\"> ";
        bid = 1;
        sid = 2;
      }
      else
      {
        tag = "</Div>";
        str[0] = "<div id=\"player\" style=\"position:absolute; width:"+rect[0].width+"%; height:"+rect[0].height+"; left: "+rect[0].x+"; top: "+rect[0].y+"; visibility:visible\">";
        str[1] = "<div id=\"ilayer\" style=\"position:absolute; width:"+rect[1].width+"; height:"+rect[1].height+"; left: "+rect[1].x+"; top: "+rect[1].y+"; visibility:visible\">";
        str[2] = "<div id=\"clayer\" style=\"position:absolute; width:"+rect[2].width+"; height:"+rect[2].height+"; left: "+rect[2].x+"; top: "+rect[2].y+"; visibility:visible\">";
        str[3] = "<div id=\"alayer\" style=\"position:absolute; width:"+rect[3].width+"; height:"+rect[3].height+"; left: "+rect[3].x+"; top: "+rect[3].y+"; visibility:visible\">";
        str[4] = "<div id=\"olayer\" style=\"position:absolute; left:"+rect[4].x+"; top:"+rect[4].y+"\"> ";
        str[5] = "<div id=\"blayer\" style=\"position:absolute; width:"+rect[5].width+"; height:"+rect[5].height+"; left: "+rect[5].x+"; top: "+rect[5].y+"; visibility:visible\">";
        str[6] = "<div id=\"slayer\" style=\"position:absolute; width:"+rect[6].width+"; height:"+rect[6].height+"; left: "+rect[6].x+"; top: "+rect[6].y+"; visibility:visible\">";
        bid = 3;
        sid = 4;
      }

     htag.add(str[0]);
     htag.add("<b><font face=\"Arial,Helvetica\" color=\"#3333FF\" size=+2>&nbsp; ");
     htag.add("<CENTER>"+tname+"</font></b></CENTER>");
     htag.add(tag);
     htag.add("<BR>");

     htag.add(str[1]);
     htag.add("<img SRC=\"/ordimg/"+logoname+"\" border=\"0\" height=75 width=75 align=ABSCENTER>");
     htag.add(tag);

     htag.add(str[2]);
     htag.add("<font face=\"Arial,Helvetica\" color=\"#3333FF\" size=+2> ");
     htag.add(cname);
     htag.add("</font> ");
     htag.add(tag);

     htag.add(str[3]);
     htag.add("<font face=\"Arial,Helvetica\" color=\"#000000\" size=-1> ");
     htag.add(caddr);
     htag.add("</font>");
     htag.add(tag);

     if( rOrder[6]!=null && !rOrder[6].equals("") && !rOrder[6].equalsIgnoreCase("null"))
     { 
       rAddress = db.getRecord( rOrder[6], "Address" );
     }  
     else
       rAddress = new String[17];

     htag.add("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"100%\" >");
     htag.add("<tr><td width=\"50%\">");
     htag.add(str[5]);
     htag.add("<b>Billing Address :</b>");
     htag.add("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"80%\" >");
     htag.add("<tr><td width=\"30%\">&nbsp;Name </td><td>" + db.getName( nUserID, "User") +"</td></tr>");
     htag.add("<tr><td>&nbsp;Address  </td><td>"+rAddress[2]+"</td></tr>");
     htag.add("<tr><td>&nbsp;City </td><td>"+rAddress[6]+"</td></tr>");
     htag.add("<tr><td>&nbsp;State </td><td>"+rAddress[7]+"</td></tr>");
     htag.add("<tr><td>&nbsp;Pin Code </td><td>"+rAddress[8]+"</td></tr>");
     htag.add("<tr><td>&nbsp;Phone No </td><td>"+rAddress[9]+"</td></tr>");
     htag.add("<tr><td>&nbsp;Fax No  </td><td>"+rAddress[10]+"</td></tr>");
     htag.add("</table>");
     htag.add(tag);
     htag.add("</td><td>");

     if( rOrder[7]!=null && !rOrder[7].equals("") && !rOrder[7].equalsIgnoreCase("null"))
     { 
       rAddress = null;
       rAddress = db.getRecord( rOrder[7], "Address" );
     }  
     else
       rAddress = new String[17];

     htag.add(str[6]);
     htag.add("<b>Shipment Address :</b>");
     htag.add("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"80%\" >");
     htag.add("<tr><td width=\"30%\">&nbsp;Name </td><td>" + db.getName( nUserID, "User") +"</td></tr>");
     htag.add("<tr><td>&nbsp;Address  </td><td>"+rAddress[2]+"</td></tr>");
     htag.add("<tr><td>&nbsp;City </td><td>"+rAddress[6]+"</td></tr>");
     htag.add("<tr><td>&nbsp;State </td><td>"+rAddress[7]+"</td></tr>");
     htag.add("<tr><td>&nbsp;Pin Code </td><td>"+rAddress[8]+"</td></tr>");
     htag.add("<tr><td>&nbsp;Phone No </td><td>"+rAddress[9]+"</td></tr>");
     htag.add("<tr><td>&nbsp;Fax No  </td><td>"+rAddress[10]+"</td></tr>");
     htag.add("</table>");
     htag.add(tag);
     htag.add("</td></tr>");
     htag.add("</table>");

     Page page = new Page();
     Head head = new Head();

      Script scr1 = new Script( "JavaScript", null );
      HtmlTag scrdata1 = new HtmlTag();
      scrdata1.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr1.add(scrdata1);
      head.add(scr1);

     Script scr = new Script( "JavaScript", null );
     HtmlTag scrdata = new HtmlTag();
     scrdata.add("<!-- Start Hidding" + "\n");

     scrdata.add(" function callForm(flag){       \n"+
                 "  var url = \"/JOrder/servlets/NewAddr?nFlag=\"+flag \n"+
                 "  var AddrWindow = window.open(url,\"AddrWindow\",\"menubar=0,scrollbars=1,resizable=0,width=600,height=400\") \n"+
                 "} \n"+

                 "function callTable(flag) {\n"+
                 "  var url = \"/JOrder/servlets/AddrTable?nFlag=\"+flag \n"+
                 "  var AddrWindow = window.open(url,\"AddrWindow\",\"menubar=0,scrollbars=1,resizable=0,width=500,height=300\") \n"+
                 "} \n"+

                 " function call_form( oid, id ) { \n"+
                 "  with( document.forms[0] ) {   \n"+
                 "    parent.location.href = \"/JOrder/servlets/OrdManFrame?pvMode=U&pnOrderID=\"+oid+\"&pnOrderDtlsID=\"+id+\"&vTemplName=\"+vTemplName.value+\"&Browser=\"+Browser.value\n"+
                 "  }                    \n"+
                 " }                     \n");

     scrdata.add("// End Hidding -->");
     scr.add(scrdata);
     
     head.add(scr);

     Body body = new Body("/ordimg/BACKGR2.GIF",null); 
     Form form = new Form("/JOrder/servlets/OrderUpdEntry", "POST", "_parent", null, null);
     form.add( new FormHidden("pnTransID", nTransID, null ) );
     form.add( new FormHidden("pnOrderID", pnOrderID, null ) );
     form.add( new FormHidden("vTemplName", vTemplName, null ) );
     form.add( new FormHidden("Browser", Browser, null ) );
     form.add( new FormHidden( "billaddr_id", rOrder[6], null ) );
     form.add( new FormHidden( "shipaddr_id", rOrder[7], null ) );
     form.add( new FormHidden( "nFkClientID", rOrder[4], null ) );
     form.add( new FormHidden("vAction", "", null ) );

     HtmlTag ordnr = new HtmlTag();
     HtmlTag orddt = new HtmlTag();

     ordnr.add( util.createLabelItem( rOrder[1], vLabelAttrib ) );
     ordnr.add( new FormHidden( "vOrder_No", rOrder[1], null) );

     IngDate dt = new IngDate();
     orddt.add( util.createLabelItem( IngDate.dateToStr(rOrder[2]),vLabelAttrib ));
     orddt.add( new FormHidden("dOrder_Dt", IngDate.dateToStr(rOrder[2]), null ) );

     vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDER_NR", "Order No" );
     vBPLate1 = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDER_DT", "Order Date" );

     if(templname.equalsIgnoreCase("OrdTempl3"))
     {
       form.add("<BR><BR><BR><BR><BR><BR>&nbsp;&nbsp;&nbsp;");
       form.add("<b><font color=\"#3333FF\">"+vBPLate+"</font></b>");
       form.add(ordnr);
       form.add( util.getBlankSpaces(10) );
       form.add("<b><font color=\"#3333FF\">"+vBPLate1+"</font></b>");
       form.add(orddt);
       form.add( util.getBlankSpaces(10) );
       vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_CLIENT_NAME", "Select Client" );
       form.add("<b><font color=\"#3333FF\">"+vBPLate+"</font></b>");
       form.add( util.createLabelItem( db.getName( rOrder[4], "Client"), vLabelAttrib ));
       form.add("<BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>");
     }
     else
     {
       form.add("<BR><BR>"); 
       form.add( util.getBlankSpaces(125) );
       form.add("<b><font color=\"#3333FF\">"+vBPLate+"</font></b>");
       form.add(ordnr);
       form.add("<BR>"+util.getBlankSpaces(125) );
       form.add("<b><font color=\"#3333FF\">"+vBPLate1+"</font></b>");
       form.add(orddt);
       form.add("<BR>"+util.getBlankSpaces(125) );
       vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_CLIENT_NAME", "Select Client" );
       form.add("<b><font color=\"#3333FF\">"+vBPLate+"</font></b>");
       form.add( util.createLabelItem( db.getName( rOrder[4], "Client"), vLabelAttrib ));
       form.add("<BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>");
     }

     form.add("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"80%\" > ");
     form.add("<tr><td> ");
     form.add("&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"button\" value=\"Select\" onClick=\"callTable("+bid+")\">&nbsp;<input type=\"button\" value=\"New\" onClick=\"callForm("+bid+")\"> ");
     form.add("</td><td> ");
     form.add("&nbsp;&nbsp;<input type=\"button\" value=\"Select\" onClick=\"callTable("+sid+")\">&nbsp;<input type=\"button\" value=\"New\" onClick=\"callForm("+sid+")\"> ");
     form.add("</td></tr> ");
     form.add("</table> ");
     form.add("<BR> ");
     form.add("<table  BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=3 WIDTH=\"70%\" > ");
     form.add("<TR> ");
     form.add("<TD width=\"10%\"><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Note</Font></b></TD> ");
     form.add("<TD><font face=\"Arial,Helvetica\" size=-1> ");
     form.add(note);
     form.add( new FormHidden( "notes", note, null ) );
     form.add("</Font></TD></TR>");
     form.add("</table> ");
     form.add("<BR> ");
     form.add("<hr ALIGN=LEFT SIZE=4 WIDTH=\"100%\"><br>");

     TableCol space = new TableCol( "&nbsp;",null, null, null,null);
     TableCol blank = new TableCol( null,null, null, null,null);

     Table tab = new Table("1","center","Border=\"0\" COLS=4 Width=\"90%\"");
     TableRow row = new TableRow("Left",null,null);
     vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_DM_ORDSTAT", "Order Status" );
     TableCol col = new TableCol( util.GetBoilerPlate( vImgOption, vImagePath+"OrdStat.gif", vBPLate, vLabelAttrib ), null, null, null, "width=\"10%\"");
     HtmlTag sel =  util.createList( nUserID, "TR_ORDER", "cDM_OrdStat", pvMode, vOrdStatQry, rOrder[5], null, vListAttrib);
     TableCol col1 = new TableCol( sel, null, null, null, "width=\"15%\"");
     vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_EXPDEL_DT", "Exp Del Date" );
     TableCol col2 = new TableCol( util.GetBoilerPlate( vImgOption, vImagePath+"expdeldt.gif", vBPLate, vLabelAttrib ), null, null, null, "width=\"13%\"" );
     TableCol col3 = new TableCol( util.createTextItem( nUserID, "TR_ORDER", "dExpDel_Dt", pvMode, "10", "10", IngDate.dateToStr(rOrder[13]), "onBlur=\"top.check_date(this)\"", vTextAttrib ), null, null, null, null );
     col3.add( util.createLabelItem( "DD.MM.YYYY", vLabelAttrib) );
     row.add(col);
     row.add(col1);
     row.add(col2);
     row.add(col3);
     tab.add(row);

     form.add( tab);
     form.add( new NL(1));
     form.add( new FormHidden("nDelete", "", "") );

     String pvWhereClause = "Where Fk_Order_ID = "+ pnOrderID + " ORDER BY Pos_Nr";
     String vCols = "Fk_Order_ID, OrderDtls_ID, Pos_Nr, Fk_Item_ID, Fk_ItemPack_ID";
     String vTitles = "Order ID, OrderDtls ID, Pos No, Item Name, ItemPack Name";
     Table tb = OrdManForm.createOrdDtlsManTable(vCols, pvWhereClause, vTitles);

     form.add( tb);

     form.add("<BR>");
     form.add("<hr ALIGN=LEFT SIZE=4 WIDTH=\"100%\"><br>");
     form.add("<table  BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=3 WIDTH=\"80%\" > ");
     form.add("<TR> ");
     form.add("<TD width=\"15%\" vAlign=\"Top\"><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Instructions</font></b></TD>");
     form.add("<TD><font face=\"Arial,Helvetica\" size=-1>");
     form.add(inst);
     form.add( new FormHidden( "Inst", inst, null ) );
     form.add("</Font></TD> ");
     form.add("</TR><TR>");
     form.add("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark1</Font></b></TD> ");
     form.add("<TD><font face=\"Arial,Helvetica\" size=-1>");
     form.add("<INPUT Type=\"Text\" name=\"remark1\" value=\""+rOrder[27]+"\" Size=50 MaxLength=100>");
     form.add("</Font></TD></TR><TR>");
     form.add("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark2</Font></b></TD>");
     form.add("<TD><font face=\"Arial,Helvetica\" size=-1>");
     form.add("<INPUT Type=\"Text\" name=\"remark2\" value=\""+rOrder[28]+"\" Size=50 MaxLength=100>");
     form.add("</Font></TD></TR><TR>");
     form.add("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark3</Font></b></TD>");
     form.add("<TD><font face=\"Arial,Helvetica\" size=-1>");
     form.add("<INPUT Type=\"Text\" name=\"remark3\" value=\""+rOrder[29]+"\" Size=50 MaxLength=100>");
     form.add("</Font></TD></TR>");
     form.add("</table>");
     
     body.add(htag);
     body.add(form);
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
       String vIDArray[]=null;
       String vStatus=null;
       String sTransID=null; 
       String nLangID=null;
       String query=null;

       nLangID  = Parse.GetValueFromString( vPID, "LangID" );
       String nUserID  = Parse.GetValueFromString( vPID, "UserID" );
       String nClientID   = Parse.GetValueFromString( vPID, "ClientID");
       String nEmpID   = Parse.GetValueFromString( vPID, "EmployeeID");
       String nRecSecID  = Parse.GetValueFromString( vPID, "RecSecID");

       DBConnect db = new DBConnect();
       Message msg = new Message();
       Valid val = new Valid();
       IngDate dt = new IngDate();
       Connection conn=null;
       PreparedStatement pstmt=null;
       Statement stmt=null;

       String usr = db.getName( nUserID, "User" );

       sTransID=request.getParameter("pnTransID");
       String vTemplName = request.getParameter("vTemplName");
       String Browser = request.getParameter("Browser");
       String pnOrderID = request.getParameter("pnOrderID");
       String vAction = request.getParameter("vAction");
       String nDelete[] = request.getParameterValues("nDelete");
       String vOrder_No = request.getParameter("vOrder_No");
       String dOrder_Dt = request.getParameter("dOrder_Dt");
       String nFkClientID = request.getParameter("nFkClientID");
       String cDM_OrdStat = request.getParameter("cDM_OrdStat");
       String dExpDel_Dt = request.getParameter("dExpDel_Dt");
       String billaddrid = request.getParameter("billaddr_id");
       String shipaddrid = request.getParameter("shipaddr_id");
       String remark1 = request.getParameter("remark1");
       String remark2 = request.getParameter("remark2");
       String remark3 = request.getParameter("remark3");

       if( !( nFkClientID == null || nFkClientID.equals("") || nFkClientID.equalsIgnoreCase("null") ) )
          nClientID = nFkClientID ;

       vStatus = Trans.checkTransValidity( sTransID );

       if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
       {
         if( vAction!= null && vAction.equalsIgnoreCase("Delete") )          
         {
           vIDArray=Parse.parse(nDelete[0],"~");
           try
           {    
             conn = db.GetDBConnection();
             pstmt = conn.prepareStatement("DELETE FROM T_OrderDtls WHERE OrderDtls_Id=?"); 
             for(int i=0;i<vIDArray.length;i++)
             {  
               int bid = Integer.parseInt(vIDArray[i]);  
               pstmt.setInt(1,bid);
               pstmt.executeUpdate();         
             }
             nMsgID = 4;
           }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=7;}
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
           Trans.setTransID( sTransID );
         }
         else if( vAction!= null && vAction.equalsIgnoreCase("Update") )
         {
           try
           {    
             conn = db.GetDBConnection();
             stmt = conn.createStatement();
             query = " Update T_Order "+
                     " Set Order_Nr = '"+vOrder_No+"',"+
                     "     Order_Dt = '"+IngDate.strToDate( dOrder_Dt )+"',"+
                     "     Fk_Employee_ID = "+nEmpID+","+
                     "     Fk_Client_ID = "+nClientID+","+
                     "     DM_OrdStat = '"+ cDM_OrdStat +"',"+ 
                     "     BillAdd_ID = "+val.IsNull(billaddrid)+","+
                     "     ShipAdd_ID = "+val.IsNull(shipaddrid)+","+
                     "     ExpDel_Dt = '"+IngDate.strToDate(dExpDel_Dt)+"',"+
                     "     Remarks1 = "+val.IsNull(remark1)+","+
                     "     Remarks2 = "+val.IsNull(remark2)+","+
                     "     Remarks3 = "+val.IsNull(remark3)+","+
                     "     Modifier = '"+ usr +"',"+
                     "     Change_Dt ='"+ dt +"',"+
                     "     Fk_RecSec_ID="+nRecSecID+
                     " Where Order_ID = "+ pnOrderID;
             stmt.executeUpdate(query);
             nMsgID = 5;
           }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=8;}
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
           Trans.setTransID( sTransID );
         }
       }  

       if( nMsgID <= 5 )
         response.sendRedirect("/JOrder/servlets/OrdManFrame?pvMode=D&pnOrderID="+pnOrderID+"&vTemplName="+vTemplName+"&Browser="+Browser+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
    }

}