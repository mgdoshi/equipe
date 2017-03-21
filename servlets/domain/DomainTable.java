import ingen.html.*;
import ingen.html.form.*; 
import ingen.html.head.*;
import ingen.html.para.*;
import ingen.html.character.*;
import ingen.html.table.*;
import ingen.html.jwa.*;
import ingen.html.util.*;
import ingen.html.db.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DomainTable extends HttpServlet
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
      String vDomainName=null;
      String vImagePath=null;
      String vBPLate=null;
      String query=null;
      String vImgOption=null;
      String vFormType=null;
      String vLabelAttrib=null;
      String vTextAttrib=null;
      String vListAttrib=null;
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

      String pvDomain = request.getParameter("pvDomain");
      WebUtil util = new WebUtil();
      ConfigData cdata = new ConfigData();
      DBConnect db = new DBConnect();
      Statement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      vFormType = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "WD_TABLE", "Domain / T" );

      if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
        vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_Domain/";
      else
        vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );
     
      vTextAttrib  = Scheme.getSchemeProperty( nSchemeID, 'B', 'N' );
      vListAttrib  = Scheme.getSchemeProperty( nSchemeID, 'C', 'N' );

        try
        {
          conn = db.GetDBConnection();
          stmt = conn.createStatement();       
          query = "Select Domain_Name From T_Domain " +
                  " WHERE Domain = '"+pvDomain+"'";
          rs = stmt.executeQuery(query);
          if( rs.next() )
          {
            vDomainName = rs.getString(1);           
          }  
       }catch(SQLException sexe){System.out.println(sexe);}
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

        Page page = new Page();
        Head head = new Head();
        Script scr = new Script( "JavaScript", null );
        HtmlTag scrdata = new HtmlTag();
        scrdata.add("<!-- Start Hidding" + "\n" +
                    "top.show_form(\""+vFormType+"\")" + "\n" +
                    "// End Hidding -->");
        scr.add(scrdata);
        head.add(scr);
        Body body = new Body("/ordimg/BACKGR2.GIF",null);
        Form form = new Form();
        form.add( new NL(3) );
        Table tab1 = new Table("1","center","Border=\"0\" width=\"60%\" COLS=6");
        TableRow row = new TableRow(null,null,null);
        vBPLate = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_DOMAIN", "Domain" );
        TableHeader head1 = new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"DOMAIN.gif", vBPLate, vLabelAttrib ),null, null, null,null); 
        TableCol col = new TableCol(util.createLabelItem(pvDomain,vLabelAttrib), null, null, null,null); 
        vBPLate = cdata.GetConfigValue( "ST_DOMAIN", nLangID, "BL_LABEL.B_DOMAIN_DOMAIN_NAME", "Description" );
        TableHeader head2 = new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"Desc.gif", vBPLate, vLabelAttrib ),null, null, null,null); 
        TableCol col1 = new TableCol(util.createLabelItem(vDomainName,vLabelAttrib),null, null, null,null); 
        row.add(head1);
        row.add(col);
        row.add(head2);
        row.add(col1);
        tab1.add(row);
        form.add(tab1);
        form.add(new NL(2) );
        String vCols = "Sequence_Nr, Attrib, Attrib_Desc, Fk_Lang_ID";
        String vTitles = "Seq Nr, Attribute, Description, Language";
        form.add(new FormHidden("pvDomain", pvDomain,null)); 
        form.add(DomainForm.createDomTable(vCols, pvDomain, vTitles));
        body.add(form);
        page.add(head);
        page.add(body);
        out.println(page);
    }
}
