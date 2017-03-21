import ingen.html.*;
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
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MenuSecTable extends HttpServlet
{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        String nLangID=null;
        String nSchemeID=null;
        String vFormType=null;
        String vBPLate=null;
        String vWhereClause = null;
        String vImgOption=null;
        String vImagePath=null;
        String vColumns=null;
        String vTitles=null;
        String vLabelAttrib=null;
        String pvSecLevel = request.getParameter("cDM_SecLevel");
        String pnRefID = request.getParameter("nRef_ID");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        DBConnect db = new DBConnect();
        WebUtil util = new WebUtil();
        ConfigData cdata = new ConfigData();
        CookieUtil PkCookie = new CookieUtil();
        String vPID = PkCookie.getCookie(request,"PID");
        if(vPID==null)
        {
          out.println(WebUtil.IllegalEntry());
          return;
        }

        nLangID   = Parse.GetValueFromString( vPID, "LangID" );
        nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
        vImgOption = Parse.GetValueFromString( vPID, "Image" );

        vFormType = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "WD_TABLE", "MenuSec / T" );

        if(vImgOption!=null && vImgOption.equalsIgnoreCase("ON"))
          vImagePath = "/ordimg/" + (db.getName( nLangID, "Lang" )).toLowerCase() + "/BP_MenuSec/";
        else
          vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );

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
        vColumns = "Menu_Name, DM_SecLevel, Ref_ID, Allow_Flg";
        vTitles  = "Menu Name, Ref Type, Ref ID, Allow Flg";

        Table tab = new Table("1","center","Border=\"0\" width=\"80%\" ");
        TableRow row = new TableRow("Left",null,null);
        vBPLate = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_LABEL.B_MENUSEC_DM_SECLEVEL", "Security Level" );
        TableHeader head1 = new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"seclev.gif", vBPLate, vLabelAttrib ),null, null, null,null);
        TableCol col1 = new TableCol( Domain.getDomainDescFrmAttrib( "SECLEVEL", pvSecLevel, nLangID ),null, null, null,null);
        row.add( head1 );
        row.add( col1 );
        if( Domain.getDomainDescFrmAttrib( "SECLEVEL", pvSecLevel, nLangID ).equals("Group") )
        {
          vBPLate = cdata.GetConfigValue( "SY_GROUP", nLangID, "BL_LABEL.B_GROUP_GROUP_NAME", "Group Name" );
          TableHeader head2 = new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"grp.gif", vBPLate, vLabelAttrib ),null, null, null,null);
          TableCol col2 = new TableCol( db.getName( Integer.parseInt(pnRefID), "Group"), null, null, null,null);
          row.add( head2 );
          row.add( col2 );
        }
        else if( Domain.getDomainDescFrmAttrib( "SECLEVEL", pvSecLevel, nLangID ).equals("User") )
        {
          vBPLate = cdata.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_USER_NAME", "User Name" );
          TableHeader head2 = new TableHeader(util.GetBoilerPlate(vImgOption, vImagePath+"usr.gif", vBPLate, vLabelAttrib ),null, null, null,null);
          TableCol col2 = new TableCol( db.getName( Integer.parseInt(pnRefID), "User"), null, null, null,null);
          row.add( head2 );
          row.add( col2 );
        }
        tab.add(row); 
       
        vWhereClause = " WHERE DM_SecLevel = '"+pvSecLevel+"'";
        if( !( pnRefID == null || pnRefID.equals("")|| pnRefID.equalsIgnoreCase("null") )  )
           vWhereClause += " AND Ref_ID = "+ pnRefID ;
        vWhereClause += " ORDER BY Menu_Name";

        Table tab1 = MenuSecForm.createMenuSecTable(vColumns, vWhereClause, vTitles, nLangID );

        body.add(new NL(3));
        body.add(tab);
        body.add(tab1);
        page.add(head);
        page.add(body);
        out.println(page);
    }
}
