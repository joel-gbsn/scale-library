package notes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


class AccidentalTest {
	
	@AfterEach
	void tearDown() throws Exception {
	    // reset the list of accidentals
		Accidental.getAccidentals().clear();
	}

	@Test
	void testAccidental() {
		// test creating a flat
		Accidental flat = new Accidental("b", -1);
		assertEquals("b", flat.getSymbol());
		assertEquals(-1, flat.getSemitones());
		assertEquals(1, Accidental.getAccidentals().size());
		assertTrue(Accidental.getAccidentals().contains(flat));
		
		// test creating a natural
		Accidental natural = new Accidental("", 0);
		assertEquals("", natural.getSymbol());
		assertEquals(0, natural.getSemitones());
		assertEquals(2, Accidental.getAccidentals().size());
		assertTrue(Accidental.getAccidentals().contains(natural));
		
		// test creating a double sharp
		Accidental doubleSharp = new Accidental("x", 2);
		assertEquals("x", doubleSharp.getSymbol());
		assertEquals(2, doubleSharp.getSemitones());
		assertEquals(3, Accidental.getAccidentals().size());
		assertTrue(Accidental.getAccidentals().contains(doubleSharp));
	}

	@Test
	void testCreateAllAccidentals() {
		Accidental.createAllAccidentals();
		assertEquals(7, Accidental.getAccidentals().size());
	}

	@Test
	void testConvertSemitones() {
		// test some valid accidentals
		Accidental flat = new Accidental("b", -1);
		assertEquals(flat, Accidental.convertSemitones(-1));
		
		Accidental natural = new Accidental("", 0);
		assertEquals(natural, Accidental.convertSemitones(0));
		
		// test a non-existing accidental
		assertNull(Accidental.convertSemitones(4), "Only accidentals up to 3 sharps or flats are included");
	}

	@Test
	void testToString() {
		// test a few different accidentals
		Accidental flat = new Accidental("b", -1);
		assertEquals("b", flat.toString());
		
		Accidental natural = new Accidental("", 0);
		assertEquals("", natural.toString());
		
		Accidental doubleSharp = new Accidental("x", 2);
		assertEquals("x", doubleSharp.toString());
	}

	@Test
	void testEqualsObject() {
		// test the same accidental
		Accidental flat1 = new Accidental("b", -1);
		Accidental flat2 = new Accidental("b", -1);
		assertTrue(flat1.equals(flat2));

		// test different accidentals
		Accidental sharp = new Accidental("#", 1);
		assertFalse(flat1.equals(sharp));
	}
}
