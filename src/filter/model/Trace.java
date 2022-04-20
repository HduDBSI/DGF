package filter.model;

import java.util.ArrayList;
import java.util.List;

public class Trace {
	public List<Task> taskList;
	
	
	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	public Trace(){
		taskList = new ArrayList<Task>();
	}
	
	/**
	 * 解析输入的一行日志，日志格式String，任务之间用空格隔开
	 * 解析得到一个Trace对象
	 * @param oneline
	 */
	public void parseTraceFromOneLine(String oneline){
		String[] labelList = oneline.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)",-1);
//		for(int i = 0;i < labelList.length;i++){
//			Task task = new Task();
//			task.setName(labelList[i]);
//			this.getTaskList().add(task);
//		}
		Task task = new Task();
		task.setName(labelList[1]);
		this.getTaskList().add(task);
	}
	
	public int getTaskSize(){
		return taskList.size();
	}
	
	/**
	 * 添加的事件最后是到是在原有index事件的后面
	 * @param index
	 * @param task
	 */
	public void addTask(int index,Task task){
		taskList.add(index, task);
	}
	/**######
	 * 删除index上的事件
	 * @param index
	 *
	 */
	public void removeTask(int index){
		taskList.remove(index);
	}
	
	public void printTrace(){
		for(int i = 0;i < taskList.size();i++){
			System.out.print(taskList.get(i).getName() + " ");
		}
		System.out.println();
	}
}
