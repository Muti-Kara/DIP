package dip.data;

import java.util.ArrayList;

/**
 * Simple data structure to hold two Integer ArrayList.
 * @author Muti Kara
*/
public class Line {

	ArrayList<Integer> w = new ArrayList<>();
	ArrayList<Integer> h = new ArrayList<>();
	
	int wEnd = -1;
	int hEnd = -1;
	int wBegin = -1;
	int hBegin = -1;
	
	public Line(int w, int h) {
		wBegin = w;
		wEnd = h;
		extend(w, h);
	}
	
	public void extend(int w, int h) {
		this.wEnd = w;
		this.hEnd = h;
		
		this.w.add(w);
		this.h.add(h);
	}
	
	public int getwEnd() {
		return wEnd;
	}

	public int gethEnd() {
		return hEnd;
	}

	public int getwBegin() {
		return wBegin;
	}

	public int gethBegin() {
		return hBegin;
	}
	
	public int getSize() {
		return w.size();
	}
	
	public int getW(int index) {
		return w.get(index);
	}
	
	public int getH(int index) {
		return h.get(index);
	}
	
}
