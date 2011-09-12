import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 実行クラス
 * @author yosuke
 *
 */
public class Main {
	private static int MAX_TEST_COUNT;
	
	public void fileloadSolver(String inputFile, String outputFile, String answeredFile) throws IOException {
		BufferedReader br = null;
		BufferedReader br2 = null;
		
		final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFile)));
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
			if (answeredFile != null && !answeredFile.isEmpty()) {
				br2 = new BufferedReader(new FileReader(new File(answeredFile)));
			}
			String line = "";
			
			for (int i=0; i<5000; i++) {
				if (br2 != null) {
					line = br2.readLine();
				}
				System.out.println(line);
				System.out.println(line.length());
				if (line.length() > 300) {
					line = "";
				}
				answerBuffer.add(line);
			}
			int totalCount = 0;
			for (String answer : answerBuffer) {
				if (answer.isEmpty())
				totalCount++;
			}
			System.out.println("Total pazzle : " + totalCount);
			
			int pazzleCount = 1;
			int solvedCount = 0;
			int beforeSolved = 0;
				int lineNum = 0;
				br = new BufferedReader(new FileReader(new File(inputFile)));
				beforeSolved = solvedCount;
				int answerIndex = 0;
				while ((oneline = br.readLine()) != null) {
					if (lineNum == 0) {
						StringTokenizer st = new StringTokenizer(oneline);
//						if (!isAlreadyRead) {
//						MAX_LEFT = Integer.parseInt(st.nextToken());
//						MAX_RIGHT = Integer.parseInt(st.nextToken());
//						MAX_UP = Integer.parseInt(st.nextToken());
//						MAX_DOWN = Integer.parseInt(st.nextToken());
//						isAlreadyRead = true;
//						}
					} else if (lineNum == 1) {
						MAX_TEST_COUNT = Integer.parseInt(oneline);
					} else {
						if (answerBuffer.get(answerIndex).isEmpty()) {
							StringTokenizer st = new StringTokenizer(oneline, ",");
							int width = Integer.parseInt(st.nextToken());
							int height = Integer.parseInt(st.nextToken());
							String panel = st.nextToken();
							
							SlidePuzzle sp = new SlidePuzzle(width, height, panel);
							sp.printBoard();
							try {
								sp.solvePuzzle();
							} catch (Throwable t) {
								t.printStackTrace();
								//noop
							}
							if (sp.solved()) {
								solvedCount++;
								System.out.println("solved! : " + solvedCount);
							} else {
//								System.out.println("current top : ");
//								sp.printCurrentDistanceTop();
//								System.out.println();
//								System.out.println("current top(max) : ");
//								sp.printCurrentMaxDistanceTop();
							}
							String operationAnswer = sp.getSolvedString();
							System.out.println(operationAnswer);
							System.out.println("pazzleCount : " + pazzleCount + " solvedCount : " +solvedCount + " rate : " + (double) solvedCount / pazzleCount);
							//						bw.write(operationAnswer + "\n");
							answerBuffer.set(answerIndex, operationAnswer);
							pazzleCount++;
						}
						answerIndex++;
					}
//					bw.flush();
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
		}
	}
	
	public void solveFromOneline(String oneline) {
		StringTokenizer st = new StringTokenizer(oneline, ",");
		int width = Integer.parseInt(st.nextToken());
		int height = Integer.parseInt(st.nextToken());
		String panel = st.nextToken();
		
		SlidePuzzle sp = new SlidePuzzle(width, height, panel);
		sp.printBoard();
		sp.solvePuzzle();
		if (!sp.solved()) {
			System.out.println("current top : ");
			sp.printCurrentDistanceTop();
			System.out.println();
			System.out.println("current top(max) : ");
			sp.printCurrentMaxDistanceTop();
		}
		String operationAnswer = sp.getSolvedString();
		System.out.println(operationAnswer);
	}
	
	public static void main(String[] args) throws IOException {
		Main main = new Main();
		main.fileloadSolver(args[0], args[1], args[2]);
	}

}
