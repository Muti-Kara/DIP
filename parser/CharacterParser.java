package image.parser;

import java.util.ArrayList;

import image.data.MyImage;

/**
* LineParser
* @author Muti Kara
*/
public class CharacterParser extends Parser {
	
	/**
	* Sets the parameters for character parsing
	* @param originalBuffer
	* @param buffer
	*/
	public CharacterParser(MyImage originalBuffer, MyImage buffer) {
		this.windowSize = new int[]{4, 3, 2, 1};
		this.horizontalDiscontinuity = 10;
		this.verticalDiscontinuity = 5;
		this.originalBuffer = originalBuffer.rotate();
		this.buffer = buffer.rotate();
		this.integral = new double[this.buffer.getWidth()][this.buffer.getHeight()];
		this.filterResponse = new double[this.buffer.getWidth()][this.buffer.getHeight()];
		this.usedFilterSize = new int[this.buffer.getWidth()][this.buffer.getHeight()];
	}
	
	/**
	 * rotates chars' MyImages once more to normalize them
	 * */
	@Override
	public ArrayList<MyImage> extractLines(int lineHeight) {
		ArrayList<MyImage> chars = super.extractLines(lineHeight);
		for (int i = 0; i < chars.size(); i++)
			chars.set(i, chars.get(i).rotate());
		return chars;
	}
	
}
