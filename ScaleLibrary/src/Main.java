import datamanagement.Reader;
import processor.Processor;
import ui.UserInterface;

public class Main {
	
	public static void main(String[] args) {
		String filename = "ScaleLibrary/src/scales.txt";
		Reader reader = new Reader(filename);
		Processor processor = new Processor(reader);
		UserInterface ui = new UserInterface(processor);
		ui.start();
	}
}
