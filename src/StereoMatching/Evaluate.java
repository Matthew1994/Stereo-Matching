package StereoMatching;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.xml.transform.Templates;

public class Evaluate {
	private int[][] rawMatrix = null;
	private int[][] goalMatrix = null;
	private int width = 0;
	private int height = 0;
	
	private static final int MAX_DIFF = 3;
	
	public Evaluate(BufferedImage rawImg, BufferedImage goalImg) throws FileNotFoundException {
		width = rawImg.getWidth();
		height = rawImg.getHeight();
		int[] pixels = new int[width*height];
		
		rawImg.getRGB(0, 0, width, height, pixels, 0, width);
		rawMatrix = changeDimension2(pixels, width);
		
		//转换颜色空间
		BufferedImage buf= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics tmp = buf.getGraphics();
		tmp.drawImage(goalImg, 0, 0, null);
		tmp.dispose();
		
		int[] pixels2 = new int[width*height];
		buf.getRGB(0, 0, width, height, pixels2, 0, width);
		
		goalMatrix = changeDimension2(pixels2, width);
		
	}
	
	public double compare() throws FileNotFoundException {
		int counter = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int g = (goalMatrix[j][i]&0x000000ff);
				int r = rawMatrix[j][i]&0x000000ff;
				
				if (g - r > MAX_DIFF || g - r < -MAX_DIFF)
					counter++;
			}
		}
		

		return (double)counter/(double)(width*height);
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
	
}
