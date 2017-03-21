import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import java.io.*;
import java.net.*;
import netscape.javascript.*;

public class TreeDemo extends Applet implements ActionListener
{
   private Font font = new Font("Arial",Font.PLAIN,10);
   Panel p;
   ScrollPane sp = new ScrollPane();
   private String target;
   private String DataSource;
   private Color Background = new Color(239,220,241);
   private Color HiBackground;
   private Color Foreground;
   private Color HiForeground;
   private Color SelForeground = new Color(255,0,0);
   IBranch current;
   IBranch cursel;
   int depth=0;
   int k=0;

   public void init()
   {
     String temp = getParameter("Target");
     if(temp!=null)
	   target = temp;
     else
      target = "_blank";

     temp = getParameter("DataSource"); 
     if(temp!=null)
       DataSource = temp;
     else
	 DataSource = "Menu.txt";

     int RGB[] = parseInt(getParameter("SelForeground")," ");
     if(RGB!=null && RGB.length==3)
     {
       SelForeground = new Color(RGB[0],RGB[1],RGB[2]);
     }
     
     RGB = parseInt(getParameter("Background")," ");
     if(RGB!=null && RGB.length==3)
     {
       Background = new Color(RGB[0],RGB[1],RGB[2]);
     }
     setBackground(Background);
 
     RGB = parseInt(getParameter("HiBackground")," ");
     if(RGB!=null && RGB.length==3)
     {
  	 HiBackground = new Color(RGB[0],RGB[1],RGB[2]);
     }
	
     RGB = parseInt(getParameter("Foreground")," ");
     if(RGB!=null && RGB.length==3)
     {
  	 Foreground = new Color(RGB[0],RGB[1],RGB[2]);
     }
     
     RGB = parseInt(getParameter("HiForeground")," ");
     if(RGB!=null && RGB.length==3)
     {
  	 HiForeground = new Color(RGB[0],RGB[1],RGB[2]);
     }

     String fontdata[]= parse(getParameter("Font")," ");
     if(fontdata!=null && fontdata.length==2)
     {
   	 font = new Font(fontdata[0],Font.PLAIN,Integer.parseInt(fontdata[1]));
     }
     setLayout(new BorderLayout());
   }

   public void sample(String str)
   {
      String[] array = parse(str,",");
      String[] Data1 = new String[array.length];
      for(int i=0;i<array.length;i++)
      {
         if(Data1[k]!=null)
           Data1[k] += array[i]+',';
         else
           Data1[k] = array[i]+',';
         if( i%4 == 3)
         {
           Data1[k] = Data1[k].substring(0,Data1[k].length()-1);
           k++;
         }
      }
       if(p!=null)  
        {
          sp.remove(p);
          remove(sp);
        }
        p = new Panel();
        p.setLayout(new GridLayout(0,1));
        current=new IBranch(null,sp,p,"Help",true,"",null,depth);
        setmenuparam(current);
        depth = depth+2;
        p.add(current);
        String data="";
         for(int i=0;i<k;i++)
         {
           data = new String(Data1[i]);
           data = data.trim();
           String tokens[] = parse(data,",");
           if(tokens[0].equals("POPUPBEGIN"))
           {
             IBranch tmp = new IBranch(current,sp,p,tokens[1].trim(),true,"",null,depth);
             setmenuparam(tmp);
             current.add(tmp);
             current = tmp;
             depth=depth+2;
           }
           else if(tokens[0].equals("MENUITEM"))
           {
             IBranch tmp = new IBranch(current,sp,p,tokens[1].trim(),false,tokens[2].trim(),tokens[3].trim(),depth); 
             setmenuparam(tmp);
             tmp.addActionListener(this);
             current.add(tmp); 
           }
           else if(tokens[0].equals("POPUPEND"))
           {
             depth=depth-2;
             IBranch tmp = current.getOwner();
             if(tmp!=null)
               current = tmp;
             else
               break;
           }
           else
           { 
           }
	} 
       sp.add(p);
       add(sp,"Center");
       validate();
   }

   public void createTree(JSObject str)
   {
        if(p!=null)  
        {
          sp.remove(p);
          remove(sp);
        }
        p = new Panel();
        p.setLayout(new GridLayout(0,1));
        current=new IBranch(null,sp,p,"Help",true,"",null,depth);
        setmenuparam(current);
        depth = depth+2;
        p.add(current);
        Double len = (Double)str.getMember("length");
        int n = (int)Math.round(len.doubleValue());
        String data="";
         for(int i=0;i<n;i++)
         {
           data = new String((String)str.getSlot(i));
           data = data.trim();
           String tokens[] = parse(data,",");
           if(tokens[0].equals("POPUPBEGIN"))
           {
             IBranch tmp = new IBranch(current,sp,p,tokens[1].trim(),true,"",null,depth);
             setmenuparam(tmp);
             current.add(tmp);
             current = tmp;
             depth=depth+2;
           }
           else if(tokens[0].equals("MENUITEM"))
           {
             IBranch tmp = new IBranch(current,sp,p,tokens[1].trim(),false,tokens[2].trim(),tokens[3].trim(),depth); 
             setmenuparam(tmp);
             tmp.addActionListener(this);
             current.add(tmp); 
           }
           else if(tokens[0].equals("POPUPEND"))
           {
             depth=depth-2;
             IBranch tmp = current.getOwner();
             if(tmp!=null)
               current = tmp;
             else
               break;
           }
	} 
       sp.add(p);
       add(sp,"Center");
       validate();
   }

   public void startWith(String args)
   {
      if(args==null)
        return;
      String name[] = parse(args,"|");
      IBranch tmp = current;
      int i=0;
      if(name!=null)
      {
         tmp.setCollapse(true);
         for(i=0;i<name.length-1;i++)
         {
            IBranch tmp1 = tmp.getBranch(name[i]);
            if(tmp1==null)
               return;
            else
            {
               System.out.println(name[i]);   
               tmp1.setCollapse(true);
               tmp = tmp1;
            }
         }
         tmp.getBranch(name[i]).Status=1;
         if(IBranch.currentnode!=null) 
           IBranch.currentnode.Status=0;   
         IBranch.currentnode=tmp.getBranch(name[i]);
         p.validate();
         sp.validate();
         repaint();  
         String str = tmp.getBranch(name[i]).getUrl();
         try
         {
           URL url = new URL (getDocumentBase(),str);
           getAppletContext().showDocument(url,target);
           if(cursel!=null)
           {        
             cursel.setForeground(Foreground);
             cursel.setHiForeground(HiForeground);   
             cursel.repaint();
           }
           cursel = tmp.getBranch(name[i]);  
           tmp.getBranch(name[i]).setForeground(SelForeground);   
           tmp.getBranch(name[i]).setHiForeground(SelForeground);   
           tmp.getBranch(name[i]).repaint();
           
        }catch(MalformedURLException url){ System.out.println(url);}
      }
   }

   public void setmenuparam(IBranch current)
   {
     if(current!=null)
     {
       current.setForeground(Foreground);
       current.setHiForeground(HiForeground);
       current.setHiBackground(HiBackground);
       current.setFont(font);
     }
   }

   public void stop()
   {
      if( IBranch.currentnode != null )
        IBranch.currentnode.Status = 0;
      IBranch.currentnode = null;
   }

   public void actionPerformed(ActionEvent evt)
   {
      String str = ((IBranch)evt.getSource()).getUrl();
      try
      {
        URL url = new URL (getDocumentBase(),str);
        getAppletContext().showDocument(url,target);
        if(cursel!=null)        
        {
           cursel.setForeground(Foreground);
           cursel.setHiForeground(HiForeground);   
           cursel.repaint();
        }
        cursel = (IBranch)evt.getSource();  
        cursel.setForeground(SelForeground);
        cursel.setHiForeground(SelForeground);   
        cursel.repaint();
      }catch(MalformedURLException url){ System.out.println(url);}
   }

   int[] parseInt(String s, String sep)
   {  
     if(s!=null && sep!=null)
     {
       StringTokenizer st = new StringTokenizer(s, sep);
	 int[] result = new int[st.countTokens()];
	 for (int i=0; i<result.length; i++)
	 {
	   result[i] = Integer.parseInt(st.nextToken());
	 }
	 return result;
     }
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
}
