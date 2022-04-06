package image.parser;

import java.util.ArrayList;

import image.GrayBuffer;

/**
* LineParser
*/
public class LineParser {
	static final int MIN_LINE_HEIGHT = 50;
	static final int LINE_CONTRAST = 0;
	static final int GAP = 20;
	
	public static ArrayList<GrayBuffer> parseLines(GrayBuffer buffer) {
		ArrayList<GrayBuffer> lines = new ArrayList<>();
		
		int start = GAP, end = 0;
		for(int h = GAP; h < buffer.getHeight() - GAP; h++) {
			double sum = 0;
			for(int w = 0; w < buffer.getWidth(); w++) {
				sum += buffer.get(w, h) - buffer.getBackground() - LINE_CONTRAST;
			}
			if(sum > 0) {
				if(start == -1){
					start = h;
					if(start - end > MIN_LINE_HEIGHT)
						lines.add(buffer.cut(0, end - GAP, buffer.getWidth(), start - end + 2*GAP));
				}
			}else{
				if(start != -1){
					end = h;
					start = -1;
				}
			}
		}
		
		return lines;
	}
	
}
