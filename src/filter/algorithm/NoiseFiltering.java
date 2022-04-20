package filter.algorithm;

import java.util.List;
import java.util.Map;

import filter.model.FreMatrix;
import filter.model.Task;
import filter.model.Trace;

public class NoiseFiltering {

	public static Trace filtering(double[][] mixeds,FreMatrix fMatrix,Trace trace){
		List<Task> oldTaskList = trace.getTaskList();
		Map<String,Integer> taskIndexMap = fMatrix.getTaskIndexMap();
		Trace newTrace = new Trace();
		newTrace.getTaskList().add(new Task(trace.getTaskList().get(0).getName()));
		List<Task> newTaskList = newTrace.getTaskList();
		String taska = newTaskList.get(0).getName();
		String taskb;
		//是否丢弃轨迹的判定因子(改动)
		double setTh = 0.6;
		double threshold = 1;
		double punish =0.8;
		//calculate the parallel block degree
		double md = 0;
		boolean ts = false;
		boolean neverMatch = false;
		for(int i = 1;i < trace.getTaskSize();i++){
			taskb = oldTaskList.get(i).getName();
			int indexa = taskIndexMap.get(taska);
			int indexb = taskIndexMap.get(taskb);
			while(mixeds[indexa][indexb]< 0.49999999 && i<trace.getTaskSize()){
				
				threshold = threshold*punish*(1+2*(1/punish - 1)*mixeds[indexa][indexb]);
				if(threshold < setTh){
					neverMatch = true;
					newTrace = null;
					break;
				}
				
				if(i==trace.getTaskSize()-1){
					neverMatch = true;
					newTrace = null;
					break;
				}
				taskb = oldTaskList.get(++i).getName();
				indexb = taskIndexMap.get(taskb);
				
				if(ts){
					threshold = threshold*Math.pow(punish,md);
				}
				
			}
			if(neverMatch){
				break;
			}
			taska = oldTaskList.get(i).getName();
			newTrace.getTaskList().add(new Task(taska));
		}
		return newTrace;
	}
}
