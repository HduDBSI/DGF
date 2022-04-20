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
	 * ���������һ����־����־��ʽString������֮���ÿո����
	 * �����õ�һ��Trace����
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
	 * ��ӵ��¼�����ǵ�����ԭ��index�¼��ĺ���
	 * @param index
	 * @param task
	 */
	public void addTask(int index,Task task){
		taskList.add(index, task);
	}
	/**######
	 * ɾ��index�ϵ��¼�
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
