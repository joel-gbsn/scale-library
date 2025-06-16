package scales;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import files.*;
import notes.*;

/**
 * Represents a scale library system where users can view musical scales and create their own.
 * @author Joel Gibson
 */
public class ScaleLibrary {
	
	/**
	 * The complete catalogue of scales in the library. This maps scale formats to the list of all scale
	 * collections available for that format.
	 */
	private Map<String, ArrayList<ScaleCollection>> library;
	
	/**
	 * The scanner object for getting user input.
	 */
	private Scanner scanner = new Scanner(System.in);
	
	/**
	 * Gets the full catalogue of scales in the library.
	 * @return the map of scale collection lists
	 */
	private Map<String, ArrayList<ScaleCollection>> getLibrary() {
		return library;
	}
	
	/**
	 * Prompts the user to select a scale type filtered by the given format.
	 * @param format the format of the scales to filter by
	 */
	private void searchScales(String format) {
		// get the list of scale collections with the given format
		ArrayList<ScaleCollection> scalesList = getLibrary().get(format);
		
		// check if any scales are available
		if (scalesList.isEmpty()) {
			System.out.println("No " + format + " found.\n");
			return;
		}
		
		// prompt user to select an option number corresponding to a scale type
		int option = -1;
		while (true) {
			printMenuHeading("Search " + format);
			
			// print all scale types
			for (int i = 0; i < scalesList.size(); i++) {
				System.out.println((i + 1) + ". " + scalesList.get(i));
			}
			
			System.out.println();
			
			// get input from the user
			option = getOption(scalesList.size());
			
			// check if user wants to quit
			if (option == 0) {
				return;
			}
			
			// prompt user for specific scales to display
			selectScalesToPrint(scalesList.get(option - 1));
		}
	}
	
	/**
	 * Prompts the user to select specific scales from the given collection and prints their note content.
	 * Users can also select to view the interval pattern associated with this scale type
	 * @param scales the scale collection to prompt user with
	 */
	private void selectScalesToPrint(ScaleCollection scales) {
		// print instructions for how user can select a scale to display
		printMenuHeading("Search " + scales + "s");
		System.out.println("Enter a root note (e.g. Bb) to view the scale, or type 'pattern'");
		System.out.println("to view the interval pattern for this scale type.\n");
		
		// get input from user and print the associated scales
		String input = "";
		while (true) {
			// prompt user to enter an option
			System.out.print("Enter an option, or 0 to quit: ");
			input = getUserInput().toLowerCase();
			
			// check if user want to quit
			if ("0".equals(input)) {
				System.out.println();
				return;
			}
			
			// print the sequence of intervals for this scale type
			if ("pattern".equals(input)) {
				System.out.println();
				scales.printIntervals();
				continue;
			}
			
			// get the requested scale
			Scale scale = scales.getAllScales().get(input);
			
			// print the scale, if available
			if (scale == null) {
				System.out.println("Scale not found.\n");
			} else {
				System.out.println();
				scale.printNotes();
			}
		}
	}
	
	/**
	 * Prompts user to create a custom scale to store in the library.
	 */
	private void addCustomScale() {
		printMenuHeading("Add custom scale");
		
		// prompt user for scale format
		String input = "";
		System.out.print("Enter the scale format (e.g. scale, arpeggio), or 0 to quit: ");
		input = getUserInput();
		String format = input.toLowerCase();
		
		System.out.println();
		
		// check if user wants to quit
		if ("0".equals(input)) {
			return;
		}
		
		// prompt user for scale type
		System.out.print("Enter the scale name/type (e.g. major, minor), or 0 to quit: ");
		input = getUserInput();
        String type = input.toLowerCase();
        
        System.out.println();
		
        // check if user wants to quit
		if ("0".equals(input)) {
			return;
		}
		
		// prompt user for interval sequence
		String[] intervals;
		while (true) {
			System.out.print("Enter the interval sequence (e.g. 1, 2, b3, 5, 6, 8), or 0 to quit: ");
			input = getUserInput();
			
			// check if user wants to quit
			if ("0".equals(input)) {
				System.out.println();
				return;
			}
			
			// check if interval sequence is valid
			intervals = input.split(",\\s*");
			if (isValid(intervals)) {
				System.out.println();
				break;
			} else {
				System.out.println("Invalid interval sequence.\n");
			}
		}
		
		// prompt user for whether scale should be simplified using enharmonics
		boolean simplify = false;
		while (true) {
			System.out.print("Should the scale be simplified using enharmonics (y/n)? ");
			input = getUserInput().toLowerCase();
			
			// check if user wants to quit
			if ("0".equals(input)) {
				System.out.println();
				return;
			}
			
			// check if user entered a valid response ('yes' or 'no')
			if ("y".equals(input) || "yes".equals(input)) {
				simplify = true;
				break;
			} else if ("n".equals(input) || "no".equals(input)) {
				break;
			} else {
				System.out.println("Invalid option.\n");
			}
		}
		
		System.out.println();
		
		// create the new scale collection using the user's input
		ScaleCollection scales = new ScaleCollection(format, type, intervals, simplify);
		scales.createAllScales();
		getLibrary().get("custom scales").add(scales);
		
		// save the new scale to file
		writeCustomScales("custom.txt");
		System.out.println("New " + format + " successfully added.\n");
	}
	
	/**
	 * Prompts the user to remove custom scales from the library.
	 */
	private void removeCustomScale() {
		// get the list of current custom scales
        ArrayList<ScaleCollection> scalesList = getLibrary().get("custom scales");
		
        // check if any custom scales were found
		if (scalesList.isEmpty()) {
			System.out.println("No custom scales found.\n");
			return;
		}
		
		// prompt user for custom scales to remove
		int option = -1;
		while (true) {
			// check if there are any more custom scales to remove
			if (scalesList.isEmpty()) {
				System.out.println("All custom scales have now been removed.\n");
				return;
			}
			
			// display all custom scales
			printMenuHeading("Remove custom scales");
			for (int i = 0; i < scalesList.size(); i++) {
				System.out.println((i + 1) + ". " + scalesList.get(i));
			}
			
			System.out.println();
			
			// prompt user to enter an option number
			option = getOption(scalesList.size());
			
			// check if user wants to quit
			if (option == 0) {
				return;
			}
			
			// remove the scale from the library
			scalesList.remove(option - 1);
			writeCustomScales("custom.txt");
			System.out.println("Scale successfully removed.\n");
		}
	}
	
	/**
	 * Checks if the given array of interval names is valid.
	 * @param intervals the array of interval names to check
	 * @return true if all intervals are valid, otherwise false
	 */
	private boolean isValid(String[] intervals) {
		// try to create an interval from each interval name
		for (String interval : intervals) {
			if (Interval.getInterval(interval) == null) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Writes the current list of custom scales to the given file.
	 * @param filename the file to write to
	 */
	private void writeCustomScales(String filename) {
		// get the current list of custom scales
		ArrayList<ScaleCollection> scalesList = getLibrary().get("custom scales");
		
		// prepare the file for writing
		File file = new File(filename);
		FileWriter fw = null;
		PrintWriter pw = null;
		
		try {
			// create the file writers
			fw = new FileWriter(file, false);
			pw = new PrintWriter(fw);
			
			// write each custom scale to the file
			for (ScaleCollection scales : scalesList) {
				pw.println(scales.toFileLine());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			try {
				// close the file writers
				pw.flush();
				fw.close();
				pw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Loads all scales from the scale files into the library.
	 */
	private void loadAllScales() {
		// create a new map to store the lists of scale collections
		library = new HashMap<String, ArrayList<ScaleCollection>>();
		
		// read the scale files and create each scale collection
		library.put("scales", FileLoader.loadFileData("scales.txt"));
		library.put("modes", FileLoader.loadFileData("modes.txt"));
		library.put("arpeggios", FileLoader.loadFileData("arpeggios.txt"));
		library.put("custom scales", FileLoader.loadFileData("custom.txt"));
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to the scale library!\n");
		
		// create a new scale library
		ScaleLibrary librarySystem = new ScaleLibrary();
		
		// create all scale components
		Accidental.createAllAccidentals();
		Note.createAllNotes();
		Interval.createAllIntervals();
		
		// create all scale collections from the scale files
		librarySystem.loadAllScales();
		
		// get options to interact with user
		int option;
		while (true) {
			// prompt user for an option
			librarySystem.printMainOptions();
			option = librarySystem.getOption(6);
			
			if (option == 0) {
				// exit the system
				break;
				
			} else if (option == 1) {
				// look up scales
				librarySystem.searchScales("scales");
				
			} else if (option == 2) {
				// look up modes
				librarySystem.searchScales("modes");
				
			} else if (option == 3) {
				// look up arpeggios
				librarySystem.searchScales("arpeggios");
				
			} else if (option == 4) {
				// look up custom scales
				librarySystem.searchScales("custom scales");
				
			} else if (option == 5) {
				// add custom scales
				librarySystem.addCustomScale();
				
			} else if (option == 6) {
				// remove custom scales
				librarySystem.removeCustomScale();
			}
		}
		
		// close the library
		System.out.println("System closed.");
		librarySystem.scanner.close();
	}
}
