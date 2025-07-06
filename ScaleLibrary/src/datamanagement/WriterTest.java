package datamanagement;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.Interval;
import util.Scale;

class WriterTest {
	
	Writer writer;
	
	Scale testScale1;
	Scale testScale2;

	@BeforeEach
	void setUp() throws Exception {
		this.writer = new Writer("test3.txt");
		
		List<Interval> intervals1 = new ArrayList<>();
		intervals1.add(Interval.getInterval("1"));
		intervals1.add(Interval.getInterval("b3"));
		intervals1.add(Interval.getInterval("#4"));
		this.testScale1 = new Scale("testScale1", intervals1, true);
		
		List<Interval> intervals2 = new ArrayList<>();
		intervals2.add(Interval.getInterval("4"));
		this.testScale2 = new Scale("testScale2", intervals2, false);
	}

	@Test
	void testWriteScales() {
		// test a list of scales
		List<Scale> scales = new ArrayList<>();
		scales.add(testScale1);
		scales.add(testScale2);
		this.writer.writeScales(scales);

		// check the correct lines were written
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
		assertEquals("testScale1; 1, b3, #4; true", lines.get(0));
		assertEquals("testScale2; 4; false", lines.get(1));

		// test an empty list of scales
		scales.clear();
		this.writer.writeScales(scales);

		// check the correct lines were written
		lines.clear();
		br = null;
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
	void testToFileLine() {
		// test a standard scale
		assertEquals("testScale1; 1, b3, #4; true", writer.toFileLine(this.testScale1));
		
		// test a scale with only one interval
		assertEquals("testScale2; 4; false", writer.toFileLine(this.testScale2));
	}
}
