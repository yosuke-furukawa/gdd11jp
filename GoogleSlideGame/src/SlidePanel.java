import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 
 * @author yosuke
 * 
 */
public class SlidePanel {
	private int width;
	private int height;
	private String panel = "";
	private static String correctPanelString;
	private List<List<Character>> panelList = new ArrayList<List<Character>>();
	private static List<List<Character>> correctList;
	private String operation = "";
	private static String operationAnswer = "";
	private static String answerString = "";
	private static List<String> ansCandidateList = null;
	private static Map<String, String> historyOperationMap = new HashMap<String, String>();
	private static Map<String, Integer> historyEvalMap = new HashMap<String, Integer>();
	private static long startmills;
	private int zeroX;
	private int zeroY;
	private int count;
	private static int min = -1;
	private static int alpha = 0;
	private static long MAXTIME = 30;


	private final static int UP_OP = 0;
	private final static int LEFT_OP = 1;
	private final static int RIGHT_OP = 2;
	private final static int DOWN_OP = 3;
	private final static int MAX_COUNT = Integer.MAX_VALUE;
	private final static int MAX_LIST_SIZE = Integer.MAX_VALUE;

	private static int MAX_LEFT;
	private static int MAX_RIGHT;
	private static int MAX_UP;
	private static int MAX_DOWN;
	private static int MAX_TEST_COUNT;
	
	private static int maxDistance = 0;

	SlidePanel(int width, int height, String panel) {
		this.width = width;
		this.height = height;
		this.panel = panel;
		createPanel();
		correctPanelString = "";
		createAnswerPanel();
		correctList = new ArrayList<List<Character>>();
		createCorrectPanel();
		findZeroPosition();
		
		if (min < 0) {
			min = differenceFromAnswer();
		}
	}
	
	public static void initialize() {
		correctPanelString = "";
		correctList = null;
		operationAnswer = "";
		answerString = "";
		ansCandidateList = null;
		historyOperationMap = new HashMap<String, String>();
		historyEvalMap = new HashMap<String, Integer>();
		min = -1;
	}

	public void solveSlidePanel() {
		startmills = System.currentTimeMillis();
//		if (width >= 6 || height >= 6) {
//			//今は諦める・・・。
//			return;
//		}
		List<String> list = Arrays.asList(panel);
		historyOperationMap.put(panel, "");
		historyEvalMap.put(panel, differenceFromAnswer());
		count = 0;
		while (count < MAX_COUNT && !list.isEmpty()) {
//			System.out.println("count : " + count);
			long time = System.currentTimeMillis();
			long duration = time - startmills;
			long secs = (duration / 1000);
			if (secs > MAXTIME) {
				break;
			}
			ansCandidateList = new ArrayList<String>();
			for (String temp : list) {
				Set<Integer> opSet = new HashSet<Integer>();
				time = System.currentTimeMillis();
				duration = time - startmills;
				secs = (duration / 1000);
				if (secs > MAXTIME) {
					break;
				}
				for (int i=0; i<4; i++) {
					int operation = i;
					if (operation == UP_OP && !opSet.contains(operation)) {
						SlidePanel sp = new SlidePanel(width, height, temp);
						if (sp.zeroY > 0 && !isBlock(sp.zeroX, sp.zeroY - 1)) {
							sp.up();
							changeHistory(sp, temp);
							String ans = sp.createString();
							if (ans.equals(answerString)) {
								break;
							}
						}
					} else if (operation == LEFT_OP && !opSet.contains(operation)) {
						SlidePanel sp = new SlidePanel(width, height, temp);
						if (sp.zeroX > 0 && !isBlock(sp.zeroX - 1, sp.zeroY)) {
							sp.left();
							changeHistory(sp, temp);
							String ans = sp.createString();
							if (ans.equals(answerString)) {
								break;
							}
						}
					} else if (operation == RIGHT_OP && !opSet.contains(operation)) {
						SlidePanel sp = new SlidePanel(width, height, temp);
						if (sp.zeroX < width
								&& !isBlock(sp.zeroX + 1, sp.zeroY)) {
							sp.right();
							changeHistory(sp, temp);
							String ans = sp.createString();
							if (ans.equals(answerString)) {
								break;
							}
						}
					} else if (operation == DOWN_OP && !opSet.contains(operation)) {
						SlidePanel sp = new SlidePanel(width, height, temp);
						if (sp.zeroY < height
								&& !isBlock(sp.zeroX, sp.zeroY + 1)) {
							sp.down();
							changeHistory(sp, temp);
							String ans = sp.createString();
							if (ans.equals(answerString)) {
								break;
							}
						}
					}
					opSet.add(operation);
					if (opSet.size() >= 4) {
						break;
					}
				}
				if (historyOperationMap.containsKey(correctPanelString)) {
					answerString = correctPanelString;
					operationAnswer = historyOperationMap.get(answerString);
					return;
				}
			}
			// System.out.println(ansCandidateList);
			list = ansCandidateList;
//			System.out.println(list.size());
//			System.out.println(count);
			count++;
		}
	}
	
	public void changeHistory(SlidePanel sp, String temp) {
		String ans = sp.createString();
		if (!historyOperationMap.containsKey(ans)) {
//			System.out.println("min " + min);
			int afterEval = sp.differenceFromAnswer();
//			double weight = 1 + count/100;
			if (afterEval <= min + alpha) {
//				if (ansCandidateList.size() < MAX_LIST_SIZE) {
					ansCandidateList.add(ans);
					historyOperationMap.put(ans, historyOperationMap.get(temp)
							+ sp.operation);
					historyEvalMap.put(ans, afterEval);
//				}
				if (afterEval < min) {
					min = afterEval;
				}
			}
		}
//		System.out.println(ansCandidateList.size());

	}
	
	private Map<Character, List<List<Integer>>> distanceMap = new HashMap<Character, List<List<Integer>>>();
	public void createDistanceList(List<List<Character>> list) {
		List<List<Integer>> distanceList = new ArrayList<List<Integer>>();
		for (int y1 = 0; y1 < height; y1++) {
			List<Integer> xList = new ArrayList<Integer>();
			for (int x1 = 0; x1 < width; x1++) {
				xList.set(x1, Integer.MAX_VALUE);
			}
			distanceList.add(xList);
		}
		
		for (int y1 = 0; y1 < height; y1++) {
			for (int x1 = 0; x1 < width; x1++) {
				Character ans = list.get(y1).get(x1);
				if (!ans.equals('=')) {
					for (int y2 = 0; y2 < height; y2++) {
						for (int x2 = 0; x2 < width; x2++) {
							Character c = list.get(y2).get(x2);
							if (c.equals(ans)) {
								distanceList.get(y2).set(x2, 0);
							} else if (!c.equals('=')) {
								distanceList.get(y2).set(x2, 1);
							}
						}
					}
				}
			}
		}
	}
	
	
	public void maxDistanceFromAnswer() {
		int result = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Character c = getChar(x, y);
				int ansX = 0;
				int ansY = 0;
				int difX = 0;
				int difY = 0;
				if (!c.equals('=')) {
					Character a = ' ';
					for (ansY = 0; ansY < height; ansY++) {
						for (ansX = 0; ansX < width; ansX++) {
							a = correctList.get(ansY).get(ansX);
							if (c.equals(a)) {
								break;
							}
						}
						if (c.equals(a)) {
							break;
						}
					}
					difX = Math.abs(x - ansX);
					difY = Math.abs(y - ansY);
				}
//				int weight = 1 + count/10;
				result = difX + difY;
				if (maxDistance < result) {
					maxDistance = result;
				}
			}
		}
	}

	
	public int differenceFromAnswer() {
		int result = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Character c = getChar(x, y);
				int ansX = 0;
				int ansY = 0;
				int difX = 0;
				int difY = 0;
				if (!c.equals('=') && !c.equals('0')) {
					Character a = ' ';
					for (ansY = 0; ansY < height; ansY++) {
						for (ansX = 0; ansX < width; ansX++) {
							a = correctList.get(ansY).get(ansX);
							if (c.equals(a)) {
								break;
							}
						}
						if (c.equals(a)) {
							break;
						}
					}
					difX = Math.abs(x - ansX);
					difY = Math.abs(y - ansY);
				}
//				int weight = 1 + count/10;
				result += difX + difY;
			}
		}
		return result;
	}

	public void createAnswerPanel() {
		correctPanelString = "";
		List<Character> list = new ArrayList<Character>();
		List<Integer> blockPosList = new ArrayList<Integer>();
		for (int i = 0; i < panel.length(); i++) {
			if (panel.charAt(i) == '=') {
				blockPosList.add(i);
			} else {
				if (panel.charAt(i) != '0') {
					list.add(panel.charAt(i));
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
	}

	public String getPanel() {
		return panel;
	}

	public void up() {
		if (zeroY > 0 && !isBlock(zeroX, zeroY - 1)) {
			swap(zeroX, zeroY, zeroX, zeroY - 1);
			zeroX = zeroX;
			zeroY = zeroY - 1;
			operation += "U";
		} else {
			throw new Error("no op.");
		}
	}

	public void down() {
		if (zeroY < height && !isBlock(zeroX, zeroY + 1)) {
			swap(zeroX, zeroY, zeroX, zeroY + 1);
			zeroX = zeroX;
			zeroY = zeroY + 1;
			operation += "D";
		} else {
			throw new Error("no op.");
		}
	}

	public void right() {
		if (zeroX < width && !isBlock(zeroX + 1, zeroY)) {
			swap(zeroX, zeroY, zeroX + 1, zeroY);
			zeroX = zeroX + 1;
			zeroY = zeroY;
			operation += "R";
		} else {
			throw new Error("no op.");
		}
	}

	public void left() {
		if (zeroX > 0 && !isBlock(zeroX - 1, zeroY)) {
			swap(zeroX, zeroY, zeroX - 1, zeroY);
			zeroX = zeroX - 1;
			zeroY = zeroY;
			operation += "L";
		} else {
			throw new Error("no op.");
		}
	}

	public boolean isBlock(int x, int y) {
		Character c;
		if (x >= 0 && y >= 0 && x < width && y < height) {
			c = getChar(x, y);
			return c.equals('=');
		} else {
			return true;
		}

	}

	public void swap(int fromX, int fromY, int toX, int toY) {
		Character from;
		Character to;
		from = getChar(fromX, fromY);
		to = getChar(toX, toY);
		setChar(toX, toY, from);
		setChar(fromX, fromY, to);
	}

	public void setChar(int x, int y, Character c) {
		List<Character> xList = panelList.get(y);
		xList.set(x, c);
	}

	public Character getChar(int x, int y) {
		Character result;
		if (x >= 0 && y >= 0 && x < width && y < height) {
			result = panelList.get(y).get(x);
		} else {
			throw new Error("no op");
		}
		return result;
	}

	private void findZeroPosition() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (getChar(x, y).equals('0')) {
					zeroX = x;
					zeroY = y;
				}
			}
		}
	}

	private void createPanel() {
		for (int y = 0; y < height; y++) {
			List<Character> xList = new ArrayList<Character>();
			for (int x = y * width; x < (y + 1) * width; x++) {
				xList.add(panel.charAt(x));
			}
			panelList.add(xList);
		}
	}

	public void createCorrectPanel() {
		for (int y = 0; y < height; y++) {
			List<Character> xList = new ArrayList<Character>();
			for (int x = y * width; x < (y + 1) * width; x++) {
				xList.add(correctPanelString.charAt(x));
			}
			correctList.add(xList);
		}
	}

	public String createString() {
		String result = "";
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				result += getChar(x, y);
			}
		}
		return result;
	}

	public void printPanel() {
		for (List<Character> xList : panelList) {
			for (Character s : xList) {
				System.out.print(" " + s);
			}
			System.out.println();
		}
	}

	public void printCorrectPanel() {
		for (List<Character> xList : correctList) {
			for (Character s : xList) {
				System.out.print(" " + s);
			}
			System.out.println();
		}
	}

	public void updateLRUD(String operationAnswer) {
		for (int i = 0; i < operationAnswer.length(); i++) {
			Character c = operationAnswer.charAt(i);
			if (c.equals('L')) {
				if (MAX_LEFT <= 0) {
					throw new Error("max left is zero.");
				}
				MAX_LEFT--;
			} else if (c.equals('R')) {
				if (MAX_RIGHT <= 0) {
					throw new Error("max right is zero.");
				}
				MAX_RIGHT--;
			} else if (c.equals('U')) {
				if (MAX_UP <= 0) {
					throw new Error("max up is zero.");
				}
				MAX_UP--;
			} else if (c.equals('D')) {
				if (MAX_DOWN <= 0) {
					throw new Error("max down is zero.");
				}
				MAX_DOWN--;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// 3,3,120743586
		// 3,4,5362190=B47A
		// 3,5,=B02564=3A7CD9E
		// 4,4,32465871FAC0=9BE
		// 5,3,3CBE48=A=60D521
		// 6,6,1A3B458J26EZKF09LUD7GCIOP==MNTVWXYSH
		BufferedReader br = null;
		BufferedReader br2 = null;
		
		final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(args[1])));
		final List<String> answerBuffer = new ArrayList<String>();
		
		try {
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
			    public void run() { 
			    	try {
			    		for (String op : answerBuffer) {
			    			bw.write(op + "\n");
			    		}
			    		bw.flush();
			    	} catch (IOException e) {
			    		e.printStackTrace();
			    	} finally {
			    		try {
			    			bw.close();
			    		} catch (IOException e) {
			    			e.printStackTrace();
			    		}
			    	}
			    }
			});
			String oneline = "";
			boolean isAlreadyRead = false;
			//途中から始められる用
			if (args.length > 2 && !args[2].isEmpty()) {
				br2 = new BufferedReader(new FileReader(new File(args[2])));
			}
			if (args.length > 3 && !args[3].isEmpty()) {
				alpha = Integer.parseInt(args[3]);
			}
			String line = "";
			
			for (int i=0; i<5000; i++) {
				if (br2 != null) {
					line = br2.readLine();
				}
				answerBuffer.add(line);
			}
			
			
			int puzzleCount = 1;
			int solvedCount = 0;
			int beforeSolved = 0;
			while (true) {
				int lineNum = 0;
				br = new BufferedReader(new FileReader(new File(args[0])));
				beforeSolved = solvedCount;
				int answerIndex = 0;
				while ((oneline = br.readLine()) != null) {
					if (lineNum == 0) {
						StringTokenizer st = new StringTokenizer(oneline);
						if (!isAlreadyRead) {
						MAX_LEFT = Integer.parseInt(st.nextToken());
						MAX_RIGHT = Integer.parseInt(st.nextToken());
						MAX_UP = Integer.parseInt(st.nextToken());
						MAX_DOWN = Integer.parseInt(st.nextToken());
						isAlreadyRead = true;
						}
					} else if (lineNum == 1) {
						MAX_TEST_COUNT = Integer.parseInt(oneline);
					} else {
						System.out.println(alpha);
						if (answerBuffer.get(answerIndex).isEmpty()) {
							StringTokenizer st = new StringTokenizer(oneline, ",");
							int width = Integer.parseInt(st.nextToken());
							int height = Integer.parseInt(st.nextToken());
							String panel = st.nextToken();
							SlidePanel sp = new SlidePanel(width, height, panel);
							sp.printPanel();
							try {
								sp.solveSlidePanel();
							} catch (Throwable t) {
								t.printStackTrace();
								//noop
							}
							if (!operationAnswer.isEmpty()) {
								solvedCount++;
								System.out.println("solved! : " + solvedCount);
							}
							System.out.println(operationAnswer);
							System.out.println("puzzleCount : " + puzzleCount + " solvedCount : " +solvedCount + " rate : " + (double) solvedCount / puzzleCount);
							//						bw.write(operationAnswer + "\n");
							answerBuffer.set(answerIndex, operationAnswer);
							initialize();
							puzzleCount++;
						}
						answerIndex++;
					}
//					bw.flush();
					lineNum++;
				}
				alpha += 2;
				if (beforeSolved == solvedCount && alpha > 100) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
			if (br2 != null) {
				br2.close();
			}
		}
		// BufferedReader stdReader =
		// new BufferedReader(new InputStreamReader(System.in));
		// String line;
		// while ((line = stdReader.readLine()) != null) {
		// if (line.indexOf('U') == 0) {
		// sp.up();
		// sp.printPanel();
		// } else if (line.indexOf('D') == 0) {
		// sp.down();
		// sp.printPanel();
		// } else if (line.indexOf('R') == 0) {
		// sp.right();
		// sp.printPanel();
		// } else if (line.indexOf('L') == 0) {
		// sp.left();
		// sp.printPanel();
		// }
		// }
		//
		// stdReader.close();
	}

}
