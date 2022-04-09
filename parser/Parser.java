package image.parser;

import image.GrayBuffer;

/**
* Parser
*/
public abstract class Parser {
	
	GrayBuffer buffer;
	
	public Parser(GrayBuffer buffer) {
		this.buffer= buffer;
	}
	
	abstract void parse();
	
}
