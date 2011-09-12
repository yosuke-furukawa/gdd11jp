import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * ボードの状況を記録して、評価するクラス
 * @author yosuke
 *
 */
public class AnswerBoard {
	
	/**
	 * 比較評価メソッド
	 * @param map
	 * @return
	 */
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	            	//距離を計測
	                int compareto = e1.getValue().compareTo(e2.getValue());
	                if(compareto > 0) {
	    		    	return 1;
	    		    } else if(compareto == 0) {
	    		    	//計測した距離が同じ場合に、パズルが綺麗に並んでいる方を優先する
	    		    	String keyATemp = ((String)e1.getKey()).replace('0', 'z');
	    		    	String keyBTemp = ((String)e2.getKey()).replace('0', 'z');
	    		    	return keyATemp.compareTo(keyBTemp);
	    		    } else {
	    		    	return -1;
	    		    }
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
	/**  keyにボードの文字列、valueに総合距離を持つマップ */
	private Map<String, Integer> answerDistanceMap = new TreeMap<String, Integer>();
	/** keyにボードの文字列、valueに文字間の最大距離長を持つマップ*/
	private Map<String, Integer> answerMaxDistanceCharMap = new TreeMap<String, Integer>();
	
	/**
	 * Historyに追加するメソッド
	 * @param key - ボードの文字列
	 * @param distance - 総合距離
	 * @param maxCharDistance - 文字間の最大距離長
	 */
	public void put(String key, Integer distance, Integer maxCharDistance) {
		answerDistanceMap.put(key, distance);
		answerMaxDistanceCharMap.put(key, maxCharDistance);
	}
	
	public int size() {
		return answerDistanceMap.keySet().size();
	}
	/** 距離に基づいて、解答候補リストを作成する */
	public List<String> getListFromCandidate(int size) {
		List<String> answerList = new ArrayList<String>();
		Set<Map.Entry<String, Integer>> ansSet = entriesSortedByValues(answerDistanceMap);
		int index = 0;
		for (Map.Entry<String, Integer> answer : ansSet) {
			if (index < size) {
				answerList.add(answer.getKey());
			} else {
				break;
			}
			index++;
		}
		return answerList;
	}
	
	public List<String> getListFromDistance(int distance) {
		List<String> answerList = new ArrayList<String>();
		Set<Entry<String, Integer>> ansSet = answerDistanceMap.entrySet();
		for (Entry<String, Integer> answerEntry : ansSet) {
			int value = answerEntry.getValue();
			if (value <= distance) {
				answerList.add(answerEntry.getKey());
			} else {
				break;
			}
		}
		return answerList;
	}
	
	public List<String> getListFromMaxCharDistance(int maxChardistance) {
		List<String> answerList = new ArrayList<String>();
		Set<Entry<String, Integer>> ansSet = answerMaxDistanceCharMap.entrySet();
		for (Entry<String, Integer> answerEntry : ansSet) {
			int value = answerEntry.getValue();
			if (value <= maxChardistance) {
				answerList.add(answerEntry.getKey());
			} else {
				break;
			}
		}
		return answerList;
	}
	
	public boolean contains(String key) {
		return answerDistanceMap.containsKey(key);
	}
}
