import java.util.HashMap;
import java.util.Map;

/**
 * 移動した際のヒストリーを残すアルゴリズム。
 * @author yosuke
 *
 */
public class History {
	private Map<String, String> operationHistoryMap = new HashMap<String, String>();
	private Map<String, Integer> distanceHistoryMap = new HashMap<String, Integer>();
	private Map<String, Integer> maxDistanceHistoryMap = new HashMap<String, Integer>();
	
	
	
	public void putHistory(String key, String operation, int distance, int maxDistance) {
		String temp = operationHistoryMap.get(key);
		if (temp == null) {
			operationHistoryMap.put(key, operation);
			distanceHistoryMap.put(key, distance);
			maxDistanceHistoryMap.put(key, maxDistance);
		} else if (temp != null && !temp.isEmpty()) {
			if (temp.length() > operation.length()) {
				operationHistoryMap.put(key, operation);
				distanceHistoryMap.put(key, distance);
				maxDistanceHistoryMap.put(key, maxDistance);
			}
		}
	}
	
	public int getDistanceHistory(String key) {
		int result = -1;
		if (distanceHistoryMap.containsKey(key)) {
			result = distanceHistoryMap.get(key);
		}
		return result;
	}
	
	public String getOperationHistory(String key) {
		return operationHistoryMap.get(key);
	}
	
	public int getMaxDistanceHistory(String key) {
		int result = -1;
		if (maxDistanceHistoryMap.containsKey(key)) {
			result = maxDistanceHistoryMap.get(key);
		}
		return result;
	}
	
	public String getCurrentDistanceTop() {
		int min = Integer.MAX_VALUE;
		String result = "";
		for (String key : operationHistoryMap.keySet()) {
			int temp = getDistanceHistory(key);
			if (min < temp) {
				min = temp;
				result = key;
			}
		}
		return result;
	}
	
	public String getCurrentMaxDistanceTop() {
		int min = Integer.MAX_VALUE;
		String result = "";
		for (String key : operationHistoryMap.keySet()) {
			int temp = getMaxDistanceHistory(key);
			if (min < temp) {
				min = temp;
				result = key;
			}
		}
		return result;
	}
}
