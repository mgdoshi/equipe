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

public class OrderEntry extends HttpServlet
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
      String billid=null;
      String shipid=null;
      String templname=null; 
      String tname=null; 
      String cname=null; 
      String caddr=null; 
      String logoname=null;
      String remark1=null;
      String remark2=null;
      String remark3=null;
      String note=null;
      String inst=null;
      int dflag=0,sflag=0,bflag=0,nflag=0,iflag=0,rflag1=0,rflag2=0,rflag3=0;
      int nIndex = 0;
      int nPosCount = 0;
      Object avItemCapt[] = new Object[13];
      HtmlTag avItemTag[] = new HtmlTag[13];

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
      int pnOrdDtlsCount = Integer.parseInt( request.getParameter("pnOrdDtlsCount"));
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

      vAutoGen = Param.getParamValue("AUTOGEN_ORDER_NR" );
      if( vAutoGen.equalsIgnoreCase("ON") && ( pnOrderID==null || pnOrderID.equals("") || pnOrderID.equalsIgnoreCase("null") ) )
        vOrderNr = Param.getParamValue( "AUTO_ORDERNR_PREFIX" ) + "..." + Param.getParamValue( "AUTO_ORDERNR_SUFIX" );
	
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

      String vUnitTypeQry = " SELECT Attrib, Attrib_Desc "+
                            " FROM   T_Domain " +
                            " WHERE  Domain = 'UNITTYPE' "+
                            " AND    Fk_Lang_ID = " + nLangID +
                            " ORDER  BY Sequence_Nr";

      String vItemQry = " SELECT itm.Item_ID, itm.Item_Name || '-' || itm.Item_Desc "+
                        " FROM   T_Item itm, T_UserItem uit   "+
                        " WHERE  uit.Fk_Item_ID = itm.Item_ID "+
                        " AND    uit.Fk_User_ID = "+ nUserID +
                        " AND    itm.InActive = '1' "+
                        " ORDER BY itm.Item_Name ";
  
      String vItmPackQry = " SELECT ItemPack_ID, ItemPack_Name ||'-' || ItemPack_Desc "+
                           " FROM T_ItemPack ";

      if( nUserID.equalsIgnoreCase("0") )
         vClientQry = " SELECT Client_ID, Client_Name  "+
                      " FROM   T_Client                "+
                      " Order By Client_Name           ";      
      else 
         vClientQry = " SELECT cln.Client_ID, cln.Client_Name   "+
                      " FROM   T_Client cln, T_UserClient ucl   "+
                      " WHERE  ucl.Fk_Client_ID = cln.Client_ID "+
                      " AND    ucl.Fk_User_ID = " + nUserID +
                      " Order By cln.Client_Name";

   /* Note : Be careful while handelling the following array, any small mistake may 
            mislead in creating a dynamic custom field table. */

      avItemCapt[0]  = util.GetBoilerPlate( vImgOption, vImagePath+"num1.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_NUM1", "Cust Num1" ), vLabelAttrib );
      avItemCapt[1]  = util.GetBoilerPlate( vImgOption, vImagePath+"num2.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_NUM2", "Cust Num2" ), vLabelAttrib );
      avItemCapt[2]  = util.GetBoilerPlate( vImgOption, vImagePath+"num3.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_NUM3", "Cust Num3" ), vLabelAttrib );
      avItemCapt[3]  = util.GetBoilerPlate( vImgOption, vImagePath+"num4.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_NUM4", "Cust Num4" ), vLabelAttrib );
      avItemCapt[4]  = util.GetBoilerPlate( vImgOption, vImagePath+"num5.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_NUM5", "Cust Num5" ), vLabelAttrib );
      avItemCapt[5]  = util.GetBoilerPlate( vImgOption, vImagePath+"text1.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_TEXT1", "Cust Text1" ), vLabelAttrib );
      avItemCapt[6]  = util.GetBoilerPlate( vImgOption, vImagePath+"text2.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_TEXT2", "Cust Text2" ), vLabelAttrib );
      avItemCapt[7]  = util.GetBoilerPlate( vImgOption, vImagePath+"text3.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_TEXT3", "Cust Text3" ), vLabelAttrib );
      avItemCapt[8]  = util.GetBoilerPlate( vImgOption, vImagePath+"text4.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_TEXT4", "Cust Text4" ), vLabelAttrib );
      avItemCapt[9] = util.GetBoilerPlate( vImgOption, vImagePath+"text5.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_TEXT5", "Cust Text5" ), vLabelAttrib );
      avItemCapt[10] = util.GetBoilerPlate( vImgOption, vImagePath+"rem1.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_REM1", "Cust Remarks1" ), vLabelAttrib );
      avItemCapt[11] = util.GetBoilerPlate( vImgOption, vImagePath+"rem2.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_REM2", "Cust Remarks2" ), vLabelAttrib );
      avItemCapt[12] = util.GetBoilerPlate( vImgOption, vImagePath+"rem3.gif", cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_REM3", "Cust Remarks3" ), vLabelAttrib );
 
      avItemTag[0]  = util.createTextItem( nUserID, "TR_ORDDTLS", "nNum1", pvMode, "10", "40", null, "onBlur=\"top.check_num(this)\"", vTextAttrib );
      avItemTag[1]  = util.createTextItem( nUserID, "TR_ORDDTLS", "nNum2", pvMode, "10", "40", null, "onBlur=\"top.check_num(this)\"", vTextAttrib );
      avItemTag[2]  = util.createTextItem( nUserID, "TR_ORDDTLS", "nNum3", pvMode, "10", "40", null, "onBlur=\"top.check_num(this)\"", vTextAttrib );
      avItemTag[3]  = util.createTextItem( nUserID, "TR_ORDDTLS", "nNum4", pvMode, "10", "40", null, "onBlur=\"top.check_num(this)\"", vTextAttrib );
      avItemTag[4]  = util.createTextItem( nUserID, "TR_ORDDTLS", "nNum5", pvMode, "10", "40", null, "onBlur=\"top.check_num(this)\"", vTextAttrib );
      avItemTag[5]  = util.createTextItem( nUserID, "TR_ORDDTLS", "vText1", pvMode, "20", "100", null, null, vTextAttrib );
      avItemTag[6]  = util.createTextItem( nUserID, "TR_ORDDTLS", "vText2", pvMode, "20", "100", null, null, vTextAttrib );
      avItemTag[7]  = util.createTextItem( nUserID, "TR_ORDDTLS", "vText3", pvMode, "20", "100", null, null, vTextAttrib );
      avItemTag[8]  = util.createTextItem( nUserID, "TR_ORDDTLS", "vText4", pvMode, "20", "100", null, null, vTextAttrib );
      avItemTag[9]  = util.createTextItem( nUserID, "TR_ORDDTLS", "vText5", pvMode, "20", "100", null, null, vTextAttrib );
      avItemTag[10] = util.createTextItem( nUserID, "TR_ORDDTLS", "vRemarks1", pvMode, "30", "500", null, null, vTextAttrib );
      avItemTag[11] = util.createTextItem( nUserID, "TR_ORDDTLS", "vRemarks2", pvMode, "30", "500", null, null, vTextAttrib );
      avItemTag[12] = util.createTextItem( nUserID, "TR_ORDDTLS", "vRemarks3", pvMode, "30", "500", null, null, vTextAttrib );

      if(pnOrderID!=null && !pnOrderID.equals("") && !pnOrderID.equalsIgnoreCase("null"))
      { 
        rOrder = db.getRecord( pnOrderID, "Order" );
      }  
      else
        rOrder = new String[15];

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
          remark1  = rs.getString("text5");
          remark2  = rs.getString("text6");
          remark3  = rs.getString("text7");
          caddr    = rs.getString("text9");
          inst    = rs.getString("text10");
 
          dflag    = Integer.parseInt(rs.getString("flag1").trim());
          bflag    = Integer.parseInt(rs.getString("flag2").trim());
          sflag    = Integer.parseInt(rs.getString("flag3").trim());
          nflag    = Integer.parseInt(rs.getString("flag4").trim());
          iflag    = Integer.parseInt(rs.getString("flag5").trim());
          rflag1   = Integer.parseInt(rs.getString("flag6").trim());
          rflag2   = Integer.parseInt(rs.getString("flag7").trim());
          rflag3   = Integer.parseInt(rs.getString("flag8").trim());

          billid   = rs.getString("num1");
          shipid   = rs.getString("num2"); 
         
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

	 if( billid!=null && !billid.equals("") && !billid.equalsIgnoreCase("null"))
     { 
       rAddress = db.getRecord( billid, "Address" );
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

     if( shipid!=null && !shipid.equals("") && !shipid.equalsIgnoreCase("null"))
     { 
       rAddress = null;
       rAddress = db.getRecord( shipid, "Address" );
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
                 "} \n");

     scrdata.add("// End Hidding -->");
     scr.add(scrdata);
     
     head.add(scr);
     Body body = new Body("/ordimg/BACKGR2.GIF",null); 
     Form form = new Form("/JOrder/servlets/OrderEntry", "POST", "_parent", null, null);
     form.add( new FormHidden("pnTransID", nTransID, null ) );
     form.add( new FormHidden("pnOrderID", pnOrderID, null ) );
     form.add( new FormHidden("vTemplName", vTemplName, null ) );
     form.add( new FormHidden("Browser", Browser, null ) );
     form.add( new FormHidden("vAction", "", null ) );

     if( vOrderNr==null || vOrderNr.equals("") || vOrderNr.equalsIgnoreCase("null") )
       vOrderNr = rOrder[1];

     HtmlTag ordnr = new HtmlTag();
     HtmlTag orddt = new HtmlTag();
     HtmlTag clnname = new HtmlTag();
     if( vAutoGen.equalsIgnoreCase("ON") )
     {
       ordnr.add( util.createLabelItem( vOrderNr, vLabelAttrib ) );
       ordnr.add( new FormHidden( "vOrder_No", vOrderNr, null) );
     }
     else
     {
       ordnr.add( util.createTextItem( nUserID, "TR_ORDER", "vOrder_No", pvMode, "10", "40", rOrder[1], null, vTextAttrib ));
     }

     IngDate dt = new IngDate();
     if( dflag == 0 )
     {
       if( pvMode.equalsIgnoreCase("I") )
       {
         orddt.add( util.createLabelItem( IngDate.dateToStr(dt),vLabelAttrib ) );
         orddt.add( new FormHidden("dOrder_Dt", IngDate.dateToStr(dt), null ) );
       } 
       else if( pvMode.equalsIgnoreCase("U") )
       {
         orddt.add( util.createLabelItem( rOrder[2],vLabelAttrib ));
         orddt.add( new FormHidden("dOrder_Dt", rOrder[2], null ) );
       }
     }
     else
     {
       if( rOrder[2]==null || rOrder[3].equals("") || rOrder[3].equals("null") )
         rOrder[2] = IngDate.dateToStr(dt); 
       orddt.add( util.createTextItem( nUserID, "TR_ORDER", "dOrder_Dt", pvMode, "10", "10", rOrder[2], "onBlur=\"top.check_date(document.forms[0].dOrder_Dt)\"", vTextAttrib ));
     }

     if( db.getName( nClientID, "Client" ).equalsIgnoreCase("INTERNAL") )
     {
       clnname.add( util.createList( nUserID, "TR_ORDER", "nFkClientID", "I", vClientQry, null, null, vListAttrib ) );
     }
     else
       clnname.add( new FormHidden( "nFkClientID", null, null) );

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
       form.add(clnname);
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
       form.add(clnname);
       form.add("<BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>");
     }

     form.add("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"80%\" > ");
     form.add("<tr><td> ");
     if( bflag == 1) 
       form.add("&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"button\" value=\"Select\" onClick=\"callTable("+bid+")\">&nbsp;<input type=\"button\" value=\"New\" onClick=\"callForm("+bid+")\"> ");
     form.add("</td><td> ");
     if( sflag == 1) 
       form.add("&nbsp;&nbsp;<input type=\"button\" value=\"Select\" onClick=\"callTable("+sid+")\">&nbsp;<input type=\"button\" value=\"New\" onClick=\"callForm("+sid+")\"> ");
     form.add("</td></tr> ");
     form.add("</table> ");
     form.add("<BR> ");
     form.add("<table  BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=3 WIDTH=\"70%\" > ");
     form.add("<TR> ");
     form.add("<TD width=\"10%\"><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Note</Font></b></TD> ");
     form.add("<TD><font face=\"Arial,Helvetica\" size=-1> ");
     if( nflag == 1 ) 
       form.add("<INPUT Type=\"Text\" name=\"notes\" value=\""+note+"\" Size=50 MaxLength=100>");
     else
     {
       form.add(note);
       form.add( new FormHidden( "notes", note, null ) );
     } 
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
     TableCol col1 = new TableCol( util.createLabelItem( Domain.getDomainDescFrmAttrib( "ORDSTAT", "A", nLangID ), vLabelAttrib ), null, null, null, "width=\"15%\"");
     col1.add( new FormHidden( "cDM_OrdStat", "A", null ) );
     vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_EXPDEL_DT", "Exp Del Date" );
     TableCol col2 = new TableCol( util.GetBoilerPlate( vImgOption, vImagePath+"expdeldt.gif", vBPLate, vLabelAttrib ), null, null, null, "width=\"13%\"" );
     TableCol col3 = new TableCol( util.createTextItem( nUserID, "TR_ORDER", "dExpDel_Dt", pvMode, "10", "10", null, "onBlur=\"top.check_date(this)\"", vTextAttrib ), null, null, null, null );
     col3.add( util.createLabelItem( "DD.MM.YYYY", vLabelAttrib) );
     row.add(col);
     row.add(col1);
     row.add(col2);
     row.add(col3);
     tab.add(row);

     Table tb = new Table("1","center","Border=\"0\" COLS=14");
  
     TableRow rw = new TableRow("Left",null,null);
     vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_POS_NR", "Pos No" );
     TableCol cl = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"posnr.gif", vBPLate, vLabelAttrib ),null, null, null,null);
     cl.add( WebUtil.NotNull ); 
     vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_FK_ITEM_ID", "Item" );
     TableCol cl1 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"item.gif", vBPLate, vLabelAttrib ),null, null, null,null);
     cl1.add( WebUtil.NotNull );
     vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_FK_ITEMPACK_ID", "Item Pack" );
     TableCol cl2 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"itmpak.gif", vBPLate, vLabelAttrib ),null, null, null,null);
     cl2.add( WebUtil.NotNull );
     vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_UNITPRICE", "Unit Price" );
     TableCol cl3 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unitprice.gif", vBPLate, vLabelAttrib ),null, null, null,null);
     cl3.add( WebUtil.NotNull );
     rw.add( cl );
     rw.add( cl1 );
     rw.add( cl2 );
     rw.add( cl3 );
     for( int i=0; i<10; i++ )
        rw.add(blank); 
     tb.add(rw);

     TableRow rw1 = new TableRow("Left",null,null);
     rw1.add(space);
     vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_QUANTITY", "Quantity" );
     TableCol cl4 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"quant.gif", vBPLate, vLabelAttrib ),null, null, null,null);
     cl4.add( WebUtil.NotNull ); 
     vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDERDTLS_DM_UNITTYPE", "Unit Type" );
     TableCol cl5 = new TableCol(util.GetBoilerPlate(vImgOption, vImagePath+"unittype.gif", vBPLate, vLabelAttrib ),null, null, null,null);
     cl5.add( WebUtil.NotNull );
     rw1.add( cl4 );
     rw1.add( cl5 );
     for( int i=0; i<11; i++ )
        rw1.add(blank); 
     tb.add(rw1);

     TableRow rw2 = new TableRow("Left",null,null);
     rw2.add(space);
     for( int i=0; i<13; i++)
     {
       if( (avItemTag[i].toString()).indexOf( "<FONT" )  >= 0 )
       {
          TableCol cl6 = new TableCol( avItemCapt[i], null, null, null,null);
          rw2.add( cl6 );
          nIndex = nIndex + 1;
       }
     }
     for( int i=0; i< 13-nIndex; i++ )
       rw2.add( blank );
     tb.add(rw2);
     
	 TableCol cl7 = new TableCol( util.createTextItem( nUserID, "TR_ORDDTLS", "nPos_No", pvMode, "5", "20", null, "onBlur=\"top.check_num(this)\"", vTextAttrib ), null, null, null, null );
     TableCol cl8 = new TableCol( util.createTextItem( nUserID, "TR_ORDDTLS", "nUnitPrice", pvMode, "10", "40", null, "onBlur=\"top.check_num(this)\"", vTextAttrib ), null, null, null, null );
     TableCol cl9 = new TableCol( util.createTextItem( nUserID, "TR_ORDDTLS", "nQuantity", pvMode, "10", "40", null, "onBlur=\"top.check_num(this)\"", vTextAttrib ), null, null, null, null );
     HtmlTag sel1 =  util.createList( nUserID, "TR_ORDDTLS", "nFk_ItemPack_ID", pvMode, vItmPackQry, null, null, vListAttrib);

     for( int j = 0; j< pnOrdDtlsCount+5; j++ )
     {
       TableRow rw3 = new TableRow("Left",null,null);
       rw3.add( cl7 );
	   HtmlTag sel =  util.createList( nUserID, "TR_ORDDTLS", "nFk_Item_ID", pvMode, vItemQry, null, "onChange=\"parent.show_UnitType( this.options[this.selectedIndex].value, '"+nPosCount+"' )\"", vListAttrib);
       rw3.add( new TableCol( sel, null, null, null, null ) );
       rw3.add( new TableCol( sel1, null, null, null, null ) );
       rw3.add( cl8 );
       for( int i=0; i<10; i++ )
          rw3.add(blank); 
       tb.add(rw3);

       TableRow rw4 = new TableRow("Left",null,null);
       rw4.add(space);
       rw4.add( cl9 );
       TableCol cl10 = new TableCol( util.createTextItem( nUserID, "TR_ORDDTLS", "vUnitType", pvMode, "10", "40", null, "onFocus=\"document.forms[0].nNum1["+nPosCount+"].focus()\"", vTextAttrib ), null, null, null, null );
       cl10.add( new FormHidden("cDM_UnitType", null, null ) );
       rw4.add( cl10 );
       for( int i=0; i<11; i++ )
         rw4.add(blank); 
       tb.add(rw4);
 
       TableRow rw5 = new TableRow("Left",null,null);
       rw5.add(space);
       nIndex = 0;
       for( int i=0; i<13; i++)
       {
         if( (avItemTag[i].toString()).indexOf( "<FONT" )  >= 0 )
         {
            TableCol cl11 = new TableCol( avItemTag[i], null, null, null,null);
            rw5.add( cl11 );
            nIndex = nIndex + 1;
         }
         else 
            rw5.add( avItemTag[i] );
       }
       for( int i=0; i< 13-nIndex; i++ )
         rw5.add( blank );
       tb.add(rw5);

       TableRow rw6 = new TableRow("Left",null,null);
       rw6.add(space);
       tb.add(rw6);

       nPosCount = nPosCount + 1;
     }    
       
     tb.add( new FormHidden( "nPos_No", null, null ) );
     tb.add( new FormHidden( "nFk_Item_ID", null, null ) );
     tb.add( new FormHidden( "nFk_ItemPack_ID", null, null ) );
     tb.add( new FormHidden( "nQuantity", null, null ) );
     tb.add( new FormHidden( "vUnitType", null, null ) );
     tb.add( new FormHidden( "cDM_UnitType", null, null ) );
     tb.add( new FormHidden( "nUnitPrice", null, null ) );
     tb.add( new FormHidden( "vText1", null, null ) );
     tb.add( new FormHidden( "vText2", null, null ) );
     tb.add( new FormHidden( "vText3", null, null ) );
     tb.add( new FormHidden( "vText4", null, null ) );
     tb.add( new FormHidden( "vText5", null, null ) );
     tb.add( new FormHidden( "nNum1", null, null ) );
     tb.add( new FormHidden( "nNum2", null, null ) );
     tb.add( new FormHidden( "nNum3", null, null ) );
     tb.add( new FormHidden( "nNum4", null, null ) );
     tb.add( new FormHidden( "nNum5", null, null ) );
     tb.add( new FormHidden( "vRemarks1", null, null ) );
     tb.add( new FormHidden( "vRemarks2", null, null ) );
     tb.add( new FormHidden( "vRemarks3", null, null ) );
     tb.add( new FormHidden( "billaddr_id", billid, null ) );
     tb.add( new FormHidden( "shipaddr_id", shipid, null ) );
     tb.add( new FormHidden( "pnOrdDtlsCount", Integer.toString(pnOrdDtlsCount+5), null ) );

     form.add(tab);
     form.add(tb);

     form.add("<BR>");
     form.add("<hr ALIGN=LEFT SIZE=4 WIDTH=\"100%\"><br>");
     form.add("<table  BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=3 WIDTH=\"80%\" > ");
     form.add("<TR> ");
     form.add("<TD width=\"15%\" vAlign=\"Top\"><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Instructions</font></b></TD><TD>");
     if( iflag == 1)
       form.add("<TextArea Name=Inst Rows=3 Cols=50>"+inst+"</TextArea></TD> ");
     else
     {
       form.add(inst);
       form.add( new FormHidden( "Inst", inst, null ) );
     }
     form.add("</TR><TR>");
     form.add("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark1</Font></b></TD> ");
     form.add("<TD><font face=\"Arial,Helvetica\" size=-1>");
     if( rflag1 == 1) 
        form.add("<INPUT Type=\"Text\" name=\"remark1\" value=\""+remark1+"\" Size=50 MaxLength=100>");
     else
     {
        form.add(remark1);
        form.add( new FormHidden( "remark1", remark1, null ) );
     }
     form.add("</Font></TD> ");
     form.add("</TR>");
     form.add("<TR>");
     form.add("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark2</Font></b></TD>");
     form.add("<TD><font face=\"Arial,Helvetica\" size=-1>");
     if( rflag2 == 1 )  
        form.add("<INPUT Type=\"Text\" name=\"remark2\" value=\""+remark2+"\" Size=50 MaxLength=100>");
     else
     {
        form.add(remark2);
        form.add( new FormHidden( "remark2", remark2, null ) );
     }
     form.add("</Font></TD></TR><TR>");
     form.add("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark3</Font></b></TD>");
     form.add("<TD><font face=\"Arial,Helvetica\" size=-1>");
     if( rflag3 == 1 )
        form.add("<INPUT Type=\"Text\" name=\"remark3\" value=\""+remark3+"\" Size=50 MaxLength=100>");
     else
     {
        form.add(remark3);
        form.add( new FormHidden( "remark3", remark3, null ) );
     }
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
      String query = null;
	  String vStatus = null;
      String nLangID   = Parse.GetValueFromString( vPID, "LangID");
      String nUserID   = Parse.GetValueFromString( vPID, "UserID");
      String nClientID   = Parse.GetValueFromString( vPID, "ClientID");
	  String nEmpID   = Parse.GetValueFromString( vPID, "EmployeeID");
 	  String nRecSecID  = Parse.GetValueFromString( vPID, "RecSecID");

      String vTemplName = request.getParameter("vTemplName");
      String Browser = request.getParameter("Browser");
      String pnTransID = request.getParameter("pnTransID");
      String pnOrderID = request.getParameter("pnOrderID");
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
      String vAction = request.getParameter("vAction");
      String nPos_No[] = request.getParameterValues("nPos_No");
      String nFk_ItemPack_ID[] = request.getParameterValues("nFk_ItemPack_ID");
      String nFk_Item_ID[] = request.getParameterValues("nFk_Item_ID");
      String nQuantity[] = request.getParameterValues("nQuantity");
      String vUnitType[] = request.getParameterValues("vUnitType");
      String cDM_UnitType[] = request.getParameterValues("cDM_UnitType");
      String nUnitPrice[] = request.getParameterValues("nUnitPrice");
      String vText1[] = request.getParameterValues("vText1");
      String vText2[] = request.getParameterValues("vText2");
      String vText3[] = request.getParameterValues("vText3");
      String vText4[] = request.getParameterValues("vText4");
      String vText5[] = request.getParameterValues("vText5");
      String nNum1[] = request.getParameterValues("nNum1");
      String nNum2[] = request.getParameterValues("nNum2");
      String nNum3[] = request.getParameterValues("nNum3");
      String nNum4[] = request.getParameterValues("nNum4");
      String nNum5[] = request.getParameterValues("nNum5");
      String vRemarks1[] = request.getParameterValues("vRemarks1");
      String vRemarks2[] = request.getParameterValues("vRemarks2");
      String vRemarks3[] = request.getParameterValues("vRemarks3");
      int pnOrdDtlsCount = Integer.parseInt( request.getParameter("pnOrdDtlsCount"));

      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      DBConnect db = new DBConnect();
      ConfigData cdata = new ConfigData();
      Valid val = new Valid();
      Message msg = new Message(); 
      IngDate dt = new IngDate();
      int order_id = -1;

      if( !( nFkClientID == null || nFkClientID.equals("") || nFkClientID.equalsIgnoreCase("null") ) )
         nClientID = nFkClientID ;

      String usr = db.getName( nUserID, "User" );
      vStatus    = Trans.checkTransValidity( pnTransID );

      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
        if( vAction.equalsIgnoreCase("Insert"))
        {
          try
          {
            conn = db.GetDBConnection();
            stmt = conn.createStatement();

            order_id = Integer.parseInt(db.getNextVal("S_Order"));
            String ordnr = Parse.replaceStr( vOrder_No, "...", Integer.toString(order_id) );
            query = "INSERT INTO T_Order( Order_ID, Order_Nr, Order_Dt, Fk_Employee_ID, "+
                    "            Fk_Client_ID, DM_OrdStat, BillAdd_ID, ShipAdd_ID, ExpDel_Dt, "+
                    "            Remarks1, Remarks2, Remarks3, "+
                    "            Modifier, Change_Dt, Fk_RecSec_ID ) "+
                    "VALUES( "+ order_id +", "+ val.IsNull( ordnr ) +", '"+ IngDate.strToDate( dOrder_Dt ) +"', "+ nEmpID +", "+
                    " "+ nClientID +", '"+ cDM_OrdStat +"', "+ val.IsNull(billaddrid) +", "+ val.IsNull(shipaddrid) +", "+
                    " '"+ IngDate.strToDate(dExpDel_Dt) +"', "+ val.IsNull(remark1) +", "+
		            " "+ val.IsNull(remark2) +", "+ val.IsNull(remark3) +", "+
                    "'"+ usr +"','"+ dt +"',"+ nRecSecID+ ")";
            stmt.executeUpdate(query);

            for( int i = 0; i< pnOrdDtlsCount; i++ )
			{
			  if( !( nFk_Item_ID[i]==null || nFk_Item_ID[i].equals("") || nFk_Item_ID[i].equalsIgnoreCase("null") ) )
			  {
                int orderdtls_id = Integer.parseInt(db.getNextVal("S_OrderDtls"));
                query = "	INSERT INTO T_OrderDtls( OrderDtls_ID, Fk_Order_ID, Pos_Nr, "+
                        "              Fk_ItemPack_ID, Fk_Item_ID, Quantity, DM_UnitType, "+
                        "              UnitPrice, Text1, Text2, Text3, Text4, Text5, "+
                        "              Num1, Num2, Num3, Num4, Num5, Remarks1, Remarks2, "+
                        "                 Remarks3, Modifier, Change_Dt ) "+
                        "VALUES( "+ orderdtls_id+ ", "+ order_id +", " + nPos_No[i] +", "+
                        " "+ nFk_ItemPack_ID[i] + ", "+ nFk_Item_ID[i] +", "+ nQuantity[i] +", "+
				  	    " '"+ cDM_UnitType[i] + "', "+ nUnitPrice[i] + ", "+ val.IsNull(vText1[i]) +", "+
     				    " "+ val.IsNull(vText2[i]) + ", "+ val.IsNull(vText3[i]) +", "+ val.IsNull(vText4[i]) +", "+
					    " "+ val.IsNull(vText5[i]) +", "+ val.IsNull(nNum1[i])  + ", "+ val.IsNull(nNum2[i]) +", "+
					    " "+ val.IsNull(nNum3[i]) +", "+ val.IsNull(nNum4[i]) +", "+ val.IsNull(nNum5[i]) +", "+
					    " "+ val.IsNull(vRemarks1[i]) +", "+ val.IsNull(vRemarks2[i]) +", "+
                        " "+ val.IsNull(vRemarks3[i]) +",'"+ usr + "','"+ dt +"' )";
                 stmt.executeUpdate(query);
              }
            }
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
            }catch(SQLException sexe){errMsg=sexe.getMessage();nMsgID=6;}
           }
           Trans.setTransID(pnTransID);
        }
     }

     if(nMsgID <= 5 )
       response.sendRedirect("/JOrder/servlets/OrderFrame?pvMode=P&pnOrderID="+order_id+"&vTemplName="+vTemplName+"&Browser="+Browser+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
     else
       out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }

}