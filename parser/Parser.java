package dip.parser;

import java.util.ArrayList;

import dip.data.Line;
import dip.data.MyImage;

/**
 * Parser class for applying a parsing routine
 * Use parse() method to get an ArrayList of MyImages
 * @author Muti Kara
*/
public abstract class Parser {
	int verticalDiscontinuity;
	int horizontalDiscontinuity;
	
	int[] windowSize;
	double[][] integral;
	double[][] filterResponse;
	int[][] usedFilterSize;
	
	MyImage buffer;
	MyImage originalBuffer;
	
	/**
	* parses the image and etracts lines with given lineHeight
	* @param lineHeight
	* @return ArrayList of line's MyImages.
	*/
	public ArrayList<MyImage> parse(int lineHeight) {
		buildIntegralImage();
		applyFilters();
		findCenters();
		return extractLines(lineHeight);
	}
	
	/**
	 * Builds integral image of given MyImage
	 * */
	public void buildIntegralImage() {
		integral = buffer.convertToDouble2D();
		for(int w = 0; w < buffer.getWidth(); w++) {
			for(int h = 0; h < buffer.getHeight(); h++) {
				if(w == 0 && h == 0) {
					integral[w][h] = buffer.get(w, h);
				} else if (w == 0) {
					integral[w][h] = integral[w][h - 1] + buffer.get(w, h);
				} else if (h == 0) {
					integral[w][h] = integral[w - 1][h] + buffer.get(w, h);
				} else {
					integral[w][h] = integral[w - 1][h] + integral[w][h - 1] + buffer.get(w, h) - integral[w - 1][h - 1];
				}
			}
		}
	}
	
	/**
	 * Applies the kernels with sizes in WINDOW_SIZES
	 * Puts the maximum values in filterResponse array
	 * Puts the size used for maximum response usedFilterSize array
	 * */
	public void applyFilters() {
		for(int w = 4*windowSize[0]; w < buffer.getWidth() - 4*windowSize[0]; w++){
			for(int h = 2*windowSize[0]; h < buffer.getHeight() - 2*windowSize[0]; h++){
				for(int size : windowSize) {
					double sizeResponse = kernel(w, h, size);
					if (filterResponse[w][h] > sizeResponse) {
						filterResponse[w][h] = sizeResponse;
						usedFilterSize[w][h] = size;
					}
				}
			}
		}
	}
	
	/**
	 * Finds center of lines
	 * */
	public void findCenters() {
		for(int h = 2*windowSize[0]; h < buffer.getHeight() - 2*windowSize[0]; h++){
			for(int w = 4*windowSize[0]; w < buffer.getWidth() - 4*windowSize[0]; w++){
				boolean flag = true;
				for(int k = h - usedFilterSize[w][h]; k <= h + usedFilterSize[w][h]; k++){
					if(h != k && k >= 0 && k < buffer.getHeight() && filterResponse[w][h] <= filterResponse[w][k]) {
						flag = false;
						break;
					}
				}
				if(flag) {
					integral[w][h] *= -1;
				}
			}
		}
	}
	
	/**
	* follows centers and creates and ArrayList of lines' MyImages
	* @param lineHeight
	* @return ArrayList of lines' MyImages
	*/
	public ArrayList<MyImage> extractLines(int lineHeight) {
		ArrayList<MyImage> parsedObj = new ArrayList<>();
		
		for(int w = 0; w < buffer.getWidth(); w++) {
			for (int h = 0; h < buffer.getHeight(); h++) {
				if(integral[w][h] < 0){
					Line line = new Line(w, h);
					extractLine(line);
					if (line.getSize() > 3 * horizontalDiscontinuity)
						parsedObj.add(originalBuffer.getLineImg(line, lineHeight));
				}
			}
		}
		
		return parsedObj;
	}
	
	/**
	* extends line until the end of image or a discontinuity
	* @param line
	*/
	public void extractLine(Line line) {
		int w = line.getwEnd();
		int h = line.gethEnd();
		
		for (int error = 1; error < horizontalDiscontinuity; error++) {
			boolean flag = false;
			for(int k = h - verticalDiscontinuity; k <= h + verticalDiscontinuity; k++){
				if (getIntegral(w+error, k) < 0){
					flag = true;
					line.extend(w+error, k);
					extractLine(line);
					integral[w+error][k] *= -1;
				}
			}
			if (flag)
				return;
		}
		
	}
	
	/**
	* For size = 1, it applies a kernel:
	*  1  1  1  1  1  1  1  1
	* -1 -1 -1 -1 -1 -1 -1 -1
	* -1 -1 -1 -1 -1 -1 -1 -1
	*  1  1  1  1  1  1  1  1
	* @param x
	* @param y
	* @param size
	* @return result of kernel
	*/
	public double kernel(int x, int y, int size) {
		return
			+ getIntegral(x + 4 * size, y + 2 * size)
			- getIntegral(x + 4 * size, y - 2 * size)
			- getIntegral(x - 4 * size, y + 2 * size)
			+ getIntegral(x - 4 * size, y - 2 * size)
			- 3 * (
			+ getIntegral(x + 4 * size, y + size)
			- getIntegral(x + 4 * size, y - size)
			- getIntegral(x - 4 * size, y + size)
			+ getIntegral(x - 4 * size, y - size)
			);
	}
	
	/**
	* 
	* @param x
	* @param y
	* @return integral[x][y] for valid coordinates, or 0 for invalid coordinates
	*/
	public double getIntegral(int x, int y) {
		if (x < 0 || x >= integral.length)
			return 0;
		if (y < 0 || y >= integral[0].length)
			return 0;
		return integral[x][y];
	}
	
}
