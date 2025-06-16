package ui;

import java.util.Scanner;

import processor.Processor;

public class UserInterface {
	
	private Processor processor;
	private Scanner scanner;
	
	public UserInterface(Processor processor) {
		this.processor = processor;
		this.scanner = new Scanner(System.in);
	}
	
	public void start() {
		System.out.println("Welcome to the Scale Library!\n");
		
		int option = -1;
		while (option != 0) {
			printMainMenu();
			option = getOption(3);
		}
		
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
		int option = -1;
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
		System.out.println("2. Search arpeggios.");
		System.out.println("3. Extra feature.");
	}

}
