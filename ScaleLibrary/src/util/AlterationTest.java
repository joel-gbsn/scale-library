package util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AlterationTest {

	@Test
	void testGetAlterationString() {
		// test some valid symbols
		assertEquals(-2, Alteration.getAlteration("bb").getSemitoneChange());
		assertEquals(-1, Alteration.getAlteration("b").getSemitoneChange());
		assertEquals(0, Alteration.getAlteration("").getSemitoneChange());
		assertEquals(1, Alteration.getAlteration("#").getSemitoneChange());
		assertEquals(2, Alteration.getAlteration("x").getSemitoneChange());
		
		// test some invalid symbols
		assertNull(Alteration.getAlteration("B"));
		assertNull(Alteration.getAlteration("##"));
		assertNull(Alteration.getAlteration("a"));
		assertNull(Alteration.getAlteration("1"));
	}

	@Test
	void testGetAlterationInt() {
		// test some valid alterations
		assertEquals("bb", Alteration.getAlteration(-2).toString());
		assertEquals("b", Alteration.getAlteration(-1).toString());
		assertEquals("", Alteration.getAlteration(0).toString());
		assertEquals("#", Alteration.getAlteration(1).toString());
		assertEquals("x", Alteration.getAlteration(2).toString());
		
		// test some invalid alterations
		assertNull(Alteration.getAlteration(-3));
		assertNull(Alteration.getAlteration(3));
	}
	
	@Test
	void testCreateAllAlterations() {
		// test all alterations are correctly created
		Map<String, Alteration> alterations = Alteration.createAllAlterations();
		assertEquals(5, alterations.size());
		assertTrue(alterations.containsKey("bb"));
		Alteration alteration = alterations.get("bb");
		assertEquals(-2, alteration.getSemitoneChange());
		assertEquals("bb", alteration.toString());
	}
}
