package StereoMatching;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;


public class SSD {
	private int[][] leftMatrix = null;
	private int[][] rightMatrix = null;
	private int width = 0;
	private int height = 0;
	
	private int MAX_DISPARITY = 79;
	private int VECTOR_SIZE = 5;
	
	public SSD(BufferedImage leftImg, BufferedImage rightImg) {
		width = leftImg.getWidth();
		height = leftImg.getHeight();
		int[] pixels = new int[width*height];
		leftImg.getRGB(0, 0, width, height, pixels, 0, width);
		leftMatrix = changeDimension2(pixels, width);
		rightImg.getRGB(0, 0, width, height, pixels, 0, width);
		rightMatrix = changeDimension2(pixels, width);
	}
	
	public BufferedImage getLeftDisparityMap() {
		return getDisparityMap(0);
	}
	
	public BufferedImage getRightDisparityMap() {
		return getDisparityMap(1);
	}
	
	private BufferedImage getDisparityMap(int flag) {
		int[][] disparityMatrix = new int[height][width];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				//flag==0代表是左视觉,否则是右视觉
				disparityMatrix[j][i] = getMinDisparityValue(i, j, MAX_DISPARITY, flag, VECTOR_SIZE);
			}
		}
	
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int singleChannel = disparityMatrix[j][i]*3;
				disparityMatrix[j][i] = (singleChannel<<16) | (singleChannel<<8) | (singleChannel) |0xff000000;
			}
		}
		
        
		BufferedImage disparityMap = new BufferedImage(width, height, 5);
		disparityMap.setRGB(0, 0, width, height, changeDimension1(disparityMatrix), 0, width);
		
		return disparityMap;
	}
	
	/* 
    * 在(x,y)处获取使得特征向量差距最小的d
	* 其中x,y代表坐标,dmax代表视差值最大取值,flag代表是左视觉还是右视觉,n代表特征向量的大小
	* */
	private int getMinDisparityValue(int x, int y, int dmax, int flag, int n) {
		double disparityValue = 1000000;
		int d = 0;
		for (int i = 0; i <= dmax; i++) {
			int[] leftVector = null;
			int[] rightVector =null;
			if (flag == 0) {
				leftVector = getFeatureVector(x, y, 0, n);
				rightVector = x-i >= 0 ? getFeatureVector(x-i, y, 1, n) : getFeatureVector(x, y, 1, n);
			}
			else {
				leftVector = x+i < width ? getFeatureVector(x+i, y, 0, n) : getFeatureVector(x, y, 0, n);
				rightVector = getFeatureVector(x, y, 1, n);
			}
			double disTemp = getDist(leftVector, rightVector); 
			if ( disTemp < disparityValue) {
				disparityValue = disTemp;
				d = i;
			}
		}
		return d;
	}
	
   /* 
    * 在(x,y)处提取n*n的特征向量
	* 其中x,y代表坐标,flag代表是左视觉还是右视觉,n代表特征向量的大小
	* */
	private int[] getFeatureVector(int x, int y, int flag, int n) {
		int[] vector = new int[n*n];
		if (flag == 0) {
			int count = 0;
			for (int i = -(n/2); i <= (n/2); i++) {
				for (int j = -(n/2);j <= (n/2); j++) {
					//vector[i*n+j] = ((x+i<width)&&(x-i>=0)&&(y+j<height)&&(y-j>=0) )?leftMatrix[y+j][x+i]:leftMatrix[y][x];
					vector[count++] = ((x+i) < width && (x+i) >= 0 && (y+j) < height && (y+j) >= 0) ? leftMatrix[y+j][x+i] : leftMatrix[y][x];
				}
			}
		}
		else {
			int count = 0;
			for (int i = -(n/2); i <= (n/2); i++) {
				for (int j = -(n/2);j <= (n/2); j++) {
					//vector[i*n+j] = ((x+i<width)&&(x-i>=0)&&(y+j<height)&&(y-j>=0) )?rightMatrix[y+j][x+i]:rightMatrix[y][x];
					vector[count++] = ((x+i) < width && (x+i) >= 0 && (y+j) < height && (y+j) >= 0) ? rightMatrix[y+j][x+i] : rightMatrix[y][x];
				}
			}
		}
		
		return vector;
	}
	
	//计算向量之间的差距(对应位置作差然后求和)
	protected double getDist(int[] left, int[] right) {
		int len = left.length;
		int sum = 0;
		for (int i = 0; i < len; i++) {
			
			double lefttemp = ((left[i]&0x00ff0000)>>16)*0.299 + ((left[i]&0x0000ff00)>>8)*0.587 + (left[i]&0x000000ff)*0.114;
			double righttemp = ((right[i]&0x00ff0000)>>16)*0.299 + ((right[i]&0x0000ff00)>>8)*0.587 + (right[i]&0x000000ff)*0.114;	
			
			sum += (int)lefttemp-(int)righttemp > 0 ? (int)lefttemp-(int)righttemp : (int)righttemp-(int)lefttemp;
		}
		return (double)sum;
	}
	
	public void setWindowSize(int n) {
		VECTOR_SIZE = (n%2 == 0) ? n+1 : n;
	}
	
	public int getWindowSize() {
		return VECTOR_SIZE;
	}
	
	protected int[][] getLeftMatrix() {
		return leftMatrix;
	}
	
	protected int[][] getRightMatrix() {
		return rightMatrix;
	}
	
	protected int getWidth() {
		return width;
	}
	
	protected int getHeight() {
		return height;
	}
	
	//change the array dimension to 2
	private static int[][] changeDimension2(int[] oldArr, int width) {
		int[][] newArr = new int[oldArr.length/width][width];
		for (int i = 0; i < oldArr.length/width; i++) {
			for (int y = 0; y < width; y++) {
				newArr[i][y] = oldArr[i*width + y];
			}
		}
		return newArr;
	}
	
	//change the array dimension to 1
	private static int[] changeDimension1(int[][] oldArr) {
		int[] newArr = new int[oldArr.length*oldArr[0].length];
		for (int i = 0; i < oldArr.length; i++) {
			for (int y = 0; y < oldArr[0].length; y++) {
				newArr[i*oldArr[0].length + y] = oldArr[i][y];
			}
		}
		return newArr;
	}
	
}
