package filter.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import filter.algorithm.GlobalSimilarity;
import filter.algorithm.MixedSimilarity;
import filter.algorithm.NoiseFiltering;
import filter.algorithm.PartSimilarity;
import filter.model.FreMatrix;
import filter.model.Trace;
import filter.tool.JDOM;
import filter.tool.Tool;

public class Main {

	/**
	 * 1. ������־�ļ���������־�е��¼�����������Ƶ�α� 2. ������ضȣ��õ���ضȱ� 3. ��־�켣�й���������Ϊ
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String rootFoldDir = "D:/study/������/������/201230/sourceCode/InfrequentDetect";
		
		//String[] fileNames = {"0.05-billing10000","0.10-billing10000","0.15-billing10000","0.20-billing10000"};
		//String[] fileNames = {"0.15-HB","0.25-HB","0.15-RTF","0.25-RTF","0.15-BPI2020","0.25-BPI2020","0.15-SC","0.25-SC","0.15-HD","0.25-HD"};
		//String[] fileNames = {"0.20-log-0.05","0.20-log-0.10","0.20-log-0.15","0.20-log-0.20"};
		String[] fileNames = {"0.05-example"};
		
		for(String fileName:fileNames){
			String noiseFoldDir = rootFoldDir + "/noise";
			//String noiseFoldDir = rootFoldDir + "/log";
			String toFilterTrPath = noiseFoldDir + "/"+ fileName + ".csv";
			String filteredFoldDir = rootFoldDir +"/filtered";
			//String filteredFoldDir = rootFoldDir +"/synthetic";
			String alpha = fileName.substring(0,4);
			filter(noiseFoldDir, toFilterTrPath, filteredFoldDir, fileName,Double.parseDouble(alpha));
		}
		
	}

	public static void filter(String noiseFoldDir,String toFilterTrPath,String filteredFoldDir,String fileName,double alpha) throws IOException {
		double globalParamter = alpha;
		double mixedParamter = 0.5;

		List<String> stringList = Tool.readLine(toFilterTrPath);

		List<Trace> traceList = new ArrayList<Trace>();

		Set<String> taskSet;

		List<String> taskList = new ArrayList<String>();

		List<Trace> newTraceList = new ArrayList<Trace>();
		/**
		 * ���ļ��е��ַ���ת����trace����
		 */
		System.out.println("������������־��ʼ��");
//		for (int i = 0; i < stringList.size(); i++) {
//			Trace trace = new Trace();
//			trace.parseTraceFromOneLine(stringList.get(i));
//			//Tool.printTrace(trace);
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
		System.out.println("������������־����");
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
		 * ��������Ƶ�α�
		 */
		System.out.println("���DFD����");
		FreMatrix fMatrix = new FreMatrix(taskList, traceList);
		/**
		 * ����ֲ�������ض�
		 */
		double[][] parts = PartSimilarity.getPartSimilarity(fMatrix);
		System.out.println("����ֲ���������");
		Tool.printMatrix(parts);
		/**
		 * ����ȫ��������ض�
		 */
		double[][] globals = GlobalSimilarity.getGlobalSimilarity(fMatrix, globalParamter);
		System.out.println("���ȫ����������");
		Tool.printMatrix(globals);
		/**
		 * ��������������ض�
		 */
		double[][] mixeds = MixedSimilarity.getMixedSimilarity(parts, globals,
				mixedParamter);
		System.out.println("��������������");
		Tool.printMatrix(mixeds);
		/**
		 * ��������
		 */
		System.out.println("���˹�����־��ʼ�����������־" + traceList.size() + "��");
		for (int i = 0; i < traceList.size(); i++) {
			Trace newTrace = NoiseFiltering.filtering(mixeds, fMatrix,
					traceList.get(i));
			if (newTrace != null) {
				newTraceList.add(newTrace);
				// newTrace.printTrace();
			}
		}
		System.out.println("���˹�����־����");

		/**
		 * ����MXML�ļ�
		 */
		//JDOM.createMxml(noiseFoldDir + "/" + fileName + ".csv", filteredFoldDir + "/" + fileName + ".mxml", newTraceList);
		/**
		 * ����tr��־�ļ�
		 */
		String targetTrPath = filteredFoldDir + "/" + fileName + "-DGF.csv";
		//String targetTrPath = "D:/study/������/������/201230/sourceCode/InfrequentDetect/mix/" + fileName + "-" + mixedParamter + ".csv";
		//String targetTrPath = filteredFoldDir + "/" + fileName + "-my.csv";
		Tool.createTrFile(newTraceList, targetTrPath);
		//Tool.createTrFile(traceList, targetTrPath);
	}
	

}
