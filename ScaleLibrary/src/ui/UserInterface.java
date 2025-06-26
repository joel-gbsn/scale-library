package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import processor.Processor;
import util.Interval;
import util.Note;

public class UserInterface {
	
	private Processor processor;
	private Scanner scanner;
	
	public UserInterface(Processor processor) {
		this.processor = processor;
		this.scanner = new Scanner(System.in);
	}
	
	public void start() {
		System.out.println("Welcome to the Scale Library!\n");
		
		int option;
		while (true) {
			printMainMenu();
			option = getOption(4);
			
			if (option == 0) {
				break;
				
			} else if (option == 1) {
				processor.useScaleSet("base");
				selectScale();
				
			} else if (option == 2) {
				processor.useScaleSet("custom");
				selectScale();
				
			} else if (option == 3) {
				createCustomScale();
				
			} else if (option == 4) {
				processor.useScaleSet("custom");
				removeCustomScale();
			}
		}
		
		System.out.println("Scale library closed.");
		scanner.close();
	}
	
	public void selectScale() {
		String scaleSetName;
		if ("base".equals(processor.getCurrScaleSet())) {
			scaleSetName = "scales";
		} else {
			scaleSetName = "custom scales";
		}

		printHeading("Search " + scaleSetName);

		List<String> scales = processor.getScaleNames();
		if (scales.isEmpty()) {
			System.out.println("No " + scaleSetName + " found.\n");
			return;
		}

		for (int i = 0; i < scales.size(); i++) {
			System.out.println((i + 1) + ". " + scales.get(i));
		}
		System.out.println();
		
		int option = getOption(scales.size());
		if (option == 0) {
			return;
		}

		processor.setScale(option - 1);
		searchScale();
	}
	
	public void searchScale() {
		String scaleName = processor.getScaleName();
		
		printHeading("Search " + scaleName + " scales");
		
		System.out.println("Interval pattern: " + processor.getIntervalString());
		System.out.println();
		
		String input = "";
		while (true) {
			System.out.print("Enter a root note, or 'q' to quit: ");
			input = getUserInput();
			
			if ("q".equals(input.toLowerCase())) {
				System.out.println();
				break;
			}
			
			Note root = Note.getNote(input);
			if (root == null) {
				System.out.println("Invalid note name.\n");
				continue;
			}
			
			List<String> notes = processor.getNoteSequence(root);
			if (notes == null) {
				System.out.println("Scale not found.\n");
				continue;
			}
			
			System.out.println();
			System.out.println(root + " " + scaleName + " scale:");
			printScale(notes);
		}
	}
	
	private void printScale(List<String> scale) {
		for (String note : scale.subList(0, scale.size() - 1)) {
			System.out.print(note);
			
			// print blank spaces to pad
			for (int i = 0; i < 5 - note.length(); i++) {
				System.out.print(" ");
			}
			
		}
		
		// print the final note
		System.out.println(scale.get(scale.size() - 1));
		System.out.println();
	}
	
	private String getUserInput() {
		String input = "";
		while (input.isEmpty()) {
			input = scanner.nextLine().trim();
		}
		return input;
	}
	
	/**
	 * Prompts the user to enter an option number up to the given number.
	 * @param numOptions the maximum number accepted as input
	 * @return the user's input cast to an integer
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
	
	private void printHeading(String heading) {
		String bar = "";
		for (int i = 0; i < heading.length() + 4; i++) {
			bar = bar + "-";
		}
		System.out.println(bar);
		System.out.println("  " + heading);
		System.out.println(bar);
	}
	
	private void printMainMenu() {
		printHeading("Main Menu");
		System.out.println("1. Search scales.");
		System.out.println("2. Search custom scales.");
		System.out.println("3. Create custom scale.");
		System.out.println("4. Delete custom scale.");
		System.out.println();
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
		List<String> scales = processor.getScaleNames();

		if (scales.isEmpty()) {
			System.out.println("No custom scales found.\n");
			return;
		}

		// display all custom scales
		for (int i = 0; i < scales.size(); i++) {
			System.out.println((i + 1) + ". " + scales.get(i));
		}
		System.out.println();

		// prompt user to enter an option number
		int option = getOption(scales.size());
		if (option == 0) {
			return;
		}

		// remove the scale from the library
		processor.deleteCustomScale(option - 1);
		System.out.println("Scale successfully deleted.\n");
	}
}
