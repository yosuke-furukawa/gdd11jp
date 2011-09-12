import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Util {
	private static BufferedWriter LOG;
	private Util(){}
	
	public static void writeLog(String log) {
		try {
		if (LOG == null) {
			LOG = new BufferedWriter(new FileWriter(new File("/Users/yosuke/Documents/workspace/GoogleSlideGame3/text/log.txt")));
		}
		LOG.write(log + "\n");
		LOG.flush();
		} catch(Exception e) {
			
		}
	}
	
	public static char[][] createBoard(int width, int height, String boardString) {
		char[][] board = new char[height][width];
		for (int y = 0; y < height; y++) {
			int x = 0;
			for (int i = y * width; i < (y + 1) * width; i++) {
				board[y][x] = boardString.charAt(i);
				x++;
			}
		}
		return board;
	}
	
	public static String createAnswerPanel(String boardString) {
		String correctPanelString = "";
		List<Character> list = new ArrayList<Character>();
		List<Integer> blockPosList = new ArrayList<Integer>();
		for (int i = 0; i < boardString.length(); i++) {
			if (boardString.charAt(i) == '=') {
				blockPosList.add(i);
			} else {
				if (boardString.charAt(i) != '0') {
					list.add(boardString.charAt(i));
				}
			}
		}
		Collections.sort(list);
		for (Integer i : blockPosList) {
			list.add(i, '=');
		}
		for (Character c : list) {
			correctPanelString += c;
		}
		correctPanelString += '0';
		return correctPanelString;
	}
	
	public static void closeLog() throws IOException {
		if (LOG != null) {
			LOG.flush();
			LOG.close();
		}
	}
}
