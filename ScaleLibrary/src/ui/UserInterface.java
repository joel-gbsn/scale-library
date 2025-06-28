package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import processor.Processor;
import util.Interval;
import util.Note;

/**
 * The user interface for interacting with the Scale Library.
 * @author Joel Gibson
 */
public class UserInterface {
	
	/**
	 * The processor for processing and retrieving data to display.
	 */
	private Processor processor;
	
	/**
	 * The scanner for getting user input.
	 */
	private Scanner scanner;
	
	public UserInterface(Processor processor) {
		this.processor = processor;
		this.scanner = new Scanner(System.in);
	}
	
	/**
	 * Prompts the user to enter some non-empty text.
	 * @return the user's input, trimmed of whitespace
	 */
	private String getUserInput() {
		String input = "";
		while (input.isEmpty()) {
			input = scanner.nextLine().trim();
		}
		return input;
	}
	
	/**
	 * Prompts the user to enter an option number between 1 and the given number.
	 * @param numOptions the maximum number accepted as user input
	 * @return the user's input cast to an integer, or 0 if they selected to quit
	 */
	private int getOption(int numOptions) {
		// keep prompting user until they enter an option number within range
		int option;
		while (true) {
			// prompt user to enter a number
			System.out.print("Enter an option number, or 'q' to quit: ");
			
			String input = getUserInput();
			if ("q".equals(input.toLowerCase())) {
				System.out.println();
				return 0;
			}
			
			// cast input to an integer
			try {
				option = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("Invalid option.\n");
				continue;
			}
			
			// check if input number is within the allowable range
			if (option < 1 || option > numOptions) {
				System.out.println("Invalid option.\n");
			} else {
				System.out.println();
				break;
			}
		}
		
		return option;
	}
	
	/**
	 * The entry point for the user interface.
	 */
	public void start() {
		System.out.println("Welcome to the Scale Library!\n");
		
		// execute the main menu actions requested by the user
		int option;
		while (true) {
			printMainMenu();
			option = getOption(4);
			
			if (option == 0) {
				break;
			} else if (option == 1) {
				searchScales("base");
			} else if (option == 2) {
				searchScales("custom");
			} else if (option == 3) {
				createCustomScale();
			} else if (option == 4) {
				removeCustomScale();
			}
		}
		
		System.out.println("Scale library closed.");
		scanner.close();
	}
	
	/**
	 * Displays the scales selected by the user from the given scale set
	 * @param scaleSet the scale set to search (either "base" or "custom")
	 */
	public void searchScales(String scaleSet) {
		if ("base".equals(scaleSet)) {
			printHeading("Search scales");
		} else {
			printHeading("Search custom scales");
		}
		
		// get the list of scale names
		List<String> scales = processor.getScaleNames(scaleSet);
		if (scales.isEmpty()) {
			System.out.println("No scales found.\n");
			return;
		}
		
		// find which scale the user wants to search
		int index = chooseScale(scales);
		if (index == -1) {
			return;
		}
		
		printHeading("Search " + scales.get(index) + " scales");
		System.out.println("Interval pattern: " + processor.getIntervals(scaleSet, index) + "\n");
		
		// display the scale of the root note entered by the user
		while (true) {
			Note root = chooseRoot();
			if (root == null) {
				return;
			}
			
			List<String> notes = processor.getScaleNotes(scaleSet, index, root);
			if (notes == null) {
				System.out.println("Scale not found.\n");
				continue;
			}
			
			System.out.println();
			System.out.println(root + " " + scales.get(index) + " scale:");
			printScale(notes);
		}
	}
	
	/**
	 * Prompts the user to enter a root note.
	 * @return the note entered by the user, or null if they selected to quit
	 */
	public Note chooseRoot() {
		String input = "";
		Note root;
		while (true) {
			System.out.print("Enter a root note, or 'q' to quit: ");
			input = getUserInput();
			
			if ("q".equals(input.toLowerCase())) {
				System.out.println();
				return null;
			}
			
			root = Note.getNote(input);
			
			// check if note is valid
			if (root == null) {
				System.out.println("Invalid note name.\n");
			} else if (Math.abs(root.accidental.getSemitoneChange()) > 1) {
				System.out.println("Invalid root (maximum one sharp or flat only).\n");
			} else {
				break;
			}
		}
		
		return root;
	}
	
	/**
	 * Prompts the user to select a scale from the given list of scale names.
	 * @param scales the list of scale names
	 * @return the index of the selected scale, or -1 if user selected to quit
	 */
	public int chooseScale(List<String> scales) {
		// print each scale name as a numbered list
		for (int i = 0; i < scales.size(); i++) {
			System.out.println((i + 1) + ". " + scales.get(i));
		}
		System.out.println();
		
		return getOption(scales.size()) - 1;
	}
	
	/**
	 * Prints the given list of notes.
	 * @param scale the list of notes
	 */
	private void printScale(List<String> scale) {
		for (String note : scale.subList(0, scale.size() - 1)) {
			System.out.print(note);
			
			// pad to 5 spaces
			for (int i = 0; i < 5 - note.length(); i++) {
				System.out.print(" ");
			}
		}
		
		// print the final note
		System.out.println(scale.get(scale.size() - 1) + "\n");
	}
	
	/**
	 * Prompts user to create a custom scale to store in the library.
	 */
	private void createCustomScale() {
		printHeading("Add custom scale");
		
		// prompt user for scale type
		String input = "";
		System.out.print("Enter the scale name (e.g. major), or 'q' to quit: ");
		input = getUserInput();
        String name = input.toLowerCase();
        System.out.println();
		
		if ("q".equals(input)) {
			return;
		}
		
		// prompt user for interval sequence
		List<Interval> intervals = new ArrayList<>();
		while (true) {
			System.out.print("Enter the interval sequence (e.g. 1, 2, b3, 5, 6, 8), or q to quit: ");
			
			input = getUserInput();
			if ("q".equals(input.toLowerCase())) {
				System.out.println();
				return;
			}
			
			// check if interval sequence is valid
			String[] intervalList = input.split(",\\s*");
			for (String interval : intervalList) {
				intervals.add(Interval.getInterval(interval));
			}
			
			if (intervals.contains(null)) {
				System.out.println("Invalid interval list.\n");
			}
			break;
		}
		System.out.println();
		
		// prompt user for whether scale should be simplified using enharmonics
		boolean simplify = false;
		while (true) {
			System.out.print("Should the scale be simplified using enharmonics (y/n)? Or type 'q' to quit. ");
			
			input = getUserInput().toLowerCase();
			if ("q".equals(input)) {
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
		
		processor.addCustomScale(name, intervals, simplify);
		System.out.println("Scale successfully added.\n");
	}
	
	/**
	 * Prompts the user to remove custom scales from the library.
	 */
	private void removeCustomScale() {
		printHeading("Delete custom scale");
		
		// get the list of current custom scales
		List<String> scales = processor.getScaleNames("custom");
		if (scales.isEmpty()) {
			System.out.println("No scales found.\n");
			return;
		}

		// find which scale the user wants to search
		int index = chooseScale(scales);
		if (index == -1) {
			return;
		}

		// remove the scale
		processor.deleteCustomScale(index);
		System.out.println("Scale successfully deleted.\n");
	}
	
	/**
	 * Displays the given text as a heading.
	 * @param heading the heading to print
	 */
	private void printHeading(String heading) {
		// get the correct length heading bar
		String bar = "";
		for (int i = 0; i < heading.length() + 4; i++) {
			bar = bar + "-";
		}
		
		System.out.println(bar);
		System.out.println("  " + heading);
		System.out.println(bar);
	}
	
	/**
	 * Displays the main menu to the user.
	 */
	private void printMainMenu() {
		printHeading("Main Menu");
		System.out.println("1. Search scales.");
		System.out.println("2. Search custom scales.");
		System.out.println("3. Create custom scale.");
		System.out.println("4. Delete custom scale.\n");
	}
}
