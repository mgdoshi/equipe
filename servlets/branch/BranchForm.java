import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class BranchForm
{
  public static Script ShowBranchScript(HttpServletRequest request,String pvMode, String pnBranchID, String vPID )
  {
    String vBranchName=null;
    ConfigData config = new ConfigData();
    Message msg = new Message();
    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    vBranchName = config.GetConfigValue( "ST_BRANCH", nLangID, "BL_LABEL.B_BRANCH_BRANCH_NAME", "Branch Name" );

    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode==null || pvMode.equals("") || pvMode.equalsIgnoreCase("null"))
    {
     scrdata.add("  function submit_form( pvAction ) { \n"  +
                 "  var nIdx = 0\n"                         +
                 "  if ( pvAction == \"New\") {\n"          +
                 "  if ( confirm(\"" + msg.GetMsgDesc( 15,nLangID) + "\"))\n"  +
                    "    this.location.href = \"/JOrder/servlets/BranchFrame?pvMode=I&pnBranchID=\"   \n" +
                    "}\n" +
                    " with( this.right_frame.document.forms[0] ) { \n" +
                    "  nDelete[0].value=\"\"              \n"  + 
                    "   if ( pvAction == \"Delete\") {             \n" +
                    "     for( var i=1; i<nDelete.length; i++ )  { \n" +
                    "       if( nDelete[i].checked ) {             \n" +
                    "         nIdx ++                              \n" +
                    "         nDelete[0].value += nDelete[i].value + \"~\"\n" +
                    "       } }\n" +
                    "    if( nIdx == 0 ) {\n"  +
                    "      alert(\"" + msg.GetMsgDesc( 62, nLangID ) + "\")\n" +
                    "      return\n" +
                    "    }\n" +
                    "    if ( confirm( \"" + msg.GetMsgDesc(11,nLangID) + "\")){\n" +
                    "      submit()\n" +
                    "    } } } } \n"
                 );

    }
    else
    {
      scrdata.add(" function submit_form( pvAction ) {	\n" +
                  "     var aField           		\n" +
                  "     var vErrMsg          		\n" +
                  "     if ( pvAction == \"Create\" ) { \n" +
                  "          var vURL = \"/JOrder/servlets/BranchInsAddress\"\n" +
                  "          FuncWindow = window.open( vURL, \"FuncWindow\", \"menubar=0,scrollbars=1,resizable=0,width=600,height=400\")\n" +
                  "          FuncWindow.oldWindow = top;\n"  +
                  "          return }      		\n"  +
                  "     if ( pvAction == \"Address\" ) {\n " +
                  "       var vURL = \"/JOrder/servlets/BranchAddrTable?pnBranchID=" + pnBranchID + "\"\n" +
                  "       FuncWindow = window.open( vURL, \"FuncWindow\", \"menubar=0,scrollbars=1,resizable=0,width=600,height=400\")\n" +
                  "       FuncWindow.topWindow = top;\n" +
                  "       return  }\n"  +
                  "     with( this.right_frame.document.forms[0] ){\n" +
                  "   	  vAction.value = pvAction\n" +
                  "    	  aField = new Array( \"" + vBranchName + "\",vBranch_Name.value)\n" +
                  "   	  vErrMsg = top.check_fields( aField ) \n" +
                  "   	  if ( vErrMsg == \"\" ) {             \n" +
                  "   	    if ( ( pvAction == \"SaveInsert\" || pvAction == \"Insert\" || pvAction == \"Update\" ) && confirm( \"" + msg.GetMsgDesc( 10, nLangID ) + "\"))\n" +
                  "   	      submit() }\n" +
                  "   	  else {\n" +
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

  public static synchronized Table createBranchTable(String Columns, String WhereClause, String Titles, String vPID )
  {
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";

     String nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
     String vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );

     if( WhereClause != null )
       WClause = WhereClause;

     Table table = new Table( null, "Center", Default);
     TableRow headrow = new TableRow("left", null, null);
     TableHeader th = null;
     TableRow datarow = null;
     int count = 0;

     Font f = new Font("white", "Arial", "3",null);
     DBConnect obj = new DBConnect();
     Connection conn = null;
     Statement stmt = null;
     ResultSet rs = null;
     WebUtil util = new WebUtil(); 

     try
      {
          if( Titles != null )
          {
            String []tokens = Parse.parse( Titles, ",");
            count = tokens.length;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader( tokens[i], null, null, null, DefHeadCol );
              th.setFormat(f);
              headrow.add(th);
            }
            th = new TableHeader("Delete", null, null, null, DefHeadCol );
            th.setFormat(f);
            headrow.add(th);
            table.add( headrow );
          }
          else
          {
            String []tokens = Parse.parse( Columns, ",");
            count = tokens.length;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader(util.createLabelItem(tokens[i],vLabelAttrib),null, null, null, DefHeadCol);
              headrow.add(th);
            }
            th = new TableHeader("Delete", null, null, null, DefHeadCol );
            th.setFormat(f);
            headrow.add(th);
            table.add( headrow );
          }

          conn = obj.GetDBConnection();
          stmt = conn.createStatement();
          String query = "Select " + Columns + " From T_Branch " + WClause;
          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            String URL = "/JOrder/servlets/BranchFrame?pvMode=U&pnBranchID="+id;
            Anchor anc = new Anchor( URL, util.createLabelItem( "Branch # "+id, " COLOR=\"BLUE\" " + vLabelAttrib ), null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            for( int i=2; i<=count; i++)
            {
              String data = rs.getString(i);
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";                 
              TableCol td = new TableCol( util.createLabelItem( data, vLabelAttrib ), null, null, null, null);
              datarow.add(td);
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

   public static synchronized Table createBranchAddrTable(HttpServletRequest request, String Columns, String WhereClause, String Titles, String vPID)
   {
     String nSchemeID = Parse.GetValueFromString( vPID, "SchemeID" );
     String vLabelAttrib = Scheme.getSchemeProperty( nSchemeID, 'A', 'N' );

     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";

     if( WhereClause != null )
       WClause = WhereClause;

     Table table = new Table( null, "Center", Default);
     TableRow headrow = new TableRow("left", null, null);
     TableHeader th = null;
     TableRow datarow = null;
     int count = 0;

     Font f = new Font("white", "Arial", "3",null);
     WebUtil util = new WebUtil();
     DBConnect obj = new DBConnect();
     Connection conn = null;
     Statement stmt = null;
     ResultSet rs = null;

     try
      {
          conn = obj.GetDBConnection();
          stmt = conn.createStatement();
          String query = "Select " + Columns + " From T_Address adr, T_AddressRef adf, T_Domain dom " + WClause;
          rs = stmt.executeQuery(query);

          if( Titles != null )
          {
            String []tokens = Parse.parse( Titles, ",");
            count = tokens.length;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader(tokens[i],null, null, null, DefHeadCol);
              th.setFormat(f);
              headrow.add(th);
            }
            th = new TableHeader("Delete", null, null, null, DefHeadCol );
            th.setFormat(f);
            headrow.add(th);
            table.add( headrow );
          }
          else
          {
            String []tokens = Parse.parse( Columns, ",");
            count = tokens.length-2;
            for( int i=0; i<count; i++)
            {
              th = new TableHeader(tokens[i],null, null, null, DefHeadCol);
              th.setFormat(f);
              headrow.add(th);
            }
            th = new TableHeader("Delete", null, null, null, DefHeadCol );
            th.setFormat(f);
            headrow.add(th);
            table.add( headrow );
          }

          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            String refid = rs.getString(6);
            String URL = "/JOrder/servlets/BranchUpdAddress?pnAddressID="+id+"&pnRefID="+refid;
            Anchor anc = new Anchor( URL,util.createLabelItem("Address # "+id,  " COLOR=\"BLUE\" " + vLabelAttrib ), null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            for( int i=2; i<=count; i++)
            {
              String data = rs.getString(i);
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";                 
              TableCol td = new TableCol(util.createLabelItem(data, vLabelAttrib), null, null, null, null);
              datarow.add(td);
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

