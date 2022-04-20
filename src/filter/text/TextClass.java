package filter.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.jdom.JDOMException;
import org.junit.Test;

import filter.tool.Tool;

public class TextClass {

	/*@Test
	public void createTrFromMXML() throws IOException, JDOMException{
		String mxmlPath = "D:\\workspace\\myProject\\tracedata-noise\\filtered\\Fig6p34.mxml";
		String trPath = "D:\\workspace\\myProject\\tracedata-noise\\test\\Fig6p34-from-filtered-mxml.tr";
		Tool.createTrFileFromMXML(mxmlPath, trPath);
	}*/
	
	/*@Test
	public void listAdd(){
		String a = "a";
		String b = "b";
		String c = "c";
		List<String> list = new ArrayList<String>();
		list.add(a);
		list.add(b);
		list.add(0, c);
		for(String s:list){
			System.out.println(s + " ");
		}
	}*/
	@Test
	public void test(){
		double  x= Tool.getRandom(10);
		System.out.println(x);

	}
}
