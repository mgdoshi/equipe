
import java.awt.*;
import java.awt.event.*;

public class IPane extends Component
{
   public static final int LOWERD=0;
   public static final int BEVELED=1;
   public static final int FLAT=2;

   public static final int CENTER=3;
   public static final int LEFT=4;
   public static final int RIGHT=5;

   private int sStyle=IPane.LOWERD;
   private int tAlign=IPane.LEFT;

   private Font font = new Font("Arial",Font.PLAIN,10);
   private ActionListener actionListener=null;
		 
   public IPane()
   {
      setFont(font);	
      setBackground(new Color(204,204,204));
      setForeground(Color.black);
      enableEvents(AWTEvent.MOUSE_EVENT_MASK);		
   }

   /**
     1.	Style		-	Beveled/Lowered/Center.
     2	Text Align	-	LEFT/CENTER/RIGHT.
   */

   public IPane(int sStyle,int tAlign)
   {
      this();
      this.sStyle=sStyle;
      this.tAlign=tAlign;
   }	
	
   public void addActionListener(ActionListener listener) 
   {  
      actionListener = AWTEventMulticaster.add(actionListener, listener);
   }
 
   public void removeActionListener(ActionListener listener) 
   { 
      actionListener = AWTEventMulticaster.remove(actionListener, listener);
   }

   public void processMouseEvent(MouseEvent e) 
   {  
      if(e.getID() == MouseEvent.MOUSE_PRESSED && isEnabled())
      {  
         if(actionListener != null) 
            actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "" + this));
      }
      super.processMouseEvent(e);
   }

   public Dimension getPreferredSize() 
   {
      return getMinimumSize();
   }
  
   public Dimension getMinimumSize() 
   {
      return new Dimension(getSize().width,getSize().height);
   }

   public void paint(Graphics g)
   {
      Dimension dim = getSize();
      if(sStyle!=IPane.FLAT)
      {
	 if(sStyle==IPane.BEVELED)
	 {
	    g.setColor(Color.white);
	 }
	 else
	 {
	    g.setColor(Color.black);
	 }
	 g.drawLine(0,0,dim.width,0);
	 g.drawLine(0,0,0,dim.height);				
  	 if(sStyle==IPane.BEVELED)
	 {
	    g.setColor(Color.black);
	 }	
	 else
	 {
	    g.setColor(Color.white);
	 }
	 g.drawLine(0,dim.height-1,dim.width,dim.height-1);
	 g.drawLine(dim.width-1,0,dim.width-1,dim.height-1);
      }
      String sTmp =null;
      if((sTmp=getName())!=null)
      {
	 g.setColor(getForeground());
	 g.setFont(font);
	 FontMetrics fm = g.getFontMetrics();
	 int fwidth = fm.stringWidth(sTmp);
	 int fheight = fm.getHeight();
	 if(tAlign==IPane.CENTER)
   	    g.drawString(sTmp,(getSize().width-fwidth)/2,getSize().height/2 + 4);
	 else if(tAlign==IPane.LEFT)
	    g.drawString(sTmp,0,getSize().height/2 + 4);
	 else if(tAlign==IPane.RIGHT)
	    g.drawString(sTmp,getSize().width-fwidth,getSize().height/2 + 4);
      }
   }
}

