package dip;

import java.util.ArrayList;

import dip.data.MyImage;
import dip.operation.GaussianSmoothing;
import dip.operation.GrayScale;
import dip.operation.Sauvola;
import dip.parser.CharacterParser;
import dip.parser.LineParser;
import dip.parser.Parser;

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
