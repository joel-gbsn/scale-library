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
}
