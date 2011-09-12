public class MovedPosition {
	private int oldX;
	private int oldY;
	private int newX;
	private int newY;
	private char c;
	
	public MovedPosition(char c, int oldX, int oldY, int newX, int newY) {
		this.c = c;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
	}

	public int getOldX() {
		return oldX;
	}

	public int getOldY() {
		return oldY;
	}

	public int getNewX() {
		return newX;
	}

	public int getNewY() {
		return newY;
	}

	public char getC() {
		return c;
	}

	@Override
	public String toString() {
		return "MovedPosition [oldX=" + oldX + ", oldY=" + oldY + ", newX="
				+ newX + ", newY=" + newY + ", c=" + c + "]";
	}
	
	
}