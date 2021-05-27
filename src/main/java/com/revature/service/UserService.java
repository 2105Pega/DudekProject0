package com.revature.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
//my import
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.revature.dao.UserDAOImpl;
//my import
import com.revature.model.Account;
import com.revature.model.Admin;
import com.revature.model.User;

//my import
import com.revature.dao.AdminDAOImpl;

public class UserService {

	private UserDAOImpl uDao = new UserDAOImpl();

	private AdminDAOImpl aDao = new AdminDAOImpl();

	boolean displayedAccounts = false;

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	public void depositAccount(User user) {

//		getListOfAccount(user);

		Scanner sc = new Scanner(System.in);
		System.out.println("Which account would you like to deposit to? (Enter Account #)");
		// this method has to be fixed
//		System.out.println("1. Checking\n2. Savings");
//		System.out.println("Type the number of account type");
		int accountId = sc.nextInt();
		System.out.println("Enter amount you would like to deposit ($)");
		double money = sc.nextDouble();

//		if (accountType == 1) {
		uDao.depositAccount(user, money, accountId);
		System.out.println("Would you like to make another deposit in this session? [Y/N]");
		String choice = sc.next();
		if (choice.equals("y") || choice.equals("Y")) {
			depositAccount(user);
		}
		showUserMenu(user);
//		}

//		else {
//			uDao.depositAccount(user, money, "Savings");
//			System.out.println("Would you like to make another deposit in this session? [y/n]");
//			String choice = sc.next();
//			if (choice.equals("y")) {
//				withdrawAccount(user);
//			}
//			showUserMenu(user);
//		}

	}

	public void createUser() {

		Scanner sc = new Scanner(System.in);

		System.out.println("Enter your first name");
		String fName = sc.next();
		System.out.println("Enter your last name");
		String lName = sc.next();
		System.out.println("Enter your username");
		String username = sc.next();
		System.out.println("Enter your password");
		String password = sc.next();
		System.out.println("Re-enter your password");
		String passwordVal = sc.next();

		if (password.equals(passwordVal)) {
			uDao.createUser(fName, lName, username, password);
		} else {
			while (!password.equals(passwordVal)) {
				System.out.println("\nYour passwords don't match. Please try again\n");
				System.out.println("Enter your password");
				password = sc.next();
				System.out.println("Re-enter your password");
				passwordVal = sc.next();
				
			}
			uDao.createUser(fName, lName, username, password);
			// adding this feature - bring user back to register user menu
			showMainMenu();

		}

	}

	public void withdrawAccount(User user) {

		Scanner sc = new Scanner(System.in);
		System.out.println("Which account would you like to withdraw from? (Enter Account #)");
		// this method has to be fixed
//		System.out.println("1. Checking\n2. Savings");
//		System.out.println("Type the number of account type");
		int accountId = sc.nextInt();
		System.out.println("Enter amount you would like to withdraw ($)");
		double money = sc.nextDouble();

//		if (accountType == 1) {
		uDao.withdrawAccount(user, money, accountId);
		System.out.println("Would you like to make another withdrawal in this session? [Y/N]");
		String choice = sc.next();
		if (choice.equals("y") || choice.equals("Y")) {
			withdrawAccount(user);
		}
		showUserMenu(user);

	}

	public void getListOfAccount(User user) {

//		Scanner sc = new Scanner(System.in);
//		System.out.println("See accounts?");
//
//		System.out.println("1. Yes\n2.No");
//		System.out.println("Enter selection");
//		int seeAccounts = sc.nextInt();

//		double money = sc.nextDouble();
		displayedAccounts = true;
//		if (seeAccounts == 1) {
		uDao.getListOfAccount(user);
		showUserMenu(user);
//			displayedAccounts=false;
//		}
//
//		else {
//			System.out.println("test");
//		}

	}

	public void deleteAccount(User user) {

		Scanner sc = new Scanner(System.in);
		List<Account> accountList = uDao.getListOfAccount(user);

		System.out.println("Which account would you like to delete? (Enter Account #)");
		int accNumber = sc.nextInt();

		for (Account a : accountList) {
			if (accNumber == a.getId()) {
				Account tempAccount = new Account(a.getId(), a.getBalance(), a.getType());

				if (tempAccount.getBalance() == 0) {
					uDao.deleteAccount(user, accNumber);
//					System.out.println("Account deleted");
					showUserMenu(user);
				}

				if (tempAccount.getBalance() > 0) {
					System.out.println(ANSI_BLACK + ANSI_YELLOW_BACKGROUND
							+ "\nYour account balance must be $0.00 in order to delete the account.\n" + ANSI_RESET);
					System.out.println("Do you still want to delete an account? [Y/N]");
					String choice = sc.next();
					if (choice.equals("y") || choice.equals("Y")) {
						this.deleteAccount(user);
					}
					showUserMenu(user);

				}

			}

		}

		System.out.println("Invalid entry. Enter correct account number.\n");
		this.deleteAccount(user);

	}

	public void loginUser() {
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter your username");
		String username = sc.next();
		System.out.println("Enter your password");
		String password = sc.next();

		String aName = "";
		String aPass = "";

		try {
			FileInputStream fileStream = new FileInputStream(
					"../DudekJDBCBank/src/test/resources/DB_Properties.properties");

			Properties prop = new Properties();
			prop.load(fileStream);

			aName = prop.getProperty("Admin_uname");

			aPass = prop.getProperty("Admin_pass");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		catch (IOException e2) {
			e2.printStackTrace();
		}

		catch (Exception e3) {
			e3.printStackTrace();
		}

		if (username.equals(aName) && password.equals(aPass)) {
			System.out.println("\nHello Admin!\n");
			showAdminMenu();
		}

		// This is important to understand.
		else {
			User b = uDao.loginUser(username, password);
			if (b.getUserStatus() != 0) {
				showUserMenu(b);
			}
		}

	}

	public void createAccount(User user) {

		Scanner sc = new Scanner(System.in);
		System.out.println("What type of account would you like to open?");

		System.out.println("1. Checking\n2. Savings");
		System.out.println("Choose the account type (1 = Checking or 2 = Savings)");
		int accountType = sc.nextInt();

		if (accountType == 1) {
			uDao.createAccount(user, "Checking");
			showUserMenu(user);
		}

		else {
			uDao.createAccount(user, "Savings");
			showUserMenu(user);
		}

	}

	// My Admin methods

	public void viewUsers() {
		System.out.println("Bank users:");
		aDao.viewUsers();
		showAdminMenu();
	}

	public void viewAccounts() {
		System.out.println("Bank accounts:");
		aDao.viewAccounts();
		showAdminMenu();
	}

	public void deleteUser() {
		System.out.println("Bank users:");
		aDao.viewUsers();
		Scanner sc = new Scanner(System.in);
		System.out.println("Which user would you like to delete (enter user ID)?");

		int userId = sc.nextInt();

		aDao.deleteUser(userId);
		showAdminMenu();
	}

	public void updateUser() {
		int userId;
		String fName;
		String lName;
		String uName;
		String pass;

		System.out.println("Bank users:");
		aDao.viewUsers();

		Scanner sc = new Scanner(System.in);
		System.out.println("Which user would you like to update (enter user ID)?");
		userId = sc.nextInt();
		
		System.out.println("Enter updated username");
		uName = sc.next();

		System.out.println("Enter updated first name");
		fName = sc.next();

		System.out.println("Enter updated last name");
		lName = sc.next();

		System.out.println("Enter updated password");
		pass = sc.next();

		User curr = new User(userId, fName, lName, uName, pass);

		aDao.updateUser(curr);
		showAdminMenu();

	}

	public void createUsers() {

		Scanner sc = new Scanner(System.in);

		System.out.println("Enter user first name");
		String fName = sc.next();
		System.out.println("Enter user last name");
		String lName = sc.next();
		System.out.println("Enter username");
		String username = sc.next();
		System.out.println("Enter user password");
		String password = sc.next();
		System.out.println("Re-enter user password");
		String passwordVal = sc.next();

		if (password.equals(passwordVal)) {
			aDao.createUsers(fName, lName, username, password);
			showAdminMenu();
		} else {
			// password validation
			while (!password.equals(passwordVal)) {
				System.out.println("\nYour passwords don't match. Please try again\n");
				System.out.println("Enter user password");
				password = sc.next();
				System.out.println("Re-enter user password");
				passwordVal = sc.next();
			}

//			createUsers();
			aDao.createUsers(fName, lName, username, password);
			showAdminMenu();
		}
		// added on 5.25 at 11:00pm
		showAdminMenu();

	}

	// Main Menu
	public void showMainMenu() {
		Scanner sc = new Scanner(System.in);

		System.out.println(
				"\n   Welcome to " + ANSI_YELLOW + "JDBC " + ANSI_CYAN + "Bank" + ANSI_RESET + " ©Copyright 2021");
		System.out.println("   Java Developer Best Community Bank\n");

		System.out.println(ANSI_YELLOW + "   1. Login");
		System.out.println("   2. Register");
		System.out.println("   3. Exit\n" + ANSI_RESET);

		int option = sc.nextInt();

		if (option == 1) {
			loginUser();
		}

		if (option == 2) {
			createUser();
		}

		if (option == 3) {
			
			System.out.println("\n   "+  ANSI_BLUE_BACKGROUND+ "*  *  *  *  *  * "+ ANSI_RESET+"" + ANSI_RED_BACKGROUND +"                                  " + ANSI_RESET);
			System.out.println("   "+ANSI_BLUE_BACKGROUND+" *  *  *  *  *   "+ANSI_RESET+"" + ANSI_WHITE_BACKGROUND +"                                  " + ANSI_RESET);
			System.out.println("   "+  ANSI_BLUE_BACKGROUND+ "*  *  *  *  *  * "+ ANSI_RESET+"" + ANSI_RED_BACKGROUND +"                                  " + ANSI_RESET);
			System.out.println("   "+ANSI_BLUE_BACKGROUND+" *  *  *  *  *   "+ANSI_RESET+"" + ANSI_WHITE_BACKGROUND +"                                  " + ANSI_RESET);
			System.out.println("   "+  ANSI_BLUE_BACKGROUND+ "*  *  *  *  *  * "+ ANSI_RESET+"" + ANSI_RED_BACKGROUND +"                                  " + ANSI_RESET);
			System.out.println("   "+ANSI_BLUE_BACKGROUND+" *  *  *  *  *   "+ANSI_RESET+"" + ANSI_WHITE_BACKGROUND +"                                  " + ANSI_RESET);
			System.out.println("   "+  ANSI_BLUE_BACKGROUND+ "*  *  *  *  *  * "+ ANSI_RESET+"" + ANSI_RED_BACKGROUND +"                                  " + ANSI_RESET);
			System.out.println("   "+ANSI_BLUE_BACKGROUND+" *  *  *  *  *   "+ANSI_RESET+"" + ANSI_WHITE_BACKGROUND +"                                  " + ANSI_RESET);
			System.out.println("   "+  ANSI_BLUE_BACKGROUND+ "*  *  *  *  *  * "+ ANSI_RESET+"" + ANSI_RED_BACKGROUND +"                                  " + ANSI_RESET);
			System.out.println("   "+ANSI_WHITE_BACKGROUND+"                                                   "+ ANSI_RESET);
			System.out.println("   "+ANSI_RED_BACKGROUND+"                                                   "+ ANSI_RESET);
			System.out.println("   "+ANSI_WHITE_BACKGROUND+"                                                   "+ ANSI_RESET);
			System.out.println("   "+ANSI_RED_BACKGROUND+"                                                   "+ ANSI_RESET);
			System.out.println("   "+ANSI_WHITE_BACKGROUND+"                                                   "+ ANSI_RESET);
			System.out.println("   "+ANSI_RED_BACKGROUND+"                                                   \n"+ ANSI_RESET);

			System.out.println(ANSI_YELLOW + "\n   Thank you for using our services. See you next time!" + ANSI_RESET);
			System.out.println(
					"\n              " + ANSI_YELLOW + "JDBC " + ANSI_CYAN + "Bank" + ANSI_RESET + " ©Copyright 2021");
			System.exit(0);
		}

		showMainMenu();

	}

	// User Menu
	public void showUserMenu(User user) {
		SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
		Date date = new Date();

//		System.out.println(user);

		// test - account summary
		if (displayedAccounts == false) {
			System.out.println("\nHi, " + ANSI_CYAN + user.getFirstName() + ANSI_RESET
					+ "!                                          " + ANSI_CYAN + formatter.format(date) + ANSI_RESET);
			System.out.println("Here's your Account Summary:");
			getListOfAccount(user);
		}
		displayedAccounts = false;
		// test above

		Scanner sc = new Scanner(System.in);

		System.out.println(ANSI_YELLOW + "   1. View Account/s");
		System.out.println("   2. Withdraw");
		System.out.println("   3. Deposit");
		System.out.println("   4. Create New Account");
		System.out.println("   5. Delete Account");
		System.out.println("   6. Logout\n" + ANSI_RESET);

		int option = sc.nextInt();

		switch (option) {
		case 1:
			getListOfAccount(user);
			break;
		case 2:
			withdrawAccount(user);
			break;
		case 3:
			depositAccount(user);
			break;
		case 4:
			createAccount(user);
			break;
		case 5:
			deleteAccount(user);
			break;
		case 6:
			System.out.println("\nThank you for using our services. See you next time!\n");
			showMainMenu();
			break;

		default:
			System.out.println("Invalid entry. Please enter number from 1-6.");
			showUserMenu(user);

		}
		sc.close();

	}

	// Admin Menu
	public void showAdminMenu() {
		Scanner sc = new Scanner(System.in);

		System.out.println(ANSI_YELLOW + "   1. View Accounts");
		System.out.println("   2. View Users");
		System.out.println("   3. View Users and Accounts");
		System.out.println("   4. Approve User");
		System.out.println("   5. Create New User");
		System.out.println("   6. Update User");
		System.out.println("   7. Delete User");
		System.out.println("   8. Logout\n" + ANSI_RESET);

		int option = sc.nextInt();

		switch (option) {
		case 1:
			viewAccounts();
			break;
		case 2:
			viewUsers();
			break;
		case 3:
			// view Users and Accounts
//			System.out.println("Under development...");
//			showAdminMenu();
			viewUsersAndAccounts();
			break;
		case 4:
			// approve User
//			System.out.println("Under development...");
//			showAdminMenu();
			approveUser();
			break;
		case 5:
			createUsers();
			break;
		case 6:
			updateUser();
			break;
		case 7:
			deleteUser();
			break;
		case 8:
			System.out.println("\nThank you for using our services. See you next time!\n");
			showMainMenu();
			break;

		default:
			System.out.println("Invalid entry. Please enter number from 1-8.\n");
			showAdminMenu();

		}
		sc.close();

	}

	// view users and accounts
	public void viewUsersAndAccounts() {
		System.out.println("Bank users and their accounts:\n");
		aDao.viewUsersAndAccounts();
		showAdminMenu();
	}

	public void approveUser() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Pending user approvals:");
		aDao.viewNewUsers();
		System.out.println("Which user would you like to approve? (Enter Id#)\n");
		int id = sc.nextInt();
//		User user = new User(null, null, null, null);
//		int status = 1;
//		
		aDao.approveUser(id);

		showAdminMenu();
	}

}
