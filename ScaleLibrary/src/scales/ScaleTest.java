package scales;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notes.Accidental;
import notes.Interval;
import notes.Note;

class ScaleTest {
	
	// test scales
	Scale scale1; // a common basic scale
	Scale scale2; // a novel but valid scale

	@BeforeEach
	void setUp() throws Exception {
		// create all scale components
		Accidental.createAllAccidentals();
		Note.createAllNotes();
		Interval.createAllIntervals();
		
		// create a D major scale to test
		Note root = Note.getNote("D");
		String format = "scale";
		String type = "major";
		String[] intervals1 = {"1", "2", "3", "4", "5", "6", "7", "8"};
		this.scale1 = new Scale(root, format, type, intervals1);
		
		// create a novel scale to test
		root = Note.getNote("Bb");
	    type = "test";
	    String[] intervals2 = {"1", "#2", "b4", "#5", "6", "8"};
	    this.scale2 = new Scale(root, format, type, intervals2);
	}

	@AfterEach
	void tearDown() throws Exception {
		// reset each scale component
		Interval.getIntervals().clear();
		Accidental.getAccidentals().clear();
		Note.getNotes().clear();
	}

	@Test
	void testScale() {
		// test the D major scale was created correctly
		assertEquals("D", this.scale1.getRoot().toString());
		assertEquals("scale", this.scale1.getFormat());
		assertEquals("major", this.scale1.getType());
		
		// test some of the notes
		ArrayList<Note> notes = scale1.getNotes();
		assertEquals(Note.getNote("D"), notes.get(0));
		assertEquals(Note.getNote("F#"), notes.get(2));
		assertEquals(Note.getNote("C#"), notes.get(6));
		assertEquals(Note.getNote("D"), notes.get(7));
		
		// test the novel scale was created correctly
		assertEquals("Bb", this.scale2.getRoot().toString());
		assertEquals("scale", this.scale2.getFormat());
		assertEquals("test", this.scale2.getType());
		
		// test some of the notes
		notes = scale2.getNotes();
		assertEquals(Note.getNote("Bb"), notes.get(0));
		assertEquals(Note.getNote("C#"), notes.get(1));
		assertEquals(Note.getNote("Ebb"), notes.get(2));
		assertEquals(Note.getNote("F#"), notes.get(3));
	}

	@Test
	void testShuffleAccidentalsInt() {
		// test re-spelling notes that are already spelled optimally
		this.scale1.shuffleAccidentals(1);
		assertEquals(Note.getNote("E"), this.scale1.getNotes().get(1));
		
		this.scale1.shuffleAccidentals(2);
		assertEquals(Note.getNote("F#"), this.scale1.getNotes().get(2));
		
		// test simplifying one complex note
		this.scale2.shuffleAccidentals(2);
		assertEquals(Note.getNote("D"), this.scale2.getNotes().get(2), "The Ebb should have been simplified to a D");
		assertEquals(Note.getNote("C#"), this.scale2.getNotes().get(1), "The C# should not have been simplified yet");
		assertEquals(Note.getNote("F#"), this.scale2.getNotes().get(3), "The F# should not have been simplified yet");
	}

	@Test
	void testShuffleAccidentals() {
		// test simplifying notes in a scale that is already spelled correctly
		this.scale1.shuffleAccidentals();
		assertEquals(Note.getNote("E"), this.scale1.getNotes().get(1), "The E should not have been changed");
		assertEquals(Note.getNote("F#"), this.scale1.getNotes().get(2), "The F# should not have been changed");
		
		// test simplifying notes in a scale with multiple poorly-spelled notes
		this.scale2.shuffleAccidentals();
		assertEquals(Note.getNote("D"), this.scale2.getNotes().get(2), "The Ebb should have been simplified to a D");
		assertEquals(Note.getNote("Db"), this.scale2.getNotes().get(1), "The C# should have been changed to a Db");
		assertEquals(Note.getNote("Gb"), this.scale2.getNotes().get(3), "The F# should have been changed to a Gb");
		
		// test a short chromatic scale
		Note root = Note.getNote("C");
		String[] intervals = {"1", "b2", "2", "b3", "3"};
		Scale scale = new Scale(root, "scale", "chromatic", intervals);
		scale.shuffleAccidentals();
		assertEquals(Note.getNote("C#"), scale.getNotes().get(1), "The Db should have been changed to a C#");
		assertEquals(Note.getNote("D#"), scale.getNotes().get(3), "The Eb should have been changed to a D#");
		
		// test where one enharmonic option is invalid (null)
		root = Note.getNote("C#");
		String[] intervals2 = {"1", "2", "#3", "#4"};
		scale = new Scale(root, "test", "test", intervals2);
		scale.shuffleAccidentals();
		assertEquals(Note.getNote("F#"), scale.getNotes().get(2), "The Ex should have been changed to an F#");
	}

	@Test
	void testIsValid() {
		// test a major scale
		assertTrue(this.scale1.isValid());
		
		// test a novel valid scale
		assertTrue(this.scale2.isValid());
		
		// test an invalid scale with a null note
		Note root = Note.getNote("Ab");
		String[] intervals = {"1", "2", "bbb4", "5", "7", "8"};
		Scale scale = new Scale(root, "scale", "invalid", intervals);
		assertFalse(scale.isValid(), "The 3rd note should be null, making the scale invalid");
		
		// test an invalid scale with a note exceeding a two sharps
		root = Note.getNote("C#");
		String[] intervals2 = {"1", "2", "x4", "#5", "7", "8"};
		scale = new Scale(root, "scale", "invalid", intervals2);
		assertFalse(scale.isValid(), "The 3rd note should have a triple sharp, making the scale invalid");
	}

	@Test
	void testToString() {
		// test a major scale
		assertEquals("D major scale", this.scale1.toString());
		
		// test another scale
		assertEquals("Bb test scale", this.scale2.toString());
	}

}
