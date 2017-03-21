package ingen.html.jwa;

import ingen.html.character.*;
import ingen.html.table.*;
import ingen.html.db.*;
import ingen.html.util.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;

/**
This class can be used to create a HTML table containg a data retrived from
sql query.  
*/
public class TablePrint
{
     /**
     Default Table Color and border attributes.
     */ 
     String Default  = "BORDER=0 BGCOLOR=\"#FFFFCA\"";

     /**
     Default Table Header Color.
     */ 
     String DefHeadCol =  "BGCOLOR=\"#666666\"";

     /**
     Where clause for sql query.
     */
     String WhereClause = "";

      /**
      <B><U>SUMMARY:</U></B><BR>
      This class can be used to create a HTML table containg a data retrived from sql query.  
      <BR><BR>
      <B><U>PARAMETERS</U></B><BR>
      <PRE>
      1) String tableName   : Name of a table.
      2) String Attribs     : Table Attributes like border, color and alignment 
                              can be specified in attributes.
      3) String Columns     : Comma seperated list of column names.
      4) String WhereClause : The where clause for sql query.
      5) String Titles      : Comma seperated list of Titles as alias for column names.
      </PRE><BR>
      <B><U>GENERATES</U></B><BR>
      &lt;TABLE Name="tableName" Attribs&gt; Data retrived by query in row column format with column heading&lt;/TABLE&gt;<BR>
      */
     public Table createTable( String tableName, String Attribs, String Columns, String WhereClause, String Titles)
     {
        if( Attribs != null )
           Default = Attribs;
        if( WhereClause != null )
           this.WhereClause = WhereClause;
        if( Columns == null )
           Columns = "*";
        Table table = new Table( null, "Center", Default);
        Font f = new Font("white", "Arial", "3",null);
        TableRow headrow = new TableRow("left", null, null);
        String query = "Select " + Columns + " From " + tableName + " " + WhereClause;
        String []tokens = Parse.parse( Titles, ",");
        int count = tokens.length;
        for( int i=0; i<count; i++)
        {
          TableHeader th = new TableHeader( tokens[i], null, null, null, DefHeadCol );
          headrow.add(th);
          th.setFormat(f);
        }        
        table.add( headrow ); 
        try
        { 
             DBConnect obj = new DBConnect();
             Connection conn = obj.GetDBConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query);
             while(rs.next())
             {
                TableRow datarow = new TableRow("left", null, null);
                for( int i=1; i<=count; i++)
                {
                    TableCol td = new TableCol( rs.getString(i), null, null, null, null);
                    datarow.add(td);
                } 
                table.add( datarow );                  
             }             
             rs.close();
             st.close();
             conn.close(); 
             return table; 
        }catch(Exception sexe){System.out.println(sexe);}
        return null;  
    }


     public Table createTable( String tableName, String Attribs, String WhereClause )
     {
        if( Attribs != null )
           Default = Attribs;
        if( WhereClause != null )
           this.WhereClause = WhereClause;
        Table table = new Table( null, "Center", Default);
        Font f = new Font("white", "Arial", "3",null);
        TableRow headrow = new TableRow("left", null, null);
        String query = "Select *  From " + tableName + " " + WhereClause;
        try
        { 
             DBConnect obj = new DBConnect();
             Connection conn = obj.GetDBConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query);
             ResultSetMetaData rsmd = rs.getMetaData();
             int count = rsmd.getColumnCount();
             for( int i=1; i<=count; i++)
             {
                String Name = rsmd.getColumnName( i );
                TableHeader th = new TableHeader( Name, null, null, null, DefHeadCol);
                headrow.add(th);
                th.setFormat(f);
             }
             table.add( headrow );                  
             while(rs.next())
             {
                TableRow datarow = new TableRow("left", null, null);
                for( int i=1; i<=count; i++)
                {
                    TableCol td = new TableCol( rs.getString(i), null, null, null, null);
                    datarow.add(td);
                } 
                table.add( datarow );                  
             }             
             rs.close();
             st.close();
             conn.close(); 
             return table; 
        }catch(Exception sexe){System.out.println(sexe);}
        return null;  
    }

    /**
    <B><U>SUMMARY:</U></B><BR>
      Function can be used to overide default column heading color.
    <BR><BR>
    <B><U>PARAMETERS</U></B><BR>
    <PRE> String Color  : color name or hex value of the color to be used for
                          overiding default header color.
    </PRE>
    */
    public void setHeaderColor( String color )
    {
       if( color != null)
       {
          String temp = color.toUpperCase();
          if(temp.startsWith("BGCOLOR="))   
             DefHeadCol = color;
          else
             DefHeadCol = "BGCOLOR=" + color;
       }
    }
}