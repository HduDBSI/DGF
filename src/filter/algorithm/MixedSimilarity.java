package filter.algorithm;

public class MixedSimilarity {
	
	/**
	 * ��������ض�
	 * @param parts �ֲ���ضȱ�
	 * @param globals ȫ����ضȱ�
	 * @param alpha �������
	 * @return
	 */
	public static double[][] getMixedSimilarity(double[][] parts,double[][] globals,double alpha){
		int length = parts.length;
		double[][] mixeds = new double[length][length];
		for(int i = 0;i < length;i++){
			for(int j = 0;j < length;j++){
				mixeds[i][j] = alpha*parts[i][j] + (1-alpha)*(globals[i][j]);
				
			}
		}
		return mixeds;
	}


}
