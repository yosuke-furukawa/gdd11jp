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
public class SlidePanelWithDoubleSearch {
	private int width;
	private int height;
	/** パネル */
	private String panel = "";
	/** 逆のパネル */
	private String panelFromAnswer = "";
	
	/** 正解文字列 */
	private static String correctPanelString = "";
	/** 逆正解文字列 */
	private static String correctPanelStringFromAnswer = "";

	/** パネル候補リスト */
	private List<List<Character>> panelList = new ArrayList<List<Character>>();
	/** 逆からのパネル候補リスト */
	private List<List<Character>> panelListFromAnswer = new ArrayList<List<Character>>();
	
	/** 正解リスト */
	private List<List<Character>> correctList = new ArrayList<List<Character>>();
	/** 逆からの正解リスト */
	private List<List<Character>> correctListFromAnswer = new ArrayList<List<Character>>();
	
	private String operation = "";
	private static String operationFromAnswer = "";
	private static String operationAnswer = "";
	private String answerString = "";
	private List<String> ansCandidateList = null;
	private List<String> ansCandidateListFromAnswer = null;
	
	private static Map<String, String> historyOperationMap = new HashMap<String, String>();
	private static Map<String, Integer> historyEvalMap = new HashMap<String, Integer>();
	private static Map<String, String> historyOperationMapFromAnswer = new HashMap<String, String>();
	private static Map<String, Integer> historyEvalMapFromAnswer = new HashMap<String, Integer>();	
	private static long startmills;
	private int zeroX;
	private int zeroY;
	private int count;
	private static int minFromAnswer = -1;
	private static int min = -1;
	private static final long MAXTIME = 60;
	private static final int alpha = 8;


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

	SlidePanelWithDoubleSearch(int width, int height, String panel, boolean fromStart) {
		this.width = width;
		this.height = height;
		correctList = new ArrayList<List<Character>>();
		correctListFromAnswer = new ArrayList<List<Character>>();
		
		if (fromStart) {
			this.panel = panel;
			createPanel();
			if (correctPanelStringFromAnswer.isEmpty()) {
				correctPanelStringFromAnswer = "" + panel;
			}
			createCorrectPanelString();
			createCorrectPanel();
			findZeroPosition(panelList);
			if (min < 0) {
				min = differenceFromStart();
			}
		} else {
			this.panelFromAnswer = panel;
			createPanelFromAnswer();
			if (correctPanelStringFromAnswer.isEmpty()) {
				correctPanelStringFromAnswer = "" + panel;
			}
			createCorrectPanelString();
			createCorrectPanelFromAnswer();
			findZeroPosition(panelListFromAnswer);
			if (minFromAnswer < 0) {
				min = differenceFromAnswer();
			}
			
		}
		
	}
	
	public void setup(String panel, boolean fromStart) {
		correctList = new ArrayList<List<Character>>();
		correctListFromAnswer = new ArrayList<List<Character>>();
		
		if (fromStart) {
			this.panel = panel;
			createPanel();
			createCorrectPanelString();
			createCorrectPanel();
			findZeroPosition(panelList);
			if (min < 0) {
				min = differenceFromStart();
			}
		} else {
			this.panelFromAnswer = panel;
			createPanelFromAnswer();
			createCorrectPanelString();
			createCorrectPanelFromAnswer();
			findZeroPosition(panelListFromAnswer);
			if (minFromAnswer < 0) {
				minFromAnswer = differenceFromAnswer();
			}
			
		}
	}
	
	public static void initialize() {
		operationAnswer = "";
		operationFromAnswer = "";
		correctPanelString = "";
		correctPanelStringFromAnswer = "";
		historyEvalMap = new HashMap<String, Integer>();
		historyEvalMapFromAnswer = new HashMap<String, Integer>();
		historyOperationMap = new HashMap<String, String>();
		historyOperationMapFromAnswer = new HashMap<String, String>();
		
		
		minFromAnswer = -1;
		min = -1;
	}

	public void solveSlidePanel() {
		startmills = System.currentTimeMillis();
		List<String> list = Arrays.asList(panel);
		List<String> listFromAns = Arrays.asList(correctPanelString);
		historyOperationMap.put(panel, "");
		historyEvalMap.put(panel, differenceFromStart());
		historyOperationMapFromAnswer.put(correctPanelString, "");
		historyEvalMapFromAnswer.put(correctPanelString, differenceFromStart());
		count = 0;
		while (count < MAX_COUNT && (!list.isEmpty() || !listFromAns.isEmpty())) {
			long time = System.currentTimeMillis();
			long duration = time - startmills;
			long secs = (duration / 1000);
			if (secs > MAXTIME) {
				break;
			}
			ansCandidateList = new ArrayList<String>();
			ansCandidateListFromAnswer = new ArrayList<String>();
			searchFromStart(list);
			searchFromGoal(listFromAns);

			for (String historyOperation : historyOperationMap.keySet()) {
				if (historyOperationMapFromAnswer.containsKey(historyOperation)) {
					operationAnswer = historyOperationMap.get(historyOperation);
					String upsideDownAnswer = historyOperationMapFromAnswer.get(historyOperation);
					for (int i=upsideDownAnswer.length()-1; i>=0; i--) {
						char c = upsideDownAnswer.charAt(i);
						if (c == 'U') {
							operationAnswer += 'D';
						} else if (c == 'D') {
							operationAnswer += 'U';
						} else if (c == 'R') {
							operationAnswer += 'L';
						} else if (c == 'L') {
							operationAnswer += 'R';
						}
					}
					return;
				}
			}
			// System.out.println(ansCandidateList);
			list = ansCandidateList;
			listFromAns = ansCandidateListFromAnswer;
//			System.out.println("from start " + list.size());
//			System.out.println("from goal " + listFromAns.size());
//			System.out.println(list.size());
//			System.out.println(count);
			count++;
		}
	}
	
	private void searchFromGoal(List<String> list) {
		long time;
		long duration;
		long secs;
//		findZeroPosition(panelListFromAnswer);
		for (String temp : list) {
			setup(temp, false);
			time = System.currentTimeMillis();
			duration = time - startmills;
			secs = (duration / 1000);
			if (secs > MAXTIME) {
				break;
			}
			for (int operation = 0; operation < 4; operation++) {
				if (operation == UP_OP) {
					SlidePanelWithDoubleSearch sp = new SlidePanelWithDoubleSearch(width, height, temp, false);
					if (sp.zeroY > 0 && !isBlock(sp.zeroX, sp.zeroY - 1, sp.panelListFromAnswer)) {
						sp.up(sp.panelListFromAnswer);
						changeHistoryFromAnswer(sp, temp);
						String ans = sp.createStringFromAnswer();
						if (ans.equals(panel)) {
							break;
						}
					}
				} else if (operation == LEFT_OP) {
					SlidePanelWithDoubleSearch sp = new SlidePanelWithDoubleSearch(width, height, temp, false);
					if (sp.zeroX > 0 && !isBlock(sp.zeroX - 1, sp.zeroY, sp.panelListFromAnswer)) {
						sp.left(sp.panelListFromAnswer);
						changeHistoryFromAnswer(sp, temp);
						String ans = sp.createStringFromAnswer();
						if (ans.equals(panel)) {
							break;
						}
					}
				} else if (operation == RIGHT_OP) {
					SlidePanelWithDoubleSearch sp = new SlidePanelWithDoubleSearch(width, height, temp, false);
					if (sp.zeroX < width
							&& !isBlock(sp.zeroX + 1, sp.zeroY, sp.panelListFromAnswer)) {
						sp.right(sp.panelListFromAnswer);
						changeHistoryFromAnswer(sp, temp);
						String ans = sp.createStringFromAnswer();
						if (ans.equals(panel)) {
							break;
						}
					}
				} else if (operation == DOWN_OP) {
					SlidePanelWithDoubleSearch sp = new SlidePanelWithDoubleSearch(width, height, temp, false);
					if (sp.zeroY < height
							&& !isBlock(sp.zeroX, sp.zeroY + 1, sp.panelListFromAnswer)) {
						sp.down(sp.panelListFromAnswer);
						changeHistoryFromAnswer(sp, temp);
						String ans = sp.createStringFromAnswer();
						if (ans.equals(panel)) {
							break;
						}
					}
				}
			}
			
			if (historyOperationMapFromAnswer.containsKey(panel)) {
				return;
			}
		}
	}

	private void searchFromStart(List<String> list) {
		long time;
		long duration;
		long secs;
//		printPanel();
//		findZeroPosition(panelList);
		for (String temp : list) {
			setup(temp, true);
			time = System.currentTimeMillis();
			duration = time - startmills;
			secs = (duration / 1000);
			if (secs > MAXTIME) {
				break;
			}
			for (int operation=0; operation < 4; operation++) {
				if (operation == UP_OP) {
					SlidePanelWithDoubleSearch sp = new SlidePanelWithDoubleSearch(width, height, temp, true);
					if (sp.zeroY > 0 && !isBlock(sp.zeroX, sp.zeroY - 1, sp.panelList)) {
						sp.up(sp.panelList);
						changeHistory(sp, temp);
						String ans = sp.createString();
						if (ans.equals(correctPanelString)) {
							break;
						}
					}
				} else if (operation == LEFT_OP) {
					SlidePanelWithDoubleSearch sp = new SlidePanelWithDoubleSearch(width, height, temp, true);
					if (sp.zeroX > 0 && !isBlock(sp.zeroX - 1, sp.zeroY, sp.panelList)) {
						sp.left(sp.panelList);
						changeHistory(sp, temp);
						String ans = sp.createString();
						if (ans.equals(correctPanelString)) {
							break;
						}
					}
				} else if (operation == RIGHT_OP) {
					SlidePanelWithDoubleSearch sp = new SlidePanelWithDoubleSearch(width, height, temp, true);
					if (sp.zeroX < width
							&& !isBlock(sp.zeroX + 1, sp.zeroY, sp.panelList)) {
						sp.right(sp.panelList);
						changeHistory(sp, temp);
						String ans = sp.createString();
						if (ans.equals(correctPanelString)) {
							break;
						}
					}
				} else if (operation == DOWN_OP) {
					SlidePanelWithDoubleSearch sp = new SlidePanelWithDoubleSearch(width, height, temp, true);
					if (sp.zeroY < height
							&& !isBlock(sp.zeroX, sp.zeroY + 1, sp.panelList)) {
						sp.down(sp.panelList);
						changeHistory(sp, temp);
						String ans = sp.createString();
						if (ans.equals(correctPanelString)) {
							break;
						}
					}
				}
			}
			if (historyOperationMap.containsKey(correctPanelString)) {
				return;
			}
		}
	}
	
	public void changeHistory(SlidePanelWithDoubleSearch sp, String temp) {
		String ans = sp.createString();
		if (!historyOperationMap.containsKey(ans)) {
//			System.out.println("from start " + ans);
//			System.out.println("min " + min);
			int afterEval = sp.differenceFromStart();
//			if (afterEval <= min + alpha) {
				
//				if (ansCandidateList.size() < MAX_LIST_SIZE) {
					ansCandidateList.add(ans);
					historyOperationMap.put(ans, historyOperationMap.get(temp)
							+ sp.operation);
					historyEvalMap.put(ans, afterEval);
//				}
				if (afterEval < min) {
					min = afterEval;
				}
//			}
		}
	}
	
	public void changeHistoryFromAnswer(SlidePanelWithDoubleSearch sp, String temp) {
		String ans = sp.createStringFromAnswer();
		if (!historyOperationMapFromAnswer.containsKey(ans)) {
//			System.out.println("from goal " + ans);
//			System.out.println("min " + min);
			int afterEval = sp.differenceFromAnswer();
//			if (afterEval <= minFromAnswer + alpha) {
//				if (ansCandidateList.size() < MAX_LIST_SIZE) {
					ansCandidateListFromAnswer.add(ans);
					historyOperationMapFromAnswer.put(ans, historyOperationMapFromAnswer.get(temp)
							+ sp.operation);
					historyEvalMapFromAnswer.put(ans, afterEval);
//				}
				if (afterEval < minFromAnswer) {
					minFromAnswer = afterEval;
				}
//			}
		}
	}
	
//	private int distance(List<List<Character>> list1, List<List<Character>> list2) {
//		int result = 0;
//		int ansX;
//		int ansY;
//		for (int y1 = 0; y1 < height; y1++) {
//			for (int x1 = 0; x1 < width; x1++) {
//				Character c1 = list1.get(y1).get(x1);
//				for (int y2 = 0; y2 < height; y2++) {
//					for (int x2 = 0; x2 < width; x2++) {
//						Character c2 = list2.get(y2).get(x2);
//						if (c1.equals(c2)) {
//							ansX = x2;
//							ansY = y2;
//						}
//					}
//				}
//				
//			}
//		}
//		
//		Node[][] nodes = new Node[height][width];
//		for (int y = 0; y < height; y++) {
//			for (int x = 0; x < width; x++) {
//				nodes[y][x] = new Node();
//				nodes[y][x].c = list1.get(y).get(x);
//				nodes[y][x].neighborCharacter = new ArrayList<Character>();
//				
//				nodes[y][x].isDone = false;
//				nodes[y][x].cost = -1;
//			}
//		}
//		List<List<Integer>> distanceList = new ArrayList<List<Integer>>();
//		
//		
//		
//		return result;
//	}
	
	public int difference(List<List<Character>> list1, List<List<Character>> list2) {
		int result = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Character c = list1.get(y).get(x);
				int ansX = 0;
				int ansY = 0;
				int difX = 0;
				int difY = 0;
				if (!c.equals('=')) {
				Character a = ' ';
				for (ansY = 0; ansY < height; ansY++) {
					for (ansX = 0; ansX < width; ansX++) {
						a = list2.get(ansY).get(ansX);
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
				result += difX + difY;
			}
		}
		return result;
	}
	
	public int differenceFromAnswer() {
		int result = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Character c = getChar(x, y, panelListFromAnswer);
				int ansX = 0;
				int ansY = 0;
				Character a = ' ';
				for (ansY = 0; ansY < height; ansY++) {
					for (ansX = 0; ansX < width; ansX++) {
						a = correctListFromAnswer.get(ansY).get(ansX);
						if (c.equals(a)) {
							break;
						}
					}
					if (c.equals(a)) {
						break;
					}
				}
				int difX = Math.abs(x - ansX);
				int difY = Math.abs(y - ansY);
				if (c.equals('=') || c.equals('0')) {
					difX = 0;
					difY = 0;
				}
				result += difX + difY;
			}
		}
		return result;
	}
	
	public int differenceFromStart() {
		int result = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Character c = getChar(x, y, panelList);
				int ansX = 0;
				int ansY = 0;
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
				int difX = Math.abs(x - ansX);
				int difY = Math.abs(y - ansY);
				if (c.equals('=') || c.equals('0')) {
					difX = 0;
					difY = 0;
				}
				result += difX + difY;
			}
		}
		return result;
	}

	public void createCorrectPanelString() {
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

	public void up(List<List<Character>> list) {
		if (zeroY > 0 && !isBlock(zeroX, zeroY - 1, list)) {
			swap(zeroX, zeroY, zeroX, zeroY - 1, list);
			zeroY = zeroY - 1;
			operation += "U";
		} else {
			throw new Error("no op.");
		}
	}

	public void down(List<List<Character>> list) {
		if (zeroY < height && !isBlock(zeroX, zeroY + 1, list)) {
			swap(zeroX, zeroY, zeroX, zeroY + 1, list);
			zeroY = zeroY + 1;
			operation += "D";
		} else {
			throw new Error("no op.");
		}
	}

	public void right(List<List<Character>> list) {
		if (zeroX < width && !isBlock(zeroX + 1, zeroY, list)) {
			swap(zeroX, zeroY, zeroX + 1, zeroY, list);
			zeroX = zeroX + 1;
			operation += "R";
		} else {
			throw new Error("no op.");
		}
	}

	public void left(List<List<Character>> list) {
		if (zeroX > 0 && !isBlock(zeroX - 1, zeroY, list)) {
			swap(zeroX, zeroY, zeroX - 1, zeroY, list);
			zeroX = zeroX - 1;
			operation += "L";
		} else {
			throw new Error("no op.");
		}
	}

	public boolean isBlock(int x, int y, List<List<Character>> list) {
		Character c;
		if (x >= 0 && y >= 0 && x < width && y < height) {
			c = getChar(x, y, list);
			return c.equals('=');
		} else {
			return true;
		}

	}

	public void swap(int fromX, int fromY, int toX, int toY, List<List<Character>> list) {
		Character from;
		Character to;
		from = getChar(fromX, fromY, list);
		to = getChar(toX, toY, list);
		setChar(toX, toY, from, list);
		setChar(fromX, fromY, to, list);
	}

	public void setChar(int x, int y, Character c, List<List<Character>> list) {
		List<Character> xList = list.get(y);
		xList.set(x, c);
	}

	public Character getChar(int x, int y, List<List<Character>> list) {
		Character result;
		if (x >= 0 && y >= 0 && x < width && y < height) {
			result = list.get(y).get(x);
		} else {
			throw new Error("no op");
		}
		return result;
	}

	private void findZeroPosition(List<List<Character>> list) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (getChar(x, y, list).equals('0')) {
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
	
	private void createPanelFromAnswer() {
		for (int y = 0; y < height; y++) {
			List<Character> xList = new ArrayList<Character>();
			for (int x = y * width; x < (y + 1) * width; x++) {
				xList.add(panelFromAnswer.charAt(x));
			}
			panelListFromAnswer.add(xList);
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
	
	
	
	public void createCorrectPanelFromAnswer() {
		for (int y = 0; y < height; y++) {
			List<Character> xList = new ArrayList<Character>();
			for (int x = y * width; x < (y + 1) * width; x++) {
				xList.add(correctPanelStringFromAnswer.charAt(x));
			}
			correctListFromAnswer.add(xList);
		}
	}

	public String createString() {
		String result = "";
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				result += getChar(x, y, panelList);
			}
		}
		return result;
	}
	
	public String createStringFromAnswer() {
		String result = "";
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				result += getChar(x, y, panelListFromAnswer);
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
	
	public void printAnswerPanel() {
		for (List<Character> xList : panelListFromAnswer) {
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
		// 3,4,3694217=BA05
		// 3,5,=B02564=3A7CD9E
		// 6,3,12G58BF07D6H9E34AC
		// 4,4,32465871FAC0=9BE
		// 5,3,3CBE48=A=60D521
		// 6,6,1A3B458J26EZKF09LUD7GCIOP==MNTVWXYSH
//		String oneline = "3,4,3694217=BA05";
//		StringTokenizer st = new StringTokenizer(oneline, ",");
//		int width = Integer.parseInt(st.nextToken());
//		int height = Integer.parseInt(st.nextToken());
//		String panel = st.nextToken();
//		SlidePanel sp = new SlidePanel(width, height, panel, true);
//		sp.solveSlidePanel();
//		System.out.println(operationAnswer);
		BufferedReader br = null;
		BufferedReader br2 = null;
		BufferedWriter bw = null;
		List<String> answerBuffer = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(new File(args[0])));
			bw = new BufferedWriter(new FileWriter(new File(args[1])));
			//途中から始められる用
			if (args.length > 2 && !args[2].isEmpty()) {
				br2 = new BufferedReader(new FileReader(new File(args[2])));
			}
			//
			int lineCounter = 0;
			if (args.length > 3 && !args[3].isEmpty()) {
				lineCounter  = Integer.parseInt(args[3]) + 2;
			}
			String line = "";

			for (int i=0; i<5000; i++) {
				if (br2 != null) {
					line = br2.readLine();
				}
				answerBuffer.add(line);
			}
			String oneline = "";
			int lineNum = 0;
			int puzzleCount = 1;
			int index = 0;
			int solvedCount = 0;
			while ((oneline = br.readLine()) != null) {
				System.out.println(oneline);
				if (lineNum == 0) {
					StringTokenizer st = new StringTokenizer(oneline);
					MAX_LEFT = Integer.parseInt(st.nextToken());
					MAX_RIGHT = Integer.parseInt(st.nextToken());
					MAX_UP = Integer.parseInt(st.nextToken());
					MAX_DOWN = Integer.parseInt(st.nextToken());
				} else if (lineNum == 1) {
					MAX_TEST_COUNT = Integer.parseInt(oneline);
				} else {
					if (lineNum >= lineCounter) {
						if (answerBuffer.get(index).isEmpty()) {
							StringTokenizer st = new StringTokenizer(oneline, ",");
							int width = Integer.parseInt(st.nextToken());
							int height = Integer.parseInt(st.nextToken());
							String panel = st.nextToken();
							SlidePanelWithDoubleSearch sp = new SlidePanelWithDoubleSearch(width, height, panel, true);
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
							bw.write(operationAnswer + "\n");
							initialize();
						} else {
							solvedCount++;
							System.out.println("solved! : " + solvedCount);
							System.out.println(answerBuffer.get(index));
							System.out.println("puzzleCount : " + puzzleCount + " solvedCount : " +solvedCount + " rate : " + (double) solvedCount / puzzleCount);
							bw.write(answerBuffer.get(index) + "\n");
						}
					} else {
						System.out.println(answerBuffer.get(index));
						bw.write(answerBuffer.get(index) + "\n");
					}
					index++;
					puzzleCount++;
				}
				bw.flush();
				lineNum++;
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
			if (bw != null) {
				bw.close();
			}
		}
	}

}
