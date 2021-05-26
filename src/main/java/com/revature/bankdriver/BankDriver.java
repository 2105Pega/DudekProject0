package com.revature.bankdriver;

import com.revature.model.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import com.revature.model.Admin;
import com.revature.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BankDriver {

	private static final Logger logger = LogManager.getLogger(BankDriver.class);

	public static void main(String[] args) {
		

		logger.info("Welcome to JDBC Bank © Copyright 2021");
		UserService userServ = new UserService();
//		Scanner sc = new Scanner(System.in);

//		System.out.println("\n   Welcome \r\n");
		

		// Main Menu
		userServ.showMainMenu();

//		User user1 = new User(1, "Peter", "Dudek", "Peter123", "123");

//		userServ.depositAccount(user1);

//		userServ.withdrawAccount(user1);

//		userServ.getListOfAccount(user1);

//		userServ.createUser();

//		userServ.deleteAccount(user1);

//		userServ.loginUser();		

//		userServ.createAccount(user1);

//		userServ.viewUsers();

//		userServ.viewAccounts();

//		userServ.deleteUser();

//		userServ.updateUser();

//		userServ.createUsers();
		
		



		

	}

}
