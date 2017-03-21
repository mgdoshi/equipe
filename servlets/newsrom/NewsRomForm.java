import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class NewsRomForm
{
  public static Script ShowNewsRomScript(HttpServletRequest request,String pvMode , String vPID, String pnStartPos, String pnRows)
  {
   
    String vCaption=null;
    String vNewsDate=null;
    String vMatter=null;
    String vOrgin=null;
    String nLangID=null;
    String nUserID=null;

    nUserID   = Parse.GetValueFromString( vPID, "UserID" );
    nLangID   = Parse.GetValueFromString( vPID, "LangID" );

    ConfigData config = new ConfigData();
    Message msg = new Message();
 

    vCaption  = config.GetConfigValue( "SY_NEWS", nLangID, "BL_LABEL.B_NEWS_CAPTION", "Caption" );
    vNewsDate = config.GetConfigValue( "SY_NEWS", nLangID, "BL_LABEL.B_NEWS_NEWS_DATE", "News Date" );
    vMatter   = config.GetConfigValue( "SY_NEWS", nLangID, "BL_LABEL.B_NEWS_MATTER", "Matter" );
    vOrgin    = config.GetConfigValue( "SY_NEWS", nLangID, "BL_LABEL.B_NEWS_ORGINATOR", "Orginator" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode==null || pvMode.equals("")|| pvMode.equalsIgnoreCase("null"))
    {
     scrdata.add(" function PreviousRecords()       {                            \n"+
                 "   with(this.right_frame.document.forms[0]) {                  \n"+
                 "     var nRows = parseInt(pnRows.value)                        \n"+
                 "     var nStartPos = parseInt(pnStartPos.value)                \n"+
                 "     if( nStartPos-4 < 1 )                   	                 \n"+
                 "       top.show_status(\""+ msg.GetMsgDesc( 1, nLangID )+ "\") \n"+
                 "     else  {                                                   \n"+
                 "       pnStartPos.value = nStartPos-4                          \n"+
                 "       submit() }                                              \n"+
                 "   }   					                 \n"+
                 " }    					                 \n"+

                 " function NextRecords()  {                                     \n"+
                 "   with(this.right_frame.document.forms[0])  {      	         \n"+
                 "     var nRows = parseInt(pnRows.value)                        \n"+
                 "     var nStartPos = parseInt(pnStartPos.value)                \n"+
                 "     if( nStartPos + 4 > nRows )                              \n"+
                 "         top.show_status(\""+msg.GetMsgDesc( 2, nLangID )+"\") \n"+
                 "     else {                                                    \n"+
                 "        pnStartPos.value = nStartPos+4                         \n"+
                 "        submit()  } } }                                        \n"+ 

                 " function OpenNewsWindow(pnNewsID) {                           \n"+
                 "  vURL = \"/JOrder/servlets/NewsRomWin?pnNewsID=\"+ pnNewsID   \n"+
                 "  ClientItemWindow = window.open( vURL,\"ClientItemWindow\", \"menubar=0,scrollbars=1,resizable=0,width=400,height=300\")\n" +
                 "  return }                                                     \n");
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

  public static synchronized Table createNewsRomTable(String Columns, int pnStartPos)
  {
     Table table = new Table( null, "Center", "Border=\"0\" width=\"100%\"");
     Font f = new Font("white", "Arial", "3",null);
     Font f1 = new Font(null, "Arial", "3",null);
     TableRow headrow = new TableRow("left", null, null);
     TableHeader th = null;
     TableRow datarow = null;
     int count = 0;
     int counter=1; 
     int nrows=0; 
     DBConnect obj = new DBConnect();
     Connection conn = null;
     Statement stmt = null;
     ResultSet rs = null;
     try
      {
          conn = obj.GetDBConnection();
          stmt = conn.createStatement();
          String query = "Select " + Columns + " From T_News Order By news_Dt DESC";
          rs = stmt.executeQuery(query);
          while((counter < pnStartPos) && rs.next())
           counter++;
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            String caption = rs.getString(2);
            String URL = "JavaScript:top.mid_frame.OpenNewsWindow("+id+")";
            Anchor anc = new Anchor( URL, caption +"[ By "+rs.getString(5)+"]", null, "_parent", null);
            TableCol td1 = new TableCol("<FONT SIZE=4 FACE=\"ARIAL\"><U><I>"+anc+"</U></I></FONT>", null, null, null, null);
            datarow.add(td1);
            String data = null;
            data = IngDate.dateToStr( rs.getString(3) );
            TableCol td2 = new TableCol("</TD></TR><TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+data, null, null, null, null);
            td2.setFormat(f1);
            datarow.add(td2);
            data = rs.getString(4);
            if(data!=null)
              data = data.substring(0,data.length()<300?data.length():300);
            TableCol td3 = new TableCol("</TD></TR><TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<FONT COLOR=\"BLACK\" FACE=\"Courier\">"+data+"</FONT>", null, null, null, null);
            datarow.add(td3);           
            table.add(datarow); 
            if(nrows++==3)
              break;                         
          }
       }catch(Exception sexe){System.out.println(sexe);}  
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
          }catch(SQLException sexe){}
        }
       return table;  
   }
}

