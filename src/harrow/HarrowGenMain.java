package harrow;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import harrow.actions.HarrowAction;

public class HarrowGenMain extends JFrame{

	public HarrowGenMain(){
		setTitle("Harrow Deck Character Generation");
		setSize(2200,1700);
		
		deck = HarrowDeck.getDeck();
		
		content = getContentPane();
		content.setLayout(new GridBagLayout());
		
		cm = new CardMap();
		
		JPanel viewPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		c.gridheight = 3;
		viewPanel.add(cm, c);
		
		Font f = new Font(Font.SANS_SERIF, 0, 40);
		
		text = new JTextPane();
//		text.setPreferredSize(new Dimension(1000, 1000));
		
		text.setFont(f);
		jsp = new JScrollPane(text);
		jsp.getViewport().setMinimumSize(new Dimension(500,500));
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.validate();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridy = 4;
		c.gridheight = 3;
		viewPanel.add(jsp,c);
		
		
		c.gridx = 0;c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = 2;
		c.gridheight = 5;
		c.fill = GridBagConstraints.BOTH;
		content.add(viewPanel,c);
		
		
		dealB = new JButton("Deal Cards");
		dealB.setFont(f);
		computeB = new JButton("Compute Scores");
		computeB.setFont(f);
		
		
		dealB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dThread = new DealThread(deck, cm, computeB);
				dThread.start();
			}
		});
		
		computeB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cThread = new ComputeThread(cm, dealB, text,jsp);
				cThread.start();
			}
		});
		computeB.setEnabled(false);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		buttonPanel.add(dealB);
		buttonPanel.add(computeB);
		
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridheight = 1;
		
		content.add(buttonPanel,c);
		this.validate();
	}
	
	protected Container content;
	protected JButton dealB,computeB;
	protected JTextPane text;
	protected JScrollPane jsp;
	protected HarrowDeck deck;
	protected CardMap cm;
	protected DealThread dThread;
	protected ComputeThread cThread;
	private static final long serialVersionUID = 1L;
	

	
	static class DealThread extends Thread{
		protected HarrowDeck deck;
		protected CardMap cm;
		protected JButton computeB;
		public DealThread(HarrowDeck d, CardMap c, JButton cB) {
			deck = d;
			cm = c;
			computeB = cB;
		}
		
		@Override
		public void run() {
			Position[] positions = Position.values();
			computeB.setEnabled(false);
			cm.clearCards();
			deck.shuffle();
			for (int i = 0; i < positions.length; i++) {
				cm.addCard(positions[i], deck.draw());
				try {
					sleep(500);
				}catch (InterruptedException e) {
					
				}
			}
			computeB.setEnabled(true);
		}
	}
	
	static class ComputeThread extends Thread{
		protected CardMap cm;
		protected JButton drawB;
		protected JTextPane text;
		protected pbt type;
		protected JScrollPane jsp;
		
		protected enum pbt{
			low,
			challenge,
			standard,
			original,
			high;
		}
		
		public ComputeThread(CardMap c, JButton dB, JTextPane t,JScrollPane j) {
			cm = c;
			drawB = dB;
			text = t;
			jsp = j;
		}
		
		@Override
		public void run() {
			Position[] positions = Position.values();
			drawB.setEnabled(false);
			
			String[] options = new String[] {"Low (10 pt buy)","Challenge (15 pt buy)",
					"Standard (20 pt buy)","Original (25 pt buy)","High-powered (30 pt buy)"};
			String opt = (String) JOptionPane.showInputDialog(cm, "Choose point buy amount", "Point buy type", JOptionPane.QUESTION_MESSAGE, null, options, options[3]);
			if (opt==null) {
				drawB.setEnabled(true);
				return;
			}
			
			text.setText("");
			
			int i;
			for (i = 0; i < options.length; i++) {
				if (opt.equals(options[i]))
					break;
			}
			type = (pbt.values())[i];
			
			cm.clearTokens();
			
			switch(type) {
			case low:
				cm.getTokens(Position.nature).addTokens(9);
				cm.getTokens(Position.spirit).addTokens(3);
				cm.getTokens(Position.nurture).addTokens(3);
				break;
			case challenge:
				cm.getTokens(Position.nature).addTokens(9);
				cm.getTokens(Position.spirit).addTokens(3);
				cm.getTokens(Position.nurture).addTokens(2);
				cm.getTokens(Position.strength).addTokens(1);
				cm.getTokens(Position.dexterity).addTokens(1);
				cm.getTokens(Position.constitution).addTokens(1);
				cm.getTokens(Position.intelligence).addTokens(1);
				cm.getTokens(Position.wisdom).addTokens(1);
				cm.getTokens(Position.charisma).addTokens(1);
				break;
			case standard:
				cm.getTokens(Position.nature).addTokens(9);
				cm.getTokens(Position.spirit).addTokens(6);
				cm.getTokens(Position.nurture).addTokens(4);
				cm.getTokens(Position.strength).addTokens(1);
				cm.getTokens(Position.dexterity).addTokens(1);
				cm.getTokens(Position.constitution).addTokens(1);
				cm.getTokens(Position.intelligence).addTokens(1);
				cm.getTokens(Position.wisdom).addTokens(1);
				cm.getTokens(Position.charisma).addTokens(1);
				break;
			case original:
				cm.getTokens(Position.nature).addTokens(9);
				cm.getTokens(Position.spirit).addTokens(6);
				cm.getTokens(Position.nurture).addTokens(4);
				cm.getTokens(Position.strength).addTokens(3);
				cm.getTokens(Position.dexterity).addTokens(3);
				cm.getTokens(Position.constitution).addTokens(3);
				cm.getTokens(Position.intelligence).addTokens(3);
				cm.getTokens(Position.wisdom).addTokens(3);
				cm.getTokens(Position.charisma).addTokens(3);
				break;
			case high:
				cm.getTokens(Position.nature).addTokens(9);
				cm.getTokens(Position.spirit).addTokens(6);
				cm.getTokens(Position.nurture).addTokens(3);
				cm.getTokens(Position.strength).addTokens(4);
				cm.getTokens(Position.dexterity).addTokens(4);
				cm.getTokens(Position.constitution).addTokens(4);
				cm.getTokens(Position.intelligence).addTokens(4);
				cm.getTokens(Position.wisdom).addTokens(4);
				cm.getTokens(Position.charisma).addTokens(4);
				break;
			}
			
			int sleepAmount = 200; 
			
			cm.repaint();
			try {
				Thread.sleep(sleepAmount);
			}catch(InterruptedException e) {}
			
			
			HarrowAction ha;
			HarrowCard card;
			for (i = 0; i <= Position.nurture.ordinal(); i++) {
				card = cm.getCard(positions[i]);
				ha = card.getAction();
				println(card.name()+": "+ha);
				ha.perform(cm, positions[i]);
				cm.repaint();
				try {
					sleep(sleepAmount);
				}catch (InterruptedException e) {
					
				}
			}
			
			Position p = cm.getStartCard();
			
			if(p==null) {
				System.err.println("Start card was not set!");
				drawB.setEnabled(true);
				return;
			}
			
			for (i = 0; i <= 5; i++) {
				card = cm.getCard(p);
				ha = card.getAction();
				println(card.name()+": "+ha);
				ha.perform(cm, p);
				cm.repaint();
				try {
					sleep(sleepAmount);
				}catch(InterruptedException e) {}
				p = p.getCW(-1);
			}
			
			Point[] pts = new Point[6];
			for (i = Position.strength.ordinal(); i <= Position.charisma.ordinal(); i++) {
				pts[i-Position.strength.ordinal()] = 
						new Point(cm.getTokens(Position.valueOf(i)).tokens(), i);
			}
			
			Arrays.sort(pts,new Comparator<Point>() {
				public int compare(Point p1,Point p2) {
					if (p1.x<p2.x)
						return 1;
					else if(p1.x==p2.x)
						return 0;
					else
						return -1;
				}
			});
			boolean[] flags=new boolean[pts.length];
			for (i = 1; i < pts.length; i++) {
				if(pts[i-1].x==pts[i].x)
					flags[i]=true;
			}
			
			int l=0;
			for (i = 0; i < pts.length; i++) {
				if (flags[i])
					l += updateAttr(Position.valueOf(pts[i].y), 0);
				else
					l = updateAttr(Position.valueOf(pts[i].y), l);
			}
			
			int sum = 0;
			for (i = Position.strength.ordinal(); i <= Position.charisma.ordinal(); i++) {
				sum+=printAttr(Position.valueOf(i));
				
			}
			
			println("Sum total: "+sum);
			
			drawB.setEnabled(true);
		}
		
		private int updateAttr(Position p,int leftover) {
			TokenPile tp = cm.getTokens(p);
			int tokens = tp.tokens();
			switch(type) {
			case low:
			case challenge:
			case standard:
				tokens += 2;
				break;
			default:  break;
			}
			int i=getScoreIndex(tokens+leftover);
//			println(p.toString().toUpperCase()+": "+tp+" -> score: "+getScoreString(i));
			int diff = tokens-costs[i];
			tp.setTokens(costs[i]);
			return diff;
		}
		
		private int printAttr(Position p) {
			int tokens = cm.getTokens(p).tokens();
			println(p.toString().toUpperCase().substring(0, 3)+": "+tokens+" tokens\t->\tscore: "+getScoreString(getScoreIndex(tokens)));
			return scores[getScoreIndex(tokens)];
		}
		
		private String getScoreString(int i) {
			return scores[i] +(i<1?"(":"(+")+mods[i]+")";
		}
		private static int[] mods = new int[] {-1,-1,0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5};
		private static int[] scores = new int[] {8,9,10,11,12,13,14,15,16,17,18,19,20};
		private static int[] costs = new int[]  {0,1,2,3,4,5,7,9,12,15,19,23,28};
		private int getScoreIndex(int tokens) {
			int i=0;
			for (i=0;tokens>costs[i]&&i<costs.length;i++);
			i=Math.max(0, i-1);
			return i; 
		}
		
		private void println(String s) {
			text.setText(text.getText()+s+"\n");
			scrollToBottom();
			System.out.println(s);
		}
		
		protected void scrollToBottom()
		{
		    SwingUtilities.invokeLater(
		        new Runnable()
		        {
		            public void run()
		            {
		                jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
		            }
		        });
		}
		
	}

	public static void main(String[] args) {
		HarrowGenMain hgm = new HarrowGenMain();
		hgm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hgm.setVisible(true);
	}

}
