/*
-------------------------------------------------------------------------------------------------
Warning : This Computer Program is protected by copyright law and international treaties.
          Unauthorised reproduction or distribution of this program , or any portion of it,
          may result in severe civil and criminal penalties , and will be prosecuted to the
          maximum extent possible under the law.

Revision : 
By 			Date				   	      Defect No.	Reasons
Manoj Doshi     16-02-99 Tuesday 9:57:03 AM	 00001	CRE:Created.

CRE : Created
BFX : Bug fix
RAC : Requirement anlaysis changing
UPD : Upgradation
------------------------------------------------------------------------------------------------
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.math.*;


public class IStatusBar extends Container
{
   private Color bkcolor = new Color(204,204,204);

   public IStatusBar()
   {
      setLayout(new FlowLayout());
      ((FlowLayout)getLayout()).setHgap(2);
      ((FlowLayout)getLayout()).setVgap(2);
      setBackground(bkcolor);
   }

   /** set the text of specified index pane object to sContent*/
   public void setText(int index,String sContent)
   {
     getComponent(index).setName(sContent);
     getComponent(index).repaint();
   }

   public void update(Graphics g)
   {
      paint(g);
   }

   public void paint(Graphics g)
   {
      Dimension dim = getSize();
      g.setColor(Color.black);
      g.drawLine(0,dim.height-1,dim.width,dim.height-1);
      g.drawLine(dim.width-1,0,dim.width-1,dim.height-1);
      g.setColor(Color.white);
      g.drawLine(0,0,dim.width,0);
      g.drawLine(0,0,0,dim.height);
      super.paint(g);
   }
}