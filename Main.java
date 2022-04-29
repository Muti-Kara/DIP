package image;

import java.io.IOException;
import java.util.ArrayList;

import image.data.MyImage;
import image.io.MyImageIO;


/**
 * An example usage of this library. Reads image from input.jpg file. Then applies line and character parsing.
 * Then it prints single characters into this folder with a generic name char(n).jpg
*/
public class Main {
	
	public static void main(String[] args) throws IOException {
		MyImage img = MyImageIO.read("input.jpg");
		
		img = img.resize(720, 480);
		
		Segmentation segment = new Segmentation(img);
		
		int k = 0;
		for (int i = 0; i < segment.size(); i++) {
			ArrayList<MyImage> line = segment.line(i);
			for (MyImage buffer : line) {
				MyImageIO.write(buffer, "char" + (k++) + ".jpg");
			}
		}
		
	}
	
}
