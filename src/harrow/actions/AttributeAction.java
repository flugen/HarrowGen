package harrow.actions;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import harrow.CardMap;
import harrow.Position;
import harrow.TokenPile;

public class AttributeAction extends HarrowAction {

	public AttributeAction(String actionString) {
		super(actionString);
		parse(actionString.toLowerCase());
	}
	
	protected void parse(String s) {
		Matcher m = Pattern.compile("\\w+").matcher(s);
		ArrayList<String> attr = new ArrayList<>();
		while(m.find()) {
			attr.add(m.group());
		}
		if (attr.isEmpty()) {
			System.err.println("Warning: no attributes found in ATTR action: "+s);
		}
		targets = new Position[attr.size()];
		for (int i = 0; i < targets.length; i++) {
			targets[i] = Position.valueOf(attr.get(i));
		}
	}
	

	@Override
	public void perform(CardMap map,Position self) {
		if(self==Position.spirit) {
			TokenPile tokens = map.getTokens(self);
			int howMany = tokens.third();
			for (Position position : targets) {
				TokenPile.stealTokens(map.getTokens(position), tokens, howMany);
			}
		}else if (self==Position.nurture) {
			TokenPile tokens = map.getTokens(self);
			TokenPile.stealTokens(map.getTokens(targets[0]), tokens, tokens.tokens());
			try {
				map.setStartCard(targets[0]);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					map.setStartCard(Position.strength);
				} catch (Exception e1) {}
			}
		}
	}

}
