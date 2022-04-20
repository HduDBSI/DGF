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
	 * 1. 加载日志文件，解析日志中的事件，构建依赖频次表 2. 计算相关度，得到相关度表 3. 日志轨迹中过滤噪声行为
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String rootFoldDir = "D:/study/大数据/读论文/201230/sourceCode/InfrequentDetect";
		
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
		 * 将文件中的字符串转换成trace对象
		 */
		System.out.println("包含噪声的日志开始：");
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
		System.out.println("包含噪声的日志结束");
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
		 * 构建依赖频次表
		 */
		System.out.println("输出DFD矩阵");
		FreMatrix fMatrix = new FreMatrix(taskList, traceList);
		/**
		 * 计算局部依赖相关度
		 */
		double[][] parts = PartSimilarity.getPartSimilarity(fMatrix);
		System.out.println("输出局部依赖矩阵");
		Tool.printMatrix(parts);
		/**
		 * 计算全局依赖相关度
		 */
		double[][] globals = GlobalSimilarity.getGlobalSimilarity(fMatrix, globalParamter);
		System.out.println("输出全局依赖矩阵");
		Tool.printMatrix(globals);
		/**
		 * 计算整体依赖相关度
		 */
		double[][] mixeds = MixedSimilarity.getMixedSimilarity(parts, globals,
				mixedParamter);
		System.out.println("输出混合依赖矩阵");
		Tool.printMatrix(mixeds);
		/**
		 * 噪声过滤
		 */
		System.out.println("过滤过的日志开始：共需过滤日志" + traceList.size() + "条");
		for (int i = 0; i < traceList.size(); i++) {
			Trace newTrace = NoiseFiltering.filtering(mixeds, fMatrix,
					traceList.get(i));
			if (newTrace != null) {
				newTraceList.add(newTrace);
				// newTrace.printTrace();
			}
		}
		System.out.println("过滤过的日志结束");

		/**
		 * 生成MXML文件
		 */
		//JDOM.createMxml(noiseFoldDir + "/" + fileName + ".csv", filteredFoldDir + "/" + fileName + ".mxml", newTraceList);
		/**
		 * 生成tr日志文件
		 */
		String targetTrPath = filteredFoldDir + "/" + fileName + "-DGF.csv";
		//String targetTrPath = "D:/study/大数据/读论文/201230/sourceCode/InfrequentDetect/mix/" + fileName + "-" + mixedParamter + ".csv";
		//String targetTrPath = filteredFoldDir + "/" + fileName + "-my.csv";
		Tool.createTrFile(newTraceList, targetTrPath);
		//Tool.createTrFile(traceList, targetTrPath);
	}
	

}
