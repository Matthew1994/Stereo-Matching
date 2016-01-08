package StereoMatching;

import java.awt.image.BufferedImage;

public class NCC extends SSD {

	public NCC(BufferedImage leftImg, BufferedImage rightImg) {
		super(leftImg, rightImg);
	}
	
	@Override
	protected double getDist(int[] left, int[] right) {
		int len = left.length;
		double topSum = 0;
		double downLeftSum = 0;
		double downRightSum = 0;
		
		for (int i = 0; i < len; i++) {
			double lefttemp = ((left[i]&0x00ff0000)>>16)*0.299 + ((left[i]&0x0000ff00)>>8)*0.587 + (left[i]&0x000000ff)*0.114;
			double righttemp = ((right[i]&0x00ff0000)>>16)*0.299 + ((right[i]&0x0000ff00)>>8)*0.587 + (right[i]&0x000000ff)*0.114;	

			topSum += lefttemp*righttemp;
			downLeftSum += lefttemp*lefttemp;
			downRightSum += righttemp*righttemp;
		}
		
		double nc = (double)(topSum/(Math.sqrt(downLeftSum*downRightSum)));
		
		return 1-nc;
	}

}
