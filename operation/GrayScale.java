package image.operation;

import image.data.MyImage;

/**
* GrayScale
* @author Muti Kara
*/
public class GrayScale {
	static final double CLF_RED = 0.299;
	static final double CLF_GREEN = 0.587;
	static final double CLF_BLUE = 0.114;
	
	/**
	* 
	* @param p
	* @return grayscaled version of p
	*/
	public static void convertRGBtoGrayScale(MyImage image) {
		for(int w = 0; w < image.getWidth(); w++){
			for(int h = 0; h < image.getHeight(); h++){
				image.set(w, h, makeGray(image.getBuffer().getRGB(w, h)));
			}
		}
	}
	
	/**
	* 
	* @param pixel's RGB
	* @return gray value for pixel
	*/
	public static int makeGray(int p) {
		return (int) ((p >> 16 & 0xff) * CLF_RED + (p >> 8 & 0xff) * CLF_GREEN + (p & 0xff) * CLF_BLUE);
	}
	
}
