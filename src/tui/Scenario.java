package tui;

import db.DataAccessException;
import db.DBConnection;
import dk.raptus.KeyboardReader; // Provided by KeyboardReader.jar (in /lib)
import java.util.List;

public class Scenario {
	private KeyboardReader kr;

	public Scenario() {
		kr = KeyboardReader.getInstance();
	}
	
	private void placeOrder()
	{
		
	}

	private void showSales()
	{
		
	}
	
	private int showMenu() {
		System.out.println("\nSALES HANDLING");
		System.out.println(" 1) Place order");
		System.out.println(" 2) Show sales");
		System.out.println(" 0) Quit program!");
		return kr.readInt("Make a choice: ", "You must type in an integer: ");
	}

	private void run() {
		int choice = -1;
		while (choice != 0) {
			choice = showMenu();
			switch (choice) {
				case 0:
					break;
				case 1:
					placeOrder();
					break;
				case 2:
					showSales();
					break;
				default:
					System.out.println("Wrong choice - let's try again!");
			}
		}
		DBConnection.getInstance().close();
		kr.close();
	}

	public static void main(String args[]) {
		new Scenario().run();
	}
}
