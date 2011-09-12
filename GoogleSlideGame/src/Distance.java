import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 距離関数の実装クラス
 * ダイクストラ法を実装し、壁を考慮した距離にしている。
 * @author yosuke
 *
 */
public class Distance {
	/** 距離を保持するマップ */
	private Map<Character, List<Integer>> distanceMap;
	private int width;
	private int height;
	/** まだ考慮されていない距離 */
	private static final int BLOCK_DISTANCE = 1000;
	public Distance(int width, int height, String correctStr) {
		this.width = width;
		this.height = height;
		distanceMap = new HashMap<Character, List<Integer>>();
		String correctString = Util.createAnswerPanel(correctStr);
		char[][] board = Util.createBoard(width, height, correctString);
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				List<Integer> list = new ArrayList<Integer>();
				char c = board[y][x];
				int difference = 0;
				if (c != '=') {				
					for (int y1=0; y1<height; y1++) {
						boolean blocked = false;
						for (int x1=0; x1<width; x1++) {
							char difC = board[y1][x1];
							if (c == difC) {
								difference = 0;
							} else {
								difference = BLOCK_DISTANCE;
							}
							list.add(difference);
						}
					}

					while(!isFinished(board, list)) {
						for (int y2=0; y2<height; y2++) {
							for (int x2=0; x2<width; x2++) {
								int distance = list.get(x2 + y2 * width);
								char difC = board[y2][x2];
								int min = BLOCK_DISTANCE;
								if (difC != '=' && distance == BLOCK_DISTANCE) {
									if (y2 > 0) {
										int dist = list.get(x2 + (y2-1)*width);
										if (dist < min) {
											min = dist;
										}
									}
									if (y2 < height-1) {
										int dist = list.get(x2 + (y2+1)*width);
										if (dist < min) {
											min = dist;
										}
									}
									if (x2 > 0) {
										int dist = list.get((x2-1) + y2*width);
										if (dist < min) {
											min = dist;
										}
									}
									if (x2 < width-1) {
										int dist = list.get((x2+1) + y2*width);
										if (dist < min) {
											min = dist;
										}
									}
									if (min != BLOCK_DISTANCE) {
										list.set(x2+y2*width, min+1);
									}
								}
							}
						}
					}

					distanceMap.put(c, list);
				}
			}
		}
	}

	/**
	 * 距離の計測が終わったかどうか終わった場合はtrue
	 * @param board ボード
	 * @param distanceList 距離リスト
	 * @return
	 */
	private boolean isFinished(char[][] board, List<Integer> distanceList) {
		boolean result = true;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				char c = board[y][x];
				int distance = distanceList.get(x + y * width);
				if (c != '=' && distance == BLOCK_DISTANCE) {
					return false;
				}
			}
		}
		return result;
	}

	/**
	 * すべての距離を計測するメソッド
	 * @param board ボード
	 * @return 距離
	 */
	public int getAllDistance(char[][] board) {
		int difference = 0;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				char c = board[y][x];
				if (c != '=') {
					difference += getDistanceChar(c, x, y);
				}
			}
		}
		return difference;
	}

	/**
	 * 最大距離長
	 * @param board ボード
	 * @return 最大距離長
	 */
	public int getMaxDistance(char[][] board) {
		int max = 0;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				char c = board[y][x];
				if (c != '=') {
					int temp = getDistanceChar(c, x, y);
					if (temp > max) {
						max = temp;
					}
				}
			}
		}
		return max;
	}

	public int getNewDistance(char c, int oldX, int oldY, int newX, int newY, int currentDistance) {
		int distance = currentDistance;
		int oldDiff = getDistanceChar(c, oldX, oldY);
		int newDiff = getDistanceChar(c, newX, newY);
		distance = distance - oldDiff + newDiff;
		return distance;
	}

	public int getDistanceChar(char c, int x, int y) {
		int distance = 0;
		if (c == '=') {
			return 0;
		}
		List<Integer> list = distanceMap.get(c);
		distance = list.get(x + y * width);
		return distance;
	}

	public static void main(String[] args) {
		Distance distance = new Distance(4, 4, "12=45=789A=CDEF0");
		System.out.println(distance.distanceMap);
		char[][] chars = {{'1','4','=', '2'},{'5','=','7', '8'},{'9','A','=','C'},{'D','E','F','0'}};
		int dist = distance.getAllDistance(chars);
		int newDist = distance.getNewDistance('1', 2, 2, 1, 1, 20);

		System.out.println(dist);
		System.out.println(newDist);
		newDist = distance.getNewDistance('2', 2, 1, 1, 0, 20);
		System.out.println(newDist);
	}


}
