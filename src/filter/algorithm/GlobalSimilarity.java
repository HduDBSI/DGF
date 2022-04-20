package filter.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import filter.model.FreMatrix;

public class GlobalSimilarity {
	
	public static double[][] getGlobalSimilarity(FreMatrix matrix,double alpha){
		List<InnerDep> innerDepList = new ArrayList<InnerDep>();
		//��������Ҳ�Ǿ���ĳ���
		int length = matrix.getFreMatrix().length;
		int[][] freMatrix = matrix.getFreMatrix();
		//����������ϵ�����Ĵ���
		int totalTimes = 0;
		//���������ֵ������ʱ����ǰ�ɵ͵����ۼӵ�������
		int partTimes = 0;
		//ȡ��ceitaʱ��ϵ�����Ĵ���
		int ceitaTimes = 0;
		//�Ƚϵ���ֵ
		double ceita = 0;
		//ȫ����ضȱ�
		double[][] globals = new double[length][length];
		for(int i = 0;i < length;i++){
			for(int j = 0;j < length;j++){
				innerDepList.add(new InnerDep(i,j,freMatrix[i][j]));
				totalTimes += freMatrix[i][j];
			}
		}
		//��������
		Collections.sort(innerDepList);
		//��ӡ������
		
		
		for(int i = 0;i < innerDepList.size();i++){
			partTimes += innerDepList.get(i).getTimes();
			if((partTimes/(double)totalTimes) < alpha){ //δ�ﵽ��ֵ������ۼ�
				ceitaTimes = innerDepList.get(i).getTimes();
				continue;
			}else{//�ﵽ��ֵ����ͣ
				break;
			}
		}
		//����ceita��ֵ
		ceita = ceitaTimes/(double)totalTimes;
		System.out.println("ceita:" + ceita + "ceitaTimes:" + ceitaTimes);
		//����ȫ�ֱ�
	
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
	 * ������ϵ
	 * @author 10619
	 *
	 */
	private static class InnerDep implements Comparable<InnerDep>{
		//������
		private Integer i;
		//������
		private Integer j;
		//ִ�д���
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
