package dip.data;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
* My image data structure for image manipulation
* @author Muti Kara
*/
public class MyImage {
	final static int markColor = (0xff << 24 | 0xff << 16 | 0);
	
	BufferedImage img;
	int width;
	int height;
	
	double mean = -1;
	
	/**
	* Creates an GrayBuffer object from given BufferedImage
	* @param img
	*/
	public MyImage(BufferedImage img) {
		this.width = img.getWidth();
		this.height = img.getHeight();
		this.img = img;
	}
	
	/**
	* 
	* @param newWidth
	* @param newHeight
	* @return resized GrayBuffer 
	*/
	public MyImage resize(int newWidth, int newHeight) {
		BufferedImage newImg = new BufferedImage(newWidth, newHeight, this.getBuffer().getType());
		Graphics2D graphics2d = newImg.createGraphics();
		graphics2d.drawImage(img, 0, 0, newWidth, newHeight, null);
		graphics2d.dispose();
		return new MyImage(newImg);
	}
	
	/**
	* Rotates the image.
	* Note: This rotation is similar with getting traspose of image rather than rotating it with an angle!
	* @return rotated image
	*/
	public MyImage rotate() {
		BufferedImage newImg = new BufferedImage(height, width, img.getType());
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				newImg.setRGB(h, w, img.getRGB(w, h));
			}
		}
		return new MyImage(newImg);
	}
	
	/**
	* resizes image with a factor
	* @param factor
	* @return resized graybuffer
	*/
	public MyImage resize(double factor) {
		return resize((int) (width * factor), (int) (height * factor));
	}
	
	/**
	* Follows line data's locations with given height.
	* 
	* @param line
	* @param lineHeight
	* @return extracted line
	*/
	public MyImage getLineImg(Line line, int lineHeight) {
		BufferedImage img = new BufferedImage(line.getSize(), lineHeight, this.img.getType());
		for (int i = 0; i < line.getSize(); i++) {
			for (int j = 0; j < lineHeight; j++) {
				try {
					img.setRGB(i, j, this.img.getRGB(line.getW(i), line.getH(i) + j - lineHeight/2));
				} catch (Exception e) {
					// Nothing requried
				}
			}
		}
		return new MyImage(img);
	}
	
	public double calculateMean() {
		if (mean != -1) {
			return mean;
		}
		double sum = 0;
		for(int w = 0; w < width; w++)
			for(int h = 0; h < height; h++)
				sum += this.get(w, h);
		return mean = sum / width * height;
	}
	
	/**
	* 
	* @return a 2D double array from BufferedImage
	*/
	public double[][] convertToDouble2D() {
		double[][] matrix = new double[width][height];
		for(int w = 0; w < width; w++)
			for(int h = 0; h < height; h++)
				matrix[w][h] = this.get(w, h);
		return matrix;
	}
	
	/**
	* 
	* @param w
	* @param h
	* @return pixel value of (w, h)
	*/
	public int get(int w, int h) {
		return img.getRGB(w, h) & 0xff;
	}
	
	/**
	* Sets pixel with value (between 0 and 255)
	* @param w
	* @param h
	* @param value
	*/
	public void set(int w, int h, int value) {
		img.setRGB(w, h, (0xff << 24 | value << 16 | value << 8 | value));
	}
	
	/**
	* 
	* @return BufferedImage object that this object holds
	*/
	public BufferedImage getBuffer() {
		return img;
	}
	
	/**
	* 
	* @return width of this BufferedImage
	*/
	public int getWidth() {
		return width;
	}

	/**
	* 
	* @return height of this BufferedImage
	*/
	public int getHeight() {
		return height;
	}
	
}
