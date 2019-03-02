package harrow.actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import harrow.CardMap;
import harrow.Position;
import harrow.TokenPile;

public class StealAction extends HarrowAction {

	public StealAction(String actionString) {
		super(actionString);
		parse(actionString.toLowerCase());
	}
	
	private enum type{
		clock,
		most,
		least,
		all,
		tome,
		hammer,
		unknown;
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
		}else if(s.contains("hammer")) {//steal from all physical card
			amounts = new int[] {Integer.parseInt(m.group())};
			t = type.hammer;
		}else if(s.contains("tome")) {//steal from all mental card
			amounts = new int[] {Integer.parseInt(m.group())};
			t = type.tome;
		}else if(s.contains("most")) {//steal from card with most tokens
			amounts = new int[] {Integer.parseInt(m.group())};
			t = type.most;
		}else if(s.contains("fewest")) {
			amounts = new int[] {Integer.parseInt(m.group())};
			t = type.least;
		}else if(s.contains("other")) {
			amounts = new int[] {Integer.parseInt(m.group())};
			t = type.all;
		}else {
			System.err.println("Unknown STEAL action: "+s);
			t = type.unknown;
		}
	}
	

	@Override
	public void perform(CardMap map,Position self) {
		switch(t) {
		case clock:
			Position other = self.getCW(amounts[1]);
			TokenPile.stealTokens(map.getTokens(self), map.getTokens(other), amounts[0]);
			break;
		case most:
			Position most=self;
			int max=-1;
			for (Position p : Position.values()) {
				if(p==self)
					continue;
				if(p.isAttribute()&&map.getTokens(p).tokens()>max) {
					max = map.getTokens(p).tokens();
					most = p;
				}
			}
			TokenPile.stealTokens(map.getTokens(self), map.getTokens(most), amounts[0]);
			break;
		case least:
			Position least=self;
			int min=Integer.MAX_VALUE;
			for (Position p : Position.values()) {
				if(p==self)
					continue;
				if(p.isAttribute()&&map.getTokens(p).tokens()<=min) {
					min = map.getTokens(p).tokens();
					least = p;
				}
			}
			TokenPile.stealTokens(map.getTokens(self), map.getTokens(least), amounts[0]);
			break;
		case all:
			for (Position p : Position.values()) {
				if(p==self||!p.isAttribute())
					continue;
				TokenPile.stealTokens(map.getTokens(self), map.getTokens(p), amounts[0]);
			}
			break;
		case hammer:
		case tome:
			for (Position p : Position.values()) {
				if(p==self) continue;
				if((t==type.hammer&&p.isPhysical())||(t==type.tome&&p.isMental()))
					TokenPile.stealTokens(map.getTokens(self), map.getTokens(p), amounts[0]);
			}
			break;
		default:
			break;
		}
	}

}
