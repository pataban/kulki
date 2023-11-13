package kulkikulki_main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import cirTT.*;
import java.io.*;

class PSI implements Serializable{
	static final long serialVersionUID=1L;
	String a;
	int b;
	public PSI() {
		a="abc";
		b=0;
	}
	public PSI(String a, int b) {
		this.a = a;
		this.b = b;
	}
	public String toString() {
		return b+" \t"+a;
	}
}

public class kulki_main {
	static final int gridsiz=10;
	static final int gridVGap=2;
	static final int gridHGap=2;
	static final int emptyBorderSiz=20;
	static final int highscoreSiz=10;
	static PSI[] highscore;
	JFrame frame;
	CirTT cir;
	int pkt=0;
	JLabel scoreLabel;
	JTextField nameIn;
	
	public static void main(String[] args){
		highscore=new PSI[highscoreSiz];
		try(ObjectInputStream oin=new ObjectInputStream(new FileInputStream("highscore.ser"))) {
			for(int i=0;i<highscoreSiz;i++)
				highscore[i]=(PSI)oin.readObject();
		}
		catch(FileNotFoundException ex) {
			try(ObjectOutputStream oout=new ObjectOutputStream(new FileOutputStream("highscore.ser"))) {
				for(int i=0;i<highscoreSiz;i++) {
					highscore[i]=new PSI();
					oout.writeObject(highscore[i]);
				}				
			}catch(Exception e) {e.printStackTrace();}
		}
		catch(Exception ex)	{ex.printStackTrace();}
		
		new kulki_main().start();
	}
	
	public void start()
	{
		frame=new JFrame("Kulki");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Box backgr=new Box(BoxLayout.X_AXIS);
		backgr.setBorder(BorderFactory.createEmptyBorder(emptyBorderSiz, emptyBorderSiz, emptyBorderSiz, emptyBorderSiz));
		frame.getContentPane().add(BorderLayout.CENTER, backgr);
		JPanel mainPanel=new JPanel(new GridLayout(gridsiz,gridsiz,gridHGap,gridVGap));
		backgr.add(mainPanel);
		
		cir=new CirTT(gridsiz,new CirListener());
		for(int i=0;i<cir.tt.length;i++)
			for(int j=0;j<cir.tt.length;j++)
				mainPanel.add(cir.tt[i][j]);
		//mainPanel.setSize(Cir.SIZ*(gridsiz+gridHGap)-gridHGap, Cir.SIZ*(gridsiz+gridVGap)-gridVGap);
	
		Box eastMainBox=new Box(BoxLayout.Y_AXIS);
		eastMainBox.setBorder(BorderFactory.createEmptyBorder(0, emptyBorderSiz, 0, 0));
		backgr.add(eastMainBox);
		
		Box nextCirBox=new Box(BoxLayout.X_AXIS);
		eastMainBox.add(new JLabel("Next:"));
		eastMainBox.add(nextCirBox);
		for(int i=0;i<cir.nextCirT.length;i++)
			nextCirBox.add(cir.nextCirT[i]);
		//nextCirBox.setSize(cir.nextCirT.length*Cir.SIZ, Cir.SIZ);
		
		scoreLabel=new JLabel("Score: "+pkt);
		eastMainBox.add(scoreLabel);
		
		eastMainBox.add(new JLabel("Best:"));
		JList<PSI> highscoreList=new JList<PSI>(highscore);
		eastMainBox.add(highscoreList);
		
		JButton surrenderButton=new JButton("Surrender");
		surrenderButton.addActionListener(new SurrenderListener());
		eastMainBox.add(surrenderButton);
		
		frame.setBounds(100,100,600, 600);
		frame.pack();
		frame.setVisible(true);
	}
	
	class CirListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			if(cir.select((Cir)ev.getSource()))
			{
				pkt+=cir.chk();
				scoreLabel.setText(""+pkt);
				if(!cir.addCir())	stop();
			}
		}
	}	
	
	class SurrenderListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			stop();
		}
	}
	
	public void stop() {
		//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		//frame=new JFrame("Kulki");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().removeAll();
		
		JLabel score=new JLabel("You scored "+pkt+" points");
		frame.add(BorderLayout.NORTH,score);

		int n=highscoreSiz-1;
		for(;n>=0;n--)	
			if(highscore[n].b>=pkt){
				break;
			}
		if(n<highscoreSiz-1){
			Box HBox=new Box(BoxLayout.Y_AXIS);
			HBox.add(new JLabel("You made Highscore nr."+(n+2)));
			nameIn=new JTextField();
			nameIn.setColumns(1);
			HBox.add(nameIn);
			nameIn.requestFocus();
			JButton saveButton=new JButton("Save");
			saveButton.addActionListener(new SaveHighscoreListener());
			HBox.add(saveButton);
			frame.add(BorderLayout.CENTER,HBox);
		}
		
		JPanel buttonPanel = new JPanel();
		JButton restartButton = new JButton("Restart");
		restartButton.addActionListener(new RestartListener());
		buttonPanel.add(restartButton);
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ExitListener());
		buttonPanel.add(exitButton);

		frame.add(BorderLayout.SOUTH,buttonPanel);
		frame.setBounds(300,300,200,100);
		frame.pack();
		frame.setVisible(true);
	}
	
	class SaveHighscoreListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			int n=highscoreSiz-1;
			for(;n>0;n--) 
				if(highscore[n-1].b<pkt)	highscore[n]=highscore[n-1];
				else{
					highscore[n]=new PSI(nameIn.getText(),pkt);
					break;
				}
			if(n==0)	highscore[0]=new PSI(nameIn.getText(),pkt);
			nameIn.setText("");
			try(ObjectOutputStream oout=new ObjectOutputStream(new FileOutputStream("highscore.ser"))) {
				for(int i=0;i<highscoreSiz;i++)
					oout.writeObject(highscore[i]);
			}catch(Exception ex) {ex.printStackTrace();}
			frame.getContentPane().remove(1);
			frame.repaint();
		}
	}
	
	class RestartListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			frame=null;
			cir=null;
			new kulki_main().start();
		}
	}
	
	class ExitListener implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			System.exit(0);
		}
	}
	
}
