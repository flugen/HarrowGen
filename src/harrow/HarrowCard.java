package harrow;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import harrow.actions.HarrowAction;
import harrow.actions.HarrowActionFactory;

public class HarrowCard {
	
	public enum Suit{
		hammer,key,shield,tome,star,crown;
		public boolean isPhysical() {
			return this==hammer||this==key||this==Suit.shield;
		}
	}
	public enum Alignment{
		LG,NG,CG,
		LN,NN,CN,
		LE,NE,CE;
		public char getLaw() {
			return this.name().charAt(0);
		}
		public char getGood() {
			return this.name().charAt(1);
		}
	}
	
	private String card;
	private String name;
	private Suit suit;
	private Alignment align;
	private String description;
	private HashMap<Position, HarrowAction> actions;
	private BufferedImage image;
	private Position pos;
	
	public HarrowCard(String n, String c, Alignment al, String d, Suit s, BufferedImage i, String actionsString) {
		this(n,c,al,d,s,i,readActions(actionsString));
	}
	
	public HarrowCard(String n, String c, Alignment al, String d, Suit s, BufferedImage i, HashMap<Position, HarrowAction> ac){
		card = c;
		name = n;
		suit = s;
		align = al;
		description = d;
		image = i;
		actions = ac;
	}
	
	public String card(){ return card; }
	public String name(){ return name; }
	public String description(){ return description; }
	public Suit suit() { return suit; }
	public Alignment alignment() { return align; }
	public BufferedImage image(){ return image; }
	public void setImage(BufferedImage i) { image = i; }
	public HarrowAction getAction(){
		if (actions.containsKey(pos))
			return actions.get(pos);
		else return null;
	}
	
	public HarrowAction getAction(Position p) {
		if(actions.containsKey(p))
			return actions.get(p);
		return null;
	}
	
	public String toString() {
		return name+": "+description;
	}
	
	private static HashMap<Position, HarrowAction> readActions(String actions){
		String[] acts = actions.split("\t");
		HashMap<Position, HarrowAction> map = new HashMap<>();
		for (int i=0;i<acts.length-1;i++) {
			map.put(Position.valueOf(i), HarrowActionFactory.parseAction(acts[i]));
		}
		HarrowAction ha = HarrowActionFactory.parseAction(acts[acts.length-1]);
		for (int i=0;i<6;i++) {
			map.put(Position.valueOf(i+Position.strength.ordinal()), ha);
		}
		return map;
	}

	public void setPosition(Position p) {
		pos = p;
	}
	
}
