/**
 * 
 */
package it.islandofcode.jvtllib.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import it.islandofcode.jvtllib.model.util.SimpleDate;

/**
 * @author Pier Riccardo Monzo
 * @date 12 mar 2018
 */
class TestSimpleDateSuccess {

	/**
	 * Test method for {@link it.islandofcode.jvtllib.model.util.SimpleDate#SimpleDate()}.
	 */
	@Test
	void testSimpleDate() {
		SimpleDate d = new SimpleDate();
		assertEquals(d.getDate(), LocalDate.MIN);
	}

	/**
	 * Test method for {@link it.islandofcode.jvtllib.model.util.SimpleDate#SimpleDate(java.lang.String)}.
	 */
	@Test
	void testSimpleDateString() {
		//try {
			SimpleDate d = new SimpleDate("Mon Sep 01 02:00:00 CEST 2008");
			assertEquals(d.getDateString(), LocalDate.MIN.toString());
		/*} catch (ParseException e) {
			fail("Catturata eccezione");
			e.printStackTrace();
		}*/
		
	}

	/**
	 * Test method for {@link it.islandofcode.jvtllib.model.util.SimpleDate#getDateString()}.
	 */
	@Test
	void testGetDateString() {
		SimpleDate d = new SimpleDate("2018-01-01");
		assertEquals(d.getDateString(), "2018-01-01");
	}

}
