package dip.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import dip.data.MyImage;


/**
* ImageReader
* Reads an image from given file name
* @author Muti Kara
*/
public class MyImageIO {
	
	public static MyImage read(String fileName) throws IOException {
		BufferedImage image = ImageIO.read(new File(fileName));
		return new MyImage(image);
	}
	
	public static void write(MyImage img, String fileName) throws IOException {
		ImageIO.write(img.getBuffer(), "JPG", new File(fileName));
	}
	
}
