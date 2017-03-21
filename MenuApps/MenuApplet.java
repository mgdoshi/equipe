/*
-------------------------------------------------------------------------------------------------
Warning  : This Computer Program is protected by copyright law and international treaties.
           Unauthorised reproduction or distribution of this program , or any portion of it,
           may result in severe civil and criminal penalties , and will be prosecuted to the 
	   maximum extent possible under the law.

File Name: MenuApplet.java
Referenced Files: IMenu.java, IMenuItem.java
Revision : 
By 		         Date						 Defect No.	Reasons
Manoj Doshi	   31-08-99 Tuesday 6:09:39 PM	  00001	    CRE:Created.

CRE : Created
BFX : Bug fix
RAC : Requirement anlaysis changing
UPD : Upgradation
------------------------------------------------------------------------------------------------
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.applet.*;
import java.net.*;

public class MenuApplet extends Applet implements ActionListener 
{
   public IStatusBar sBar=null;
   private IPane btn[] = new IPane[4];
   private static int count=0;
   private static int count1=0;
   Vect  vmain=new Vect();
   Vector FullMenu=new Vector();	
   Vector vtemp=new Vector();

   BufferedReader in;
   String DataSource;
   Image subMenu;
   IMenu im;
   int loginflag=0;
   Vector AllMenus=new Vector();
   Color defaultshbg=new Color(0,0,132);
   Color defaultshfg=new Color(255,255,255);
   Color defaultbg=new Color(208,208,208);
   Color defaultfg=new Color(0,0,0);
   Color background=null;
   Color foreground=null;
   Color shadowColor=null;
   Color shadowForeground=null;
   Panel p;
   boolean minimiseflag=true;
   static int localBrowser=-999;

   public void setBrowser(String browser)
   {
	  localBrowser=Integer.parseInt(browser);
   }	  
   public void Logout()
   {
		closeAll();
		AllMenus.removeAllElements();
		FullMenu.removeAllElements();
		vmain.removeAllElements();
		vtemp.removeAllElements();
		startFileReading();
		createFullMenu();
		createWindows();
   }

   public void init()
   {
      String temp=getParameter("DataSource");
      if(temp!=null)
        DataSource = temp;
      else
        DataSource = "Menu.txt";
      String bkground=getParameter("bgcolor");
      String frground=getParameter("fgcolor");
      String shbground=getParameter("shbgcolor");
      String shfground=getParameter("shfgcolor");
      sBar = new IStatusBar();

      p = new Panel();
      p.setLayout(new GridLayout(35,1));
      add(p,"South");

      btn[0] = new IPane(IPane.BEVELED,IPane.LEFT);
      btn[0].setSize((getSize().width-getInsets().left-getInsets().right-10)/16,20);
      btn[0].addActionListener(this);
      sBar.add(btn[0]);
      sBar.setText(0,"  Login");

      btn[1] = new IPane(IPane.LOWERD,IPane.LEFT);
      btn[1].setSize(((getSize().width-getInsets().left-getInsets().right-10)*29)/48,20);
      sBar.add(btn[1]);
 	
      btn[2] = new IPane(IPane.LOWERD,IPane.LEFT);
      btn[2].setSize((getSize().width-getInsets().left-getInsets().right-10)/6,20);
      sBar.add(btn[2]);
 		
      btn[3] = new IPane(IPane.LOWERD,IPane.LEFT);
      btn[3].setSize((getSize().width-getInsets().left-getInsets().right-10)/6,20);
      sBar.add(btn[3]);
      sBar.setText(3," © Ingenium Computing.");
      add(sBar);

      String r=null,g=null,b=null;
      if(shfground!=null)
      {
		 StringTokenizer tk=new StringTokenizer(shfground,",");
		 while(tk.hasMoreTokens())
		 {
			r=(String)tk.nextElement();
			g=(String)tk.nextElement();
			b=(String)tk.nextElement();
		 }
      }
      if( r!=null && g!=null && b!=null)
      {
		 shadowForeground=new Color(Integer.parseInt(r),Integer.parseInt(g),Integer.parseInt(b));
      }
      else
      {
		 shadowForeground=defaultshfg;
      }

      if(shbground!=null)
      {
		 StringTokenizer tk=new StringTokenizer(shbground,",");
		 while(tk.hasMoreTokens())
		 {
			r=(String)tk.nextElement();
			g=(String)tk.nextElement();
			b=(String)tk.nextElement();
		 }
	  }
	  if( r!=null && g!=null && b!=null)
      {
		 shadowColor=new Color(Integer.parseInt(r),Integer.parseInt(g),Integer.parseInt(b));
      }
      else
      {
	 shadowColor=defaultshbg;
      }

      if(bkground!=null)
      {
 	 StringTokenizer tk=new StringTokenizer(bkground,",");
	 while(tk.hasMoreTokens())
	 {
	    r=(String)tk.nextElement();
	    g=(String)tk.nextElement();
	    b=(String)tk.nextElement();
	 }
      }

      if( r!=null && g!=null && b!=null)
      {
	 background=new Color(Integer.parseInt(r),Integer.parseInt(g),Integer.parseInt(b));
      }
      else
      {
	 background=defaultbg;
      }

      if(frground!=null)
      {
	 StringTokenizer tk=new StringTokenizer(frground,",");
	 r=null;g=null;b=null;
	 while(tk.hasMoreTokens())
	 {
	    r=(String)tk.nextElement();
	    g=(String)tk.nextElement();
	    b=(String)tk.nextElement();
	 }
       }

       if( r!=null && g!=null && b!=null)
       {
	  foreground=new Color(Integer.parseInt(r),Integer.parseInt(g),Integer.parseInt(b));
       }
       else
       {
	  foreground=defaultfg;
       }
       setBackground(new Color(208,208,208));

       temp=getParameter("Img");
       subMenu=getImage(getDocumentBase(),temp);

       setSize(200,200);
	   setLocation(100,100);
	   setVisible(true);
	   Logout();
    }   

    public void start()
    {
		
    }

    public void stop()
    {
       if(im.isShowing())
       {
			  closeAll();
       }
    }

    public void startFileReading()
    {
        try
	{
           InputStream inn = MenuApplet.class.getResourceAsStream(DataSource);
	   in = new BufferedReader(new InputStreamReader(inn));
	
	   String str=(String)in.readLine();
	   while(str != null)
	   {
 	      if(str.trim().startsWith("#FILEEND"))
	      {
		 break;
	      }
	      else if(str.trim().startsWith("POPUPBEGIN"))
	      {
		 Vect  vtemp=new Vect ();
		 count++;
		 while(!str.trim().startsWith("POPUPEND"))
		 {
		    StringTokenizer tk=new StringTokenizer(str,",");
		    int i=0;
		    while(tk.hasMoreElements())
		    {
		       if(i==0)
		       {
			  String s=(String)tk.nextElement();
			  vtemp.setName(s.trim());								
		       }
		       else if(i==1)
		       {
			  String s1=(String)tk.nextElement();
			  vtemp.setLable(s1.trim());								
		       }
		       else if(i==2)
		       {
			  String s5=(String)tk.nextElement();
			  vtemp.setStatusInfo(s5.trim());
		       }
		       else if(i==3)
		       {
		          String s2=(String)tk.nextElement();
			  vtemp.setUrl(s2.trim());							
		       }
		       else if(i==4)
		       {
			  String s3=(String)tk.nextElement();
			  vtemp.setEnable(s3.trim());								
		       }
		       else
		       {
			  break;
		       }
		       i++;
         	   }
		   vmain.addElement(vtemp);
	   	   addMenuItemToMe(vtemp);
	      	   break;
	       }
 	   } 
	   else if(str.trim().startsWith("MENUITEM"))
	   {
	      Vect  vtemp=new Vect();
	      count++;
	      StringTokenizer tk=new StringTokenizer(str,",");
	      int i=0;
	      while(tk.hasMoreElements())
              {
		 if(i==0)
		 {
		    String s=(String)tk.nextElement();
		    vtemp.setName(s.trim());
		 }
		 else if(i==1)
		 {
		    String s1=(String)tk.nextElement();
		    vtemp.setLable(s1.trim());							
		 }
		 else if(i==2)
	         {
		    String s5=(String)tk.nextElement();
		    vtemp.setStatusInfo(s5.trim());							
		 }
		 else if(i==3)
		 {
		    String s2=(String)tk.nextElement();
		    vtemp.setUrl(s2.trim());							
		 }
		 else if(i==4)
		 {
		    String s3=(String)tk.nextElement();
		    vtemp.setEnable(s3.trim());							
		 }
		 else
		 {
		    break;
		 }
		 i++;
              }
  	      vmain.addElement(vtemp);
  	   }
	   str=in.readLine();
         }
        }catch(Exception e)
	 {
	    System.out.println("Exception Occure "+e);
	 }
    }

    public void addMenuItemToMe(Vect  v)
    {		
       String str=null;
       try
       {
	  str=in.readLine();
	  while(!str.startsWith("POPUPEND"))
	  {
	     if(str.trim().startsWith("MENUITEM"))
	     {
		Vect  vtemp = new Vect();					
		StringTokenizer tk=new StringTokenizer(str,",");
		int i=0;
		while(tk.hasMoreElements())
		{
	    	   if(i==0)
		   {
		      String s=(String)tk.nextElement();
		      vtemp.setName(s.trim());							
		   }
		   else if(i==1)
		   {
		      String s1=(String)tk.nextElement();
		      vtemp.setLable(s1.trim());							
		   }
		   else if(i==2)
		   {
		      String s5=(String)tk.nextElement();
		      vtemp.setStatusInfo(s5.trim());							
		   }
		   else if(i==3)
		   {
		      String s2=(String)tk.nextElement();
		      vtemp.setUrl(s2.trim());							
		   }
		   else if(i==4)
		   {
		      String s3=(String)tk.nextElement();
		      vtemp.setEnable(s3.trim());
		   }
		   else
		   {
		      break;
		   }
	           i++;
		}
		v.addElement(vtemp);	
	        str=in.readLine();
             }

	     else if(str.trim().startsWith("POPUPBEGIN"))
             {
		Vect  vtemp = new Vect();
		StringTokenizer tk=new StringTokenizer(str,",");
		int i=0;
		while(tk.hasMoreElements())
		{
		   if(i==0)
		   {
		      String s=(String)tk.nextElement();
		      vtemp.setName(s);							
		   }
		   else if(i==1)
		   {
		      String s1=(String)tk.nextElement();
		      vtemp.setLable(s1);							
		   }
		   else if(i==2)
		   {
		      String s5=(String)tk.nextElement();
		      vtemp.setStatusInfo(s5);							
		   }
		   else if(i==3)
		   {
		      String s2=(String)tk.nextElement();
		      vtemp.setUrl(s2);							
		   }
		   else if(i==4)
		   {
		      String s3=(String)tk.nextElement();
		      vtemp.setEnable(s3);							
		   }
		   else
		   {
		      break;
		   }
		   i++;
		}
		v.addElement(vtemp);
	        addMenuItemToMe( vtemp );
	        str=in.readLine();
	     }

             else if(str.trim().startsWith("POPUPEND"))
	     {
		return;
	     }
	  }
       } catch(Exception e)
	 {
	    System.out.println("Exception occures in function addMenuItemToMe() "+e);
	 }	 
    }

    public void createFullMenu()
    {
       FullMenu.removeAllElements();
       AllMenus.removeAllElements();
       im=new IMenu();
       im.setForeground(foreground);
       im.setBackground(background);
       MenuVect menuvect=new MenuVect();
       menuvect.setMenu(im);
       menuvect.setpMenuItem(null);
       FullMenu.addElement(menuvect);
       AllMenus.addElement(im);
       for(int i=0;i<vmain.size();i++)
       {
	  Vect  v=(Vect )vmain.elementAt(i);
	  if(v.getName().trim().equals("POPUPBEGIN"))
	  {
	     IMenuItem imt=new IMenuItem(v.getLable(),subMenu);
	     imt.setShadowbg(shadowColor);
	     imt.setShadowfg(shadowForeground);
	     imt.setStatusText(v.getStatusInfo().trim());
	     imt.setUrl(v.getUrl().trim());
	     int b=Integer.parseInt(v.getEnable().trim());
	     if(b == 1)
	     {
		imt.setEnable(true);
	     }
	     else if(b == 0)
	     {
		imt.setEnable(false);
	     }
    	     menuvect.addElement(imt);
	     im.addComponent(imt);
	     createSubMenu(v,imt);
	  }
	  else if(v.getName().trim().equals("MENUITEM"))
	  {
	     IMenuItem imt=new IMenuItem(v.getLable(),subMenu);
	     imt.setShadowbg(shadowColor);
	     imt.setShadowfg(shadowForeground);
	     imt.setStatusText(v.getStatusInfo().trim());
	     imt.setUrl(v.getUrl().trim());
	     int b=Integer.parseInt(v.getEnable().trim());
	     if(b == 1)
	     {
		imt.setEnable(true);
	     }
	     else if(b == 0)
	     {
		imt.setEnable(false);
	     }
	     menuvect.addElement(imt);
	     im.addComponent(imt);				
	   }
        }
    }

    public void createSubMenu(Vect  v,IMenuItem imt)
    {
       IMenu  mm=new IMenu();
       MenuVect mv=new MenuVect();
       mv.setMenu(mm);
       mv.setpMenuItem(imt);
       FullMenu.addElement(mv);
       mm.setBackground(background);
       mm.setForeground(foreground);
       imt.setChildMenu(mm);
       AllMenus.addElement(mm);
       mm.setLocation(10,10);

       for(int i=0;i<v.size();i++)
       {
	  Vect vt=(Vect) v.elementAt(i);
	  if(vt.getName().trim().equals("POPUPBEGIN"))
	  {
	     IMenuItem iimt=new IMenuItem(vt.getLable(),subMenu);
	     iimt.setShadowbg(shadowColor);
	     iimt.setShadowfg(shadowForeground);
  	     iimt.setStatusText(vt.getStatusInfo().trim());
	     iimt.setUrl(vt.getUrl().trim());
	     int b=Integer.parseInt(vt.getEnable().trim());
	     if(b == 1)
	     {
		iimt.setEnable(true);
	     }
	     else if(b == 0)
	     {
		iimt.setEnable(false);
	     }
             mv.addElement(iimt);
	     mm.addComponent(iimt);
	     mm.setMaxWH();
	     iimt.setParentMenu(((IMenu)imt.getParent()),imt);				
	     createSubMenu(vt,iimt);
	  }
	  else if(vt.getName().trim().equals("MENUITEM"))
	  {
	     IMenuItem iimt=new IMenuItem(vt.getLable(),subMenu);
	     iimt.setShadowbg(shadowColor);
	     iimt.setShadowfg(shadowForeground);
	     iimt.setStatusText(vt.getStatusInfo().trim());
	     iimt.setUrl(vt.getUrl().trim());
	     int b=Integer.parseInt(vt.getEnable().trim());
	     if(b == 1)
	     {
		iimt.setEnable(true);
	     }
	     else if(b == 0)
	     {
		iimt.setEnable(false);
	     }
             mv.addElement(iimt);
	     mm.addComponent(iimt);
	     mm.setMaxWH();
	     iimt.setParentMenu(((IMenu)imt.getParent()),imt);				
	  }
       }
       return;
    }
	
    public void closeAll()
    {
	   for(int i=0;i<vtemp.size();i++)
       {
			IMenuWin im=(IMenuWin)vtemp.elementAt(i);			
			if(im.isShowing())
			{
				im.setVisible(false);
			}
			im.dispose();
       }

    }

    public void mouseClicked(MouseEvent me){}
    public void mousePressed(MouseEvent me){}
    public void mouseEntered(MouseEvent me){}
    public void mouseExited(MouseEvent me){}
    public void mouseReleased(MouseEvent me){}
 
    public void enableMenuApplet(int flag)
    {
		
       if(flag==0)	
       {
          sBar.setText(0,"  Login");
          loginflag=0;
       }
       else 
       {
          sBar.setText(0,"  Start");
          loginflag=1;
       }		
    }

    /* This is seter method which set the String which is tobe shown in Status Bar when Mouse Click Event Occure on IMenuItem */
    public void setText(int index,String text)
    {
      if(index>0 && index<4)
        sBar.setText(index,text);
    }

    public void actionPerformed(ActionEvent evt)
    {
       if(evt.getSource().equals(btn[0]))
       {
         if(loginflag==1)
         { 
			 chm=new checkMinimise(this);
			 chm.start(); 	
 			 Component c=(Component)evt.getSource();
			 closeAll();
			 createWindows();
			 IMenuWin im=(IMenuWin)vtemp.elementAt(0);
			 Point p=(c).getLocationOnScreen();
			 im.setyposofmenu(p.y);
			 Dimension bd=(c).getPreferredSize();
			 Dimension d=im.getSize();
			 int yloc=p.y - d.height;
			 im.setLocation(p.x,yloc);
			 im.setMaxWH();
			 im.show();
			 im.toFront();
	      }
          else
          {
             try
             {
                URL url = new URL( getDocumentBase(), "/JOrder/servlets/Login" );
                getAppletContext().showDocument(url,"mid_frame");
             }catch(MalformedURLException url){ System.out.println(url);} 
          }
       }
    }

    class Vect extends Vector 
    {
       private String Name;
       private String Lable;
       private String StatusInfo;
       private String url;
       private String enable;

       public Vect()
       {
	  Name=null;
	  Lable=null;
	  StatusInfo=null;
	  url=null;
	  enable=null;
       }

       public void setName(String Name)
       {
	  this.Name=Name;
       }

       public String getName()
       {
	  return this.Name;
       }

       public void setLable(String Lable)
       {
	  this.Lable=Lable;
       }

       public String getLable()
       {
	  return this.Lable;
       }

       public void setStatusInfo(String StatusInfo)
       {
	  this.StatusInfo=StatusInfo;
       }

       public String getStatusInfo()
       {
	  return this.StatusInfo;
       }

       public void setEnable(String enable)
       {
	  this.enable=enable;
       }

       public String getEnable()
       {
	  return this.enable;
       }

       public void setUrl(String url)
       {
	  this.url=url;
       }

       public String getUrl()
       {
	  return this.url;
       }
   }

   class MenuVect extends Vector 
   {
     IMenu menu;
     IMenuItem pmenuItem;
     public MenuVect()
     {
        super();
     }
     public void setMenu(IMenu im)
     {
	this.menu=im;
     }
     public IMenu getMenu()
     {
	return this.menu;
     }
     public void setpMenuItem(IMenuItem imt)
     {
	this.pmenuItem=imt;
     }
     public IMenuItem getpMenuItem()
     {
	return this.pmenuItem;
     }
   }

   public void createWindows()
   {
      vtemp.removeAllElements();
      for(int i=0;i<FullMenu.size();i++)
      {
		
			MenuVect m=(MenuVect)FullMenu.elementAt(i);			
			IMenuWin imw=new IMenuWin(this);
			IMenu imenu=m.getMenu();
			IMenuItem imt=m.getpMenuItem();
			imw.add(imenu);
			imw.setMaxWH();
			Dimension winSize=imw.getSize();
			if(localBrowser == 0)
			{
				Component [] ca=imenu.getComponents();
				if(ca.length <= 2)
				{
					int height=winSize.height+20;
					Dimension dtemp=new Dimension(winSize.width,height);
					imw.setSize(dtemp);
				}
			}
			for(int k=0;k<m.size();k++)
			{
			   IMenuItem im=((IMenuItem)m.elementAt(k));
			   im.state=0;
			}
			vtemp.addElement(imw); 
      } 
    }

    public void enableMenu(String str, String flag)
    {
     
     if( str != null )
     {
        Vector v=new Vector();
        StringTokenizer tk=new StringTokenizer(str,"|");
        while(tk.hasMoreTokens())
        {
		  String s=tk.nextToken();
		  v.addElement(s);
		}
        String [] arr=new String[v.size()];
        MenuVect m=(MenuVect)FullMenu.elementAt(0);
        if(v.size() == 1)
        {
		  for(int i=0;i< m.size();i++)
		  {
			 IMenuItem temp=(IMenuItem)m.elementAt(i);
			if(temp != null)
			{
				if(temp.getLable().toLowerCase().equals(((String)v.elementAt(0)).toLowerCase()))
				{
					temp.setEnable(false);
					IMenu parentt=(IMenu)temp.getParent();
					parentt.remove(temp);
					parentt.validate();
 				}
            }
	      }
        }
        else 
        { 
			IMenuItem pmenu=null;
			for(int i=0;i< m.size();i++)
			{
				IMenuItem temp=(IMenuItem)m.elementAt(i);
				if(temp != null)
				{
					if(temp.getLable().toLowerCase().equals(((String)v.elementAt(0)).toLowerCase()))
					{
						pmenu=temp;
					}
				}
			}
			for(int i=1;i<v.size();i++)
			{
				IMenuItem mymenu=FindMenu((String)v.elementAt(i),pmenu);
				if(mymenu == null)
				{
					return;
				}
				pmenu=mymenu;
			}
			pmenu.setEnable(false);
 			IMenu parentt=(IMenu)pmenu.getParent();
			parentt.remove(pmenu);
 			parentt.validate();  
       }
     }
   }
   public int getLocationofsBar()
   {
		Point p=sBar.getLocationOnScreen();
		return p.y;
   }	
   public IMenuItem FindMenu(String str,IMenuItem pmenu)
   {
      for(int i=1; i < FullMenu.size();i++)
      {
 	 MenuVect m=(MenuVect)FullMenu.elementAt(i);			
	 IMenu ime= m.getMenu();
	 IMenuItem imt= m.getpMenuItem();
	 if(pmenu.equals(imt))
	 {
	    for(int k=0; k < m.size();k++)
	    {
	       IMenuItem temp=(IMenuItem)m.elementAt(k);
	       if(temp.getLable().toLowerCase().trim().equals(str.toLowerCase().trim()))
	       {
		  return temp;
	       }					
	    }
	 }
      }
      return null;
    }
	checkMinimise chm=null; 
}

class checkMinimise extends Thread
{
		MenuApplet ma=null;
		public checkMinimise(MenuApplet ma)
		{
			this.ma=ma;
		}
		public void run()
		{
		 try
		 {		
			while(true)
			{	
				Point p=ma.sBar.getLocationOnScreen();
				if(p.y < 0)
				{
				    ma.closeAll();
				    this.stop();		
				}	
				sleep(100); 
			}
		 }
		 catch(Exception e){}
		}
					
}

