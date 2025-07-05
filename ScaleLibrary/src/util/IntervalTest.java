package util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntervalTest {

	@BeforeEach
	void setUp() throws Exception {
		Interval.intervals.clear();
	}

	@Test
	void testGetInterval() {
		// test new interval is stored in map
		assertFalse(Interval.intervals.containsKey("#4"));
		Interval testInterval = Interval.getInterval("#4");
		assertTrue(Interval.intervals.containsKey("#4"));

		// test correct interval was created
		assertEquals("#4", testInterval.toString());
		assertEquals(1, testInterval.getSemitoneChange());

		// test previously created interval object is retrieved from map
		assertSame(testInterval, Interval.getInterval("#4"), "the same object should be returned if previously created");

		// test invalid interval names
		assertNull(Interval.getInterval(""));
		assertNull(Interval.getInterval("#"));
		assertNull(Interval.getInterval("0"));
		assertNull(Interval.getInterval("16"));
		assertNull(Interval.getInterval("bbb7"));
		assertNull(Interval.getInterval("b 3"));
	}

	@Test
	void testParseInterval() {
		// test valid intervals
		Interval testInterval = Interval.parseInterval("3");
		assertEquals("3", testInterval.toString());
		assertEquals(0, testInterval.getSemitoneChange());
		
		testInterval = Interval.parseInterval("bb7");
		assertEquals("bb7", testInterval.toString());
		assertEquals(-2, testInterval.getSemitoneChange());
		
		testInterval = Interval.parseInterval("#4");
		assertEquals("#4", testInterval.toString());
		assertEquals(1, testInterval.getSemitoneChange());
		
		// test invalid intervals
		assertNull(Interval.parseInterval(""));
		assertNull(Interval.parseInterval("#"));
		assertNull(Interval.parseInterval("0"));
		assertNull(Interval.parseInterval("16"), "only intervals within 2 octaves should be parsed");
		assertNull(Interval.parseInterval("bbb7"), "only a maximum of two sharps/flats are allowed");
		assertNull(Interval.parseInterval("b 3"), "whitespace not allowed");
	}

	@Test
	void testSimplifySize() {
		// test simple intervals
		assertEquals(1, Interval.getInterval("1").simplifySize());
		assertEquals(3, Interval.getInterval("3").simplifySize());
		assertEquals(3, Interval.getInterval("b3").simplifySize());
		
		// test compound intervals
		assertEquals(1, Interval.getInterval("8").simplifySize());
		assertEquals(3, Interval.getInterval("b10").simplifySize());
		assertEquals(1, Interval.getInterval("15").simplifySize());
	}

	@Test
	void testCountSemitones() {
		// test natural notes
		Note note1 = Note.getNote("C");
		Note note2 = Note.getNote("E");
		assertEquals(4, Interval.countSemitones(note1, note2));
		
		note1 = Note.getNote("F");
		note2 = Note.getNote("D");
		assertEquals(9, Interval.countSemitones(note1, note2));
		
		// test notes with accidentals
		note1 = Note.getNote("Dbb");
		note2 = Note.getNote("Dx");
		assertEquals(4, Interval.countSemitones(note1, note2));
		
		note1 = Note.getNote("F#");
		note2 = Note.getNote("Gb");
		assertEquals(0, Interval.countSemitones(note1, note2));
	}

	@Test
	void testIntervalToSemitones() {
		// test some valid intervals
		Interval testInterval = Interval.getInterval("2");
		assertEquals(2, Interval.intervalToSemitones(testInterval));
		
		testInterval = Interval.getInterval("#4");
		assertEquals(6, Interval.intervalToSemitones(testInterval));
		
		testInterval = Interval.getInterval("bb7");
		assertEquals(9, Interval.intervalToSemitones(testInterval));
		
		testInterval = Interval.getInterval("8");
		assertEquals(0, Interval.intervalToSemitones(testInterval), "compound intervals should be simplified");
	}
}
