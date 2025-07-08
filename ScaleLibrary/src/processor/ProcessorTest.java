package processor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datamanagement.Reader;
import datamanagement.Writer;
import util.Interval;
import util.Note;
import util.Scale;

class ProcessorTest {
	
	Processor processor;

	@BeforeEach
	void setUp() throws Exception {
		Reader reader = new Reader("test1.txt", "test2.txt");
		Writer writer = new Writer("test3.txt");
		this.processor = new Processor(reader, writer);
	}

	@Test
	void testAddCustomScale() {
		// test adding a custom scale
		List<Scale> customScales = this.processor.scaleSets.get("custom");
		assertTrue(customScales.isEmpty(), "custom scale set should be empty at the start");
		List<Interval> intervals = new ArrayList<>();
		intervals.add(Interval.getInterval("1"));
		intervals.add(Interval.getInterval("b3"));
		
		this.processor.addCustomScale("test1", intervals, false);
		assertEquals(1, customScales.size(), "new scale should have been added to custom set");
		
		// check the scale was written to file
		List<String> lines = new ArrayList<>();
		BufferedReader br = null;
		try {	
			br = new BufferedReader(new FileReader(new File("test3.txt")));
			String line = "";
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			fail();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				fail();
			}
		}

		assertEquals(1, lines.size(), "incorrect number of scales written");
		assertEquals("test1; 1, b3; false", lines.get(0), "scale data written incorrectly");
	}

	@Test
	void testDeleteCustomScale() {
		// create some custom scales to remove
		List<Interval> intervals1 = new ArrayList<>();
		intervals1.add(Interval.getInterval("1"));
		intervals1.add(Interval.getInterval("b3"));
		
		List<Interval> intervals2 = new ArrayList<>();
		intervals2.add(Interval.getInterval("1"));
		intervals2.add(Interval.getInterval("3"));
		
		List<Scale> customScales = this.processor.scaleSets.get("custom");
		customScales.add(new Scale("test1", intervals1, true));
		customScales.add(new Scale("test2", intervals2, true));
		
		// test removing each scale
		assertEquals(2, customScales.size());
		this.processor.deleteCustomScale(0);
		assertEquals(1, customScales.size(), "first custom scale should have been removed from the scale list");
		assertEquals("test2", customScales.get(0).getName(), "second custom scale should still be in list");
		
		this.processor.deleteCustomScale(0);
		assertEquals(0, customScales.size(), "second custom scale should have been removed from the scale list");
		
		// check the custom scale file is now empty
		List<String> lines = new ArrayList<>();
		BufferedReader br = null;
		try {	
			br = new BufferedReader(new FileReader(new File("test3.txt")));
			String line = "";
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			fail();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				fail();
			}
		}

		assertEquals(0, lines.size(), "no scales should be written for an empty scale list");
	}

	@Test
	void testGetScaleNames() {
		// test some valid scale names
		List<Scale> customScales = this.processor.scaleSets.get("custom");
		customScales.add(new Scale("test1", null, true));
		customScales.add(new Scale("test2", null, true));
		
		List<String> scaleNames = this.processor.getScaleNames("custom");
		assertEquals(2, scaleNames.size(), "incorrect number of scale names");
		assertEquals("test1", scaleNames.get(0), "first scale name incorrect");
		assertEquals("test2", scaleNames.get(1), "second scale name incorrect");
		
		// test an empty scale list
		customScales.clear();
		assertTrue(this.processor.getScaleNames("custom").isEmpty(), "no scale names should be returned when there are none");
	}

	@Test
	void testGetScaleNotes() {
		// test a valid scale
		List<Scale> customScales = this.processor.scaleSets.get("custom");
		List<Interval> intervals = new ArrayList<>();
		intervals.add(Interval.getInterval("1"));
		intervals.add(Interval.getInterval("b3"));
		intervals.add(Interval.getInterval("bb7"));
		customScales.add(new Scale("test1", intervals, false));
		
		List<String> notes = this.processor.getScaleNotes("custom", 0, Note.getNote("C"));
		assertEquals(3, notes.size(), "incorrect number of note names");
		assertEquals("C", notes.get(0), "natural note name incorrect");
		assertEquals("Eb", notes.get(1), "flattened note name incorrect");
		assertEquals("Bbb", notes.get(2), "doubly flattened note name incorrect");
		
		// test an invalid scale
		assertNull(this.processor.getScaleNotes("custom", 0, Note.getNote("Cb")), "scales requiring more than 2 sharps/flats should be returned as null");
	}

	@Test
	void testWriteCustomScales() {
		// create some custom scales to write
		List<Interval> intervals1 = new ArrayList<>();
		intervals1.add(Interval.getInterval("1"));
		intervals1.add(Interval.getInterval("b3"));
		Scale testScale1 = new Scale("test1", intervals1, false);
		
		List<Interval> intervals2 = new ArrayList<>();
		intervals2.add(Interval.getInterval("1"));
		intervals2.add(Interval.getInterval("3"));
		Scale testScale2 = new Scale("test2", intervals2, true);
		
		// test writing the custom scales to file
		List<Scale> customScales = this.processor.scaleSets.get("custom");
		customScales.add(testScale1);
		customScales.add(testScale2);
		this.processor.writeCustomScales();

		// check the scales were written to file
		List<String> lines = new ArrayList<>();
		BufferedReader br = null;
		try {	
			br = new BufferedReader(new FileReader(new File("test3.txt")));
			String line = "";
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			fail();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				fail();
			}
		}

		assertEquals(2, lines.size(), "incorrect number of scales written");
		assertEquals("test1; 1, b3; false", lines.get(0), "first scale written incorrectly");
		assertEquals("test2; 1, 3; true", lines.get(1), "second scale written incorrectly");
	}

	@Test
	void testGetScale() {
		// test some valid scales
		List<Scale> customScales = this.processor.scaleSets.get("custom");
		customScales.add(new Scale("test1", null, false));
		customScales.add(new Scale("test2", null, false));
		customScales.add(new Scale("test3", null, false));
		
		assertEquals("test1", this.processor.getScale("custom", 0).getName(), "first scale incorrectly returned");
		assertEquals("test2", this.processor.getScale("custom", 1).getName(), "middle scale incorrectly returned");
		assertEquals("test3", this.processor.getScale("custom", 2).getName(), "last scale incorrectly returned");
	}

	@Test
	void testGetIntervalList() {
		// test a valid scale
		List<Scale> customScales = this.processor.scaleSets.get("custom");
		List<Interval> intervals = new ArrayList<>();
		intervals.add(Interval.getInterval("1"));
		intervals.add(Interval.getInterval("b3"));
		intervals.add(Interval.getInterval("bb7"));
		customScales.add(new Scale("test1", intervals, false));

		List<String> intervalNames = this.processor.getIntervalList("custom", 0);
		assertEquals(3, intervalNames.size(), "incorrect number of intervals");
		assertEquals("1", intervalNames.get(0), "unaltered interval name incorrect");
		assertEquals("b3", intervalNames.get(1), "lowered interval name incorrect");
		assertEquals("bb7", intervalNames.get(2), "doubly lowered interval name incorrect");
	}
}
