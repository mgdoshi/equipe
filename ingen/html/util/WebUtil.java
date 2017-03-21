package ingen.html.util;

import ingen.html.list.*;
import ingen.html.para.*;
import ingen.html.*;
import ingen.html.head.*;
import ingen.html.form.*;
import ingen.html.character.*;
import ingen.html.db.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

public class WebUtil
{
   public static Font NotNull=null;

   static
   {
      NotNull = new Font("Red","Arail", "4", null);
      Bold b = new Bold();
      b.add("&nbsp*"); 
      NotNull.add(b);     
   }

   public Object GetBoilerPlate( String option, String imgname, String bpname, String attrib)
   {
     if(option!=null && option.equalsIgnoreCase("ON")) 
       return (new Img( imgname, "Center", bpname, null, null));    
     Font font = new Font( null, null, null,attrib);
     font.add(bpname);
     return font; 
   }

   public HtmlTag createLabelItem( String label, String labelAttrib )
   {
     Font font = new Font( null, null, null, labelAttrib );
     font.add(label);
     return font;
   }

   public Anchor createLink( String nUserID, String objParentName, String objName, String Mode, String Url, String imgName, String imgAlt, String statusText)
   {
     Anchor retVal=null; 
     try
     {
       int id = Integer.parseInt(nUserID.trim());
       retVal = createLink( id,objParentName,objName,Mode,Url,imgName,imgAlt,statusText);
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retVal; 
   }

   public Anchor createLink( int nUserID, String objParentName, String objName, String Mode, String Url, String imgName, String imgAlt, String statusText)
   {
      int nSecStat;
      String vImgURL;
      String vOnMouseOver;
      String vOnMouseDown;
      String vOnMouseUp;
      String vOnMouseOut;
      Anchor anc = null;
      nSecStat = ObjSec.chkObjSec( Integer.toString(nUserID), objParentName, objName, Mode );
      vImgURL      = "/ordimg/BP_Button/"+imgName+"1.gif";
      vOnMouseOver = " onMouseOver = \"return top.show_status( '"+statusText+"')\" ";
      vOnMouseDown = " onMouseDown = \"document."+imgName+".src = top."+imgName+"[1].src\" ";
      vOnMouseUp   = " onMouseUp = \"document."+imgName+".src = top."+imgName+"[0].src\" ";
      vOnMouseOut  = " onMouseOut = \"return top.show_status('')\" ";
      if (nSecStat <= 0)
        anc =  new Anchor(Url,new Img( vImgURL, null, imgAlt, null, "BORDER=\"0\" NAME=\""+imgName+"\"" ), objName, null,vOnMouseOver+vOnMouseDown+vOnMouseUp+vOnMouseOut );
      return anc; 
   }

   public HtmlTag createList( String nUserID, String objName, String name, String Mode, String query, String selValue, String listAttrib, String fontAttrib)
   {
     HtmlTag retVal=null; 
     try
     {
       int id = Integer.parseInt(nUserID.trim());
       retVal = createList( id,objName,name,Mode,query,selValue,listAttrib,fontAttrib);
     }catch(NumberFormatException nexe){System.out.println(nexe);}
     return retVal; 
   }

   public HtmlTag createList( int nUserID, String objName, String name, String Mode, String query, String selValue, String listAttrib, String fontAttrib)
   {
      int nSecStat;
      HtmlTag retval=null;
      nSecStat = ObjSec.chkObjSec( Integer.toString(nUserID), objName, name, Mode );
      Font font = new Font( null,null,null, fontAttrib);

      if( nSecStat <= 0 || ( nSecStat == 3 && selValue==null) )
      {
        try
        {
          FormSelect list = new FormSelect(name,null,listAttrib);
          FormSelectOption option1=null;
          DBConnect obj = new DBConnect();
          Connection conn = obj.GetDBConnection();
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(query);
          option1 = new FormSelectOption("",null,null,null);
          list.add(option1);
          while(rs.next())
          {
            FormSelectOption option=null;
            String temp = rs.getString(1);
            if(temp.equals(selValue))
              option = new FormSelectOption(temp,rs.getString(2),"Selected",null);  
            else   
              option = new FormSelectOption(temp,rs.getString(2),null,null);  
            list.add(option);
          }
          stmt.close();
          conn.close();
          font.add(list);
          retval = font; 
        }
        catch(Exception e)
        {
	  System.out.println("Exception Occures	"+e);
	  System.out.println("Exception Occures	"+e.getMessage());
        }
      }
      else if ( nSecStat >= 1 && nSecStat <= 3)
      {
         if( selValue==null )
           retval = (HtmlTag)new FormHidden(name,selValue,null); 
         else
         {
           try
           {
              DBConnect obj = new DBConnect();
              Connection conn = obj.GetDBConnection();
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(query);
              while(rs.next())
              {
                String temp = rs.getString(1);
                if(temp.equals(selValue))
                {   
                  font.add(rs.getString(2));
                  font.add(new FormHidden(name,selValue,null)); 
                  retval = font;
                  break;
                } 
              }
              stmt.close();
              conn.close();
          }
          catch(Exception e)
          {
	    System.out.println("Exception Occures	"+e);
	    System.out.println("Exception Occures	"+e.getMessage());
          }
        }
        
      }
      return retval; 
    }

    public HtmlTag createTextItem( String nUserID, String parent, String name, String Mode, String size, String maxsize, String value, String textAttrib, String fontAttrib )
    {
      HtmlTag retVal=null; 
      try
      {
        int id = Integer.parseInt(nUserID.trim());
        retVal = createTextItem( id,parent,name,Mode,size,maxsize,value,textAttrib,fontAttrib);
      }catch(NumberFormatException nexe){System.out.println(nexe);}
      return retVal; 
    }

    public HtmlTag createTextItem( int nUserID, String parent, String name, String Mode, String size, String maxsize, String value, String textAttrib, String fontAttrib )
    {
      int nSecStat;
      HtmlTag retval=null;
      FormText textbox = null;
      nSecStat = ObjSec.chkObjSec( Integer.toString(nUserID), parent, name, Mode );

      Font font = new Font( null,null,null, fontAttrib); 
      if( nSecStat <= 0 || ( nSecStat == 3 && value==null) )
      { 
        font.add(new FormText(name,size,maxsize,value,textAttrib));
        retval = font; 
      }
      else if ( nSecStat == 1 )
      {
        retval = (HtmlTag)new FormHidden(name,"",null); 
      }
      else if (nSecStat== 2 || ( nSecStat== 3 && value!=null )) 
      { 
        font.add(value); 
        font.add(new FormHidden(name,value,null));
        retval = font;
      }
      return retval;
    } 


    public HtmlTag createPasswdItem( String nUserID, String parent, String name, String Mode, String size, String maxsize, String value, String textAttrib, String fontAttrib )
    {
      HtmlTag retVal=null; 
      try
      {
        int id = Integer.parseInt(nUserID.trim());
        retVal = createPasswdItem( id,parent,name,Mode,size,maxsize,value,textAttrib,fontAttrib);
      }catch(NumberFormatException nexe){System.out.println(nexe);}
      return retVal; 
    } 

    public HtmlTag createPasswdItem( int nUserID, String parent, String name, String Mode, String size, String maxsize, String value, String textAttrib, String fontAttrib )
    {
      int nSecStat;
      HtmlTag retval=null;
      FormText textbox = null;
      nSecStat = ObjSec.chkObjSec( Integer.toString(nUserID), parent, name, Mode );

      Font font = new Font( null,null,null, fontAttrib); 
      if( nSecStat <= 0 || ( nSecStat == 3 && value==null) )
      { 
        font.add(new FormPassword(name,size,maxsize,value,textAttrib));
        retval = font;
      }
      else if ( nSecStat == 1  || nSecStat== 2 || ( nSecStat== 3 && value!=null ) )
      {
        font.add(value); 
        font.add(new FormHidden(name,value,null));
        retval = font;
      }
      return retval;
    }


    public HtmlTag createTextAreaItem( String nUserID, String parent, String name, String Mode, String rows, String cols, String value, String textAttrib, String fontAttrib )
    {
      HtmlTag retVal=null; 
      try
      {
        int id = Integer.parseInt(nUserID.trim());
        retVal = createTextAreaItem( id,parent,name,Mode,rows,cols,value,textAttrib,fontAttrib);
      }catch(NumberFormatException nexe){System.out.println(nexe);}
      return retVal; 
    }
 
    public HtmlTag createTextAreaItem( int nUserID, String parent, String name, String Mode, String rows, String cols, String value, String textAttrib, String fontAttrib )
    {
      int nSecStat;
      HtmlTag retval=null;
      FormText textbox = null;
      nSecStat = ObjSec.chkObjSec( Integer.toString(nUserID), parent, name, Mode );

      Font font = new Font( null,null,null, fontAttrib); 
      if( nSecStat <= 0 || ( nSecStat == 3 && value==null) )
      { 
        FormTextArea txt = new FormTextArea( name, rows, cols, null, "WRAP=\"Virtual\" "+textAttrib);
        txt.add(value);
        font.add(txt);
        retval = font;
      }
      else if ( nSecStat == 1  || nSecStat== 2 || ( nSecStat== 3 && value!=null ) )
      {
        font.add(value);
        font.add(new FormHidden( name, value, null));
        retval = font;
      }
      return retval;
    }

    public static Page IllegalEntry()
    {
      Page page = new Page(); 
      Head head = new Head();
      head.add(new Title("Illegal Entry"));   
      page.add(head); 

      Body body = new Body();
      Center cen = new Center();
      cen.setFormat(new Font("Red","Arail", "6", null ));
      Blink bln = new Blink("Illegal Entry!");
      cen.add(bln);  
      Para para = new Para();
      UnOrderList list = new UnOrderList(); 
      para.add(cen);   
      para.add(" <b> <font color=\"#3333FF\"> <font size=+2> &nbsp;&nbsp;&nbsp;</font><font size=+3>Possible Causes : </font> </font> </b>");
      list.add(new ListItem("<b><font color=\"#000000\"><font size=+2> You might have tried to access the 'ORDER TRACKING SYSTEM' without properly login on to the system. </font></font></b>"));
      list.add(new ListItem("<b> <font color=\"#000000\"> <font size=+2> You might have set the 'Disable cookies' option in 'Preferences setting' of browser.</font> </font> </b> ") );
      list.add(new ListItem("<b> <font color=\"#000000\"> <font size=+2> Your Browser might be incompatible to support the cookies.</font> </font> </b> ") );
      para.add(list);
      body.add(para);
      page.add(body);
      return page;
    }    

    public static Page ShowException(String errMsg, int nMsgNo, String nLangID)
    {
      Page retVal=new Page(); 
      try
      {
        int id = Integer.parseInt(nLangID.trim());
        retVal = ShowException(errMsg,nMsgNo,id);
      }catch(NumberFormatException nexe){System.out.println(nexe);}
      return retVal; 
    }
    public static Page ShowException(String errMsg, int nMsgNo, int nLangID)
    {
      Message msg = new Message();  
      Page page = new Page(); 
      Head head = new Head();
      head.add(new Title("Illegal Entry"));   
      page.add(head); 

      Body body = new Body();
      Center cen = new Center();
      cen.setFormat(new Font("Red","Arail", "6", null ));
      Blink bln = new Blink("Exception!");
      cen.add(bln);  
      Para para = new Para();
      UnOrderList list = new UnOrderList(); 
      para.add(cen);   
      para.add(" <b>&nbsp;&nbsp;&nbsp;<font size=+3>Exception : </font> </b>");
      list.add(new ListItem("<b><font color=\"#000000\" size=+2> "+errMsg+" </font></b>"));
      list.add(new ListItem("<b><font color=\"#000000\" size=+2> "+msg.GetMsgDesc(nMsgNo,nLangID)+" </font></b>"));
      para.add(list);
      body.add(para);
      page.add(body);
      return page;
    }    

    public static String getBlankSpaces(int ncount)
    {
      String retVal="";
      for(int i=0;i<ncount;i++) 
      {
         retVal += "&nbsp;";
      }
      return retVal;    
    }
}