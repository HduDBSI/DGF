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
	 * @param sourceUrl ԭ�ļ������ڻ�ȡ�ĵ��ṹ�Ľṹ����������Ͻ����޸ģ�����Ҫ��ͷ��ʼ���������ĵ��ṹ
	 * @param targetUrl ���mxml�ļ���ַ
	 * @param traceList �����б���ĵ�ǰ�Ĺ켣����sourceUrl�������滻ΪtraceList�е�����
	 * @return
	 */
	public static boolean createMxml(String sourceUrl,String targetUrl,List<Trace> traceList){
		SAXBuilder saxBuilder = new SAXBuilder();
		InputStream in;
		try {
			in = new FileInputStream(sourceUrl);
			InputStreamReader isr = new InputStreamReader(in, "UTF-8");
			// 3.ͨ��saxBuilder��build�����������������ص�saxBuilder��
			Document document = saxBuilder.build(isr);
			// 4.ͨ��document�����ȡxml�ļ��ĸ��ڵ�
			Element rootElement = document.getRootElement();
			// 5.��ȡ���ڵ��µ��ӽڵ��List����
			Element processes = (Element)rootElement.getChildren("Process").get(0);
			List<Element> processList = processes.getChildren("ProcessInstance");


			//List<Element> processList = rootElement.getChildren("trace");
			System.out.println("ProcessInstance de size:" + processList.size());
			for(int i = 0;i<traceList.size();i++){
				Trace trace = traceList.get(i);
				List<Task> taskList = trace.getTaskList(); // �켣����
				processList.get(i).removeChildren("AuditTrailEntry");
				for(int j = 0;j< taskList.size();j++){
					Element auditTrailEntry = new Element("AuditTrailEntry");
					auditTrailEntry.addContent(new Element("WorkflowModelElement").setText(taskList.get(j).getName()));
					auditTrailEntry.addContent(new Element("EventType").setText("complete"));
					processList.get(i).addContent(auditTrailEntry);
				}
			}
			/**
			 * ȥ��ԭmxml�ļ���û�б��޸ĵĹ켣
			 */
			int processSize = processList.size();
			for(int i = traceList.size();i<processSize;i++){
				processList.remove(traceList.size());
			}
			//�����µ�mxml�ļ�
			XMLOutputter XMLOut = new XMLOutputter(FormatXML());  //����xml�ļ�
			try {
				XMLOut.output(document, 
						new FileOutputStream(targetUrl));
			} catch (FileNotFoundException e) {
				System.out.println("����xml�ļ�ʧ�ܣ�");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("����xml�ļ�ʧ�ܣ�");
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
		// ���ж�books.xml�ļ���JDOM����
		// ׼������
		// 1.����һ��SAXBuilder�Ķ���
		SAXBuilder saxBuilder = new SAXBuilder();
		InputStream in;
		try {
			// 2.����һ������������xml�ļ����ص���������
			in = new FileInputStream("F:\\JAVA\\example-logs\\1.xml");
			InputStreamReader isr = new InputStreamReader(in, "UTF-8");
			// 3.ͨ��saxBuilder��build�����������������ص�saxBuilder��
			Document document = saxBuilder.build(isr);
			// 4.ͨ��document�����ȡxml�ļ��ĸ��ڵ�
			Element rootElement = document.getRootElement();
			// 5.��ȡ���ڵ��µ��ӽڵ��List����
			/*Element processes = (Element)rootElement.getChildren("Process").get(0);
			List<Element> processList = processes.getChildren("ProcessInstance");*/
			Element log = (Element)rootElement.getChildren("log").get(0);
			List<Element> processList = log.getChildren("trace");
			for(int i = 0;i<processList.size();i++){
				List<Element> taskList = processList.get(i).getChildren("AuditTrailEntry");
				for(int j = 0;j< taskList.size();j++){
					Element workflowModelElement = taskList.get(j).getChild("WorkflowModelElement");
					String name = workflowModelElement.getValue();
					workflowModelElement.setText("1111"); //��ֵ
				}
			}
			//�����µ�mxml�ļ�
			XMLOutputter XMLOut = new XMLOutputter(FormatXML());  //����xml�ļ�
			try {
				XMLOut.output(document, 
						new FileOutputStream("F:\\JAVA\\example-logs\\1-1.mxml"));
			} catch (FileNotFoundException e) {
				System.out.println("����xml�ļ�ʧ�ܣ�");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("����xml�ļ�ʧ�ܣ�");
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
        //��ʽ�����ɵ�xml�ļ� 
        Format format = Format.getCompactFormat();  
        format.setEncoding("utf-8");  
        format.setIndent(" ");  
        return format;  
    } 
}
