import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * スライドパズルの解答をチェックするチェッカー。
 * 
 * @author yosuke
 *
 */
public class Checker {
	private static int MAX_LEFT;
	private static int MAX_RIGHT;
	private static int MAX_UP;
	private static int MAX_DOWN;
	private static int MAX_TEST_COUNT;
	/** 最大どれくらいの長さの手を保存するか */
	private final static int MAX_LENGTH = Integer.MAX_VALUE;
	
	private static void fileload(String inputCheckerFile, String answerFile, String outputFile) throws NumberFormatException, IOException {
		BufferedReader br1 = new BufferedReader(new FileReader(new File(inputCheckerFile)));
		BufferedReader br2 = new BufferedReader(new FileReader(new File(answerFile)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFile)));
		
		
		int answerIndex = 0;
		String oneline;
		int lineNum = 0;
		while ((oneline = br1.readLine()) != null) {
			if (lineNum == 0) {
				StringTokenizer st = new StringTokenizer(oneline);
				MAX_LEFT = Integer.parseInt(st.nextToken());
				MAX_RIGHT = Integer.parseInt(st.nextToken());
				MAX_UP = Integer.parseInt(st.nextToken());
				MAX_DOWN = Integer.parseInt(st.nextToken());
			} else if (lineNum == 1) {
				MAX_TEST_COUNT = Integer.parseInt(oneline);
			} else {
				br1.close();
				break;
			}
			lineNum++;
		}
		while ((oneline = br2.readLine()) !=null) {
			System.out.println(oneline.length());
			System.out.println(oneline);
			if (oneline.length() < MAX_LENGTH) {
				bw.write(oneline + "\n");
			for (char c : oneline.toCharArray()) {
				if (c == 'U') {
					MAX_UP--;
				} else if (c == 'R') {
					MAX_RIGHT--;
				} else if (c == 'L') {
					MAX_LEFT--;
				} else if (c == 'D') {
					MAX_DOWN--;
				}
			}
			if (!oneline.isEmpty()) {
				answerIndex++;
			}
			} else {
				bw.write("\n");
			}
			bw.flush();
		}
		br2.close();
		bw.close();
		System.out.println(answerIndex);
		System.out.println("MAX_UP : " + MAX_UP);
		System.out.println("MAX_DOWN : " + MAX_DOWN);
		System.out.println("MAX_LEFT : " + MAX_LEFT);
		System.out.println("MAX_RIGHT : " + MAX_RIGHT);

	}
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		Checker.fileload(args[0], args[1], args[2]);
	}

}
