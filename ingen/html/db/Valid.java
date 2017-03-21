package ingen.html.db;

import java.sql.*;
import java.io.*;

public class Valid
{
   private String objDesc = null;
   public String IsNull( String par )
   {
     if( par == null || par.trim().equals("") || par.trim().equalsIgnoreCase("null"))
       return null;
     else
       return "'"+par+"'";
   }

}

