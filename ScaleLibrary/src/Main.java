import datamanagement.Reader;
import processor.Processor;
import ui.UserInterface;

public class Main {
	
	public static void main(String[] args) {
		String baseFilename = "scales.txt";
		String customFilename = "custom.txt";
		
		Reader reader = new Reader(baseFilename, customFilename);
		Processor processor = new Processor(reader);
		UserInterface ui = new UserInterface(processor);
		
		ui.start();
	}
}
