import java.util.List;

/**
 * �X���C�h�p�Y���̖{��
 * @author yosuke
 *
 */
public class SlidePuzzle {
	/** �{�[�h */
	private Board board;
	private int width;
	private int height;
	/** �p�Y�������� */
	private String puzzle;
	/** �����p�Y�������� */
	private String correctPuzzle;
	/** �Ֆʂ�history */
	private History history;
	private Distance distance;
	private AnswerBoard answerBoard;
	
	
	private final static int UP_OP = 0;
	private final static int LEFT_OP = 1;
	private final static int RIGHT_OP = 2;
	private final static int DOWN_OP = 3;
	
	/** �^�C���A�E�g����(�b) */
	private static long MAXTIME = 30;
	private static long startmills;
	/** �}�؂�T�C�Y */
	private static int MAXSIZE = 5000;
	public SlidePuzzle(int width, int height, String puzzle) {
		history = new History();
		this.width = width;
		this.height = height;
		this.puzzle = puzzle;
		correctPuzzle = Util.createAnswerPanel(puzzle);
		distance = new Distance(width, height, correctPuzzle);
		char[][] chars = Util.createBoard(width, height, puzzle);
		board = new Board(width, height, puzzle);
		int distInt = distance.getAllDistance(chars);
		int maxDistInt = distance.getMaxDistance(chars);
		history.putHistory(puzzle, "", distInt, maxDistInt);
		answerBoard = new AnswerBoard();
		answerBoard.put(
				puzzle,
				distInt,
				distance.getMaxDistance(chars));
	}
	
	public void solvePuzzle() {
		startmills = System.currentTimeMillis();
		List<String> candidateList = null;
		while (true) {
			long time = System.currentTimeMillis();
			long duration = time - startmills;
			long secs = (duration / 1000);
			if (secs > MAXTIME) {
				break;
			}
			candidateList = answerBoard.getListFromCandidate(MAXSIZE);
//			Util.writeLog(candidateList.get(0));
			answerBoard = new AnswerBoard();
			for (String oldPazzle : candidateList) {
				String operationHistory = history.getOperationHistory(oldPazzle);
				int currentDistance = history.getDistanceHistory(oldPazzle);
				int currentMaxDistance = history.getMaxDistanceHistory(oldPazzle);
				for (int op = 0; op < 4; op++) {
					switch(op) {
					case UP_OP : 
						board = new Board(width, height, oldPazzle);
						if (board.up()) {
							MovedPosition position = board.getPosition();
							move(operationHistory + "U", position, currentDistance, currentMaxDistance);
						}
						break;
					case LEFT_OP: 
						board = new Board(width, height, oldPazzle);
						if (board.left()) {
							MovedPosition position = board.getPosition();
							move(operationHistory + "L", position, currentDistance, currentMaxDistance);
						}
						break;
					case RIGHT_OP:
						board = new Board(width, height, oldPazzle);
						if (board.right()) {
							MovedPosition position = board.getPosition();
							move(operationHistory + "R", position, currentDistance, currentMaxDistance);
						}
						break;
					case DOWN_OP:
						board = new Board(width, height, oldPazzle);
						if (board.down()) {
							MovedPosition position = board.getPosition();
							move(operationHistory + "D", position, currentDistance, currentMaxDistance);
						}
						break;
					}
					//�𓚂���������I���B
					if (answerBoard.contains(correctPuzzle)) {
						return;
					}
				}
			}
		}
	}
	
	/**
	 * �𓚂��o�����ǂ���
	 * �o���ꍇ��true
	 * @return
	 */
	public boolean solved() {
		String operation = history.getOperationHistory(correctPuzzle);
		return operation != null;
	}
	
	/**
	 * �𓚂̑��앶����𓾂�B
	 * @return
	 */
	public String getSolvedString() {
		String historyOperation = history.getOperationHistory(correctPuzzle);
		if (historyOperation != null && !historyOperation.isEmpty()) {
			return historyOperation;
		} else {
			return "";
		}
	}
	
	public void printCurrentDistanceTop() {
		String top = history.getCurrentDistanceTop();
		System.out.println(top);
		Board board = new Board(width, height, top);
		board.printBoard();
	}
	
	public void printCurrentMaxDistanceTop() {
		String top = history.getCurrentMaxDistanceTop();
		Board board = new Board(width, height, top);
		board.printBoard();
	}
	
	public void printBoard() {
		board.printBoard();
	}
	
	/***
	 * �ړ��Ƃ��̋�����history�ɉ����鏈���B
	 * @param operation
	 * @param position
	 * @param currentDistance
	 * @param currentMaxDistance
	 */
	private void move(String operation, MovedPosition position, int currentDistance, int currentMaxDistance) {
		String boardStr = board.getBoardString();
		char changedChar = position.getC();
		int oldX = position.getOldX();
		int oldY = position.getOldY();
		int newX = position.getNewX();
		int newY = position.getNewY();
		if (history.getOperationHistory(boardStr) == null) {
			char[][] c = Util.createBoard(width, height, boardStr);
			int newDistance = distance.getNewDistance(changedChar, oldX, oldY, newX, newY, currentDistance);
			int newMaxDistance = distance.getMaxDistance(c);
			
			answerBoard.put(boardStr, newDistance, newMaxDistance);
			history.putHistory(boardStr, operation, newDistance, newMaxDistance);
		}
	}

}
