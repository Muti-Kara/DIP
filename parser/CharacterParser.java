package image.parser;

import java.util.ArrayList;

import image.GrayBuffer;

/**
* CharacterParser
*/
public class CharacterParser {
	
	static final int MIN_CHAR_LEN = 50;
	static final double CHAR_CONTRAST = 3;
	static final int GAP = 10;
	
	public static ArrayList<GrayBuffer> parseCharacters(GrayBuffer buffer) {
		ArrayList<GrayBuffer> chars = new ArrayList<>();
		
		int start = GAP, end = 0;
		for(int w = GAP; w < buffer.getWidth(); w++) {
			double sum = 0;
			for(int h = 0; h < buffer.getHeight() - GAP; h++) {
				sum += buffer.get(w, h) - buffer.getBackground() - CHAR_CONTRAST;
			}
			if(sum > 0) {
				if(start == -1){
					start = w;
					if (start - end > MIN_CHAR_LEN)
						chars.add(buffer.cut(end - GAP, 0, start - end + 2*GAP, buffer.getHeight()));
				}
			}else{
				if(start != -1){
					end = w;
					start = -1;
				}
			}
		}
		
		return chars;
	}
	
}
