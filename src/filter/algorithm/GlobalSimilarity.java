package filter.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import filter.model.FreMatrix;

public class GlobalSimilarity {
	
	public static double[][] getGlobalSimilarity(FreMatrix matrix,double alpha){
		List<InnerDep> innerDepList = new ArrayList<InnerDep>();
		//任务数，也是矩阵的长度
		int length = matrix.getFreMatrix().length;
		int[][] freMatrix = matrix.getFreMatrix();
		//所有依赖关系发生的次数
		int totalTimes = 0;
		//计算低于阈值的总数时，当前由低到高累加到的数量
		int partTimes = 0;
		//取得ceita时关系发生的次数
		int ceitaTimes = 0;
		//比较的阈值
		double ceita = 0;
		//全局相关度表
		double[][] globals = new double[length][length];
		for(int i = 0;i < length;i++){
			for(int j = 0;j < length;j++){
				innerDepList.add(new InnerDep(i,j,freMatrix[i][j]));
				totalTimes += freMatrix[i][j];
			}
		}
		//升序排序
		Collections.sort(innerDepList);
		//打印排序结果
		
		
		for(int i = 0;i < innerDepList.size();i++){
			partTimes += innerDepList.get(i).getTimes();
			if((partTimes/(double)totalTimes) < alpha){ //未达到阈值则继续累加
				ceitaTimes = innerDepList.get(i).getTimes();
				continue;
			}else{//达到阈值后暂停
				break;
			}
		}
		//计算ceita阈值
		ceita = ceitaTimes/(double)totalTimes;
		System.out.println("ceita:" + ceita + "ceitaTimes:" + ceitaTimes);
		//计算全局表
	
		for(int i = 0;i < length;i++){
			for(int j = 0;j < length;j++){
				if (freMatrix[i][j] == 0){
					globals[i][j] = 0;
				}else{
					//globals[i][j] = 1/(1+Math.exp(1-freMatrix[i][j]/(double)ceitaTimes));
					globals[i][j] = (Math.exp(2*freMatrix[i][j]/(double)ceitaTimes)-1)/(Math.exp(2*freMatrix[i][j]/(double)ceitaTimes)+1);
				}

			}
		}
		return globals;
	}
	
	/**
	 * 依赖关系
	 * @author 10619
	 *
	 */
	private static class InnerDep implements Comparable<InnerDep>{
		//横坐标
		private Integer i;
		//纵坐标
		private Integer j;
		//执行次数
		private Integer times;
		
		public InnerDep(Integer i,Integer j,Integer times){
			this.i = i;
			this.j = j;
			this.times = times;
		}
		public Integer getI() {
			return i;
		}
		public void setI(Integer i) {
			this.i = i;
		}
		public Integer getJ() {
			return j;
		}
		public void setJ(Integer j) {
			this.j = j;
		}
		public Integer getTimes() {
			return times;
		}
		public void setTimes(int times) {
			this.times = times;
		}
		@Override
		public int compareTo(InnerDep arg0) {
			return this.getTimes().compareTo(arg0.getTimes());
		}
	}
}
