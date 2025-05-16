package notes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NoteTest {

	@BeforeEach
	void setUp() throws Exception {
		// create all accidentals
		Accidental.createAllAccidentals();
	}

	@AfterEach
	void tearDown() throws Exception {
		// reset each scale component
		Note.getNotes().clear();
		Accidental.getAccidentals().clear();
		Interval.getIntervals().clear();
	}

	@Test
	void testNote() {
		// test creating a natural note
		assertNull(Note.getNote("C"));
		Accidental accidental = new Accidental("", 0);
		Note note = new Note("C", accidental);
		assertEquals("C", note.getLetterName());
		assertEquals("", note.getAccidental().getSymbol());
		assertNotNull(Note.getNote("C"));
		
		// test creating a note with an accidental
		assertNull(Note.getNote("F#"));
		accidental = new Accidental("#", 1);
		note = new Note("F", accidental);
		assertEquals("F", note.getLetterName());
		assertEquals("#", note.getAccidental().getSymbol());
		assertNotNull(Note.getNote("F#"));
	}

	@Test
	void testCreateAllNotes() {
		Note.createAllNotes();
		assertEquals(49, Note.getNotes().size());
		
		// test some valid notes that should have been created
		assertNotNull(Note.getNote("A"));
		assertNotNull(Note.getNote("a"));
		assertNotNull(Note.getNote("Bb"));
		assertNotNull(Note.getNote("F#"));
		assertNotNull(Note.getNote("Ebb"));
		assertNotNull(Note.getNote("Fx"));
		assertNotNull(Note.getNote("Abbb"));
		
		// test some invalid notes and random text
		assertNull(Note.getNote("Fxx"));
		assertNull(Note.getNote("Abbbb"));
		assertNull(Note.getNote("z"));
		assertNull(Note.getNote(""));
	}

	@Test
	void testGetNote() {
		// create and retrieve some valid notes
		Accidental accidental = new Accidental("", 0);
		Note note = new Note("A", accidental);
		assertSame(note, Note.getNote("A"));
		assertSame(note, Note.getNote("a"));

		accidental = new Accidental("b", -1);
		note = new Note("B", accidental);
		assertSame(note, Note.getNote("Bb"));

		accidental = new Accidental("x", 2);
		note = new Note("C", accidental);
		assertSame(note, Note.getNote("Cx"));

		// test some invalid notes
		assertNull(Note.getNote("Z"));
		assertNull(Note.getNote("Axx"));
	}

	@Test
	void testAddInterval() {
		Note.createAllNotes();
		Interval.createAllIntervals();
		
		// test adding some simple intervals
		Note note1 = Note.getNote("C");
		Interval interval = Interval.getInterval("5");
		Note note2 = note1.addInterval(interval);
		assertEquals(Note.getNote("G"), note2);
		
		interval = Interval.getInterval("#4");
		note2 = note1.addInterval(interval);
		assertEquals(Note.getNote("F#"), note2);
		
		interval = Interval.getInterval("bb7");
		note2 = note1.addInterval(interval);
		assertEquals(Note.getNote("Bbb"), note2);
		
		// test some enharmonic notes
		interval = Interval.getInterval("bb2");
		note2 = note1.addInterval(interval);
		assertEquals(Note.getNote("Dbb"), note2);
		
		interval = Interval.getInterval("#7");
		note2 = note1.addInterval(interval);
		assertEquals(Note.getNote("B#"), note2);
		
		// test some compound intervals
		interval = Interval.getInterval("10");
		note2 = note1.addInterval(interval);
		assertEquals(Note.getNote("E"), note2);
		
		interval = Interval.getInterval("8");
		note2 = note1.addInterval(interval);
		assertEquals(Note.getNote("C"), note2);
		
		// test an interval that returns a null note
		interval = Interval.getInterval("bb7");
		note1 = Note.getNote("Cbb");
		note2 = note1.addInterval(interval);
		assertNull(note2, "An interval requiring a note with four flats should return null");
	}

	@Test
	void testToString() {
		Note.createAllNotes();
		
		// test some notes with different accidentals
		Note note = Note.getNote("A");
		assertEquals("A", note.toString());
		
		note = Note.getNote("Bb");
		assertEquals("Bb", note.toString());
		
		note = Note.getNote("F#");
		assertEquals("F#", note.toString());
		
		note = Note.getNote("Abb");
		assertEquals("Abb", note.toString());
		
		note = Note.getNote("Cx");
		assertEquals("Cx", note.toString());
	}

	@Test
	void testEqualsObject() {
		// test different instances of the same note
		Accidental accidental = new Accidental("", 0);
		Note note1 = new Note("C", accidental);
		Note note2 = new Note("C", accidental);
		assertTrue(note1.equals(note2));
		
		accidental = new Accidental("#", 1);
	    note1 = new Note("F", accidental);
		note2 = new Note("F", accidental);
		assertTrue(note1.equals(note2));
		
		// reset and create all accidentals and notes
		Accidental.getAccidentals().clear();
		Accidental.createAllAccidentals();
		Note.getNotes().clear();
		Note.createAllNotes();
		
		// test unequal notes
		note1 = Note.getNote("C");
		note2 = Note.getNote("D");
		assertFalse(note1.equals(note2));
		
		note1 = Note.getNote("B");
		note2 = Note.getNote("Bb");
		assertFalse(note1.equals(note2));
		
		// test enharmonic notes
		note1 = Note.getNote("C#");
		note2 = Note.getNote("Db");
		assertFalse(note1.equals(note2), "Enharmonic notes should be considered different");
	}
}
