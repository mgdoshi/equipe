import ingen.html.table.*;
import ingen.html.form.*;
import ingen.html.character.*;
import java.sql.*;
import java.util.*;
import ingen.html.*;
import ingen.html.db.*;
import ingen.html.util.*;

public class Test
{
 public static void main(String args[])
 { 
   
   Vector table = new Vector();
   TableRow datarow = new TableRow("left", null, null);
   table.addElement(datarow);
   table.addElement(datarow);
   ((TableRow)table.elementAt(0)).add("HI");
   ((TableRow)table.elementAt(1)).add("HI HI");
   if(table.elementAt(0).equals(datarow))
     System.out.println(table.elementAt(0));
   table.removeElementAt(0);
   if(table.elementAt(0).equals(datarow))
     System.out.println(table.elementAt(0));
  }
}
