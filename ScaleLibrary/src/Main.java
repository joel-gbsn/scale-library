import datamanagement.Reader;
import datamanagement.Writer;
import processor.Processor;
import ui.UserInterface;

/**
 * The entry point of the program.
 * @author Joel Gibson
 */
public class Main {
	
	public static void main(String[] args) {
		// the input file names
		String baseFilename = "scales.txt";
		String customFilename = "custom.txt";
		
		// create the class dependencies
		Reader reader = new Reader(baseFilename, customFilename);
		Writer writer = new Writer(customFilename);
		Processor processor = new Processor(reader, writer);
		UserInterface ui = new UserInterface(processor);
		
		ui.start();
	}
}
