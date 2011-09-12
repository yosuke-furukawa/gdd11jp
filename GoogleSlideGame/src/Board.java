import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * ボードを表現するクラス
 * @author yosuke
 *
 */
public class Board {
	private final char[][] board;
	private String boardString;
	private int value;
	private int width;
	private int height;
	private MovedPosition position;
	
	/** zero の位置*/
	private int zeroX;
	private int zeroY;
	
	public Board(int width, int height, String boardString) {
		this.boardString = boardString;
		this.width = width;
		this.height = height;
		board = Util.createBoard(width, height, boardString);
		findZeroPosition();
	}
	
	
	
	private void findZeroPosition() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (board[y][x] == '0') {
					zeroX = x;
					zeroY = y;
					return;
				}
			}
		}
	}
	
	public void printBoard() {
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				System.out.print(" " + board[y][x]);
			}
			System.out.println();
		}
	}
	
	public void createString() {
		boardString = "";
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				boardString += board[y][x];
			}
		}
	}
	
	public boolean up() {
		if (zeroY > 0 && !isBlock(zeroX, zeroY - 1)) {
			swap(zeroX, zeroY, zeroX, zeroY - 1);
			zeroY = zeroY - 1;
		} else {
			return false;
		}
		return true;
	}
	
	public boolean left() {
		if (zeroX > 0 && !isBlock(zeroX - 1, zeroY)) {
			swap(zeroX, zeroY, zeroX - 1, zeroY);
			zeroX = zeroX - 1;
		} else {
			return false;
		}
		return true;
	}
	
	public boolean right() {
		if (zeroX < width && !isBlock(zeroX + 1, zeroY)) {
			swap(zeroX, zeroY, zeroX + 1, zeroY);
			zeroX = zeroX + 1;
		} else {
			return false;
		}
		return true;
	}

	public boolean down() {
		if (zeroY < height && !isBlock(zeroX, zeroY + 1)) {
			swap(zeroX, zeroY, zeroX, zeroY + 1);
			zeroY = zeroY + 1;
		} else {
			return false;
		}
		return true;
	}
	
	public boolean isBlock(int x, int y) {
		char c;
		if (x >= 0 && y >= 0 && x < width && y < height) {
			c = board[y][x];
			return c == '=';
		} else {
			return true;
		}

	}

	private void swap(int fromX, int fromY, int toX, int toY) {
		char from = board[fromY][fromX];
		char to = board[toY][toX];
		board[toY][toX] = from;
		board[fromY][fromX] = to;
		position = new MovedPosition(to, toX, toY, fromX, fromY);
	}
	
	public MovedPosition getPosition() {
		return position;
	}

	public String getBoardString() {
		createString();
		return boardString;
	}
	
	
	
	@Override
	public String toString() {
		return "Board [board=" + Arrays.toString(board) + ", boardString="
				+ boardString + ", value=" + value + ", width=" + width
				+ ", height=" + height + ", position=" + position + ", zeroX="
				+ zeroX + ", zeroY=" + zeroY + "]";
	}



	public static void main(String[] args) {
		StringTokenizer st = new StringTokenizer("3,3,120743586", ",");
		int width = Integer.parseInt(st.nextToken());
		int height = Integer.parseInt(st.nextToken());
		String panel = st.nextToken();
		Board board = new Board(width, height, panel);
		board.printBoard();
		System.out.println(board.getBoardString());
		
		System.out.println(board.down());
		board.printBoard();
		System.out.println(board.getBoardString());

		System.out.println(board.left());
		board.printBoard();
		System.out.println(board.getBoardString());
		System.out.println(board.up());
		board.printBoard();
		System.out.println(board.right());
		System.out.println(board.getBoardString());
		board.printBoard();
		
		Board board2 = new Board(width, height, panel);
		board2.printBoard();
		System.out.println(board2.getBoardString());
		System.out.println(board2.up());
		System.out.println(board2.position);
	}

}
