package image.operation;

import image.data.MyImage;

/**
* Sauvola
* @author Muti Kara
*/
public class Sauvola {
	static final double MAX_STADNARD_DEVIATION = 128;
	static final double SAUVOLA_PARAMETER = 0.38;
	static final int WINDOW_SIZE = 21;
	
	/**
	 * Binarizes this grayscale image
	 * */
	public static void binarize(MyImage image) {
		boolean[][] record = new boolean[image.getWidth()][image.getHeight()];
		for(int w = WINDOW_SIZE/2; w < image.getWidth() - WINDOW_SIZE/2; w++){
			for(int h = WINDOW_SIZE/2; h < image.getHeight() - WINDOW_SIZE/2; h++){
				int[] histogram = findHistogram(image, w, h);
				double mean = findMean(histogram);
				double standartDeviation = findStandartDeviation(histogram, mean);
				double threshold = mean * (1 + SAUVOLA_PARAMETER * ((standartDeviation/MAX_STADNARD_DEVIATION) - 1));
				record[w][h] = image.get(w, h) > threshold;
				if(w%500 == 0 && h%800 == 0) {
					System.out.println("The location (" + w + ", " + h + ") have binarized.");
				}
			}
		}
		for(int w = 0; w < image.getWidth(); w++){
			for(int h = 0; h < image.getHeight(); h++){
				if(record[w][h]) {
					image.set(w, h, 255);
				} else {
					image.set(w, h, 0);
				}
			}
		}
	}
	
	/**
	* 
	* @param histogram
	* @return standart deviation of given histogram
	*/
	public static double findStandartDeviation(int[] histogram, double mean) {
		double standartDeviation = 0;
		for(int i = 0; i < 255; i++) {
			standartDeviation += Math.pow(histogram[i] - mean, 2.0);
		}
		return Math.sqrt(standartDeviation / (WINDOW_SIZE*WINDOW_SIZE));
	}
	
	/**
	* 
	* @param histogram
	* @param width
	* @param height
	* @return mean value of given histogram
	*/
	public static double findMean(int[] histogram) {
		double avg = 0;
		for(int i = 0; i < 255; i++) {
			avg += i * histogram[i];
		}
		return avg / (WINDOW_SIZE*WINDOW_SIZE);
	}
	
	/**
	* 
	* @param image
	* @param x
	* @param y
	* @return histogram of x and y centered window
	*/
	public static int[] findHistogram(MyImage image, int x, int y) {
		int[] histogram = new int[256];
		for(int w = x - WINDOW_SIZE/2; w < x + WINDOW_SIZE/2; w++){
			for(int h = y - WINDOW_SIZE/2; h < y + WINDOW_SIZE/2; h++){
				histogram[image.get(w, h)]++;
			}
		}
		return histogram;
	}
	
}
