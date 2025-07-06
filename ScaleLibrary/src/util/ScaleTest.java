package util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScaleTest {
	
	Scale testScale1;
	Scale testScale2;

	@BeforeEach
	void setUp() throws Exception {
		// scale with non-simplified note names
		List<Interval> intervals1 = new ArrayList<>();
		intervals1.add(Interval.getInterval("1"));
		intervals1.add(Interval.getInterval("2"));
		intervals1.add(Interval.getInterval("b3"));
		intervals1.add(Interval.getInterval("bb7"));
		this.testScale1 = new Scale("test1", intervals1, false);
		
		
		// scale with simplified note names
		List<Interval> intervals2 = new ArrayList<>();
		intervals2.add(Interval.getInterval("1"));
		intervals2.add(Interval.getInterval("#4"));
		intervals2.add(Interval.getInterval("bb7"));
		this.testScale2 = new Scale("test2", intervals2, true);
	}

	@Test
	void testGetIntervalNames() {
		// test some valid intervals
		List<String> intervals = this.testScale1.getIntervalNames();
		assertEquals(4, intervals.size());
		assertEquals("1", intervals.get(0));
		assertEquals("2", intervals.get(1));
		assertEquals("b3", intervals.get(2));
		assertEquals("bb7", intervals.get(3));
	}

	@Test
	void testGetScale() {
		// test that scales are stored in the map
		assertTrue(this.testScale1.scales.isEmpty());
		List<Note> scale = this.testScale1.getScale(Note.getNote("C"));
		assertEquals(4, scale.size());
		assertSame(Note.getNote("C"), scale.get(0));
		assertSame(Note.getNote("D"), scale.get(1));
		assertSame(Note.getNote("Eb"), scale.get(2));
		assertSame(Note.getNote("Bbb"), scale.get(3));
		assertSame(scale, this.testScale1.scales.get("C"), "the scale object should be stored in the map");
		assertSame(scale, this.testScale1.getScale(Note.getNote("C")), "the same object should be returned when required again");
	}

	@Test
	void testGenerateScale() {
		// test a regular scale
		List<Note> scale = this.testScale1.generateScale(Note.getNote("C"));
		assertEquals(4, scale.size());
		assertSame(Note.getNote("C"), scale.get(0));
		assertSame(Note.getNote("D"), scale.get(1));
		assertSame(Note.getNote("Eb"), scale.get(2));
		assertSame(Note.getNote("Bbb"), scale.get(3));

		// test a simplified scale
		scale = this.testScale2.generateScale(Note.getNote("C#"));
		assertEquals(3, scale.size());
		assertSame(Note.getNote("C#"), scale.get(0));
		assertSame(Note.getNote("G"), scale.get(1), "Fx should have been simplified to G");
		assertSame(Note.getNote("A#"), scale.get(2), "Bb should have been simplified to A#");
		
		// test a scale that would require more than 2 sharps/flats
		assertNull(this.testScale1.generateScale(Note.getNote("Cb")));
	}

	@Test
	void testSimplifyAccidentalsListOfNoteNote() {
		// test simplifying as many notes as possible
		List<Note> notes = new ArrayList<>();
		notes.add(Note.getNote("Cx"));
		notes.add(Note.getNote("Ebb"));
		notes.add(Note.getNote("Fx"));
		notes.add(Note.getNote("Bb"));
		
		this.testScale1.simplifyAccidentals(notes, Note.getNote("Cx"));
		assertSame(Note.getNote("Cx"), notes.get(0), "root notes should not be simplified");
		assertSame(Note.getNote("D"), notes.get(1), "Ebb should have been simplified to D");
		assertSame(Note.getNote("G"), notes.get(2), "Fx should have been simplified to G");
		assertSame(Note.getNote("A#"), notes.get(3), "Bb should have been simplified to A#");
	}

	@Test
	void testSimplifyAccidentalsListOfNoteNoteInt() {
		// test simplifying a single note at a specified index
		List<Note> notes = new ArrayList<>();
		notes.add(Note.getNote("C"));
		notes.add(Note.getNote("Ebb"));
		notes.add(Note.getNote("Fx"));
		notes.add(Note.getNote("Bb"));
		
		this.testScale1.simplifyAccidentals(notes, Note.getNote("C"), 2);
		assertSame(Note.getNote("C"), notes.get(0), "root notes should not be simplified");
		assertSame(Note.getNote("Ebb"), notes.get(1), "only note at index 2 should have been changed");
		assertSame(Note.getNote("G"), notes.get(2), "Fx should have been simplified to G");
		assertSame(Note.getNote("Bb"), notes.get(3), "only note at index 2 should have been changed");
	}

	@Test
	void testCompareTo() {
		// test scale names are compared alphabetically
		assertTrue(this.testScale1.compareTo(this.testScale2) < 0);
		
		// test identical scale names
		Scale testScale3 = new Scale("test1", null, false);
		assertTrue(this.testScale1.compareTo(testScale3) == 0);
	}
}
