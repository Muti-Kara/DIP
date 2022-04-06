package image.io;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import image.GrayBuffer;

/**
* ImageWriter
*/
public class ImageWriter {
	
	public static void write(GrayBuffer img, String fileName) throws IOException {
		ImageIO.write(img.getBuffer(), "JPG", new File(fileName));
	}
	
}
