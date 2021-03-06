import ingen.html.util.*;
import ingen.html.table.*;
import ingen.html.character.*;
import ingen.html.*;
import ingen.html.form.*;
import ingen.html.db.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmployeeForm
{
  public static Script ShowEmployeeScript(HttpServletRequest request,String pvMode, String pnEmployeeID , String vPID)
  {
    String vEmpName=null;
    String vBranchName=null;
    ConfigData config = new ConfigData();
    Message msg = new Message();
    String nLangID  = Parse.GetValueFromString( vPID, "LangID" );
    vEmpName    = config.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_LABEL.B_EMPLOYEE_EMPLOYEE_NAME", "Employee Name" );
    vBranchName = config.GetConfigValue( "ST_EMPLOYEE", nLangID, "BL_LABEL.B__EMPLOYEE_FK_BRANCH_ID", "Branch Name" );
    Script scr = new Script( "JavaScript", null );
    HtmlTag scrdata = new HtmlTag();
    scrdata.add("<!-- Start Hidding" + "\n");
    if( pvMode!= null && pvMode.equalsIgnoreCase("N"))
    {
     scrdata.add("  function submit_form( pvAction ) { \n"  +
                 "  var nIdx = 0\n"                         +
                 "  if ( pvAction == \"New\") {\n"          +
                 "  if ( confirm(\"" + msg.GetMsgDesc( 15,nLangID) + "\"))\n"  +
                    "    this.location.href = \"/JOrder/servlets/EmployeeFrame?pvMode=I&pnEmployeeID=\"   \n" +
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
                  "     var FuncWindow 		 	\n" + 
                  "     var vURL = \"/JOrder/servlets/ShowEmplFunc\" \n "+
                  "     if ( pvAction == \"Assign\" ) {                          \n "+
                  "        FuncWindow = window.open( vURL, \"FunWindow\", \"menubar=0,scrollbars=1,resizable=0,width=350,height=255\") \n"+
                  "        FuncWindow.oldWindow = top; 		                             \n"+
                  "        return  } \n"+
                  "     if ( pvAction == \"Create\" ) { \n" +
                  "          var vURL = \"/JOrder/servlets/EmpInsAddress\"\n" +
                  "          FuncWindow = window.open( vURL, \"FuncWindow\", \"menubar=0,scrollbars=1,resizable=0,width=600,height=400\")\n" +
                  "          FuncWindow.oldWindow = top;\n"  +
                  "          return }      		\n"  +
                  "     if ( pvAction == \"Address\" ) {\n " +
                  "       var vURL = \"/JOrder/servlets/EmpAddrTable?pnEmployeeID=" + pnEmployeeID + "\"\n" +
                  "       FuncWindow = window.open( vURL, \"FuncWindow\", \"menubar=0,scrollbars=1,resizable=0,width=600,height=400\")\n" +
                  "       FuncWindow.topWindow = top;\n" +
                  "       return  }\n"  +
                  "     with( this.right_frame.document.forms[0] ){\n" +
                  "   	  vAction.value = pvAction\n" +
                  "       var nBranchID = ( nFk_Branch_ID.type == \"select-one\" ? nFk_Branch_ID.options[nFk_Branch_ID.selectedIndex].value : nFk_Branch_ID.value ) \n"+
                  "    	  aField = new Array( \""+ vEmpName + "\", vEmployee_Name.value, \"" + vBranchName + "\", nBranchID)\n" +
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

  public static synchronized Table createEmpTable(String Columns, String WhereClause, String Titles)
  {
     String WClause=""; 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";
     String DefHeadCol =  "BGCOLOR=\"#666666\"";

     String empname = null;

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
          String query = " Select " + Columns + " From T_Employee emp, T_UserEmployee ump " +
                         " WHERE emp.Employee_ID = ump.FK_Employee_ID " + 
                         " UNION " +
                         " Select " + Columns + " From T_Employee emp, T_User usr " +
                         " WHERE NOT EXISTS ( Select 1 "+
                         "                    From T_UserEmployee ump " + 
                         "                    Where ump.FK_Employee_ID = usr.User_ID ) ";

          rs = stmt.executeQuery(query);
          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            TableCol td2 = null;
            String id = rs.getString(1);
            String URL = "/JOrder/servlets/EmployeeFrame?pvMode=U&pnEmployeeID="+id;
            Anchor anc = new Anchor( URL, "Employee # "+id, null, "_parent", null);
            TableCol td1 = new TableCol( anc, null, null, null, null);
            datarow.add(td1);
            td1.setFormat(f1);
            for( int i=2; i<=count; i++)
            {
              String data =  rs.getString(i);
              if( i == 2)
                empname = data;
              if( i==4 )
              {
                if ( data != null )
                  data = obj.getDesc( Integer.parseInt(data), "Branch" );
                else
                  data = "&nbsp;";
              }
              if( data == null || data.equals("") || data.equalsIgnoreCase("null"))
                data = "&nbsp;";
              TableCol td = new TableCol( data, null, null, null, null);
              datarow.add(td);
              td.setFormat(f1);
            }
            if( empname.equals("ADMIN") )
              td2 =  new TableCol( "&nbsp;" , null, null, null, null);
            else
            {
              FormCheckBox chkbox = new FormCheckBox("nDelete", id, null, null);
              td2 =  new TableCol( chkbox , "center", null, null, null);
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

  public static synchronized Table createEmpAddrTable(HttpServletRequest request, String Columns, String WhereClause, String Titles, String vPID)
  {
    String nLangID = Parse.GetValueFromString( vPID, "LangID" );
    String DefHeadCol =  "BGCOLOR=\"#666666\"";
    String WClause = null;
    String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";

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
            th = new TableHeader( "Delete",null, null, null, DefHeadCol );
            headrow.add(th);
            th.setFormat(f);
            table.add( headrow );
          }

          while(rs.next())
          {
            datarow = new TableRow("left", null, null);
            String id = rs.getString(1);
            String refid = rs.getString(6);
            String URL = "/JOrder/servlets/EmpUpdAddress?pnAddressID="+id+"&pnRefID="+refid;
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
            TableCol td2 =  new TableCol( chkbox , "center", null, null, null);
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

