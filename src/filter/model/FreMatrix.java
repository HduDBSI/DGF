package filter.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import filter.tool.Tool;

public class FreMatrix {

	private Map<String,Integer> taskIndexMap = new HashMap<String,Integer>();
	
	private int[][] freMatrix;
	
	public FreMatrix(List<String> taskList,List<Trace> traceList){
		for(int i = 0;i < taskList.size();i++){
			taskIndexMap.put(taskList.get(i), i);
		}
		Map<String,Integer> taskIndexMap = new HashMap<String,Integer>();
		for(int i = 0;i < taskList.size();i++){
			taskIndexMap.put(taskList.get(i), i);
		}
		/**
		 * 构建频次依赖表
		 */
		freMatrix = new int[taskList.size()][taskList.size()];
		for(int i = 0;i < traceList.size();i++){
			List<Task> tList = traceList.get(i).getTaskList();
			for(int j=0;j<tList.size()-1;j++){
				String a = tList.get(j).getName();
				String b = tList.get(j+1).getName();
				freMatrix[taskIndexMap.get(a)][taskIndexMap.get(b)]+=1;
			}
		}
		Tool.printMatrix(freMatrix);
	}
	
	

	public Map<String, Integer> getTaskIndexMap() {
		return taskIndexMap;
	}

	public void setTaskIndexMap(Map<String, Integer> taskIndexMap) {
		this.taskIndexMap = taskIndexMap;
	}

	public int[][] getFreMatrix() {
		return freMatrix;
	}

	public void setFreMatrix(int[][] freMatrix) {
		this.freMatrix = freMatrix;
	}
	
	
}
