import java.awt.*;
import java.awt.event.*;
import java.util.*;

class IBranch extends Component
{
   private ActionListener actionListener=null;
   private Font font = new Font("Arial",Font.PLAIN,10);
   private int sheight,swidth,ascent,descent;
   public int Status=0;
   private int hmargin=5,vmargin=2;
   private String Label="";
   private int width,height;
   private String stext;
   private String url;
   private int depth;
   boolean popup=false;
   boolean collapse=false;
   private Color HiBackground = new Color(207,184,210);
   private Color Foreground = new Color(0,0,0);
   private Color HiForeground = new Color(0,0,255);
   private Vector childlist = new Vector();
   IBranch parent;
   Container con;
   ScrollPane sp ;
   static IBranch currentnode;
   static Vector pads;

   static
   {
     pads = new Vector();
   }

   public IBranch (IBranch parent,ScrollPane sp,Container con,String Label,boolean popup,String stext,String url,int depth)
   {
      this.parent=parent;
      this.popup=popup;
      this.stext =  stext;
      this.url = url;
      this.depth=depth;
      this.con=con;
      this.sp=sp;
      setLabel(Label);
      enableEvents(AWTEvent.MOUSE_EVENT_MASK);
   }

   public void setLabel(String Label)
   {
      if(Label!=null)
  	 this.Label = Label;
      FontMetrics fm = getFontMetrics(font);
      sheight=fm.getHeight();
      swidth=fm.stringWidth(this.Label);
      ascent=fm.getAscent();
      descent=fm.getDescent();
      width = swidth + 2*hmargin + 10 + depth*hmargin;
      height = sheight;
      setSize(width,height);							
   }

   public void add(IBranch item)
   {
      childlist.addElement(item);
   }

   public String getStext()
   {
      return stext;
   }

   public String getUrl()
   {
      return url;
   }

   public IBranch getOwner()
   {
      return parent;
   }

   public IBranch getBranch(String name)
   {
      for(int i=0;i<childlist.size();i++)
      {
  	 IBranch item = (IBranch)childlist.elementAt(i);
         if(item.Label.equals(name))
         {
            return item;
         }
      }
      return null;
   }

   public void setCollapse(boolean flag)
   {
	collapse=flag;	
        if(collapse)
          showChilds();             
        else
          hideChilds();   
   }

      public void setForeground(Color Foreground)
      {
        if(Foreground!=null) 
        {
          this.Foreground = Foreground;
          super.setForeground(Foreground);
        }
      }

	public void setHiForeground(Color HiForeground)
      {
        if(HiForeground!=null) 
          this.HiForeground = HiForeground;
      }

	public void setHiBackground(Color HiBackground)
      {
        if(HiBackground!=null) 
          this.HiBackground = HiBackground;
      }
	
      public void setFont(Font font)
      {
        if(font!=null)
        {
          this.font = font;
          super.setFont(font);
          setLabel(null);
        }
      }   
	
   public void paint(Graphics g)
   {
      int x = (depth+1)*hmargin;
      int y = height/2-5;
      Rectangle rect = null;
      if(popup)
        rect = new Rectangle(x-hmargin,vmargin-2,x+swidth+10+hmargin,height+(2*vmargin));
      else
        rect = new Rectangle(x-hmargin,vmargin-2,x+swidth+hmargin,height+(2*vmargin));
      switch(Status)
      {
         case 0:
	          g.setColor(Foreground);	
	          break;
         case 1:
	 	  if(isEnabled())
	 	  {
		     g.setColor(HiBackground);
		     g.fillRect(rect.x,rect.y,rect.width-2,rect.height-2);
		     g.setColor(Color.white);
		     g.drawLine(rect.x,rect.y,rect.x + rect.width-1,rect.y);
		     g.drawLine(rect.x,rect.y,rect.x,rect.height-1);
		     g.setColor(Color.black);
		     g.drawLine(rect.x + rect.width-1,rect.height-1,rect.x + rect.width-1,rect.y);
		     g.drawLine(rect.x + rect.width-1,rect.height-1,rect.x,rect.height-1);
		     g.setColor(HiForeground);	
		  }
		  break;
      }
      if(popup)
      {
         if(collapse)
         {
           int x1 = x-5;
           int y1 = y+2;
           g.fillPolygon(new int[] {x1,x1+10,x1+5},new int[] {y1,y1,y1+5},3);
         }
         else
           g.fillPolygon(new int[] {x,x+5,x},new int[] {y,y+5,y+10},3);
         }
      else
      {
         y=y+2;
         g.fillPolygon(new int[] {x,x+5,x+5,x},new int[] {y,y,y+5,y+5},4);
      }
      g.drawString(Label,x+10,(height/2) + (ascent/2));
   } 

   public synchronized void addActionListener(ActionListener listener)
   {
      actionListener = AWTEventMulticaster.add(actionListener, listener);
   }

   public synchronized void removeActionListener(ActionListener listener)
   {
      actionListener = AWTEventMulticaster.remove(actionListener, listener);
   }

   public void processMouseEvent(MouseEvent e)
   {
      if(isEnabled())
      {	
	 if(e.getID() == MouseEvent.MOUSE_ENTERED)
	 {
            if(currentnode!=null)  
            {
		currentnode.Status=0;
                currentnode.repaint();
            }
            currentnode = this;
	    Status=1;
	    repaint();
	 }
	 else if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getSource().equals(this))
	 {
            if(!popup)
            {
              if(actionListener != null)
		actionListener.actionPerformed(new ActionEvent(this,0,Label));
            }
            else
            {
              if(collapse)
                hideChilds();
              else
                showChilds();             
              con.validate();
              sp.validate();
              repaint();
            }
         }
      }
      super.processMouseEvent(e);
   }

   public void showChilds()
   {
      int count = con.getComponentCount();
      IBranch tmp=null;
      int index=0;
      for(int i=0;i<count;i++)
      {
         tmp = (IBranch)con.getComponent(i);
         if(tmp.equals(this))
         {
            index = i+1;
            break;
         }
      }
      if(popup && tmp!=null)
      { 
        for(int i=0;i<childlist.size();i++)
        {
            con.add((IBranch)childlist.elementAt(i),index++);
        }
        collapse = true;
      }
      padding();
   }

   public void hideChilds()
   {
      if(popup)
      {
        for(int i=0;i<childlist.size();i++)
        {
            IBranch tmp = (IBranch)childlist.elementAt(i);
            if(tmp.popup && tmp.collapse)
               tmp.hideChilds();
            con.remove(tmp);
        }
        collapse = false;
      }
      padding();
   }

   public void padding()
   {
       removepadding();
       Panel p = (Panel)sp.getComponent(0);
       int con = p.getComponentCount();
       int cheight = sp.getSize().height-4;
       int aheight = con * (height+(2*vmargin));
       int padcount = (cheight - aheight)/(height+(2*vmargin)); 
       if( aheight < cheight)
       {
         IPad pad[] = new IPad[padcount];
         for(int i=0;i<padcount;i++)
         {
           pad[i] = new IPad(width,height+(2*vmargin));
           p.add(pad[i]);
           pads.addElement(pad[i]);
         }
       }
   }

   public void removepadding()
   {
      Panel p = (Panel)sp.getComponent(0);        
      int count = pads.size();
      for(int i=0;i<count;i++)
      {
         p.remove((IPad)pads.elementAt(i));
      }
      for(int i=0;i<count;i++)
         pads.removeElementAt(0);
   }
        

   public Dimension getPreferredSize()
   {
      return getMinimumSize();
   }

   public Dimension getMinimumSize()
   {
      return new Dimension(width,height+(2*vmargin));
   }
}

class IPad extends Component
{
   int width=0,height=0;

   public IPad(int width,int height)
   {
     this.width=width;
     this.height=height;
   }

   public Dimension getPreferredSize()
   {
      return getMinimumSize();
   }

   public Dimension getMinimumSize()
   {
      return new Dimension(width,height);
   }
}