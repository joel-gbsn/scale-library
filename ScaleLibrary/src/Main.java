import datamanagement.Reader;
import datamanagement.Writer;
import processor.Processor;
import ui.UserInterface;

public class Main {
	
	public static void main(String[] args) {
		String baseFilename = "scales.txt";
		String customFilename = "custom.txt";
		
		Reader reader = new Reader(baseFilename, customFilename);
		Writer writer = new Writer(customFilename);
		Processor processor = new Processor(reader, writer);
		UserInterface ui = new UserInterface(processor);
		
		ui.start();
	}
}
