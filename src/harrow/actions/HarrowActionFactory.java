package harrow.actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import harrow.CardMap;
import harrow.Position;

public class HarrowActionFactory {

	public static HarrowAction parseAction(String actionString) {
		actionString = actionString.trim();
		
		Matcher m = Pattern.compile("\\w+").matcher(actionString.toLowerCase());
		
		if(!m.find())
			return null;
		
		String firstword = m.group();
		
		HarrowAction ha;
		
		switch(firstword) {
		case "give":
			ha = new GiveAction(actionString);
			break;
		case "steal":
			ha = new StealAction(actionString);
			break;
		case "read":
			ha = new ReadAction(actionString);
			break;
		default:
			if(Position.valueOf(firstword)!=null) {
				ha = new AttributeAction(actionString);
				break;
			}
			System.err.println("Unknown action: "+actionString);
			ha = new HarrowAction(actionString) {
				
				@Override
				public void perform(CardMap map,Position self) {
					System.out.println("I do nothing!");
				}
			};
			break;
		}
		
		return ha;
	}

}
