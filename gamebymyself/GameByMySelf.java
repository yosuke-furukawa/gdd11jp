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
 * ���D��T�����g����Google Devquiz�̈�l�Q�[���������B
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
	 * ���D��T���I�ɁA�S�p�^�[�������Ȃ���͂Â��ŏ���������@�B
	 * 
	 * @param integers ����̃��X�g
	 * @return �ŒZ�ō폜���鐔
	 */
	public void breadthFirstSearch(List<List<Integer>> list) {
		count++;
		List<List<Integer>> newList = new ArrayList<List<Integer>>();
		for (List<Integer> tempList : list) {
			for (int operation = 0 ; operation < 2; operation++) {
				//2�Ŋ��� -> DIV_OP, mod5���g���č폜���� -> MOD_OP
				if (operation == DIV_OP) {
					//���ׂĂ̔z��̒l��2�Ŋ���A�l��newList���i�[����
					//���ׂĂ̔z��̒l��0�Ȃ�A2�Ŋ���Ӗ��������̂ŕ]�����Ȃ��B
					if (!isAllZero(tempList)) {
						newList.add(allDiv(tempList));
					}
				} else if (operation == MOD_OP) {
					//mod5�𗘗p���āA�l���폜����B
					//mod5�𗘗p���Ă��ЂƂ��폜�ł��Ȃ��ꍇ�͕]�����Ȃ��B
					if (containsMod5(tempList)) {
						newList.add(allMod(tempList));
					}
				}
			}
			//��z�����������A�I���B
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
	 * ���ׂĂ̒l��0�Ȃ�true, ��ł�0����Ȃ����false
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
	 * �ЂƂł�mod5�ō폜�ł����true,�폜�ł��Ȃ��ꍇ��false
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
	 * ���ׂ�2�Ŋ���
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
	 * ���ׂ�5�Ŋ����ė]��0�̂��̂��폜����
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
	 * inputFile�����[�h���āA���������s���A�񐔂�outputFile�ɏ������ށB
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
