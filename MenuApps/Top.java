
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;

public class Top extends Applet
{
   String clientname,appname,date;
   FontMetrics fm;
   Font f = new Font("Arial",Font.PLAIN,11);

   public void init()
   {
      String ptext;
      setBackground(new Color(204,204,204));
      setFont(f);
      fm = getFontMetrics(f);
      clientname = getParameter("clientname");
      appname = getParameter("appname");
      date = getParameter("date");
   }

   public void paint(Graphics g)
   {
      int twidth = getSize().width;
      int theight = getSize().height;
      int fheight = fm.getHeight();
      int ascent=fm.getAscent();
      int descent=fm.getDescent();
      int baseline = theight/2 + (ascent/2);
      g.drawString(clientname,5,baseline);
      g.drawString(date,twidth-fm.stringWidth(date)-5 ,baseline);
      g.drawString(appname,twidth-fm.stringWidth(date) -fm.stringWidth(appname)-30,baseline);
      g.setColor(Color.white);
      g.drawLine(1,baseline+2,twidth,baseline+2);
      g.setColor(Color.black);
      g.drawLine(1,baseline+3,twidth,baseline+3);
   }

   public void setDate(String str)
   {
         date = str;
         repaint();      
   }
}