package filter.algorithm;

import filter.model.FreMatrix;

public class PartSimilarity {

	/**
	 * 
	 * @param matrix 依赖关系表
	 * @param alpha 噪声比例
	 * @return
	 */
	public static double[][] getPartSimilarity(FreMatrix matrix){
		//任务数量
		int length = matrix.getFreMatrix().length;
		//前驱事件种类的数量
		int[] preEventClassNum = new int[length];
		//后继事件种类的数量
		int[] postEventClassNum = new int[length];
		//前驱事件的数量
		int[] preEventNum = new int[length];
		//后继事件的数量
		int[] postEventNum = new int[length];
		//前驱密度
		double[] preDensity = new double[length];
		//后继密度
		double[] postDensity = new double[length];
		//相似度
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
		//计算平均值
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
		//计算相似度
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
