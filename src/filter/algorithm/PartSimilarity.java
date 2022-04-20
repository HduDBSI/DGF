package filter.algorithm;

import filter.model.FreMatrix;

public class PartSimilarity {

	/**
	 * 
	 * @param matrix ������ϵ��
	 * @param alpha ��������
	 * @return
	 */
	public static double[][] getPartSimilarity(FreMatrix matrix){
		//��������
		int length = matrix.getFreMatrix().length;
		//ǰ���¼����������
		int[] preEventClassNum = new int[length];
		//����¼����������
		int[] postEventClassNum = new int[length];
		//ǰ���¼�������
		int[] preEventNum = new int[length];
		//����¼�������
		int[] postEventNum = new int[length];
		//ǰ���ܶ�
		double[] preDensity = new double[length];
		//����ܶ�
		double[] postDensity = new double[length];
		//���ƶ�
		double[][] similarityMatrix = new double[length][length];
		
		for(int i = 0;i < length;i++){
			for(int j = 0;j < length;j++){
				int time = matrix.getFreMatrix()[i][j];
				if(time!=0){
					preEventClassNum[j]+=1;
					preEventNum[j]+=time;
					postEventClassNum[i]+=1;
					postEventNum[i]+=time;
				}
			}
		}
		//����ƽ��ֵ
		for(int i = 0;i < length;i++){
			if (preEventClassNum[i] == 0){
				preDensity[i] = 0;
			}else {
				preDensity[i] = preEventNum[i]/(double)preEventClassNum[i];
			}
			if(postEventClassNum[i] == 0){
				postDensity[i] = 0;
			}else{
				postDensity[i] = postEventNum[i]/(double)postEventClassNum[i];
			}
		}
		//�������ƶ�
		for(int i = 0;i < length;i++){
			for(int j = 0;j < length;j++){
				int time = matrix.getFreMatrix()[i][j];
				if(preDensity[j]==0||postDensity[i]==0)
				{
					similarityMatrix[i][j] = 0;
				}else {
					
					//double number = 1.0 - 1.0/((1.0+Math.exp((time-preDensity[j])*4/preDensity[j]))*2) - 1.0/((1.0+Math.exp((time-postDensity[i])*4/postDensity[i]))*2);
					
					double number = (Math.exp(2*time/postDensity[i])-1)/(2*(Math.exp(2*time/postDensity[i])+1)) + (Math.exp(2*time/preDensity[j])-1)/(2*(Math.exp(2*time/preDensity[j])+1));
					
					if (number<0) {
						similarityMatrix[i][j]=0;
					}else{
						similarityMatrix[i][j]=number;
					}
					

				}

			}
		}
		
		return similarityMatrix;
	}
	
}
