package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 幅優先探索を使ってGoogle Devquizの一人ゲームを解く。
 * @author yosuke
 *
 */
public class GameByMySelf {
	private int DIV_OP = 0;
	private int MOD_OP = 1;
	private int testcaseNumber = 0;
	private int numofnum = 0;
	private int count = 0;
	
	/**
	 * 幅優先探索的に、全パターンを作りながら力づくで処理する方法。
	 * 
	 * @param integers 数列のリスト
	 * @return 最短で削除する数
	 */
	public void breadthFirstSearch(List<List<Integer>> list) {
		count++;
		List<List<Integer>> newList = new ArrayList<List<Integer>>();
		for (List<Integer> tempList : list) {
			for (int operation = 0 ; operation < 2; operation++) {
				//2で割る -> DIV_OP, mod5を使って削除する -> MOD_OP
				if (operation == DIV_OP) {
					//すべての配列の値を2で割り、値をnewList似格納する
					//すべての配列の値が0なら、2で割る意味が無いので評価しない。
					if (!isAllZero(tempList)) {
						newList.add(allDiv(tempList));
					}
				} else if (operation == MOD_OP) {
					//mod5を利用して、値を削除する。
					//mod5を利用してもひとつも削除できない場合は評価しない。
					if (containsMod5(tempList)) {
						newList.add(allMod(tempList));
					}
				}
			}
			//空配列を見つけたら、終了。
			for (List<Integer> intList : newList) {
				if (intList.isEmpty()) {
					return;
				}
			}
		}
		breadthFirstSearch(newList);
	}
	
	private void clearCount() {
		count = 0;
	}
	
	public int getCount() {
		return count;
	}
	
	/**
	 * すべての値が0ならtrue, 一つでも0じゃなければfalse
	 * @param integers
	 * @return
	 */
	private boolean isAllZero(List<Integer> integers) {
		for (Integer num : integers) {
			if (num != 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * ひとつでもmod5で削除できればtrue,削除できない場合はfalse
	 * @param integers
	 * @return
	 */
	private boolean containsMod5(List<Integer> integers) {
		for (Integer num : integers) {
			if (num % 5 == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * すべて2で割る
	 * @param integers
	 * @return
	 */
	private List<Integer> allDiv(List<Integer> integers) {
		List<Integer> result = new ArrayList<Integer>();
		for (Integer num : integers) {
			result.add(num/2);
		} 
		return result;
	}
	
	/**
	 * すべて5で割って余り0のものを削除する
	 * @param integers
	 * @return
	 */
	private List<Integer> allMod(List<Integer> integers) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i=0; i<integers.size(); i++) {
			if (integers.get(i) % 5 != 0) {
				result.add(integers.get(i));
			}
		}
		return result;
	}
	
	/**
	 * inputFileをリードして、処理を実行し、回数をoutputFileに書きこむ。
	 * @param inputFile
	 * @param outputFile
	 * @throws IOException
	 */
	private void loadFile(File inputFile, File outputFile) throws IOException {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(inputFile));
			bw = new BufferedWriter(new FileWriter(outputFile));
			String line;
			int lineNum = 0;
			while((line = br.readLine()) != null) {
				if (lineNum == 0) {
					testcaseNumber = Integer.parseInt(line);
				} else if (lineNum%2 == 1) {
					numofnum = Integer.parseInt(line);
				} else {
					StringTokenizer st = new StringTokenizer(line);
					List<List<Integer>> list = new ArrayList<List<Integer>>();
					List<Integer> intList = new ArrayList<Integer>();
					while (st.hasMoreTokens()) {
						intList.add(Integer.parseInt(st.nextToken()));
					}
					assert intList.size() == numofnum;
					list.add(intList);
					breadthFirstSearch(list);
					bw.write(getCount() + "\n");
					clearCount();
				}
				lineNum++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
			if (bw != null) {
				bw.close();
			}
		}
	}
	
	public static void main(String[] args) {
		GameByMySelf main = new GameByMySelf();
		try {
			main.loadFile(new File(args[0]), new File(args[1]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
