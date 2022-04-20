package filter.main;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;

import filter.model.Task;
import filter.model.Trace;
import filter.tool.JDOM;
import filter.tool.Tool;

public class CreateNoise {

	/**
	 * 1. 选择噪声事件 2. 随机插入日志中
	 *
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		DecimalFormat df = new DecimalFormat("######0.00");
		
		double noiseRatio = 0.10;
		
		int noiseEventSize = 0;
		
		String trDirth = "D:/study/大数据/读论文/201230/sourceCode/InfrequentDetect/log/0.csv"; 
																				
		
		int depSize = 0;
		//String rootDir = "";
		String targetDir = "D:/study/大数据/读论文/201230/sourceCode/InfrequentDetect/log";
		//String[] aa = trDirth.split("\\\\");
		//String trFile = aa[aa.length - 1];
		//String bb = trFile.substring(0, trFile.indexOf("."));
		//String postfix = ".mxml";
		//String fileName = bb + postfix;

		
		List<String> stringList = Tool.readLine(trDirth);
		
		List<Trace> traceList = new ArrayList<Trace>();
		
		int traceSize = stringList.size();
		
		Set<String> taskSet;
		
		Set<String> startTaskSet = new HashSet<String>();
		Set<String> endTaskSet = new HashSet<String>();
		startTaskSet.add("Triage");
		endTaskSet.add("Prepare");
		endTaskSet.add("Check");
		endTaskSet.add("Organize Ambulance");
		endTaskSet.add("Register");
		
		List<String> taskList = new ArrayList<String>();
		/**
		 * 将文件中的字符串转换成trace对象
		 */
//		for (int i = 0; i < stringList.size(); i++) {
//			Trace trace = new Trace();
//			
//			String oneline = stringList.get(i);
//			trace.parseTraceFromOneLine(stringList.get(i));
//		
//			String[] labelList = oneline.split(" ");
//			startTaskSet.add(labelList[0]);
//			endTaskSet.add(labelList[labelList.length-1]);
//			
//			traceList.add(trace);
//		}
		String[] inputLine = stringList.get(1).split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)",-1);
		String traceId = inputLine[0];
		Trace trace = new Trace();
		for (int i = 1; i < stringList.size(); i++) {
			inputLine = stringList.get(i).split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)",-1);
			String getId = inputLine[0];
			if(getId.equals(traceId)){
				trace.parseTraceFromOneLine(stringList.get(i));
				//Tool.printTrace(trace);
			}else{
				traceList.add(trace);
				traceId = getId;
				trace = new Trace();
				trace.parseTraceFromOneLine(stringList.get(i));
				//Tool.printTrace(trace);
			}
		}
		traceList.add(trace);
		taskSet = Tool.getDistinctTask(traceList);
		taskList.addAll(taskSet);
		/**
		 * 打印任务集合
		 */
		Iterator<String> it = taskSet.iterator();
		System.out.println("打印任务集合++++++++++++++++++++++++++++++++++++++++++");
		while (it.hasNext()) {
			String str = it.next();
			System.out.print(str + ",");
		}
		System.out.println();

		
		/**
		 * 日志中依赖的数量
		 */
	
//		for (int i = 0; i < traceList.size(); i++) {
//			depSize += traceList.get(i).getTaskSize() - 1;
//		}
//		System.out.println("依赖数量=" + depSize);
		System.out.println("事件数量=" + (traceSize-1));
		
		
		/**
		 * 日志中噪声数量
		 */
		
			//noiseRatio = noiseRatio + 0.05;
			//noiseEventSize = (int) ((depSize * noiseRatio) / (2 * (1 - noiseRatio)));
			noiseEventSize = (int) ((traceSize * noiseRatio) / (1 - noiseRatio));
			/**
			 * 插入的事件数量=（依赖数量 * 噪声比率）/[2*（1-噪声比率）]
			 * 因为插入一个事件会产生2个依赖关系，2*x/(totalDepSize + x)=ratio,求得x为上述公式
			 */
			System.out.println("插入事件数量=" + noiseEventSize);
			for (int i = 0; i < noiseEventSize; i++) {
		
				//Trace selectedTrace = traceList.get(Tool.getRandom(traceSize));
				Trace selectedTrace = traceList.get((int)(Math.random()*5000));
		
				//if(selectedTrace.getTaskSize()<3){break;}
				if(selectedTrace.getTaskSize()<3){continue;}
				//int insertIndex = Tool.getRandom(selectedTrace.getTaskSize()-2) + 1;
				int insertIndex = (int)(Math.random()*(selectedTrace.getTaskSize()-2))+1;
				int noiseIndex;
				Task insertTask;
				
				if(Math.random()< 1) {
					do {
						//noiseIndex = Tool.getRandom(taskList.size());
						noiseIndex = (int)(Math.random()*taskList.size());
					}
					while (startTaskSet.contains(taskList.get(noiseIndex)) || endTaskSet.contains(taskList.get(noiseIndex)));
					//endTaskSet应该可以加
					insertTask = new Task(taskList.get(noiseIndex));
					selectedTrace.addTask(insertIndex, insertTask);
				}else{
					selectedTrace.removeTask(insertIndex);
				}
	
			}
		
			/**
			 * 生成MXML日志文件
			 */

//			String sourceMxmlUrl = rootDir + "\\" + fileName;
//			String targetMxmlUrl = targetDir + "\\" + df.format(noiseRatio) + "-"
//					+ fileName;
//			JDOM.createMxml(sourceMxmlUrl, targetMxmlUrl, traceList);

			/**
			 * 生成tr日志文件
			 */
			String targetTrPath = targetDir + "/" + "log-" + df.format(noiseRatio) + ".csv";
			Tool.createTrFile(traceList, targetTrPath);
		

	}

}
