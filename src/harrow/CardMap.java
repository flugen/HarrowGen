package harrow;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CardMap extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private CardButton[] map;
	private GridLayout gl;
	private static HashMap<Position, Integer> layout;
	protected Position start;
	
	static {
		layout = new HashMap<>();
		Position[] lay = {Position.strength,null,Position.nature,null,Position.intelligence,
				Position.dexterity,Position.body,Position.spirit,Position.mind,Position.wisdom,
				Position.constitution,null,Position.nurture,null,Position.charisma};
		for (int ii = 0; ii < lay.length; ii++) {
			if (lay[ii]!=null) {
				layout.put(lay[ii], ii);
			}
		}
	}
	
	public CardMap() {
		//setSize(500,500);
		Position[] positions = Position.values();
		map = new CardButton[positions.length];
		gl = new GridLayout(3, 5, 10, 5);
		setLayout(gl);
		
		for (int i = 0; i < 15; i++) {
			this.add(new JLabel());
		}
		Component temp;
		int pos;
		for (int i = 0; i < map.length; i++) {
			pos = layout.get(positions[i]);
			map[i] = new CardButton(pos);
			
			map[i].setPosition(positions[i]);
			temp = this.getComponent(pos);
			this.add(map[i],pos);
			this.remove(temp);
		}
		
		
	}
	
	public HarrowCard getCard(Position p) {
		return map[p.ordinal()].card();
	}
	
	public void addCard(Position p, HarrowCard c) {
		System.out.println("Adding card "+c.name()+" into the "+p+" slot.");
		map[p.ordinal()].setCard(c);
		c.setPosition(p);
	}
	
	public void clearCards() {
		for (int i = 0; i < map.length; i++) {
			map[i].setCard(null);
		}
	}
	
	public void setStartCard(Position p) throws Exception{
		switch(p) {
		case body:
		case mind:
		case nature:
		case nurture:
		case spirit:
			throw new Exception("The "+p+" position is not a valid start position");
		default:
			start = p;
		}
	}
	
	public Position getStartCard() {
		return start;
	}
	
	public TokenPile getTokens(Position p) {
		return map[p.ordinal()].tokens();
	}
	
	public void clearTokens() {
		for (int i = 0; i < map.length; i++) {
			map[i].tokens.zeroTokens();
		}
	}
	
	public void repaint() {
		super.repaint();
		if (map==null)
			return;
		for (int i = 0; i < map.length; i++) {
			map[i].repaint();
		}
	}
	
	class CardButton extends JButton{

		private static final long serialVersionUID = 1L;
		private HarrowCard card;
		private Position pos;
		protected TokenPile tokens;
		
 		protected CardButton(int index) {
			setBlankIcon();
			tokens = new TokenPile();
			setFont(new Font(Font.SANS_SERIF, 0, 30));
			addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
					System.out.println("The "+pos+" slot holds "+card.name()+": "+card.description()+"\n"
							+"and "+tokens);
					}catch (Exception e2) {
						// No card
					}
				}
			});
		}
		
		protected void setPosition(Position p) {
			pos = p;
		}
		
		public void repaint() {
			super.repaint();
			if(tokens!=null)
				setText(tokens.toString());
		}
		
		protected void setBlankIcon() {
			setIcon(new ImageIcon(HarrowDeck.getDefaultImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT),""));
		}
		
		protected void setCard(HarrowCard c) {
			card = c;
			if (card!=null) {
				setIcon(new ImageIcon(card.image().getScaledInstance(300, 300, Image.SCALE_DEFAULT), card.description()));
				setToolTipText(c.description());
			}else {
				setBlankIcon();
				setToolTipText(null);
			}
		}
		
		public TokenPile tokens() {
			return tokens;
		}
		
		public HarrowCard card() {
			return card;
		}
		
		public Position position() {
			return pos;
		}
		
	}

}
