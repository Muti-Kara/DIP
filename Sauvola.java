package image;

/**
* Sauvola
*/
public class Sauvola {
	static final double CLF_RED = 0.299;
	static final double CLF_GREEN = 0.587;
	static final double CLF_BLUE = 0.114;
	
	static final double MAX_STADNARD_DEVIATION = 128;
	static final double SAUVOLA_PARAMETER = 0.5;
	static final int WINDOW_SIZE = 19;
	
	
	/**
	* 
	* @param p
	* @return grayscaled version of p
	*/
	public static void convertRGBtoGrayScale(GrayBuffer image) {
		for(int w = 0; w < image.getWidth(); w++){
			for(int h = 0; h < image.getHeight(); h++){
				image.set(w, h, makeGray(image.getBuffer().getRGB(w, h)));
			}
		}
	}
	
	/**
	* 
	* @param pixel's RGB
	* @return gray value for pixel
	*/
	public static int makeGray(int p) {
		return (int) ((p >> 16 & 0xff) * CLF_RED + (p >> 8 & 0xff) * CLF_GREEN + (p & 0xff) * CLF_BLUE);
	}
	
	/**
	 * Binarizes this grayscale image
	 * */
	public static void binarize(GrayBuffer image) {
		boolean[][] record = new boolean[image.getWidth()][image.getHeight()];
		for(int w = WINDOW_SIZE/2; w < image.getWidth() - WINDOW_SIZE/2; w++){
			for(int h = WINDOW_SIZE/2; h < image.getHeight() - WINDOW_SIZE/2; h++){
				int[] histogram = findHistogram(image, w, h);
				double mean = findMean(histogram);
				double standartDeviation = findStandartDeviation(histogram, mean);
				double threshold = mean * (1 + SAUVOLA_PARAMETER * ((standartDeviation/MAX_STADNARD_DEVIATION) - 1));
				record[w][h] = image.get(w, h) > threshold;
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
		double avg = mean;
		double standartDeviation = 0;
		for(int i = 0; i < 255; i++) {
			standartDeviation += Math.pow(histogram[i] - avg, 2.0);
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
	public static int[] findHistogram(GrayBuffer image, int x, int y) {
		int[] histogram = new int[256];
		for(int w = x - WINDOW_SIZE/2; w < x + WINDOW_SIZE/2; w++){
			for(int h = y - WINDOW_SIZE/2; h < y + WINDOW_SIZE/2; h++){
				histogram[image.get(w, h)]++;
			}
		}
		return histogram;
	}
	
}
