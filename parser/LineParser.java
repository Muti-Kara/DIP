package image.parser;

import java.util.ArrayList;

import image.GrayBuffer;
import image.Line;

/**
* LineParser
*/
public class LineParser extends Parser {
	final int[] WINDOW_SIZES = new int[]{25, 24, 23, 22, 21, 20, 19};
	final int SIMPLIFIER = 5;
	final int VERTICAL_DISCONTINUITY = 25;
	final int HORIZONTAL_DISCONTINUITY = 75;
	
	double[][] integral;
	double[][] filterResponse;
	int[][] usedFilterSize;
	
	ArrayList<Line> lines = new ArrayList<>();
	
	public LineParser(GrayBuffer buffer) {
		super(buffer);
		integral = new double[buffer.getWidth()][buffer.getHeight()];
		filterResponse = new double[buffer.getWidth()][buffer.getHeight()];
		usedFilterSize = new int[buffer.getWidth()][buffer.getHeight()];
	}
	
	
	public void parse() {
		integral = buffer.convertToDouble2D();
		buildIntegralImage();
		applyFilters();
		findCenters();
		extractLines();
	}
	
	public void findCenters() {
		for(int h = 2*WINDOW_SIZES[0]; h < buffer.getHeight() - 2*WINDOW_SIZES[0]; h++){
			for(int w = 4*WINDOW_SIZES[0]; w < buffer.getWidth() - 4*WINDOW_SIZES[0]; w++){
				boolean flag = true;
				for(int k = h - usedFilterSize[w][h]; k <= h + usedFilterSize[w][h]; k++){
					if(h != k && filterResponse[w][h] <= filterResponse[w][k]) {
						flag = false;
						break;
					}
				}
				if(flag) {
					buffer.mark(w, h);;
					integral[w][h] *= -1;
				}
			}
		}
		System.out.println("Centers have been found");
	}
	
	public void extractLines() {
		for(int w = 0; w < buffer.getWidth(); w++) {
			for (int h = 0; h < buffer.getHeight(); h++) {
				if(integral[w][h] < 0){
					Line line = new Line(w, h);
					extractLine(line);
					lines.add(line);
				}
			}
		}
	}
	
	public void extractLine(Line line) {
		int w = line.getwEnd();
		int h = line.gethEnd();
		
		for (int error = 1; error < HORIZONTAL_DISCONTINUITY; error++) {
			boolean flag = false;
			for(int k = h - VERTICAL_DISCONTINUITY; k <= h + VERTICAL_DISCONTINUITY; k++){
				if (integral[w+error][k] < 0){
					flag = true;
					line.extend(w+error, k);
					extractLine(line);
					buffer.unMark(w+error, k);
					integral[w+error][k] *= -1;
				}
			}
			if (flag)
				return;
		}
		
	}
	
	/**
	 * Builds integral image of given GrayBuffer
	 * */
	public void buildIntegralImage() {
		System.out.println("Integral image is building");
		for(int w = 0; w < buffer.getWidth(); w++) {
			for(int h = 0; h < buffer.getHeight(); h++) {
				if(w == 0 && h == 0) {
					integral[w][h] = buffer.get(w, h) / SIMPLIFIER;
				} else if (w == 0) {
					integral[w][h] = integral[w][h - 1] + buffer.get(w, h) / SIMPLIFIER;
				} else if (h == 0) {
					integral[w][h] = integral[w - 1][h] + buffer.get(w, h) / SIMPLIFIER;
				} else {
					integral[w][h] = integral[w - 1][h] + integral[w][h - 1] + buffer.get(w, h) / SIMPLIFIER - integral[w - 1][h - 1];
				}
			}
		}
		double sum = 0;
		for(int w = 0; w < buffer.getWidth(); w++)
			for(int h = 0; h < buffer.getHeight(); h++)
				sum += integral[w][h];
		System.out.println(sum);
	}
	
	/**
	 * Applies the kernels with sizes in WINDOW_SIZES
	 * Puts the maximum values in filterResponse array
	 * Puts the size used for maximum response usedFilterSize array
	 * */
	public void applyFilters() {
		for(int w = 4*WINDOW_SIZES[0]; w < buffer.getWidth() - 4*WINDOW_SIZES[0]; w++){
			for(int h = 2*WINDOW_SIZES[0]; h < buffer.getHeight() - 2*WINDOW_SIZES[0]; h++){
				for(int size : WINDOW_SIZES) {
					double sizeResponse = kernel(w, h, size);
					if (filterResponse[w][h] > sizeResponse) {
						filterResponse[w][h] = sizeResponse;
						usedFilterSize[w][h] = size;
					}
				}
			}
		}
		System.out.println("Line segmentation filters have applied.");
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
			+ integral[x + 4 * size][y + 2 * size]
			- integral[x + 4 * size][y - 2 * size]
			- integral[x - 4 * size][y + 2 * size]
			+ integral[x - 4 * size][y - 2 * size]
			- 3 * (
			+ integral[x + 4 * size][y + size]
			- integral[x + 4 * size][y - size]
			- integral[x - 4 * size][y + size]
			+ integral[x - 4 * size][y - size]
			);
	}
	
	public int getSize() {
		return lines.size();
	}
	
	public Line getLine(int index) {
		return lines.get(index);
	}
	
}
