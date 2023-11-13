package cirTT;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CirTT{
	private static final Random rgen=new Random();
	public static final Color[] colt= {new Color(255,0,0),new Color(0,255,0),new Color(0,0,255),new Color(255,255,0),new Color(0,255,255),new Color(255,0,255)};
	public static final int addN=3;
	public static final int chkN=5;
	
	public final Cir[][] tt;
	public final Cir[] nextCirT;
	
	private int cirCount=0;
	private Cir sel=null;
	
	public CirTT(int siz,ActionListener cirListener) {
		tt=new Cir[siz][siz];
		for(int i=0;i<tt.length;i++)
			for(int j=0;j<tt.length;j++)
				{
					tt[i][j]=new Cir(i,j,colt[rgen.nextInt(colt.length)]);
					tt[i][j].addActionListener(cirListener);
				}
		
		nextCirT=new Cir[addN];
		for(int i=0;i<nextCirT.length;i++)
		{
			nextCirT[i]=new Cir(0,0,colt[rgen.nextInt(colt.length)]);
			nextCirT[i].setOn(true);
		}
		addCir();
	}
	
	public boolean addCir() 
	{
		if(cirCount+addN>tt.length*tt.length)	return false;
		else
		{
			Cir []t=new Cir[tt.length*tt.length];
			int te=0;
			for(int i=0;i<tt.length;i++)
				for(int j=0;j<tt[i].length;j++)
					if(tt[i][j].getOn()==false)
						t[te++]=tt[i][j];
			for(int i=0;i<nextCirT.length;i++)
			{
				int r=rgen.nextInt(te);
				while(t[r].getOn()==true)	r=rgen.nextInt(te);
				t[r].setColor(nextCirT[i].getColor());
				t[r].setOn(true);
				nextCirT[i].setColor(colt[rgen.nextInt(colt.length)]);
			}
			cirCount+=addN;
		}
		return true;
	}
	
	public boolean select(Cir c)
	{
		c.select();
		if(sel==null)	sel=c;
		else if(sel==c)	sel=null;
		else
		{
			int[][]rtt=new int[tt.length][tt[0].length];
			for(int i=0;i<tt.length;i++)
				for(int j=0;j<tt[i].length;j++)
					rtt[i][j]=(tt[i][j].getOn())?-1:0;
			rtt[c.x][c.y]=0;
			chkSwap(rtt,sel.x,sel.y);
			
			if(rtt[c.x][c.y]==1)
			{
				Cir.swap(sel,c);
				c.select();
				sel.select();
				sel=null;
				return true;
			}
			else
			{
				c.select();
				sel.select();
				sel=null;
			}
		}
		return false;
	}
	
	private void chkSwap(int[][]rtt,int i,int j) 
	{
		rtt[i][j]=1;
		if((i-1>=0)&&(rtt[i-1][j]==0))				chkSwap(rtt,i-1,j);
		if((j-1>=0)&&(rtt[i][j-1]==0))				chkSwap(rtt,i,j-1);
		if((i+1<rtt.length)&&(rtt[i+1][j]==0))		chkSwap(rtt,i+1,j);
		if((j+1<rtt[i].length)&&(rtt[i][j+1]==0))	chkSwap(rtt,i,j+1);
	}
	
	public int chk()
	{
		int c,pkt=0;
		for(int i=0;i<tt.length;i++)
			for(int j=0;j<tt[i].length;j++)
			{
				c=1;
				while((j+c<tt[i].length)&&(tt[i][j].equals(tt[i][j+c])))	c++;
				if(c>=chkN)
				{
					pkt+=c;
					cirCount-=c;
					for(;c>0;c--,j++)
						tt[i][j].setOn(false);
				}
			}
		
		for(int j=0;j<tt[0].length;j++)
			for(int i=0;i<tt.length;i++)
			{
				c=1;
				while((i+c<tt.length)&&(tt[i][j].equals(tt[i+c][j])))	c++;
				if(c>=chkN)
				{
					pkt+=c;
					cirCount-=c;
					for(;c>0;c--,i++)
						tt[i][j].setOn(false);
				}
			}
		
		for(int i=0;i<tt.length;i++)
			for(int j=0;j<tt[i].length;j++)
			{
				c=1;
				while((i+c<tt.length)&&(j+c<tt[i].length)&&(tt[i][j].equals(tt[i+c][j+c])))	c++;
				if(c>=chkN)
				{
					pkt+=c;
					cirCount-=c;
					for(;c>0;c--,i++,j++)
						tt[i][j].setOn(false);
				}
			}
		
		for(int i=0;i<tt.length;i++)
			for(int j=tt[i].length-1;j>=0;j--)
			{
				c=1;
				while((i+c<tt.length)&&(j-c>=0)&&(tt[i][j].equals(tt[i+c][j-c])))	c++;
				if(c>=chkN)
				{
					pkt+=c;
					cirCount-=c;
					for(;c>0;c--,i++,j--)
						tt[i][j].setOn(false);
				}
			}
		
		return pkt;
	}
	
}
