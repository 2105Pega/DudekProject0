package com.revature.banktests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.revature.model.Account;
import com.revature.model.User;

class BankTests {

	@Test
	void userConstructor() {

		User user1 = new User("John", "Smith", "Johny52", "pass123");
		assertEquals("Johny52", user1.getUsername());
	}
	
//	@Test
//	void failTest() {
//		fail("Not yet implemented");
//	}
	
	@Test
	void accountConstructor() {

		Account account1 = new Account(500, "Checking");
		assertEquals(500, account1.getBalance());
	}

}
