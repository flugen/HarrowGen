package harrow.actions;

import java.awt.Point;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import harrow.CardMap;
import harrow.Position;
import harrow.TokenPile;

public class GiveAction extends HarrowAction{
	
	private enum type{
		clock,
		most,
		least,
		all_cards,
		tome,
		hammer,
		all,
		nature,
		twocard,
		threecard,
		unknown;
	}
	
	public GiveAction(String actionString) {
		super(actionString);
		parse(actionString.toLowerCase());
	}

	protected type t;
	
	protected void parse(String s) {
		Matcher m = Pattern.compile("\\d").matcher(s);
		m.find();
		if(s.contains("clockwise")) { //easy one
//			m.find();
			amounts = new int[2];
			amounts[0] = Integer.parseInt(m.group());
			m.find();
			amounts[1] = Integer.parseInt(m.group());
			t = type.clock;
		}else if(s.contains("ability")) {
			if(s.contains("hammer")) {//give to all physical card
				amounts = new int[] {Integer.parseInt(m.group())};
				t = type.hammer;
			}else if(s.contains("tome")) {//steal from all mental card
				amounts = new int[] {Integer.parseInt(m.group())};
				t = type.tome;
			}else if(s.contains("every")) {
				amounts = new int[] {Integer.parseInt(m.group())};
				t = type.all_cards;
			}else if(s.contains("most")) {//steal from card with most tokens
				amounts = new int[] {Integer.parseInt(m.group())};
				t = type.most;
			}else if(s.contains("fewest")) {
				amounts = new int[] {Integer.parseInt(m.group())};
				t = type.least;
			}else {
				System.err.println("Unknown GIVE ABILITY action: "+s);
				t = type.unknown;
			}
		}else if(s.contains("all")) {
			targets = new Position[] {findPosition(s,1)};
			t = type.all;
		}else if(s.contains("body")) {
			int amt1 = Integer.parseInt(m.group());
			if(m.find()) {
				amounts = new int[] {amt1, Integer.parseInt(m.group())};
				targets = new Position[] {findPosition(s, 1),findPosition(s, 2)};
			}else {
				amounts = new int[] {amt1};
				targets = new Position[] {findPosition(s, 1),findPosition(s, 2),findPosition(s, 3)};
			}
			t = type.nature;
		}else if(s.contains("/3")) {
			amounts = new int[] {Integer.parseInt(m.group())};
			if(s.contains("rest")) {
				targets = new Position[] {findPosition(s, 1),findPosition(s, 2)};
				t = type.twocard;
			}else {
				targets = new Position[] {findPosition(s, 1),findPosition(s, 2),findPosition(s, 3)};
				t = type.threecard;
			}
		}else {
			System.err.println("Unknown GIVE action: "+s);
			t = type.unknown;
		}
	}
	
	
	private static final Position[] posArray = Position.values();
	private static final Pattern[] posStrings;
	static {
		posStrings = new Pattern[posArray.length];
		for (int i = 0; i < posArray.length; i++) {
			posStrings[i] = Pattern.compile(posArray[i].name());
		}
	}
	
	private Position findPosition(String s,int which) {
		if(which<1||which>posArray.length)
			return null;
		which--;
		Point[] placement = new Point[posArray.length];
		Matcher m;
		for (int i = 0; i < posStrings.length; i++) {
			m = posStrings[i].matcher(s);
			if(m.find())
				placement[i] = new Point(m.start(), i);
			else
				placement[i] = new Point(Integer.MAX_VALUE, i);
		}
		Arrays.sort(placement, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				if (o1.x==-1&&o2.x==-1)
					return 0;
				else if(o1.x==-1&&o2.x!=-1)
					return -1;
				else if(o2.x==-1&&o2.x!=-1)
					return 1;
				else
					return Integer.compare(o1.x, o2.x);
			}
		});
		
		return posArray[placement[which].y];
	}

	public void perform(CardMap map,Position self) {
		TokenPile sp = map.getTokens(self);
		switch (t) {
		case all:
			sp.giveTokens(map.getTokens(targets[0]), sp.tokens());
			break;
		case all_cards:
			for (Position p : posArray) {
				if (p==self) continue;
				if (p.isAttribute())
					sp.giveTokens(map.getTokens(p), amounts[0]);
			}
			break;
		case clock:
			sp.giveTokens(map.getTokens(self.getCW(amounts[1])), amounts[0]);
			break;
		case hammer:
		case tome:
			for (Position p : posArray) {
				if (p==self) continue;
				if ((t==type.hammer&&p.isPhysical())||(t==type.tome&&p.isMental()))
					sp.giveTokens(map.getTokens(p), amounts[0]);
			}
			break;
		case most:
			Position most=null;
			int max=-1;
			for (Position p:posArray) {
				if (p==self||!p.isAttribute())
					continue;
				int i = map.getTokens(p).tokens();
				if(i>max) {
					max = i;
					most = p;
				}
			}
			sp.giveTokens(map.getTokens(most), amounts[0]);
			break;
		case least:
			Position least=null;
			int min=Integer.MAX_VALUE;
			for (Position p : posArray) {
				if(p==self||!p.isAttribute()) continue;
				int i = map.getTokens(p).tokens();
				if(i<min) {
					min = i;
					least = p;
				}
			}
			sp.giveTokens(map.getTokens(least), amounts[0]);
			break;
		case nature:
			if(amounts.length==1) {
				for (Position p : targets) {
					sp.giveTokens(map.getTokens(p), amounts[0]);
				}
			}else {
				sp.giveTokens(map.getTokens(targets[0]), amounts[0]);
				sp.giveTokens(map.getTokens(targets[1]), amounts[1]);
			}
			break;
		case threecard:
			int howmany = sp.third();
			for(Position p:targets) {
				sp.giveTokens(map.getTokens(p), howmany);
			}
			break;
		case twocard:
			int onethird = sp.third();
			int twothird = sp.twothird();
			sp.giveTokens(map.getTokens(targets[0]), twothird);
			sp.giveTokens(map.getTokens(targets[1]), onethird);
		default:
			break;
		}
	}
	
}
