package StereoMatching;

import java.awt.image.BufferedImage;


public class ASW extends SSD{
	
	//通过查表还原Lab的值
	private int[] LabTable = new int[1024];
	
	public ASW(BufferedImage leftImg, BufferedImage rightImg) {
		super(leftImg, rightImg);
		for (int i = 0; i < 1024; i++) {
		    if (i > 9)
		       LabTable[i] = (int)(Math.pow((float)i / 1020, 1.0F / 3) * (1 << 10) + 0.5 );
		    else
		       LabTable[i] = (int)((29 * 29.0 * i / (6 * 6 * 3 * 1020) + 4.0 / 29) * (1 << 10) + 0.5 );
		}
	}
	
	@Override
	protected double getDist(int[] left, int[] right) {		
		int len = left.length;
		double topSum = 0;
		double downSum = 0;
		for (int i = 0; i < len; i++) {
			double swl = getAdaptiveSupportWeight(left, i);
			double swr = getAdaptiveSupportWeight(right, i);
			topSum += swl*swr*getIntensityDistance(left[i], right[i]);
			downSum += swl*swr;
		}
		return topSum/downSum;
	}
	
	/*
	 * window是传入的特征矩阵,n是指特征矩阵中的第n个
	 */
	private  double getAdaptiveSupportWeight(int[] window, int n) {
		final int k  = 100; 
		double colorCost = getColorDistance(window, n);
		double disCost = getSpatialDistance(window, n);
		return (double)(k*Math.exp(-(colorCost + disCost)));
		
	}
	
	private double getColorDistance(int[] window, int n) {
		int len = window.length;
		
		//经验值
		final int yc = 45;
		
		int[] center = RGBToLab(window[len/2]);
		int[] neighbor = RGBToLab(window[n]);
		double c = (double)Math.sqrt((center[0] - neighbor[0])*(center[0] - neighbor[0])+(center[1] - neighbor[1])*(center[1] - neighbor[1]) + (center[2] - neighbor[2])*(center[2] - neighbor[2]));
		
		return c/yc;
	}
	
	//欧几里得距离
	private double getSpatialDistance(int[] window, int n) {
		int len = (int)Math.sqrt(window.length);
		//经验值
		final int yp = 5;
		double dis = (double)Math.sqrt((n/len - len/2)*(n/len - len/2) + (n%len - len/2)*(n%len - len/2));
		
		return dis/yp;
	}
	
	private int getIntensityDistance(int left, int right) {
		int r = ((left&0x00ff0000)>>16) - ((right&0x00ff0000)>>16);
		int g = ((left&0x0000ff00)>>8) - ((right&0x0000ff00)>>8);
		int b = (left&0x000000ff) - (right&0x000000ff);
		return (r > 0 ? r : -r) + (g > 0 ? g : -g) + (b > 0 ? b : -b);
	}
	
	//牺牲一点精确度,将浮点数换成整数运算,提高运算速度
	private int[] RGBToLab(int pixel) {
		final int big_shift = 18;
		final int HalfShiftValue = 512;
		final int shift = 10;
		final int offset = 128 << shift;
		final int ScaleLC = (16 * (1 << shift));
		final int ScaleLT = 116;
		final int ScaleY = 903;
		final int para1 = 500;
		final int para2 = 200;
		final int ThPara = 9;
		
		int[] rgbImg = new int[3];
		rgbImg[0] = (pixel&0x00ff0000) >> 16;
		rgbImg[1] = (pixel&0x0000ff00) >> 8;
		rgbImg[2] = pixel&0x000000ff;
		
		int[] labImg = new int[3];
		
	    int X = (rgbImg[0] * 199049 + rgbImg[1] * 394494 + rgbImg[2] * 455033 + 524288) >> (big_shift);
	    int Y = (rgbImg[0] * 75675 + rgbImg[1] * 749900 + rgbImg[2] * 223002 + 524288) >> (big_shift);
	    int Z = (rgbImg[0] * 915161 + rgbImg[1] * 114795 + rgbImg[2] * 18621 + 524288) >> (big_shift);

	    labImg[0] = Y > ThPara ? ((ScaleLT * LabTable[Y] - ScaleLC + HalfShiftValue) >> shift) : ((ScaleY * Y) >> shift);
	    labImg[1] = (para1 * (LabTable[X] - LabTable[Y]) + HalfShiftValue + offset) >> shift;
	    labImg[2] = (para2 * (LabTable[Y] - LabTable[Z]) + HalfShiftValue + offset) >> shift;
	    return labImg;
	}
	
}
