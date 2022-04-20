package filter.algorithm;

public class MixedSimilarity {
	
	/**
	 * 计算混合相关度
	 * @param parts 局部相关度表
	 * @param globals 全局相关度表
	 * @param alpha 混合因子
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
