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
	final int charSmoothing = 10;
	final int lineHeight = 80;
	final int charWidth = 30;
	
	ArrayList<ArrayList<MyImage>> lines = new ArrayList<>();
	
	/**
	* Segmentation stage is done at constructor
	* @param image
	 * @throws IOException
	*/
	public Segmentation(MyImage image) {
		GrayScale.convertRGBtoGrayScale(image);;
		
		Sauvola.binarize(image);
		
		GaussianSmoothing.prepareKernel(lineSmoothing);
		MyImage smoothedImg = GaussianSmoothing.smooth(image);
		
		Parser lineParser = new LineParser(image, smoothedImg);
		ArrayList<MyImage> rawLines = lineParser.parse(lineHeight);
		
		System.out.println(rawLines.size() + " lines.");
		
		GaussianSmoothing.prepareKernel(charSmoothing);
		for (int i = 0; i < rawLines.size(); i++) {
			smoothedImg = GaussianSmoothing.smooth(rawLines.get(i));
			Parser charParser = new CharacterParser(rawLines.get(i), smoothedImg);
			ArrayList<MyImage> rawChars = charParser.parse(charWidth);
			
			int cnt = 0;
			
			if (rawChars.size() < 5) {
				continue;
			}
			
			ArrayList<MyImage> line = new ArrayList<>();
			
			for (int j = 0; j < rawChars.size(); j++) {
				cnt++;
				line.add( rawChars.get(j) );
			}
			
			System.out.println(cnt + " characters in line " + (i+1));
			lines.add(line);
		}
	}
	
	public MyImage[] line(int index) {
		return lines.get(index).toArray(new MyImage[0]);
	}
	
	public int size() {
		return lines.size();
	}
	
}
