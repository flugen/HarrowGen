package harrow.actions;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import harrow.CardMap;
import harrow.HarrowCard;
import harrow.Position;

public class ReadAction extends HarrowAction {

	public ReadAction(String actionString) {
		super(actionString);
		parse(actionString.toLowerCase());
	}
	
	protected void parse(String s) {
		targets = new Position[1];
		Matcher m=Pattern.compile("read as if the card in the (\\w+)").matcher(s);
		if(!m.find()) {
			System.err.println("Warning: could not parse READ action: "+s);
		}
		targets[0] = Position.valueOf(m.group(1));
	}
	

	@Override
	public void perform(CardMap map,Position self) {
		HarrowAction ha = map.getCard(targets[0]).getAction(Position.nurture);
		if(ha instanceof ReadAction) {
			ArrayList<Position> remaining = new ArrayList<>(Position.values().length);
			for (int i = 0; i < Position.values().length; i++) {
				remaining.add(i, Position.valueOf(i));
			}
			remaining.remove(self);
			remaining.remove(targets[0]);
			HarrowCard[] cards = new HarrowCard[remaining.size()];
			for (int i = 0; i < cards.length; i++) {
				cards[i] = map.getCard(remaining.get(i));
			}
			HarrowCard destiny = 
					(HarrowCard) JOptionPane.showInputDialog(map, "You can choose your own destiny! What will you choose?", "Your destiny awaits...", JOptionPane.QUESTION_MESSAGE, null, cards, cards[0]);
			if(destiny==null) {
				destiny = cards[new Random().nextInt(cards.length)];
			}
			destiny.getAction(self).perform(map, self);
		} else 
			ha.perform(map,Position.nurture);
	}

}
