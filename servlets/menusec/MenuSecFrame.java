import ingen.html.*;
import ingen.html.frame.*;
import ingen.html.head.*;
import ingen.html.db.*;
import ingen.html.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MenuSecFrame extends HttpServlet
{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        String nLangID=null;
        String nUserID=null;
        String vPID=null;
        String vGroupName = null;
        String vUserName = null;
        String pvMode = request.getParameter("pvMode");
        String pvSecLevel = request.getParameter("pvSecLevel");
        String pnRefID = request.getParameter("pnRefID");
        String pvMessage = request.getParameter("pvMessage");

        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();

        DBConnect db = new DBConnect();
        ConfigData cdata = new ConfigData();
        WebUtil util = new WebUtil();
        Message msg = new Message();
        CookieUtil PkCookie = new CookieUtil();
        vPID = PkCookie.getCookie(request,"PID");
        if(vPID==null)
        {
          out.println(WebUtil.IllegalEntry());
          return;
        }

        nUserID   = Parse.GetValueFromString( vPID, "UserID" );
        nLangID   = Parse.GetValueFromString( vPID, "LangID" );

        vGroupName = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_LABEL.B_MENUSEC_GROUP_NAME", "Group Name" );
        vUserName  = cdata.GetConfigValue( "SY_MENUSEC", nLangID, "BL_LABEL.B_MENUSEC_USER_NAME", "User Name" );

        Page page = new Page();
        Head head = new Head();
        Frame right=null;
        Title title = new Title("Order Tracking System");
        head.add(title);
        Script scr = new Script( "JavaScript", null );
        HtmlTag scrdata = new HtmlTag();
        scrdata.add("<!-- Start Hidding" + "\n");
        if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
        {
          scrdata.add(" function submit_form( ){ \n"+
                      "    if ( confirm( \""+ msg.GetMsgDesc( 15, nLangID ) + "\" )  )               \n"+
                      "       this.location.href = \"/JOrder/servlets/MenuSecFrame?pvMode=D&pvSecLevel=&pnRefID=\" \n"+
                      "   }	\n");
        }
        else if( pvMode!=null && pvMode.equalsIgnoreCase("I") )
        {
          scrdata.add("  function submit_form() {                                                    \n"+
                      "     var aField           		             	                     \n"+
                      "     with( this.right_frame.document.forms[0] ) {	                     \n"+
                      "       nAllow_Flg[0].value=\"\"                       \n"  + 
                      "	      vAction.value = ( pnCount.value > 0 ? \"Update\" : \"Insert\" )        \n"+
                      "	      pnCount.value = nAllow_Flg.length                                      \n"+
                      "       for ( var i=1; i<nAllow_Flg.length; i++ ) {                            \n"+
                      "	         nAllow_Flg[0].value += ( nAllow_Flg[i].checked ? \"1\" : \"0\" ) +\"~\"  \n"+
                      "	      }      						                     \n"+
                      "	      if ( confirm( \""+ msg.GetMsgDesc( 10, nLangID ) + "\" ) ) {           \n"+
                      "	        submit()                                                             \n"+
                      "       }						                             \n"+
                      "    }						                             \n"+
                      " }\n ");
        }
        else if( pvMode!=null &&  pvMode.equalsIgnoreCase("D")  )
        {
          scrdata.add(" function GetSecLevelPriv() {                                                  \n"+
                      "    var pValue                                                                 \n"+
                      "    with(this.right_frame.document.forms[0])   {                               \n"+
                      "      for( var i=0; i< nPriv.length; i++ ) {                                   \n"+
                      "        if(nPriv[i].checked)    {                                              \n"+
                      "          pValue = nPriv[i].value                                              \n"+
                      "        }                                                                      \n"+
                      "      }                                                                        \n"+
                      "      if( pValue == \"S\" )   {                                                \n"+
                      "        if ( confirm( \""+ msg.GetMsgDesc( 15, nLangID ) + "\" )  )            \n"+
                      "          top.mid_frame.location.href = \"/JOrder/servlets/MenuSecFrame?pvMode=I\"+\"&\"+\"pvSecLevel=\"+pValue+\"&\"+\"pnRefID=\" \n"+
                      "      }                                                                        \n"+
                      "      if( pValue == \"G\" )   {                                                \n"+
                      "	       if ( pvGroupName.options[pvGroupName.selectedIndex].value == \"\" ) {  \n "+
                      "	         alert( \"" +msg.GetMsgDesc( 26, nLangID) +"\" ); pvGroupName.focus(); pvGroupName.select(); return; \n "+
                      "        }  				              	                      \n "+
                      "        if ( confirm( \"" + msg.GetMsgDesc( 15, nLangID ) + "\" )  )           \n"+
                      "          top.mid_frame.location.href = \"/JOrder/servlets/MenuSecFrame?pvMode=I\"+\"&\"+\"pvSecLevel=\"+pValue+\"&\"+\"pnRefID=\"+pvGroupName.options[pvGroupName.selectedIndex].value \n"+
                      "      }                                                                        \n"+
                      "      if( pValue == \"U\" )   {                                                \n"+
                      "	       if ( pvUserName.options[pvUserName.selectedIndex].value == \"\" ) {    \n "+
                      "	         alert( \"" +msg.GetMsgDesc( 27, nLangID) +"\" ); pvUserName.focus(); pvUserName.select(); return; \n "+
                      "        }  				              	                      \n "+
                      "        if ( confirm( \"" + msg.GetMsgDesc( 15, nLangID ) +"\" )  )            \n"+
                      "          top.mid_frame.location.href = \"/JOrder/servlets/MenuSecFrame?pvMode=I\"+\"&\"+\"pvSecLevel=\"+pValue+\"&\"+\"pnRefID=\"+pvUserName.options[pvUserName.selectedIndex].value \n"+
                      "      }   \n"+
                      "    } \n"+
                      "  }\n ");
        }
        scrdata.add("// End Hidding -->");
        scr.add(scrdata);
        head.add(scr);
        head.add(Info.ShowStatus(pvMessage));
        head.add(Info.ShowFormInfo( request, "SY_MENUSEC", vPID )); 
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        Frame left  = new Frame("/JOrder/servlets/MenuSecToolBar?pvMode="+pvMode,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
           right = new Frame("/JOrder/servlets/MenuSecTable?cDM_SecLevel="+pvSecLevel+"&nRef_ID="+pnRefID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if( pvMode!=null && pvMode.equalsIgnoreCase("D")  )
           right = new Frame("/JOrder/servlets/DefineMenuSec","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if( pvMode!=null && pvMode.equalsIgnoreCase("I")  )
           right = new Frame("/JOrder/servlets/MenuSecEntry?pvSecLevel="+pvSecLevel+"&pnRefID="+pnRefID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        NoFrame nof = new NoFrame();
        set.add(left);
        set.add(right);
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }
}