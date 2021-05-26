package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.revature.bankdriver.BankDriver;
import com.revature.model.Account;
import com.revature.model.User;
import com.revature.util.ConnectionUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.PSQLException;

import com.revature.service.UserService;

public class UserDAOImpl implements UserDAO {

	private static final Logger logger = LogManager.getLogger(User.class);

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

	@Override
	public List<Account> getListOfAccount(User user) {
		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "select * from account_table ac\r\n"
					+ "inner join user_account_join_table ua on ac.account_id = ua.account_id where user_id = ?"
					+ "order by account_type";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, user.getId());

			ResultSet result = statement.executeQuery();

			List<Account> accList = new ArrayList<Account>();

			while (result.next()) {
				Account a = new Account(result.getInt("account_id"), result.getDouble("account_balance"),
						result.getString("account_type"));

				accList.add(a);
			}
			System.out.println("\n");
			for (int i = 0; i < accList.size(); i++) {
				System.out.println(
						ANSI_CYAN + "-----------------------------------------------------------" + ANSI_RESET);
				System.out.printf(
						accList.get(i).getType() + " Account #" + accList.get(i).getId() + "      Balance: $%,.2f\n\n",
						accList.get(i).getBalance());
//				System.out.println("\n");

			}
			System.out
					.println(ANSI_CYAN + "-----------------------------------------------------------\n" + ANSI_RESET);

			return accList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean deleteAccount(User user, int accNumber) {

		try (Connection conn = ConnectionUtils.getConnection()) {

			// as SQL query
			// foreign key constraint:
			// 1) first delete the record with the foreign key
//			String sql = "delete from user_account_join_table where account_id = ?;\r\n"
//					// 2) then delete the record with the primary key
//					+ "delete from account_table where account_id = ?";

			// OR as SQL query with user-defined function
			String sql = "create or replace function delete_user_account(accId integer)\r\n" + "returns void\r\n"
					+ "language plpgsql\r\n" + "as \r\n"
					+ "$$ --It is a way for plpgsql to know that we are about to define a function\r\n"
					+ "declare --where we put our variable declarations to be used in the function\r\n"
					+ "--	total integer;\r\n" + "	begin\r\n"
					+ "delete from user_account_join_table where account_id = accId;\r\n"
					+ "delete from account_table where account_id = accId;		\r\n" + "	end;\r\n" + "$$;"
					+ "select delete_user_account(?)";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setInt(1, accNumber);
			// this below is needed for regular SQL query
//			statement.setInt(2, accNumber);

			statement.executeUpdate();
			logger.info("" + user.getUsername() + "'s account #" + accNumber + " has been deleted!");
			System.out.println("\n" + ANSI_CYAN + user.getUsername() + ANSI_RESET + " your account #" + accNumber
					+ " has been deleted!");
			return true;

		} catch (PSQLException e) {
			// this is only for SQL query with user-defined function
			logger.warn(e);
			logger.info("" + user.getUsername() + "'s account#" + accNumber + " has been deleted!");
			System.out.println("\n" + ANSI_CYAN + user.getUsername() + ANSI_RESET + " your account #" + accNumber
					+ " has been deleted!");
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean depositAccount(User user, double money, int accountId) {

		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "Update account_table set account_balance = account_balance + ? where account_id = ?";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setDouble(1, money);
			statement.setInt(2, accountId);

			statement.executeUpdate();

			logger.info("(User id = " + user.getId() + ") " + user.getUsername() + "'s funds have been deposited: $"
					+ money + " to " + accountId + " account.");
			System.out.printf("\n$%,.2f have been deposited into account #" + accountId + ".\n", money);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean withdrawAccount(User user, double money, int accountId) {

		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "Update account_table set account_balance = account_balance - ? where account_id = ?";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setDouble(1, money);
			statement.setInt(2, accountId);

			statement.executeUpdate();
			logger.info("(User id = " + user.getId() + ") " + user.getUsername() + "'s funds have been withdrawn: $"
					+ money + " to " + accountId + " account.");
			System.out.printf("\n$%,.2f have been withdrawn from account #" + accountId + ".\n", money);
			return true;

		} catch (SQLException e) {
			logger.error(e);
			System.out.println("\n   " + ANSI_YELLOW_BACKGROUND + ANSI_BLACK + "OVERDRAFT PROTECTION!" + ANSI_RESET
					+ "  Requested amount exceeds your available funds. Please enter a lower amount.\r\n");
		}

		return false;
	}

	@Override
	public User loginUser(String username, String password) {

		int count = 0;
		int attempts = 2;

		do {
			try (Connection conn = ConnectionUtils.getConnection()) {

				String sql = "select * from user_table \r\n" + "where user_username = ? and user_password = ?";

				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, username);
				statement.setString(2, password);

				ResultSet result = statement.executeQuery();

				if (result.next()) {

					if (result.getString("user_username").equals(username)
							&& result.getString("user_password").equals(password)
							&& result.getInt("user_status") == 0) {
						count++;
						System.out
								.println("\nPlease wait until your account is approved by our system administrator.\n");
						User newUser = new User(result.getInt("user_id"), result.getString("user_fname"),
								result.getString("user_lname"), result.getString("user_username"),
								result.getString("user_password"), result.getInt("user_status"));
						return newUser;

					}

					if (result.getString("user_username").equals(username)
							&& result.getString("user_password").equals(password)
							&& result.getInt("user_status") == 1) {
						System.out.println("\nUser verified!\n");

						User currUser = new User(result.getInt("user_id"), result.getString("user_fname"),
								result.getString("user_lname"), result.getString("user_username"),
								result.getString("user_password"), result.getInt("user_status"));
						count++;

						return currUser;
					}
				}

//				else if (result.next()) {
//
//					if (result.getString("user_username").equals(username)
//							&& result.getString("user_password").equals(password)) {
//						System.out.println("Credentials verified!");
//
//						User currUser = new User(result.getInt("user_id"), result.getString("user_fname"),
//								result.getString("user_lname"), result.getString("user_id"),
//								result.getString("user_id"));
//						count++;
//
//						return currUser;
//					}
//				}

				else if (!result.next()) {
					Scanner sc = new Scanner(System.in);

					if (attempts > 1) {
						System.out.println("\nInvalid username/password combination. You have 2 attempts left.");
						System.out.println("After that you will have to wait 30 minutes before you can log in.\r\n");
					}
					if (attempts == 1) {
						System.out.println("\nInvalid username/password combination. You have 1 attempt left.");
						System.out.println("After that you will have to wait 30 minutes before you can log in.\r\n");
					}
					if (attempts == 0) {
						logger.error("Invalid username/password combination was entered 3 times.");
						SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
						Date date = new Date();

						System.out.println(
								"\nYou have used all your attempts. Your banking app is temporarily locked.\n");
						System.out.println(
								"You can try to log in again in 30 minutes from " + formatter.format(date) + ".");
						Thread.sleep(5000);

						System.out.println("......3");
						Thread.sleep(800);
						System.out.println("....2");
						Thread.sleep(700);
						System.out.println("..1");
						Thread.sleep(600);
						System.out.println("\nGood bye!");

						System.exit(1);
					}

					System.out.println("1. Try again");
					System.out.println("2. Main Menu\n");
					int choice = sc.nextInt();

					if (choice == 2) {
//						System.out.println("Good bye!");
//						System.exit(0);
						count++;

						User oddUser = new User(1000, "name", "lastName", "username", "user_password", 0);
						return oddUser;
					}
					if (choice == 1) {
						attempts--;
						System.out.println("Enter your username");
						username = sc.next();
						System.out.println("Enter your password");
						password = sc.next();

					}

				}

			} catch (SQLException e) {
				e.printStackTrace();

			} catch (InterruptedException e2) {
				e2.printStackTrace();

			}

		} while (count < 1);

		return null;
	}

	@Override
	public boolean createUser(String firstName, String lastName, String username, String password) {

		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "insert into user_table(user_fname, user_lname, user_username, user_password)"
					+ "values(?, ?, ?, ?)";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setString(3, username);
			statement.setString(4, password);

			statement.executeUpdate();
			System.out.println("\nYour registration has been submitted!\n");

			return true;

		}

		catch (SQLException e) {
			logger.error(e);
			System.out
					.println("\nThis username is not available. Please choose a different one in order to register.\n");

		}
		return false;

	}

	@Override
	public boolean createAccount(User user, String accountType) {

		try (Connection conn = ConnectionUtils.getConnection()) {

			// 1)first insert the new account into account_table
			String sql = "insert into account_table(account_balance, account_type)\r\n" + "values (0, ?);"
			// 2) then enter user Id into join_table (account id will be temporarily null)
					+ "insert into user_account_join_table(user_id)\r\n" + "values(?);" +

					// 3) update join_table with the recent most account id where user id matches
					// and account id is null
					"update user_account_join_table set account_id = (select max(account_id) from account_table)\r\n"
					+ "where user_id = ? and account_id is null;";

//					"select * from user_table ut\r\n"
//					+ "inner join user_account_join_table uajt on ut.? = uajt.user_id \r\n"
//					+ "inner join account_table at2 on at2.(select max(account_id) from account table) = uajt.account_id";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setString(1, accountType);
			statement.setInt(2, user.getId());
			statement.setInt(3, user.getId());

			statement.executeUpdate();

			logger.info("" + accountType + " account has been created for " + user.getFirstName() + " "
					+ user.getLastName() + ".");
			System.out.println("\n" + accountType + " account has been created.\n");
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean showAllAssets(int userId) {
		try (Connection conn = ConnectionUtils.getConnection()) {

			// 1)first insert the new account into account_table
//			String sql = ""
//
//			PreparedStatement statement = conn.prepareStatement(sql);
//
//			statement.setString(1, accountType);
//			statement.setInt(2, user.getId());
//			statement.setInt(3, user.getId());
//
//			statement.executeUpdate();
//
//			logger.info("" + accountType + " account has been created for " + user.getFirstName() + " "
//					+ user.getLastName() + ".");
//			System.out.println("" + accountType + " account  has been created.");
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}
}
