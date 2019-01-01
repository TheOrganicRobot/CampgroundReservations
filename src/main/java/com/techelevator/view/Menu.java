package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Menu {

	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		int selectedOption = 0;
		try {
			selectedOption = Integer.valueOf(userInput);

			if (selectedOption <= options.length && selectedOption > 0) {
				choice = options[selectedOption - 1];
			}
			if (choice == null || selectedOption < 1 && selectedOption > options.length) {
				out.println("\n*** " + userInput + " is not a valid option ***\n");
			}
		} catch (NumberFormatException e) {
			out.println("\n*** " + userInput + " is not a valid option ***\n");
		}

		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}
}
