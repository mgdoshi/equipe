/*
-------------------------------------------------------------------------------------------------
Warning  : This Computer Program is protected by copyright law and international treaties.
           Unauthorised reproduction or distribution of this program , or any portion of it,
		   may result in severe civil and criminal penalties , and will be prosecuted to the 
		   maximum extent possible under the law.

File Name: IMenuWin.java
Revision : 
By 					Date								Defect No.	Reasons
Kishore Godse	   8-09-99    Wednesday 6:09:39 PM		00001		CRE:Created.


CRE : Created
BFX : Bug fix
RAC : Requirement anlaysis changing
UPD : Upgradation
------------------------------------------------------------------------------------------------
*/

import java.awt.*;
import java.awt.event.*;

public class IMenuWin extends Window
{
		IMenu pp=null;
		public MenuApplet mip=null;
		public IMenuWin(MenuApplet im)
		{
			super(new Frame());
			this.mip=im;
		}
		public void setMaxWH()
		{
			Dimension d=pp.setMaxWH();
			this.setSize(d.width,d.height);
		}
		public void add(IMenu p)
		{
			super.add(p);
			p.setParentWin(this);
			this.setSize(p.getPreferredSize());
			this.pp=p;
			super.setBackground(p.getBackground());
		}
		public void showMe()
		{
			super.setVisible(true);
		}
		public void setyposofmenu(int y)
		{
			ypos=y;
		}
		public int getyposofmenu()
		{
			return this.ypos;
		}
		int ypos=0;
}