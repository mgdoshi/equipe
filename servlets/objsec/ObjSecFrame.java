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

public class ObjSecFrame extends HttpServlet
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

        String nLangID=null;
        String nUserID=null;
        String pvMode = request.getParameter("pvMode");
        String pvParentObj = request.getParameter("pvParentObj");
        String pvObjType = request.getParameter("pvObjType");
        String pvSecLevel = request.getParameter("pvSecLevel");
        String pnRefID = request.getParameter("pnRefID");
        String pvMessage = request.getParameter("pvMessage");

        DBConnect db = new DBConnect();
        ConfigData cdata = new ConfigData();
        WebUtil util = new WebUtil();
        Message msg = new Message();

        nUserID   = Parse.GetValueFromString( vPID, "UserID" );
        nLangID   = Parse.GetValueFromString( vPID, "LangID" );

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
          scrdata.add("  function submit_form( pvAction )  { \n"+
                      "     if ( confirm( \""+ msg.GetMsgDesc( 15, nLangID )+ "\" )  )\n"+
                      "        this.location.href = \"/JOrder/servlets/ObjSecFrame?pvMode=D&pvParentObj=&pvObjType=&pvSecLevel=&pnRefID=\" \n"+
                      "   }    \n" );
        }
        else if( pvMode!=null && ( pvMode.equalsIgnoreCase("I") || pvMode.equalsIgnoreCase("U") ) )
        {
          scrdata.add("  function submit_form() {                                                      \n "+
                      "    with( this.right_frame.document.forms[0] )                                  \n "+
                      "     {		                                                    	       \n "+
                      "	      vAction.value = ( pnCount.value > 0 ? \"Update\" : \"Insert\" )          \n "+
                      "	      pnCount.value = vObj_Name.length                                         \n "+
                      "       nDisplay_Flg[0].value =\"\" \n"+
                      "       nInsert_Flg[0].value =\"\" \n"+
                      "       nUpdate_Flg[0].value =\"\" \n"+
                      "       nUpdNull_Flg[0].value =\"\" \n"+
                      "	      for( var j=1; j<nDisplay_Flg.length; j++ ) {                             \n "+
                      "         if( nDisplay_Flg[j].type == 'hidden')                                  \n "+
                      "           nDisplay_Flg[0].value += nDisplay_Flg[j].value + \"~\"               \n"+
                      "         else                                                                   \n"+
                      "	          nDisplay_Flg[0].value += ( nDisplay_Flg[j].checked ? \"1\" : \"0\" ) + \"~\" \n "+
                      "	      }      						                       \n "+
                      "	      for( var j=1; j<nInsert_Flg.length; j++ ) {                              \n "+
                      "         if( nInsert_Flg[j].type == 'hidden')                                   \n "+
                      "           nInsert_Flg[0].value += nInsert_Flg[j].value + \"~\"                 \n"+
                      "         else                                                                   \n"+
                      "	          nInsert_Flg[0].value  += ( nInsert_Flg[j].checked  ? \"1\" : \"0\" ) + \"~\" \n "+
                      "	      }      						                       \n "+
                      "	      for( var j=1; j<nUpdate_Flg.length; j++ ) {                              \n "+
                      "         if( nUpdate_Flg[j].type == 'hidden')                                   \n "+
                      "           nUpdate_Flg[0].value += nUpdate_Flg[j].value + \"~\"                 \n"+
                      "         else                                                                   \n"+
                      "	          nUpdate_Flg[0].value  += ( nUpdate_Flg[j].checked  ? \"1\" : \"0\" ) + \"~\" \n "+
                      "       }      						                       \n "+
                      "	      for( var j=1; j<nUpdNull_Flg.length; j++ ) {                             \n "+
                      "         if( nUpdNull_Flg[j].type == 'hidden')                                  \n "+
                      "           nUpdNull_Flg[0].value += nUpdNull_Flg[j].value + \"~\"               \n"+
                      "         else                                                                   \n"+
                      "	          nUpdNull_Flg[0].value += ( nUpdNull_Flg[j].checked ? \"1\" : \"0\" ) + \"~\" \n "+
                      "       }      						                       \n "+
                      "	     if ( confirm( \""+ msg.GetMsgDesc( 10, nLangID ) + "\" ) )                \n "+
                      "	         submit()                                                              \n "+
                      "     }                                                                          \n "+
                      "  }\n ");
        }
        else if( pvMode!=null &&  pvMode.equalsIgnoreCase("D")  )
        {
          scrdata.add(" function submit_form( pvAction ) {					      \n "+
                      "   with( this.right_frame.document.forms[0] )                                  \n "+
                      "   {					                                      \n "+
                      "     if ( pvAction == \"New\" || pvAction == \"Query\" ) {                     \n "+
                      "       for ( var i=1; i<vSecLevel.length; i++ ) {   	                      \n "+
                      "          if ( vSecLevel[i].checked )                                          \n "+
                      "            vSecLevel[0].value = vSecLevel[i].value                            \n "+
                      "       }					              	                      \n "+
                      "	      if ( vSecLevel[2].checked  && vGroup.options[vGroup.selectedIndex].value == \"\" ) {\n "+
                      "	        alert( \"" +msg.GetMsgDesc( 26, nLangID) +"\" ); vGroup.focus(); vGroup.select(); return; \n "+
                      "       }  				              	                      \n "+
                      "	      if ( vSecLevel[3].checked && vUser.options[vUser.selectedIndex].value == \"\" ) { \n "+
                      "	        alert( \""+ msg.GetMsgDesc( 27, nLangID) +"\" ); vUser.focus(); vUser.select(); return;  \n "+
                      "       }  				              	                     \n "+
                      "     }					              	                     \n "+
                      "     if ( ( pvAction == \"New\" || pvAction == \"Query\" ) && vFormName.options[vFormName.selectedIndex].value == \"\" ) { \n "+
                      "	      alert( \"" + msg.GetMsgDesc( 28, nLangID) +"\" ); vFormName.focus(); vFormName.select(); return; \n "+
                      "     }					              	                    \n "+
                      "     if ( confirm( \"" + msg.GetMsgDesc( 15, nLangID ) + "\" )  ) {              \n "+ 
                      "       vAction.value = pvAction    	              	                    \n "+
                      "       submit()        		              	                            \n "+
                      "     }					              	                    \n "+
                      "   }					              	                    \n "+
                      " } \n");
        }
        scrdata.add("// End Hidding -->");
        scr.add(scrdata);
        head.add(scr);
        head.add(Info.ShowStatus(pvMessage));
        head.add(Info.ShowFormInfo(request, "SY_OBJSEC", vPID));
        FrameSet set = new FrameSet(null,"5%,*","FRAMEBORDER=\"NO\" BORDER=\"1\" scrolling=\"no\"");
        Frame left  = new Frame("/JOrder/servlets/ObjSecToolBar?pvMode="+pvMode,"left_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\" SCROLLING=\"no\"");
        if( pvMode!=null && pvMode.equalsIgnoreCase("N"))
           right = new Frame("/JOrder/servlets/ObjSecTable?pvParentObj="+pvParentObj+"&pvObjType="+pvObjType+"&pvSecLevel="+pvSecLevel+"&pnRefID="+pnRefID,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if( pvMode!=null && pvMode.equalsIgnoreCase("D")  )
           right = new Frame("/JOrder/servlets/DefineObjSec","right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        else if( pvMode!=null && ( pvMode.equalsIgnoreCase("I") || pvMode.equalsIgnoreCase("U") ) )
           right = new Frame("/JOrder/servlets/ObjSecEntry?pvMode="+pvMode+"&pvSecLevel="+pvSecLevel+"&pnRefID="+pnRefID+"&pvParentObj="+pvParentObj,"right_frame","FRAMEBORDER=\"0\" MARGINWIDTH=\"0\" MARGINHEIGHT=\"0\"");
        NoFrame nof = new NoFrame();
        set.add(left);
        set.add(right);
        set.add(nof);
        page.add(head);
        page.add(set);
        out.println(page);
    }
}