package util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NoteTest {

	@BeforeEach
	void setUp() throws Exception {
		Note.notes.clear();
	}

	@Test
	void testGetNote() {
		// test new note is stored in map
		assertFalse(Note.notes.containsKey("a"));
		Note testNote = Note.getNote("A");
		assertTrue(Note.notes.containsKey("a"));
		
		// test correct note was created
		assertEquals("A", testNote.getLetter());
		assertEquals("", testNote.getAccidental().toString());
		assertEquals(0, testNote.getSemitoneChange());
		
		// test previously created note object is retrieved from map
		assertSame(testNote, Note.getNote("a"), "the same object should be returned if previously created");
		
		// test invalid note names
		assertNull(Note.getNote(""));
		assertNull(Note.getNote("#"));
		assertNull(Note.getNote("F##"));
		assertNull(Note.getNote("ABC"));
		assertNull(Note.getNote("H"));
		assertNull(Note.getNote("A b"));
	}

	@Test
	void testParseNote() {
		// test valid notes
		Note testNote = Note.parseNote("A");
		assertEquals("A", testNote.getLetter());
		assertEquals("", testNote.getAccidental().toString(), "a blank accidental should be used when no accidental is given");
		assertEquals(0, testNote.getSemitoneChange());
		
		testNote = Note.parseNote("Bb");
		assertEquals("B", testNote.getLetter());
		assertEquals("b", testNote.getAccidental().toString(), "'b' after letter should be parsed as a flat");
		assertEquals(-1, testNote.getSemitoneChange());
		
		testNote = Note.parseNote("fx");
		assertEquals("F", testNote.getLetter(), "lowercase letter should have been converted to uppercase");
		assertEquals("x", testNote.getAccidental().toString());
		assertEquals(2, testNote.getSemitoneChange());
		
		// test invalid notes
		assertNull(Note.parseNote(""));
		assertNull(Note.parseNote("#"));
		assertNull(Note.parseNote("F##"));
		assertNull(Note.parseNote("ABC"));
		assertNull(Note.parseNote("H"));
		assertNull(Note.parseNote("A b"));
	}

	@Test
	void testChangeAccidental() {
		// test some valid changes
		Note testNote1 = Note.getNote("A");
		Note testNote2 = Note.getNote("Ab");
		Note testNote3 = Note.getNote("Ax");
		
		assertSame(testNote2, testNote1.changeAccidental(-1), "note lowered incorrectly");
		assertSame(testNote3, testNote1.changeAccidental(2), "note raised incorrectly");
		
		// test changes outside of valid range
		assertNull(testNote1.changeAccidental(-3), "notes that would require more than 2 flats are invalid");
		assertNull(testNote3.changeAccidental(1), "notes that would require more than 2 sharps are invalid");
	}

	@Test
	void testAddInterval() {
		// test some valid intervals
		Note testNote1 = Note.getNote("A");
		Note testNote2 = Note.getNote("C#");
		Note testNote3 = Note.getNote("D#");
		
		assertSame(testNote2, testNote1.addInterval(Interval.getInterval("3")));
		assertSame(testNote3, testNote1.addInterval(Interval.getInterval("#4")), "modified interval added incorrectly");
		
		// test some intervals outside valid range
		Note testNote4 = Note.getNote("Bbb");
		Note testNote5 = Note.getNote("Cx");
		assertNull(testNote4.addInterval(Interval.getInterval("bb7")), "notes that would require more than 2 flats are invalid");
		assertNull(testNote5.addInterval(Interval.getInterval("#5")), "notes that would require more than 2 sharps are invalid");
	}

	@Test
	void testToString() {
		// test some notes with different accidentals
		Note testNote1 = Note.getNote("A");
		Note testNote2 = Note.getNote("C#");
		Note testNote3 = Note.getNote("Bbb");
		
		assertEquals("A", testNote1.toString());
		assertEquals("C#", testNote2.toString());
		assertEquals("Bbb", testNote3.toString());
	}

	@Test
	void testEqualsObject() {
		// test same note but different object
		Note testNote1 = new Note("A", "");
		Note testNote2 = new Note("A", "");
		
		assertTrue(testNote1.equals(testNote2));
		
		// test some different notes
		Note testNote3 = Note.getNote("F");
		Note testNote4 = Note.getNote("F#");
		Note testNote5 = Note.getNote("Gb");
		
		assertFalse(testNote3.equals(testNote4), "same letter with different accidental should not be equal");
		assertFalse(testNote4.equals(testNote5), "enharmonic notes should not be equal");
	}
}
