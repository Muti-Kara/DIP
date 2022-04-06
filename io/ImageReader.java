package image.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import image.GrayBuffer;

/**
* ImageReader
*/
public class ImageReader {
	
	public static GrayBuffer read(String fileName) throws IOException {
		BufferedImage image = ImageIO.read(new File(fileName));
		return new GrayBuffer(image);
	}
	
}
