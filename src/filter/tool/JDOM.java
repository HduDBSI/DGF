package filter.tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.Content;


import filter.model.Task;
import filter.model.Trace;


public class JDOM {

	
	
	/**
	 * 
	 * @param sourceUrl 原文件，用于获取文档结构的结构，在这基础上进行修改，不需要从头开始构建整个文档结构
	 * @param targetUrl 输出mxml文件地址
	 * @param traceList 程序中保存的当前的轨迹，将sourceUrl中任务替换为traceList中的任务
	 * @return
	 */
	public static boolean createMxml(String sourceUrl,String targetUrl,List<Trace> traceList){
		SAXBuilder saxBuilder = new SAXBuilder();
		InputStream in;
		try {
			in = new FileInputStream(sourceUrl);
			InputStreamReader isr = new InputStreamReader(in, "UTF-8");
			// 3.通过saxBuilder的build方法，将输入流加载到saxBuilder中
			Document document = saxBuilder.build(isr);
			// 4.通过document对象获取xml文件的根节点
			Element rootElement = document.getRootElement();
			// 5.获取根节点下的子节点的List集合
			Element processes = (Element)rootElement.getChildren("Process").get(0);
			List<Element> processList = processes.getChildren("ProcessInstance");


			//List<Element> processList = rootElement.getChildren("trace");
			System.out.println("ProcessInstance de size:" + processList.size());
			for(int i = 0;i<traceList.size();i++){
				Trace trace = traceList.get(i);
				List<Task> taskList = trace.getTaskList(); // 轨迹任务
				processList.get(i).removeChildren("AuditTrailEntry");
				for(int j = 0;j< taskList.size();j++){
					Element auditTrailEntry = new Element("AuditTrailEntry");
					auditTrailEntry.addContent(new Element("WorkflowModelElement").setText(taskList.get(j).getName()));
					auditTrailEntry.addContent(new Element("EventType").setText("complete"));
					processList.get(i).addContent(auditTrailEntry);
				}
			}
			/**
			 * 去除原mxml文件中没有被修改的轨迹
			 */
			int processSize = processList.size();
			for(int i = traceList.size();i<processSize;i++){
				processList.remove(traceList.size());
			}
			//产生新的mxml文件
			XMLOutputter XMLOut = new XMLOutputter(FormatXML());  //生成xml文件
			try {
				XMLOut.output(document, 
						new FileOutputStream(targetUrl));
			} catch (FileNotFoundException e) {
				System.out.println("生成xml文件失败！");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("生成xml文件失败！");
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 进行对books.xml文件的JDOM解析
		// 准备工作
		// 1.创建一个SAXBuilder的对象
		SAXBuilder saxBuilder = new SAXBuilder();
		InputStream in;
		try {
			// 2.创建一个输入流，将xml文件加载到输入流中
			in = new FileInputStream("F:\\JAVA\\example-logs\\1.xml");
			InputStreamReader isr = new InputStreamReader(in, "UTF-8");
			// 3.通过saxBuilder的build方法，将输入流加载到saxBuilder中
			Document document = saxBuilder.build(isr);
			// 4.通过document对象获取xml文件的根节点
			Element rootElement = document.getRootElement();
			// 5.获取根节点下的子节点的List集合
			/*Element processes = (Element)rootElement.getChildren("Process").get(0);
			List<Element> processList = processes.getChildren("ProcessInstance");*/
			Element log = (Element)rootElement.getChildren("log").get(0);
			List<Element> processList = log.getChildren("trace");
			for(int i = 0;i<processList.size();i++){
				List<Element> taskList = processList.get(i).getChildren("AuditTrailEntry");
				for(int j = 0;j< taskList.size();j++){
					Element workflowModelElement = taskList.get(j).getChild("WorkflowModelElement");
					String name = workflowModelElement.getValue();
					workflowModelElement.setText("1111"); //设值
				}
			}
			//产生新的mxml文件
			XMLOutputter XMLOut = new XMLOutputter(FormatXML());  //生成xml文件
			try {
				XMLOut.output(document, 
						new FileOutputStream("F:\\JAVA\\example-logs\\1-1.mxml"));
			} catch (FileNotFoundException e) {
				System.out.println("生成xml文件失败！");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("生成xml文件失败！");
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Format FormatXML(){  
        //格式化生成的xml文件 
        Format format = Format.getCompactFormat();  
        format.setEncoding("utf-8");  
        format.setIndent(" ");  
        return format;  
    } 
}
