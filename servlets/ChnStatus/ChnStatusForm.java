import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChnStatusForm
{
  public static Script ShowChnStatusScript(String pvMode,String vPID )
  {
    Message msg = new Message();
    ConfigData cdata = new ConfigData();  
    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if(pvMode!=null && pvMode.equalsIgnoreCase("T"))
    {  
       scrdata.add(" function submit_form( ) {	\n" +
                  "  var nIdx = 0               \n" +
                  "  with( this.right_frame.document.forms[0] ){\n" +
                  "   nPrint[0].value=\"\"                      \n" +
                  "    for( var i=1; i<nPrint.length; i++ ){    \n" +
                  "      if( nPrint[i].checked ) {              \n" +
                  "        nIdx ++                              \n" +
                  "        nPrint[0].value += nPrint[i].value + \"~\" \n" +
                  "  } }                                        \n" +
                  "  if( nIdx == 0 ) {                          \n" +
                  "   alert( \""+msg.GetMsgDesc( 63, nLangID )+"\" ); return }    \n" +
                  "  if ( confirm( \""+msg.GetMsgDesc( 15, nLangID )+"\" ))\n" +
                  "   submit()\n"+ 
                  "  } }       \n");   
    }
    else if(pvMode!=null && pvMode.equalsIgnoreCase("Q"))
    {
      scrdata.add(" function submit_form( pvAction ) {  \n" +
                  "  with(this.right_frame.document.forms[0]){ \n" +
                  "  vAction.value = pvAction                  \n" +
                  "  if( confirm( \""+msg.GetMsgDesc( 15, nLangID )+"\" ) ) submit() \n" +
                  "  } }  \n" );
    }
    else if(pvMode!=null && pvMode.equalsIgnoreCase("L"))
    {  
      scrdata.add(" function submit_form( ) {	\n" +
                  "  if ( confirm( \""+msg.GetMsgDesc( 15, nLangID )+"\" ))\n" +
                  "   this.right_frame.document.forms[0].submit()          \n" +      
                  "  }                                                     \n");
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

   public static synchronized Table createChnStatusTable(String Columns, String WhereClause, String Titles)
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
          String query = "Select " + Columns + " From T_Order ord, T_Domain dom " + WClause;
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
            datarow.add(new TableCol(rs.getString(2), null, null, null, null));
            datarow.add(new TableCol(IngDate.dateToStr(rs.getString(3)), null, null, null, null));
            datarow.add(new TableCol(rs.getString(4), null, null, null, null));
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

