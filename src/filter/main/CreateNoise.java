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
	 * 1. ѡ�������¼� 2. ���������־��
	 *
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		DecimalFormat df = new DecimalFormat("######0.00");
		
		double noiseRatio = 0.10;
		
		int noiseEventSize = 0;
		
		String trDirth = "D:/study/������/������/201230/sourceCode/InfrequentDetect/log/0.csv"; 
																				
		
		int depSize = 0;
		//String rootDir = "";
		String targetDir = "D:/study/������/������/201230/sourceCode/InfrequentDetect/log";
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
		 * ���ļ��е��ַ���ת����trace����
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
		 * ��ӡ���񼯺�
		 */
		Iterator<String> it = taskSet.iterator();
		System.out.println("��ӡ���񼯺�++++++++++++++++++++++++++++++++++++++++++");
		while (it.hasNext()) {
			String str = it.next();
			System.out.print(str + ",");
		}
		System.out.println();

		
		/**
		 * ��־������������
		 */
	
//		for (int i = 0; i < traceList.size(); i++) {
//			depSize += traceList.get(i).getTaskSize() - 1;
//		}
//		System.out.println("��������=" + depSize);
		System.out.println("�¼�����=" + (traceSize-1));
		
		
		/**
		 * ��־����������
		 */
		
			//noiseRatio = noiseRatio + 0.05;
			//noiseEventSize = (int) ((depSize * noiseRatio) / (2 * (1 - noiseRatio)));
			noiseEventSize = (int) ((traceSize * noiseRatio) / (1 - noiseRatio));
			/**
			 * ������¼�����=���������� * �������ʣ�/[2*��1-�������ʣ�]
			 * ��Ϊ����һ���¼������2��������ϵ��2*x/(totalDepSize + x)=ratio,���xΪ������ʽ
			 */
			System.out.println("�����¼�����=" + noiseEventSize);
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
					//endTaskSetӦ�ÿ��Լ�
					insertTask = new Task(taskList.get(noiseIndex));
					selectedTrace.addTask(insertIndex, insertTask);
				}else{
					selectedTrace.removeTask(insertIndex);
				}
	
			}
		
			/**
			 * ����MXML��־�ļ�
			 */

//			String sourceMxmlUrl = rootDir + "\\" + fileName;
//			String targetMxmlUrl = targetDir + "\\" + df.format(noiseRatio) + "-"
//					+ fileName;
//			JDOM.createMxml(sourceMxmlUrl, targetMxmlUrl, traceList);

			/**
			 * ����tr��־�ļ�
			 */
			String targetTrPath = targetDir + "/" + "log-" + df.format(noiseRatio) + ".csv";
			Tool.createTrFile(traceList, targetTrPath);
		

	}

}
