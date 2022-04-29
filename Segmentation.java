package image;

import java.util.ArrayList;

import image.data.MyImage;
import image.operation.GaussianSmoothing;
import image.operation.GrayScale;
import image.operation.Sauvola;
import image.parser.CharacterParser;
import image.parser.LineParser;
import image.parser.Parser;

/**
* Segments the given image and holds an ArrayList of [ArrayList of (MyImages)(characters)][lines].
* @author Muti Kara
*/
public class Segmentation {
	final int lineSmoothing = 60;
	final int charSmoothing = 15;
	final int lineHeight = 80;
	final int charWidth = 20;
	
	ArrayList<ArrayList<MyImage>> lines = new ArrayList<>();
	
	/**
	* Segmentation stage is done at constructor
	* @param image
	*/
	public Segmentation(MyImage image) {
		GrayScale.convertRGBtoGrayScale(image);;
		
		Sauvola.binarize(image);
		
		GaussianSmoothing.prepareKernel(lineSmoothing);
		MyImage smoothedImg = GaussianSmoothing.smooth(image);
		
		Parser lineParser = new LineParser(image, smoothedImg);
		ArrayList<MyImage> rawLines = lineParser.parse(lineHeight);
		
		GaussianSmoothing.prepareKernel(charSmoothing);
		for (int i = 0; i < rawLines.size(); i++) {
			smoothedImg = GaussianSmoothing.smooth(rawLines.get(i));
			Parser charParser = new CharacterParser(rawLines.get(i), smoothedImg);
			ArrayList<MyImage> rawChars = charParser.parse(charWidth);
			
			if (rawChars.size() < 5) {
				continue;
			}
			
			ArrayList<MyImage> line = new ArrayList<>();
			
			for (int j = 0; j < rawChars.size(); j++) {
				line.add( rawChars.get(j) );
			}
			
			lines.add(line);
		}
	}
	
	public ArrayList<MyImage> line(int index) {
		return lines.get(index);
	}
	
	public int size() {
		return lines.size();
	}
	
}
