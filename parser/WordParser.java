package image.parser;

import java.util.ArrayList;

import image.GrayBuffer;

/**
* WordParser
*/
public class WordParser {
	
	static final int MIN_WORD_GAP = 130;
	static final int WORD_CONTRAST = 3;
	static final int GAP = 30;
	
	public static ArrayList<GrayBuffer> parseWords(GrayBuffer buffer) {
		ArrayList<GrayBuffer> words = new ArrayList<>();
		ArrayList<Integer> checkpoints = new ArrayList<>();
		
		int checkpoint1 = GAP, checkpoint2 = GAP, counter = 0;
		for(int w = GAP; w < buffer.getWidth(); w++) {
			double sum = 0;
			for(int h = 0; h < buffer.getHeight() - GAP; h++) {
				sum += buffer.get(w, h) - buffer.getBackground() - WORD_CONTRAST;
			}
			if(sum > 0) {
				if(counter == 0 && w - checkpoint1 > MIN_WORD_GAP) {
					checkpoint2 = w;
				}
				counter++;
			}else{
				if(counter > MIN_WORD_GAP) {
					checkpoint1 = w;
					checkpoints.add(checkpoint2);
					checkpoints.add(checkpoint1);
				}
				counter = 0;
			}
		}
		
		checkpoints.add(checkpoint2);
		
		for (int i = 1; i < checkpoints.size() - 1; i += 2) {
			if (checkpoints.get(i) < checkpoints.get(i+1) )
				words.add(buffer.cut(checkpoints.get(i) - GAP, 0, checkpoints.get(i+1) - checkpoints.get(i) + 2*GAP, buffer.getHeight()));
		}
		
		return words;
	}
	
}
