import ingen.html.*;
import ingen.html.frame.*;
import ingen.html.para.*;
import ingen.html.character.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChnOrdList extends HttpServlet
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
      Vector rOrdID = new Vector();
      Vector rOrdNr = new Vector();
      Vector rOrdDt = new Vector();
      Vector rOrdStat = new Vector();
      Vector rOrdBillID = new Vector();
      Vector rOrdShipID = new Vector();
      Vector rOrdRem1 = new Vector();
      Vector rOrdRem2 = new Vector();
      Vector rOrdRem3 = new Vector();
      Vector rOrdDtls = new Vector();
      Vector rOrdExpDelDt = new Vector();
      String rAddress[] = null;
      String vIDArray[]=null;
      String vImagePath=null;
      String vBPLate=null;
      String vBPLate1=null;
      String vLabelAttrib=null;
      String vListAttrib=null;
      String vFormType=null;
      String vAutoGen=null;
      String templname=null;
      String tname=null; 
      String cname=null; 
      String caddr=null; 
      String logoname=null;
      String note=null;
      String inst=null;
      String query = null;

      DBConnect db = new DBConnect();
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      String str[]= new String[7];
      Rectangle rect[] =  new Rectangle[7]; 
      String tag = null;
      int bid = -1;
      int sid = -1;

      String vTemplName = request.getParameter("vTemplName");
      String vFrom_Order_ID = request.getParameter("vFrom_Order_ID");
      String vTo_Order_ID = request.getParameter("vTo_Order_ID");
      String nPrint = request.getParameter("nPrint");

      ConfigData cdata = new ConfigData();
      ConfigData cdata1 = new ConfigData();
      WebUtil util = new WebUtil();

      nAuditID  = Parse.GetValueFromString( vPID, "AuditID" );
      nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      nLangID   = Parse.GetValueFromString( vPID, "LangID" );
      nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );

      vFormType = cdata.GetConfigValue( "TR_ORDER", nLangID, "WD_FORM_LIST", "Order List " );
      vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

      String  vOrdStatQry = " SELECT Attrib, Attrib_Desc FROM T_Domain "+
                            " WHERE Domain = 'ORDSTAT' "+
                            " AND Fk_Lang_ID = "+ nLangID +
                            " ORDER BY Sequence_NR";
      try
      {
        conn =  db.GetDBConnection();
        stmt = conn.createStatement();
        query = " SELECT * from T_CLSTEMPL "+
                " WHERE Alias_Name = '"+ vTemplName +"'"+
                " AND Ref_ID = "+nUserID;
        rs = stmt.executeQuery(query);
        if(rs.next())
        {
          templname = rs.getString("templ_name");
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
          if(rs!=null)
            rs.close();
          if(stmt!=null)
            stmt.close();
          if(conn!=null)
            conn.close();
        }catch(SQLException sexe){System.out.println(sexe.getMessage());}
      } 

     try
     {
        conn =  db.GetDBConnection();
        stmt = conn.createStatement();
        vIDArray=Parse.parse(nPrint,"~");
        for(int i=0;i<vIDArray.length;i++)
        {  
          query = "Select * from t_Order Where Order_ID=" +vIDArray[i];
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            rOrdID.addElement(rs.getString(1));
            rOrdNr.addElement(rs.getString(2));
            rOrdDt.addElement(rs.getString(3));
   	    rOrdStat.addElement(rs.getString(6));
  	    rOrdBillID.addElement(rs.getString(7));
   	    rOrdShipID.addElement(rs.getString(8));
   	    rOrdExpDelDt.addElement(rs.getString(14));
   	    rOrdRem1.addElement(rs.getString(28));
   	    rOrdRem2.addElement(rs.getString(29));
   	    rOrdRem3.addElement(rs.getString(30));
          }               
        }
      }catch(SQLException sexe){System.out.println(sexe.getMessage());}
      finally 
      {
        try
        {
          if(rs!=null)
            rs.close();
          if(stmt!=null)
            stmt.close();
          if(conn!=null)
            conn.close();
        }catch(SQLException sexe){System.out.println(sexe.getMessage());}
      }

      Page page = new Page();
      Head head = new Head();
      Script scr = new Script( "JavaScript", null );
      HtmlTag scrdata = new HtmlTag();
      scrdata.add("<!-- Start Hidding" + "\n" +
                  "top.show_form(\""+vFormType+"\")" + "\n" +
                  "// End Hidding -->");
      scr.add(scrdata);
      head.add(scr);
      nTransID = Trans.getTransID( nAuditID, 'M');
      Body body = new Body("/ordimg/BACKGR2.GIF",null);
      Form form = new Form("ChnOrdList", "Post", "_parent",null,null);
      form.add(new FormHidden("rOrdNr",null, null )); 
      form.add(new FormHidden("cDM_OrdStat",null, null )); 
      form.add(new FormHidden("pnTransID",nTransID,null));
      form.add(new FormHidden("vTo_Order_ID",vTo_Order_ID, null )); 
      form.add(new FormHidden("vFrom_Order_ID",vFrom_Order_ID,null));
      form.add(new FormHidden("vTemplName",vTemplName, null ));  
      form.add(new FormHidden("nPrint",nPrint, null ));  

      for( int i=0; i<rOrdNr.size(); i++ )
      {
         HtmlTag htag = new HtmlTag();
         htag.add("<b><font face=\"Arial,Helvetica\" color=\"#3333FF\" size=+2>");
         htag.add("<CENTER>"+tname+"</font></b></CENTER>");
         htag.add("<BR>");
         vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDER_NR", "Order No" );
         vBPLate1 = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_ORDER_DT", "Order Date" );

         if( templname.equalsIgnoreCase("OrdTempl1") )
         {
  	    htag.add("<table Align=\"centre\" BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"90%\" >");
    	    htag.add("<tr align=\"left\"><td width=\"25%\" align=\"center\">");
	    htag.add("<img SRC=\"/ordimg/"+logoname+"\" border=\"0\" height=75 width=75 align=ABSCENTER>");
	    htag.add("</td><td width=\"45%\">");
            htag.add("<font face=\"Arial,Helvetica\" color=\"#3333FF\" size=+2> ");
            htag.add(cname);
            htag.add("</font> ");
            htag.add("<BR><font face=\"Arial,Helvetica\" color=\"#000000\" size=-1> ");
            htag.add(caddr);
            htag.add("</font>");
	    htag.add("</td><td>");
            htag.add("<b><font color=\"#3333FF\" face=\"Arial\" >"+vBPLate+"</font></b>");
            htag.add( util.createLabelItem( rOrdNr.elementAt(i).toString(), vLabelAttrib ) );
            htag.add( new FormHidden("rOrdNr",rOrdNr.elementAt(i).toString(), null ) );
            htag.add("<BR><b><font color=\"#3333FF\" face=\"Arial\" >"+vBPLate1+"</font></b>");
            htag.add( util.createLabelItem( IngDate.dateToStr(rOrdDt.elementAt(i).toString()), vLabelAttrib ) );
	    htag.add("</td></tr></table><BR>");
         }
	 else if( templname.equalsIgnoreCase("OrdTempl2") )
	 {
   	    htag.add("<table Align=\"center\" BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"90%\" >");
	    htag.add("<tr align=\"left\"><td width=\"45%\">");
            htag.add("<font face=\"Arial,Helvetica\" color=\"#3333FF\" size=+2> ");
            htag.add(cname);
            htag.add("</font> ");
            htag.add("<BR><font face=\"Arial,Helvetica\" color=\"#000000\" size=-1> ");
            htag.add(caddr);
            htag.add("</font>");
   	    htag.add("</td><td width=\"25%\" align=\"center\">");
    	    htag.add("<img SRC=\"/ordimg/"+logoname+"\" border=\"0\" height=75 width=75 align=ABSCENTER>");
  	    htag.add("</td><td>");
            htag.add("<b><font color=\"#3333FF\" face=\"Arial\" >"+vBPLate+"</font></b>");
            htag.add( util.createLabelItem( rOrdNr.elementAt(i).toString(), vLabelAttrib ) );
            htag.add( new FormHidden("rOrdNr",rOrdNr.elementAt(i).toString(), null ) );
            htag.add("<BR><b><font color=\"#3333FF\" face=\"Arial\" >"+vBPLate1+"</font></b>");
            htag.add( util.createLabelItem( IngDate.dateToStr(rOrdDt.elementAt(i).toString()), vLabelAttrib ) );
	    htag.add("</td></tr></table><BR>");
       }
       else if( templname.equalsIgnoreCase("OrdTempl3") )
       {
   	  htag.add("<table Align=\"center\" BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"90%\" >");
 	  htag.add("<tr align=\"left\"><td width=\"70%\">");
          htag.add("<font face=\"Arial,Helvetica\" color=\"#3333FF\" size=+2>");
          htag.add(cname);
          htag.add("</font> ");
          htag.add("<BR><font face=\"Arial,Helvetica\" color=\"#000000\" size=-1>");
          htag.add(caddr);
          htag.add("</font>");
	  htag.add("</td><td align=\"center\">");
    	  htag.add("<img SRC=\"/ordimg/"+logoname+"\" border=\"0\" height=75 width=75 align=ABSCENTER>");
	  htag.add("</td></tr></table>");
	  htag.add("<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font color=\"#3333FF\" face=\"Arial\">"+vBPLate+"</font></b>&nbsp;&nbsp;&nbsp;");
          htag.add( util.createLabelItem( rOrdNr.elementAt(i).toString(), vLabelAttrib ) );
          htag.add( new FormHidden("rOrdNr",rOrdNr.elementAt(i).toString(), null ) );
          htag.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font color=\"#3333FF\" face=\"Arial\">"+vBPLate1+"</font></b>&nbsp;&nbsp;&nbsp;");
          htag.add( util.createLabelItem( IngDate.dateToStr(rOrdDt.elementAt(i).toString()), vLabelAttrib ) );
          htag.add("<BR><BR>");
       }

       if( rOrdBillID.elementAt(i)!=null )
       { 
         rAddress = db.getRecord( rOrdBillID.elementAt(i).toString(), "Address" );
       }  
       else
         rAddress = new String[17];

       htag.add("<table Align=\"center\" BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"90%\" >");
       htag.add("<tr><td width=\"45%\">");
       htag.add("<b>Billing Address :</b>");
       htag.add("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"100%\" >");
       htag.add("<tr><td width=\"30%\">&nbsp;Name </td><td>" + db.getName( nUserID, "User") +"</td></tr>");
       htag.add("<tr><td>&nbsp;Address  </td><td>"+rAddress[2]+"</td></tr>");
       htag.add("<tr><td>&nbsp;City </td><td>"+rAddress[6]+"</td></tr>");
       htag.add("<tr><td>&nbsp;State </td><td>"+rAddress[7]+"</td></tr>");
       htag.add("<tr><td>&nbsp;Pin Code </td><td>"+rAddress[8]+"</td></tr>");
       htag.add("<tr><td>&nbsp;Phone No </td><td>"+rAddress[9]+"</td></tr>");
       htag.add("<tr><td>&nbsp;Fax No  </td><td>"+rAddress[10]+"</td></tr>");
       htag.add("</table>");
       htag.add("</td><td>");

       if( rOrdShipID.elementAt(i)!=null)
       { 
         rAddress = null;
         rAddress = db.getRecord( rOrdShipID.elementAt(i).toString(), "Address" );
       }  
       else
         rAddress = new String[17];

       htag.add("<b>Shipment Address :</b>");
       htag.add("<table BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=2 WIDTH=\"100%\" >");
       htag.add("<tr><td width=\"30%\">&nbsp;Name </td><td>" + db.getName( nUserID, "User") +"</td></tr>");
       htag.add("<tr><td>&nbsp;Address  </td><td>"+rAddress[2]+"</td></tr>");
       htag.add("<tr><td>&nbsp;City </td><td>"+rAddress[6]+"</td></tr>");
       htag.add("<tr><td>&nbsp;State </td><td>"+rAddress[7]+"</td></tr>");
       htag.add("<tr><td>&nbsp;Pin Code </td><td>"+rAddress[8]+"</td></tr>");
       htag.add("<tr><td>&nbsp;Phone No </td><td>"+rAddress[9]+"</td></tr>");
       htag.add("<tr><td>&nbsp;Fax No  </td><td>"+rAddress[10]+"</td></tr>");
       htag.add("</table>");
       htag.add("</td></tr>");
       htag.add("</table><BR>");
       form.add(htag);
       form.add("<table align=\"center\" BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=3 WIDTH=\"90%\" > ");
       form.add("<TR>");
       form.add("<TD width=\"10%\"><b><font face=\"Arial,Helvetica\" size=-1>Note</Font></b></TD> ");
       form.add("<TD><font face=\"Arial,Helvetica\" size=-1> ");
       form.add( util.createLabelItem( note, vLabelAttrib ) );
       form.add("</Font></TD></TR>");
       form.add("</table> ");
       form.add("<BR> ");
       form.add("<hr ALIGN=LEFT SIZE=4 WIDTH=\"100%\"><br>");

       TableCol space = new TableCol( "&nbsp;",null, null, null,null);
       TableCol blank = new TableCol( null,null, null, null,null);

       Table tab = new Table("1","center","Border=\"0\" COLS=4 Width=\"90%\"");
       TableRow row = new TableRow("Left",null,null);
       vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_DM_ORDSTAT", "Order Status" );
       TableHeader col = new TableHeader( util.createLabelItem( vBPLate, vLabelAttrib ), null, null, null, "width=\"10%\"");
       TableCol col1 = new TableCol(util.createList( nUserID, "TR_ORDER","cDM_OrdStat","I", vOrdStatQry,rOrdStat.elementAt(i).toString(), null, vListAttrib), null, null, null, "width=\"15%\"");
       vBPLate = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_EXPDEL_DT", "Exp Del Date" );
       TableHeader col2 = new TableHeader( util.createLabelItem( vBPLate, vLabelAttrib ), null, null, null, "width=\"13%\"" );
       TableCol col3 = null;
       if(rOrdExpDelDt.elementAt(i)!=null) 
        col3 = new TableCol( util.createLabelItem( IngDate.dateToStr(rOrdExpDelDt.elementAt(i).toString()), vLabelAttrib ), null, null, null, null );
       else
        col3 = new TableCol( util.createLabelItem( "&nbsp;", vLabelAttrib ), null, null, null, null );
       row.add(col);
       row.add(col1);
       row.add(col2);
       row.add(col3);
       tab.add(row);

       form.add(tab);
       form.add(new NL(1));

       Table tb = new Table("1","center","valign=\"Left\" border=\"0\" width=\"90%\" COLS=\"6\" cellpadding=\"3\" cellspacing=\"3\" BGCOLOR=\"#FFFFCA\"");
       TableRow rw = new TableRow("Left", null, "BGCOLOR=\"#666666\"");
       vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDER_POS_NR", "Pos No" );
       TableHeader hd1 = new TableHeader( util.createLabelItem( vBPLate, "COLOR=\"White\""+vLabelAttrib ), "left", null, null, null);
       vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDER_FK_ITEM_ID", "Item Name" );
       TableHeader hd2 = new TableHeader( util.createLabelItem( vBPLate, "COLOR=\"White\""+vLabelAttrib ), "left", null, null, null);
       vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDER_FK_ITEMPACK_ID", "Item Pack Name" );
       TableHeader hd3 = new TableHeader( util.createLabelItem( vBPLate, "COLOR=\"White\""+vLabelAttrib ), "left", null, null, null);
       vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDER_QUANTITY", "Quantity" );
       TableHeader hd4 = new TableHeader( util.createLabelItem( vBPLate, "COLOR=\"White\""+vLabelAttrib ), "left", null, null, null);
       vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDER_UNITTYPE", "Unit Type" );
       TableHeader hd5 = new TableHeader( util.createLabelItem( vBPLate, "COLOR=\"White\""+vLabelAttrib ), "left", null, null, null);
       vBPLate = cdata1.GetConfigValue( "TR_ORDDTLS", nLangID, "BL_LABEL.B_ORDER_UNITPRICE", "Unit Price" );
       TableHeader hd6 = new TableHeader( util.createLabelItem( vBPLate, "COLOR=\"White\""+vLabelAttrib ), "left", null, null, null);
       rw.add( hd1 );
       rw.add( hd2 );
       rw.add( hd3 );
       rw.add( hd4 );
       rw.add( hd5 );
       rw.add( hd6 );
       tb.add( rw );

       Vector rPosNr = new Vector();
       Vector rItmName = new Vector();
       Vector rItmPkName = new Vector();
       Vector rQuantity = new Vector();
       Vector rUnitType = new Vector();
       Vector rUnitPrice = new Vector();

       try
       {
         conn =  db.GetDBConnection();
         stmt = conn.createStatement();
         query = " Select * From T_OrderDtls Where Fk_Order_ID=" + rOrdID.elementAt(i).toString() +
		 " Order By OrderDtls_ID ";
		 rs = stmt.executeQuery(query);

         while(rs.next())
         {
             rPosNr.addElement(rs.getString(3));
             rItmName.addElement( db.getName( rs.getString(4), "Item" ));
             rItmPkName.addElement( db.getName( rs.getString(5), "ItemPack"));
             rQuantity.addElement(rs.getString(6));
             rUnitType.addElement(Domain.getDomainDescFrmAttrib( "UNITTYPE", rs.getString(7), nLangID ));
             rUnitPrice.addElement(rs.getString(8));
         }
 
       }catch(SQLException sexe){System.out.println(sexe.getMessage());}
       finally 
       {
         try
         {
           if(rs!=null)
             rs.close();
           if(stmt!=null)
             stmt.close();
           if(conn!=null)
             conn.close();
         }catch(SQLException sexe){System.out.println(sexe.getMessage());}
       } 

       for( int j=0; j<rPosNr.size(); j++ )
       {
         try
         {
          TableRow rw1 = new TableRow("Left",null,null);
          rw1.add( new TableCol( util.createLabelItem( rPosNr.elementAt(j).toString(), vLabelAttrib ), null, null, null, null) );
          rw1.add( new TableCol( util.createLabelItem( rItmName.elementAt(j).toString(), vLabelAttrib ), null, null, null, null) );
          rw1.add( new TableCol( util.createLabelItem( rItmPkName.elementAt(j).toString(), vLabelAttrib ), null, null, null, null) );
          rw1.add( new TableCol( util.createLabelItem( rQuantity.elementAt(j).toString(), vLabelAttrib ), null, null, null, null) );
          rw1.add( new TableCol( util.createLabelItem( rUnitType.elementAt(j).toString(), vLabelAttrib ), null, null, null, null) );
          rw1.add( new TableCol( util.createLabelItem( rUnitPrice.elementAt(j).toString(), vLabelAttrib ), null, null, null, null) );
	  tb.add(rw1);
         }catch( Exception exe){ out.println( exe ); }
       }   
       form.add(tb);
       form.add("<BR>");
       form.add("<hr ALIGN=LEFT SIZE=4 WIDTH=\"100%\"><br>");
       form.add("<table  BORDER=0 CELLSPACING=0 CELLPADDING=0 COLS=3 WIDTH=\"90%\" > ");
       form.add("<TR> ");
       form.add("<TD width=\"15%\" vAlign=\"Top\"><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Instructions</font></b></TD><TD>");
       form.add( util.createLabelItem( inst, vLabelAttrib ) );
       form.add("</TR><TR>");
       form.add("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark1</Font></b></TD> ");
       form.add("<TD><font face=\"Arial,Helvetica\" size=-1>");
       if(rOrdRem1.elementAt(i)!=null)  
        form.add(rOrdRem1.elementAt(i).toString());
       form.add("</Font></TD> ");
       form.add("</TR>");
       form.add("<TR>");
       form.add("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark2</Font></b></TD>");
       form.add("<TD><font face=\"Arial,Helvetica\" size=-1>");
       if(rOrdRem2.elementAt(i)!=null)  
         form.add(rOrdRem2.elementAt(i).toString());
       form.add("</Font></TD></TR><TR>");
       form.add("<TD><b><font face=\"Arial,Helvetica\" size=-1>&nbsp;&nbsp;&nbsp;&nbsp;Remark3</Font></b></TD>");
       form.add("<TD><font face=\"Arial,Helvetica\" size=-1>");
       if(rOrdRem3.elementAt(i)!=null)  
         form.add(rOrdRem3.elementAt(i).toString());
       form.add("</Font></TD></TR>");
       form.add("</table>");
       form.add(new HRule());
     }
     page.add(head);
     body.add(form);  
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
      String nUserID   = Parse.GetValueFromString( vPID, "UserID" );
      String nLangID   = Parse.GetValueFromString( vPID, "LangID" );

      String rOrdNr = request.getParameter("nPrint");
      String cDM_OrdStat[] = request.getParameterValues("cDM_OrdStat");
      String sTransID = request.getParameter("pnTransID");
      String vTemplName = request.getParameter("vTemplName");
      String vFrom_Order_ID = request.getParameter("vFrom_Order_ID");
      String vTo_Order_ID = request.getParameter("vTo_Order_ID");

      DBConnect db = new DBConnect();
      Connection conn = null;
      Statement stmt = null;
      Message msg = new Message(); 
      Valid val = new Valid();
    
      vStatus = Trans.checkTransValidity( sTransID );
      if(vStatus!=null && !vStatus.equalsIgnoreCase("ERROR"))
      {
         try
         {  
  
           conn = db.GetDBConnection();
           String order_id[] = Parse.parse(rOrdNr,"~");
           for(int i=0;i<order_id.length;i++)
           {  
             int id = Integer.parseInt(order_id[i].trim()); 
             stmt = conn.createStatement();
             stmt.executeUpdate("UPDATE T_ORDER SET DM_ORDSTAT='"+cDM_OrdStat[i+1]+"' WHERE Order_ID="+id+"");         
           }
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
         Trans.setTransID( sTransID );
       }  
       if(nMsgID <=5 )
         response.sendRedirect("/JOrder/servlets/ChnStatusFrame?pvMode=T&vTemplName="+vTemplName+"&vFrom_Order_ID="+vFrom_Order_ID+"&vTo_Order_ID="+vTo_Order_ID+"&pvMessage="+URLEncoder.encode(msg.GetMsgDesc(nMsgID,nLangID)));
       else
         out.println(WebUtil.ShowException(errMsg, nMsgID, nLangID));
   }
}
