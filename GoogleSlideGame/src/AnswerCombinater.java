import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AnswerCombinater {
	
	private static int size(List<String> list) {
		int result = 0;
		
		for (String entry : list) {
			if (!entry.isEmpty()) {
				result++;
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader copyFrom1 = new BufferedReader(new FileReader(new File(args[0])));
		BufferedReader copyFrom2 = new BufferedReader(new FileReader(new File(args[1])));
		BufferedWriter copyTo = new BufferedWriter(new FileWriter(new File(args[2])));
		try {
		List<String> copyFromList1 = new ArrayList<String>();
		List<String> copyFromList2 = new ArrayList<String>();
		
		String line;
		while ((line = copyFrom1.readLine()) != null) {
			copyFromList1.add(line);
		}
		while ((line = copyFrom2.readLine()) != null) {
			copyFromList2.add(line);
		}
		
		List<String> toList = new ArrayList<String>();
		for (int i=0; i<5000; i++) {
			String line1 = "";
			if (i < copyFromList1.size()) {
				line1 = copyFromList1.get(i);
			}
			String line2 = "";
			if (i < copyFromList2.size()) {
				line2 = copyFromList2.get(i);
			}
			
			if (!line1.isEmpty() && !line2.isEmpty()) {
				if (line1.length() < line2.length()) {
					toList.add(line1);
				} else {
					toList.add(line2);
				}
			} else if (!line1.isEmpty() || !line2.isEmpty()) {
				if (!line1.isEmpty()) {
					toList.add(line1);
				} else {
					toList.add(line2);
				}
			} else {
				toList.add("");
			}
		}
		int size1 = size(copyFromList1);
		int size2 = size(copyFromList2);
		int size3 = size(toList);
		System.out.println("copy1 : " + size1);
		System.out.println("copy2 : " + size2);
		System.out.println("to : " + size3);
		
		
		for (String entry : toList) {
			copyTo.write(entry + "\n");
		}
		copyTo.flush();
		} finally {
			copyFrom1.close();
			copyFrom2.close();
			copyTo.close();
		}
	}

}
