/*
-------------------------------------------------------------------------------------------------
Warning          : This Computer Program is protected by copyright law and international treaties.
                   Unauthorised reproduction or distribution of this program , or any portion of it,
		           may result in severe civil and criminal penalties , and will be prosecuted to the 
		           maximum extent possible under the law.

File Name        : IMenu.java
Referenced Files : IMenuItem.java,MenuApplet.java
Revision : 
By 					Date								Defect No.	Reasons
Kishore Godse	   31-08-99    Tuesday 6:09:39 PM		00001		CRE:Created.


CRE : Created
BFX : Bug fix
RAC : Requirement anlaysis changing
UPD : Upgradation
------------------------------------------------------------------------------------------------
*/

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class IMenu extends Panel implements MouseListener
{
	//Constructor of IMenu which takes Applet in which this IMenu is Embeded
	public IMenu()
	{
		
		this.addMouseListener(this);
		setLayout(new GridLayout(menus.size(),1));
	}
	
	//This is seter Method which sets yposition of This IMenu
	public void setyposofmenu(int y)
	{
		this.yposofMenu=y;
	}
	
	//This is seter Method which sets the background of IMenu
	public void setBackground(Color c)
	{
		bgcolor=c;
		super.setBackground(c);
	}
	
	//This is seter Method which sets the Foreground  of IMenu
	public void setForeground(Color c)
	{
		fgcolor=c;
		super.setForeground(c);
	}
	
	//This is seter Method which sets parent IMenu to this IMenu if current IMenu is popupMenu
	public void setParentMenu(IMenu parMenu,IMenuItem parMenuItem)
	{
		Component [] arr=getComponents();
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i] instanceof IMenuItem)
			{
				((IMenuItem)arr[i]).setParentMenu(parMenu,parMenuItem);
			}
		}
	}

	//This is seter Method which sets x,y Location of IMenu on the Screen
	public void setLocation(int x,int y)
	{
		Dimension ypos=this.getSize();
		if((ypos.height+y)> yposofMenu)
		{
			int yy=y-ypos.height;
			super.setLocation(x,yy+7);
		}
		else
		{
			super.setLocation(x,y);
		}
	}

	public void setParentWin(IMenuWin ma)
	{
		Component [] arr=getComponents();
		for(int i=0;i<arr.length;i++)
		{
			((IMenuItem)arr[i]).parentMenuWin=ma;
		}
	}

	//This is seter Method which sets the Maximum width and height of IMenu accordingly it also set the size of IMenuItem present in this IMenu
	public Dimension setMaxWH()
	{
		int width=0;
		int height=0;
		Component [] arr=getComponents();
		for(int i=0;i< arr.length;i++)
		{
			IMenuItem imi=(IMenuItem)arr[i];
			Dimension d=imi.getPreferredSize();
			int h=d.height;
			int w=d.width;
			height+=h+5;
			if(w > width)
			{
				width=w;
			}
		}
		Iheight=height;
		Iwidth=width + ww;
		setSize(Iwidth,Iheight);
		Dimension d=new Dimension(Iwidth,Iheight);
		return d;
	}

	//This is seter Method which sets the Maximum width and height of IMenu accordingly it also set the size of IMenuItem present in this IMenu
	public void setMaxWidthHeight()
	{
		int width=0;
		int height=0;
		for(int i=0;i< menus1.length;i++)
		{
			IMenuItem imi=(IMenuItem)menus1[i];
			Dimension d=imi.getPreferredSize();
			int h=d.height;
			int w=d.width;
			height+=h+5;
			if(w > width)
			{
				width=w;
			}
		}
		Iheight=height;
		Iwidth=width + ww;
		setSize(Iwidth,Iheight);
		for(int j=0;j< menus1.length;j++)
		{
			IMenuItem imi=menus1[j];
			Dimension d=imi.getPreferredSize();
			int h=d.height;
			imi.width=width;
		}
	}

	//This is seter Method which sets the parent IMenu of this IMenu if this IMenu is popupMenu
	public void setParentMenu(IMenu im)
	{
		this.parentMenu=im;
	}

	public void setParentWindow(IMenuWin im)
	{
		this.parentMenuWin=im;
	}

	//This is seter Method which sets the parent IMenuItem of this IMenu if this IMenu is popupMenu
	public void setParentMenuItem(IMenuItem imi)
	{
		this.parentMenuItem=imi;
	}
	
	public Dimension getMinimumSize()
	{
		int width=0;
		int height=0;
		for(int i=0;i< menus.size();i++)
		{
			IMenuItem imi=(IMenuItem)menus.elementAt(i);
			Dimension d=imi.getPreferredSize();
			int w=d.width;
			height=height+d.height;
			if(w > width)
			{
				width=w;
			}
		}
		Iheight=height;
		Iwidth=width+leftmargin+rightmargin;
		return new Dimension(width,Iheight);
	}
	public Dimension getSize()
	{
		Component [] arrc=getComponents();
		int widthw=0;
		int heighth=0;
		for (int i=0;i<arrc.length;i++)
		{
			Dimension d=arrc[i].getPreferredSize();
			if(d.width > widthw)
			{
				widthw=d.width;
			}
			heighth+=d.height;
		}
		return new Dimension(widthw+leftmargin+rightmargin,heighth+topmargin+bottommargin);
	}
	public Dimension getPreferredSize()
	{
		return getMinimumSize();
	}
	//This method closes all IMenu  
	public void closeAll()
	{
		mip.closeAll();
	}

	//This method show current IMenu on Screen and put thie IMenu on Front
	public void showMe()
	{
		//super.setVisible(true);
		//super.toFront();
	}

	//This method adds IMenuItem to the current IMenu
	public void addComponent(IMenuItem imt)
	{
		imt.addMouseListener(this);
		imt.setBackground(bgcolor);
		imt.setForeground(fgcolor);
		Dimension d=imt.getPreferredSize();
		int width=d.width;
		imt.setBounds(leftmargin,0,imt.width,imt.height);
		add(imt,ii++);
		if(compowidth < width)
		{
			compowidth=width; 
			Component [] arr=getComponents();
			for(int i=0;i<arr.length;i++)
			{
				((IMenuItem)arr[i]).setSideBarWidth(ww);
				((IMenuItem)arr[i]).width=width+ww;
			}
		}
		else
		{
			Component [] arr=getComponents();
			for(int i=0;i<arr.length;i++)
			{
				((IMenuItem)arr[i]).setSideBarWidth(ww);
				((IMenuItem)arr[i]).width=compowidth +ww;
			}
		}
	}

	//This method adds IMenuItem to the current IMenu
	public void addComponents()
	{
		setLayout(new GridLayout(menus.size(),1));
		for(int i=0;i< menus1.length;i++)
		{	
			menus1[i].setBounds(leftmargin,0,menus1[i].width,menus1[i].height);
			add(menus1[i],i);
		}
	}

	//This method adds Listener to this IMenu
	public void addListener()
	{
		Component [] arr=getComponents();
		for(int i=0;i< arr.length;i++)
		{
			arr[i].addMouseListener(this);
		}
	}
	
	public void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.drawLine(0,0,0,Iheight);
		g.drawLine(0,0,Iwidth,0);
		g.drawLine(1,1,1,Iheight-1);
		g.drawLine(1,1,Iwidth-1,1);

		g.setColor(new Color(134,134,134));
		g.drawLine(Iwidth-1,0,Iwidth-1,Iheight-1);
		g.drawLine(0,Iheight-1,Iwidth-1,Iheight-1);
		g.drawLine(Iwidth-2,0,Iwidth-2,Iheight-2);
		g.drawLine(0,Iheight-2,Iwidth-2,Iheight-2);
		g.setColor(Color.green);
		if(sideimage != null)
		{
			g.drawImage(sideimage,2,2,this);
		}
		else
		{
			g.fillRect(2,2,ww,Iheight-4);
		}
		super.paint(g);
	}
	
	public void mouseClicked(MouseEvent me){}
	public void mousePressed(MouseEvent me){}
	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	public void mouseReleased(MouseEvent me){}

	private Vector menus=new Vector();
	private static int yposofMenu=0;
	private IMenuItem [] menus1;
	private boolean isSubMenu=false;
	private boolean isMainMenu=false;
	private IMenu     parentMenu=null;
	private IMenuItem parentMenuItem=null;
	private final int leftmargin=5;
	private final int rightmargin=5;
	private final int topmargin=5;
	private final int bottommargin=5;
	private int Iheight=0;
	private int Iwidth=0;
	private int ww=0;
	MenuApplet mip=null;
	private Image sideimage=null;
	private int ii=0;
	private int compowidth =0;
	private Color bgcolor=null;
	private Color fgcolor=null;
	private IMenuWin parentMenuWin=null;
}