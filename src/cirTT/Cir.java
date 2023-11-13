package cirTT;

import java.awt.*;
import javax.swing.*;

public class Cir extends JButton{
	final static long serialVersionUID=1L;
	private Color col,backCol;
	private boolean on;
	private boolean selected;
	final int x,y;
	public final static int SIZ=50;
	public Cir(int x,int y,Color c) {
		this.x=x;
		this.y=y;
		col=c;
		on=false;
		selected=false;
		backCol=new Color(255,255,255);
		this.setMinimumSize(new Dimension(SIZ,SIZ));
		this.setSize(SIZ, SIZ);
	}
	public Color getColor() {
		return col;
	}
	public void setColor(Color c) {
		col=c;
		this.repaint();
	}
	public boolean getOn() {
		return on;
	}
	public void setOn(boolean b) {
		on=b;
		this.repaint();
	}
	public void select() {
		if(selected)
		{	
			backCol=new Color(255,255,255);	
			selected=false;	
			this.repaint();
		}
		else
		{
			backCol=new Color(0,0,0);
			selected=true;
			this.repaint();
		}
	}
	public static void swap(Cir a,Cir b){
		Color tmp=a.col;
		a.col=b.col;
		b.col=tmp;
		boolean tmp2=a.on;
		a.on=b.on;
		b.on=tmp2;
		a.repaint();
		b.repaint();
	}
	public boolean equals(Cir c) {
		if((this.col.equals(c.col))&&(this.on==true)&&(c.on==true))	return true;
		else 	return false;
	}
	public void paintComponent(Graphics gr)
	{
		gr.setColor(backCol);
		gr.fillRect(0, 0,this.getWidth(), this.getHeight());
		if(on)
		{
			gr.setColor(col);
			gr.fillOval(0, 0,this.getWidth(), this.getHeight());
		}
	}
}
