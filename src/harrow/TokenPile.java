package harrow;

public class TokenPile {

	private int size;
	
	public TokenPile() {
		size = 0;
	}
	
	public int tokens() {
		return size;
	}
	
	public int takeTokens(int howMany) {
		int s=size;
		size=Math.max(0, size-howMany);
		return s-size; 
	}
	
	public void addTokens(int howMany) {
		size+=howMany;
	}
	
	public static void stealTokens(TokenPile t,TokenPile v, int howMany) {
		t.addTokens(v.takeTokens(howMany));
	}
	
	public int third() {
		return size/3;
	}
	
	public int twothird() {
		return 2*size/3;
	}
	
	public void giveTokens(TokenPile other,int howMany) {
		stealTokens(other, this, howMany);
	}
	
	public String toString() {
		return size + " tokens";
	}

	public void zeroTokens() {
		size = 0;
	}

	public void setTokens(int i) {
		size = i;
	}
}
