package ingen.html.db;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Message
{
   public String GetMsgDesc(int MsgNo, String langID)
   {
     String retval=""; 
     try
     { 
       int langid = Integer.parseInt(langID.trim());
       retval = GetMsgDesc(MsgNo,langid);
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retval;
   }
   public String GetMsgDesc(int MsgNo, int langID)
   {
     String msgDesc=""; 
     Statement stmt = null;
     Connection conn = null;
     ResultSet rs = null;
     try
     {
       DBConnect obj = new DBConnect();
       conn = obj.GetDBConnection();
       String sql = "  Select Msg_Desc " +
                    "  From   T_Msg"+
                    "  Where  Msg_ID = " + MsgNo +
                    "  And    FK_Lang_ID = " + langID;
       stmt = conn.createStatement();
       rs = stmt.executeQuery(sql);
       if(rs.next())
       {
         msgDesc = rs.getString(1);
       }
       stmt.close();
       conn.close();
     }catch(Exception sexe){System.out.println(sexe);msgDesc=null;}
     return msgDesc;
   }
}

