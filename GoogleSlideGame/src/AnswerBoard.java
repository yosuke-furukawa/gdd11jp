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
 * �{�[�h�̏󋵂��L�^���āA�]������N���X
 * @author yosuke
 *
 */
public class AnswerBoard {
	
	/**
	 * ��r�]�����\�b�h
	 * @param map
	 * @return
	 */
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	            	//�������v��
	                int compareto = e1.getValue().compareTo(e2.getValue());
	                if(compareto > 0) {
	    		    	return 1;
	    		    } else if(compareto == 0) {
	    		    	//�v�����������������ꍇ�ɁA�p�Y�����Y��ɕ���ł������D�悷��
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
	/**  key�Ƀ{�[�h�̕�����Avalue�ɑ������������}�b�v */
	private Map<String, Integer> answerDistanceMap = new TreeMap<String, Integer>();
	/** key�Ƀ{�[�h�̕�����Avalue�ɕ����Ԃ̍ő勗���������}�b�v*/
	private Map<String, Integer> answerMaxDistanceCharMap = new TreeMap<String, Integer>();
	
	/**
	 * History�ɒǉ����郁�\�b�h
	 * @param key - �{�[�h�̕�����
	 * @param distance - ��������
	 * @param maxCharDistance - �����Ԃ̍ő勗����
	 */
	public void put(String key, Integer distance, Integer maxCharDistance) {
		answerDistanceMap.put(key, distance);
		answerMaxDistanceCharMap.put(key, maxCharDistance);
	}
	
	public int size() {
		return answerDistanceMap.keySet().size();
	}
	/** �����Ɋ�Â��āA�𓚌�⃊�X�g���쐬���� */
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
