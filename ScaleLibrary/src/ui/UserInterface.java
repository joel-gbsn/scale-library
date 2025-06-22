package ui;

import java.util.List;
import java.util.Scanner;

import processor.Processor;
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
				processor.setBaseScales();
				selectScale();
			} else if (option == 2) {
				processor.setCustomScales();
				selectScale();
			} else if (option == 3) {
				
			} else if (option == 4) {
				
			}
		}
		
		System.out.println("Scale library closed.");
		scanner.close();
	}
	
	public void selectScale() {
		printHeading("Search scales");
		List<String> scales = processor.getScaleNames();
		
		if (scales.isEmpty()) {
			System.out.println("No scales available.");
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
		
		System.out.println("Interval pattern:");
		printSequence(processor.getIntervalSequence());
		
		String input = "";
		while (true) {
			System.out.print("Enter a root note, or 'q' to quit: ");
			input = getUserInput();
			
			if ("q".equals(input.toLowerCase())) {
				break;
			}
			
			Note root = Note.getNote(input);
			if (root == null) {
				System.out.println("Invalid note name.\n");
				continue;
			}
			
			System.out.println(root + " " + scaleName + " scale:");
			printSequence(processor.getNoteSequence(root));
		}
	}
	
	private void printSequence(List<String> sequence) {
		for (String element : sequence.subList(0, sequence.size() - 1)) {
			System.out.print(element);
			
			// print blank spaces to pad
			for (int i = 0; i < 5 - element.length(); i++) {
				System.out.print(" ");
			}
			
		}
		
		// print the final note
		System.out.println(sequence.get(sequence.size() - 1));
		System.out.println();
	}
	
	public boolean isValidNote(String note) {
		return null != Note.getNote(note);
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
			System.out.print("Enter an option number, or 0 to quit: ");
			String input = getUserInput();
			
			// cast input to an integer
			try {
				option = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("Invalid option.\n");
				continue;
			}
			
			// check if input number is within the allowable range
			if (option < 0 || option > numOptions) {
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

}
