package filter.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import filter.model.Task;
import filter.tool.Tool;

/**
 * 
 */
public class Test {
    public static void main(String[] args)throws IOException{
    	File file = new File("D:/study/大数据/读论文/201230/sourceCode/InfrequentDetect/noise/example.csv");
		//当前文件夹中存在该文件的话，先删除该文件
		if(file.exists()){
			file.delete();
		}
		 file.createNewFile();
		 FileWriter out=new FileWriter (file);
		 BufferedWriter bw= new BufferedWriter(out);
		 
		 bw.write("caseId");
		 bw.write(",");
		 bw.write("event");
		 bw.newLine();
		 for(int i=0;i<100;i++){
			 String id = Integer.toString(i);
			 bw.write(id+",A");
			 bw.newLine();
			 bw.write(id+",B");
			 bw.newLine();
			 bw.write(id+",C");
			 bw.newLine();
			 bw.write(id+",D");
			 bw.newLine();
			 bw.write(id+",E");
			 bw.newLine();
			 bw.write(id+",G");
			 bw.newLine();
			 bw.write(id+",H");
			 bw.newLine();
         }  
		 for(int i=100;i<200;i++){
			 String id = Integer.toString(i);
			 bw.write(id+",A");
			 bw.newLine();
			 bw.write(id+",C");
			 bw.newLine();
			 bw.write(id+",B");
			 bw.newLine();
			 bw.write(id+",D");
			 bw.newLine();
			 bw.write(id+",E");
			 bw.newLine();
			 bw.write(id+",G");
			 bw.newLine();
			 bw.write(id+",H");
			 bw.newLine();
         }
		 for(int i=200;i<300;i++){
			 String id = Integer.toString(i);
			 bw.write(id+",A");
			 bw.newLine();
			 bw.write(id+",B");
			 bw.newLine();
			 bw.write(id+",C");
			 bw.newLine();
			 bw.write(id+",D");
			 bw.newLine();
			 bw.write(id+",F");
			 bw.newLine();
			 bw.write(id+",G");
			 bw.newLine();
			 bw.write(id+",H");
			 bw.newLine();
         }
		 for(int i=300;i<400;i++){
			 String id = Integer.toString(i);
			 bw.write(id+",A");
			 bw.newLine();
			 bw.write(id+",C");
			 bw.newLine();
			 bw.write(id+",B");
			 bw.newLine();
			 bw.write(id+",D");
			 bw.newLine();
			 bw.write(id+",F");
			 bw.newLine();
			 bw.write(id+",G");
			 bw.newLine();
			 bw.write(id+",H");
			 bw.newLine();
         }
		 for(int i=400;i<500;i++){
			 String id = Integer.toString(i);
			 bw.write(id+",A");
			 bw.newLine();
			 bw.write(id+",I");
			 bw.newLine();
			 bw.write(id+",H");
			 bw.newLine();
         }
		 
		 bw.write("500,A");
		 bw.newLine();
		 bw.write("500,B");
		 bw.newLine();
		 bw.write("500,D");
		 bw.newLine();
		 bw.write("500,G");
		 bw.newLine();
		 bw.write("500,H");
		 bw.newLine();
		 
		 bw.write("501,A");
		 bw.newLine();
		 bw.write("501,B");
		 bw.newLine();
		 bw.write("501,C");
		 bw.newLine();
		 bw.write("501,D");
		 bw.newLine();
		 bw.write("501,E");
		 bw.newLine();
		 bw.write("501,F");
		 bw.newLine();
		 bw.write("501,G");
		 bw.newLine();
		 bw.write("501,H");
		 bw.newLine();
		 
		 bw.flush();
		 bw.close();
    }
}
