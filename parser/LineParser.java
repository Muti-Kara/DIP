package image.parser;

import image.data.MyImage;

/**
* LineParser
* @author Muti Kara
*/
public class LineParser extends Parser {
	
	/**
	* Sets the parameters for line parsing
	* @param originalBuffer
	* @param buffer
	*/
	public LineParser(MyImage originalBuffer, MyImage buffer) {
		this.windowSize = new int[]{12, 11, 10, 9, 8, 7};
		this.horizontalDiscontinuity = 50;
		this.verticalDiscontinuity = 20;
		this.originalBuffer = originalBuffer;
		this.buffer = buffer;
		this.integral = new double[buffer.getWidth()][originalBuffer.getHeight()];
		this.filterResponse = new double[buffer.getWidth()][buffer.getHeight()];
		this.usedFilterSize = new int[buffer.getWidth()][buffer.getHeight()];
	}
	
}
