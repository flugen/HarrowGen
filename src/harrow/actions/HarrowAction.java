package harrow.actions;

import harrow.CardMap;
import harrow.Position;

public abstract class HarrowAction {
	
	protected Position[] targets;
	
	protected int[] amounts;
	
	protected String action;
	
	public HarrowAction(String action) {
		this.action = action;
	}
	
	public abstract void perform(CardMap map,Position self);
	
	public String toString() {
		return action;
	}

}
