package filter.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import filter.model.Task;
import filter.model.Trace;


public class Tool {

	/**
	 * 从文件中
	 * @param fileDirth
	 * @return
	 */
	public static List<String> readLine(String fileDirth) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		List<String> lineList = new ArrayList<String>();
		try {
			String str = "";
			fis = new FileInputStream(fileDirth);// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new
											// InputStreamReader的对象
			while ((str = br.readLine()) != null) {
				lineList.add(str);
			}
			// 当读取的一行不为空时,把读到的str的值赋给str1
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lineList;
	}
	
	/**
	 * 将trace事件转换成tr格式
	 * @param traceList
	 * @throws IOException 
	 */
	public static void createTrFile(List<Trace> traceList,String filePath) throws IOException{
		File file = new File(filePath);
		//当前文件夹中存在该文件的话，先删除该文件
		if(file.exists()){
			file.delete();
		}
		 file.createNewFile();
		 FileWriter out=new FileWriter (file);
		 BufferedWriter bw= new BufferedWriter(out);
//		 for(int i=0;i<traceList.size();i++){
//			 List<Task> taskList = traceList.get(i).getTaskList();
//			 for(int j = 0;j < taskList.size();j++){
//				 bw.write(taskList.get(j).getName());
//				 if(j != taskList.size()-1){
//					 bw.write(",");
//				 }
//			 }
//			 bw.newLine();
//         }  
		 bw.write("caseId");
		 bw.write(",");
		 bw.write("event");
		 bw.newLine();
		 for(int i=0;i<traceList.size();i++){
			 List<Task> taskList = traceList.get(i).getTaskList();
			 String id = Integer.toString(i);
			 for(int j = 0;j < taskList.size();j++){
				 bw.write(id);
				 bw.write(",");
				 bw.write(taskList.get(j).getName());
//				 if(j != taskList.size()-1){
//					 bw.write(",");
//				 }
				 bw.newLine();
			 }	 
         }  
		 bw.flush();
		 bw.close();
	}
	
	public static void createTrFileFromMXML(String mxmlPath,String trPath) throws IOException, JDOMException{
		SAXBuilder saxBuilder = new SAXBuilder();
		List<String> stringList = new ArrayList<String>();
		InputStream in;
		in = new FileInputStream(mxmlPath);
		InputStreamReader isr = new InputStreamReader(in, "UTF-8");
		// 3.通过saxBuilder的build方法，将输入流加载到saxBuilder中
		Document document = saxBuilder.build(isr);
		// 4.通过document对象获取xml文件的根节点
		Element rootElement = document.getRootElement();
		// 5.获取根节点下的子节点的List集合
		Element processes = (Element)rootElement.getChildren("Process").get(0);
		List<Element> processList = processes.getChildren("ProcessInstance");
		for(int i = 0;i<processList.size();i++){
			List<Element> taskList = processList.get(i).getChildren("AuditTrailEntry");
			StringBuffer stringBuffer = new StringBuffer();
			for(int j = 0;j < taskList.size();j++){
				String taskString = ((Element)taskList.get(j).getChildren("WorkflowModelElement").get(0)).getText();
				stringBuffer.append(taskString);
				if(j!=taskList.size()-1){
					stringBuffer.append(" ");
				}
			}
			stringList.add(stringBuffer.toString());
		}
		
		
		File file = new File(trPath);
		//当前文件夹中存在该文件的话，先删除该文件
		if(file.exists()){
			file.delete();
		}
		 file.createNewFile();
		 FileWriter out=new FileWriter (file);
		 BufferedWriter bw= new BufferedWriter(out);
		 for(int i=0;i<stringList.size();i++){
			 bw.write(stringList.get(i));
			 bw.newLine();
         }  
		 bw.flush();
		 bw.close();
	}
	
	/**
	 * 得到一个日志文件中的所有的不同的任务，返回类型为Set<String>
	 * 所有不同任务的集合
	 * @param traceList
	 * @return
	 */
	public static Set<String> getDistinctTask(List<Trace> traceList) {
		Set<String> taskSet = new HashSet<String>();
		for (Trace trace : traceList) {
			for (Task task : trace.getTaskList()) {
				taskSet.add(task.getName());
			}
		}
		return taskSet;
	}
	
	/**
	 * 随机生成事件插入的位置 
	 * 轨迹ABCDEF，size=6，则生成的index取值区间为[0,4]
	 * @param traceSize
	 * @return
	 */
	public static int getRandomInsertIndex(int traceSize){
		return ((int)(Math.random()*100))%(traceSize-1);
	}
	
	/**
	 * 随机生成需要插入事件的轨迹下标index
	 * @param traceSize
	 * @return
	 */
	public static int getRandomTraceIndex(int traceSize){
		return ((int)(Math.random()*100))%(traceSize);
	}
	
	/**
	 * 返回[0,i-1]
	 * @param i
	 * @return
	 */
	public static int getRandom(int i){
		return ((int)(Math.random()*100))%(i);
	}
	
	public static void printTrace(Trace trace){
		for(int i = 0;i < trace.getTaskSize();i++){
			System.out.print(trace.getTaskList().get(i).getName() + " ");
		}
		System.out.println();
	}
	
	/**
	 * 打印频次依赖表
	 */
	public static void printMatrix(int[][] matrix){
		for(int i = 0;i < matrix.length;i++){
			for(int j = 0;j < matrix[i].length;j++){
				System.out.printf("%8s",matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * 打印频次依赖表
	 */
	public static void printMatrix(double[][] matrix){
		for(int i = 0;i < matrix.length;i++){
			for(int j = 0;j < matrix[i].length;j++){
				System.out.printf("%8s",String.format("%.2f", matrix[i][j]) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
