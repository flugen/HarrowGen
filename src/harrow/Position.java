package harrow;

public enum Position{
	nature,
	spirit,
	body,
	mind,
	nurture,
	strength,
	dexterity,
	constitution,
	intelligence,
	wisdom,
	charisma;
	
	public Position getCW(int amount) {
		if (amount==0)
			return this;
		else if (amount>0)
			amount--;
		else
			amount++;
		Position next;
		switch(this) {
		case body:
		case mind:
		case nature:
		case nurture:
		case spirit:
			return this;
		case strength:
			next = amount>0?intelligence:dexterity;
			break;
		case dexterity:
			next = amount>0?strength:constitution;
			break;
		case constitution:
			next = amount>0?dexterity:charisma;
			break;
		case intelligence:
			next = amount>0?wisdom:strength;
			break;
		case wisdom:
			next = amount>0?charisma:intelligence;
			break;
		case charisma:
			next = amount>0?constitution:wisdom;
			break;
		default:
			return null;
		}
		return next.getCW(amount);
	}

	
	private static Position[] all=Position.values();
	public static Position valueOf(int i) {
		if (i<0||i>all.length) return null;
		return all[i];
	}
	
	public boolean isAttribute() {
		switch(this) {
		case nature:
		case body:
		case spirit:
		case mind:
		case nurture:
			return false;
		default:
			return true;
		}
	}
	
	public boolean isPhysical() {
		switch(this) {
		case strength:
		case dexterity:
		case constitution:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isMental() {
		switch(this) {
		case intelligence:
		case wisdom:
		case charisma:
			return true;
		default:
			return false;
		}
	}
}