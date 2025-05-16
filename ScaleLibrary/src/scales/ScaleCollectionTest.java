package scales;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notes.Accidental;
import notes.Interval;
import notes.Note;

class ScaleCollectionTest {
	
	// test scale groups
	ScaleCollection scales1; // collection comprising a common basic scale type
	ScaleCollection scales2; // collection comprising a novel but valid scale type

	@BeforeEach
	void setUp() throws Exception {
		// create all scale components
		Accidental.createAllAccidentals();
		Note.createAllNotes();
		Interval.createAllIntervals();
		
		// create a collection of major scales to test
		String format = "scale";
		String type = "major";
		String[] intervals1 = {"1", "2", "3", "4", "5", "6", "7", "8"};
		boolean simplify = false;
		this.scales1 = new ScaleCollection(format, type, intervals1, simplify);

		// create a collection of non-standard scales to test
		type = "test";
		String[] intervals2 = {"1", "#2", "b4", "#5", "6", "8"};
		simplify = true;
		this.scales2 = new ScaleCollection(format, type, intervals2, simplify);
	}

	@AfterEach
	void tearDown() throws Exception {
		// reset each scale component
		Interval.getIntervals().clear();
		Accidental.getAccidentals().clear();
		Note.getNotes().clear();
	}

	@Test
	void testScaleCollection() {
		// test the major scale collection was created correctly
		assertEquals("scale", this.scales1.getFormat());
		assertEquals("major", this.scales1.getType());
		String[] intervals = {"1", "2", "3", "4", "5", "6", "7", "8"};
		assertArrayEquals(intervals, this.scales1.getIntervals());
		assertEquals(false, this.scales1.getSimplify());
		
		// test the novel scale collection was created correctly
		assertEquals("scale", this.scales2.getFormat());
		assertEquals("test", this.scales2.getType());
		String[] intervals2 = {"1", "#2", "b4", "#5", "6", "8"};
		assertArrayEquals(intervals2, this.scales2.getIntervals());
		assertEquals(true, this.scales2.getSimplify());
	}

	@Test
	void testCreateAllScales() {
		// test creating all major scales
		this.scales1.createAllScales();
		assertEquals(21, this.scales1.getAllScales().size());
		
		// test a scale from the major scale collection
		Scale scale = this.scales1.getAllScales().get("g");
		assertEquals(Note.getNote("G"), scale.getRoot());
		assertTrue(scale.isValid());
		assertEquals(8, scale.getNotes().size());
		
		// test creating all scales in the novel collection
		this.scales2.createAllScales();
		assertEquals(21, this.scales2.getAllScales().size());
		
		// test a scale from the novel scale collection
		scale = this.scales2.getAllScales().get("g");
		assertEquals(Note.getNote("G"), scale.getRoot());
		assertTrue(scale.isValid());
		assertEquals(6, scale.getNotes().size());
		assertEquals(Note.getNote("B"), scale.getNotes().get(2), "The Cb should have been simplified to a B");
		
		// test that the b5 from blues scales are changed correctly
		String[] intervals = {"1", "b3", "4", "b5", "5", "b7", "8"};
		ScaleCollection scales = new ScaleCollection("scale", "blues", intervals, false);
		scales.createAllScales();
		scale = scales.getAllScales().get("c");
		assertEquals(Note.getNote("F#"), scale.getNotes().get(3), "The Gb should have been changed to an F#");
		
		// test where some scales in the collection are invalid
		String[] intervals2 = {"1", "2", "bb4", "5", "7", "8"};
		scales = new ScaleCollection("scale", "invalid", intervals2, false);
		scales.createAllScales();
		assertEquals(13, scales.getAllScales().size(), "Invalid scales should not be included in the final collection");
	}

	@Test
	void testToFileLine() {
		// test the major scale collection
		assertEquals("scale; major; 1, 2, 3, 4, 5, 6, 7, 8; false", this.scales1.toFileLine());
		
		// test the novel scale collection
		assertEquals("scale; test; 1, #2, b4, #5, 6, 8; true", this.scales2.toFileLine());
	}

	@Test
	void testToString() {
		// test the major scale collection
		assertEquals("major scale", this.scales1.toString());
		
		// test the novel scale collection
		assertEquals("test scale", this.scales2.toString());
	}

}
