package cn.edu.ncist.model;

public class Normalization{
	
	public static float getResult(double x, int n){
		float result = 0;
		
		if(n >10 ){
			n = 10;
		}
		if(n <= 0){
			result = 0;
		}
		else{
			result = (float)(x + 0.01 * (n - 1));
		}
		
		return result;
	}
	
	public static float getValue(int n){
		float result = (float)0.1;
		if(n == 0){
			result = (float)0.9;
		}
		
		return result;
	}
}