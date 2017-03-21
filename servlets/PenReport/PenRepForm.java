import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PenRepForm
{
  public static Script ShowPenRepScript(String pvMode,String vPID )
  {
    Message msg = new Message();
    ConfigData cdata = new ConfigData();  
    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    String vClientName = cdata.GetConfigValue("TR_ORDER", nLangID, "BL_LABEL.B_ORDER_CLIENT_NAME","Client Name");
    String vTemplName = cdata.GetConfigValue( "TR_ORDER", nLangID, "BL_LABEL.B_ORDER_TEMPLATE", "Select Order Template" );
    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if(pvMode!=null && pvMode.equalsIgnoreCase("T"))
    {  
      scrdata.add(" function submit_form( ) {	\n" +
                  "  var nIdx = 0               \n" +
                  "  with( this.right_frame.document.forms[0] ){\n" +
                  "    for( var i=1; i<nPrint.length; i++ ){    \n" +
                  "      if( nPrint[i].checked ) {              \n" +
                  "        nIdx ++                              \n" +
                  "        nPrint[0].value += nPrint[i].value + \"~\" \n" +
                  "  } }                                        \n" +
                  "  if( nIdx == 0 ) {                          \n" +
                  "   alert( \""+msg.GetMsgDesc( 63, nLangID )+"\" ); return }    \n" +
                  "  if ( confirm( \""+msg.GetMsgDesc( 15, nLangID )+"\" )) submit()\n" +
                  "  } }                                                          \n");
    }
    else if(pvMode!=null && pvMode.equalsIgnoreCase("Q"))
    {
      scrdata.add(" function submit_form( pvAction ) {  \n" +
                  "  var vErrMsg=\"\"                          \n" +
                  "  with(this.right_frame.document.forms[0]){ \n" +
                  "  vAction.value = pvAction                  \n" +
                  "    if ( vTemplName.options.length>1 ) {     \n"+
                  "  aField = new Array( '"+vClientName+"', pnClientID.options[pnClientID.selectedIndex].value, '"+vTemplName+"', vTemplName.options[vTemplName.selectedIndex].value)\n" +
                  "  vErrMsg = top.check_fields( aField )   }                            \n" +
                  "  if( vErrMsg == \"\" ) {                          	                 \n" +
                  "    if( confirm( \""+msg.GetMsgDesc( 15, nLangID )+"\" ) ) submit() } \n" +
                  "  else {                                                              \n" +
                  "    vErrMsg = \""+msg.GetMsgDesc(9, nLangID )+"\" + vErrMsg           \n" +
                  "    alert( vErrMsg )    \n" +
                  "    return              \n" +
                  "  } } }   \n" );
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

   public static synchronized Table createPenRepTable(String Columns, String WhereClause, String Titles)
   {
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";

     if( WhereClause != null )
       WClause = WhereClause;

     Table table = new Table( null, "Center", Default);
     Font f = new Font("white", "Arial", "3",null);
     Font f1 = new Font(null, "Arial", "3",null);
     TableRow headrow = new TableRow("left", null, null);
     TableHeader th = null;
     TableRow datarow = null;
     int count = 0;

     DBConnect obj = new DBConnect();
     Connection conn = null;
     Statement stmt = null;
     ResultSet rs = null;

     try
      {
          conn = obj.GetDBConnection();
          stmt = conn.createStatement();
          String query = "Select " + Columns + " From T_Order ord, T_Client cln " + WClause;
          rs = stmt.executeQuery(query);
          if( Titles != null )
          {
            String []tokens = Parse.parse( Titles, ",");
            count = tokens.length;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol );
              headrow.add(th);
              th.setFormat(f);
            }
            th = new TableHeader( "Print", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }
          else
          {
            String []tokens = Parse.parse( Columns, ",");
            count = tokens.length-2;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol);
              headrow.add(th);
              th.setFormat(f);
            }
            th = new TableHeader( "Print", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }

          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            datarow.add(new TableCol( "Order # " + id, null, null, null, null));
            datarow.add(new TableCol(IngDate.dateToStr(rs.getString(2)), null, null, null, null));
            datarow.add(new TableCol(rs.getString(3), null, null, null, null));
            String dt = IngDate.dateToStr(rs.getString(4)); 
            if(dt!=null && !dt.trim().equals("") && !dt.trim().equalsIgnoreCase("null"))   
              datarow.add(new TableCol(dt, null, null, null, null));
            else 
              datarow.add(new TableCol("&nbsp;", null, null, null, null));
            FormCheckBox chkbox = new FormCheckBox("nPrint", id, null, null);
            datarow.add(new TableCol( chkbox , "center", null, null, null));
            table.add( datarow );
         }
         return table; 
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

