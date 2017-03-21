package ingen.html.db;

import ingen.html.util.*;
import java.sql.*;

public class IngDate extends java.sql.Date
{
  public IngDate()
  {
    super(System.currentTimeMillis()); 
  }

  public static Date strToDate(String dt)
  {
    Date retval=null;
    String tmp[] = Parse.parse(dt,".");
    if(tmp.length==3)
    {
      String result = tmp[2]+'-'+tmp[1]+'-'+tmp[0];
      retval = Date.valueOf(result);  
    }
    return retval; 
  }

  public static String dateToStr(Date dt)
  {
    String retval=null;
    String tmp[] = Parse.parse(dt.toString(),"-");
    if(tmp.length==3)
    {
      retval = tmp[2]+'.'+tmp[1]+'.'+tmp[0];
    }
    return retval; 
  } 

  public static String dateToStr(String dt)
  {
    String retval=null;
    if(dt!=null)
    { 
      dt = dt.trim();
      dt = dt.substring(0,dt.indexOf(' ',0));      
      String tmp[] = Parse.parse(dt,"-");
      if(tmp.length==3)
      {
        retval = tmp[2]+'.'+tmp[1]+'.'+tmp[0];
      }
    }  
    return retval; 
  } 

}
