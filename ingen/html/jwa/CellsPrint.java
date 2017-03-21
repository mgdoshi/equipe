package ingen.html.jwa;

import ingen.html.character.*;
import ingen.html.table.*;
import ingen.html.db.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class CellsPrint
{
     String Default  = "";
     String DefHeadCol =  "";
     String WhereClause = "";
     public Table printCell( String query, int nRows, String Format, int startPos )
     {
        Table table = new Table( null, "Center", Default);
        Font f = new Font("white", "Arial", "3",null);
        try
        { 
             DBConnect obj = new DBConnect();
             Connection conn = obj.GetDBConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query);
             int count = rs.getMetaData().getColumnCount();     
             int cpos=1; 
             while(rs.next())
             {
                if(cpos<startPos)
                {
                  cpos++;
                  continue;
                }
                else if(cpos==startPos+nRows)
                  break;  
                else
                {
                  cpos++;
                  TableRow datarow = new TableRow("left", null, null);
                  for( int i=1; i<=count; i++)
                  {
                    TableCol td = new TableCol( rs.getString(i), null, null, null, null);
                    datarow.add(td);
                  } 
                  table.add( datarow );                  
                }             
             }
             rs.close();
             st.close();
             conn.close(); 
             return table; 
        }catch(Exception sexe){System.out.println(sexe);}
        return null;  
    }

    String[] parse(String s, String sep)
	{
		if(s!=null && sep!=null)
		{
			StringTokenizer st = new StringTokenizer(s, sep);
			String result[] = new String[st.countTokens()];
			for (int i=0; i<result.length; i++)
			{
				result[i] = st.nextToken();
			}
			return result;
		}
		return null;
    }
    
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