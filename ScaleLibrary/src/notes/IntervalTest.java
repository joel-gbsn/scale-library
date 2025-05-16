package notes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntervalTest {

	@BeforeEach
	void setUp() throws Exception {
		// create all notes and accidentals
		Accidental.createAllAccidentals();
		Note.createAllNotes();
	}

	@AfterEach
	void tearDown() throws Exception {
		// reset each scale component
		Interval.getIntervals().clear();
		Accidental.getAccidentals().clear();
		Note.getNotes().clear();
	}

	@Test
	void testInterval() {
		// test creating a perfect 4th
		assertNull(Interval.getInterval("4"));
		Accidental quality = new Accidental("", 0);
		Interval interval = new Interval(4, quality);
		assertEquals(4, interval.getNumber());
		assertEquals("", interval.getQuality().getSymbol());
		assertNotNull(Interval.getInterval("4"));
		
		// test creating an augmented 5th
		assertNull(Interval.getInterval("#5"));
		quality = new Accidental("#", 1);
		interval = new Interval(5, quality);
		assertEquals(5, interval.getNumber());
		assertEquals("#", interval.getQuality().getSymbol());
		assertNotNull(Interval.getInterval("#5"));
	}

	@Test
	void testCreateAllIntervals() {
		// test that all possible intervals within 2 octaves have been created
		Interval.createAllIntervals();
		assertEquals(105, Interval.getIntervals().size());
		
		// test a few intervals that should have been created
		assertNotNull(Interval.getInterval("4"));
		assertNotNull(Interval.getInterval("b3"));
		assertNotNull(Interval.getInterval("x5"));
		assertNotNull(Interval.getInterval("bbb7"));
		
		// test some invalid intervals and random text
		assertNull(Interval.getInterval("0"));
		assertNull(Interval.getInterval("xx5"));
		assertNull(Interval.getInterval("a"));
		assertNull(Interval.getInterval(""));
	}

	@Test
	void testSimplifyNumber() {
		Interval.createAllIntervals();
		
		// test some simple intervals
		Interval interval = Interval.getInterval("7");
		assertEquals(7, interval.simplifyNumber());
		
		interval = Interval.getInterval("b5");
		assertEquals(5, interval.simplifyNumber());
		
		// test some compound intervals
		interval = Interval.getInterval("10");
		assertEquals(3, interval.simplifyNumber(), "A 10th should be simplified to a 3rd");
		
		interval = Interval.getInterval("#11");
		assertEquals(4, interval.simplifyNumber(), "An 11th should be simplified to a 4th");
		
		interval = Interval.getInterval("15");
		assertEquals(1, interval.simplifyNumber(), "A 15th should be simplified to a unison");
	}

	@Test
	void testGetInterval() {
		// create and retrieve some valid intervals
		Accidental quality = new Accidental("", 0);
		Interval interval = new Interval(4, quality);
		assertSame(interval, Interval.getInterval("4"));
		
		quality = new Accidental("b", -1);
		interval = new Interval(3, quality);
		assertSame(interval, Interval.getInterval("b3"));
		
		quality = new Accidental("#", 1);
		interval = new Interval(11, quality);
		assertSame(interval, Interval.getInterval("#11"));
		
		// test an interval outside 2 octaves
		assertNull(Interval.getInterval("16"));
		
		// test some invalid intervals
		assertNull(Interval.getInterval("-1"));
		assertNull(Interval.getInterval("0"));
	}

	@Test
	void testCountSemitones() {
		// test some standard intervals
		Note note1 = Note.getNote("C");
		Note note2 = Note.getNote("D");
		assertEquals(2, Interval.countSemitones(note1, note2));
		
		note1 = Note.getNote("F");
		note2 = Note.getNote("E");
		assertEquals(11, Interval.countSemitones(note1, note2));
		
		note1 = Note.getNote("C#");
		note2 = Note.getNote("G#");
		assertEquals(7, Interval.countSemitones(note1, note2));
		
		note1 = Note.getNote("Fb");
		note2 = Note.getNote("F");
		assertEquals(1, Interval.countSemitones(note1, note2));
		
		// test the same note
		note1 = Note.getNote("D");
		note2 = Note.getNote("D");
		assertEquals(0, Interval.countSemitones(note1, note2));
		
		note1 = Note.getNote("D#");
		note2 = Note.getNote("Eb");
		assertEquals(0, Interval.countSemitones(note1, note2));
	}

	@Test
	void testIntervalToSemitones() {
		Interval.createAllIntervals();
		
		// test some simple intervals
		Interval interval = Interval.getInterval("3");
		assertEquals(4, Interval.intervalToSemitones(interval));
		
	    interval = Interval.getInterval("#4");
		assertEquals(6, Interval.intervalToSemitones(interval));

		interval = Interval.getInterval("1");
		assertEquals(0, Interval.intervalToSemitones(interval));
		
		interval = Interval.getInterval("bb2");
		assertEquals(0, Interval.intervalToSemitones(interval));
		
		// test some compound intervals
		interval = Interval.getInterval("8");
		assertEquals(0, Interval.intervalToSemitones(interval));

		interval = Interval.getInterval("b10");
		assertEquals(3, Interval.intervalToSemitones(interval));
	}

	@Test
	void testToString() {
		Interval.createAllIntervals();
		
		// test some intervals with different qualities
		Interval interval = Interval.getInterval("7");
		assertEquals("7", interval.toString());
		
		interval = Interval.getInterval("b3");
		assertEquals("b3", interval.toString());
		
		interval = Interval.getInterval("#11");
		assertEquals("#11", interval.toString());
		
		interval = Interval.getInterval("bb7");
		assertEquals("bb7", interval.toString());
	}
}
