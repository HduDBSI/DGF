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
	 * ���ļ���
	 * @param fileDirth
	 * @return
	 */
	public static List<String> readLine(String fileDirth) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // ���ڰ�װInputStreamReader,��ߴ������ܡ���ΪBufferedReader�л���ģ���InputStreamReaderû�С�
		List<String> lineList = new ArrayList<String>();
		try {
			String str = "";
			fis = new FileInputStream(fileDirth);// FileInputStream
			// ���ļ�ϵͳ�е�ĳ���ļ��л�ȡ�ֽ�
			isr = new InputStreamReader(fis);// InputStreamReader ���ֽ���ͨ���ַ���������,
			br = new BufferedReader(isr);// ���ַ��������ж�ȡ�ļ��е�����,��װ��һ��new
											// InputStreamReader�Ķ���
			while ((str = br.readLine()) != null) {
				lineList.add(str);
			}
			// ����ȡ��һ�в�Ϊ��ʱ,�Ѷ�����str��ֵ����str1
		} catch (FileNotFoundException e) {
			System.out.println("�Ҳ���ָ���ļ�");
		} catch (IOException e) {
			System.out.println("��ȡ�ļ�ʧ��");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// �رյ�ʱ����ð����Ⱥ�˳��ر���󿪵��ȹر������ȹ�s,�ٹ�n,����m
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lineList;
	}
	
	/**
	 * ��trace�¼�ת����tr��ʽ
	 * @param traceList
	 * @throws IOException 
	 */
	public static void createTrFile(List<Trace> traceList,String filePath) throws IOException{
		File file = new File(filePath);
		//��ǰ�ļ����д��ڸ��ļ��Ļ�����ɾ�����ļ�
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
		// 3.ͨ��saxBuilder��build�����������������ص�saxBuilder��
		Document document = saxBuilder.build(isr);
		// 4.ͨ��document�����ȡxml�ļ��ĸ��ڵ�
		Element rootElement = document.getRootElement();
		// 5.��ȡ���ڵ��µ��ӽڵ��List����
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
		//��ǰ�ļ����д��ڸ��ļ��Ļ�����ɾ�����ļ�
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
	 * �õ�һ����־�ļ��е����еĲ�ͬ�����񣬷�������ΪSet<String>
	 * ���в�ͬ����ļ���
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
	 * ��������¼������λ�� 
	 * �켣ABCDEF��size=6�������ɵ�indexȡֵ����Ϊ[0,4]
	 * @param traceSize
	 * @return
	 */
	public static int getRandomInsertIndex(int traceSize){
		return ((int)(Math.random()*100))%(traceSize-1);
	}
	
	/**
	 * ���������Ҫ�����¼��Ĺ켣�±�index
	 * @param traceSize
	 * @return
	 */
	public static int getRandomTraceIndex(int traceSize){
		return ((int)(Math.random()*100))%(traceSize);
	}
	
	/**
	 * ����[0,i-1]
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
	 * ��ӡƵ��������
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
	 * ��ӡƵ��������
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
