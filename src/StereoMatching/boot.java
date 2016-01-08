package StereoMatching;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Time;

import javax.imageio.ImageIO;

public class boot {
	public static void main(String[] args) throws IOException {
		
		//测试图片所在文件夹的路径
		File testDirectory = new File("/home/matthew/Desktop/DIP/finalPro/test");
		String[] list = testDirectory.list();
		
		//保存程序运行输出图片的文件夹
		File outputDirectory = new File("/home/matthew/Desktop/DIP/finalPro/result/output");
		if (!outputDirectory.exists() || !outputDirectory.isDirectory()) {
			outputDirectory.mkdir();
		}
		
		//测试结果写入的文件
		//FileOutputStream recordFile = new FileOutputStream("/home/matthew/Desktop/DIP/finalPro/result/ssd.txt");   
		//FileOutputStream recordFile = new FileOutputStream("/home/matthew/Desktop/DIP/finalPro/result/asw.txt");
		FileOutputStream recordFile = new FileOutputStream("/home/matthew/Desktop/DIP/finalPro/result/ncc.txt");
		PrintStream resultOut = new PrintStream(recordFile);
		
		for (int i = 0; i < list.length; i++) {
			//创建输出文件夹
			File subDirectory = new File(outputDirectory.getPath()+"/"+list[i]);
			if (!subDirectory.exists() || !subDirectory.isDirectory()) {
				subDirectory.mkdir();
			}
			
			//获取原图
			File rawImgLeft = new File(testDirectory.getPath()+"/"+list[i]+"/"+"view1.png");
			File rawImgRight = new File(testDirectory.getPath()+"/"+list[i]+"/"+"view5.png");
			BufferedImage leftImg = ImageIO.read(rawImgLeft);
			BufferedImage rightImg = ImageIO.read(rawImgRight);
			
			//获取标准测试样例
			File standardImgLeft = new File(testDirectory.getPath()+"/"+list[i]+"/"+"disp1.png");
			File standardImgRight = new File(testDirectory.getPath()+"/"+list[i]+"/"+"disp5.png");
			
			/*
			//运行SSD
			SSD ssd = new SSD(leftImg, rightImg);
			BufferedImage outputImageL = ssd.getLeftDisparityMap();
			BufferedImage outputImageR = ssd.getRightDisparityMap();
			
			ASW asw = new ASW(leftImg, rightImg);
			BufferedImage outputImageL = asw.getLeftDisparityMap();
			BufferedImage outputImageR = asw.getRightDisparityMap();
			
			*/
			
			
			//运行NCC
			NCC ncc = new NCC(leftImg, rightImg);
			BufferedImage outputImageL = ncc.getLeftDisparityMap();
			BufferedImage outputImageR = ncc.getRightDisparityMap();
			
			/*
			//保存图片到output文件夹,命名为testcasename_disp1_SSD.png
			File saveImgLeft = new File(subDirectory.getPath()+"/"+list[i]+"_disp1_SSD.png");
			File saveImgRight = new File(subDirectory.getPath()+"/"+list[i]+"_disp5_SSD.png");
			
			File saveImgLeft = new File(subDirectory.getPath()+"/"+list[i]+"_disp1_ASW.png");
			File saveImgRight = new File(subDirectory.getPath()+"/"+list[i]+"_disp5_ASW.png");
			*/
			
			File saveImgLeft = new File(subDirectory.getPath()+"/"+list[i]+"_disp1_NCC.png");
			File saveImgRight = new File(subDirectory.getPath()+"/"+list[i]+"_disp5_NCC.png");
			
			ImageIO.write(outputImageL, "png", saveImgLeft);
			ImageIO.write(outputImageR, "png", saveImgRight);
			
			//计算错误率
			BufferedImage standardLeft = ImageIO.read(standardImgLeft);
			BufferedImage standardRight = ImageIO.read(standardImgRight);
			Evaluate evaluateL = new Evaluate(outputImageL, standardLeft);
			Evaluate evaluateR = new Evaluate(outputImageR, standardRight);
			
			//结果输出到文件
			resultOut.println("------------------------------------------------");
			resultOut.println(formatString(list[i], 12)+ " |  " + formatFloat(evaluateL.compare(), 13, 8)  + " |  " + formatFloat(evaluateR.compare(), 13, 8)+ " |  ");
		}
		
		resultOut.close();
		

	}
	
	//规定浮点数输出格式
	public static String formatFloat(double d, int min_length, int precision) {  
        String format = "%-" + (min_length < 1 ? 1 : min_length) + "." + (precision < 0 ? 0 : precision) + "f";  
        return String.format(format, d);  
    }
	
	//规定字符串输出格式
	public static String formatString(String str, int min_length) {  
        String format = "%-" + (min_length < 1 ? 1 : min_length) + "s";  
        return String.format(format, str);  
    }  
}
