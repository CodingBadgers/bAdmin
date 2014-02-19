package uk.codingbadgers.badmin;

import org.junit.Test;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileCriteria;

import static org.junit.Assert.*;

public class MojangIDTest {

	private static final String TEST1_USERNAME = "TheFish97";
	private static final String TEST1_ID = "af94b13ea3a344db81260e75f7cb4c65";
	
	private static final String TEST2_USERNAME = "n3wton";
	private static final String TEST2_ID = "75e91e7685ff4229b4c89d9eb5c3f72b";
	
	@Test
	public void test1() {
		long startTime = System.currentTimeMillis();
		HttpProfileRepository repo = new HttpProfileRepository();
		
		ProfileCriteria criteria = new ProfileCriteria(TEST1_USERNAME, bAdmin.MOJANG_AGENT); 
		Profile[] results = repo.findProfilesByCriteria(criteria);

		if (results.length == 0) {
			fail("No account found for " + TEST1_USERNAME);
		}

		Profile profile = results[0];

		if (results.length > 1) {
			fail("Multiple account found for " + TEST1_USERNAME);
		}

		long endTime = System.currentTimeMillis();
		assertEquals(profile.getId(), TEST1_ID);
		System.out.println("Test1: " + (endTime - startTime));
	}
	
	@Test
	public void test2() {
		long startTime = System.currentTimeMillis();
		HttpProfileRepository repo = new HttpProfileRepository();
		
		ProfileCriteria criteria = new ProfileCriteria(TEST2_USERNAME, bAdmin.MOJANG_AGENT); 
		Profile[] results = repo.findProfilesByCriteria(criteria);

		if (results.length == 0) {
			fail("No account found for " + TEST2_USERNAME);
		}

		Profile profile = results[0];

		if (results.length > 1) {
			fail("Multiple account found for " + TEST2_USERNAME);
		}

		long endTime = System.currentTimeMillis();
		assertEquals(profile.getId(), TEST2_ID);
		System.out.println("Test2: " + (endTime - startTime));
	}
}
