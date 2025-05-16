package files;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notes.*;
import scales.*;

class FileLoaderTest {

	@BeforeEach
	void setUp() throws Exception {
		// create all scale components
		Accidental.createAllAccidentals();
		Note.createAllNotes();
		Interval.createAllIntervals();
	}
	
	@AfterEach
	void tearDown() throws Exception {
		// reset each scale component
		Interval.getIntervals().clear();
		Accidental.getAccidentals().clear();
		Note.getNotes().clear();
	}

	@Test
	void testLoadFileData() {
		// test the scales.txt file
		ArrayList<ScaleCollection> allScales = FileLoader.loadFileData("scales.txt");
		assertEquals(12, allScales.size());
		
		// test the first scale set in the list
		ScaleCollection scales = allScales.get(0);
		assertEquals("scale", scales.getFormat());
		assertEquals("major", scales.getType());
		String[] intervals = {"1", "2", "3", "4", "5", "6", "7", "8"};
		assertArrayEquals(intervals, scales.getIntervals());
		assertEquals(false, scales.getSimplify());
		assertEquals(21, scales.getAllScales().size());
		
		// test the modes.txt file
		allScales = FileLoader.loadFileData("modes.txt");
		assertEquals(7, allScales.size());

		// test the last scale set in the list
		scales = allScales.get(allScales.size() - 1);
		assertEquals("mode", scales.getFormat());
		assertEquals("locrian", scales.getType());
		String[] intervals2 = {"1", "b2", "b3", "4", "b5", "b6", "b7", "8"};
		assertArrayEquals(intervals2, scales.getIntervals());
		assertEquals(false, scales.getSimplify());
		assertEquals(21, scales.getAllScales().size());
	}
}
