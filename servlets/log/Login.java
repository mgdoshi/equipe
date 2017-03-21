import ingen.html.db.*;
import ingen.html.util.*;
import ingen.html.para.*;
import ingen.html.table.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.character.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Login extends HttpServlet
{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        DBConnect db = new DBConnect();
        ConfigData conf = new ConfigData();
        Message msg = new Message();
        WebUtil util = new WebUtil();
        String vImgOpt;
        String vImgPath=null;
        String vLang=null;
        String vFormType=null;
        String vUserName=null;
        String vPassWd=null;
        String vLabelStyle=null;
        int nLangID=1;           
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        vImgOpt = Param.getParamValue( "DEFAULT_IMAGE_SETTING" );
        vLang = Param.getParamValue( "DEFAULT_LANG" );
        nLangID = Integer.parseInt( db.getID( vLang, "Lang"));

        if(vImgOpt!=null && vImgOpt.equalsIgnoreCase("ON"))
          vImgPath = "/ordimg/" + vLang.toLowerCase() + "/BP_System/";
        else
          vLabelStyle = "SIZE=\"3\" COLOR=\"Black\" FACE=\"Arial\"";

        vFormType = conf.GetConfigValue( "SY_USER", nLangID, "WD_LOGIN","Login Form" );
        vUserName = conf.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_USER_NAME", "User Name" );
        vPassWd   = conf.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_USER_PASS", "User PassWord" );
        
        Page page = new Page();
        Head head = new Head();

        Script scr = new Script( "JavaScript", null );
        HtmlTag scrdata = new HtmlTag();
        scrdata.add("<!-- Start Hidding \n" );
        scrdata.add(" function submit_form() { \n" +
                    "   var aField \n" +
                    "   var vErrMsg \n"+
                    "   with( document.forms[0] ) { \n" +
           	    "     aField = new Array( \""+vUserName+"\", pvUser.value, \""+vPassWd+"\", pvPassWd.value ) \n"+
                    "     vErrMsg = top.check_fields( aField ) \n "+
        	    "     if ( vErrMsg == \"\" ) { \n"+
                    "       if ( confirm( \"" + msg.GetMsgDesc( 15, nLangID ) + "\" )  ) \n "+
 	            "         document.forms[0].submit() \n" +
                    "     } \n "+
	            "     else { \n"+                
                    "         vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                    "         alert( vErrMsg )\n" +
                    "         return          \n " +
                    "     } \n" +
                    "   } \n"+
                    " } \n" );
        scrdata.add("// End Hidding -->");
        scr.add(scrdata);

        Body body = new Body("/ordimg/BACKGR2.GIF",null);
        Form form = new Form("Login","POST",null,null,null);

        Script scr1 = new Script( "JavaScript", null );
        HtmlTag scrdata1 = new HtmlTag();
        scrdata1.add("<!-- Start Hidding" + "\n" +
                     "top.show_form(\"'"+vFormType+"'\")" + "\n" + 
                     "// End Hidding -->");
        scr1.add(scrdata1);

        Center cen = new Center();
        NL nl = new NL(4);
        HRule hr = new HRule( "WIDTH=\"80%\"" );
        Font f = new Font( "Brown","Arial,Helvetica","+1",null);
        Table tab = new Table( "1","center","Border=\"0\" width=\"45%\" COLS=2");
          TableRow row = new TableRow("Center",null,null);
             TableCol col = new TableCol( conf.GetConfigValue( "SY_USER", nLangID, "BL_LABEL.B_USER_LOGIN", "Login to Order Tracking System" ),null, null, null,"WIDTH=\"25%\"");
          TableRow row1 = new TableRow("Center",null,null);
             TableCol col1 = new TableCol( hr, null, null, null,null);
        NL nl1 = new NL(1);
        col.setFormat( f );
        row.add(col);
        row1.add(col1);
        tab.add(row);
        tab.add(row1);
        Table tab1 = new Table( "1","center","Border=\"0\" width=\"50%\" COLS=2");
          TableRow row2 = new TableRow("Center",null,null);
             TableCol col2 = new TableCol( util.GetBoilerPlate( vImgOpt, vImgPath+"B_USER.gif", vUserName, vLabelStyle),null, null, null,"WIDTH=\"25%\"");
             FormText user = new FormText( "pvUser", "20", "100", null, "onBlur=\"this.value=this.value.toUpperCase()\"");
             TableCol col3 = new TableCol( user, null, null, null,"WIDTH=\"20%\"");
          TableRow row3 = new TableRow("Center",null,null);
             TableCol col4 = new TableCol( "&nbsp;", null, null, null,null);
             TableCol col5 = new TableCol( null, null, null, null,null);
          TableRow row4 = new TableRow("Center",null,null);
             TableCol col6 = new TableCol( util.GetBoilerPlate( vImgOpt, vImgPath+"B_PASS.gif", vPassWd, vLabelStyle),null, null, null,null);
             FormPassword passwd = new FormPassword( "pvPassWd", "20", "100", null, null);
             TableCol col7 = new TableCol( passwd, null, null, null, null);
        row2.add(col2);
        row2.add(col3);
        row3.add(col4);
        row3.add(col5);
        row4.add(col6);
        row4.add(col7);
        tab1.add(row2);
        tab1.add(row3);
        tab1.add(row4);
        NL nl2 = new NL(2);
        FormHidden lang = new FormHidden("pnLangID", Integer.toString( nLangID ), null);
        FormButton login = new FormButton(null, " Login ", "onClick=\"submit_form()\"");
        form.add( tab );
        form.add( nl1 );
        form.add( tab1 );
        form.add( nl2 );
        form.add( lang );
        form.add( login );
        cen.add( form );
        body.add( nl );
        body.add( cen);
        head.add( scr );
        head.add( scr1 );
        page.add( head );
        page.add( body );
        out.println(page);
    }       

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        User usr = new User();
        Message msg = new Message();
        CookieUtil util = new CookieUtil();
        DBConnect db = new DBConnect();
        int nUserID=0;
        int nAuditID=0;
        String vPID=null;
        IngDate dt = new IngDate(); 
        PrintWriter out = response.getWriter();
        String pvUser = request.getParameter("pvUser");
        String pvPassWd = request.getParameter("pvPassWd");
        int pnLangID = Integer.parseInt( request.getParameter("pnLangID") );
        boolean flag = usr.isLoginValid( pvUser, pvPassWd );
        if( flag )
        {
          response.setContentType("text/html");
          nUserID = Integer.parseInt( db.getID( pvUser, "User" ) );
          nAuditID = Integer.parseInt( db.getNextVal( "S_Audit" ) );
          try
          {
            Connection conn = db.GetDBConnection();
            Statement stmt = conn.createStatement();
            String query = "Insert Into T_Audit( Audit_ID, Fk_User_ID, Login_Dt ) Values("+ nAuditID+","+nUserID+",'"+dt+"')";
            if( stmt.executeUpdate(query) == 0 )
            {
               System.out.println( "Failed Login ");     
            }   
            stmt.close();
            conn.close();              
          }catch(Exception sexe){System.out.println(sexe);}
          vPID = "AuditID="     + Integer.toString(nAuditID) +
                 "~UserID="     + Integer.toString(nUserID)  +
                 "~ClientID="   + usr.getClientIDForUser(nUserID)  +
                 "~EmployeeID=" + usr.getEmployeeIDForUser(nUserID) +
                 "~RecSecID="   + usr.getRecSecIDForUser(nUserID)  +
                 "~LangID="     + usr.getLangIDForUser(nUserID) +
                 "~SchemeID="   + usr.getSchemeIDForUser(nUserID) +
                 "~Image="      + Param.getParamValue( "DEFAULT_IMAGE_SETTING" ) + "~"; 
          Cookie cookie = util.setCookie( response, "PID", vPID,-1 );
          Page page = new Page();
          Body body = new Body("/ordimg/BACKGR2.GIF",null);
          Script scr = new Script( "JavaScript", null );
          HtmlTag scrdata = new HtmlTag();
          scrdata.add("<!-- Start Hidding \n" +
                      " top.bot_frame.document.MenuApplet.enableMenuApplet(1) \n"+
                      " // End Hidding -->" );
          Script scr1 = new Script( "JavaScript", null );
          HtmlTag scrdata1 = new HtmlTag();
          scrdata1.add("<!-- Start Hidding \n" +
                       " top.show_status( \"" + msg.GetMsgDesc( 22, pnLangID) +"\" ) \n"+
                       " // End Hidding -->");
          NL nl = new NL( 4 );
          Center cen = new Center();
          Font f = new Font( "#3333FF", "Arial,Helvetica", "+3", null );
          HtmlTag tag = new HtmlTag();
          tag.add( "Welcome to Order Entry &"+"amp; Tracking System<BR>");
          tag.setFormat( f );
          f = new Font( "#333333", "Arial,Helvetica", null, null );
          HtmlTag tag1 = new HtmlTag();
          tag1.add( "Copyright &"+"copy; 1999 <BR>");
          tag1.setFormat( f );
          f = new Font( "#000000", "Arial,Helvetica", "+1", null );
          HtmlTag tag2 = new HtmlTag();
          tag2.add( "Ingenium Computing Private Limited, Pune INDIA<BR>" );
          tag2.setFormat( f );
          f = new Font( "#000000", "Arial,Helvetica", null, null );
          HtmlTag tag3 = new HtmlTag();
          tag3.add( " #2, Shenshah, Off North Main Road,<BR> ");
          tag3.setFormat( f );
          HtmlTag tag4 = new HtmlTag();
          tag4.add( " Koregaon Park, Pune INDIA.<BR>");
          tag4.setFormat( f );
          HtmlTag tag5 = new HtmlTag();
          tag5.add( " Tel : 91-020-636773 <BR>");
          tag5.setFormat( f );
          HtmlTag tag6 = new HtmlTag();
          tag6.add( " Email : " );
          Anchor anc = new Anchor( "mailto:ingenium@wminet.net", null, null, null, null );
          anc.add( " ingenium@wminet.net <BR>");
          tag6.add( anc );
          tag6.setFormat( f );
          HtmlTag tag7 = new HtmlTag();
          tag7.add( "All Rights Reserved <BR>" );
          tag7.setFormat( f );
          cen.add( tag );
          cen.add( tag1 );
          cen.add( tag2 );
          cen.add( tag3 );
          cen.add( tag4 );
          cen.add( tag5 );
          cen.add( tag6 );
          cen.add( tag7 );
          page.add( body );
          scr.add(scrdata);
          body.add( scr ); 
          scr1.add(scrdata1);
          body.add( scr1 ); 
          body.add( nl );
          body.add( cen );
          out.println( page );
        } 
        else    
        {
          response.setContentType("text/html");
          Script scr = new Script();  
          HtmlTag scrdata = new HtmlTag();
          scrdata.add("<!-- Start Hidding \n" +
                       " this.location.href=\"/JOrder/servlets/Login\" \n"+
                       " top.show_status( \"" + msg.GetMsgDesc( 23, pnLangID) +"\" ) "+
                       "\n// End Hidding -->");
          scr.add(scrdata);
          out.println(scr);
        }
    }

}
