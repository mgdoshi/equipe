/*
-------------------------------------------------------------------------------------------------
Warning         : This Computer Program is protected by copyright law and international treaties.
                  Unauthorised reproduction or distribution of this program , or any portion of it,
		          may result in severe civil and criminal penalties , and will be prosecuted to the 
		          maximum extent possible under the law.

File Name       :IMenuItem.java
Referenced Files:IMenu.java,MenuApplet.java
Revision        : 
By 					Date								Defect No.	Reasons
Kishore Godse	   31-08-99    Tuesday 6:09:39 PM		00001		CRE:Created.


CRE             : Created
BFX             : Bug fix
RAC             : Requirement anlaysis changing
UPD             : Upgradation
------------------------------------------------------------------------------------------------
*/
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import netscape.javascript.*;

public class IMenuItem extends Component 
{
	/*  Constructor which takes MenuItem Label  and  PopupMenuItem image as Parameter */
	public IMenuItem(String Name,Image img)
	{
		this.Name=Name;
		submenu=img;
		setHeightWidth(Name);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);		
	}

	/* This method sets the color of shadow when mouse over event occures on IMenuItem */
	public void setShadowbg(Color c)
	{
		this.shadowbg=c;
	}
	/* This method sets the color IMenuItem Lable when mouse over event occures on IMenuItem*/
	public void setShadowfg(Color c)
	{
		this.shadowfg=c;
	}
	/* This method sets the background color of IMenuItem */
	public void setBackground(Color c)
	{
		this.bkground=c;
	}
	/* This method sets the foreground color of Label of IMenuItem */
	public void setForeground(Color c)
	{
		this.foreground=c;
	}
	/* This is seter method which set the String which is tobe shown in Status Bar when Mouse Click Event Occure on IMenuItem */
	public void setStatusText(String s)
	{
		this.statusText=s;
	}
	/* This is seter method which set URL which is tobe called when Mouse Click Event Occure on IMenuItem */
	public void setUrl(String s)
	{
		this.url=s;
	}
	/* This is seter method which set Label of IMenuItem */
	public void setLable(String s)
	{
		this.Name=s;
	}
	/* This is seter method which set state of IMenuItem */
	public void setEnable(boolean s)
	{
		this.enable=s;
	}
	/*This Method is used by parent (Container) which contains this IMenuItem if parent contains SIDEBAR */
	public void setSideBarWidth(int w)
	{
		this.ww=w;
	}
	/* This is seter method which set width of IMenuItem according to the Label given to It */
	public void setHeightWidth(String Name1)
	{
		f=new Font("Arial",Font.PLAIN,11);
		FontMetrics fm=Toolkit.getDefaultToolkit().getFontMetrics(f);
		int ascent=fm.getMaxAscent();
		int descent=fm.getMaxDescent();
		this.height=ascent+topmargin+bottommargin;
		int size=fm.stringWidth(Name1);
		this.width=(size+25 )+rightmargin+leftmargin;
		
	}
	/* This is seter method which set child IMenu of this IMenuItem if this IMenuItem is popup MenuItem*/
	public void setChildMenu(IMenu child)
	{
		this.childMenu=child;
		this.childMenuWin=(IMenuWin)child.getParent();
		this.hasChild=true;
	}
	/* This is seter method which set parent IMenuItem of this IMenuItem and parent IMenuItem's Container 
	   This method is used for all IMenuItem present in ChildMenu of any IMenuItem  */
	public void setParentMenu(IMenu parent,IMenuItem parentItem)
	{
		if(parent != null && parentItem != null)
		{
			this.parentMenu=parent;
			this.parentMenuWin=(IMenuWin)parent.getParent();
			this.parentMenuItem=parentItem;
			this.hasParent=true;
		}
	}

	/*This is geter Method used to access Status String which is tobe set into status bar when this IMenuItem is clicked */
	public String getStatusText()
	{
		return this.statusText;
	}
	/*This is geter Method used to access URL  which is tobe called when this IMenuItem is clicked  */
	public String getUrl()
	{
		return this.url;
	}
	/*This is geter Method used get state of IMenuItem */
	public boolean isEnable()
	{
		return enable;
	}

	/*This is geter Method used to access Label of IMenuItem  */

	public String getLable()
	{
		return this.Name;
	}
	/*This Method is used if you want to add Seprator after this IMenuItem */
	public void addSeprator()
	{
		seprator=true;
		this.height=this.height+10;
		getMinimumSize(); 
	}
	/*This Method return the size of IMenuItem */
	public Dimension getPreferredSize() 
	{
		return getMinimumSize();
	}
	/*This Method return the size of IMenuItem  */
	public Dimension getMinimumSize() 
	{
		return new Dimension(width,height);
	}
	/*This Method return background color of IMenuItem */
	public Color getBackground()
	{
		return bkground;
	}
	/*This Method return foreground color of IMenuItem  */
	public Color getForeground()
	{
		return foreground;
	}

	public void paint(Graphics g)
	{
		this.Name=this.Name.trim();
		g.setFont(f);
		if(enable == false)
		{
			g.setColor(Color.black);
			if(seprator)
			{
				g.setColor(Color.white);
				g.drawLine(ww+2,height-2,ww+width-4,height-2);
				g.setColor(Color.black);
				g.drawLine(ww+2,height-3,ww+width-4,height-3);
				g.drawString(Name,ww+topmargin+10,11+bottommargin);		
			}
			else
			{
				g.setColor(foreground);
				g.drawString(Name,ww+topmargin+10,11+bottommargin);		
			}
			if(childMenu != null)
			{
				g.drawImage(submenu,ww+width-4-7,(height/2)/2,this);
			}
			g.drawRect(ww+2,2,width-4,height-1);
			
		}
		else
		{
			switch(state)
			{
				case 0:
						g.setColor(Color.black);
						if(seprator)
						{
							g.setColor(Color.white);
							g.drawLine(ww+2,height-2,ww+width-4,height-2);
							g.setColor(Color.black);
							g.drawLine(ww+2,height-3,ww+width-4,height-3);
							g.drawString(Name,ww+topmargin+10,11+bottommargin);		
						}
						else
						{
							g.setColor(foreground);
							g.drawString(Name,ww+topmargin+10,11+bottommargin);		
						}
						if(childMenu != null)
						{
							g.drawImage(submenu,ww+width-4-7,(height/2)/2,this);
							
						}
						break;

				case 1:
						/*g.setColor(Color.white);
						g.drawLine(1,0,width-4,0);
						g.drawLine(2,1,width-4,1);
						g.drawLine(1,1,1,height);
						g.drawLine(2,2,2,height);*/
						
							
						g.setColor(shadowbg);
						if(seprator)
						{
							g.fillRect(ww+2,2,width-4,height-8);
							g.setColor(Color.black);
							g.drawLine(ww + 2 , height-3 ,ww+ width-4 , height-3);
							g.setColor(Color.white);
							g.drawLine(ww + 2 , height-2 ,ww+ width-4 , height-2);
						}
						else
						{
							g.fillRect(ww+2,2,width-4,height);
						}
						g.setColor(shadowfg);
						g.drawString(Name,ww+topmargin+10,11+bottommargin);		
						if(this.parentMenuItem != null)
						{
							this.parentMenuItem.state=1;
							this.parentMenuItem.repaint();
						}
						if(childMenu != null)
						{
							g.drawImage(submenu,ww+width-4-7,(height/2)/2,this);
							Point p=this.getLocationOnScreen();
							Dimension d=this.getPreferredSize();
							IMenu par1=childMenu;
							IMenuWin parwin1=(IMenuWin)par1.getParent();
							int y=parwin1.mip.getLocationofsBar();
							Dimension d1=parwin1.getSize();
							if((d1.height+p.y) > y)
							{
								((IMenuWin)childMenu.getParent()).setLocation(p.x+d.width+ww,y-d1.height);
							    ((IMenuWin)childMenu.getParent()).showMe(); 	
							}
							else
							{
								((IMenuWin)childMenu.getParent()).setLocation(p.x+d.width+ww,p.y);
							    ((IMenuWin)childMenu.getParent()).showMe(); 	
							}
							
						}
						/*g.setColor(Color.black);
						g.drawLine(width-2,0,width-2,height);
						g.drawLine(width-3,1,width-3,height);
						g.drawLine(2,height+1,width-2,height+1);
						g.drawLine(2,height+2,width-2,height+2);*/
						
						break;

				case 2:
						break;

				case 3:
						g.setColor(foreground);
						g.drawString(Name,ww+topmargin+10,12+bottommargin);
						Component c=(Component)this.getParent();
						if(c instanceof IMenu)
						{
							IMenu im=(IMenu)c;
							if(hasChild)
							{
								this.state=1;
								this.repaint();
							}
							else
							{
								im.closeAll();
							}
						}
						break;
				default:
						break;
			}

		}		
	}
	
	public void addMouseListener(MouseListener ml)
	{
	   mouseListener = AWTEventMulticaster.add(mouseListener, ml);
	   enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}
	public void removeActionListener(MouseListener ml) 
	{
  	   mouseListener = AWTEventMulticaster.remove(mouseListener, ml);     
	}
	public void processMouseEvent(MouseEvent me) 
	{
		Component c=(Component)me.getSource();
		Component c1=(Component)c.getParent();
		((Window)c1.getParent()).toFront();
		if(me.getID() == me.MOUSE_ENTERED)
		{
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			if(c1 instanceof IMenu)
			{
				Component [] arr=((IMenu)c1).getComponents();
				for(int i=0;i<arr.length;i++)
				{
					if(arr[i] instanceof IMenuItem)
					{
						IMenuItem imi=((IMenuItem)arr[i]);
						((IMenuItem)arr[i]).state=0;
						((IMenuItem)arr[i]).repaint();
						CloseChilds((IMenuItem)imi);
					}
				}
			}
			mouseListener.mouseEntered(new MouseEvent(this,MouseEvent.MOUSE_ENTERED,0,0,0,0,1,false));
			((IMenuItem)c).state=1;
		} 
		else if(me.getID() == me.MOUSE_EXITED)
		{
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			mouseListener.mouseExited(new MouseEvent(this,MouseEvent.MOUSE_EXITED,0,0,0,0,1,false));
			state=0;
		}
		else if(me.getID() == me.MOUSE_PRESSED)
		{
			if(MenuApplet.localBrowser == 0)
			{
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				if(this.hasChild){}
				else
				{
					if(enable)
					{
						IMenu par=(IMenu)getParent();
						IMenuWin parwin=(IMenuWin)par.getParent();
						parwin.setVisible(false);		
						parwin.mip.closeAll();
						parwin.dispose();						
						String Lable=getLable();
						String StatusInfo=getStatusText();
						String url=getUrl();
						boolean en=isEnable();
						parwin.mip.setText(1,StatusInfo);
      					if(url.equals("") || url.equals(null))
  						{
						}
       					else
 						{
						if( Lable.equals("About") )
						{
							 JSObject window = JSObject.getWindow(parwin.mip);
							 window.eval("window.open('"+url+"', 'AboutWin','menubar=0,scrollbar=1,resizable=1')");
						}
						else if( Lable.equals("Help") )
						{
								 JSObject window = JSObject.getWindow(parwin.mip);
								 window.eval("window.open( '"+url+"', 'AboutWin','menubar=0,scrollbar=1,resizable=1')");
						}
						else
						{ 
                        	try
	                    	{
 	    						  URL u=new URL(parwin.mip.getDocumentBase(),url);
								  parwin.mip.getAppletContext().showDocument( u, "mid_frame" );
							}
							catch(MalformedURLException mfue)
							{
                        		  System.out.println("Exception Occures "+mfue);
                        	}
						} 
					}
				 }
			 }
			 state=3;
		  }
		  repaint();
		}
		else if(me.getID() == me.MOUSE_CLICKED && MenuApplet.localBrowser != 0)
		{
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			if(this.hasChild){}
			else
			{
				if(enable)
				{
					IMenu par=(IMenu)getParent();
					IMenuWin parwin=(IMenuWin)par.getParent();
					parwin.setVisible(false);		
					parwin.mip.closeAll();
					parwin.dispose();						
					String Lable=getLable();
					String StatusInfo=getStatusText();
					String url=getUrl();
					boolean en=isEnable();
                                        parwin.mip.setText(1,StatusInfo);
      	                                if(url.equals("") || url.equals(null))
  	                                    {
                                        }
       	                                else
 	                                    {
                                          if( Lable.equals("About") )
                                          {
                                             JSObject window = JSObject.getWindow(parwin.mip);
										     window.eval("window.open('"+url+"', 'AboutWin','menubar=0,scrollbar=1,resizable=1')");
                                          }
										  else if( Lable.equals("Help") )
										  {
											 
                                             JSObject window = JSObject.getWindow(parwin.mip);
											 window.eval("window.open( '"+url+"', 'AboutWin','menubar=0,scrollbar=1,resizable=1')");
                                          }
										  else
                                          { 
                             				try
	                       					{
												  URL u=new URL(parwin.mip.getDocumentBase(),url);
												  parwin.mip.getAppletContext().showDocument( u, "mid_frame" );
											}
											catch(MalformedURLException mfue)
											{
                           						  System.out.println("Exception Occures "+mfue);
                        					}
                                          } 
                                       }
                                }
			}
			state=3;
		}
		repaint();
	}
	public void CloseChilds(IMenuItem imi)
	{
		if(imi.childMenu != null)
		{
			Component [] arr=imi.childMenu .getComponents();
			for(int i=0; i  < arr.length ;i++)
			{
				  ((IMenuItem)arr[i]).state=0;	
				  if(((IMenuItem)arr[i]).childMenu != null )
				  {
					CloseChilds((IMenuItem)arr[i]);
				  }
				  else
				  {
					IMenu ii=(IMenu)((IMenuItem)arr[i]).getParent();

					((IMenuWin)ii.getParent()).toBack();
					((IMenuWin)ii.getParent()).setVisible(false);
				  }
			}
			return;			
		}
	}
	
	private Color bkground=null; 
	private Color foreground=null; 
	private Color shadowbg=null;
	private Color shadowfg=null;
	public int state=0;
	private boolean hasParent=false;
	private boolean hasChild=false;
	private int topmargin=1;
	private int bottommargin=1;
	private int leftmargin=5;
	private int rightmargin=5;
	int height;
	int width;
	private String Name="";
	MouseListener mouseListener = null;
	private IMenuItem parentMenuItem=null;
	private IMenu     parentMenu=null;
	private IMenu     childMenu=null; 
	public IMenuWin  childMenuWin=null;
	public IMenuWin  parentMenuWin=null;
	private boolean seprator=false;
	private MenuApplet menu = null;
	Font f;
	Image submenu=null;
	private int ww=0;	
	private String statusText=null;
	private String url=null;	
	private boolean enable=true;

}