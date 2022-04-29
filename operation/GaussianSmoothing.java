package image.operation;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import image.data.MyImage;

/**
* GaussianSmoothing
* @author Muti Kara
*/
public class GaussianSmoothing {
	
	static float[] data;
	
	static Kernel kernel;
	static ConvolveOp filter;
	
	public static MyImage smooth(MyImage input) {
		BufferedImage output = filter.filter(input.getBuffer(), null);
		return new MyImage(output);
	}
	
	public static float[] prepareData(int radius) {
		data = new float[(2*radius + 1) * (2*radius + 1)];
		float sum = 0;
		for(int i = 0; i <= 2*radius; i++) {
			for(int j = 0; j <= 2*radius; j++) {
				int index = i * (2*radius + 1) + j;
				data[index] = (float) (gaussian(i, radius, radius/2) * gaussian(j, radius, radius/2));
				sum += data[index];
			}
		}
		
		for(int i = 0; i < data.length; i++) {
			data[i] /= sum;
		}
		
		return data;
	}
	
	public static double gaussian(double x, double mu, double sigma) {
		double a = (x - mu) / sigma;
		return Math.exp(-0.5 * a * a);
	}
	
	public static void prepareKernel(int size) {
		kernel = new Kernel(size, size, prepareData(size/2));
		filter = new ConvolveOp(kernel);
	}
	
}
