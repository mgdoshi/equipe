
import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ClnRepForm
{
  public static Script ShowClnRepScript(String pvMode,String vPID )
  {
    ConfigData config = new ConfigData();
    Message msg = new Message();
    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    String nUserID  = Parse.GetValueFromString( vPID, "UserID" );
    String vClientName = config.GetConfigValue( "TR_REPORT", nLangID, "BL_LABEL.B_REPORT_CLIENT_NAME", "Client Name" );
		        

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode!=null && pvMode.equalsIgnoreCase("D") )
    {
      scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "     var aField           		\n" +
                  "     var vErrMsg          		\n" +
                  "     with( this.right_frame.document.forms[0] ){\n" +
                  "    	  aField = new Array( \""+ vClientName + "\",pnClientID.options[pnClientID.selectedIndex].value )\n" +
                  "   	  vErrMsg = top.check_fields( aField ) \n" +
									"       vOrdStat[0].value = \"\"             \n" +
                  "   	  if ( vErrMsg == \"\" ) {             \n" +
                  "   for( var i=1; i<vOrdStat.length; i++ )   \n" +
                  "     if( vOrdStat[i].checked )              \n" +
                  "       vOrdStat[0].value += \"~\" + vOrdStat[i].value + \"~\" \n" +
                  "   if ( confirm( \""+msg.GetMsgDesc( 15, nLangID )+"\" ) )submit()\n" +
                  "   }                                        \n" +
                  "   	  else {                               \n" +
                  "         vErrMsg = \" "+ msg.GetMsgDesc( 9, nLangID ) +"\"+ vErrMsg + \".\" \n"+
                  "   	    alert( vErrMsg )\n" +
                  "   	    return          \n " +
                  "   }	} }  \n"
                 );
    }
    scrdata.add("// End Hidding -->");
    scr.add(scrdata);
    return scr;
  }

  public static synchronized Table createClientTable(String Columns, String WhereClause, String Titles, String vPID)
  {
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";
     String clnname = null;
     String nLangID  = Parse.GetValueFromString( vPID, "LangID");


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
     Statement stmt =null;
     ResultSet rs = null;

     try
      {
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
            th = new TableHeader( "Delete", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }
          else
          {
            String []tokens = Parse.parse( Columns, ",");
            count = tokens.length;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol);
              headrow.add(th);
              th.setFormat(f);
            }
            th = new TableHeader( "Delete", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }
          conn = obj.GetDBConnection();
          stmt = conn.createStatement();
          String query = " Select " + Columns + " From T_Client cln, T_UserClient ucl " +
                         " WHERE cln.Client_ID = ucl.FK_Client_ID " + 
                         " UNION " +
                         " Select " + Columns + " From T_Client cln, T_User usr " +
                         " WHERE NOT EXISTS ( Select 1 "+
                         "                    From T_UserClient ucl " + 
                         "                    Where ucl.FK_User_ID = usr.User_ID ) ";  

          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            TableCol td2 = null;
            String id = rs.getString(1);
            String URL = "/JOrder/servlets/ClientFrame?pvMode=U&pnClientID="+id;
            Anchor anc = new Anchor( URL, "Client # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            for( int i=2; i<=count; i++)
            {
              String data =  rs.getString(i);
              if( i == 4)
                clnname = data;
              if( i==3 )
                 data = Domain.getDomainDescFrmAttrib("CLNTYPE",data, nLangID);
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            if( clnname.equals("INTERNAL") )
              td2 =  new TableCol( "&nbsp;" , null, null, null, null);
            else
            {
              FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
              td2 =  new TableCol( chkbox , null, null, null, null);
            }
            datarow.add(td2);
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
      return null;  
  }

  public static synchronized Table createClientAddrTable(HttpServletRequest request, String Columns, String WhereClause, String Titles,String vPID)
  {
   
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";
     String nLangID  = Parse.GetValueFromString( vPID, "LangID");

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
          String query = "Select " + Columns + " From T_Address adr, T_AddressRef adf " + WClause;
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
            th = new TableHeader( "Delete", null, null, null, DefHeadCol );
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
            th = new TableHeader( "Delete", null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }

          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            String refid = rs.getString(6);
            String URL = "/JOrder/servlets/ClientUpdAddress?pnAddressID="+id+"&pnRefID="+refid;
            Anchor anc = new Anchor( URL, "Address # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            String addtype = Domain.getDomainDescFrmAttrib("ADDTYPE",rs.getString(2), nLangID);
            TableCol td3 = new TableCol( addtype, null, null, null, null);
            datarow.add(td3);
            td3.setFormat(f1);
            for( int i=3; i<=count; i++)
            {
              String data = rs.getString(i);
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";                 
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
            TableCol td2 =  new TableCol( chkbox , null, null, null, null);
            datarow.add(td2);
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
      return null;  
  }
}

